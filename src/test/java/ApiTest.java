package com.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class ApiTest {

    
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        System.out.println("Base URI ayarlandı: " + RestAssured.baseURI);
    }

    
    @Test
    public void testGetPostById() {
        System.out.println("GET isteği gönderiliyor...");

        Response response = given()
                .when()
                .get("/posts/1") 
                .then()
                .log().all()
                .assertThat() 
                .statusCode(200) 
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("userId", greaterThan(0)) 
                .time(lessThan(3000L))
                .extract().response(); 

        System.out.println("GET isteği başarılı. Yanıt süresi: " + response.getTime() + " ms");
    }

    
    @Test
    public void testCreateNewPost() {
        System.out.println("POST isteği gönderiliyor...");

       
        String requestBody = "{\n" +
                "  \"title\": \"Yapay Zeka\",\n" +
                "  \"body\": \"Bu bir test içeriğidir.\",\n" +
                "  \"userId\": 1\n" +
                "}";

        Response response = given()
                .header("Content-Type", "application/json") 
                .body(requestBody) 
                .when()
                .post("/posts") 
                .then()
                .log().all()
                .assertThat()
                .statusCode(201) 
                .contentType(ContentType.JSON)
                .body("title", equalTo("Yapay Zeka"))
                .body("body", equalTo("Bu bir test içeriğidir."))
                .body("userId", equalTo(1))
                .time(lessThan(3000L)) 
                .extract().response();

        System.out.println("POST başarılı. Oluşturulan gönderi ID'si: " + response.jsonPath().getInt("id"));
        System.out.println("Yanıt süresi: " + response.getTime() + " ms");
    }
}

