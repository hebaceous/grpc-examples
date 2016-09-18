package me.hebaceous.grpc;

import io.grpc.*;
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GRpcGlobalInterceptor
public class GrpcInterceptor implements ServerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(GrpcInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        LOGGER.info("received headers:{}", headers);
        return new SimpleForwardingServerCallListener<ReqT>(next.startCall(call, headers)) {
            @Override
            public void onMessage(ReqT message) {
                super.onMessage(message);
            }
        };
    }

}
