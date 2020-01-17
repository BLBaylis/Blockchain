package blockchain;

//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    /*public static Blockchain deserialize() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("./blockchain.ser");
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return (Blockchain) obj;
    }*/

    private static int getNumOfLeadingZeros() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try (scanner) {
                System.out.println("Enter how many zeros the hash must starts with: ");
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.print("Only numbers are allowed");
                scanner.nextLine();
            }
        }
    }

    public static void main(String[] args) /*throws IOException, ClassNotFoundException*/ {
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
        Blockchain blockchain = new Blockchain(getNumOfLeadingZeros());
        //}
        for (int i = 0; i < 5; i++) {
            blockchain.addNewBlock();
        }
        if (blockchain.validate()) {
            blockchain.print();
        } else {
            System.out.print("Blockchain invalid");
        }


    }
}

