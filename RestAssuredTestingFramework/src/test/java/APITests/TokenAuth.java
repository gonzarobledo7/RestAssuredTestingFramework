package APITests;
 
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
 
import java.util.Random;
 
public class TokenAuth {
 
    private String token;
 
    @BeforeClass(groups = { "API Books" })
    public void setup() {
        // Generar un correo electrónico aleatorio
        String randomEmail = generateRandomEmail();
        System.out.println("Email generado: " + randomEmail);
        RestAssured.baseURI = "https://simple-books-api.glitch.me";
        Response response = given()
                .contentType("application/json")
                .body("{\"clientName\": \"Postman\", \"clientEmail\": \"" + randomEmail + "\"}")
                .when()
                .post("/api-clients/")
                .then()
                .statusCode(201)
                .extract()
                .response();
 
        token = response.jsonPath().getString("accessToken");
    }
 
    @Test(priority = 2,      groups = { "API Books", "API Regression" })
    public void testTokenBooks() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/orders")
                .then()
                .log().all()
                .statusCode(200);
 
    }
 
    @Test
    public void getAllBooks() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/books")
                .then()
                .log().all();
    }
 
    private String generateRandomEmail() {
        Random random = new Random();
        int randomNumber = random.nextInt(10000); // Generar un número aleatorio entre 0 y 9999
        return "user" + randomNumber + "@example.com";
    }
}