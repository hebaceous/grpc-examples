package me.hebaceous.grpc;

import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.ResolvedServerInfo;
import io.grpc.ResolvedServerInfoGroup;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
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
        List<ResolvedServerInfoGroup> servers = Arrays.stream(uri.getAuthority()
                .split(";"))
                .map(ipPortPair -> {
                    String[] ipPort = ipPortPair.split(",");
                    String ip = ipPort[0];
                    int port = Integer.parseInt(ipPort[1]);
                    return ResolvedServerInfoGroup
                            .builder()
                            .add(new ResolvedServerInfo(new InetSocketAddress(ip, port)))
                            .build();
                }).collect(Collectors.toList());
        listener.onUpdate(servers, Attributes.EMPTY);
    }

    @Override
    public void shutdown() {

    }
}
