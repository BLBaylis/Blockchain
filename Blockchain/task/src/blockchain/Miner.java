package blockchain;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class Miner extends Thread {
    private Blockchain blockchain;
    private Block latestBlock;
    private int id;

    Miner(Blockchain blockchain, int id) {
        this.blockchain = blockchain;
        latestBlock = blockchain.getLatestBlock();
        this.id = id;
    }

    @Override
    public void run() {
        String hashPrefix = blockchain.createHashPrefix();
        while (blockchain.getSize() < 5) {
            Instant start = Instant.now();
            long nonce = generateNonce();
            long magicNumber = findMagicNumber(nonce, hashPrefix);
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            long generationTime = timeElapsed.toSeconds();
            publishBlock(magicNumber, generationTime);
        }
    }

    private void publishBlock(long magicNumber, long generationTime) {
        if (latestBlock != blockchain.getLatestBlock()) {
            return;
        }
        blockchain.addNewBlock(new Block(latestBlock, magicNumber, generationTime, id));
    }

    private long findMagicNumber(long nonce, String hashPrefix) {
        long magicNumber = nonce;
        while (blockchain.getSize() < 5) {
            magicNumber++;
            latestBlock = blockchain.getLatestBlock();
            String prevHash = latestBlock == null ? "0" : latestBlock.hash;
            String hash = HashUtils.createBlockHash(magicNumber, prevHash);
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
