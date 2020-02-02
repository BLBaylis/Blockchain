package blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private long id;
    private User sender;
    private String text;
    private PublicKey key;
    private List<byte[]> messageData;

    Message(long id, User sender, String text, PublicKey key) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.key = key;
        messageData = new ArrayList<>();
        messageData.add(text.getBytes());
    }

    List<byte[]> getMessageData() {
        return messageData;
    }

    User getSender() {
        return sender;
    }

    long getId() {
        return id;
    }

    PublicKey getPublicKey() {
        return key;
    }

    void sign(PrivateKey privateKey) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(privateKey);
            rsa.update(messageData.get(0));
            messageData.add(rsa.sign());
        } catch (Exception e) {
            System.out.println("Unable to sign message");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("%s [User Id: %d]: %s", sender.getName(), sender.getId(), text);
    }
}
