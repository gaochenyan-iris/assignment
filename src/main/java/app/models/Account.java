package app.models;

import java.util.List;

import app.models.Transaction;
import io.jsondb.annotation.Id;

public class Account {
    @Id
    private String id;
    private long balance;
    private List<Transaction> transactions;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public long getBalance() {
        return balance;
    }
    public void setBalance(long balance) {
        this.balance = balance;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = sortTransactionByTimeStamp(transactions);
    }

    private List<Transaction> sortTransactionByTimeStamp(List<Transaction> transactions){
        if (transactions != null) {
            transactions.sort((Transaction trx1, Transaction trx2) ->
                trx2.getTimestamp().compareTo(trx1.getTimestamp()));
        }
        return transactions;
    }
}
