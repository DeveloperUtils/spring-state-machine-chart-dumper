package net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.impl.w3m;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Christoph Graupner on 8/27/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class Sha1Id implements IId {
    String fHash;

    public Sha1Id(String aS) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        fHash = sha1(aS);
    }

    private static String sha1(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes("UTF-8"));

        return new BigInteger(1, crypt.digest()).toString(16);
    }

    @Override
    public String toString() {
        return fHash;
    }
}
