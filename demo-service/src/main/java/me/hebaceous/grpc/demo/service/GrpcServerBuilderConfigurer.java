package me.hebaceous.grpc.demo.service;

import io.grpc.ServerBuilder;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.stereotype.Component;

/**
 * Created by hebaceous on 2016/12/27.
 *
 * @author hebaceous
 */
@Component
public class GrpcServerBuilderConfigurer extends GRpcServerBuilderConfigurer {

    @Override
    public void configure(ServerBuilder<?> serverBuilder) {
    }

}
