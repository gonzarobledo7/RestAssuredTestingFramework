package APITests;
 
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
 
public class APITests {
 
    @Test(enabled = false)
    public void testRickAndMortyAPI() {
        //Get -> Siempre que hagamos un GET es para traernos informacion, lo que tenga ese endpoint
        with()
        .baseUri("https://rickandmortyapi.com/")
        .get()
        .then()
        .log().all();
    }

    @Test(enabled = true)
    public void createUser(){ //Lo principal que tiene un metodo de tipo POST es que tiene uqe tener un cuerpo
        String body = "{\n" +
                "    \"nombre\": \"GonzalitoxD\",\n" +
                "    \"chamba\": \"RestAssuredCUrso\"\n" +
                "}";
        given()
                .baseUri("https://reqres.in/api")
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201)  //Recurso creado
                .log().all();
    }

    @Test(enabled = false)
    public void updateUser(){ //El metodo PUT se trata de actualizar un recurso ya existente dentro de nuestra BD
        String body = "{\n" +
                "    \"name\": \"Gonzalito\",\n" +
                "    \"job\": \"Testing Free Rango\"\n" +
                "}";
        given()
                .baseUri("https://reqres.in/api")
                .contentType(ContentType.JSON)
                .body(body)              //Va a requerir tambien un BODY.
                .when()
                .put("users/431")   //Vamos a necesitas un ID o identificar para seleccionar lo que queremos actualizar
                .then()
                .statusCode(200)    //El status code cambia, recibimos un 200
                .log().all();           
                              
    }

    @Test(enabled = false)
    public void deleteUser(){
        given()
                .baseUri("https://reqres.in/api")
                .when()                             //Es un metodo parecido al GET
                .delete("/users/431")           //Borramos por completo un recurso
                .then()
                .statusCode(204)    //
                .log().all();

    }

    @Test(enabled = false)
    public void validateFieldInJSON(){
        Response response = given()
        .when()
        .get("https://jsonplaceholder.typicode.com/users/1")
        .then()
        .statusCode(200)
        .extract().
        response();
        
        //Extraemos los datos de la respuesta JSON
        System.out.println(response.asString());

        //Extraer los datos de la respuesta JSON
        String name = response.jsonPath().getString("name");
        String username = response.jsonPath().getString("username");
        String email = response.jsonPath().getString("email");
        String lat = response.jsonPath().getString("addres.geo.lat");

        //Imprimimos datos extraidos
        System.out.println("Name: " + name);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Latitude: " + lat);


        //Validamos los datos extraidos
        response.then().body("name", equalTo("Leanne Graham"));
        response.then().body("username", equalTo("Bret"));
        response.then().body("email", equalTo("Sincere@april.biz"));
        response.then().body("address.geo.lat", equalTo("-37.3159"));


    }
}

