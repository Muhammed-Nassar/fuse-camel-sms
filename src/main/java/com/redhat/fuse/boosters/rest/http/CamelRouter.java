package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redhat.fuse.boosters.rest.http.pojos.*;

/**
 * A simple Camel REST DSL route that implements the greetings service.
 * 
 */
@Component
public class CamelRouter extends RouteBuilder {

	@Value("${sms.service.url}")
	String SMS_SERVICE_URL;
    @Override
    public void configure() throws Exception {

        // @formatter:off
        restConfiguration()
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Greeting REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "sms-service/")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);
        
        rest("/send").description("SMS Service")
            .get("/{name}").outType(Greetings.class)
                .route().routeId("sms-api")
                .to("direct:sendSMS");        
        
        from("direct:sendSMS").description("SendTransfer REST service implementation route")
        .streamCaching()
        .log("Sending SMS")
        .setBody(simple("SMS sent"));
      
        
        // @formatter:on Credit, Rev, Block/Unblock
    }

}
