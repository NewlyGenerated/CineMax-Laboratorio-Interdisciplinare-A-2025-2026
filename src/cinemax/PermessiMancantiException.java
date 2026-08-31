// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * Eccezione sollevata quando un utente prova a eseguire un'operazione non consentita per il suo ruolo.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class PermessiMancantiException extends RuntimeException {
    /**
     * Costruisce una nuova eccezione con il messaggio di dettaglio specificato.
     * @param message il messaggio che descrive la causa dell'eccezione
     */
    public PermessiMancantiException(String message) {
        super(message);
    }
}
