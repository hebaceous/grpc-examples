package me.hebaceous.grpc;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import me.hebaceous.grpc.UserServiceGrpc.UserServiceBlockingStub;
import me.hebaceous.grpc.UserServiceGrpc.UserServiceStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrpcApplicationTests {

    private final static Logger LOGGER = LoggerFactory.getLogger(GrpcApplicationTests.class);

    @Value("${grpc.port}")
    private int grpcPort;

    private ManagedChannel managedChannel;
    private UserServiceBlockingStub userServiceBlockingStub;
    private UserServiceStub userServiceStub;

    @Before
    public void before() {
        managedChannel = ManagedChannelBuilder.forAddress("localhost", grpcPort).usePlaintext(true).build();
        userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel);
        userServiceStub = UserServiceGrpc.newStub(managedChannel);
    }

    @After
    public void after() {
        managedChannel.shutdown();
    }

    @Test
    public void testAllBlock() {
        userServiceBlockingStub.all(Empty.getDefaultInstance())
                .forEachRemaining(user -> System.out.println(user.getId() + ":"+ user.getName()));
    }

    @Test
    public void testAllAsync() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        userServiceStub.all(Empty.getDefaultInstance(), new ClientResponseObserver<Empty, UserProto.User>() {
            @Override
            public void onNext(UserProto.User value) {
                LOGGER.info("onNext:{}", value);
            }
            @Override
            public void onError(Throwable t) {
                LOGGER.info("onError", t);
            }
            @Override
            public void onCompleted() {
                LOGGER.info("onCompleted");
                countDownLatch.countDown();
            }
            @Override
            public void beforeStart(ClientCallStreamObserver<Empty> requestStream) {
                LOGGER.info("beforeStart:{}", requestStream);
            }
        });
        countDownLatch.await();
    }
}
