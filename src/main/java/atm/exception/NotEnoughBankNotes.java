package atm.exception;

public class NotEnoughBankNotes extends Exception {
    private long availableAmount;

    public NotEnoughBankNotes(long data, String message) {
        super(message);
        this.availableAmount = data;
    }

    public NotEnoughBankNotes(String message) {
        super(message);
    }

    public long getAvailableAmount() {
        return availableAmount;
    }
}
