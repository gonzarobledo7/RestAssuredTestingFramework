package APITests;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;



public class authenticationTest {


    @Test
    public void testBasicAuthEndPoint(){
        given()
                .header("Authorization", "Basic cG9zdG1hbjpwYXNzd29yZA==")
                .when()
                .get("http://echo.getpostman.com//basic-auth")
                .then()
                .statusCode(200);
    }
    
}
