// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * Eccezione sollevata quando un proiezionista prova ad aggiungere una proiezione ma quest'ultima va a sovrapporsi
 * temporalmente con un'altra già esistente.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class SovrapposizioneProiezioneException extends RuntimeException {
    /**
     * Costruisce una nuova eccezione con il messaggio di dettaglio specificato.
     * @param message il messaggio che descrive la causa dell'eccezione
     */
    public SovrapposizioneProiezioneException(String message) {
        super(message);
    }
}
