package agorbahn.peer_to_peer.adapters;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import agorbahn.peer_to_peer.Constants;

/**
 * Created by Adam on 12/20/2016.
 */

public class BlowfishEncryption {
    public String encryptBlowfish(String to_encrypt, String strkey) {
        try {
            SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), Constants.KEY);
            Cipher cipher = Cipher.getInstance(Constants.KEY);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(cipher.doFinal(to_encrypt.getBytes()));
        } catch (Exception e) { return null; }
    }

    public String decryptBlowfish(String to_decrypt, String strkey) {
        try {
            SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), Constants.KEY);
            Cipher cipher = Cipher.getInstance(Constants.KEY);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(to_decrypt.getBytes());
            return new String(decrypted);
        } catch (Exception e) { return null; }
    }

    public String randomKey() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String key = sb.toString();
        return key;
    }
}
