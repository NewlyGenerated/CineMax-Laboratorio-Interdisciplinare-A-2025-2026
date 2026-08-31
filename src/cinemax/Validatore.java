// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * La classe fornisce metodi di utilità per la validazione dei dati, come stringhe od oggetti.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
final class Validatore {
    /**
     * Costruttore privato per prevenire l'istanziazione di questa classe di utilità.
     */
    private Validatore() {
    }

    /**
     * Valida la stringa inserita come parametro assicurandosi che non sia nulla o vuota.
     * @param stringaInserita la stringa da validare
     * @param messaggioErrore il messaggio passato al costruttore dell'eccezione
     * @throws IllegalArgumentException se la stringa inserita è nulla o vuota
     */
    public static void validaStringa(String stringaInserita, String messaggioErrore) {
        if (stringaInserita == null || stringaInserita.isBlank()) {
            throw new IllegalArgumentException(messaggioErrore);
        }
    }

    /**
     * Valida un oggetto generico assicurandosi che non sia nullo.
     * @param oggetto         l'oggetto da validare
     * @param messaggioErrore il messaggio dell'eccezione in caso di oggetto nullo
     * @throws IllegalArgumentException se l'oggetto passato è nullo
     */
    public static void validaOggetto(Object oggetto, String messaggioErrore) {
        if (oggetto == null) {
            throw new IllegalArgumentException(messaggioErrore);
        }
    }

    /**
     * Valida un oggetto Utente assicurandosi che non sia nullo, richiamando il metodo generico.
     * @param utente l'oggetto Utente da validare
     * @throws IllegalArgumentException se l'oggetto passato è nullo
     */
    public static void validaOggetto(Utente utente) {
        validaOggetto(utente, "L'utente non può essere nullo.");
    }

    /**
     * Valida un oggetto Proiezione assicurandosi che non sia nullo, richiamando il metodo generico.
     * @param proiezione l'oggetto Proiezione da validare
     * @throws IllegalArgumentException se l'oggetto passato è nullo
     */
    public static void validaOggetto(Proiezione proiezione) {
        validaOggetto(proiezione, "La proiezione non può essere nulla.");
    }

    /**
     * Valida un oggetto Film assicurandosi che non sia nullo, richiamando il metodo generico.
     * @param film l'oggetto Film da validare
     * @throws IllegalArgumentException se l'oggetto passato è nullo
     */
    public static void validaOggetto(Film film) {
        validaOggetto(film, "Il film non può essere nullo.");
    }

    /**
     * Valida un oggetto Prenotazione assicurandosi che non sia nullo, richiamando il metodo generico.
     * @param prenotazione l'oggetto Prenotazione da validare
     * @throws IllegalArgumentException se l'oggetto passato è nullo
     */
    public static void validaOggetto(Prenotazione prenotazione) {
        validaOggetto(prenotazione, "La prenotazione non può essere nulla.");
    }

    /**
     * Valida un oggetto Ruolo assicurandosi che non sia nullo, richiamando il metodo generico.
     * @param ruolo l'oggetto Ruolo da validare
     * @throws IllegalArgumentException se l'oggetto passato è nullo
     */
    public static void validaOggetto(Ruolo ruolo) {
        validaOggetto(ruolo, "Il ruolo dell'utente non può essere nullo.");
    }

    /**
     * Valida un oggetto Genere assicurandosi che non sia nullo, richiamando il metodo generico.
     * @param genere l'oggetto Genere da validare
     * @throws IllegalArgumentException se l'oggetto passato è nullo
     */
    public static void validaOggetto(Genere genere) {
        validaOggetto(genere, "Il genere del film non può essere nullo.");
    }

    /**
     * Valida un oggetto GestoreDati assicurandosi che non sia nullo, richiamando il metodo generico.
     * @param gestoreDati l'oggetto GestoreDati da validare
     * @throws IllegalArgumentException se l'oggetto passato è nullo
     */
    public static void validaOggetto(GestoreDati gestoreDati) {
        validaOggetto(gestoreDati, "Il gestore dati del sistema non può essere nullo.");
    }
}
