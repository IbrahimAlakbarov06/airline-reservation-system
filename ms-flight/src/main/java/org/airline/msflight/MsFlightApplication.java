package org.airline.msflight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsFlightApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsFlightApplication.class, args);
    }

}
