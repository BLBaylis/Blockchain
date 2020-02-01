package blockchain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;


class KeyGenerator {
    private KeyPair pair;

    KeyGenerator() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            this.pair = keyGen.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    KeyPair getKeyPair() {
        return this.pair;
    }

}
