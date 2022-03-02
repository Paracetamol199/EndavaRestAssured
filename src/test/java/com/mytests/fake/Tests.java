package com.mytests.fake;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class Tests {

    @Test
    public void verifyTitleAndBodyForFirstPost() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
				.header("ContentType", "application/json; charset=UTF-8")
				.header("Authorization", "accessToken")
                .get("https://jsonplaceholder.typicode.com/posts/1");
        response.then().statusCode(200);
        response.body().prettyPeek();

        assertThat("The 'title' is incorrect", response.body().jsonPath().get("title"), Matchers.is("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"));
        assertThat("The 'body' is incorrect", response.body().jsonPath().get("body"), Matchers.is("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"));
    }

	@Test
	public void verifyAllPosts() {
		Integer id;
			Response response = RestAssured.given().contentType(ContentType.JSON)
					.get("https://jsonplaceholder.typicode.com/posts");
			response.then().statusCode(200);
//			response.body().prettyPeek();
		ArrayList<HashMap<String, String>> iterations = response.then().extract().path("findAll {it.id}");
		for (id=1; id<=iterations.size(); id++) {

			ArrayList<HashMap<String, String>> ids = response.then().extract().path("findAll {it.id == " + id + "}.id");
			ArrayList<HashMap<String, String>> titles = response.then().extract().path("findAll {it.id == " + id + "}.title");
			ArrayList<HashMap<String, String>> bodies = response.then().extract().path("findAll {it.id == " + id + "}.body");

			assertThat("The 'id' is null", ids, Matchers.notNullValue());
			assertThat("The 'title' is null", titles, Matchers.notNullValue());
			assertThat("The 'title' is empty", titles, Matchers.not(Matchers.empty()));
			assertThat("The 'body' is null", bodies, Matchers.notNullValue());
			assertThat("The 'body' is empty", titles, Matchers.not(Matchers.empty()));

			System.out.println("\n" + id + " TITLE: " + response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.title").toString());
			System.out.println("\n" + id + " BODY: " + response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.body").toString());
		}
	}

	@Test
	public void verifyValuesForAllUsersV1() {
    	Integer id;
    	for (id=1; id<=10; id++){
			Response response = RestAssured.given().contentType(ContentType.JSON)
					.get("https://jsonplaceholder.typicode.com/users/" + id);
			response.then().statusCode(200);
			response.body().prettyPeek();

			assertThat("The 'id' is incorrect", response.body().jsonPath().get("id"), Matchers.is(id));
			assertThat("The 'name' is incorrect", response.body().jsonPath().get("name"), Matchers.notNullValue());
			assertThat("The 'username' is incorrect", response.body().jsonPath().get("username"), Matchers.notNullValue());
			assertThat("The 'email' is incorrect", response.body().jsonPath().get("email"), Matchers.notNullValue());
			assertThat("The 'email' is incorrect", response.body().jsonPath().get("email"), Matchers.containsString("@"));
			assertThat("The 'email' is incorrect", response.body().jsonPath().get("email"), Matchers.containsString("."));
			assertThat("The 'address' is incorrect", response.body().jsonPath().get("address.street"), Matchers.notNullValue());
			assertThat("The 'address' is incorrect", response.body().jsonPath().get("address.suite"), Matchers.notNullValue());
			assertThat("The 'address' is incorrect", response.body().jsonPath().get("address.city"), Matchers.notNullValue());
			assertThat("The 'address' is incorrect", response.body().jsonPath().get("address.zipcode"), Matchers.notNullValue());
			assertThat("The 'address' is incorrect", response.body().jsonPath().get("address.geo.lat"), Matchers.notNullValue());
			assertThat("The 'address' is incorrect", response.body().jsonPath().get("address.geo.lng"), Matchers.notNullValue());
			assertThat("The 'address' is incorrect", response.body().jsonPath().get("phone"), Matchers.notNullValue());
			assertThat("The 'phone' is incorrect", response.body().jsonPath().get("phone"), Matchers.notNullValue());
			assertThat("The 'website' is incorrect", response.body().jsonPath().get("website"), Matchers.notNullValue());
			assertThat("The 'website' is incorrect", response.body().jsonPath().get("website"), Matchers.notNullValue());
			assertThat("The 'company' is incorrect", response.body().jsonPath().get("company.name"), Matchers.notNullValue());
			assertThat("The 'company' is incorrect", response.body().jsonPath().get("company.catchPhrase"), Matchers.notNullValue());
			assertThat("The 'company' is incorrect", response.body().jsonPath().get("company.bs"), Matchers.notNullValue());
		}
	}
	@Test
	public void verifyValuesForAllUsersV2() {
		Integer id;
		Response response = RestAssured.given().contentType(ContentType.JSON)
				.get("https://jsonplaceholder.typicode.com/users");
		response.then().statusCode(200);
//			response.body().prettyPeek();
		ArrayList<HashMap<String, String>> iterations = response.then().extract().path("findAll {it.id}");
		System.out.println(iterations.size());
		for (id=1; id<=iterations.size(); id++) {

			ArrayList<HashMap<String, String>> ids = response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.id");
			ArrayList<HashMap<String, String>> names = response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.name");
			ArrayList<HashMap<String, String>> userNames = response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.username");
			ArrayList<HashMap<String, String>> emails = response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.email");
			ArrayList<HashMap<String, String>> addressCity = response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.address.city");
			ArrayList<HashMap<String, String>> addressStreet = response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.address.street");
			ArrayList<HashMap<String, String>> addressGeo = response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.address.geo");

			assertThat("The 'id' is null", ids, Matchers.notNullValue());
			assertThat("The 'name' is null", names, Matchers.notNullValue());
			assertThat("The 'name' is empty", names, Matchers.not(Matchers.empty()));
			assertThat("The 'userName' is null", userNames, Matchers.notNullValue());
			assertThat("The 'userName' is empty", userNames, Matchers.not(Matchers.empty()));
			assertThat("The 'email' is null", emails, Matchers.notNullValue());
			assertThat("The 'email' is empty", emails, Matchers.not(Matchers.empty()));
			assertThat("The 'city' is null", addressCity, Matchers.notNullValue());
			assertThat("The 'city' is empty", addressCity, Matchers.not(Matchers.empty()));
			assertThat("The 'street' is null", addressStreet, Matchers.notNullValue());
			assertThat("The 'street' is empty", addressStreet, Matchers.not(Matchers.empty()));
			assertThat("The 'geo' is empty", addressGeo, Matchers.not(Matchers.empty()));

			System.out.println("\n" + id + " NAME: " + response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.name").toString());
			System.out.println("\n" + id + " USERNAME: " + response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.username").toString());
			System.out.println("\n" + id + " EMAIL: " + response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.email").toString());
			System.out.println("\n" + id + " ADDRESS: City: " + response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.address.city").toString());
			System.out.println("\n" + id + " ADDRESS: Street: " + response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.address.street").toString());
			System.out.println("\n" + id + " ADDRESS: Geo: " + response.then().extract().body().jsonPath().get("findAll {it.id == " + id + "}.address.geo").toString());
		}
	}


	@Test
	public void verifyNameAndCompanyNameForFirstUser() {
		Response response = RestAssured.given().contentType(ContentType.JSON)
				.get("https://jsonplaceholder.typicode.com/users/1");
		response.then().statusCode(200);
		response.body().prettyPeek();

		assertThat("The 'name' is incorrect", response.body().jsonPath().get("name"), Matchers.notNullValue());
		assertThat("The 'name' is incorrect", response.body().jsonPath().get("name"), Matchers.is("Leanne Graham"));
		assertThat("The 'company name' is incorrect", response.body().jsonPath().get("company.name"), Matchers.is("Romaguera-Crona"));
	}

	@Test
	public void deleteTheFirstPost() {
		Response response = RestAssured.given().contentType(ContentType.JSON)
				.delete("https://jsonplaceholder.typicode.com/posts/1");

		response.then().statusCode(200);
	}

    @Test
    public void checkAddress() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/users");
        response.then().statusCode(200).body("[0].address.street", is("Kulas Light"));
    }

    @Test
    public void testUserNames() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/users");
        response.then().statusCode(200).body("findAll {it.id>8}", is(notNullValue()));
    }


    @Test
    public void verifyAllUsersHaveAddress() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/users");
        ArrayList<HashMap<String, String>> usernames = response.then().extract().path("findAll {it.address}");
        response.then().statusCode(200).body("findAll {it.id>8}", is(notNullValue()));
    }

    @Test
    public void testPhoto() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("http://jsonplaceholder.typicode.com/photos");
        response.then().statusCode(200)
                .body("findAll {it.id == 1 && it.albumId == 1}[0].url", is("https://via.placeholder.com/600/92c952"));
    }

    @Test
    public void verifyTheSecondPhotoURL() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("http://jsonplaceholder.typicode.com/photos");
        response.then().statusCode(200);

        assertThat("The URL is incorrect",
                response.then().extract().body().jsonPath().get("findAll {it.id == 2 && it.albumId == 1}[0].url"),
                Matchers.is("https://via.placeholder.com/600/771796"));
    }
}
