// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * Eccezione sollevata quando un utente tenta di prenotare un film ma possiede un'età inferiore
 * rispetto all'età minima consigliata per la visione della pellicola.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class EtaMinimaConsentitaException extends RuntimeException {
    /**
     * Costruisce una nuova eccezione con il messaggio di dettaglio specificato.
     * @param message il messaggio che descrive la causa dell'eccezione
     */
    public EtaMinimaConsentitaException(String message) {
        super(message);
    }
}
