package me.hebaceous.grpc.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

//@GRpcGlobalInterceptor
public class JWTServerInterceptor implements ServerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(JWTServerInterceptor.class);

    private static final ServerCall.Listener NOOP_LISTENER = new ServerCall.Listener() {
    };

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        String jwtToken = headers.get(Constants.JWT_METADATA_KEY);
        if (StringUtils.isEmpty(jwtToken)) {
            call.close(Status.UNAUTHENTICATED.withDescription("jwt Token is missing from headers"), headers);
            return NOOP_LISTENER;
        }
        Context context = Context.current();
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Constants.JWT_SECRET)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
            Integer uid = decodedJWT.getClaim(Constants.UID).asInt();
            // TODO check uid
            context.withValue(Constants.UID_CTX_KEY, uid.toString());
        } catch (Exception e) {
            call.close(Status.UNAUTHENTICATED.withDescription("Verification failed - Unauthenticated!"), headers);
            return NOOP_LISTENER;
        }
        return Contexts.interceptCall(context, call, headers, next);
    }

}
