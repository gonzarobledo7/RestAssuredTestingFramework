package APITests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class TestingPerformanceAPI {
    
    @Test
    public void validResponseTime(){
        RestAssured.baseURI = "https://simple-books-api.glitch.me";

        given()
                .header("Content-Type", "application/json")
                .when()
                .get("/books")
                .then()
                .assertThat()
                .statusCode(200)
                .time(lessThan(2900L)); // Validamos que la respuesta sea menor a 2.9 segundos.
    }
}
