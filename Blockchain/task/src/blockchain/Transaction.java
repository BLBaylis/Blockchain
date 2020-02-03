package blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private long id;
    private User buyer;
    private User seller;
    private int amount;
    private boolean isSigned = false;
    private PublicKey key;
    private List<byte[]> transactionData;

    Transaction(long id, User buyer, User seller, int amount, PublicKey key) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.amount = amount;
        this.key = key;
        transactionData = new ArrayList<>();
        transactionData.add(toString().getBytes());
    }

    List<byte[]> getTransactionData() {
        return transactionData;
    }

    User getBuyer() {
        return buyer;
    }

    long getId() {
        return id;
    }

    PublicKey getPublicKey() {
        return key;
    }

    void sign(PrivateKey privateKey) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(privateKey);
            rsa.update(transactionData.get(0));
            transactionData.add(rsa.sign());
            isSigned = true;
        } catch (Exception e) {
            System.out.println("Unable to sign transaction");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("%s [User Id: %d] sent %d to %s [User Id: %d]",
                buyer.getName(),
                buyer.getId(),
                amount,
                seller.getName(),
                seller.getId()
        );
    }
}
