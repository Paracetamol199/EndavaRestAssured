package com.mytests.fake;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.FilterContext;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.FilterableResponseSpecification;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.junit.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.junit.MatcherAssert.assertThat;

public class HomeworkTests {

    @Test
    public void verifyIfStatusCodeIs404WhenPhotoNotExists() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("http://jsonplaceholder.typicode.com/albums?userId=1");
        response.then().statusCode(404);

        assertThat("The Status cs404",
                response.then().extract().body().jsonPath().get("findAll {it.id == 2 && it.albumId == 1}[0].url"),
                Matchers.is("https://via.placeholder.com/600/771796"));
    }

    @Test
    public void verifyAllUsersEmailsThatEndsWithBiz() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/users/");
        System.out.println("The Response status code is: " + response.getStatusCode());
        assertThat("The status Code is 200", response.getStatusCode(), Matchers.is(200) );
       // response.then().log().all();
        System.out.println(response.then().extract().body().jsonPath().get("findAll {it}").toString());

    }
}
