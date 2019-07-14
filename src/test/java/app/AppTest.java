package app;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import app.models.Customer;
import app.rest.AccountOpeningRequest;
import app.rest.StandardResponse;
import app.services.AccountOpeningService;
import io.jsondb.JsonDBTemplate;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    private static final Logger logger = Logger.getLogger(AppTest.class.getName());
    private JsonDBTemplate jsonDB;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );

        try {
            String dbFilesLocation = "./testdb/";
            String baseScanPackage = "app.models";
            File dbDir = new File(dbFilesLocation);
            FileUtils.deleteDirectory(dbDir);
            jsonDB = new JsonDBTemplate(dbFilesLocation, baseScanPackage);
            jsonDB.createCollection(Customer.class);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testAccountOpeningWithInvalidParameters() {
        Customer cus1 = new Customer();
        cus1.setId("0001");
        cus1.setFirstname("Clark");
        cus1.setLastname("Kent");
        jsonDB.insert(cus1);

        AccountOpeningService accService = new AccountOpeningService(jsonDB);
        // bad request due to no requested customer
        AccountOpeningRequest request = new AccountOpeningRequest("002", 1);
        String result = accService.openNewAccountForExistingCustomer(request);
        StandardResponse resp = new Gson().fromJson(result, StandardResponse.class);
        assertEquals(resp.getStatus(), Response.Status.BAD_REQUEST);
        assertEquals(resp.getMessage(), "The customer does not exist.");

        // bad request due to empty customerId
        request = new AccountOpeningRequest("", 1);
        result = accService.openNewAccountForExistingCustomer(request);
        resp = new Gson().fromJson(result, StandardResponse.class);
        assertEquals(resp.getStatus(), Response.Status.BAD_REQUEST);
        assertEquals(resp.getMessage(), "CustomerId cannot be null or empty.");

        // bad request due to negative initialCredit
        request = new AccountOpeningRequest("0001", -1);
        result = accService.openNewAccountForExistingCustomer(request);
        resp = new Gson().fromJson(result, StandardResponse.class);
        assertEquals(resp.getStatus(), Response.Status.BAD_REQUEST);
        assertEquals(resp.getMessage(), "InitialCredit cannot be negative.");

    }

    public void testSucceedOpeningAccount() {
        Customer cus1 = new Customer();
        String id = "0002";
        cus1.setId(id);
        cus1.setFirstname("Lois");
        cus1.setLastname("Lane");
        jsonDB.insert(cus1);

        AccountOpeningService accService = new AccountOpeningService(jsonDB);
        AccountOpeningRequest request = new AccountOpeningRequest(id, 100);
        String result = accService.openNewAccountForExistingCustomer(request);
        StandardResponse resp = new Gson().fromJson(result, StandardResponse.class);
        assertEquals(resp.getStatus(), Response.Status.CREATED);

        Customer lois = jsonDB.findById(id, Customer.class);
        assertNotNull(lois.getAccounts());
        assertEquals(lois.getAccounts().size(), 1);
        assertEquals(lois.getAccounts().get(0).getBalance(), 100);
        assertNotNull(lois.getAccounts().get(0).getTransactions());
        assertEquals(lois.getAccounts().get(0).getTransactions().size(), 1);

    }
}
