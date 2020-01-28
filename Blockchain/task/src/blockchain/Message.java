package blockchain;

public class Message {
    private String message;
    private String sender;

    Message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", message, sender);
    }
}
