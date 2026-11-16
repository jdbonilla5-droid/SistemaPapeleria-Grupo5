
package com.papeleria.seguridad;

import java.security.SecureRandom;
import java.security.GeneralSecurityException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SeguridadClave {

    private static final int ITERACIONES = 600000;
    private static final int LONGITUD_HASH = 256;
    private static final int LONGITUD_SAL = 16;

    public static String generarHash(char[] clave) {

        byte[] sal = new byte[LONGITUD_SAL];
        new SecureRandom().nextBytes(sal);

        try {
            byte[] hash = derivarClave(
                    clave, sal, ITERACIONES
            );

            return "PBKDF2-SHA256$"
                    + ITERACIONES + "$"
                    + Base64.getEncoder().encodeToString(sal)
                    + "$"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "No se pudo proteger la contraseña.", e
            );
        }
    }

    public static boolean verificar(
            char[] clave, String hashGuardado) {

        try {
            String[] partes = hashGuardado.split("\\$");

            if (partes.length != 4
                    || !partes[0].equals("PBKDF2-SHA256")) {
                return false;
            }

            int iteraciones = Integer.parseInt(partes[1]);

            if (iteraciones < 100000
                    || iteraciones > 2000000) {
                return false;
            }

            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);

            if (sal.length != LONGITUD_SAL
                    || esperado.length != LONGITUD_HASH / 8) {
                return false;
            }

            byte[] obtenido = derivarClave(
                    clave, sal, iteraciones
            );

            return java.security.MessageDigest.isEqual(
                    esperado, obtenido
            );

        } catch (GeneralSecurityException
                | IllegalArgumentException
                | NullPointerException e) {
            return false;
        }
    }

    private static byte[] derivarClave(
            char[] clave, byte[] sal, int iteraciones)
            throws GeneralSecurityException {

        PBEKeySpec especificacion = new PBEKeySpec(
                clave, sal, iteraciones, LONGITUD_HASH
        );

        try {
            SecretKeyFactory fabrica =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256"
                    );

            return fabrica.generateSecret(
                    especificacion
            ).getEncoded();

        } finally {
            especificacion.clearPassword();
        }
    }
}
