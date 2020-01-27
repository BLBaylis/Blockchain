package blockchain;

//import java.io.*;
import com.sun.nio.sctp.AbstractNotificationHandler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

class Blockchain /*implements Serializable*/ {
    //private static final long serialVersionUID = 1L;
    private static Blockchain instance = new Blockchain();
    private volatile Block latestBlock;
    private volatile int size;
    private int numOfLeadingZeros = 0;
    private List<Message> messagesForNextBlock = new ArrayList<>(20);

    private Blockchain() { }

    static Blockchain getInstance() {
        return instance;
    }

    Block getLatestBlock() {
        return latestBlock;
    }

    int getSize() {
        return size;
    }

    /*private void serialize() throws IOException {
        FileOutputStream fos = new FileOutputStream("./blockchain.ser");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        oos.close();
    }*/

    synchronized void message(Message message) {
        messagesForNextBlock.add(message);
    }

    boolean validate() {
        Block currBlock = latestBlock;
        if (currBlock == null) {
            return false;
        }
        while (!"0".equals(currBlock.prevHash) && currBlock.prevBlock != null) {
            Block prevBlock = currBlock.prevBlock;
            if (!currBlock.prevHash.equals(prevBlock.hash)) {
                return false;
            }
            currBlock = prevBlock;
        }
        return true;
    }

    private boolean validateBlock(Block submittedBlock) {
        String hash = submittedBlock.hash;
        String prevHash = latestBlock == null ? "0" : latestBlock.hash;
        String hashPrefix = createHashPrefix();
        if (!HashUtils.createBlockHash(submittedBlock.magicNumber, prevHash).equals(hash)) {
            return false;
        }
        return submittedBlock.prevHash.equals(prevHash) && HashUtils.isValidHash(hash, hashPrefix);
    }

    private void updateMiningDifficulty(long generationTime) {
        if (generationTime < 10) {
            numOfLeadingZeros++;
        } else if (generationTime > 15) {
            numOfLeadingZeros--;
        }
    }

    private String createDifficultyChangeMessage(int prevDifficulty) {
        if (prevDifficulty < numOfLeadingZeros) {
            return "N was decreased by 1";
        } else if (prevDifficulty == numOfLeadingZeros) {
            return "N stays the same";
        } else {
            return "N was increased by 1";
        }
    }

    synchronized void addNewBlock(Block submittedBlock) /*throws IOException*/ {
        if (validateBlock(submittedBlock)) {
            int prevDifficulty = numOfLeadingZeros;
            updateMiningDifficulty(submittedBlock.generationTime);
            submittedBlock.difficultyChangeMessage = createDifficultyChangeMessage(prevDifficulty);
            submittedBlock.print();
            latestBlock = submittedBlock;
            size++;
            //serialize();
        }
    }

    String createHashPrefix() {
        StringBuilder str = new StringBuilder();
        while (str.length() < numOfLeadingZeros) {
            str.append("0");
        }
        return str.toString();
    }

    void print() {
        Deque<Block> queue = new ArrayDeque<>(5);
        Block currBlock = latestBlock;
        while (currBlock != null && queue.size() < 5) {
            if (currBlock.id < 6) {
                queue.addFirst(currBlock);
            }
            currBlock = currBlock.prevBlock;
        }
        while (!queue.isEmpty()) {
            Block blockToPrint = queue.removeFirst();
            blockToPrint.print();
        }
    }
}
