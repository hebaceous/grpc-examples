package me.hebaceous.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import me.hebaceous.grpc.UserProto.User;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@GRpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase implements InitializingBean {

    private final ConcurrentMap<Integer, User> usersMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        User user1 = User.newBuilder().setId(5).setName("木木").build();
        User user2 = User.newBuilder().setId(7).setName("乐乐").build();
        usersMap.put(user1.getId(), user1);
        usersMap.put(user2.getId(), user2);
    }

    @Override
    public void all(Empty request, StreamObserver<User> responseObserver) {
        usersMap.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

}
