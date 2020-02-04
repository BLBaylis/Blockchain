package blockchain;

import java.util.HashMap;
import java.util.Map;

class UserDatabase {
    private static Map<Long, User> database = new HashMap<>();

    static void addUser(User user) {
        database.putIfAbsent(user.getId(), user);
    }

    static User getUser(long userId) {
        return database.get(userId);
    }

    static int size() {
        return database.size();
    }
}
