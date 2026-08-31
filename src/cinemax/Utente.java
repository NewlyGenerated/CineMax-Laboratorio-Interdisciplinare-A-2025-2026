// Kejsi Xhafaj, 759934, VA
package cinemax;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Rappresenta un utente all'interno del sistema e contiene le informazioni principali ad esso associate, come ad esempio le generalità e le
 * credenziali di accesso. Questo utente è identificato tramite il suo username univoco.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class Utente {
    /**
     * L'attributo <code>nome</code> contiene il nome di battesimo dell'utente.
     */
    private String nome;
    /**
     * L'attributo <code>cognome</code> definisce il cognome dell'utente.
     */
    private String cognome;
    /**
     * L'attributo <code>username</code> indica l'identificatore univoco utilizzato dall'utente per effettuare l'accesso al sistema.
     */
    private String username;
    /**
     * L'attributo <code>passwordHash</code> specifica la password dell'utente cifrata per ragioni di sicurezza.
     */
    private String passwordHash;
    /**
     * L'attributo <code>saltBase64</code> esprime il valore casuale generato e utilizzato nel processo di cifratura della password.
     */
    private String saltBase64;
    /**
     * L'attributo <code>iterazioniHashing</code> definisce il numero di iterazioni applicate per generare l'hash della password.
     */
    private int iterazioniHashing;
    /**
     * L'attributo <code>luogoDomicilio</code> contiene l'indirizzo o la città in cui l'utente risiede o è domiciliato.
     */
    private String luogoDomicilio;
    /**
     * L'attributo <code>ruolo</code> definisce la tipologia di utenza e i relativi permessi di accesso all'interno del sistema.
     */
    private Ruolo ruolo;
    /**
     * L'attributo <code>dataNascita</code> indica la data di nascita dell'utente.
     */
    private LocalDate dataNascita;

    /**
     * Costruisce un nuovo utente dopo aver verificato la validità di tutti i dati inseriti.
     * @param nome              il nome dell'utente
     * @param cognome           il cognome dell'utente
     * @param username          lo username univoco (max 50 caratteri, solo alfanumerico, minuscolo)
     * @param passwordHash      l'hash della password codificato in Base64
     * @param saltBase64        il salt codificato in Base64
     * @param iterazioniHashing il numero di iterazioni applicate per generare l'hash della password
     * @param luogoDomicilio    il luogo di domicilio (max 100 caratteri)
     * @param ruolo             il ruolo assegnato all'utente
     * @param dataNascita       la data di nascita dell'utente (non futura e non antecedente al 1850)
     * @throws IllegalArgumentException se uno qualsiasi dei parametri è nullo, vuoto o non rispetta le regole di validazione
     */
    public Utente(String nome, String cognome, String username, String passwordHash, String saltBase64, int iterazioniHashing, String luogoDomicilio, Ruolo ruolo, LocalDate dataNascita) {
        validaNome(nome);
        validaCognome(cognome);
        validaUsername(username);
        validaPasswordHash(passwordHash);
        validaSaltBase64(saltBase64);
        validaIterazioni(iterazioniHashing);
        validaLuogoDomicilio(luogoDomicilio);
        validaRuolo(ruolo);
        validaDataNascita(dataNascita);
        this.nome = nome;
        this.cognome = cognome;
        this.username = username.trim().toLowerCase();
        this.passwordHash = passwordHash;
        this.saltBase64 = saltBase64;
        this.iterazioniHashing = iterazioniHashing;
        this.dataNascita = dataNascita;
        this.luogoDomicilio = luogoDomicilio;
        this.ruolo = ruolo;
    }

    /**
     * Costruisce un nuovo utente, senza specificare la data di nascita, dopo aver verificato la validità di tutti i dati inseriti.
     * @param nome              il nome dell'utente
     * @param cognome           il cognome dell'utente
     * @param username          lo username univoco (max 50 caratteri, solo alfanumerico, minuscolo)
     * @param passwordHash      l'hash della password codificato in Base64
     * @param saltBase64        il salt codificato in Base64
     * @param iterazioniHashing il numero di iterazioni applicate per generare l'hash della password
     * @param luogoDomicilio    il luogo di domicilio (max 100 caratteri)
     * @param ruolo             il ruolo assegnato all'utente
     * @throws IllegalArgumentException se uno qualsiasi dei parametri è nullo, vuoto o non rispetta le regole di validazione
     */
    public Utente(String nome, String cognome, String username, String passwordHash, String saltBase64, int iterazioniHashing, String luogoDomicilio, Ruolo ruolo) {
        validaNome(nome);
        validaCognome(cognome);
        validaUsername(username);
        validaPasswordHash(passwordHash);
        validaSaltBase64(saltBase64);
        validaIterazioni(iterazioniHashing);
        validaLuogoDomicilio(luogoDomicilio);
        validaRuolo(ruolo);
        this.nome = nome;
        this.cognome = cognome;
        this.username = username.trim().toLowerCase();
        this.passwordHash = passwordHash;
        this.saltBase64 = saltBase64;
        this.iterazioniHashing = iterazioniHashing;
        this.luogoDomicilio = luogoDomicilio;
        this.ruolo = ruolo;
        this.dataNascita = null;
    }

    /**
     * Restituisce il numero di iterazioni per l'hashing.
     * @return il numero di iterazioni impostato per l'hashing
     */
    public int getIterazioniHashing() {
        return iterazioniHashing;
    }

    /**
     * Imposta il valore di iterazioniHashing.
     * @param iterazioniHashing il nuovo valore da impostare
     * @throws IllegalArgumentException se il numero passato per le iterazioni sono inferiori alle raccomandazioni OWASP
     */
    public void setIterazioniHashing(int iterazioniHashing) {
        validaIterazioni(iterazioniHashing);
        this.iterazioniHashing = iterazioniHashing;
    }

    /**
     * Restituisce il nome dell'utente.
     * @return il nome dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore del nome.
     * @param nome il nuovo valore da impostare
     * @throws IllegalArgumentException se il nome inserito supera i 50 caratteri
     */
    public void setNome(String nome) {
        validaNome(nome);
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     * @return il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore del cognome.
     * @param cognome il nuovo valore da impostare
     * @throws IllegalArgumentException se il cognome inserito supera i 50 caratteri
     */
    public void setCognome(String cognome) {
        validaCognome(cognome);
        this.cognome = cognome;
    }

    /**
     * Restituisce lo username dell'utente.
     * @return lo username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Imposta il valore dello username.
     * @param username il nuovo username da impostare (max 50 caratteri, solo lettere minuscole e cifre)
     * @throws IllegalArgumentException se lo username inserito è nullo, vuoto, maggiore di 50 caratteri o contiene caratteri non ammessi (quelli speciali)
     */
    public void setUsername(String username) {
        validaUsername(username);
        this.username = username.trim().toLowerCase();
    }

    /**
     * Restituisce l'hash della password dell'utente codificato in Base64.
     * @return la password hashata
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Imposta il valore della password hashata.
     * @param passwordHash il nuovo valore da impostare
     * @throws IllegalArgumentException se l'hash inserito è nullo o vuoto
     */
    public void setPasswordHash(String passwordHash) {
        validaPasswordHash(passwordHash);
        this.passwordHash = passwordHash;
    }

    /**
     * Restituisce il luogo di domicilio dell'utente.
     * @return il luogo di domicilio
     */
    public String getLuogoDomicilio() {
        return luogoDomicilio;
    }

    /**
     * Imposta il valore del luogo di domicilio.
     * @param luogoDomicilio il nuovo valore da impostare
     * @throws IllegalArgumentException se il luogo di domicilio inserito è nullo, vuoto o supera i 100 caratteri
     */
    public void setLuogoDomicilio(String luogoDomicilio) {
        validaLuogoDomicilio(luogoDomicilio);
        this.luogoDomicilio = luogoDomicilio;
    }

    /**
     * Restituisce il ruolo dell'utente.
     * @return il ruolo assegnato all'utente
     */
    public Ruolo getRuolo() {
        return ruolo;
    }

    /**
     * Imposta il ruolo dell'utente.
     * @param ruolo il nuovo ruolo da impostare
     * @throws IllegalArgumentException se il ruolo inserito è nullo
     */
    public void setRuolo(Ruolo ruolo) {
        validaRuolo(ruolo);
        this.ruolo = ruolo;
    }

    /**
     * Restituisce la data di nascita dell'utente, se inserita. Altrimenti restituisce null.
     * @return la data di nascita
     */
    public LocalDate getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta il valore della data di nascita dell'utente.
     * @param dataNascita la nuova data di nascita da impostare
     * @throws IllegalArgumentException se la data di nascita inserita è nulla, antecedente al 1850 o futura
     */
    public void setDataNascita(LocalDate dataNascita) {
        validaDataNascita(dataNascita);
        this.dataNascita = dataNascita;
    }

    /**
     * Restituisce gli anni dell'utente, se ha inserito la sua data di nascita. Altrimenti restituisce 0.
     * @return l'età dell'utente, altrimenti 0 se non ha inserito alcuna data di nascita
     */
    public int getAnniUtente() {
        if (dataNascita == null) {
            return 0;
        }
        Period calcoloAnniUtente = Period.between(dataNascita, LocalDate.now());
        return calcoloAnniUtente.getYears();
    }

    /**
     * Restituisce il salt utilizzato per l'hashing della password codificato in Base64.
     * @return il salt in formato Base64
     */
    public String getSaltBase64() {
        return saltBase64;
    }

    /**
     * Imposta il valore del salt utilizzato per l'hashing.
     * @param saltBase64 il nuovo valore da impostare
     * @throws IllegalArgumentException se il valore inserito è nullo o vuoto
     */
    public void setSaltBase64(String saltBase64) {
        validaSaltBase64(saltBase64);
        this.saltBase64 = saltBase64;
    }

    /**
     * Verifica la validità del nome dell'utente.
     * @param nome il nome da verificare
     * @throws IllegalArgumentException se il nome supera i 50 caratteri
     */
    private void validaNome(String nome) {
        Validatore.validaStringa(nome, "L'utente non ha inserito un nome valido.");
        if (nome.length() > 50) {
            throw new IllegalArgumentException("L'utente ha inserito un nome più lungo di 50 caratteri.");
        }
    }

    /**
     * Verifica la validità del cognome dell'utente.
     * @param cognome il cognome da verificare
     * @throws IllegalArgumentException se il cognome supera i 50 caratteri
     */
    private void validaCognome(String cognome) {
        Validatore.validaStringa(cognome, "L'utente non ha inserito un cognome valido.");
        if (cognome.length() > 50) {
            throw new IllegalArgumentException("L'utente ha inserito un cognome più lungo di 50 caratteri.");
        }
    }

    /**
     * Verifica la validità dello username dell'utente.
     * @param username lo username da verificare
     * @throws IllegalArgumentException se lo username è nullo, vuoto, maggiore di 50 caratteri o contiene caratteri non ammessi (quelli speciali)
     */
    private void validaUsername(String username) {
        Validatore.validaStringa(username, "L'utente non ha inserito uno username valido.");
        if (username.trim().length() > 50 || !username.matches("^[a-z0-9]+$")) {
            throw new IllegalArgumentException("L'utente non ha inserito uno username in minuscolo che sia di lunghezza minore o uguale a 50 caratteri. " +
                    "I caratteri speciali (!, $, /, etc..) e gli spazi bianchi non sono ammessi.");
        }
    }

    /**
     * Verifica la validità dell'hash della password.
     * @param passwordHash l'hash della password da verificare
     * @throws IllegalArgumentException se l'hash risulta nullo o vuoto
     */
    private void validaPasswordHash(String passwordHash) {
        Validatore.validaStringa(passwordHash, "Qualcosa è andato storto con il calcolo della password hash.");
    }

    /**
     * Verifica la validità del salt utilizzato per l'hashing.
     * @param saltBase64 il salt da verificare
     * @throws IllegalArgumentException se il salt risulta nullo o vuoto
     */
    private void validaSaltBase64(String saltBase64) {
        Validatore.validaStringa(saltBase64, "La saltBase64 è nulla. Qualcosa è andato storto.");
    }

    /**
     * Verifica la validità del luogo di domicilio.
     * @param luogoDomicilio il luogo di domicilio da verificare
     * @throws IllegalArgumentException se il luogo di domicilio è nullo, vuoto o maggiore di 100 caratteri
     */
    private void validaLuogoDomicilio(String luogoDomicilio) {
        Validatore.validaStringa(luogoDomicilio, "L'utente non ha inserito un luogo di domicilio valido.");
        if (luogoDomicilio.length() > 100) {
            throw new IllegalArgumentException("L'utente ha inserito un luogo di domicilio più lungo di 100 caratteri.");
        }
    }

    /**
     * Verifica la validità della data di nascita dell'utente, se inserita.
     * @param dataNascita la data di nascita da verificare
     * @throws IllegalArgumentException se la data di nascita è nulla, antecedente al 1850 o futura
     */
    private void validaDataNascita(LocalDate dataNascita) {
        if (dataNascita == null || dataNascita.getYear() < 1850 || dataNascita.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("L'utente non ha inserito una data di nascita valida.");
        }
    }

    /**
     * Verifica la validità del ruolo dell'utente.
     * @param ruolo il ruolo da verificare
     * @throws IllegalArgumentException se il ruolo risulta nullo
     */
    private void validaRuolo(Ruolo ruolo) {
        Validatore.validaOggetto(ruolo);
    }

    /**
     * Verifica la validità del numero di iterazioni per l'hashing.
     * @param iterazioniHashing il numero di iterazioni da verificare
     * @throws IllegalArgumentException se le iterazioni per l'hashing passate come parametro sono inferiori alle raccomandazioni OWASP
     */
    private void validaIterazioni(int iterazioniHashing) {
        if (iterazioniHashing < 600000) {
            throw new IllegalArgumentException("Le iterazioni per l'hashing non possono essere inferiori a 600.000.");
        }
    }

    /**
     * Verifica se l'utente ha il ruolo di proiezionista.
     * @return vero se è proiezionista, falso altrimenti
     */
    public boolean isProiezionista() {
        return this.ruolo != null && this.ruolo == Ruolo.PROIEZIONISTA;
    }

    /**
     * Verifica se l'utente ha il ruolo di cliente.
     * @return vero se è cliente, falso altrimenti
     */
    public boolean isCliente() {
        return this.ruolo != null && this.ruolo == Ruolo.CLIENTE;
    }

    /**
     * Verifica se l'utente ha il ruolo di bigliettaio.
     * @return vero se è bigliettaio, falso altrimenti
     */
    public boolean isBigliettaio() {
        return this.ruolo != null && this.ruolo == Ruolo.BIGLIETTAIO;
    }

    /**
     * Restituisce una rappresentazione in formato stringa dell'utente, contenente i suoi dati principali formattati.
     * @return la stringa con le informazioni dell'utente
     */
    @Override
    public String toString() {
        if (dataNascita != null) {
            DateTimeFormatter formatoData = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
            return "Nome: " + this.nome + "\n" +
                    "Cognome: " + this.cognome + "\n" +
                    "Username: " + this.username + "\n" +
                    "Luogo Domicilio: " + this.luogoDomicilio + "\n" +
                    "Ruolo: " + this.ruolo + "\n" +
                    "Data di Nascita: " + dataNascita.format(formatoData) + "\n";
        }
        return "Nome: " + this.nome + "\n" +
                "Cognome: " + this.cognome + "\n" +
                "Username: " + this.username + "\n" +
                "Luogo Domicilio: " + this.luogoDomicilio + "\n" +
                "Ruolo: " + this.ruolo + "\n" +
                "Data di Nascita: " + "Non hai inserito una data di nascita" + "\n";
    }
}
