package blockchain;

//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    /*public static Blockchain deserialize() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("./blockchain.ser");
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return (Blockchain) obj;
    }*/

    public static void main(String[] args) throws InterruptedException /*throws IOException, ClassNotFoundException*/ {
        /*Path path = Paths.get("./blockchain.ser");
        boolean blockchainAlreadyExists = Files.exists(path);
        Blockchain blockchain;
        if (blockchainAlreadyExists) {
            blockchain = deserialize();
            if (!blockchain.validate()) {
                System.out.println("Blockchain invalid");
                return;
            }
        } else {*/
        //Blockchain blockchain = Blockchain.getInstance();
        //}
        Blockchain blockchain = Blockchain.getInstance();
        ExecutorService executor = Executors.newFixedThreadPool(9);
        for (int i = 1; i < 10; i++) {
            executor.submit(new Miner(blockchain, i));
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
}
