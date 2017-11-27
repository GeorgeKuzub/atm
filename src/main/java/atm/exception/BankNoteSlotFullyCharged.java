package atm.exception;

public class BankNoteSlotFullyCharged extends Exception {
    public BankNoteSlotFullyCharged(String message) {
        super(message);
    }
}
