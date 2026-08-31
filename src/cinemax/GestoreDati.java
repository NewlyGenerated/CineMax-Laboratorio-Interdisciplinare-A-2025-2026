// Kejsi Xhafaj, 759934, VA
package cinemax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Contiene i metodi usati per operare sui file CSV, utilizzandoli per salvare e caricare
 * i dati necessari per il funzionamento corretto del sistema.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class GestoreDati {
    /**
     * L'attributo <code>FILE_PROIEZIONI</code> definisce il nome del file CSV utilizzato per la memorizzazione dei dati delle proiezioni.
     */
    private static final String FILE_PROIEZIONI = "proiezioni.csv";
    /**
     * L'attributo <code>FILE_UTENTI</code> definisce il nome del file CSV dedicato al salvataggio e recupero delle informazioni degli utenti.
     */
    private static final String FILE_UTENTI = "utenti.csv";
    /**
     * L'attributo <code>FILE_PRENOTAZIONI</code> definisce il nome del file CSV utilizzato per la memorizzazione dei dati delle prenotazioni effettuate.
     */
    private static final String FILE_PRENOTAZIONI = "prenotazioni.csv";
    /**
     * L'attributo <code>DIVISORE_CSV</code> specifica la stringa o il carattere utilizzato per separare i valori all'interno delle righe dei file CSV.
     */
    private static final String DIVISORE_CSV = ",";
    /**
     * L'attributo <code>CARTELLA_DATA</code> rappresenta il percorso della cartella in cui sono contenuti e in cui verranno cercati i file CSV dei dati.
     */
    private static final Path CARTELLA_DATA = Files.exists(Paths.get("data")) ? Paths.get("data") : Paths.get("..", "data");
    /**
     * L'attributo <code>PERCORSO_PROIEZIONI</code> definisce il percorso completo per accedere al file delle proiezioni, a partire dalla cartella base dei dati.
     */
    private static final Path PERCORSO_PROIEZIONI = CARTELLA_DATA.resolve(FILE_PROIEZIONI);
    /**
     * L'attributo <code>PERCORSO_UTENTI</code> definisce il percorso completo per accedere al file degli utenti, a partire dalla cartella base dei dati.
     */
    private static final Path PERCORSO_UTENTI = CARTELLA_DATA.resolve(FILE_UTENTI);
    /**
     * L'attributo <code>PERCORSO_PRENOTAZIONI</code> definisce il percorso completo per accedere al file delle prenotazioni, a partire dalla cartella base dei dati.
     */
    private static final Path PERCORSO_PRENOTAZIONI = CARTELLA_DATA.resolve(FILE_PRENOTAZIONI);
    /**
     * L'attributo <code>palinsesto</code> gestisce e memorizza la collezione delle proiezioni cinematografiche attualmente programmate.
     */
    private final Palinsesto palinsesto;
    /**
     * L'attributo <code>listaUtenti</code> mantiene in memoria l'elenco di tutti gli utenti registrati all'interno del sistema.
     */
    private final List<Utente> listaUtenti;
    /**
     * L'attributo <code>listaPrenotazioni</code> mantiene in memoria l'elenco di tutte le prenotazioni effettuate dagli utenti.
     */
    private final List<Prenotazione> listaPrenotazioni;

    /**
     * Inizializza il gestore dei dati istanziando le strutture dati principali
     * (palinsesto, lista utenti e lista prenotazioni) e le popola immediatamente caricando
     * i dati dai rispettivi file CSV.
     */
    public GestoreDati() {
        palinsesto = new Palinsesto();
        listaUtenti = new LinkedList<>();
        listaPrenotazioni = new LinkedList<>();
        caricaProiezioniDaCSV();
        caricaUtentiDaCSV();
        caricaPrenotazioniDaCSV();
        allineaPostiPrenotati();
    }

    /**
     * Carica all'interno del palinsesto tutte le proiezioni contenute nel relativo file CSV.
     */
    private void caricaProiezioniDaCSV() {
        boolean fileDaAggiornare = false; // per il file senza UUID
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (BufferedReader br = Files.newBufferedReader(PERCORSO_PROIEZIONI)) {
            // per saltare la prima riga del CSV (che sarebbe l'intestazione del file)
            String linea = br.readLine();
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] datiProiezione = separaRigaCSV(linea, false);
                Proiezione proiezione = null;
                try {
                    if (datiProiezione.length == 8) {
                        LocalDateTime inizioProiezione = LocalDateTime.parse(datiProiezione[0], formatoData);
                        String titolo = datiProiezione[1];
                        Genere genere = Genere.daStringa(datiProiezione[2]);
                        String regista = datiProiezione[3];
                        int anno = Integer.parseInt(datiProiezione[4]);
                        int durata = Integer.parseInt(datiProiezione[5]);
                        int etaMinima = Integer.parseInt(datiProiezione[6]);
                        double prezzo = Double.parseDouble(datiProiezione[7]);
                        LocalDateTime fineProiezione = inizioProiezione.plusMinutes(durata);
                        Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                        proiezione = new Proiezione(inizioProiezione, fineProiezione, film, prezzo, 0);
                        fileDaAggiornare = true;
                    } else if (datiProiezione.length == 11) {
                        String UUID = datiProiezione[0];
                        LocalDateTime inizioProiezione = LocalDateTime.parse(datiProiezione[1], formatoData);
                        LocalDateTime fineProiezione = LocalDateTime.parse(datiProiezione[2], formatoData);
                        String titolo = datiProiezione[3];
                        Genere genere = Genere.daStringa(datiProiezione[4]);
                        String regista = datiProiezione[5];
                        int anno = Integer.parseInt(datiProiezione[6]);
                        int durata = Integer.parseInt(datiProiezione[7]);
                        int etaMinima = Integer.parseInt(datiProiezione[8]);
                        double prezzo = Double.parseDouble(datiProiezione[9]);
                        int postiPrenotati = Integer.parseInt(datiProiezione[10]);
                        Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                        proiezione = new Proiezione(UUID, inizioProiezione, fineProiezione, film, prezzo, postiPrenotati);
                    }
                    if (proiezione != null) {
                        palinsesto.aggiungiProiezione(proiezione);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Errore di validazione nella riga del CSV (" + linea + "): " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Errore generico nella riga del CSV (" + linea + "): " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante il parsing di proiezioni.csv: " + e.getMessage());
        }
        if (fileDaAggiornare) {
            try {
                salvaProiezioniSuCSV();
            } catch (IllegalStateException e) {
                System.err.println("Impossibile aggiornare proiezioni.csv al formato nuovo: " + e.getMessage());
            }
        }
    }

    /**
     * Carica all'interno della lista utenti tutti gli utenti presenti nel relativo file CSV.
     */
    private void caricaUtentiDaCSV() {
        try (BufferedReader br = Files.newBufferedReader(PERCORSO_UTENTI)) {
            String linea = br.readLine();
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] datiUtente = separaRigaCSV(linea, true);
                try {
                    String nome = datiUtente[0];
                    String cognome = datiUtente[1];
                    String username = datiUtente[2];
                    String passwordHash = datiUtente[3];
                    String saltBase64 = datiUtente[4];
                    int iterazioniHashing = Integer.parseInt(datiUtente[5]);
                    String luogoDomicilio = datiUtente[6];
                    Ruolo ruolo = Ruolo.trovaRuoloDaStringa(datiUtente[7]);
                    String dataNascitaStringa = datiUtente[8];
                    Utente utente;
                    if (dataNascitaStringa != null && !dataNascitaStringa.isBlank()) {
                        LocalDate dataNascita = LocalDate.parse(dataNascitaStringa);
                        utente = new Utente(nome, cognome, username, passwordHash, saltBase64, iterazioniHashing, luogoDomicilio, ruolo, dataNascita);
                    } else {
                        utente = new Utente(nome, cognome, username, passwordHash, saltBase64, iterazioniHashing, luogoDomicilio, ruolo);
                    }
                    aggiungiUtente(utente);
                } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                    System.err.println("Errore di validazione nella riga del CSV (" + linea + "): " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Errore generico nella riga del CSV (" + linea + "): " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante il parsing di utenti.csv: " + e.getMessage());
        }
    }

    /**
     * Carica all'interno della lista prenotazioni tutte le prenotazioni presenti nel relativo file CSV.
     */
    private void caricaPrenotazioniDaCSV() {
        try (BufferedReader br = Files.newBufferedReader(PERCORSO_PRENOTAZIONI)) {
            String linea = br.readLine();
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] datiPrenotazione = separaRigaCSV(linea, false);
                try {
                    String codiceUnivoco = datiPrenotazione[0];
                    String usernameUtente = datiPrenotazione[1];
                    String codiceProiezione = datiPrenotazione[2];
                    int numeroBiglietti = Integer.parseInt(datiPrenotazione[3]);
                    Prenotazione prenotazione = new Prenotazione(codiceUnivoco, usernameUtente, codiceProiezione, numeroBiglietti);
                    aggiungiPrenotazione(prenotazione);
                } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                    System.err.println("Errore di validazione nella riga del CSV (" + linea + "): " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Errore generico nella riga del CSV (" + linea + "): " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante il parsing di prenotazioni.csv: " + e.getMessage());
        }
    }

    /**
     * Sovrascrive il file CSV delle proiezioni con i nuovi dati presenti all'interno del palinsesto.
     * @throws IllegalStateException se ci sono errori durante il salvataggio sul file CSV
     */
    public void salvaProiezioniSuCSV() {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (BufferedWriter bw = Files.newBufferedWriter(PERCORSO_PROIEZIONI)) {
            String intestazione = String.join(DIVISORE_CSV,
                    "codice_univoco",
                    "data_ora_inizio_proiezione",
                    "data_ora_fine_proiezione",
                    "titolo_film",
                    "genere",
                    "regista",
                    "anno",
                    "durata_minuti",
                    "eta_minima",
                    "prezzo_biglietto",
                    "posti_prenotati");
            bw.write(intestazione);
            bw.newLine();
            for (Proiezione p : palinsesto.getListaProiezioni()) {
                Film f = p.getFilmProiettato();
                String dataFormattata = p.getInizioProiezione().format(formatoData);
                String dataFineFormattata = p.getFineProiezione().format(formatoData);
                String riga = String.join(DIVISORE_CSV,
                        proteggiCampoCSV(p.getCodiceUnivoco()),
                        dataFormattata,
                        dataFineFormattata,
                        proteggiCampoCSV(f.getTitolo()),
                        f.getGenere().name(),
                        proteggiCampoCSV(f.getRegista()),
                        String.valueOf(f.getAnno()),
                        String.valueOf(f.getDurata()),
                        String.valueOf(f.getEtaMinima()),
                        String.valueOf(p.getPrezzoBiglietto()),
                        String.valueOf(p.getPostiPrenotati()));
                bw.write(riga);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Errore durante il salvataggio del file proiezioni.csv", e);
        }
    }

    /**
     * Sovrascrive il file CSV degli utenti con i nuovi dati presenti all'interno della lista utenti.
     * @throws IllegalStateException se ci sono errori durante il salvataggio sul file CSV
     */
    public void salvaUtentiSuCSV() {
        try (BufferedWriter bw = Files.newBufferedWriter(PERCORSO_UTENTI)) {
            String intestazione = String.join(DIVISORE_CSV,
                    "nome",
                    "cognome",
                    "username",
                    "password_hash",
                    "salt",
                    "iterazioni_hashing",
                    "luogo_domicilio",
                    "ruolo",
                    "data_nascita");
            bw.write(intestazione);
            bw.newLine();
            for (Utente u : listaUtenti) {
                String dataNascitaStringa = (u.getDataNascita() != null) ? u.getDataNascita().toString() : "";
                String riga = String.join(DIVISORE_CSV,
                        proteggiCampoCSV(u.getNome()),
                        proteggiCampoCSV(u.getCognome()),
                        proteggiCampoCSV(u.getUsername()),
                        proteggiCampoCSV(u.getPasswordHash()),
                        proteggiCampoCSV(u.getSaltBase64()),
                        String.valueOf(u.getIterazioniHashing()),
                        proteggiCampoCSV(u.getLuogoDomicilio()),
                        u.getRuolo().name(),
                        dataNascitaStringa);
                bw.write(riga);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Errore durante il salvataggio del file utenti.csv", e);
        }
    }

    /**
     * Sovrascrive il file CSV delle prenotazioni con i nuovi dati presenti all'interno della lista prenotazioni.
     * @throws IllegalStateException se ci sono errori durante il salvataggio sul file CSV
     */
    public void salvaPrenotazioniSuCSV() {
        try (BufferedWriter bw = Files.newBufferedWriter(PERCORSO_PRENOTAZIONI)) {
            String intestazione = String.join(DIVISORE_CSV,
                    "codice_univoco",
                    "username_utente",
                    "codice_proiezione",
                    "numero_biglietti");
            bw.write(intestazione);
            bw.newLine();
            for (Prenotazione p : listaPrenotazioni) {
                String riga = String.join(DIVISORE_CSV,
                        proteggiCampoCSV(p.getCodiceUnivoco()),
                        proteggiCampoCSV(p.getUsernameUtente()),
                        proteggiCampoCSV(p.getCodiceProiezione()),
                        String.valueOf(p.getNumeroBiglietti()));
                bw.write(riga);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Errore durante il salvataggio del file prenotazioni.csv", e);
        }
    }

    /**
     * Restituisce il palinsesto.
     * @return il palinsesto
     */
    public Palinsesto getPalinsesto() {
        return palinsesto;
    }

    /**
     * Restituisce la lista delle prenotazioni in modalità di sola lettura, impedendo che possa venir modificata erroneamente.
     * @return una lista non modificabile delle prenotazioni
     */
    public List<Prenotazione> getListaPrenotazioni() {
        return Collections.unmodifiableList(listaPrenotazioni);
    }

    /**
     * Aggiunge una prenotazione alla lista prenotazioni, dopo averla validata.
     * @param prenotazione la prenotazione da aggiungere
     * @throws IllegalArgumentException se la prenotazione passata è nulla
     */
    public void aggiungiPrenotazione(Prenotazione prenotazione) {
        Validatore.validaOggetto(prenotazione);
        listaPrenotazioni.add(prenotazione);
    }

    /**
     * Rimuove una prenotazione dalla lista prenotazioni, dopo averla validata.
     * @param prenotazione la prenotazione da rimuovere
     * @throws IllegalArgumentException se la prenotazione passata è nulla
     */
    public void rimuoviPrenotazione(Prenotazione prenotazione) {
        Validatore.validaOggetto(prenotazione);
        listaPrenotazioni.remove(prenotazione);
    }

    /**
     * Ricerca una prenotazione tramite il suo codice univoco, validando prima il codice passato come parametro.
     * @param codicePrenotazione il codice usato per la ricerca
     * @return la prenotazione trovata, null se non viene trovata
     * @throws IllegalArgumentException se il codice passato è nullo o vuoto
     */
    public Prenotazione cercaPrenotazioneTramiteCodice(String codicePrenotazione) {
        Validatore.validaStringa(codicePrenotazione, "Il codice della prenotazione da cercare non può essere nullo o vuoto.");
        for (Prenotazione p : listaPrenotazioni) {
            if (p.getCodiceUnivoco().equalsIgnoreCase(codicePrenotazione)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Restituisce la lista degli utenti in modalità solo lettura, impedendo che possa venir modificata erroneamente.
     * @return una lista non modificabile degli utenti
     */
    public List<Utente> getListaUtenti() {
        return Collections.unmodifiableList(listaUtenti);
    }

    /**
     * Aggiunge un utente alla lista utenti, dopo averlo validato.
     * @param utente l'utente da aggiungere
     * @throws IllegalArgumentException se l'utente passato è nullo o se esiste già un utente con quell'username
     */
    public void aggiungiUtente(Utente utente) {
        Validatore.validaOggetto(utente);
        if (cercaUtente(utente.getUsername()) != null) {
            throw new IllegalArgumentException("Esiste già un utente con username: " + utente.getUsername());
        }
        listaUtenti.add(utente);
    }

    /**
     * Rimuove un utente dalla lista utenti, dopo averlo validato.
     * @param utente l'utente da rimuovere
     * @throws IllegalArgumentException se l'utente passato è nullo
     */
    public void rimuoviUtente(Utente utente) {
        Validatore.validaOggetto(utente);
        listaUtenti.remove(utente);
    }

    /**
     * Ricerca un utente all'interno della lista utenti tramite il suo username (univoco).
     * @param username lo username dell'utente da cercare
     * @return l'utente trovato, null se non viene trovato
     * @throws IllegalArgumentException se lo username passato è nullo o vuoto
     */
    public Utente cercaUtente(String username) {
        Validatore.validaStringa(username, "Lo username da cercare non può essere nullo o vuoto.");
        for (Utente utenteDaCercare : listaUtenti) {
            if (utenteDaCercare.getUsername().equalsIgnoreCase(username.trim().toLowerCase())) {
                return utenteDaCercare;
            }
        }
        return null;
    }

    /**
     * Separa una riga CSV nei suoi campi, ripulendoli dalle eventuali virgolette di protezione.
     * @param linea         la riga da separare
     * @param mantieniVuoti se vero, i campi vuoti in coda vengono conservati
     * @return l'array dei campi ripuliti
     */
    private String[] separaRigaCSV(String linea, boolean mantieniVuoti) {
        String[] campi = linea.split(DIVISORE_CSV + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", mantieniVuoti ? -1 : 0);
        for (int i = 0; i < campi.length; i++) {
            campi[i] = pulisciCampoCSV(campi[i]);
        }
        return campi;
    }

    /**
     * Rimuove le virgolette di protezione da un campo CSV, ripristinando quelle raddoppiate.
     * @param campo il campo da ripulire
     * @return il campo nella sua forma originale
     */
    private String pulisciCampoCSV(String campo) {
        String risultato = campo.trim();
        if (risultato.length() >= 2 && risultato.startsWith("\"") && risultato.endsWith("\"")) {
            risultato = risultato.substring(1, risultato.length() - 1).replace("\"\"", "\"");
        }
        return risultato;
    }

    /**
     * Protegge un campo racchiudendolo fra virgolette se contiene caratteri che rompono il formato CSV.
     * @param campo il campo da proteggere
     * @return il campo pronto per essere scritto su file
     */
    private String proteggiCampoCSV(String campo) {
        if (campo == null) {
            return "";
        }
        if (campo.contains(DIVISORE_CSV) || campo.contains("\"") || campo.contains("\n") || campo.contains("\r")) {
            return "\"" + campo.replace("\"", "\"\"") + "\"";
        }
        return campo;
    }

    /**
     * Ricalcola i posti prenotati di ogni proiezione a partire dalle prenotazioni effettivamente caricate, garantendo la coerenza
     * fra i due file anche in caso di righe scartate.
     */
    private void allineaPostiPrenotati() {
        for (Proiezione p : palinsesto.getListaProiezioni()) {
            int totale = 0;
            for (Prenotazione pr : listaPrenotazioni) {
                if (pr.getCodiceProiezione().equalsIgnoreCase(p.getCodiceUnivoco())) {
                    totale += pr.getNumeroBiglietti();
                }
            }
            if (totale != p.getPostiPrenotati()) {
                try {
                    p.setPostiPrenotati(totale);
                    System.err.println("Posti prenotati riallineati per la proiezione " + p.getCodiceUnivoco() + ".");
                } catch (PostiEsauritiException e) {
                    System.err.println("Prenotazioni incoerenti per la proiezione " + p.getCodiceUnivoco() + ": " + e.getMessage());
                }
            }
        }
    }
}
