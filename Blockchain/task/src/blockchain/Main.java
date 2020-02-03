package blockchain;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.getInstance();
        String[] userNames = new String[]{"Brad", "Sam", "Gill", "Trev", "Jazz"};
        ExecutorService messengerExecutor = Executors.newFixedThreadPool(userNames.length);
        Arrays.stream(userNames)
                .map(userName -> new User(blockchain.getUserId(), userName, new KeyGenerator().getKeyPair()))
                .forEach(user -> messengerExecutor.submit(new RandomIntervalMessageSender(user)));
        ExecutorService minerExecutor = Executors.newFixedThreadPool(10);
        IntStream.rangeClosed(1, 10).forEach(minerId -> minerExecutor.submit(new Miner(minerId)));
        minerExecutor.shutdown();
        try {
            minerExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        messengerExecutor.shutdown();
        if (blockchain.validate()) {
            blockchain.print();
        } else {
            System.out.println("Blockchain invalid!");
        }
    }
}
