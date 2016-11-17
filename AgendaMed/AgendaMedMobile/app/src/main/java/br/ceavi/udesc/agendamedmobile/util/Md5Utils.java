package br.ceavi.udesc.agendamedmobile.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Samuel Felício Adriano
 */
public class Md5Utils {

    public static String toMd5(String str) {
        MessageDigest mDigest;

        try {
            mDigest = MessageDigest.getInstance("MD5");

            byte[] MD5value = mDigest.digest(str.getBytes("UTF-8"));

            StringBuffer sb = new StringBuffer();

            for(byte b : MD5value) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}