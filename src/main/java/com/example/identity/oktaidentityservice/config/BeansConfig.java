package com.example.identity.oktaidentityservice.config;

import com.okta.commons.http.config.Proxy;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.AuthorizationMode;
import com.okta.sdk.client.ClientBuilder;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.api.UserApi;
import com.okta.sdk.resource.client.ApiClient;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

@Generated
@Configuration
public class BeansConfig {

    //@Value("${identity.api.proxy-proxyRequired:false}")
    @Value("${identity.api.proxy-proxyRequired:false}")
    private boolean proxyRequired;

    @Value("${identity.api.private-key:}")
    Resource privateKeyFiles;

    @Bean
    @ConditionalOnProperty(name = "identity.api.token")
    public ApiClient clientWithToken(@Value("${identity.api.url}") String orgUrl,
                                     @Value("${identity.api.token}") String authToken) {

        return clientBuilder(orgUrl)
                .setClientCredentials(new TokenClientCredentials(authToken))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "identity.api.client-id")
    public ApiClient oAuthClient(
            @Value("${identity.api.url}") String orgUrl,
            @Value("${identity.api.client-id}") String clientId,
            @Value("${identity.api.key-id}") String keyId) throws IOException {

        var builder = clientBuilder(orgUrl);

        if (proxyRequired) {
            builder.setProxy(new Proxy("localhost", 9000));
        }

        return builder.setAuthorizationMode(AuthorizationMode.PRIVATE_KEY)
                .setKid(keyId)
                .setClientId(clientId)
                .setPrivateKey(privateKeyFiles.getInputStream())
                .setScopes(new HashSet<>(Arrays.asList("okta.users.read", "okta.users.manage")))
                .build();
    }

    private ClientBuilder clientBuilder(String orgUrl) {
        return Clients.builder().setOrgUrl(orgUrl);
    }

    @Bean
    public UserApi userApi(ApiClient apiClient) {
        return new UserApi(apiClient);
    }

//    @Bean
//    @Primary
//    public ApiClient dummyOktaApiClient() {
//        return new ApiClient();
//    }
}
