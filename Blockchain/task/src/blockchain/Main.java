package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Blockchain blockchain = Blockchain.getInstance();
        RandomIntervalMessageSender messageSender = new RandomIntervalMessageSender();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        IntStream.rangeClosed(1, 10).forEach(minerId -> executor.submit(new Miner(minerId)));
        messageSender.start();
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        messageSender.end();
        if (blockchain.validate()) {
            blockchain.print();
        } else {
            System.out.println("Blockchain invalid!");
        }
    }
}
