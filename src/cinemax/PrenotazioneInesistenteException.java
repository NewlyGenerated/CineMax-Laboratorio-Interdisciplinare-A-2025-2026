// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * Eccezione sollevata quando un utente prova a cercare una prenotazione non esistente.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class PrenotazioneInesistenteException extends RuntimeException {
    /**
     * Costruisce una nuova eccezione con il messaggio di dettaglio specificato.
     * @param message il messaggio che descrive la causa dell'eccezione
     */
    public PrenotazioneInesistenteException(String message) {
        super(message);
    }
}
