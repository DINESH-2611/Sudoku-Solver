package com.example;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")  // Base URL path for your API
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        // Register resources and Swagger OpenAPI support
        packages("com.example.resource");  // Package with your resources
        register(OpenApiResource.class);  // Register OpenAPI resource to serve /openapi.json
    }
}