package endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import requestbody.UserRequestBody;

import static io.restassured.RestAssured.given;

public class UserEndPoints {


    public static Response createUser(UserRequestBody userRequestBody){

        Response response= given().contentType(ContentType.JSON)
                .accept(ContentType.JSON).body(userRequestBody).when().post(Routes.postUrl);
        return response;
    }

    public static Response getUser(String username){

        Response response= given().pathParam("username", username).when().get(Routes.getUrl);
        return response;
    }
}
