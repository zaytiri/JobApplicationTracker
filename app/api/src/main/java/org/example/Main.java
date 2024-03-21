package org.example;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) {
        final ResourceConfig rc = new ResourceConfig().packages("org.example");
        final URI uri = URI.create(BASE_URI);
        final org.glassfish.grizzly.http.server.HttpServer server =
                GrizzlyHttpServerFactory.createHttpServer(uri, rc);
        System.out.println("Server started at " + BASE_URI);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.shutdownNow();
        }
    }
}