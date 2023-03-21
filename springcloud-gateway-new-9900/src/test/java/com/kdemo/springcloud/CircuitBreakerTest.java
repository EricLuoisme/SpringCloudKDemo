package com.kdemo.springcloud;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Roylic
 * 2023/3/21
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
public class CircuitBreakerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @RepeatedTest(50)
    public void circuitBreakerTest() {
        webClient.post()
                .uri("/router/department/add")
                .accept(MediaType.ALL)
                .exchange()
                .expectBody(String.class)
                .consumeWith(result -> System.out.println(result.getResponseBody()));
    }

}
