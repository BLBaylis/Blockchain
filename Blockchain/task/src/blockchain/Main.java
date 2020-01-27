package blockchain;

//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
        List<User> users = createUsers();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        IntStream.rangeClosed(1, 10).forEach(minerId -> executor.submit(new Miner(blockchain, minerId)));
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    private static List<User> createUsers() {
        String[] bradMessages = {
                "Hi, I'm Brad",
                "The fox in the top hat whispered into the ear of the rabbit",
                "The knives were out and she was sharpening hers",
                "The crowd yells and screams for more memes",
                "I purchased a baby clown from the Russian terrorist black market",
                "I hope that, when I've built up my savings, I'll be able to travel to Mexico",
        };
        User brad = new User("Brad", Arrays.asList(bradMessages));

        String[] trevMessages = {
                "Hi, I'm Trevor",
                "Potato wedges probably are not best for relationships",
                "There is a fly in the car with us",
                "He was 100% into fasting with her until he understood that meant he couldn't eat",
                "If the Easter Bunny and the Tooth Fairy had babies would they take your teeth and " +
                        "leave chocolate for you?",
                "If I don’t like something, I’ll stay away from it."
        };
        User trev = new User("Trev", Arrays.asList(trevMessages));

        String[] gillMessages = {
                "Hi, I'm Gill",
                "Love is not like pizza",
                "Your girlfriend bought your favorite cookie crisp cereal but forgot to get milk",
                "Please wait outside of the house",
                "I hear that Nancy is very pretty",
                "She saw no irony asking me to change but wanting me to accept her for who she is"
        };
        User gill = new User("Gill", Arrays.asList(gillMessages));

        String[] samMessages = {
                "Hi, I'm Sam",
                "It was the scarcity that fueled his creativity",
                "You can't compare apples and oranges, but what about bananas and plantains?",
                "She only paints with bold colors; she does not like pastels",
                "He had reached the point where he was paranoid about being paranoid",
                "Never underestimate the willingness of the greedy to throw you under the bus"
        };
        User sam = new User("Sam", Arrays.asList(samMessages));

        String[] jazzMessages = {
                "Hi, I'm Jazz",
                "Woof!",
                "Treat please"
        };
        User jazz = new User("Jazz", Arrays.asList(jazzMessages));

        return new ArrayList<>(Arrays.asList(brad, trev, gill, sam, jazz));
    }
}
