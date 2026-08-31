// Kejsi Xhafaj, 759934, VA
package cinemax;

import java.time.LocalDate;

/**
 * Contiene al suo interno i metodi usati per la gestione degli utenti all'interno del sistema, permettendo di operare sui
 * dati relativi agli utenti.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class GestoreUtenti {
    /**
     * L'attributo <code>gestoreDati</code> memorizza il riferimento al gestore principale dei dati, necessario per accedere
     * alle liste e salvare le informazioni sugli utenti.
     */
    private final GestoreDati gestoreDati;

    /**
     * Costruisce un nuovo gestore degli utenti associandolo al gestore dei dati principale del sistema.
     * @param gestoreDati l'istanza di GestoreDati utilizzata per accedere alle liste e gestire il salvataggio
     * @throws IllegalArgumentException se il gestore dati passato risulta nullo
     */
    public GestoreUtenti(GestoreDati gestoreDati) {
        Validatore.validaOggetto(gestoreDati);
        this.gestoreDati = gestoreDati;
    }

    /**
     * Registra un nuovo utente nel sistema dopo averne validato i dati e cifrato la password.
     * @param nomeInserito           il nome dell'utente
     * @param cognomeInserito        il cognome dell'utente
     * @param usernameInserito       lo username scelto dall'utente (deve essere univoco)
     * @param passwordInChiaro       la password scelta dall'utente (successivamente hashata)
     * @param luogoDomicilioInserito il luogo di domicilio dell'utente
     * @param ruoloInserito          il ruolo dell'utente
     * @param dataNascitaInserita    la data di nascita dell'utente (opzionale)
     * @throws IllegalArgumentException se lo username o la password risultano nulli o vuoti, oppure se esiste già un utente registrato con lo stesso username
     * @throws IllegalStateException    se si verifica un errore durante l'hashing della password o se ci sono errori durante il salvataggio sul file CSV
     */
    public void registraCliente(String nomeInserito, String cognomeInserito, String usernameInserito, char[] passwordInChiaro, String luogoDomicilioInserito, Ruolo ruoloInserito, LocalDate dataNascitaInserita) {
        String username;
        Validatore.validaStringa(usernameInserito, "Lo username non può essere nullo o vuoto.");
        Validatore.validaOggetto(ruoloInserito);
        if (passwordInChiaro == null || passwordInChiaro.length == 0) {
            throw new IllegalArgumentException("La password non può essere nulla o vuota.");
        }
        username = usernameInserito.trim().toLowerCase();
        if (gestoreDati.cercaUtente(username) != null) {
            throw new IllegalArgumentException("Esiste già un utente con questo username.");
        }
        byte[] salt = Sicurezza.generaSalt();
        int iterazioni = Sicurezza.getIterazioni();
        String passwordHash = Sicurezza.aBase64(Sicurezza.hashPassword(passwordInChiaro, salt, iterazioni));
        String saltBase64 = Sicurezza.aBase64(salt);
        Utente utenteDaRegistrare;
        if (dataNascitaInserita != null) {
            utenteDaRegistrare = new Utente(nomeInserito, cognomeInserito, username, passwordHash, saltBase64, iterazioni, luogoDomicilioInserito, ruoloInserito, dataNascitaInserita);
        } else {
            utenteDaRegistrare = new Utente(nomeInserito, cognomeInserito, username, passwordHash, saltBase64, iterazioni, luogoDomicilioInserito, ruoloInserito);
        }
        gestoreDati.aggiungiUtente(utenteDaRegistrare);
        gestoreDati.salvaUtentiSuCSV();
    }

    /**
     * Esegue l'autenticazione di un utente tramite username e password in chiaro.
     * @param username         lo username dell'utente che tenta l'accesso
     * @param passwordInChiaro la password fornita dall'utente
     * @return un oggetto Utente se l'autenticazione ha successo, null se le credenziali non sono valide o se l'utente non esiste
     * @throws IllegalStateException    se si verifica un errore durante l'hashing della password o se ci sono errori durante il salvataggio sul file CSV
     * @throws IllegalArgumentException se si verifica un errore durante l'impostazione dei valori sull'utente, causa validazione fallita
     */
    public Utente accesso(String username, char[] passwordInChiaro) {
        if (username == null || username.isBlank() || passwordInChiaro == null || passwordInChiaro.length == 0) {
            return null;
        }
        Utente utenteCorrente = gestoreDati.cercaUtente(username);
        if (utenteCorrente != null) {
            String passwordHashPrevista = utenteCorrente.getPasswordHash();
            String salt = utenteCorrente.getSaltBase64();
            int iterazioniPreviste = utenteCorrente.getIterazioniHashing();
            if (Sicurezza.verificaLogin(passwordInChiaro, Sicurezza.daBase64(salt), Sicurezza.daBase64(passwordHashPrevista), iterazioniPreviste)) {
                int iterazioniNuove = Sicurezza.getIterazioni();
                if (iterazioniPreviste != iterazioniNuove) {
                    byte[] nuovoSalt = Sicurezza.generaSalt();
                    try {
                        byte[] nuovaHash = Sicurezza.hashPassword(passwordInChiaro, nuovoSalt, iterazioniNuove);
                        utenteCorrente.setPasswordHash(Sicurezza.aBase64(nuovaHash));
                        utenteCorrente.setSaltBase64(Sicurezza.aBase64(nuovoSalt));
                        utenteCorrente.setIterazioniHashing(iterazioniNuove);
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        utenteCorrente.setPasswordHash(passwordHashPrevista);
                        utenteCorrente.setSaltBase64(salt);
                        utenteCorrente.setIterazioniHashing(iterazioniPreviste);
                        throw e;
                    }
                    gestoreDati.salvaUtentiSuCSV();
                }
                return utenteCorrente;
            }
        }
        return null;
    }

    /**
     * Modifica i dati personali di un utente, aggiornando solo i campi per i quali viene fornito un nuovo valore valido.
     * @param utenteCorrente        l'utente che sta provando a modificare i propri dati
     * @param nuovoNome             il nuovo nome da impostare (parametro opzionale, se null viene ignorato)
     * @param nuovoCognome          il nuovo cognome da impostare (parametro opzionale, se null viene ignorato)
     * @param nuovoLuogoDomicilio   il nuovo luogo di domicilio da impostare (parametro opzionale, se null viene ignorato)
     * @param nuovaDataNascita      la nuova data di nascita da impostare (parametro opzionale, se null viene ignorato)
     * @param nuovaPasswordInChiaro la nuova password da impostare (parametro opzionale, se null viene ignorato)
     * @throws IllegalArgumentException se l'utente risulta nullo o se uno dei nuovi valori non è valido
     * @throws IllegalStateException    se si verifica un errore durante l'hashing della password o se ci sono errori durante il salvataggio sul file CSV
     */
    public void modificaDatiPersonali(Utente utenteCorrente, String nuovoNome, String nuovoCognome, String nuovoLuogoDomicilio, LocalDate nuovaDataNascita, char[] nuovaPasswordInChiaro) {
        Validatore.validaOggetto(utenteCorrente);
        String nomePrecedente = utenteCorrente.getNome();
        String cognomePrecedente = utenteCorrente.getCognome();
        String domicilioPrecedente = utenteCorrente.getLuogoDomicilio();
        LocalDate nascitaPrecedente = utenteCorrente.getDataNascita();
        String hashPrecedente = utenteCorrente.getPasswordHash();
        String saltPrecedente = utenteCorrente.getSaltBase64();
        int iterazioniPrecedenti = utenteCorrente.getIterazioniHashing();
        try {
            if (nuovoNome != null && !nuovoNome.isBlank()) {
                utenteCorrente.setNome(nuovoNome);
            }
            if (nuovoCognome != null && !nuovoCognome.isBlank()) {
                utenteCorrente.setCognome(nuovoCognome);
            }
            if (nuovoLuogoDomicilio != null && !nuovoLuogoDomicilio.isBlank()) {
                utenteCorrente.setLuogoDomicilio(nuovoLuogoDomicilio);
            }
            if (nuovaDataNascita != null) {
                utenteCorrente.setDataNascita(nuovaDataNascita);
            }
            if (nuovaPasswordInChiaro != null && nuovaPasswordInChiaro.length > 0) {
                byte[] nuovoSalt = Sicurezza.generaSalt();
                int iterazioni = Sicurezza.getIterazioni();
                byte[] nuovoHash = Sicurezza.hashPassword(nuovaPasswordInChiaro, nuovoSalt, iterazioni);
                utenteCorrente.setPasswordHash(Sicurezza.aBase64(nuovoHash));
                utenteCorrente.setSaltBase64(Sicurezza.aBase64(nuovoSalt));
                utenteCorrente.setIterazioniHashing(iterazioni);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            utenteCorrente.setNome(nomePrecedente);
            utenteCorrente.setCognome(cognomePrecedente);
            utenteCorrente.setLuogoDomicilio(domicilioPrecedente);
            if (nascitaPrecedente != null) {
                utenteCorrente.setDataNascita(nascitaPrecedente);
            }
            utenteCorrente.setPasswordHash(hashPrecedente);
            utenteCorrente.setSaltBase64(saltPrecedente);
            utenteCorrente.setIterazioniHashing(iterazioniPrecedenti);
            throw e;
        }
        gestoreDati.salvaUtentiSuCSV();
    }
}
