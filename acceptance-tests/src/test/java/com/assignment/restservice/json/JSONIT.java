package com.assignment.restservice.json;

import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.assignment.restservice.environment.CIEnvironmentExtension;

import io.restassured.http.ContentType;

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
}