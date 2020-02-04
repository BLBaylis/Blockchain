package blockchain;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static List<Miner> createMiners(int amount, ExecutorService executorService) {
        return IntStream.rangeClosed(1, amount)
                .boxed()
                .map(minerId -> new Miner(
                                Blockchain.getUserId(),
                                "Miner " + minerId,
                                new KeyGenerator().getKeyPair(),
                                executorService
                        )
                )
                .collect(Collectors.toList());
    }

    private static List<User> createUsers(String... names) {
        return Stream.of(names)
                .map(userName -> new User(Blockchain.getUserId(), userName, new KeyGenerator().getKeyPair()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.getInstance();
        ExecutorService minerExecutor = Executors.newFixedThreadPool(10);
        List<Miner> miners = createMiners(10, minerExecutor);
        List<User> users = createUsers("Brad", "Sam", "Gill", "Trev", "Jazz");
        users.addAll(miners);
        users.forEach(UserDatabase::addUser);
        ExecutorService TransactionExecutor = Executors.newFixedThreadPool(users.size());
        users.forEach(user -> TransactionExecutor.submit(new RandomIntervalTransactionSender(user)));
        miners.forEach(Miner::mine);
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
