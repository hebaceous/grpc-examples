package me.hebaceous.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import me.hebaceous.grpc.UserServiceGrpc.UserServiceBlockingStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static me.hebaceous.grpc.UserServiceGrpc.newBlockingStub;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoadBalancerCustomerNameResolverTests {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoadBalancerCustomerNameResolverTests.class);

    private ManagedChannel managedChannel;
    private UserServiceBlockingStub userServiceBlockingStub;

    @Before
    public void before() {
        managedChannel = ManagedChannelBuilder.forTarget("hebaceous://localhost,6565;127.0.0.1,6565")
                .usePlaintext(true)
                .nameResolverFactory(new CustomerNameResolverProvider())
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .build();
        userServiceBlockingStub = newBlockingStub(managedChannel);
    }

    @After
    public void after() {
        managedChannel.shutdown();
    }

    @Test
    public void testAllBlock() throws InterruptedException {
        // 1
        userServiceBlockingStub.all(Empty.getDefaultInstance())
                .forEachRemaining(user ->
                        System.out.println(user.getId() + ":" + user.getName()));
        // 2
        UserProto.User user = userServiceBlockingStub.fetchById(Int32Value.newBuilder().setValue(5).build());
        System.out.println(user);
    }
}
