package blog.braindose.demo.threescale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.anyOf;
import org.junit.jupiter.api.Test;
import blog.braindose.demo.threescale.model.Loan;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class LoanFunctionTest {

    @Test
    public void testCreateLoan() {

        Loan l = new Loan("John Doe", "1259-1235-56432-2587", 150000, 0.5);

        given()
                .contentType(ContentType.JSON)
                .body(l)
                .when()
                .post("/loan")
                .then()
                .statusCode(is(201));
    }

    @Test
    public void testCreateLoanWithInssuficientInfo() {

        Loan l = new Loan("John Doe", "1259-1235-56432-2587", 150000);
        given()
                .contentType(ContentType.JSON)
                .body(l)
                .when()
                .post("/loan")
                .then()
                .statusCode(is(500));
    }

   
}