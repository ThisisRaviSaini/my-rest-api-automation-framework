package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import endpoints.UserEndPoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import requestbody.UserRequestBody;
import responsebody.UserResponseBody;

public class UserTest {

    SoftAssert softAssert = new SoftAssert();

    UserRequestBody userRequestBody;

    @BeforeClass
    public UserRequestBody createUserTestData() {
        UserRequestBody userRequestBody = new UserRequestBody();
        Faker faker = new Faker();

        userRequestBody.setId(faker.idNumber().hashCode());
        userRequestBody.setFirstName(faker.name().firstName());
        userRequestBody.setLastName(faker.name().lastName());
        userRequestBody.setCompany(faker.company().name());

        return userRequestBody;
    }


    @Test
    public void createUserTest() throws JsonProcessingException {
        Response response = UserEndPoints.createUser(userRequestBody);

        ObjectMapper objectMapper = new ObjectMapper();
        UserResponseBody userResponseBody = objectMapper.readValue(response.asString(), UserResponseBody.class);

        System.out.println(userResponseBody.getCode());
        System.out.println(userResponseBody.getStatus());
        System.out.println(userResponseBody.getMessage());

        softAssert.assertEquals(userResponseBody.getCode(), "200");
        softAssert.assertEquals(userResponseBody.getStatus(), "Success");
        softAssert.assertEquals(userResponseBody.getMessage(), "User created successfully");

        softAssert.assertAll();


    }
}
