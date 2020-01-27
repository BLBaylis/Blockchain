package blockchain;

public class Message {
    private String message;
    private User sender;

    Message(String message, User sender) {
        this.message = message;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", message, sender.getName());
    }
}
