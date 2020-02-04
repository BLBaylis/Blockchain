package blockchain;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;

class User {
    private String name;
    private long id;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Blockchain blockchain = Blockchain.getInstance();

    User(long id, String name, KeyPair keyPair) {
        this.name = name;
        this.id = id;
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    String getName() {
        return name;
    }

    long getId() {
        return id;
    }

    PublicKey getPublicKey() {
        return publicKey;
    }

    void createTransaction() {
        Random random = new Random();
        User seller = UserDatabase.getUser(random.nextInt(UserDatabase.size()));
        int amount = random.nextInt(151);
        Transaction transaction = new Transaction(Blockchain.getTransactionId(), this, seller, amount, publicKey);
        transaction.sign(privateKey);
        blockchain.sendTransaction(transaction);
    }
}
