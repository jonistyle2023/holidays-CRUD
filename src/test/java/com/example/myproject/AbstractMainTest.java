package com.example.myproject;


import io.helidon.config.Config;
import io.helidon.http.Status;
import io.helidon.webclient.api.ClientResponseTyped;
import io.helidon.webclient.http1.Http1Client;
import io.helidon.webclient.http1.Http1ClientResponse;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.testing.junit5.SetUpRoute;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.CoreMatchers.containsString;

abstract class AbstractMainTest {
    private final Http1Client client;

    protected AbstractMainTest(Http1Client client) {
        this.client = client;
    }

    @SetUpRoute
    static void routing(HttpRouting.Builder builder) {
        Config config = Config.create();
        Main.configureAppRoutes(builder);
    }

    
    @Test
    void testGreet() {
        ClientResponseTyped<Message> response = client.get("/greet").request(Message.class);
        assertThat(response.status(), is(Status.OK_200));
        assertThat(response.entity().getMessage(), is("Hello World!"));
    }

    @Test
    void testGreetJoe() {
        ClientResponseTyped<Message> response = client.get("/greet/Joe").request(Message.class);
        assertThat(response.status(), is(Status.OK_200));
        assertThat(response.entity().getMessage(), is("Hello Joe!"));
    }

    
    @Test
    void testMetricsObserver() {
        try (Http1ClientResponse response = client.get("/observe/metrics").request()) {
            assertThat(response.status(), is(Status.OK_200));
        }
    }

    
    @Test
    void testSimpleGreet() {
        ClientResponseTyped<String> response = client.get("/simple-greet").request(String.class);
        assertThat(response.status(), is(Status.OK_200));
        assertThat(response.entity(), is("Hello World!"));
    }

}
