package blockchain;

import java.util.Scanner;

public class Main {

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

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain(getNumOfLeadingZeros());
        for (int i = 0; i < 5; i++) {
            blockchain.addNewBlock();
        }
        if (blockchain.validateBlockchain()) {
            blockchain.print();
        } else {
            System.out.println("Blockchain invalid");
        }
    }
}
