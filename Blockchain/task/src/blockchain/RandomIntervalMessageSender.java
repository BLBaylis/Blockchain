package blockchain;

import java.util.Arrays;
import java.util.Random;

class RandomIntervalMessageSender extends Thread {
    private Blockchain blockchain = Blockchain.getInstance();
    private static User[] users = {
            new User("Brad", new KeyGenerator().getKeyPair()),
            new User("Gill", new KeyGenerator().getKeyPair()),
            new User("Trev", new KeyGenerator().getKeyPair()),
            new User("Sam", new KeyGenerator().getKeyPair()),
            new User("Jazz", new KeyGenerator().getKeyPair())
    };
    private boolean isRunning = true;

    @Override
    public void run() {
        Random random = new Random();
        while (isRunning) {
            User user = users[random.nextInt(users.length)];
            int delay = random.nextInt(5) * 100;
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
