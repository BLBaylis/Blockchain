package blockchain;

import java.util.Random;

class RandomIntervalTransactionSender implements Runnable {
    private User buyer;

    RandomIntervalTransactionSender(User buyer) {
        this.buyer = buyer;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            int delay = random.nextInt(10) * 200;
            try {
                Thread.sleep(delay);
                buyer.createTransaction();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
