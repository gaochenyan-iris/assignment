package app.rest;

public class AccountOpeningRequest implements Validable{
    public final String customerId;
    public final long initialCredit;
    private String errorMsg;

    public AccountOpeningRequest(String customerId, long initialCredit) {
        this.customerId = customerId;
        this.initialCredit = initialCredit;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public boolean isValid() {
        if (customerId == null || customerId.isEmpty()) {
            errorMsg = "CustomerId cannot be null or empty.";
            return false;
        }
        
        if (initialCredit < 0) {
            errorMsg = "InitialCredit cannot be negative.";
            return false;
        }
        return true;
    }

}
