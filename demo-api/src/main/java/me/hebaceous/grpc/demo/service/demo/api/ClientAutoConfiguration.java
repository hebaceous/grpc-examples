package me.hebaceous.grpc.demo.service.demo.api;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(LoadBalancerClient.class)
@ConditionalOnProperty(prefix = "spring.cloud.consul.discovery", name = "register", havingValue = "false")
@AutoConfigureAfter(RibbonAutoConfiguration.class)
public class ClientAutoConfiguration {

    @Bean
    ServiceHelper serviceHelper(LoadBalancerClient loadBalancerClient) {
        return new ServiceHelper(loadBalancerClient);
    }

}
