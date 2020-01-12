package blockchain;

import java.util.Date;

class Blockchain {
    private Block head;
    private Block lastBlock;
    private int numOfLeadingZeros;

    Blockchain(int numOfLeadingZeros) {
        this.numOfLeadingZeros = numOfLeadingZeros;
        this.lastBlock = new Block(numOfLeadingZeros, null,"0");
        this.head = this.lastBlock;
    }

    static class Block {
        private int id;
        private static int blockNum = 0;
        private String hash;
        private String prevHash;
        private Block nextBlock;
        private Block prevBlock;
        private String magicNumber;
        private long timeStamp;

        Block(int numOfLeadingZeros, Block prevBlock, String prevHash) {
            this.prevBlock = prevBlock;
            this.prevHash = prevHash;
            this.id = ++Block.blockNum;
            this.timeStamp = new Date().getTime();
            this.magicNumber = Hash.findMagicNumber(numOfLeadingZeros);
            this.hash = Hash.applySha256(this.magicNumber);
        }
    }

    boolean validateBlockchain() {
        Block currBlock = lastBlock;
        while (!"0".equals(currBlock.prevHash) || currBlock.prevBlock != null) {
            Block prevBlock = currBlock.prevBlock;
            if (!currBlock.prevHash.equals(prevBlock.hash)) {
                return false;
            }
            currBlock = prevBlock;
        }
        return true;
    }

    void addNewBlock() {
        Block newBlock = new Block(numOfLeadingZeros, lastBlock, lastBlock.hash);
        lastBlock.nextBlock = newBlock;
        lastBlock = newBlock;
    }

    void print() {
        Block currBlock = head;
        while (currBlock.nextBlock != null && currBlock.id < 6) {
            System.out.println();
            System.out.println("Block:");
            System.out.println("Id: " + currBlock.id);
            System.out.println("Timestamp: " + currBlock.timeStamp);
            System.out.println("Magic Number: " + currBlock.magicNumber);
            System.out.println("Hash of the previous block:");
            System.out.println(currBlock.prevHash);
            System.out.println("Hash of the block:");
            System.out.println(currBlock.hash);
            currBlock = currBlock.nextBlock;
        }
    }


}
