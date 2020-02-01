package blockchain;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

class User {
    private String name;
    //private static int userId = 0;
    //private int id;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    User(String name, KeyPair keyPair) {
        this.name = name;
        //this.id = ++userId;
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    String getName() {
        return name;
    }

    //int getId() {
    //return id;
    //}

    PublicKey getPublicKey() {
        return publicKey;
    }

    Message createMessage() {
        String text = MessageTextGenerator.getRandomMessageText();
        Message message = new Message(this, text);
        message.sign(privateKey);
        return message;
    }
}
