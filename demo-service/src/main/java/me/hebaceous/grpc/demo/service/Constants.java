package me.hebaceous.grpc.demo.service;

import io.grpc.Context;
import io.grpc.Metadata;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public interface Constants {

    String JWT_SECRET = "hebaceous";

    String UID = "uid";

    Context.Key<String> UID_CTX_KEY = Context.key(UID);

    Metadata.Key<String> JWT_METADATA_KEY = Metadata.Key.of("jwt", ASCII_STRING_MARSHALLER);

}
