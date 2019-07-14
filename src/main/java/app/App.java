package app;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import app.models.Account;
import app.models.Customer;
import app.models.Transaction;
import app.rest.AccountOpeningRequest;
import app.services.AccountOpeningService;
import app.services.CustomerRequestService;
import io.jsondb.JsonDBTemplate;

public class App 
{
    private static final Logger logger = Logger.getLogger(App.class.getName());
    private static JsonDBTemplate jsonDBTemplate;

    public static void main(String[] args) {
        try {
            init();
            CustomerRequestService customerService = new CustomerRequestService(jsonDBTemplate);
            AccountOpeningService accountService = new AccountOpeningService(jsonDBTemplate);

            post("/account", (request, response) -> {
                response.type("application/json");
                AccountOpeningRequest accRequest = new Gson().fromJson(
                        request.body(), AccountOpeningRequest.class);

                return accountService.openNewAccountForExistingCustomer(accRequest);
            });
            get("/customer/:customerId", (request, response) -> {
                response.type("application/json");

                return customerService.getCustomerInfoById(request.params(":customerId"));
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }

    }

    // Initiate the jsondb and create three customers.
    private static void init() throws IOException, InterruptedException {
        String dbFilesLocation = "./db/";
        String baseScanPackage = "app.models";
        File dbDir = new File(dbFilesLocation);
        FileUtils.deleteDirectory(dbDir);
        jsonDBTemplate = new JsonDBTemplate(dbFilesLocation, baseScanPackage);
        jsonDBTemplate.createCollection(Customer.class);

        String accountId = "acc" + new Date().getTime();
        Transaction trx1 = new Transaction();
        trx1.setFrom("acc1563140985012");
        trx1.setTo(accountId);
        trx1.setAmount(100);
        trx1.setTimestamp(new Date());

        Thread.sleep(2000);
        Transaction trx2 = new Transaction();
        trx2.setFrom("acc1563140955765");
        trx2.setTo(accountId);
        trx2.setAmount(110);
        trx2.setTimestamp(new Date());

        Account acc = new Account();
        acc.setId(accountId);
        acc.setBalance(210);
        acc.setTransactions(new ArrayList<Transaction>(Arrays.asList(trx1, trx2)));

        Customer cus1 = new Customer();
        cus1.setId("0001");
        cus1.setFirstname("Tom");
        cus1.setLastname("Hanks");
        cus1.setAccounts(new ArrayList<Account>(Arrays.asList(acc)));

        jsonDBTemplate.insert(cus1);

        Customer cus2 = new Customer();
        cus2.setId("0002");
        cus2.setFirstname("Clark");
        cus2.setLastname("Kent");
        jsonDBTemplate.insert(cus2);

        Customer cus3 = new Customer();
        cus3.setId("0003");
        cus3.setFirstname("Snow");
        cus3.setLastname("White");
        jsonDBTemplate.insert(cus3);
    }
}
