package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

class HashUtils {

    static String createBlockHash(long magicNumber, String prevHash) {
        String magicNumberStr = Long.toString(magicNumber);
        return applySha256(magicNumberStr + prevHash);
    }

    /* Applies Sha256 to a string and returns a hash. */
    private static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    static boolean isValidHash(String hash, String hashPrefix) {
        return hash.substring(0, hashPrefix.length()).equals(hashPrefix);
    }
}
