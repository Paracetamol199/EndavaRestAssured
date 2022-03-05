package com.mytests.fake;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import javax.sound.sampled.FloatControl;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.junit.MatcherAssert.assertThat;

public class HomeworkTests {


//- Write automation tests in order to validate other status codes, other than 200 (400 FOR E.G.) for all endpoints;
//- Print all the users which their emails domain ends with biz; -done
//- Using the users endpoint print all the ids and its names for which the lat coordinate is between -39 and 15 and lng is between -100 and 50; -done
//- Using the photos endpoint for each albumId count all the ids and print the number; -done
// - Using the photos endpoint check if the title from an album is anagram with the title from another album. -done


//    @Test
//    public void verifyIfStatusCodeIs404WhenPhotoNotExists() {
//        Response response = RestAssured.given().contentType(ContentType.JSON)
//                .get("http://jsonplaceholder.typicode.com/albumω");
//        response.then().statusCode(404);
//
////        assertThat("The Status cs404",
////                response.then().extract().body().jsonPath().get("findAll {it.id == 2 && it.albumId == 1}[0].url"),
////                Matchers.is("https://via.placeholder.com/600/771796"));
//    }

    @Test
    public void verifyAllUsersEmailsThatEndsWithBiz() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/users/");
        System.out.println("The Response status code is: " + response.getStatusCode());
        assertThat("The status Code is 200", response.getStatusCode(), Matchers.is(200));
        ArrayList<HashMap<String, String>> usernamesFiltered = response.then().extract()
                .path("findAll { it.email.endsWith('.biz') }");

        Integer userId;
        String userEmail = "";
        for (userId = 0; userId < usernamesFiltered.size(); userId++) {
            userEmail = response.then().extract().body().jsonPath().get("findAll {it.email.endsWith('.biz')}[" + userId + "].email").toString();

            System.out.println("The user[" + userId + "] email is: " + userEmail);

            assertThat("Check that user email endsWith .biz", userEmail, Matchers.endsWith(".biz"));
        }
    }

    @Test
    public void verifyIfAUserItsLocatedBetweenGivenCoordinates() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/users/");
        System.out.println("The Response status code is: " + response.getStatusCode());
        assertThat("The status Code is 200", response.getStatusCode(), Matchers.is(200));

        ArrayList<String> numberOfUsers = response.then().extract().path("findAll {it}");

        ArrayList<String> lngForUsers = response.then().extract().path("findAll {it}.address.geo.lng");
        ArrayList<String> latForUsers = response.then().extract().path("findAll {it}.address.geo.lat");

        ArrayList<Float> lngForUsersFloat = convertArrayListStringToArrayListFloat(lngForUsers);
        ArrayList<Float> latForUsersFloat = convertArrayListStringToArrayListFloat(latForUsers);

        ArrayList<Integer> idsWhichMeetAsserations = new ArrayList<>();

        ArrayList<Integer> userIds = new ArrayList<>();
        ArrayList<String> userNames = new ArrayList<>();
        Integer i;

        for (i = 0; i < numberOfUsers.size(); i++) {
            if (lngForUsersFloat.get(i) > -130 && lngForUsersFloat.get(i) < 50
                    && latForUsersFloat.get(i) > -50 && latForUsersFloat.get(i) < 15) {
                idsWhichMeetAsserations.add(i);
                userIds.add(response.then().extract().body().jsonPath().get("findAll {it}[" + i + "].id"));
                userNames.add(response.then().extract().body().jsonPath().get("findAll {it}[" + i + "].name"));
            }
        }
        for (i = 0; i < idsWhichMeetAsserations.size(); i++) {
            System.out.println("The userId " + userIds.get(i) + " with name " + userNames.get(i) + " is located between given coordinates");
        }
    }

    @Test
    public void verifyAllPhotosAlbumIdAndCountAllIdsAssociated() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/photos/");

        System.out.println("The Response status code is: " + response.getStatusCode());
        assertThat("The status Code is 200", response.getStatusCode(), Matchers.is(200));

        ArrayList<HashMap<String, String>> numberOfAlbumId = response.then().extract().path("findAll {it}.albumId");
        List<HashMap<String, String>> numberOfUniqueAlbumId = numberOfAlbumId.stream().distinct().collect(Collectors.toList());

        System.out.println("Total number of albumIds is: " + numberOfAlbumId.size());
        System.out.println("Total number of unique albumIds is: " + numberOfUniqueAlbumId.size());
        Integer i;
        Integer albumId;
        for (i = 0; i < numberOfUniqueAlbumId.size(); i++) {
            albumId = Integer.valueOf(String.valueOf(numberOfUniqueAlbumId.get(i)));
            ArrayList<HashMap<String, String>> albumIds = response.then().extract().path("findAll {it.albumId ==" + albumId + "}.id");
            System.out.println("The albumId " + albumId + " has " + albumIds.size() + " ids");
            assertThat("The number of ids for each unique albumId is 50", albumIds.size(), Matchers.equalTo(50));
        }
    }

    @Test
    public void verifyIfTitleFromAnAlbumIsAnagramWithAnotherTitleAlbum() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/photos/");

        System.out.println("The Response status code is: " + response.getStatusCode());
        assertThat("The status Code is 200", response.getStatusCode(), Matchers.is(200));

        ArrayList<HashMap<String, String>> numberOfTitles = response.then().extract().path("findAll {it}.title");
        System.out.println("Total number of titles is: " + numberOfTitles.size());

        String title = response.then().extract().path("findAll {it}[10].title").toString();
        String sortedTitle = sortString(title);
        Integer i;
        List<Integer> numberOfIds = new ArrayList<>();

        for (i = 0; i < numberOfTitles.size(); i++) {
            String titleToCompare = response.then().extract().path("findAll {it}[" + i + "].title").toString();
            String titleToCompareSorted = sortString(titleToCompare);
            if (sortedTitle.equals(titleToCompareSorted)) {
                numberOfIds.add(i);
                assertThat("Title is angram with another title from list", titleToCompareSorted, Matchers.is(sortedTitle));
            }
        }
        if (numberOfIds.size() == 0)
            System.out.println("This title is not anagram with any title");
        else if (numberOfIds.size() == 1)
            System.out.println("This title is anagram with title " + numberOfIds);
        else
            System.out.println("This title is anagram with " + numberOfIds.size() + " titles and the titles are: " + numberOfIds);
    }

    public String sortString(String stringToBeSorted) {
        char[] chars = stringToBeSorted.toCharArray();
        Arrays.sort(chars);
        return new String(chars).toLowerCase(Locale.ROOT);
    }

    public ArrayList<Float> convertArrayListStringToArrayListFloat(ArrayList<String> arrayToBeConverted) {
        ArrayList<Float> arrayFloat = new ArrayList<>();
        for (String values : arrayToBeConverted)
            arrayFloat.add(Float.parseFloat(values));
        return arrayFloat;
    }
}
