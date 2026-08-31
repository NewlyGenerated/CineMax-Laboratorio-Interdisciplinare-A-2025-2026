// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * Eccezione sollevata quando l'utente prova a prenotare un posto, ma i posti all'interno della sala per
 * quella specifica proiezione sono esauriti.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class PostiEsauritiException extends RuntimeException {
    /**
     * Costruisce una nuova eccezione con il messaggio di dettaglio specificato.
     * @param message il messaggio che descrive la causa dell'eccezione
     */
    public PostiEsauritiException(String message) {
        super(message);
    }
}
