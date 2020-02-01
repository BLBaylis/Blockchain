package blockchain;

import java.security.PublicKey;
import java.security.Signature;
import java.util.List;

public class MessageVerifier {

    static boolean verifySignature(Message message) throws Exception {
        PublicKey publicKey = message.getSender().getPublicKey();
        List<byte[]> messageData = message.getMessageData();
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        sig.update(messageData.get(0));
        return sig.verify(messageData.get(1));
    }

}
