package tests;

import com.github.javafaker.Faker;
import endpoints.UserEndPoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import requestbody.UserRequestBody;

public class UserTest {

    public static UserRequestBody userRequestBody;

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
    public static void createUSerTest() {
        Response response = UserEndPoints.createUser(userRequestBody);
    }
}
