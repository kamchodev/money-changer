package com.money.changer.cantor.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.changer.cantor.account.model.AccountDto;
import com.money.changer.cantor.account.model.NewAccount;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class AccountIntegrationTest {

    private String url = "http://localhost:" + 8090;

    @Test
    public void shouldGetAccount() {
        AccountDto accountDto = given()
                .when()
                    .log().all()
                    .get(url + "/get-account/200")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(200))
                    .body("name", equalTo("Jan"))
                    .body("surname", equalTo("Kowalski"))
                    .extract()
                    .as(AccountDto.class);

        assertThat(new BigDecimal("1000.00"), Matchers.comparesEqualTo(accountDto.getAccountBalancePLN()));
        assertThat(new BigDecimal("300.50"), Matchers.comparesEqualTo(accountDto.getAccountBalanceUSD()));
    }

    @Test
    public void shouldReturnAccountNotExistException() {
        given()
                .when()
                    .log().all()
                    .get(url + "/get-account/222")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("message", equalTo("Account does not exist"));
    }

    @Test
    public void shouldAddNewAccount() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        NewAccount newAccount = new NewAccount("Tomasz", "Testowy", new BigDecimal("2000.00"), new BigDecimal("1555.00"));

        AccountDto accountDto = given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(newAccount))
                .when()
                    .log().all()
                    .post(url + "/create-account")
                .then()
                .log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("name", equalTo("Tomasz"))
                    .body("surname", equalTo("Testowy"))
                    .extract()
                    .as(AccountDto.class);

        assertThat(new BigDecimal("2000.00"), Matchers.comparesEqualTo(accountDto.getAccountBalancePLN()));
        assertThat(new BigDecimal("1555.00"), Matchers.comparesEqualTo(accountDto.getAccountBalanceUSD()));
    }

    @Test
    public void shouldNotCreatAccountWithoutName() {
        String payload = "{\n" +
                "  \"surname\": \"Kowalski\",\n" +
                "  \"accountBalancePLN\": \"300.00\"\n" +
                "}";

            given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(payload)
                .when()
                    .log().all()
                    .post(url + "/create-account")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors[0].defaultMessage", equalTo("Name has to be present"));
    }

    @Test
    public void shouldNotCreatAccountWithoutSurname() {
        String payload = "{\n" +
                "  \"name\": \"Jan\",\n" +
                "  \"accountBalancePLN\": \"300.00\"\n" +
                "}";

            given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(payload)
                .when()
                    .log().all()
                    .post(url + "/create-account")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors[0].defaultMessage", equalTo("Surname has to be present"));
    }

    @Test
    public void shouldNotCreatAccountWithoutAccountBalancePLN() {
        String payload = "{\n" +
                "  \"name\": \"Jan\",\n" +
                "  \"surname\": \"Kowalski\"\n" +
                "}";

            given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(payload)
                .when()
                    .log().all()
                    .post(url + "/create-account")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors[0].defaultMessage", equalTo("Account Balance PLN has to be present"));
    }

    @Test
    public void shouldChangePlnToUsd() {
            given()
                .when()
                    .log().all()
                    .param("id", 2000)
                    .param("amountPLN", "1000.00")
                    .patch(url + "/change-PLN-USD")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldChangeUsdToPln() {
        given()
                .when()
                    .log().all()
                    .param("id", 2001)
                    .param("amountUSD", "200.00")
                    .patch(url + "/change-USD-PLN")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldNotChangePlnToUsdNoFunds() {
            given()
                .when()
                    .log().all()
                    .param("id", 2000)
                    .param("amountPLN", "100000.00")
                    .patch(url + "/change-PLN-USD")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .body("message", equalTo("Not enough funds"));
    }

    @Test
    public void shouldNotChangeUsdToPlnNoFunds() {
            given()
                .when()
                    .log().all()
                    .param("id", 2001)
                    .param("amountUSD", "100000.00")
                    .patch(url + "/change-USD-PLN")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .body("message", equalTo("Not enough funds"));
    }
}
