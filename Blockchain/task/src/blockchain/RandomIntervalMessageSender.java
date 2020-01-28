package blockchain;

import java.util.Arrays;
import java.util.Random;

class RandomIntervalMessageSender extends Thread {
    private Blockchain blockchain = Blockchain.getInstance();
    private volatile boolean isRunning = true;

    @Override
    public void run() {
        while (isRunning) {
            int delay = (new Random().nextInt(5)) * 10;
            try {
                Thread.sleep(delay);
                Message message = MessageGenerator.generateRandomMessage();
                blockchain.sendMessage(message);
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
