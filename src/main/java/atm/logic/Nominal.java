package atm.logic;

public enum Nominal {
    ONE(1), TWO(2), FIVE(5), TEN(10), TWENTY(20), FIFTY(50), HUNDRED(100);

    private int nominalVal;

    Nominal(int nominalVal) {
        this.nominalVal = nominalVal;
    }

    public int getVal() {
        return nominalVal;
    }
}
