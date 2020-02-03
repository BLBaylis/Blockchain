package blockchain;

import java.security.PublicKey;
import java.security.Signature;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

class Blockchain {
    private final static Blockchain instance = new Blockchain();
    private int messageId = 0;
    private int userId = 0;
    private final int targetBlockchainSize = 6;
    private final long targetGenerationTime = 1000;
    private volatile Block latestBlock;
    private int size;
    private int numOfLeadingZeros = 0;
    private Instant blockCreationTimerStart = Instant.now();
    private List<Message> prevMessages;
    private List<Message> newMessages = new ArrayList<>();

    private Blockchain() { }

    static Blockchain getInstance() {
        return instance;
    }

    private static boolean verifySignature(Message message) {
        PublicKey publicKey = message.getSender().getPublicKey();
        List<byte[]> messageData = message.getMessageData();
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update(messageData.get(0));
            return sig.verify(messageData.get(1));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    synchronized int getMessageId() {
        return ++messageId;
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

    List<Message> getPrevMessages() {
        if (prevMessages == null) {
            return null;
        }
        return new ArrayList<>(prevMessages);
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

    synchronized int getUserId() {
        return ++userId;
    }

    void sendMessage(Message message) {
        User sender = message.getSender();
        if (message.getId() >= messageId || sender.getPublicKey() != message.getPublicKey()
                || verifySignature(message)) {
            newMessages.add(message);
        }
    }

    synchronized void submit(long magicNumber, int minerId) {
        String hash = HashUtils.createBlockHash(magicNumber, latestBlock == null ? "0" : latestBlock.hash, prevMessages);
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
                    minerId,
                    prevMessages,
                    generationTime,
                    difficultyChange.getMessage())
            );
        }
    }

    private void addNewBlock(Block newBlock) {
        latestBlock = newBlock;
        prevMessages = newMessages;
        newMessages = new ArrayList<>();
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
