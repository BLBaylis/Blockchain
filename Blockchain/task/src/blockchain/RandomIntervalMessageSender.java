package blockchain;

import java.util.Random;

class RandomIntervalMessageSender implements Runnable {
    private static Blockchain blockchain = Blockchain.getInstance();
    private User sender;

    RandomIntervalMessageSender(User sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            int delay = random.nextInt(10) * 200;
            try {
                Thread.sleep(delay);
                blockchain.sendMessage(sender.createMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
