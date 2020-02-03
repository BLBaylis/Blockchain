package blockchain;

import java.util.Random;

class TransactionGenerator {
    private static Random random = new Random();
    private static User placeholder = new User(Blockchain.getUserId(), "Placeholder", new KeyGenerator().getKeyPair());

    static User getRandomUser() {
        return placeholder;
    }

    static int getRandomAmount() {
        return random.nextInt(151);
    }
}
