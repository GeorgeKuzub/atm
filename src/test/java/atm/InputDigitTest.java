package atm;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class InputDigitTest {

    private String strDigit;

    public InputDigitTest(String strDigit) {
        this.strDigit = strDigit;
    }

    @Parameterized.Parameters
    public static Collection<String> data() {
        return Arrays.asList(new String[]{"0", "1", "2", "3", "5", "6", "7", "8", "9", "10", "o"});
    }

    @Test
    public void testIsItDigit() {
        if (strDigit.equalsIgnoreCase("o") ||
                strDigit.equalsIgnoreCase("10")) {
            Assert.assertFalse(Util.isItDigit(strDigit));
        } else {
            Assert.assertTrue(Util.isItDigit(strDigit));
        }
    }

}
