package app.services;

import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.models.Customer;
import app.rest.StandardResponse;
import io.jsondb.JsonDBTemplate;

public class CustomerRequestService {
    private static final Logger logger = Logger.getLogger(CustomerRequestService.class.getName());
    private JsonDBTemplate jsonDB;
    
    public CustomerRequestService(JsonDBTemplate db) {
        this.jsonDB = db;
    }
    
    public String getCustomerInfoById(String customerId) {
        Customer customer = jsonDB.findById(customerId, Customer.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (customer != null) {
            logger.info("Find the customer, customerId: " + customerId);
            return gson.toJson(
                    new StandardResponse(Response.Status.OK)
                    .withData(gson.toJsonTree(customer)));
        } else {
            logger.info("Couldn't find the customer, customerId: " + customerId);
            return gson.toJson(
                    new StandardResponse(Response.Status.NOT_FOUND)
                    .withMessage("No requested customer."));
        }
    }

}
