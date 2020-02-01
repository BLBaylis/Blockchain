package blockchain;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

//TODO: encrypt hashes
public class AsymmetricCryptography {
    private static Cipher cipher;

    public AsymmetricCryptography() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance("RSA");
    }

    public static String encryptText(String msg, PrivateKey key)
            throws IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decryptText(String msg, PublicKey key)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(msg)), StandardCharsets.UTF_8);
    }
}
