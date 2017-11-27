package atm.logic;

import java.time.LocalDateTime;

public class Transaction {
    private long amount;
    private LocalDateTime timeStampTransaction;

    public Transaction(long amount, Account account) {
        this.amount = amount;
        this.timeStampTransaction = LocalDateTime.now();
    }

    public long getAmount() {
        return amount;
    }

    public LocalDateTime getTimeStampTransaction() {
        return timeStampTransaction;
    }
}