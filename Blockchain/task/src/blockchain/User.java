package blockchain;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

class User {
    private Blockchain blockchain = Blockchain.getInstance();
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

    Message createMessage() {
        String text = MessageTextGenerator.getRandomMessageText();
        Message message = new Message(blockchain.getMessageId(), this, text, publicKey);
        message.sign(privateKey);
        return message;
    }
}
