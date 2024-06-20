package com.assignment.restservice.json;

import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_CREATED;

import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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
			.get("/posts/{id}",1)
		.then().assertThat()
			.statusCode(HTTP_OK);
    }

	@Test
    public void deleteNonExistingPost() {

        given()
        .when()
			.get("/posts/{id}",1213243535)
		.then().assertThat()
			.statusCode(HTTP_NOT_FOUND);
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
			.get("/posts/{id}", 1000)
		.then().assertThat()
			.statusCode(HTTP_NOT_FOUND).and()
			.body("$", Matchers.aMapWithSize(0));
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
}
