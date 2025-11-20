package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import endpoints.UserEndPoints;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import requestbody.UserRequestBody;
import responsebody.UserResponseBody;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        response.statusCode();
        response.getStatusCode();
        response.time();
        response.getTime();
        response.statusLine();
        response.andReturn();
        response.asPrettyString();
        response.getHeaders();
        response.header("Ravi");
        response.contentType();
        response.cookies();
        response.getSessionId();
        response.jsonPath();
        response.path("Name");
        response.asPrettyString();
        String json = "{\n" +
                "  \"code\": \"200\",\n" +
                "  \"status\": \"Success\",\n" +
                "  \"message\": \"User data fetched\",\n" +
                "  \"metadata\": {\n" +
                "    \"requestId\": \"REQ-123\",\n" +
                "    \"timestamp\": 1732070400000\n" +
                "  },\n" +
                "  \"user\": {\n" +
                "    \"id\": 101,\n" +
                "    \"firstName\": \"Ravi\",\n" +
                "    \"lastName\": \"Saini\",\n" +
                "    \"roles\": [\"Admin\", \"QA\", \"SDET\"],\n" +
                "    \"address\": {\n" +
                "      \"city\": \"Gurgaon\",\n" +
                "      \"pincode\": 122001\n" +
                "    }\n" +
                "  },\n" +
                "  \"projects\": [\n" +
                "    {\n" +
                "      \"name\": \"Payments\",\n" +
                "      \"tech\": [\"Kafka\", \"Java\", \"Microservices\"],\n" +
                "      \"active\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"UPI\",\n" +
                "      \"tech\": [\"Redis\", \"Java\", \"API\"],\n" +
                "      \"active\": false\n" +
                "    }\n" +
                "  ],\n" +
                "  \"scores\": [10, 20, 30, 40]\n" +
                "}";

/*
        {
                "code": "200",
                "status": "Success",
                "message": "User data fetched",
                "metadata": {
                "requestId": "REQ-123",
                "timestamp": 1732070400000
        },
            "user": {
                    "id": 101,
                    "firstName": "Ravi",
                    "lastName": "Saini",
                    "roles": ["Admin", "QA", "SDET"],
                     "address": {
                        "city": "Gurgaon",
                        "pincode": 122001
            }
        },
            "projects": [
            {
                "name": "Payments",
                "tech": ["Kafka", "Java", "Microservices"],
                "active": true
            },
            {
                "name": "UPI",
                "tech": ["Redis", "Java", "API"],
                "active": false
            }
        ],
            "scores": [10, 20, 30, 40]
        }*/

        // ---------------- SAMPLE COMPLEX JSON ----------------

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        // ---------------- SIMPLE FIELDS ----------------
        assertThat(root.get("code").asText(), is("200"));
        assertThat(root.get("status").asText(), equalTo("Success"));
        assertThat(root.get("message").asText(), containsString("fetched"));

        // ---------------- NESTED OBJECT: metadata ----------------
        JsonNode meta = root.get("metadata");
        assertThat(meta.get("requestId").asText(), startsWith("REQ-"));
        assertThat(meta.get("timestamp").asLong(), greaterThan(1000L));

        // ---------------- NESTED OBJECT: user ----------------
        JsonNode user = root.get("user");
        assertThat(user.get("id").asInt(), greaterThan(0));
        assertThat(user.get("firstName").asText(), equalTo("Ravi"));
        assertThat(user.get("lastName").asText(), equalToIgnoringCase("SAINI"));

        // ---------------- ARRAY OF STRINGS: roles ----------------
        JsonNode roles = user.get("roles");
        assertThat(roles.size(), is(3));
        assertThat(roles.toString(), containsString("QA"));
        assertThat(roles.toString(), containsString("SDET"));


        // ---------------- NESTED CHILD OBJECT: address ----------------
        JsonNode address = user.get("address");
        assertThat("City is Wrong", address.get("city").asText(), equalTo("Gurgaon"));
        assertThat("Pin code is Wrong", address.get("pincode").asInt(), is(122001));

        // ---------------- ARRAY OF OBJECTS: projects ----------------
        JsonNode projects = root.get("projects");
        assertThat(projects.size(), is(2));

        // Project 1
        JsonNode p1 = projects.get(0);
        assertThat(p1.get("name").asText(), is("Payments"));
        assertThat(p1.get("active").asBoolean(), is(true));

        JsonNode p1Tech = p1.get("tech");
        assertThat(p1Tech.size(), is(3));
        assertThat(p1Tech.toString(), containsString("Kafka"));

        // Project 2
        JsonNode p2 = projects.get(1);
        assertThat(p2.get("name").asText(), is("UPI"));
        assertThat(p2.get("active").asBoolean(), is(false));

        JsonNode p2Tech = p2.get("tech");
        assertThat(p2Tech.toString(), containsString("Redis"));
        assertThat(p2Tech.toString(), containsString("API"));

        // ---------------- ARRAY OF NUMBERS: scores ----------------
        JsonNode scores = root.get("scores");

        assertThat(scores.size(), is(4));
        assertThat(scores.get(0).asInt(), is(10));
        assertThat(scores.get(3).asInt(), greaterThan(20));

        // Validate every element > 0
        for (JsonNode score : scores) {
            assertThat(score.asInt(), greaterThan(0));
        }

        // Validate exact values
        assertThat(scores.get(1).asInt(), equalTo(20));
        assertThat(scores.get(2).asInt(), equalTo(30));

        // ---------------- ADVANCED MATCHERS ----------------
        assertThat(scores.toString(), containsString("40"));
        assertThat(user.get("firstName").asText(), not(isEmptyString()));
        assertThat(root.has("projects"), is(true));

        // Validate JSON structure with allOf()
        assertThat(root.get("status").asText(),
                allOf(notNullValue(), containsString("Success")));

        // Ravi's Assertion Practice //

        // If you want to use hasItem, convert roles first:
        List<String> roleList = new ObjectMapper()
                .convertValue(roles, new TypeReference<List<String>>() {
                });
        assertThat(roleList, hasItem("QA"));


        //If you want to use hasItemInArray, convert roles first:
        String[] rolesArray = new ObjectMapper()
                .convertValue(roles, String[].class);

        assertThat(rolesArray, hasItemInArray("QA"));
        assertThat(rolesArray, hasItemInArray("SDET"));


/*        System.out.println(userResponseBody.getCode());
          System.out.println(userResponseBody.getStatus());
          System.out.println(userResponseBody.getMessage());*/

        softAssert.assertEquals(userResponseBody.getCode(), "200");
        softAssert.assertEquals(userResponseBody.getStatus(), "Success");
        softAssert.assertEquals(userResponseBody.getMessage(), "User created successfully");

        softAssert.assertAll();
    }
}
