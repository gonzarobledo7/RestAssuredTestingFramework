package APITests;
 
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Lead;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
 
import APITests.Utils.OrderResponse;
 
import static io.qameta.allure.SeverityLevel.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
 
import java.util.Random;
 
public class BooksAPITests {
 
        private String token;
 
        @BeforeClass
        @Test(groups = { "API Books", "API Regression" })
        @Epic("BackEnd")
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
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(CRITICAL)
        @Description("Validación del esquema del endpoint books, cubriendo estructura y tipos")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Schema") })
        @Epic("BackEnd")
        @Feature("Contrato del Endpoint")
        @Story("Creación de la estructura de datos")
        @Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
        public void validateBooksSchema() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName("El esquema del endpoint /books es el correcto");
                });
 
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .get("/books")
                                .then()
                                .assertThat()
                                .statusCode(200)
                                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/books-schema.json"));
        }
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(BLOCKER)
        @Description("Una consulta del tipo GET al endpoint /Books devuelve un status code 200")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Test Positivo") })
        @Epic("BackEnd")
        @Feature("Obtener libros mediante la API")
        @Story("GET para Books")
        @Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
        public void validarStatusCode200() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName(
                                        "Valida que al enviar un GET con un token válido al endpoint /books obtenemos un código de estado 200");
                });
 
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .get("/books")
                                .then()
                                .assertThat()
                                .statusCode(200);
        }
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(NORMAL)
        @Description("Utilizamos un ID que no existe y enviamos una solicitud GET al endpoint /books/")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Test Negativo") })
        @Epic("BackEnd")
        @Lead("Juancito Líder")
        @Feature("Obtener libros mediante la API")
        @Story("Manejo de errores")
        @Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
        public void validarStatusCode404() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName("Status Code 404 al buscar un libro cuyo ID no existe");
                });
 
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .get("/books/999")
                                .then()
                                .assertThat()
                                .statusCode(404);
        }
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(NORMAL)
        @Description("Un usuario cuyo token no es válido intenta crear una orden para un libro")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Test Negativo") })
        @Epic("BackEnd")
        @Feature("Orders")
        @Story("Authentication")
        @Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
        public void validarStatusCode401CuandoElTokenEsInvalido() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName("Valida que al proveer un token inválido, obtenemos un status code 401");
                });
 
                String cuerpoSolicitud = "{\n" +
                                "    \"bookId\": 1,\n" +
                                "    \"customerName\": \"Patito Miner\"\n" +
                                "}";
 
                given()
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer TOKENINVALIDO")
                                .body(cuerpoSolicitud)
                                .when()
                                .post("/orders")
                                .then()
                                .assertThat()
                                .statusCode(401)
                                .log().all();
        }
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(NORMAL)
        @Description("Enviamos una consulta del tipo POST al endpoint /orders con un cuerpo vacío para validar un status code 400")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Test Negativo") })
        @Epic("BackEnd")
        @Feature("Orders")
        @Story("Manejo de errores")
        @Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
        public void validarStatusCode400CuandoElCuerpoEsInvalido() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName(
                                        "Orden con cuerpo vacío devuelve un 400");
                });
 
                String cuerpoSolicitud = "{}";
                System.out.println(token);
 
                given()
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + token)
                                .body(cuerpoSolicitud)
                                .when()
                                .post("/orders")
                                .then()
                                .assertThat()
                                .statusCode(400)
                                .log().all();
        }
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(NORMAL)
        @Description("Una solicitud POST al endpoint /api-clients para crear un cliente devuelve un código de estado 400 cuando no se incluye un email en el cuerpo de la solicitud")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Test Negativo") })
        @Issue("https://github.com/vdespa/introduction-to-postman-course/issues/18")
        @Epic("BackEnd")
        @Feature("Autenticación")
        @Story("Manejo de errores")
        public void validarSolicitudSinEmail() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName("Crear un usuario sin email devuelve un código 400");
                });
 
                String cuerpoSolicitudSoloNombre = "{\n" +
                                "    \"clientName\": \"Patito\"\n" +
                                "}";
 
                given()
                                .header("Content-Type", "application/json")
                                .body(cuerpoSolicitudSoloNombre)
                                .when()
                                .post("/api-clients")
                                .then()
                                .assertThat()
                                .statusCode(400)
                                .body("error", equalTo("Invalid or missing client email."))
                                .log().all();
        }
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(NORMAL)
        @Description("Cuando enviamos un request del tipo POST al endpoint /api-client con clientName y clientEmail, el cliente se crea satisfactoriamente y devuelve un código de estado 201")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Test Positivo") })
        @Epic("BackEnd")
        @Feature("Autenticación")
        @Story("Creación de usuario")
        @Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
        public void validarSolicitudValida() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName("Crea un usuario válido");
                });
 
                String cuerpoSolicitudValido = "{\n" +
                                "    \"clientName\": \"Patito\",\n" +
                                "    \"clientEmail\": \"patito" + System.currentTimeMillis() + "@example.com\"\n" +
                                "}";
 
                given()
                                .header("Content-Type", "application/json")
                                .body(cuerpoSolicitudValido)
                                .when()
                                .post("/api-clients")
                                .then()
                                .assertThat()
                                .statusCode(201)
                                .body("accessToken", org.hamcrest.Matchers.notNullValue())
                                .log().all();
        }
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(NORMAL)
        @Description("Mide el tiempo de la respuesta GET al endpoint /books y que este sea menor a 2.9 segundos")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Performance Test") })
        @Epic("BackEnd")
        @Feature("Books")
        @Story("Performance de la búsqueda de libros")
        @Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
        public void validarTiempoDeRespuesta() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName("El tiempo de respuesta es satisfactorio");
                });
 
                given()
                                .header("Content-Type", "application/json")
                                .when()
                                .get("/books")
                                .then()
                                .assertThat()
                                .statusCode(200)
                                .time(lessThan(2900L)); // Validamos que la respuesta sea menor a 2.9 segundos.
        }
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(NORMAL)
        @Description("Al realizar dos solicitudes PUT una tras la otra, el resultado es el mismo, es decir, idempotente.")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Test de Idempotencia") })
        @Epic("BackEnd")
        @Feature("Orders")
        @Story("Actualización de órdenes")
        @Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
        public void validarIdempotenciaPUT() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName("Actualizar una orden funciona correctamente");
                });
 
                OrderResponse orderResponse = crearOrden(token);
                String orderId = orderResponse.getOrderId();
                System.out.println(orderId);
 
                String cuerpoActualización = "{\n" +
                                "    \"customerName\": \"Nuevo Nombre\"\n" +
                                "}";
                // Primera actualización
                Response primerPUT = given()
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + token)
                                .body(cuerpoActualización)
                                .when()
                                .patch("/orders/" + orderId)
                                .then()
                                .assertThat()
                                .statusCode(204)
                                .extract().response();
 
                // Segunda actualización
                Response segundoPUT = given()
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + token)
                                .body(cuerpoActualización)
                                .when()
                                .patch("/orders/" + orderId)
                                .then()
                                .assertThat()
                                .statusCode(204)
                                .extract().response();
 
                // Validamos que ambas respuestas son iguales
                Assert.assertEquals(primerPUT.getStatusCode(), segundoPUT.getStatusCode());
                Assert.assertEquals(primerPUT.getTime(), segundoPUT.getTime(), 1000);
        }
 
        @Test(groups = { "API Books", "API Regression" })
        @Severity(NORMAL)
        @Description("Una vez borramos una orden con un request DELETE al endpoint /orders/orderId, si intentamos repetir el request para el mismo ID obtenemos 404")
        @Owner("Patito")
        @Tags({ @Tag("Books"), @Tag("Test de Idempotencia") })
        @Epic("BackEnd")
        @Feature("Orders")
        @Story("Borrado de órdenes")
        @Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
        public void validarIdempotenciaDELETE() {
                Allure.getLifecycle().updateTestCase(testResult -> {
                        testResult.setName("Una orden se puede borrar solo una vez");
                });
 
                String orderId = crearOrden(token).getOrderId();
 
                // Primera eliminación
                Response primerDelete = given()
                                .header("Authorization", "Bearer " + token)
                                .when()
                                .delete("/orders/" + orderId)
                                .then()
                                .assertThat()
                                .statusCode(204)
                                .extract().response();
                // Segunda eliminación
                Response segundoDelete = given()
                                .header("Authorization", "Bearer " + token)
                                .when()
                                .delete("/orders/" + orderId)
                                .then()
                                .assertThat()
                                .statusCode(404)
                                .extract().response();
 
                // La primera eliminación fue exitosa (204) y la segunda no (un 404)
                Assert.assertEquals(primerDelete.getStatusCode(), 204);
                Assert.assertEquals(segundoDelete.getStatusCode(), 404);
 
        }
 
        // Método auxiliar para crear una orden
        private OrderResponse crearOrden(String token) {
                String cuerpoOrden = "{\n" +
                                "    \"bookId\": 1,\n" +
                                "    \"customerName\": \"Patito\"\n" +
                                "}";
                Response respuesta = given()
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + token)
                                .body(cuerpoOrden)
                                .when()
                                .post("/orders")
                                .then()
                                .assertThat()
                                .statusCode(201)
                                .extract().response();
 
                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setOrderId(respuesta.jsonPath().getString("orderId"));
                return orderResponse;
        }
 
        private String generateRandomEmail() {
                Random random = new Random();
                int randomNumber = random.nextInt(10000); // Generar un número aleatorio entre 0 y 9999
                return "user" + randomNumber + "@example.com";
        }
 
}