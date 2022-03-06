package com.mytests.fake;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.junit.MatcherAssert.assertThat;

public class HomeworkTests {

    @Test
    public void verifyIfStatusCodeIs404WhenUrlNotExists() {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get("http://jsonplaceholder.typicode.com/testing");
        response.then().statusCode(404);

        assertThat("The status Code is 200", response.getStatusCode(), Matchers.is(404));
    }

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
        int titleId = 2;
        String title = response.then().extract().path("findAll {it}[" + titleId + "].title").toString();
        String sortedTitle = sortAndLowerCaseString(title);

        System.out.println("The title chose is: " + title);
        System.out.println("The title sorted is: " + sortedTitle);
        int i;
        List<Integer> numberOfIds = new ArrayList<>();

        for (i = 0; i < numberOfTitles.size(); i++) {
            if (i != titleId) {
                String titleToCompare = response.then().extract().path("findAll {it}[" + i + "].title").toString();
                String titleToCompareSorted = sortAndLowerCaseString(titleToCompare);
                if (sortedTitle.equals(titleToCompareSorted)) {
                    numberOfIds.add(i);
                    assertThat("Title is angram with another title from list", titleToCompareSorted, Matchers.is(sortedTitle));
                }
            }
        }
        if (numberOfIds.size() == 0)
            System.out.println("This title is not anagram with any title");
        else if (numberOfIds.size() == 1)
            System.out.println("This title is anagram with title " + numberOfIds);
        else
            System.out.println("This title is anagram with " + numberOfIds.size() + " titles and the titles are: " + numberOfIds);
    }

    @Test
    public void verifyIfAnagramMethodItWorks() {
        ArrayList<String> numberOfTitles = new ArrayList<>();
        numberOfTitles.add("Geeks");
        numberOfTitles.add("eegsk");
        numberOfTitles.add("siunj");
        numberOfTitles.add("keeGs");
        numberOfTitles.add("junsj");
        numberOfTitles.add("uomiuoij");

        System.out.println("Total number of titles is: " + numberOfTitles.size());
        int titleId = 0;
        String title = numberOfTitles.get(titleId);
        String sortedTitle = sortAndLowerCaseString(title);
        System.out.println("The title chose is: " + title);
        System.out.println("The title sorted is: " + sortedTitle);
        int i;
        List<Integer> numberOfIds = new ArrayList<>();

        for (i = 0; i < numberOfTitles.size(); i++) {
            if (i != titleId) {
                String titleToCompare = numberOfTitles.get(i);
                String titleToCompareSorted = sortAndLowerCaseString(titleToCompare);
                if (sortedTitle.equals(titleToCompareSorted)) {
                    numberOfIds.add(i);
                    assertThat("Title is angram with another title from list", titleToCompareSorted, Matchers.is(sortedTitle));
                }
            }
        }
        if (numberOfIds.size() == 0)
            System.out.println("This title is not anagram with any title");
        else if (numberOfIds.size() == 1)
            System.out.println("This title is anagram with title " + numberOfIds);
        else
            System.out.println("This title is anagram with " + numberOfIds.size() + " titles and the titles are: " + numberOfIds);
    }

    public String sortAndLowerCaseString(String stringToBeSorted) {
        String lowerCaseString = stringToBeSorted.toLowerCase();
        char[] chars = lowerCaseString.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public ArrayList<Float> convertArrayListStringToArrayListFloat(ArrayList<String> arrayToBeConverted) {
        ArrayList<Float> arrayFloat = new ArrayList<>();
        for (String values : arrayToBeConverted)
            arrayFloat.add(Float.parseFloat(values));
        return arrayFloat;
    }
}
