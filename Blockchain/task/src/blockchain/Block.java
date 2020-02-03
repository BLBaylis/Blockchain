package blockchain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

class Block implements Serializable {
    int id;
    String hash;
    String prevHash;
    Block prevBlock;
    private long magicNumber;
    private long timeStamp;
    private Long generationTime;
    private long minerId;
    private String difficultyChangeMessage;
    private List<Transaction> transactions;

    Block(Block prevBlock,
          long magicNumber,
          String hash,
          long minerId,
          List<Transaction> transactions,
          long generationTime,
          String difficultyChangeMessage
    ) {
        timeStamp = new Date().getTime();
        this.prevBlock = prevBlock;
        id = prevBlock == null ? 1 : prevBlock.id + 1;
        prevHash = prevBlock == null ? "0" : prevBlock.hash;
        this.magicNumber = magicNumber;
        this.minerId = minerId;
        this.transactions = transactions;
        this.hash = hash;
        this.generationTime = generationTime;
        this.difficultyChangeMessage = difficultyChangeMessage;
    }

    void print() {
        System.out.println();
        System.out.println("Block:");
        System.out.println("Created by miner # " + minerId);
        System.out.println("Id: " + id);
        System.out.println("Timestamp: " + timeStamp);
        System.out.println("Magic number: " + magicNumber);
        System.out.println("Hash of the previous block:");
        System.out.println(prevHash);
        System.out.println("Hash of the block:");
        System.out.println(hash);
        System.out.println("Block data:");
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No transactions");
        } else {
            transactions.stream().map(Transaction::toString).forEach(System.out::println);
        }
        System.out.println("Block was generating for " + generationTime + " milliseconds");
        System.out.println(difficultyChangeMessage);
    }
}
