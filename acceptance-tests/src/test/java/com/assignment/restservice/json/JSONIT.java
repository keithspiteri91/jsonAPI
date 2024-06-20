package com.assignment.restservice.json;

import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.assignment.restservice.environment.CIEnvironmentExtension;

import io.restassured.http.ContentType;

import java.io.File;
import java.io.IOException;

@Tag("acceptance")
@ExtendWith(CIEnvironmentExtension.class)
public class JSONIT {

	@Test
    public void getPost() {

        given()
        .when()
			.get("/posts")
		.then().assertThat()
			.statusCode(HTTP_OK).and()
			.body("$", not(empty())).and()
			.body("size()", greaterThan(0));
    }

	@Test
    public void deletePost() {

        given()
        .when()
			.delete("/posts/{id}",1)
		.then().assertThat()
			.statusCode(HTTP_OK);
    }
	
	@Test
	public void specificPost_Found() {
		given()
		.when()
			.get("/posts/{id}", 1)
		.then().assertThat()
			.statusCode(HTTP_OK).and()
			.body("$", not(empty())).and()
			.body("id", equalTo(1));
	}
	
	@Test
	public void post_NotFound() {
		given()
		.when()
			.post("/posts/{id}", 1000)
		.then().assertThat()
			.statusCode(HTTP_INTERNAL_ERROR);
		
	}
	
	@Test
	public void createPost_FromFile_Success() throws IOException {
		File jsonFile = new File("src/test/resources/create-post-request.json");

		given()
			.contentType(ContentType.JSON)
			.body(jsonFile)
		.when()
			.post("/posts")
		.then().assertThat()
			.statusCode(HTTP_CREATED).and()
			.body("$", not(empty())).and()
			.body("id", notNullValue())
			.body("title", equalTo("This is a Test Post"));

	}
	
	@Test
	public void modifyPost_FromFiles_Success() throws IOException {
		File jsonFile = new File("src/test/resources/modify-post-request.json");

		given()
			.contentType(ContentType.JSON)
			.body(jsonFile)
		.when()
			.put("/posts/{id}", 2)
		.then().assertThat()
			.statusCode(HTTP_OK).and()
			.body("$", not(empty()));

	}

	@Test
	public void modifyPost_FromFiles_Failure() throws IOException {
		File jsonFile = new File("src/test/resources/modify-post-request.json");

		given()
			.contentType(ContentType.JSON)
			.body(jsonFile)
		.when()
			.put("/posts/{id}", 2342)
		.then().assertThat()
			.statusCode(HTTP_INTERNAL_ERROR);

	}
}
