package me.hebaceous.grpc.demo.service;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hebaceous on 2017/3/31.
 *
 * @author hebaceous
 */
public class CustomerNameResolver extends NameResolver {

    private final URI uri;

    public CustomerNameResolver(URI uri) {
        this.uri = uri;
    }

    @Override
    public String getServiceAuthority() {
        return uri.getAuthority();
    }

    @Override
    public void start(Listener listener) {
        List<EquivalentAddressGroup> servers = Arrays.stream(uri.getAuthority()
                .split(";"))
                .map(ipPortPair -> {
                    String[] ipPort = ipPortPair.split(",");
                    String ip = ipPort[0];
                    int port = Integer.parseInt(ipPort[1]);
                    return new EquivalentAddressGroup(Collections.singletonList(new InetSocketAddress(ip, port)));
                }).collect(Collectors.toList());
        listener.onAddresses(servers, Attributes.EMPTY);
    }

    @Override
    public void shutdown() {

    }
}
