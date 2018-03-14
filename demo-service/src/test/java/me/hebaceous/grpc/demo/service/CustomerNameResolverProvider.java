package me.hebaceous.grpc.demo.service;

import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.Objects;

/**
 * Created by hebaceous on 2017/3/31.
 *
 * @author hebaceous
 */
public class CustomerNameResolverProvider extends NameResolverProvider {

    private static final String SCHEME = "hebaceous";

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 10;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
        if (!Objects.equals(SCHEME, targetUri.getScheme()))
            return null;
        return new CustomerNameResolver(targetUri);
    }

    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }
}
