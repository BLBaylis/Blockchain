package blockchain;

import java.util.List;
import java.util.Random;

public class Miner extends Thread {
    //TODO:
    //   separate thread from miner class
    //   add coin reward
    private Blockchain blockchain = Blockchain.getInstance();
    private Block latestBlock;
    private List<Transaction> blockData;
    private User user;

    Miner(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        int targetBlockchainSize = blockchain.getTargetBlockchainSize();
        String hashPrefix = HashUtils.createHashPrefix(blockchain.getNumOfLeadingZeros());
        while (blockchain.getSize() < targetBlockchainSize) {
            long nonce = generateNonce();
            long magicNumber = findMagicNumber(nonce, hashPrefix);
            if (latestBlock != blockchain.getLatestBlock()) {
                continue;
            }
            blockchain.submit(magicNumber, user.getId());
        }
    }

    private void refresh() {
        latestBlock = blockchain.getLatestBlock();
        blockData = blockchain.getPrevTransactions();
    }

    private long findMagicNumber(long nonce, String hashPrefix) {
        long magicNumber = nonce;
        while (true) {
            magicNumber++;
            refresh();
            String prevHash = latestBlock == null ? "0" : latestBlock.hash;
            String hash = HashUtils.createBlockHash(magicNumber, prevHash, blockData);
            if (latestBlock != blockchain.getLatestBlock()) {
                continue;
            }
            if (HashUtils.isValidHash(hash, hashPrefix)) {
                break;
            }
        }
        return magicNumber;
    }

    private static long generateNonce() {
        Random generator = new Random();
        long nonce = generator.nextLong();
        while (nonce < 0 || nonce > Long.MAX_VALUE / 2) {
            nonce = generator.nextLong();
        }
        return nonce;
    }
}
