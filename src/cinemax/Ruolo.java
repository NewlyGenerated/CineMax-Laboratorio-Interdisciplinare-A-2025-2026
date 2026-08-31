// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * Rappresenta l'enumerazione che indica uno dei possibili ruoli che un oggetto della classe Utente può assumere.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public enum Ruolo {
    /**
     * La costante <code>PROIEZIONISTA</code> definisce l'utente addetto alla gestione e alla programmazione delle proiezioni.
     */
    PROIEZIONISTA,
    /**
     * La costante <code>CLIENTE</code> definisce l'utente standard fruitore dei servizi del cinema.
     */
    CLIENTE,
    /**
     * La costante <code>BIGLIETTAIO</code> definisce l'utente addetto alla gestione e controllo delle prenotazioni.
     */
    BIGLIETTAIO;

    /**
     * Converte una stringa nel corrispondente valore della classe enumerativa Ruolo.
     * @param ruolo la stringa da convertire nel ruolo corrispondente
     * @return l'elemento corrispondente della classe enumerativa Ruolo
     * @throws IllegalArgumentException se la stringa è nulla, vuota o non corrisponde ad alcun ruolo
     */
    public static Ruolo trovaRuoloDaStringa(String ruolo) {
        Validatore.validaStringa(ruolo, "Il ruolo da convertire non può essere nullo o vuoto.");
        for (Ruolo r : Ruolo.values()) {
            if (r.name().equalsIgnoreCase(ruolo.trim())) {
                return r;
            }
        }
        throw new IllegalArgumentException("Ruolo non riconosciuto: " + ruolo);
    }
}
