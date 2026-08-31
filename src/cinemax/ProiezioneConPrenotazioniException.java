// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * Eccezione sollevata quando un proiezionista prova a rimuovere una proiezione ma quest'ultima ha associata
 * delle prenotazioni.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class ProiezioneConPrenotazioniException extends RuntimeException {
    /**
     * Costruisce una nuova eccezione con il messaggio di dettaglio specificato.
     * @param message il messaggio che descrive la causa dell'eccezione
     */
    public ProiezioneConPrenotazioniException(String message) {
        super(message);
    }
}
