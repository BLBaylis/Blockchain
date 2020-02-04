package blockchain;

import java.security.KeyPair;
import java.util.concurrent.ExecutorService;

class Miner extends User {
    private ExecutorService minerExecutor;

    Miner(long id, String name, KeyPair keyPair, ExecutorService minerExecutor) {
        super(id, name, keyPair);
        this.minerExecutor = minerExecutor;
    }

    void mine() {
        minerExecutor.submit(new MiningRunnable(this));
    }
}
