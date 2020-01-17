package blockchain;

//import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

class Blockchain /*implements Serializable*/ {
    //private static final long serialVersionUID = 1L;
    private Block lastBlock;
    private int numOfLeadingZeros;

    Blockchain(int numOfLeadingZeros) {
        this.numOfLeadingZeros = numOfLeadingZeros;
    }

    /*private void serialize() throws IOException {
        FileOutputStream fos = new FileOutputStream("./blockchain.ser");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        oos.close();
    }*/

    boolean validate() {
        Block currBlock = lastBlock;
        while (!"0".equals(currBlock.prevHash) && currBlock.prevBlock != null) {
            Block prevBlock = currBlock.prevBlock;
            if (!currBlock.prevHash.equals(prevBlock.hash)) {
                return false;
            }
            currBlock = prevBlock;
        }
        return true;
    }

    void addNewBlock() /*throws IOException*/ {
        lastBlock = new Block(numOfLeadingZeros, lastBlock);
        //serialize();
    }

    void print() {
        Deque<Block> queue = new ArrayDeque<>(5);
        Block currBlock = lastBlock;
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
