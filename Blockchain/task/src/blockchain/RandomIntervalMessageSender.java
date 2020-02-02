package blockchain;

import java.util.Arrays;
import java.util.Random;

class RandomIntervalMessageSender extends Thread {
    private static Blockchain blockchain = Blockchain.getInstance();
    private static User[] users = {
            new User(blockchain.getUserId(), "Brad", new KeyGenerator().getKeyPair()),
            new User(blockchain.getUserId(), "Gill", new KeyGenerator().getKeyPair()),
            new User(blockchain.getUserId(), "Trev", new KeyGenerator().getKeyPair()),
            new User(blockchain.getUserId(), "Sam", new KeyGenerator().getKeyPair()),
            new User(blockchain.getUserId(), "Jazz", new KeyGenerator().getKeyPair())
    };
    private boolean isRunning = true;

    @Override
    public void run() {
        Random random = new Random();
        while (isRunning) {
            User user = users[random.nextInt(users.length)];
            int delay = random.nextInt(3) * 100;
            try {
                Thread.sleep(delay);
                blockchain.sendMessage(user.createMessage());
            } catch (Exception e) {
                System.err.println("Uncaught exception is detected! " + e
                        + " st: " + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    void end() {
        isRunning = false;
    }

}
