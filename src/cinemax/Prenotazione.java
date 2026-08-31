// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * La classe rappresenta una prenotazione relativa a una proiezione, è associata univocamente a un utente tramite il suo username.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class Prenotazione {
    /**
     * L'attributo <code>codiceUnivoco</code> identifica in modo univoco la singola prenotazione all'interno del sistema.
     */
    private final String codiceUnivoco;
    /**
     * L'attributo <code>usernameUtente</code> contiene l'identificativo dell'utente che ha effettuato la prenotazione.
     */
    private final String usernameUtente;
    /**
     * L'attributo <code>codiceProiezione</code> indica il codice di riferimento della proiezione a cui la prenotazione è associata.
     */
    private String codiceProiezione;
    /**
     * L'attributo <code>numeroBiglietti</code> definisce la quantità di biglietti riservati per questa prenotazione.
     */
    private int numeroBiglietti;

    /**
     * Costruisce un nuovo oggetto prenotazione associando un utente a una proiezione.
     * @param codiceUnivoco    il codice alfanumerico univoco che identifica la prenotazione
     * @param usernameUtente   lo username dell'utente che effettua la prenotazione
     * @param codiceProiezione il codice alfanumerico univoco che identifica la proiezione prenotata
     * @param numeroBiglietti  il numero di biglietti riservati per questa prenotazione
     * @throws IllegalArgumentException se uno qualsiasi dei parametri è nullo, vuoto o non rispetta le regole di validazione
     */
    public Prenotazione(String codiceUnivoco, String usernameUtente, String codiceProiezione, int numeroBiglietti) {
        validaCodiceProiezione(codiceProiezione);
        validaCodiceUnivoco(codiceUnivoco);
        validaUsernameUtente(usernameUtente);
        validaNumeroBiglietti(numeroBiglietti);
        this.codiceUnivoco = codiceUnivoco;
        this.usernameUtente = usernameUtente;
        this.codiceProiezione = codiceProiezione;
        this.numeroBiglietti = numeroBiglietti;
    }

    /**
     * Restituisce il codice univoco della prenotazione.
     * @return il codice univoco
     */
    public String getCodiceUnivoco() {
        return codiceUnivoco;
    }

    /**
     * Restituisce il codice della proiezione a cui la prenotazione fa riferimento.
     * @return il codice della proiezione
     */
    public String getCodiceProiezione() {
        return codiceProiezione;
    }

    /**
     * Imposta il valore del codice della proiezione dopo aver verificato la validità del parametro passato.
     * @param codiceProiezione il nuovo codice della proiezione da impostare
     * @throws IllegalArgumentException se il valore passato è nullo o vuoto
     */
    public void setCodiceProiezione(String codiceProiezione) {
        validaCodiceProiezione(codiceProiezione);
        this.codiceProiezione = codiceProiezione;
    }

    /**
     * Restituisce lo username dell'utente che ha effettuato la prenotazione.
     * @return lo username dell'utente
     */
    public String getUsernameUtente() {
        return usernameUtente;
    }

    /**
     * Restituisce il numero di biglietti acquistati.
     * @return il numero di biglietti
     */
    public int getNumeroBiglietti() {
        return numeroBiglietti;
    }

    /**
     * Imposta il valore del numero di biglietti dopo aver verificato la validità del parametro passato.
     * @param numeroBiglietti il nuovo valore da impostare
     * @throws IllegalArgumentException se il numero dei biglietti passato è inferiore o uguale a 0, oppure se maggiore di 200
     */
    public void setNumeroBiglietti(int numeroBiglietti) {
        validaNumeroBiglietti(numeroBiglietti);
        this.numeroBiglietti = numeroBiglietti;
    }

    /**
     * Verifica la validità del codice univoco della prenotazione.
     * @param codiceUnivoco il codice della prenotazione da verificare
     * @throws IllegalArgumentException se il codice univoco della prenotazione è nullo o vuoto
     */
    private void validaCodiceUnivoco(String codiceUnivoco) {
        Validatore.validaStringa(codiceUnivoco, "Il codice univoco della prenotazione non può essere nullo o vuoto.");
    }

    /**
     * Verifica la validità del codice univoco della proiezione.
     * @param codiceProiezione il codice della proiezione da verificare
     * @throws IllegalArgumentException se il codice univoco della proiezione è nullo o vuoto
     */
    private void validaCodiceProiezione(String codiceProiezione) {
        Validatore.validaStringa(codiceProiezione, "Il codice univoco della proiezione non può essere nullo o vuoto.");
    }

    /**
     * Verifica la validità dello username dell'utente.
     * @param usernameUtente lo username da verificare
     * @throws IllegalArgumentException se lo username è nullo o vuoto
     */
    private void validaUsernameUtente(String usernameUtente) {
        Validatore.validaStringa(usernameUtente, "Lo username non può essere nullo o vuoto.");
    }

    /**
     * Verifica la validità dei numeri di biglietti inseriti.
     * @param numeroBiglietti il numero di biglietti da validare
     * @throws IllegalArgumentException se il numero dei biglietti è inferiore o uguale a 0, oppure se maggiore di 200
     */
    private void validaNumeroBiglietti(int numeroBiglietti) {
        if (numeroBiglietti <= 0) {
            throw new IllegalArgumentException("Il numero di biglietti acquistati non può essere minore o uguale a 0.");
        }
        if (numeroBiglietti > Proiezione.getPostiSala()) {
            throw new IllegalArgumentException("Il numero di biglietti acquistati non può essere maggiore di " + Proiezione.getPostiSala() + ".");
        }
    }
}
