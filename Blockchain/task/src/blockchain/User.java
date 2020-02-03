package blockchain;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

class User {
    private String name;
    private long id;
    private PrivateKey privateKey;
    private PublicKey publicKey;

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

    Transaction createTransaction() {
        User seller = TransactionGenerator.getRandomUser();
        int amount = TransactionGenerator.getRandomAmount();
        Transaction transaction = new Transaction(Blockchain.getTransactionId(), this, seller, amount, publicKey);
        transaction.sign(privateKey);
        return transaction;
    }
}
