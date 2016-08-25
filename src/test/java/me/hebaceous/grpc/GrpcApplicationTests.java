package me.hebaceous.grpc;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import me.hebaceous.grpc.UserServiceGrpc.UserServiceBlockingStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrpcApplicationTests implements InitializingBean {

    @Value("${grpc.port}")
    private int grpcPort;

    private UserServiceBlockingStub userServiceBlockingStub;

    @Override
    public void afterPropertiesSet() throws Exception {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", grpcPort).usePlaintext(true).build();
        userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel);
    }

    @Test
    public void testAll() {
        userServiceBlockingStub.all(Empty.getDefaultInstance())
                .forEachRemaining(user -> System.out.println(user.getId() + ":"+ user.getName()));
    }
}
