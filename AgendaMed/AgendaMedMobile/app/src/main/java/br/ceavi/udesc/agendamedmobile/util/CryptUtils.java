package br.ceavi.udesc.agendamedmobile.util;

       import android.util.Base64;

       import java.io.UnsupportedEncodingException;
        import java.security.InvalidAlgorithmParameterException;
        import java.security.InvalidKeyException;
        import java.security.NoSuchAlgorithmException;
        //import java.util.Base64;
        import javax.crypto.Cipher;
        import javax.crypto.NoSuchPaddingException;
        import javax.crypto.spec.IvParameterSpec;
        import javax.crypto.spec.SecretKeySpec;

public class CryptUtils {

    private static final String KEY = "^chaveAgendaMed$";
    private static final String IV = "^InitialValue$!!";

    public static String encrypt(String value) {
        try {
            byte[] encrypted = getCipher(Cipher.ENCRYPT_MODE).doFinal(value.getBytes());
            return new String(Base64.encode(encrypted,1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            byte[] original = getCipher(Cipher.DECRYPT_MODE).doFinal(Base64.decode(encrypted,1));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static Cipher getCipher(int cipherMode) throws InvalidKeyException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException {

        IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(cipherMode, skeySpec, iv);

        return cipher;
    }
}
