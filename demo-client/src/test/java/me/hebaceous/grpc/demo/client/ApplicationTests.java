package me.hebaceous.grpc.demo.client;

import com.google.common.collect.Lists;
import com.google.protobuf.Empty;
import me.hebaceous.grpc.UserProto;
import me.hebaceous.grpc.demo.service.demo.api.ServiceHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private ServiceHelper serviceHelper;

    @Test
    public void testDiscoveryClient() {
        while (true) {
            try {
                String servers = discoveryClient.getInstances("demo-service")
                        .stream()
                        .map(serviceInstance -> serviceInstance.getHost() + ":" + serviceInstance.getPort())
                        .collect(Collectors.joining(","));
                System.out.println(servers);
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testLoadBalancerClient() {
        while (true) {
            try {
                ServiceInstance serviceInstance = loadBalancerClient.choose("demo-service");
                System.out.println(serviceInstance.getHost() + ":" + serviceInstance.getPort());
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testServiceHelper() {
        while (true) {
            try {
                Iterator<UserProto.User> userIterator = serviceHelper.userServiceBlockingStub("demo-service")
                        .all(Empty.getDefaultInstance());
                String users = Lists.newArrayList(userIterator)
                        .stream()
                        .map(user -> user.getId() + ":" + user.getName())
                        .collect(Collectors.joining(","));
                System.out.println(users);
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
