// Kejsi Xhafaj, 759934, VA
package cinemax;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.UUID;

/**
 * Rappresenta una proiezione di un qualsiasi Film indicandone gli orari di inizio e fine, il prezzo
 * del biglietto e un codice univoco per identificarla.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class Proiezione {
    /**
     * L'attributo <code>POSTI_SALA</code> rappresenta il numero di posti a sedere massimi all'interno della sala.
     */
    private static final int POSTI_SALA = 200;
    /**
     * L'attributo <code>codiceUnivoco</code> contiene il codice alfanumerico univoco specifico per quella proiezione.
     */
    private String codiceUnivoco;
    /**
     * L'attributo <code>inizioProiezione</code> definisce la data e orario di inizio della proiezione.
     */
    private LocalDateTime inizioProiezione;
    /**
     * L'attributo <code>fineProiezione</code> definisce la data e orario di fine della proiezione.
     */
    private LocalDateTime fineProiezione;
    /**
     * L'attributo <code>filmProiettato</code> contiene il film proiettato.
     */
    private Film filmProiettato;
    /**
     * L'attributo <code>prezzoBiglietto</code> indica il prezzo di un biglietto per quella proiezione.
     */
    private double prezzoBiglietto;
    /**
     * L'attributo <code>postiPrenotati</code> definisce il numero di posti a sedere prenotati per quella proiezione.
     */
    private int postiPrenotati;
    /**
     * L'attributo <code>glifoValuta</code> definisce il glifo della valuta da usare per rappresentare i prezzi.
     */
    private String glifoValuta = "€";

    /**
     * Costruisce una nuova proiezione specificando manualmente il codice univoco.
     * @param codiceUnivoco    il codice identificativo univoco della proiezione
     * @param inizioProiezione la data e l'ora di inizio della proiezione
     * @param fineProiezione   la data e l'ora di fine della proiezione
     * @param filmProiettato   il film associato alla proiezione
     * @param prezzoBiglietto  il prezzo del singolo biglietto per la proiezione
     * @param postiPrenotati   il numero di posti attualmente prenotati
     * @throws IllegalArgumentException se uno qualsiasi dei parametri è nullo, vuoto o non rispetta le regole di validazione
     * @throws PostiEsauritiException   se il parametro dei posti prenotati risulta superiore a 200
     */
    public Proiezione(String codiceUnivoco, LocalDateTime inizioProiezione, LocalDateTime fineProiezione, Film filmProiettato, double prezzoBiglietto, int postiPrenotati) {
        validaCodiceUnivoco(codiceUnivoco);
        validaFilmProiettato(filmProiettato);
        validaCoerenzaOrari(inizioProiezione, fineProiezione, filmProiettato.getDurata());
        validaPrezzoBiglietto(prezzoBiglietto);
        validaPostiPrenotati(postiPrenotati);
        this.codiceUnivoco = codiceUnivoco;
        this.filmProiettato = filmProiettato;
        this.prezzoBiglietto = prezzoBiglietto;
        this.postiPrenotati = postiPrenotati;
        this.inizioProiezione = inizioProiezione;
        this.fineProiezione = fineProiezione;
    }

    /**
     * Costruisce una nuova proiezione generando automaticamente un codice univoco.
     * @param inizioProiezione la data e l'ora di inizio della proiezione
     * @param fineProiezione   la data e l'ora di fine della proiezione
     * @param filmProiettato   il film associato alla proiezione
     * @param prezzoBiglietto  il prezzo del singolo biglietto per la proiezione
     * @param postiPrenotati   il numero di posti attualmente prenotati
     * @throws IllegalArgumentException se uno qualsiasi dei parametri è nullo, vuoto o non rispetta le regole di validazione
     * @throws PostiEsauritiException   se il parametro dei posti prenotati risulta superiore a 200
     */
    public Proiezione(LocalDateTime inizioProiezione, LocalDateTime fineProiezione, Film filmProiettato, double prezzoBiglietto, int postiPrenotati) {
        this(UUID.randomUUID().toString(), inizioProiezione, fineProiezione, filmProiettato, prezzoBiglietto, postiPrenotati);
    }

    /**
     * Restituisce il numero di posti presenti all'interno della sala.
     * @return il numero di posti della sala
     */
    public static int getPostiSala() {
        return POSTI_SALA;
    }

    /**
     * Verifica la coerenza fra orario di inizio, orario di fine e durata del film.
     * @param inizioProiezione l'orario di inizio da verificare
     * @param fineProiezione   l'orario di fine da verificare
     * @param durataFilm       la durata in minuti del film
     * @throws IllegalArgumentException se uno degli orari è nullo, se l'inizio è successivo alla fine oppure se l'intervallo non consente la visione intera del film
     */
    private static void validaCoerenzaOrari(LocalDateTime inizioProiezione, LocalDateTime fineProiezione, int durataFilm) {
        Validatore.validaOggetto(inizioProiezione, "L'orario di inizio proiezione non è valido.");
        Validatore.validaOggetto(fineProiezione, "L'orario di fine proiezione non è valido.");
        if (inizioProiezione.isAfter(fineProiezione)) {
            throw new IllegalArgumentException("L'inizio della proiezione non può essere dopo la sua stessa fine.");
        }
        if (fineProiezione.isBefore(inizioProiezione.plusMinutes(durataFilm))) {
            throw new IllegalArgumentException("Il film dura " + durataFilm + " minuti. La fine della proiezione non consente la sua visione intera.");
        }
    }

    /**
     * Aggiorna la durata del film e gli orari di proiezione, verificandone la coerenza dello stato finale prima di applicare le modifiche.
     * @param nuovaDurata la nuova durata del film in minuti (valori minori o uguali a 0 significano campo invariato)
     * @param nuovoInizio il nuovo orario di inizio (null significa campo invariato)
     * @param nuovaFine   il nuovo orario di fine (null significa campo invariato)
     * @throws IllegalArgumentException se lo stato finale non è coerente
     */
    public void aggiornaDurataEOrari(int nuovaDurata, LocalDateTime nuovoInizio, LocalDateTime nuovaFine) {
        LocalDateTime inizioFinale = (nuovoInizio != null) ? nuovoInizio : this.inizioProiezione;
        LocalDateTime fineFinale = (nuovaFine != null) ? nuovaFine : this.fineProiezione;
        int durataFinale = (nuovaDurata > 0) ? nuovaDurata : this.filmProiettato.getDurata();
        validaCoerenzaOrari(inizioFinale, fineFinale, durataFinale);
        if (nuovaDurata > 0) {
            this.filmProiettato.setDurata(nuovaDurata);
        }
        this.inizioProiezione = inizioFinale;
        this.fineProiezione = fineFinale;
    }

    /**
     * Restituisce il codice univoco associato alla proiezione.
     * @return il codice univoco
     */
    public String getCodiceUnivoco() {
        return codiceUnivoco;
    }

    /**
     * Imposta il valore del codice univoco.
     * @param codiceUnivoco il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore passato è nullo o vuoto
     */
    public void setCodiceUnivoco(String codiceUnivoco) {
        validaCodiceUnivoco(codiceUnivoco);
        this.codiceUnivoco = codiceUnivoco;
    }

    /**
     * Restituisce l'orario di inizio della proiezione.
     * @return l'orario di inizio della proiezione
     */
    public LocalDateTime getInizioProiezione() {
        return inizioProiezione;
    }

    /**
     * Imposta un nuovo orario di inizio della proiezione.
     * @param inizioProiezione la nuova data e orario da impostare
     * @throws IllegalArgumentException se il valore passato è nullo oppure se è successivo all'orario di fine
     */
    public void setInizioProiezione(LocalDateTime inizioProiezione) {
        validaCoerenzaOrari(inizioProiezione, this.fineProiezione, this.filmProiettato.getDurata());
        this.inizioProiezione = inizioProiezione;
    }

    /**
     * Restituisce l'orario di fine della proiezione.
     * @return l'orario di fine della proiezione
     */
    public LocalDateTime getFineProiezione() {
        return fineProiezione;
    }

    /**
     * Imposta un nuovo orario di fine della proiezione.
     * @param fineProiezione la nuova data e orario da impostare
     * @throws IllegalArgumentException se il valore passato è nullo oppure se è precedente all'orario di inizio sommato alla durata
     */
    public void setFineProiezione(LocalDateTime fineProiezione) {
        validaCoerenzaOrari(this.inizioProiezione, fineProiezione, this.filmProiettato.getDurata());
        this.fineProiezione = fineProiezione;
    }

    /**
     * Restituisce il film proiettato.
     * @return il film proiettato
     */
    public Film getFilmProiettato() {
        return filmProiettato;
    }

    /**
     * Imposta il valore del film proiettato.
     * @param filmProiettato il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore passato risulta nullo o se la sua durata eccede l'intervallo della proiezione
     */
    public void setFilmProiettato(Film filmProiettato) {
        validaFilmProiettato(filmProiettato);
        validaCoerenzaOrari(this.inizioProiezione, this.fineProiezione, filmProiettato.getDurata());
        this.filmProiettato = filmProiettato;
    }

    /**
     * Restituisce il prezzo del biglietto.
     * @return il prezzo del biglietto
     */
    public double getPrezzoBiglietto() {
        return prezzoBiglietto;
    }

    /**
     * Imposta il valore del prezzo di un singolo biglietto.
     * @param prezzoBiglietto il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore passato è inferiore a 0
     */
    public void setPrezzoBiglietto(double prezzoBiglietto) {
        validaPrezzoBiglietto(prezzoBiglietto);
        this.prezzoBiglietto = prezzoBiglietto;
    }

    /**
     * Restituisce il numero di posti prenotati.
     * @return il numero di posti prenotati
     */
    public int getPostiPrenotati() {
        return postiPrenotati;
    }

    /**
     * Imposta il valore numerico dei posti prenotati.
     * @param postiPrenotati il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore passato è minore di 0
     * @throws PostiEsauritiException   se il valore passato è superiore a 200
     */
    public void setPostiPrenotati(int postiPrenotati) {
        validaPostiPrenotati(postiPrenotati);
        this.postiPrenotati = postiPrenotati;
    }

    /**
     * Aggiorna il valore dei posti prenotati.
     * @param numeroPostiModificati numero positivo se ci sono state prenotazioni, numero negativo se ci sono state cancellazioni
     * @throws IllegalArgumentException se il numero di posti modifica i posti prenotati portandoli sotto lo 0
     * @throws PostiEsauritiException   se il numero di posti modifica i posti prenotati portandoli superiore a 200
     */
    public void aggiornaPostiPrenotati(int numeroPostiModificati) {
        int nuoviPostiPrenotati = this.postiPrenotati + numeroPostiModificati;
        setPostiPrenotati(nuoviPostiPrenotati);
    }

    /**
     * Restituisce il glifo della valuta.
     * @return il glifo
     */
    public String getGlifoValuta() {
        return glifoValuta;
    }

    /**
     * Imposta il glifo.
     * @param glifoValuta il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore passato è nullo o vuoto
     */
    public void setGlifoValuta(String glifoValuta) {
        validaGlifoValuta(glifoValuta);
        this.glifoValuta = glifoValuta;
    }

    /**
     * Verifica la validità del codice univoco della proiezione.
     * @param codiceUnivoco il codice univoco da verificare
     * @throws IllegalArgumentException se il codice univoco passato risulta nullo o vuoto
     */
    private void validaCodiceUnivoco(String codiceUnivoco) {
        Validatore.validaStringa(codiceUnivoco, "Il codice univoco non può essere nullo o vuoto.");
    }

    /**
     * Verifica la validità del prezzo del biglietto.
     * @param prezzoBiglietto il prezzo da verificare
     * @throws IllegalArgumentException se il prezzo passato è inferiore a 0
     */
    private void validaPrezzoBiglietto(double prezzoBiglietto) {
        if (prezzoBiglietto < 0) {
            throw new IllegalArgumentException("Il prezzo del biglietto non può essere inferiore a 0 euro.");
        }
    }

    /**
     * Verifica la validità della variabile rappresentante i posti prenotati.
     * @param postiPrenotati il numero di posti prenotati da verificare
     * @throws IllegalArgumentException se la variabile passata è minore di 0
     * @throws PostiEsauritiException   se la variabile passata è superiore a 200
     */
    private void validaPostiPrenotati(int postiPrenotati) {
        if (postiPrenotati < 0) {
            throw new IllegalArgumentException("I posti prenotati devono essere minimo 0.");
        } else if (postiPrenotati > POSTI_SALA) {
            throw new PostiEsauritiException("I posti prenotati possono essere massimo " + POSTI_SALA + ".");
        }
    }

    /**
     * Verifica la validità del glifo della valuta che si intende usare.
     * @param glifoValuta il glifo da verificare
     * @throws IllegalArgumentException se il glifo restituito risulta nullo
     */
    private void validaGlifoValuta(String glifoValuta) {
        Validatore.validaStringa(glifoValuta, "L'utente non ha inserito un glifo valido.");
    }

    /**
     * Verifica la validità del film da proiettare.
     * @param filmProiettato il film da verificare
     * @throws IllegalArgumentException se il film passato risulta nullo
     */
    private void validaFilmProiettato(Film filmProiettato) {
        Validatore.validaOggetto(filmProiettato);
    }

    /**
     * Restituisce una rappresentazione in formato stringa della proiezione, contenente i dati principali formattati.
     * @return la stringa con le informazioni della proiezione
     */
    @Override
    public String toString() {
        DateTimeFormatter formatoData = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        DateTimeFormatter formatoOra = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        return this.filmProiettato.toString() +
                "Giorno: " + this.inizioProiezione.format(formatoData) + "\n" +
                "Dalle: " + this.inizioProiezione.format(formatoOra) + "\n" +
                "Alle: " + this.fineProiezione.format(formatoOra) + "\n" +
                "Costo biglietto: " + String.format("%.2f", this.prezzoBiglietto) + " " + glifoValuta + "\n" +
                "Posti liberi: " + (POSTI_SALA - this.postiPrenotati) + "\n";
    }
}
