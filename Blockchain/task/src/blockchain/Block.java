package blockchain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

class Block implements Serializable {
    int id;
    String hash;
    String prevHash;
    Block prevBlock;
    long magicNumber;
    private long timeStamp;
    long generationTime;
    private int minerId;
    String difficultyChangeMessage;
    private List<Message> messages;

    Block(List<Message> messages, Block prevBlock, long magicNumber, long generationTime, int minerId) {
        this.messages = messages;
        timeStamp = new Date().getTime();
        this.prevBlock = prevBlock;
        id = prevBlock == null ? 1 : prevBlock.id + 1;
        prevHash = prevBlock == null ? "0" : prevBlock.hash;
        this.magicNumber = magicNumber;
        this.generationTime = generationTime;
        this.minerId = minerId;
        this.hash = HashUtils.createBlockHash(magicNumber, prevHash);
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
        if (messages == null) {
            System.out.println("No messages");
        } else {
            messages.stream().map(Message::toString).forEach(System.out::println);
        }
        System.out.println("Block was generating for " + generationTime + " seconds");
        System.out.println(difficultyChangeMessage);
    }
}
