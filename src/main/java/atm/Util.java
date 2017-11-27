package atm;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static byte[] generateMD5Hash(String strCode) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("MD5 algorithm wasn't found for generating hash code: "
                    + e.getMessage());
            System.exit(2);
        } catch (Error e) {
            System.err.println("Something happened unexpectedly during generating hash.");
            e.printStackTrace();
            System.exit(3);
        }

        return md.digest(strCode.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean checkPinCode(String pin, byte[] hash) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(
                    md.digest(pin.getBytes(StandardCharsets.UTF_8)), hash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(" There isn't 'MD5' algorithm  in the system : "
                    + e.getMessage());
            System.exit(1);
        }

        return false;
    }

    public static boolean isItDigit(String s) {
        if (s.length() > 1) {
            return false;
        }

        char code = s.charAt(0);
        return code >= '0' && code <= '9';
    }
}
