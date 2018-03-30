package me.hebaceous.grpc.demo.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import me.hebaceous.grpc.UserProto;
import me.hebaceous.grpc.UserServiceGrpc.UserServiceBlockingStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static me.hebaceous.grpc.UserServiceGrpc.newBlockingStub;

public class GrpcNginxTests {

    private ManagedChannel managedChannel;
    private UserServiceBlockingStub userServiceBlockingStub;

    @Before
    public void before() {
        managedChannel = ManagedChannelBuilder.forAddress("localhost", 16565)
                .usePlaintext(true)
                .build();
        userServiceBlockingStub = newBlockingStub(managedChannel);
    }

    @After
    public void after() {
        managedChannel.shutdown();
    }

    @Test
    public void testAllBlock() {
        userServiceBlockingStub.all(Empty.getDefaultInstance())
                .forEachRemaining(user -> System.out.println(user.getId() + ":" + user.getName()));
    }

    @Test
    public void testFetchById() {
        Int32Value id = Int32Value.newBuilder().setValue(5).build();
        UserProto.User user = userServiceBlockingStub.fetchById(id);
        System.out.println(user.getId() + ":" + user.getName());
    }
}
