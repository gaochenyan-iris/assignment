package app.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.models.Account;
import app.models.Customer;
import app.models.Transaction;
import app.rest.AccountOpeningRequest;
import app.rest.StandardResponse;
import io.jsondb.JsonDBTemplate;

public class AccountOpeningService {
    private static final Logger logger = Logger.getLogger(AccountOpeningService.class.getName());
    private static final String fromAcc = "Opening Account";
    private JsonDBTemplate jsonDB;

    public AccountOpeningService(JsonDBTemplate db) {
        this.jsonDB = db;
    }

    public String openNewAccountForExistingCustomer(AccountOpeningRequest request) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!request.isValid()) {
            return gson.toJson(
                    new StandardResponse(Response.Status.BAD_REQUEST)
                    .withMessage(request.getErrorMsg()));
        }

        Customer customer = jsonDB.findById(request.customerId, Customer.class);
        if (customer != null) {
            logger.info("Found the customer, customerId: " + request.customerId);
            Account acc = new Account();
            String id = "acc" + new Date().getTime();
            acc.setId(id);
            acc.setBalance(request.initialCredit);

            if (request.initialCredit > 0) {
                Transaction trx = new Transaction();
                trx.setFrom(fromAcc);
                trx.setTo(id);
                trx.setAmount(request.initialCredit);
                trx.setTimestamp(new Date());
                acc.setTransactions(new ArrayList<Transaction>(Arrays.asList(trx)));
            }

            List<Account> accountList = customer.getAccounts() == null ?
                    new ArrayList<>() : customer.getAccounts();
            accountList.add(acc);
            customer.setAccounts(accountList);

            jsonDB.save(customer, Customer.class);

            return gson.toJson(new StandardResponse(Response.Status.CREATED));
        } else {
            logger.info("No required customer");
            return gson.toJson(
                    new StandardResponse(Response.Status.BAD_REQUEST)
                    .withMessage("The customer does not exist."));
        }
    }
}
