package atm.logic;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;


public class AtmStoreTest {
    AtmStore atmStore;

    @Before
    public void setUp() {
        atmStore = new AtmStore();
    }

    @Test
    public void testAddOneBankNoteWithOneNominal() {
        atmStore.load(Nominal.ONE, 1);
        long size = atmStore.getStoredBanknotes().values().
                stream().findFirst().get();
        Assert.assertEquals(1, size);
    }

    @Test
    public void testAddHundredBankNoteWithFiftyNominal() {
        atmStore.load(Nominal.FIFTY, 100);
        long size = atmStore.getStoredBanknotes().values()
                .stream().findFirst().get();
        Assert.assertEquals(100, size);
    }

    @Test
    public void testAddMultipleBankNotesWithCommonNominal() {
        atmStore.load(Nominal.FIVE, 10);
        atmStore.load(Nominal.FIVE, 20);

        long size = atmStore.getStoredBanknotes().values().
                stream().findFirst().get();
        Assert.assertEquals(30, size);
    }

    @Test
    public void testAddMultipleBankNotesWithDifferentNominal() {
        atmStore.load(Nominal.TWENTY, 10);
        atmStore.load(Nominal.FIVE, 20);

        long qtyTwentyNominal = atmStore.getStoredBanknotes().get(Nominal.TWENTY);
        long qtyFiveNominal = atmStore.getStoredBanknotes().get(Nominal.FIVE);

        Assert.assertTrue(atmStore.getStoredBanknotes().size() == 2);
        Assert.assertEquals(qtyTwentyNominal, 10);
        Assert.assertEquals(qtyFiveNominal, 20);
    }

    @Test
    public void testAddBankNotesAndThenFetchThemAtAll() {
        atmStore.load(Nominal.TEN, 5); // 50
        atmStore.load(Nominal.TWO, 7); // 14
        atmStore.unload(64);
        Assert.assertTrue(atmStore.getStoredBanknotes().size() == 0);
    }

    @Test
    public void testAddBankNotesWithDifferentNominalAndThenFetchPartially() {
        atmStore.load(Nominal.TEN, 4); //40
        atmStore.load(Nominal.FIVE, 6); //30
        atmStore.unload(65);
        long qtyFive = atmStore.getStoredBanknotes().get(Nominal.FIVE);
        Assert.assertTrue(atmStore.getStoredBanknotes().size() == 1);
        Assert.assertEquals(qtyFive, 1);
    }

    @Test
    public void testAddBankNotesAndThenCheckOptimizedOutput() {
        atmStore.load(Nominal.HUNDRED, 2); // 200
        atmStore.load(Nominal.FIFTY, 4); // 200
        atmStore.load(Nominal.TWENTY, 10); // 200
        atmStore.load(Nominal.TEN, 20); // 200
        atmStore.load(Nominal.FIVE, 40); // 200
        atmStore.load(Nominal.TWO, 100); // 200
        atmStore.load(Nominal.ONE, 200); // 200
        atmStore.unload(216);// Expected: HUNDRED x 2->TEN x 1->FIVE x 1->ONE x 1

        Assert.assertNull(atmStore.getStoredBanknotes().get(Nominal.HUNDRED));
        Assert.assertEquals(Optional.of(19L),
                Optional.ofNullable(atmStore.getStoredBanknotes().get(Nominal.TEN)));
        Assert.assertEquals(Optional.of(39L),
                Optional.ofNullable(atmStore.getStoredBanknotes().get(Nominal.FIVE)));
        Assert.assertEquals(Optional.of(199L),
                Optional.ofNullable(atmStore.getStoredBanknotes().get(Nominal.ONE)));
    }

    @Test
    public void testGetTotalAtmBalnce() {
        atmStore.load(Nominal.HUNDRED, 2); // 200
        atmStore.load(Nominal.FIFTY, 4); // 200
        atmStore.load(Nominal.TWENTY, 10); // 200
        atmStore.load(Nominal.TEN, 20); // 200
        atmStore.load(Nominal.FIVE, 40); // 200
        atmStore.load(Nominal.TWO, 100); // 200
        atmStore.load(Nominal.ONE, 200); // 200

        Assert.assertEquals(1400, atmStore.getTotalAtmBalance());
    }

    @Test
    public void testUnloadMoreAmountThanAtmHas() {
        atmStore.load(Nominal.FIFTY, 3); // 150
        atmStore.load(Nominal.TEN, 4); // 40
        atmStore.unload(200);

        Assert.assertEquals(190, atmStore.getTotalAtmBalance());
    }

    @Test
    public void testUnloadLegalAmountWithoutFitBanknotes() {
        atmStore.load(Nominal.TEN, 4); //40
        atmStore.unload(35);

        Assert.assertEquals(40, atmStore.getTotalAtmBalance());
    }

    @Test
    public void testlLoadBankNoteOverMaxcapacity() {
        atmStore.load(Nominal.TEN, atmStore.MAX_CAPACITY_SLOT);
        atmStore.load(Nominal.TEN, 1);

        Assert.assertEquals(Optional.of(atmStore.MAX_CAPACITY_SLOT),
                Optional.ofNullable(atmStore.getStoredBanknotes().get(Nominal.TEN)));
    }

}
