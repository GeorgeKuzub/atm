package atm;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String idAccount; // idAccount matches to cardNumber
    private User user;
    private List<Transaction> listTransaction;

    public Account(User user) {
        this.user = user;
        this.idAccount = user.getCardId();
        this.listTransaction = new ArrayList<>();
    }

    public void addNewTransaction(long amount) {
        Transaction newTrans = new Transaction(amount, this);
        listTransaction.add(newTrans);
    }

    public long getBalance() {
        long balance = 0;
        for (Transaction t : listTransaction) {
            balance += t.getAmount();
        }
        return balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "idAccount='" + idAccount + '\'' +
                ", user=" + user +
                '}';
    }
}