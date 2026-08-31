// Kejsi Xhafaj, 759934, VA
package cinemax;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * La classe comprende tutti quei metodi che servono allo svolgimento di operazioni
 * quali hashing di password e verifiche per gli accessi al sistema.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
final class Sicurezza {
    /**
     * L'attributo <code>ALGORITMO</code> definisce l'algoritmo crittografico utilizzato per generare l'hash delle password.
     */
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    /**
     * L'attributo <code>LUNGHEZZA_SALT</code> specifica la dimensione in byte del salt casuale generato per aumentare la sicurezza dell'hash.
     */
    private static final int LUNGHEZZA_SALT = 16;
    /**
     * L'attributo <code>LUNGHEZZA_BIT_OUTPUT</code> indica la lunghezza in bit della chiave derivata (hash finale) generata dall'algoritmo.
     */
    private static final int LUNGHEZZA_BIT_OUTPUT = 256;
    /**
     * L'attributo <code>ITERAZIONI</code> stabilisce il numero di iterazioni dell'algoritmo di hashing, utile per rallentare eventuali attacchi di tipo brute force.
     */
    private static final int ITERAZIONI = 620000;

    /**
     * Costruttore privato per prevenire l'istanziazione di questa classe di utilità.
     */
    private Sicurezza() {
    }

    /**
     * Genera un salt casuale e sicuro utilizzando SecureRandom.
     * @return un array di byte contenente il salt generato
     */
    public static byte[] generaSalt() {
        byte[] salt = new byte[LUNGHEZZA_SALT];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Calcola l'hash di una password in chiaro utilizzando un algoritmo di hashing sicuro.
     * @param passwordInChiaro la password in chiaro da hashare.
     * @param salt             il salt casuale da applicare all'hashing
     * @param iterazioni       il numero di iterazioni da eseguire per l'algoritmo
     * @return l'array di byte rappresentante l'hash della password
     * @throws IllegalStateException se si verifica un errore crittografico durante la generazione dell'hash
     */
    public static byte[] hashPassword(char[] passwordInChiaro, byte[] salt, int iterazioni) {
        PBEKeySpec spec = new PBEKeySpec(passwordInChiaro, salt, iterazioni, LUNGHEZZA_BIT_OUTPUT);
        try {
            SecretKeyFactory generatore = SecretKeyFactory.getInstance(ALGORITMO);
            return generatore.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Errore hashing password.", e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Verifica se la password in chiaro fornita corrisponde all'hash memorizzato.
     * @param passwordInChiaro   la password dell'utente inserita in fase di accesso
     * @param salt               il salt associato all'utente
     * @param hashPrevisto       l'hash della password associato all'utente
     * @param iterazioniPreviste il numero di iterazioni usate per la creazione dell'hash previsto
     * @return vero se la password fornita produce lo stesso hash, falso altrimenti
     */
    public static boolean verificaLogin(char[] passwordInChiaro, byte[] salt, byte[] hashPrevisto, int iterazioniPreviste) {
        byte[] passwordHash = hashPassword(passwordInChiaro, salt, iterazioniPreviste);
        return MessageDigest.isEqual(passwordHash, hashPrevisto);
    }

    /**
     * Codifica un array di byte in una stringa in formato Base64.
     * @param formaByte l'array di byte da codificare
     * @return una stringa in formato Base64
     */
    public static String aBase64(byte[] formaByte) {
        return Base64.getEncoder().encodeToString(formaByte);
    }

    /**
     * Decodifica una stringa in formato Base64 nel suo array di byte.
     * @param formaStringa la stringa in formato Base64 da decodificare
     * @return un array di byte
     */
    public static byte[] daBase64(String formaStringa) {
        return Base64.getDecoder().decode(formaStringa);
    }

    /**
     * Restituisce il numero di iterazioni utilizzate per l'algoritmo di hashing.
     * @return il numero di iterazioni
     */
    public static int getIterazioni() {
        return ITERAZIONI;
    }
}
