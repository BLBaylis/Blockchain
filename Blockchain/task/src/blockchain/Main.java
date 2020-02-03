package blockchain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.getInstance();
        List<String> minerNames = IntStream.rangeClosed(1, 10)
                .boxed()
                .map(minerId -> "Miner " + minerId)
                .collect(Collectors.toList());
        List<String> userNames = new ArrayList<>(Arrays.asList("Brad", "Sam", "Gill", "Trev", "Jazz"));
        userNames.addAll(minerNames);
        ExecutorService TransactionExecutor = Executors.newFixedThreadPool(userNames.size());
        userNames.stream()
                .map(userName -> new User(Blockchain.getUserId(), userName, new KeyGenerator().getKeyPair()))
                .forEach(user -> TransactionExecutor.submit(new RandomIntervalTransactionSender(user)));
        ExecutorService minerExecutor = Executors.newFixedThreadPool(minerNames.size());
        minerNames.stream()
                .map(minerName -> new User(Blockchain.getUserId(), minerName, new KeyGenerator().getKeyPair()))
                .forEach(user -> minerExecutor.submit(new Miner(user)));
        minerExecutor.shutdown();
        try {
            minerExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        TransactionExecutor.shutdown();
        if (blockchain.validate()) {
            blockchain.print();
        } else {
            System.out.println("Blockchain invalid!");
        }
    }
}
