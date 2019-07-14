package app.models;

import java.util.List;

import app.models.Account;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "customers", schemaVersion= "1.0")
public class Customer {
	@Id
    private String id;
    private String firstname;
    private String lastname;
    private List<Account> accounts;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public List<Account> getAccounts() {
        return accounts;
    }
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

}
