package me.hebaceous.grpc.demo.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import io.grpc.stub.StreamObserver;
import me.hebaceous.grpc.UserProto;
import me.hebaceous.grpc.UserServiceGrpc;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@GRpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase implements InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final ConcurrentMap<Integer, UserProto.User> usersMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        UserProto.User user1 = UserProto.User.newBuilder().setId(5).setName("木木").build();
        UserProto.User user2 = UserProto.User.newBuilder().setId(7).setName("乐乐").build();
        usersMap.put(user1.getId(), user1);
        usersMap.put(user2.getId(), user2);
    }

    @Override
    public void all(Empty request, StreamObserver<UserProto.User> responseObserver) {
        LOGGER.info("-------all-------");
        usersMap.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void fetchById(Int32Value request, StreamObserver<UserProto.User> responseObserver) {
        LOGGER.info("-------fetchById(" + request.getValue() + ")-------");
        UserProto.User user = usersMap.get(request.getValue());
        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }
}
