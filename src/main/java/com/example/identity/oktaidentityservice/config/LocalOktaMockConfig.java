package com.example.identity.oktaidentityservice.config;

import com.okta.sdk.resource.client.ApiClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local") // Only active when spring.profiles.active=local
public class LocalOktaMockConfig {

//    @Bean
//    @Primary
//    public ApiClient mockOktaApiClient() {
//        return Mockito.mock(ApiClient.class);
//    }

    //overriding oAuthClient() in BeansConfig to by pass okta oauth failure as I dont have real private key
    //comment out this method to see real issue/error due to private key
    @Bean(name = "oAuthClient")
    @Primary
    public ApiClient oAuthClient() {
        return Mockito.mock(ApiClient.class);
    }
}
