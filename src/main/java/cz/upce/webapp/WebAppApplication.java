package cz.upce.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Martin Volenec / st46661
 *
 * This web app uses feign cleints to communicate with REST APIs.
 * Registered as eureka discovery client.
 * Also enabled circuit breaker.
 */

@SpringBootApplication
// @EnableDiscoveryClient
@EnableFeignClients
public class WebAppApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(WebAppApplication.class, args);
    }
}
