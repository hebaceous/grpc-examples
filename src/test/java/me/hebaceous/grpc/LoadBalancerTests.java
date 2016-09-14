package me.hebaceous.grpc;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.DnsNameResolverProvider;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import me.hebaceous.grpc.UserServiceGrpc.UserServiceBlockingStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static me.hebaceous.grpc.UserServiceGrpc.newBlockingStub;

public class LoadBalancerTests {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoadBalancerTests.class);

    private ManagedChannel managedChannel;
    private UserServiceBlockingStub userServiceBlockingStub;

    @Before
    public void before() {
        managedChannel = ManagedChannelBuilder.forTarget("dns:///grpc.hebaceous.me:6565")
                .usePlaintext(true)
                .nameResolverFactory(DnsNameResolverProvider.asFactory())
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
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        IntStream.range(0, 1000).forEach(index -> {
            new Thread(() -> {
                userServiceBlockingStub.all(Empty.getDefaultInstance()).forEachRemaining(System.out::println);
                countDownLatch.countDown();
            }).start();
        });
        countDownLatch.await();
    }
}
