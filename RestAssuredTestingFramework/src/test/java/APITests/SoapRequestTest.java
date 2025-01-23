package APITests;

import org.testng.Assert;
import org.testng.annotations.Test;
 
import io.restassured.RestAssured;
import io.restassured.response.Response;
 
public class SoapRequestTest {
 
    @Test
    public void testSoapRequest() {
        // URL del servicio SOAP
        String endpoint = "https://www.dataaccess.com/webservicesserver/NumberConversion.wso";
 
        String soapBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body>"
                + "<NumberToWords xmlns=\"http://www.dataaccess.com/webservicesserver/\">"
                + "<ubiNum>500</ubiNum>"
                + "</NumberToWords>"
                + "</soap:Body>"
                + "</soap:Envelope>";
 
        // Enviar la solicitud SOAP
        Response response = RestAssured.given()
                .contentType("text/xml; charset=UTF-8")
                .body(soapBody)
                .post(endpoint);
 
        // Verificar el código de estado de la respuesta
        Assert.assertEquals(response.getStatusCode(), 200);
 
        // Imprimimos la respuesta
        String responseBody = response.getBody().asString();
        System.out.println("Este es el cuerpo de la respuesta: " + responseBody);
 
        // Verificar que la respuesta contiene la palabra "Five Hundred"
        // Extraer el resultado usando xpath.
        String numberInWords = response.xmlPath().getString("//*[local-name()='NumberToWordsResult']").trim();
        Assert.assertEquals(numberInWords, "five hundred");
    }
}