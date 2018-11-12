package com.wickedwitch.cryptocurrency;

import java.security.*;
import java.security.spec.ECGenParameterSpec;


public class CryptographyHelper {


    public static String generateHash(String toHash) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA256");
            byte[] hash = digest.digest(toHash.getBytes("UTF-8"));

            StringBuffer hexadecimalBuffer = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hexadecimal = Integer.toHexString(0xff & hash[i]);
                if (hexadecimal.length() == 1) {
                    hexadecimalBuffer.append('0');
                }
                hexadecimalBuffer.append(hexadecimal);
            }

            return hexadecimalBuffer.toString();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static KeyPair elipticCurveCrypto() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec parameterSpec = new ECGenParameterSpec("secp192k1");
            keyPairGenerator.initialize(parameterSpec, secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return keyPair;

        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();

        }
        return null;
    }


    public static byte[] applyECDSASignature(PrivateKey privateKey, String input) {
        Signature signature;
        byte[] output = new byte[0];
        try {
            signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            byte[] strByte = input.getBytes();
            signature.update(strByte);
            byte[] realSignature = signature.sign();
            output = realSignature;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return output;
    }


    public static boolean verifyECDSASignature(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaSignature = Signature.getInstance("ECDSA", "BC");
            ecdsaSignature.initVerify(publicKey);
            ecdsaSignature.update(data.getBytes());
            return ecdsaSignature.verify(signature);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
