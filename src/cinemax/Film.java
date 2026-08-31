// Kejsi Xhafaj, 759934, VA
package cinemax;

import java.time.LocalDate;

/**
 * Rappresenta un'opera cinematografica e contiene tutte le informazioni descrittive principali
 * ad essa associate, come ad esempio il titolo, il genere, il regista, l'anno di uscita, la durata in minuti
 * e l'età minima consigliata per la visione.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class Film {
    /**
     * L'attributo <code>titolo</code> contiene il titolo formale dell'opera cinematografica.
     */
    private String titolo;
    /**
     * L'attributo <code>genere</code> definisce la categoria o il genere principale a cui appartiene il film.
     */
    private Genere genere;
    /**
     * L'attributo <code>regista</code> indica il nome della persona che ha diretto e realizzato il film.
     */
    private String regista;
    /**
     * L'attributo <code>anno</code> specifica l'anno solare di uscita del film nelle sale cinematografiche.
     */
    private int anno;
    /**
     * L'attributo <code>durata</code> esprime la lunghezza complessiva del film calcolata in minuti.
     */
    private int durata;
    /**
     * L'attributo <code>etaMinima</code> definisce l'età minima richiesta o consigliata per poter visionare il film.
     */
    private int etaMinima;

    /**
     * Crea un nuovo oggetto Film validando i parametri di input.
     * L'anno di uscita viene controllato in modo dinamico impedendo l'inserimento di anni precedenti all'invenzione della pellicola
     * cinematografica o superiori di 10 anni dall'anno corrente e l'inserimento di film con durata minore o uguale a 0 minuti.
     * @param titolo    il titolo del film (non può essere un valore nullo)
     * @param genere    il genere che meglio rappresenta il film
     * @param regista   il regista che ha realizzato il film
     * @param anno      l'anno di uscita (deve essere compreso tra il 1885 e 10 anni nel futuro)
     * @param durata    la durata del film in minuti (deve essere strettamente maggiore di 0)
     * @param etaMinima l'età minima per poter visionare il film (deve essere maggiore o uguale a 0)
     * @throws IllegalArgumentException se uno qualsiasi dei parametri è nullo, vuoto o non rispetta le regole di validazione
     */
    public Film(String titolo, Genere genere, String regista, int anno, int durata, int etaMinima) {
        validaTitolo(titolo);
        validaGenere(genere);
        validaRegista(regista);
        validaAnno(anno);
        validaDurata(durata);
        validaEtaMinima(etaMinima);
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durata = durata;
        this.etaMinima = etaMinima;
    }

    /**
     * Restituisce il titolo del film.
     * @return il titolo
     */
    public String getTitolo() {
        return this.titolo;
    }

    /**
     * Imposta il valore del titolo del film.
     * @param titolo il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore inserito è nullo o vuoto
     */
    public void setTitolo(String titolo) {
        validaTitolo(titolo);
        this.titolo = titolo;
    }

    /**
     * Restituisce il genere a cui il film appartiene.
     * @return il genere
     */
    public Genere getGenere() {
        return this.genere;
    }

    /**
     * Imposta il valore del genere del film.
     * @param genere il nuovo valore da impostare
     * @throws IllegalArgumentException se il genere inserito è nullo
     */
    public void setGenere(Genere genere) {
        validaGenere(genere);
        this.genere = genere;
    }

    /**
     * Restituisce il regista del film.
     * @return il regista
     */
    public String getRegista() {
        return this.regista;
    }

    /**
     * Imposta il valore del regista del film.
     * @param regista il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore inserito è nullo o vuoto
     */
    public void setRegista(String regista) {
        validaRegista(regista);
        this.regista = regista;
    }

    /**
     * Restituisce l'anno di uscita del film.
     * @return l'anno
     */
    public int getAnno() {
        return this.anno;
    }

    /**
     * Imposta il valore di anno.
     * @param anno il nuovo valore da impostare
     * @throws IllegalArgumentException se l'anno non è compreso tra il 1885 e 10 anni nel futuro
     */
    public void setAnno(int anno) {
        validaAnno(anno);
        this.anno = anno;
    }

    /**
     * Restituisce la durata in minuti del film.
     * @return la durata
     */
    public int getDurata() {
        return this.durata;
    }

    /**
     * Imposta il valore di durata.
     * @param durata il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore inserito è inferiore o uguale a 0
     */
    public void setDurata(int durata) {
        validaDurata(durata);
        this.durata = durata;
    }

    /**
     * Restituisce l'età minima per poter visionare il film.
     * @return l'età minima
     */
    public int getEtaMinima() {
        return this.etaMinima;
    }

    /**
     * Imposta il valore di etaMinima.
     * @param etaMinima il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore inserito è inferiore a 0
     */
    public void setEtaMinima(int etaMinima) {
        validaEtaMinima(etaMinima);
        this.etaMinima = etaMinima;
    }

    /**
     * Verifica la validità del titolo del film.
     * @param titolo il titolo da verificare
     * @throws IllegalArgumentException se il titolo risulta nullo o vuoto
     */
    private void validaTitolo(String titolo) {
        Validatore.validaStringa(titolo, "Il titolo del film non può essere un valore nullo o vuoto.");
    }

    /**
     * Verifica la validità del regista del film.
     * @param regista il regista da verificare
     * @throws IllegalArgumentException se il regista risulta nullo o vuoto
     */
    private void validaRegista(String regista) {
        Validatore.validaStringa(regista, "Il regista del film non può essere un valore nullo o vuoto.");
    }

    /**
     * Verifica la validità del genere del film.
     * @param genere il genere da verificare
     * @throws IllegalArgumentException se il genere risulta nullo
     */
    private void validaGenere(Genere genere) {
        Validatore.validaOggetto(genere);
    }

    /**
     * Verifica la validità dell'anno di uscita del film nelle sale.
     * @param anno l'anno da verificare
     * @throws IllegalArgumentException se l'anno non è compreso tra il 1885 e 10 anni nel futuro
     */
    private void validaAnno(int anno) {
        int annoCorrente = LocalDate.now().getYear();
        int annoMassimo = annoCorrente + 10;
        if (anno < 1885 || anno > annoMassimo) {
            throw new IllegalArgumentException("L'anno di uscita deve essere compreso tra il 1885 e il " + annoMassimo + ".");
        }
    }

    /**
     * Verifica la validità della durata in minuti del film.
     * @param durata la durata da verificare
     * @throws IllegalArgumentException se la durata è inferiore o uguale ai 0 minuti
     */
    private void validaDurata(int durata) {
        if (durata <= 0) {
            throw new IllegalArgumentException("La durata del film in minuti deve essere maggiore di 0.");
        }
    }

    /**
     * Verifica la validità dell'età minima per poter visionare il film.
     * @param etaMinima l'età minima da verificare
     * @throws IllegalArgumentException se l'età minima è inferiore a 0
     */
    private void validaEtaMinima(int etaMinima) {
        if (etaMinima < 0) {
            throw new IllegalArgumentException("L'età minima per visionare il film deve essere maggiore o uguale a 0.");
        }
    }

    /**
     * Restituisce una rappresentazione testuale delle informazioni del film.
     * @return una stringa contenente i dettagli principali del film
     */
    @Override
    public String toString() {
        return "Titolo: " + this.titolo + "\n" +
                "Regista: " + this.regista + "\n" +
                "Genere: " + this.getGenere() + "\n" +
                "Anno: " + this.anno + "\n" +
                "Durata: " + this.durata + "\n";
    }
}
