package atm.logic;

import atm.exception.BankNoteSlotFullyCharged;
import atm.exception.NotEnoughAtmMoney;
import atm.exception.NotEnoughBankNotes;

import java.util.*;

public class AtmStore {
    long MAX_CAPACITY_SLOT = 100000; // quantity limit for each type of banknote
    private Map<Nominal, Long> storedBanknotes;

    public AtmStore() {
        storedBanknotes = new TreeMap<>(Collections.reverseOrder());

        // initial values of banknotes inside ATM
//        storedBanknotes.put(Nominal.HUNDRED, 100L);
//        storedBanknotes.put(Nominal.FIFTY, 300L);
//        storedBanknotes.put(Nominal.TWENTY, 350L);
//        storedBanknotes.put(Nominal.TEN, 400L);
//        storedBanknotes.put(Nominal.FIVE, 450L);
//        storedBanknotes.put(Nominal.TWO, 500L);
//        storedBanknotes.put(Nominal.ONE, 500L);
    }

    public long load(Nominal note, long num) {
        if (num <= 0) {
            return 0;
        }

        Set<Nominal> noteSet = storedBanknotes.keySet();
        long updatedVal;

        if (noteSet.contains(note)) {
            long currentVal = storedBanknotes.get(note);
            updatedVal = currentVal + num;
        } else {
            updatedVal = num;
        }

        try {
            checkAtmBankNoteSlotCapacity(updatedVal);
            storedBanknotes.put(note, updatedVal);
        } catch (BankNoteSlotFullyCharged e) {
            System.err.printf("The slot for banknote [%s] is fully charged.%n", note);
            System.err.printf("Impossible to load the current banknote%n");
            System.err.printf("Max capacity for all banknotes is : %d", MAX_CAPACITY_SLOT);
            return 0;
        }
        return note.getVal() * num;
    }

    public boolean unload(long requestAmount) {
        Map<Nominal, Long> satisfiedCombination;
        try {
            checkAtmMoney(requestAmount);
            satisfiedCombination =
                    getOptimizedCombination(requestAmount);
        } catch (NotEnoughAtmMoney e) {
            System.err.println("Sorry, we can't withdraw request amount " +
                    "since there isn't enough money in ATM. \n" +
                    "Available ATM balance to withdraw: [ " + e.getAvailableAtmBalance() + " ]");
            return false;
        } catch (NotEnoughBankNotes e) {
            System.err.println("Sorry, we can't withdraw request amount " +
                    "since there isn't enough banknotes. \n" +
                    "Available amount to withdraw: [ " + e.getAvailableAmount() + " ]");
            return false;
        }

        Set<Nominal> removingSetBankNotes = satisfiedCombination.keySet();

        for (Nominal nominal : removingSetBankNotes) {
            long margin = storedBanknotes.get(nominal) - satisfiedCombination.get(nominal);
            if (margin == 0) {
                storedBanknotes.remove(nominal);
            } else {
                storedBanknotes.put(nominal, margin);
            }
        }
        return true;
    }

    public long getTotalAtmBalance() {
        return storedBanknotes.keySet()
                .stream()
                .mapToLong(k -> k.getVal() * storedBanknotes.get(k))
                .sum();
    }

    public Map<Nominal, Long> getStoredBanknotes() {
        return storedBanknotes;
    }

    private Map<Nominal, Long> getOptimizedCombination(long requestedAmount)
            throws NotEnoughBankNotes {
        Map<Nominal, Long> change = new HashMap<>();

        long leftOverAmount = requestedAmount;
        for (Nominal bankNote : storedBanknotes.keySet()) {
            int fitNominal = bankNote.getVal();

            if (fitNominal > leftOverAmount) {
                continue;
            }

            long availableNumberNotes = storedBanknotes.get(bankNote);
            long neededNumberNotes = leftOverAmount / fitNominal;

            if (neededNumberNotes <= availableNumberNotes) {
                change.put(bankNote, neededNumberNotes);
                leftOverAmount = leftOverAmount % fitNominal;
            } else {
                change.put(bankNote, availableNumberNotes);
                leftOverAmount = leftOverAmount - availableNumberNotes * bankNote.getVal();
            }

            if (leftOverAmount == 0) {
                return change;
            }

        }

        if (leftOverAmount != 0) {
            throw new NotEnoughBankNotes(requestedAmount - leftOverAmount,
                    "Unfortunately, there aren't enough banknotes");
        }
        return null;
    }

    private void checkAtmMoney(long requestAmount) throws NotEnoughAtmMoney {
        if (requestAmount > getTotalAtmBalance()) {
            throw new NotEnoughAtmMoney(requestAmount,
                    "Unfortunately, there aren't enough banknotes");
        }
    }

    private void checkAtmBankNoteSlotCapacity(long qty) throws BankNoteSlotFullyCharged {
        if (qty > MAX_CAPACITY_SLOT) {
            throw new BankNoteSlotFullyCharged("Slot is fully charged" +
                    " for this type of banknote.");
        }

    }
}