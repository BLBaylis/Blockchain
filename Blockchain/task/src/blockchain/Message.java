package blockchain;

import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private User sender;
    private String text;
    private List<byte[]> messageData;

    Message(User sender, String text) {
        this.sender = sender;
        this.text = text;
        messageData = new ArrayList<>();
        messageData.add(text.getBytes());
    }

    List<byte[]> getMessageData() {
        return messageData;
    }

    public User getSender() {
        return sender;
    }

    public void sign(PrivateKey privateKey) {
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
        return String.format("%s: %s", sender.getName(), text);
    }
}
