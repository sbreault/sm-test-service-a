package org.acme.getting.started;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        
        given()
          .when().get("/service-a")
          .then()
             .statusCode(200)
             .body(is("Why did the chicken cross the road?")); 
    }

}