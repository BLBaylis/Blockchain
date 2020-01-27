package blockchain;

import java.util.List;

class User {
    private String name;
    private List<String> messages;

    User(String name, List<String> messages) {
        this.name = name;
        this.messages = messages;
    }

    String getName() {
        return name;
    }
}
