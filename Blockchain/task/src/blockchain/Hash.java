package blockchain;

import java.security.MessageDigest;
import java.util.Random;

public class Hash {

        /* Applies Sha256 to a string and returns a hash. */
        static String applySha256(String input){
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                /* Applies sha256 to our input */
                byte[] hash = digest.digest(input.getBytes("UTF-8"));
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

        static String findMagicNumber(int numOfZeros) {
            String hash;
            String startHash = createRequiredStartString(numOfZeros);
            long magicNumber = createNonce();
            String magicNumberStr;
            do {
                magicNumber++;
                magicNumberStr = Long.toString(magicNumber);
                hash = applySha256(magicNumberStr);
            } while (!hash.substring(0, numOfZeros).equals(startHash));
            return magicNumberStr;
        }

        private static String createRequiredStartString(int numOfZeros) {
            StringBuilder str = new StringBuilder();
            while (str.length() < numOfZeros) {
                str.append("0");
            }
            return str.toString();
        }

        private static long createNonce() {
            Random generator = new Random();
            long magicNumber = generator.nextLong();
            while (magicNumber < 0 || magicNumber > Long.MAX_VALUE / 2) {
                magicNumber = generator.nextLong();
            }
            return magicNumber;
        }
}
