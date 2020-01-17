package blockchain;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

class Block implements Serializable {
    int id;
    String hash;
    String prevHash;
    Block prevBlock;
    private String magicNumber;
    private long timeStamp;
    private long generationTime;

    Block(int numOfLeadingZeros, Block prevBlock) {
        timeStamp = new Date().getTime();
        this.prevBlock = prevBlock;
        id = prevBlock == null ? 1 : prevBlock.id + 1;
        prevHash = prevBlock == null ? "0" : prevBlock.hash;

        //gen new hash
        Instant start = Instant.now();
        magicNumber = findMagicNumber(numOfLeadingZeros);
        hash = Hash.applySha256(magicNumber + timeStamp + prevHash);
        Instant end = Instant.now();


        Duration timeElapsed = Duration.between(start, end);
        generationTime = timeElapsed.toSeconds();

    }

    private String findMagicNumber(int numOfZeros) {
        String hash;
        String startHash = createRequiredStringPrefix(numOfZeros);
        long magicNumber = generateNewMagicNumber();
        String magicNumberStr;
        do {
            magicNumber++;
            magicNumberStr = Long.toString(magicNumber);
            hash = Hash.applySha256(magicNumberStr + timeStamp + prevHash);
        } while (!hash.substring(0, numOfZeros).equals(startHash));
        return magicNumberStr;
    }

    private static String createRequiredStringPrefix(int numOfZeros) {
        StringBuilder str = new StringBuilder();
        while (str.length() < numOfZeros) {
            str.append("0");
        }
        return str.toString();
    }

    private static long generateNewMagicNumber() {
        Random generator = new Random();
        long magicNumber = generator.nextLong();
        while (magicNumber < 0 || magicNumber > Long.MAX_VALUE / 2) {
            magicNumber = generator.nextLong();
        }
        return magicNumber;
    }

    void print() {
        System.out.println();
        System.out.println("Block:");
        System.out.println("Id: " + id);
        System.out.println("Timestamp: " + timeStamp);
        System.out.println("Magic number: " + magicNumber);
        System.out.println("Hash of the previous block:");
        System.out.println(prevHash);
        System.out.println("Hash of the block:");
        System.out.println(hash);
        System.out.println("Block was generating for " + generationTime + " seconds");
    }
}
