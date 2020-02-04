package blockchain;

import java.security.PublicKey;
import java.security.Signature;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

class Blockchain {

    private final static Blockchain instance = new Blockchain();
    private static long transactionId = 0;
    private static long userId = 0;
    private final int targetBlockchainSize = 15;
    private final long targetGenerationTime = 100;
    private volatile Block latestBlock;
    private int size;
    private int numOfLeadingZeros = 0;
    private Instant blockCreationTimerStart = Instant.now();
    private List<Transaction> prevTransactions;
    private List<Transaction> newTransactions = new ArrayList<>();

    private Blockchain() { }

    static Blockchain getInstance() {
        return instance;
    }

    static synchronized long getUserId() {
        return ++userId;
    }

    static synchronized long getTransactionId() {
        return ++transactionId;
    }

    Block getLatestBlock() {
        return latestBlock;
    }

    int getSize() {
        return size;
    }

    int getTargetBlockchainSize() {
        return targetBlockchainSize;
    }

    int getNumOfLeadingZeros() {
        return numOfLeadingZeros;
    }

    private static boolean verifySignature(Transaction transaction) {
        PublicKey publicKey = transaction.getBuyer().getPublicKey();
        List<byte[]> transactionData = transaction.getTransactionData();
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update(transactionData.get(0));
            return sig.verify(transactionData.get(1));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    List<Transaction> getPrevTransactions() {
        return prevTransactions == null ? null : new ArrayList<>(prevTransactions);
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

    private boolean validateBlock(String hash) {
        String hashPrefix = HashUtils.createHashPrefix(numOfLeadingZeros);
        return HashUtils.isValidHash(hash, hashPrefix);
    }

    int getBalanceFromId(long userId) {
        User user = UserDatabase.getUser(userId);
        List<Transaction> userTransactions = new ArrayList<>();
        //everyone gets a free 100VC for testing purposes
        int minedCurrency = 100;
        Block currBlock = latestBlock;
        while (currBlock != null) {
            List<Transaction> blockTransactions = currBlock.getTransactions();
            if (blockTransactions != null) {
                userTransactions.addAll(blockTransactions);
            }
            if (currBlock.getMiner() == user) {
                minedCurrency += 100;
            }
            currBlock = currBlock.prevBlock;
        }
        Map<Boolean, Integer> totals = userTransactions.stream()
                .filter(transaction -> transaction.getBuyer() == user || transaction.getSeller() == user)
                .collect(
                        Collectors.partitioningBy(transaction -> transaction.getSeller() == user,
                                Collectors.summingInt(Transaction::getAmount))
                );
        return totals.get(true) - totals.get(false) + minedCurrency;
    }

    synchronized void sendTransaction(Transaction transaction) {
        User buyer = transaction.getBuyer();
        long buyerId = buyer.getId();

        //TODO:
        //  message verification from stage 5
        if (/*transaction.getId() >= transactionId || */buyer.getPublicKey() == transaction.getPublicKey()
                && verifySignature(transaction) && transaction.getAmount() <= getBalanceFromId(buyerId)) {
            newTransactions.add(transaction);
        }
    }

    synchronized void submit(long magicNumber, User miner) {
        String hash = HashUtils.createBlockHash(magicNumber, latestBlock == null ? "0" : latestBlock.hash, prevTransactions);
        if (validateBlock(hash)) {
            long generationTime = Duration.between(blockCreationTimerStart, Instant.now()).toMillis();
            DifficultyChange difficultyChange = DifficultyChange.findDifficultyChange(
                    generationTime,
                    targetGenerationTime
            );
            if (difficultyChange == DifficultyChange.INCREASE) {
                numOfLeadingZeros++;
            } else if (difficultyChange == DifficultyChange.DECREASE) {
                numOfLeadingZeros--;
            }
            addNewBlock(new Block(
                    latestBlock,
                    magicNumber,
                    hash,
                    miner,
                    prevTransactions,
                    generationTime,
                    difficultyChange.getMessage())
            );
        }
    }

    private void addNewBlock(Block newBlock) {
        latestBlock = newBlock;
        prevTransactions = newTransactions;
        newTransactions = new ArrayList<>();
        blockCreationTimerStart = Instant.now();
        size++;
    }

    void print() {
        Deque<Block> queue = new ArrayDeque<>(targetBlockchainSize);
        Block currBlock = latestBlock;
        while (currBlock != null && queue.size() < targetBlockchainSize) {
            if (currBlock.id < targetBlockchainSize + 1) {
                queue.addFirst(currBlock);
            }
            currBlock = currBlock.prevBlock;
        }
        while (!queue.isEmpty()) {
            Block blockToPrint = queue.removeFirst();
            blockToPrint.print();
        }
    }

    enum DifficultyChange {
        INCREASE("N was increased by 1"),
        DECREASE("N was decreased by 1"),
        NO_CHANGE("N stays the same");

        private final String message;

        DifficultyChange(String message) {
            this.message = message;
        }

        static DifficultyChange findDifficultyChange(long generationTime, long targetGenerationTime) {
            if (generationTime == targetGenerationTime) {
                return NO_CHANGE;
            }
            return generationTime < targetGenerationTime ? INCREASE : DECREASE;
        }

        public String getMessage() {
            return message;
        }
    }
}
