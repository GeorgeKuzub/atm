package atm.exception;

public class NotEnoughAtmMoney extends Exception {
    private long availableAtmBalance;

    public NotEnoughAtmMoney(String message) {
        super(message);
    }

    public NotEnoughAtmMoney(long availableAtmBalance, String message) {
        super(message);
        this.availableAtmBalance = availableAtmBalance;
    }

    public long getAvailableAtmBalance() {
        return availableAtmBalance;
    }
}
