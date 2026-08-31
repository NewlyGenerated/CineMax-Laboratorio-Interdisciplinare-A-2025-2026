// Kejsi Xhafaj, 759934, VA
package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Rappresenta l'interfaccia utente a riga di comando dell'applicazione.
 * <p>
 * Questa classe è il punto di ingresso per l'interazione con l'utente. Ha la responsabilità di:
 * <ul>
 *     <li>Mostrare i menu e gli output delle operazioni.</li>
 *     <li>Acquisire e gestire gli input dell'utente.</li>
 *     <li>Mandare i vari comandi e operazioni richieste dall'utente al backend.</li>
 * </ul>
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class InterfacciaTerminale {
    /**
     * L'attributo <code>FORMATO_PROMPT</code> definisce il formato richiesto nei prompt per le date.
     */
    private static final DateTimeFormatter FORMATO_PROMPT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    /**
     * L'attributo <code>scanner</code> gestisce la lettura dell'input inserito dagli utenti.
     */
    private final Scanner scanner;
    /**
     * L'attributo <code>gestoreUtenti</code> memorizza il riferimento al gestore utenti che si occupa di tutte le operazioni relative agli utenti.
     */
    private GestoreUtenti gestoreUtenti;
    /**
     * L'attributo <code>gestoreProiezioni</code> memorizza il riferimento al gestore proiezioni che si occupa di tutte le operazioni relative alle proiezioni.
     */
    private GestoreProiezioni gestoreProiezioni;
    /**
     * L'attributo <code>gestorePrenotazioni</code> memorizza il riferimento al gestore prenotazioni che si occupa di tutte le operazioni relative alle proiezioni.
     */
    private GestorePrenotazioni gestorePrenotazioni;
    /**
     * L'attributo <code>utenteCorrente</code> memorizza l'utente che sta correntemente utilizzando il sistema.
     */
    private Utente utenteCorrente;

    /**
     * Costruisce l'interfaccia terminale, inizializzando lo scanner che verrà usato per gestire l'input degli utenti e impostando
     * l'utente corrente a nullo, considerandolo fino al suo login un utente di tipo guest.
     */
    public InterfacciaTerminale() {
        this.scanner = new Scanner(System.in);
        this.utenteCorrente = null;
    }

    /**
     * Inizializza i gestori e mostra il menu principale del sistema.
     * @param gestoreUtenti       il GestoreUtenti del sistema, gestisce i dati relativi agli utenti
     * @param gestoreProiezioni   il GestoreProiezioni del sistema, gestisce i dati relativi alle proiezioni
     * @param gestorePrenotazioni il GestorePrenotazioni del sistema, gestisce i dati relativi alle prenotazioni
     */
    public void avvia(GestoreUtenti gestoreUtenti, GestoreProiezioni gestoreProiezioni, GestorePrenotazioni gestorePrenotazioni) {
        this.gestoreUtenti = gestoreUtenti;
        this.gestoreProiezioni = gestoreProiezioni;
        this.gestorePrenotazioni = gestorePrenotazioni;
        System.out.println("=============================");
        System.out.println("     BENVENUTO IN CINEMAX    ");
        System.out.println("=============================");
        boolean inEsecuzione = true;
        while (inEsecuzione) {
            if (utenteCorrente == null) {
                inEsecuzione = mostraMenuIniziale();
            } else {
                if (utenteCorrente.isCliente()) {
                    mostraMenuCliente();
                } else if (utenteCorrente.isProiezionista()) {
                    mostraMenuProiezionista();
                } else if (utenteCorrente.isBigliettaio()) {
                    mostraMenuBigliettaio();
                }
            }
        }
        System.out.println("Chiusura in corso... Grazie per aver usato CineMax. Arrivederci!");
    }

    /**
     * Mostra le opzioni base per gli utenti non loggati.
     * @return vero se il programma deve continuare, falso se l'utente vuole uscire
     */
    private boolean mostraMenuIniziale() {
        System.out.println("\n--- MENU PRINCIPALE ---");
        System.out.println("1. Login");
        System.out.println("2. Registrati alla piattaforma");
        System.out.println("3. Prosegui come Ospite");
        System.out.println("0. Esci dal programma");
        Integer scelta = leggiIntero("Scegli un'opzione indicandone il numero:", true);
        switch (scelta) {
            case 1:
                eseguiLogin();
                break;
            case 2:
                eseguiRegistrazione();
                break;
            case 3:
                mostraMenuOspite();
                break;
            case 0:
                return false;
            default:
                System.out.println("Opzione non valida. Riprova");
        }
        return true;
    }

    /**
     * Mostra il menu dedicato agli ospiti, elencando numericamente tutte le operazioni disponibili.
     */
    private void mostraMenuOspite() {
        boolean inMenuOspite = true;
        while (inMenuOspite) {
            System.out.println("\n--- MENU OSPITE ---");
            System.out.println("1. Cerca e visualizza proiezioni");
            System.out.println("2. Effettua il login");
            System.out.println("3. Registrati come nuovo cliente");
            System.out.println("0. Torna al menu principale");
            Integer scelta = leggiIntero("Scegli un'opzione indicandone il numero:", true);
            switch (scelta) {
                case 1:
                    gestisciRicercaProiezioni();
                    break;
                case 2:
                    eseguiLogin();
                    if (utenteCorrente != null) {
                        inMenuOspite = false;
                    }
                    break;
                case 3:
                    eseguiRegistrazione();
                    break;
                case 0:
                    inMenuOspite = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova");
            }
        }
    }

    /**
     * Mostra il menu dedicato ai clienti, elencando numericamente tutte le operazioni disponibili per il loro ruolo.
     */
    private void mostraMenuCliente() {
        boolean inMenuCliente = true;
        while (inMenuCliente) {
            System.out.println("\n--- MENU CLIENTE ---");
            System.out.println("1. Cerca e visualizza proiezioni");
            System.out.println("2. Inserisci una nuova prenotazione");
            System.out.println("3. Visualizza le tue prenotazioni");
            System.out.println("4. Modifica una tua prenotazione");
            System.out.println("5. Annulla una tua prenotazione");
            System.out.println("6. Mostra i tuoi dati personali");
            System.out.println("7. Modifica i tuoi dati personali");
            System.out.println("0. Logout");
            Integer scelta = leggiIntero("Scegli un'opzione indicandone il numero:", true);
            switch (scelta) {
                case 1:
                    gestisciRicercaProiezioni();
                    break;
                case 2:
                    gestisciCreazionePrenotazione();
                    break;
                case 3:
                    stampaPropriePrenotazioni();
                    break;
                case 4:
                    gestisciModificaPrenotazione();
                    break;
                case 5:
                    gestisciEliminazionePrenotazione();
                    break;
                case 6:
                    gestisciMostraDatiUtente();
                    break;
                case 7:
                    gestisciModificaDatiPersonali();
                    break;
                case 0:
                    System.out.println("Logout effettuato.");
                    this.utenteCorrente = null;
                    inMenuCliente = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova");
            }
        }
    }

    /**
     * Mostra il menu dedicato ai proiezionisti, elencando numericamente tutte le operazioni disponibili per il loro ruolo.
     */
    private void mostraMenuProiezionista() {
        boolean inMenuProiezionista = true;
        while (inMenuProiezionista) {
            System.out.println("\n--- MENU PROIEZIONISTA ---");
            System.out.println("1. Cerca e visualizza proiezioni");
            System.out.println("2. Aggiungi una nuova proiezione");
            System.out.println("3. Modifica una proiezione esistente");
            System.out.println("4. Elimina una proiezione esistente");
            System.out.println("5. Mostra i tuoi dati personali");
            System.out.println("6. Modifica i tuoi dati personali");
            System.out.println("0. Logout");
            Integer scelta = leggiIntero("Scegli un'opzione indicandone il numero:", true);
            switch (scelta) {
                case 1:
                    gestisciRicercaProiezioni();
                    break;
                case 2:
                    gestisciAggiuntaProiezione();
                    break;
                case 3:
                    gestisciModificaProiezione();
                    break;
                case 4:
                    gestisciEliminazioneProiezione();
                    break;
                case 5:
                    gestisciMostraDatiUtente();
                    break;
                case 6:
                    gestisciModificaDatiPersonali();
                    break;
                case 0:
                    System.out.println("Logout effettuato.");
                    this.utenteCorrente = null;
                    inMenuProiezionista = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova");
            }
        }
    }

    /**
     * Mostra il menu dedicato ai bigliettai, elencando numericamente tutte le operazioni disponibili per il loro ruolo.
     */
    private void mostraMenuBigliettaio() {
        boolean inMenuBigliettaio = true;
        while (inMenuBigliettaio) {
            System.out.println("\n--- MENU BIGLIETTAIO ---");
            System.out.println("1. Cerca e visualizza proiezioni");
            System.out.println("2. Cerca una prenotazione");
            System.out.println("3. Visualizza le prenotazioni odierne");
            System.out.println("4. Mostra i tuoi dati personali");
            System.out.println("5. Modifica i tuoi dati personali");
            System.out.println("0. Logout");
            Integer scelta = leggiIntero("Scegli un'opzione indicandone il numero:", true);
            switch (scelta) {
                case 1:
                    gestisciRicercaProiezioni();
                    break;
                case 2:
                    gestisciRicercaPrenotazioni();
                    break;
                case 3:
                    gestisciPrenotazioniOdierne();
                    break;
                case 4:
                    gestisciMostraDatiUtente();
                    break;
                case 5:
                    gestisciModificaDatiPersonali();
                    break;
                case 0:
                    System.out.println("Logout effettuato.");
                    this.utenteCorrente = null;
                    inMenuBigliettaio = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova");
            }
        }
    }

    /**
     * Gestisce la procedura di login chiedendo le credenziali all'utente, in caso di successo imposta l'utente loggato come utente corrente del sistema.
     */
    private void eseguiLogin() {
        System.out.println("\n--- ACCESSO ---");
        String username = leggiStringa("Username:", true);
        char[] password = leggiPassword("Password:", true);
        try {
            Utente utenteLoggato = gestoreUtenti.accesso(username, password);
            if (utenteLoggato != null) {
                this.utenteCorrente = utenteLoggato;
                System.out.println("Accesso effettuato con successo. Bentornato, " + utenteCorrente.getNome() + "!");
            } else {
                System.out.println("Errore: Username o password errati. Riprova.");
            }
        } catch (Exception e) {
            System.out.println("Si è verificato un imprevisto: " + e.getMessage());
        } finally {
            if (password != null) {
                Arrays.fill(password, '\0');
            }
        }
    }

    /**
     * Gestisce la procedura di registrazione di un nuovo cliente chiedendo all'utente i dati necessari e passandoli al GestoreUtenti.
     */
    private void eseguiRegistrazione() {
        System.out.println("\n--- REGISTRAZIONE NUOVO CLIENTE ---");
        System.out.println("Inserisci i tuoi dati per creare un account.");
        String nome = leggiStringa("Nome:", true);
        String cognome = leggiStringa("Cognome:", true);
        String username = leggiStringa("Username (senza spazi o caratteri speciali):", true);
        char[] password = leggiPassword("Password:", true);
        String luogoDomicilio = leggiStringa("Luogo di domicilio:", true);
        LocalDate dataNascita = leggiData("Data di nascita", false);
        try {
            gestoreUtenti.registraCliente(nome, cognome, username, password, luogoDomicilio, Ruolo.CLIENTE, dataNascita);
            System.out.println("Registrazione completata con successo! Ora puoi effettuare il login.");
        } catch (IllegalArgumentException e) {
            System.out.println("Errore durante la registrazione: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Si è verificato un imprevisto: " + e.getMessage());
        } finally {
            if (password != null) {
                Arrays.fill(password, '\0');
            }
        }
        tornaAlMenu();
    }

    /**
     * Stampa a schermo i dati personali dell'utente attualmente autenticato.
     */
    private void stampaDatiUtente() {
        System.out.println("\n--- I TUOI DATI PERSONALI ---");
        System.out.println(utenteCorrente.toString());
    }

    /**
     * Mostra a schermo i dati personali dell'utente attualmente autenticato e attende conferma prima di tornare al menu.
     */
    private void gestisciMostraDatiUtente() {
        stampaDatiUtente();
        tornaAlMenu();
    }

    /**
     * Gestisce il flusso di interazione con l'utente per la modifica dei dati personali.
     * Mostra i dati attuali, raccoglie i nuovi input opzionali e invoca il gestore per l'aggiornamento, notificando l'esito dell'operazione.
     */
    private void gestisciModificaDatiPersonali() {
        stampaDatiUtente();
        System.out.println("\n--- MODIFICA I TUOI DATI PERSONALI ---");
        String scelta = leggiStringa("\nVuoi modificare i tuoi dati personali? (S/N)", true).toUpperCase();
        if (scelta.equals("S")) {
            System.out.println("\nInserisci i nuovi dati. Se non vuoi modificare un campo, premi semplicemente Invio.");
            String nuovoNome = leggiStringa("Nuovo nome", false);
            String nuovoCognome = leggiStringa("Nuovo cognome", false);
            char[] nuovaPassword = leggiPassword("Nuova password:", false);
            String nuovoLuogoDomicilio = leggiStringa("Nuovo luogo di domicilio", false);
            LocalDate dataNascita = leggiData("Nuova data di nascita", false);
            try {
                gestoreUtenti.modificaDatiPersonali(utenteCorrente, nuovoNome, nuovoCognome, nuovoLuogoDomicilio, dataNascita, nuovaPassword);
                System.out.println("Dati aggiornati con successo!");
            } catch (IllegalArgumentException e) {
                System.out.println("Errore durante la modifica dei dati: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Errore generico: " + e.getMessage());
            } finally {
                if (nuovaPassword != null) {
                    Arrays.fill(nuovaPassword, '\0');
                }
            }
        } else {
            System.out.println("Hai scelto di non modificare i tuoi dati.");
        }
        tornaAlMenu();
    }

    /**
     * Gestisce la ricerca delle proiezioni, consentendo di inserire filtri a scelta.
     */
    private void gestisciRicercaProiezioni() {
        System.out.println("\n--- RICERCA PROIEZIONI ---");
        System.out.println("Inserisci i criteri di ricerca (premi semplicemente Invio per saltare un filtro).");
        String titolo = leggiStringa("Inserisci il titolo del film (anche parziale)", false);
        Genere genere = leggiGenere("Inserisci il genere", false);
        LocalDate dataInizio = leggiData("Inserisci la data a partire dalla quale cercare", false);
        if (dataInizio == null) {
            dataInizio = LocalDate.now();
        }
        LocalDate dataFine = leggiData("Inserisci la data limite di ricerca", false);
        Double prezzoMinimo = leggiDouble("Inserisci il prezzo minimo del biglietto", false);
        Double prezzoMassimo = leggiDouble("Inserisci il prezzo massimo del biglietto", false);
        System.out.println("\nRicerca in corso...");
        List<Proiezione> risultati = gestoreProiezioni.cercaProiezione(titolo, genere, dataInizio, dataFine, prezzoMassimo, prezzoMinimo);
        Proiezione proiezioneSelezionata = mostraESelezionaProiezione(risultati);
        if (proiezioneSelezionata != null) {
            System.out.println("\nHai selezionato la seguente proiezione:\n" + gestoreProiezioni.visualizzaProiezione(proiezioneSelezionata));
            if (utenteCorrente == null) {
                System.out.println("Per prenotare i posti, effettua la registrazione al sistema oppure se sei già un utente esegui il login.");
            }
        }
        tornaAlMenu();
    }

    /**
     * Gestisce la creazione di una nuova prenotazione chiedendo i dati al cliente.
     */
    private void gestisciCreazionePrenotazione() {
        System.out.println("\n--- NUOVA PRENOTAZIONE ---");
        System.out.println("Prima di prenotare, cerca la proiezione desiderata.");
        String titolo = leggiStringa("Inserisci il titolo del film (o premi Invio per vederli tutti).", false);
        Genere genere = leggiGenere("Inserisci il genere", false);
        LocalDate dataFine = leggiData("Inserisci la data limite di ricerca.", false);
        Double prezzoMinimo = leggiDouble("Inserisci il prezzo minimo del biglietto.", false);
        Double prezzoMassimo = leggiDouble("Inserisci il prezzo massimo del biglietto.", false);
        List<Proiezione> risultati = gestoreProiezioni.cercaProiezioniPrenotabili(titolo, genere, dataFine, prezzoMassimo, prezzoMinimo);
        Proiezione proiezioneSelezionata = mostraESelezionaProiezione(risultati);
        if (proiezioneSelezionata == null) {
            System.out.println("Operazione annullata.");
            return;
        }
        String codiceProiezione = proiezioneSelezionata.getCodiceUnivoco();
        Integer numeroBiglietti = leggiIntero("Quanti biglietti vuoi prenotare per " + proiezioneSelezionata.getFilmProiettato().getTitolo() + "?", true);
        try {
            gestorePrenotazioni.creaPrenotazione(utenteCorrente, codiceProiezione, numeroBiglietti);
            System.out.println("Prenotazione effettuata con successo!");
        } catch (Exception e) {
            System.out.println("Errore generico: " + e.getMessage());
        }
        tornaAlMenu();
    }

    /**
     * Mostra una lista di prenotazioni impaginate e permette all'utente di scorrere le pagine o selezionarne una.
     * @param risultati la lista di prenotazioni da mostrare
     * @return la prenotazione selezionata, oppure null se l'utente annulla l'operazione
     */
    private Prenotazione mostraESelezionaPrenotazione(List<Prenotazione> risultati) {
        if (risultati == null || risultati.isEmpty()) {
            System.out.println("Nessuna prenotazione trovata con questi criteri.");
            return null;
        }
        int elementiPerPagina = 10;
        int totalePagine = (int) Math.ceil((double) risultati.size() / elementiPerPagina);
        int paginaCorrente = 0;
        while (true) {
            System.out.println("\n--- Lista prenotazioni (Pagina " + (paginaCorrente + 1) + " di " + totalePagine + ") ---");
            int inizio = paginaCorrente * elementiPerPagina;
            int fine = Math.min(inizio + elementiPerPagina, risultati.size());
            int elementiMostrati = fine - inizio;
            for (int i = inizio; i < fine; i++) {
                try {
                    int numeroVisualizzato = (i - inizio) + 1;
                    System.out.println("[" + numeroVisualizzato + "] \n" + gestorePrenotazioni.visualizzaPrenotazione(utenteCorrente, risultati.get(i).getCodiceUnivoco()));
                } catch (Exception e) {
                    System.out.println("Errore nel caricamento della prenotazione: " + e.getMessage());
                }
            }
            System.out.println("\nComandi disponibili:");
            System.out.println("- Digita il NUMERO [1 a " + elementiMostrati + "] per selezionare la prenotazione.");
            if (paginaCorrente < totalePagine - 1) {
                System.out.println("- Digita 'N' per andare alla pagina successiva.");
            }
            if (paginaCorrente > 0) {
                System.out.println("- Digita 'P' per tornare alla pagina precedente.");
            }
            System.out.println("- Digita '0' per annullare e tornare al menu precedente.");
            String input = leggiStringa("La tua scelta:", true).toUpperCase();
            if (input.equals("N") && paginaCorrente < totalePagine - 1) {
                paginaCorrente++;
            } else if (input.equals("P") && paginaCorrente > 0) {
                paginaCorrente--;
            } else if (input.equals("0")) {
                return null;
            } else {
                try {
                    int scelta = Integer.parseInt(input);
                    if (scelta >= 1 && scelta <= elementiMostrati) {
                        int indiceReale = inizio + (scelta - 1);
                        return risultati.get(indiceReale);
                    } else {
                        System.out.println("Errore: Numero non valido. Seleziona un numero tra 1 e " + elementiMostrati + ".");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore: Comando non riconosciuto. Usa N, P, 0 oppure un numero valido.");
                }
            }
        }
    }

    /**
     * Stampa le prenotazioni dell'utente che ha eseguito l'operazione.
     */
    private void stampaPropriePrenotazioni() {
        System.out.println("\n--- LE MIE PRENOTAZIONI ---");
        List<Prenotazione> miePrenotazioni = gestorePrenotazioni.ottieniPrenotazioniUtente(utenteCorrente);
        Prenotazione prenotazioneSelezionata = mostraESelezionaPrenotazione(miePrenotazioni);
        if (prenotazioneSelezionata != null) {
            System.out.println("\nHai selezionato la seguente prenotazione:");
            System.out.println(gestorePrenotazioni.visualizzaPrenotazione(utenteCorrente, prenotazioneSelezionata.getCodiceUnivoco()));
        }
        tornaAlMenu();
    }

    /**
     * Consente la modifica delle prenotazioni del cliente, permettendo al cliente di cambiare la proiezione relativa alla propria prenotazione.
     */
    private void gestisciModificaPrenotazione() {
        System.out.println("\n--- MODIFICA PRENOTAZIONE ---");
        List<Prenotazione> miePrenotazioni = gestorePrenotazioni.ottieniPrenotazioniUtente(utenteCorrente);
        System.out.println("Seleziona la prenotazione che desideri modificare:");
        Prenotazione daModificare = mostraESelezionaPrenotazione(miePrenotazioni);
        if (daModificare == null) {
            System.out.println("Operazione annullata.");
            return;
        }
        System.out.println("\nOra cerca la nuova proiezione a cui vuoi assistere:");
        String titolo = leggiStringa("Titolo del film:", false);
        Genere genere = leggiGenere("Genere", false);
        LocalDate dataInizio = LocalDate.now().plusDays(1);
        LocalDate dataFine = leggiData("Inserisci la data limite di ricerca", false);
        Double prezzoMinimo = leggiDouble("Inserisci il prezzo minimo del biglietto", false);
        Double prezzoMassimo = leggiDouble("Inserisci il prezzo massimo del biglietto", false);
        List<Proiezione> proiezioniTrovate = gestoreProiezioni.cercaProiezione(titolo, genere, dataInizio, dataFine, prezzoMassimo, prezzoMinimo);
        Proiezione nuovaProiezione = mostraESelezionaProiezione(proiezioniTrovate);
        if (nuovaProiezione == null) {
            System.out.println("Operazione annullata.");
            return;
        }
        System.out.println("\nTentativo di modifica in corso...");
        try {
            gestorePrenotazioni.modificaPrenotazione(utenteCorrente, daModificare.getCodiceUnivoco(), nuovaProiezione.getCodiceUnivoco());
        } catch (IllegalArgumentException e) {
            System.out.println("Impossibile modificare: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore generico: " + e.getMessage());
        }
        tornaAlMenu();
    }

    /**
     * Consente al cliente di eliminare una propria prenotazione.
     */
    private void gestisciEliminazionePrenotazione() {
        System.out.println("\n--- ANNULLA PRENOTAZIONE ---");
        List<Prenotazione> miePrenotazioni = gestorePrenotazioni.ottieniPrenotazioniUtente(utenteCorrente);
        Prenotazione prenotazioneSelezionata = mostraESelezionaPrenotazione(miePrenotazioni);
        if (prenotazioneSelezionata == null) {
            System.out.println("Operazione annullata.");
            return;
        }
        try {
            gestorePrenotazioni.eliminaPrenotazione(utenteCorrente, prenotazioneSelezionata.getCodiceUnivoco());
            System.out.println("Prenotazione annullata con successo!");
        } catch (Exception e) {
            System.out.println("Errore durante l'annullamento: " + e.getMessage());
        }
        tornaAlMenu();
    }

    /**
     * Consente a un proiezionista di creare e aggiungere una proiezione al palinsesto.
     */
    private void gestisciAggiuntaProiezione() {
        System.out.println("\n--- AGGIUNGI NUOVA PROIEZIONE ---");
        System.out.println("Inserisci prima i dettagli del Film:");
        String titolo = leggiStringa("Titolo del film:", true);
        Genere genere = leggiGenere("Genere", true);
        String regista = leggiStringa("Regista:", true);
        Integer anno = leggiIntero("Anno di Uscita:", true);
        Integer durata = leggiIntero("Durata in minuti:", true);
        Integer etaMinima = leggiIntero("Età minima consentita (inserisci 0 per film per tutti):", true);
        System.out.println("\nOra inserisci i dettagli della proiezione:");
        LocalDateTime inizioProiezione = leggiDataOra("Data e ora di inizio", true);
        Double prezzoBiglietto = leggiDouble("Costo del biglietto:", true);
        LocalDateTime fineProiezione = leggiDataOra("\nData e ora di fine della proiezione, ricordati che il film dura " + durata + " minuti, quindi" +
                " non deve finire prima delle " + inizioProiezione.plusMinutes(durata).format(FORMATO_PROMPT) + " ", true);
        System.out.println("\nTentativo di salvataggio nel palinsesto in corso...");
        try {
            gestoreProiezioni.aggiungiProiezione(utenteCorrente, titolo, genere, regista, anno, durata, etaMinima, inizioProiezione, fineProiezione, prezzoBiglietto);
            System.out.println("Successo! Proiezione aggiunta correttamente al palinsesto.");
        } catch (SovrapposizioneProiezioneException e) {
            System.out.println("Errore di sovrapposizione: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore generico: " + e.getMessage());
        }
        tornaAlMenu();
    }

    /**
     * Consente a un proiezionista di modificare una proiezione esistente nel palinsesto.
     */
    private void gestisciModificaProiezione() {
        System.out.println("\n--- MODIFICA PROIEZIONE ---");
        System.out.println("Cerca la proiezione che desideri modificare. Inserisci i parametri che preferisci per la ricerca.");
        String titolo = leggiStringa("Inserisci il titolo del film (anche parziale)", false);
        Genere genere = leggiGenere("Inserisci il genere", false);
        LocalDate dataInizio = leggiData("Inserisci la data a partire dalla quale cercare", false);
        if (dataInizio == null) {
            dataInizio = LocalDate.now();
        }
        LocalDate dataFine = leggiData("Inserisci la data limite di ricerca", false);
        Double prezzoMinimo = leggiDouble("Inserisci il prezzo minimo del biglietto", false);
        Double prezzoMassimo = leggiDouble("Inserisci il prezzo massimo del biglietto", false);
        System.out.println("\nRicerca in corso...");
        List<Proiezione> risultati = gestoreProiezioni.cercaProiezione(titolo, genere, dataInizio, dataFine, prezzoMassimo, prezzoMinimo);
        Proiezione daModificare = mostraESelezionaProiezione(risultati);
        if (daModificare == null) {
            System.out.println("Operazione annullata.");
            return;
        }
        System.out.println("\nInserisci i nuovi dati. Se non vuoi modificare un campo, premi semplicemente Invio.");
        String nuovoTitolo = leggiStringa("Nuovo titolo (" + daModificare.getFilmProiettato().getTitolo() + ")", false);
        Genere nuovoGenere = leggiGenere("Nuovo genere (" + daModificare.getFilmProiettato().getGenere() + ")", false);
        String nuovoRegista = leggiStringa("Nuovo regista (" + daModificare.getFilmProiettato().getRegista() + ")", false);
        Integer anno = leggiIntero("Nuovo anno (" + daModificare.getFilmProiettato().getAnno() + ")", false);
        int nuovoAnno = (anno == null) ? 0 : anno;
        Integer durata = leggiIntero("Nuova durata (" + daModificare.getFilmProiettato().getDurata() + ")", false);
        int nuovaDurata = (durata == null) ? 0 : durata;
        Integer eta = leggiIntero("Nuova età (" + daModificare.getFilmProiettato().getEtaMinima() + ")", false);
        int nuovaEta = (eta == null) ? -1 : eta;
        Double prezzo = leggiDouble("Nuovo prezzo (" + daModificare.getPrezzoBiglietto() + ")", false);
        double nuovoPrezzo = (prezzo == null) ? -1.0 : prezzo;
        LocalDateTime nuovoInizio = leggiDataOra("Nuovo orario di inizio (" + daModificare.getInizioProiezione().format(FORMATO_PROMPT) + ")", false);
        int durataApplicata = (nuovaDurata != 0) ? nuovaDurata : daModificare.getFilmProiettato().getDurata();
        LocalDateTime inizioApplicato = (nuovoInizio != null) ? nuovoInizio : daModificare.getInizioProiezione();
        LocalDateTime fineMinimaPossibile = inizioApplicato.plusMinutes(durataApplicata);
        System.out.println("\nSuggerimento. In base all'inizio e alla durata, la proiezione dovrebbe terminare minimo alle: " + fineMinimaPossibile.format(FORMATO_PROMPT));
        LocalDateTime nuovaFine = leggiDataOra("Nuovo orario di fine (" + daModificare.getFineProiezione().format(FORMATO_PROMPT) + ")", false);
        System.out.println("\nTentativo di modifica in corso...");
        try {
            gestoreProiezioni.modificaProiezione(utenteCorrente, daModificare.getCodiceUnivoco(), nuovoTitolo, nuovoGenere, nuovoRegista, nuovoAnno, nuovaDurata, nuovaEta, nuovoInizio, nuovaFine, nuovoPrezzo);
            System.out.println("Successo! La proiezione è stata aggiornata correttamente.");
        } catch (IllegalArgumentException | ProiezioneConPrenotazioniException e) {
            System.out.println("Impossibile modificare: " + e.getMessage());
        } catch (SovrapposizioneProiezioneException e) {
            System.out.println("Errore di orario: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore generico: " + e.getMessage());
        }
        tornaAlMenu();
    }

    /**
     * Consente a un proiezionista di eliminare una proiezione esistente nel palinsesto.
     */
    private void gestisciEliminazioneProiezione() {
        System.out.println("\n--- ELIMINA PROIEZIONE ---");
        System.out.println("Cerca la proiezione che desideri eliminare. Inserisci i parametri che preferisci per la ricerca.");
        String titolo = leggiStringa("Inserisci il titolo del film (anche parziale)", false);
        Genere genere = leggiGenere("Inserisci il genere", false);
        LocalDate dataInizio = leggiData("Inserisci la data a partire dalla quale cercare", false);
        LocalDate dataFine = leggiData("Inserisci la data limite di ricerca", false);
        Double prezzoMinimo = leggiDouble("Inserisci il prezzo minimo del biglietto", false);
        Double prezzoMassimo = leggiDouble("Inserisci il prezzo massimo del biglietto", false);
        System.out.println("\nRicerca in corso...");
        List<Proiezione> risultati = gestoreProiezioni.cercaProiezione(titolo, genere, dataInizio, dataFine, prezzoMassimo, prezzoMinimo);
        Proiezione daEliminare = mostraESelezionaProiezione(risultati);
        if (daEliminare == null) {
            System.out.println("Operazione annullata.");
            return;
        }
        System.out.println("\nTentativo di eliminazione in corso...");
        try {
            gestoreProiezioni.eliminaProiezione(utenteCorrente, daEliminare.getCodiceUnivoco());
            System.out.println("Successo! La proiezione è stata rimossa correttamente dal palinsesto.");
        } catch (ProiezioneConPrenotazioniException e) {
            System.out.println("Impossibile eliminare: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
        tornaAlMenu();
    }

    /**
     * Consente ai bigliettai di cercare prenotazioni, scegliendo se usare la ricerca tramite codice univoco o tramite vari filtri.
     */
    private void gestisciRicercaPrenotazioni() {
        System.out.println("\n--- RICERCA PRENOTAZIONI ---");
        String scelta = leggiStringa("Vuoi cercare tramite il codice prenotazione esatto? (S/N)", true).toUpperCase();
        if (scelta.equals("S")) {
            String codice = leggiStringa("Inserisci il codice della prenotazione:", true);
            try {
                String dettaglio = gestorePrenotazioni.visualizzaPrenotazione(utenteCorrente, codice);
                System.out.println("\nRisultato Ricerca:");
                System.out.println(dettaglio);
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        } else {
            System.out.println("\nInserisci i criteri di ricerca (premi Invio per saltare un filtro).");
            String nomeCognome = leggiStringa("Inserisci nome e/o cognome del cliente", false);
            String titoloFilm = leggiStringa("Inserisci il titolo del film (anche parziale)", false);
            LocalDate dataInizio = leggiData("Inserisci la data a partire dalla quale cercare", false);
            LocalDate dataFine = leggiData("Inserisci la data limite di ricerca", false);
            System.out.println("\nRicerca in corso...");
            List<Prenotazione> prenotazioniTrovate = gestorePrenotazioni.cercaPrenotazioni(utenteCorrente, nomeCognome, titoloFilm, dataInizio, dataFine);
            Prenotazione prenotazioneSelezionata = mostraESelezionaPrenotazione(prenotazioniTrovate);
            if (prenotazioneSelezionata != null) {
                try {
                    System.out.println("\n--- Dettaglio Prenotazione Selezionata ---");
                    System.out.println(gestorePrenotazioni.visualizzaPrenotazione(utenteCorrente, prenotazioneSelezionata.getCodiceUnivoco()));
                } catch (Exception e) {
                    System.out.println("Errore: " + e.getMessage());
                }
            }
        }
        tornaAlMenu();
    }

    /**
     * Consente ai bigliettai di visualizzare tutte le prenotazioni odierne e visualizzarne i dettagli.
     */
    private void gestisciPrenotazioniOdierne() {
        System.out.println("\n--- PRENOTAZIONI ODIERNE ---");
        LocalDate oggi = LocalDate.now();
        System.out.println("Ricerca delle prenotazioni per la data: " + oggi + "...");
        List<Prenotazione> prenotazioniTrovate = gestorePrenotazioni.cercaPrenotazioni(utenteCorrente, null, null, oggi, oggi);
        Prenotazione prenotazioneSelezionata = mostraESelezionaPrenotazione(prenotazioniTrovate);
        if (prenotazioneSelezionata != null) {
            try {
                System.out.println("\n--- Dettaglio prenotazione Selezionata ---");
                System.out.println(gestorePrenotazioni.visualizzaPrenotazione(utenteCorrente, prenotazioneSelezionata.getCodiceUnivoco()));
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
        tornaAlMenu();
    }

    /**
     * Mostra una lista di proiezioni impaginate e permette all'utente di scorrere le pagine o selezionarne una.
     * @param risultati la lista di proiezioni da mostrare
     * @return la proiezione selezionata, oppure null se l'utente annulla l'operazione
     */
    private Proiezione mostraESelezionaProiezione(List<Proiezione> risultati) {
        if (risultati == null || risultati.isEmpty()) {
            System.out.println("Nessuna proiezione trovata con questi criteri.");
            return null;
        }
        int elementiPerPagina = 10;
        int totalePagine = (int) Math.ceil((double) risultati.size() / elementiPerPagina);
        int paginaCorrente = 0;
        while (true) {
            System.out.println("\n--- Risultati Ricerca (Pagina " + (paginaCorrente + 1) + " di " + totalePagine + ") ---");
            int inizio = paginaCorrente * elementiPerPagina;
            int fine = Math.min(inizio + elementiPerPagina, risultati.size());
            int elementiMostrati = fine - inizio;
            for (int i = inizio; i < fine; i++) {
                int numeroVisualizzato = (i - inizio) + 1;
                System.out.println("[" + numeroVisualizzato + "] \n" + gestoreProiezioni.visualizzaProiezione(risultati.get(i)));
            }
            System.out.println("\nComandi disponibili:");
            System.out.println("- Digita il NUMERO [1 a " + elementiMostrati + "] per selezionare la proiezione.");
            if (paginaCorrente < totalePagine - 1) {
                System.out.println("- Digita 'N' per andare alla pagina successiva.");
            }
            if (paginaCorrente > 0) {
                System.out.println("- Digita 'P' per tornare alla pagina precedente.");
            }
            System.out.println("- Digita '0' per annullare e tornare al menu precedente.");
            String input = leggiStringa("La tua scelta:", true).toUpperCase();
            if (input.equals("N") && paginaCorrente < totalePagine - 1) {
                paginaCorrente++;
            } else if (input.equals("P") && paginaCorrente > 0) {
                paginaCorrente--;
            } else if (input.equals("0")) {
                return null;
            } else {
                try {
                    int scelta = Integer.parseInt(input);
                    if (scelta >= 1 && scelta <= elementiMostrati) {
                        int indiceReale = inizio + (scelta - 1);
                        return risultati.get(indiceReale);
                    } else {
                        System.out.println("Errore: Numero non valido. Seleziona un numero tra 1 e " + elementiMostrati + ".");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore: Comando non riconosciuto. Usa N, P, 0 oppure un numero valido.");
                }
            }
        }
    }

    /**
     * Richiede all'utente l'inserimento di una stringa tramite console,
     * continuando a mostrare il prompt finchè non viene fornito un input valido.
     * @param messaggio    il testo del prompt da mostrare all'utente
     * @param obbligatorio indica se l'inserimento è obbligatorio od opzionale
     * @return una stringa, null se l'inserimento è opzionale e l'utente ha deciso di saltare il campo
     */
    private String leggiStringa(String messaggio, boolean obbligatorio) {
        while (true) {
            System.out.print(messaggio + (obbligatorio ? " " : " [Premi Invio per saltare]: "));
            String input = scanner.nextLine().trim();
            if (!input.isBlank()) {
                return input;
            }
            if (!obbligatorio) {
                return null;
            }
            System.out.println("Errore: Questo campo è obbligatorio e non può essere vuoto. Riprova.");
        }
    }

    /**
     * Richiede all'utente l'inserimento di una password tramite console,
     * continuando a mostrare il prompt finchè non viene fornito un input valido.
     * @param messaggio    il testo del prompt da mostrare all'utente
     * @param obbligatorio indica se l'inserimento è obbligatorio od opzionale
     * @return un array di caratteri contenente la password, null se l'inserimento è opzionale e l'utente ha deciso di saltare il campo
     */
    private char[] leggiPassword(String messaggio, boolean obbligatorio) {
        while (true) {
            System.out.print(messaggio + (obbligatorio ? " " : " [Premi Invio per saltare]: "));
            char[] password;
            if (System.console() != null) {
                password = System.console().readPassword();
            } else {
                password = scanner.nextLine().trim().toCharArray();
            }
            if (password != null && password.length > 0) {
                return password;
            }
            if (!obbligatorio) {
                return null;
            }
            System.out.println("Errore: La password è obbligatoria e non può essere vuota. Riprova.");
        }
    }

    /**
     * Richiede all'utente l'inserimento di un numero intero tramite console,
     * continuando a mostrare il prompt finchè non viene fornito un input valido.
     * @param messaggio    il testo del prompt da mostrare all'utente
     * @param obbligatorio indica se l'inserimento è obbligatorio od opzionale
     * @return un numero intero, null se l'inserimento è opzionale e l'utente ha deciso di saltare il campo
     */
    private Integer leggiIntero(String messaggio, boolean obbligatorio) {
        while (true) {
            System.out.print(messaggio + (obbligatorio ? " " : " [Premi Invio per saltare]: "));
            String input = scanner.nextLine().trim();
            if (input.isBlank()) {
                if (!obbligatorio) {
                    return null;
                }
                System.out.println("Errore: Questo campo è obbligatorio. Riprova.");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un numero intero valido. Riprova.");
            }
        }
    }

    /**
     * Richiede all'utente l'inserimento di un numero decimale tramite console,
     * continuando a mostrare il prompt finchè non viene fornito un input valido.
     * @param messaggio    il testo del prompt da mostrare all'utente
     * @param obbligatorio indica se l'inserimento è obbligatorio od opzionale
     * @return un numero decimale, null se l'inserimento è opzionale e l'utente ha deciso di saltare il campo
     */
    private Double leggiDouble(String messaggio, boolean obbligatorio) {
        while (true) {
            System.out.print(messaggio + (obbligatorio ? " " : " [Premi Invio per saltare]: "));
            String input = scanner.nextLine().trim().replace(",", ".");
            if (input.isBlank()) {
                if (!obbligatorio) {
                    return null;
                }
                System.out.println("Errore: Questo campo è obbligatorio. Riprova.");
                continue;
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un numero decimale valido (es. 3.75). Riprova.");
            }
        }
    }

    /**
     * Richiede all'utente l'inserimento di una data tramite console,
     * continuando a mostrare il prompt finchè non viene fornito un input valido.
     * @param messaggio    il testo del prompt da mostrare all'utente
     * @param obbligatorio indica se l'inserimento è obbligatorio od opzionale
     * @return una data, null se l'inserimento è opzionale e l'utente ha deciso di saltare il campo
     */
    private LocalDate leggiData(String messaggio, boolean obbligatorio) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(messaggio + " (Formato AAAA-MM-GG)" + (obbligatorio ? ": " : " [Premi Invio per saltare]: "));
            String input = scanner.nextLine().trim();
            if (input.isBlank()) {
                if (!obbligatorio) {
                    return null;
                }
                System.out.println("Errore: Questo campo è obbligatorio. Riprova.");
                continue;
            }
            try {
                return LocalDate.parse(input, formatoData);
            } catch (DateTimeParseException e) {
                System.out.println("Errore: Devi inserire un formato data valido (es. 2010-05-16). Riprova.");
            }
        }
    }

    /**
     * Richiede all'utente l'inserimento di una data e orario tramite console,
     * continuando a mostrare il prompt finchè non viene fornito un input valido.
     * @param messaggio    il testo del prompt da mostrare all'utente
     * @param obbligatorio indica se l'inserimento è obbligatorio od opzionale
     * @return data e orario, null se l'inserimento è opzionale e l'utente ha deciso di saltare il campo
     */
    private LocalDateTime leggiDataOra(String messaggio, boolean obbligatorio) {
        DateTimeFormatter formatoOra = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        while (true) {
            System.out.print(messaggio + " (Formato AAAA-MM-GG HH:MM)" + (obbligatorio ? ": " : " [Premi Invio per saltare]: "));
            String input = scanner.nextLine().trim();
            if (input.isBlank()) {
                if (!obbligatorio) {
                    return null;
                }
                System.out.println("Errore: Questo campo è obbligatorio. Riprova.");
                continue;
            }
            try {
                return LocalDateTime.parse(input, formatoOra);
            } catch (DateTimeParseException e) {
                System.out.println("Errore: Devi inserire un formato data valido (es. 2010-05-16 17:30). Riprova.");
            }
        }
    }

    /**
     * Richiede all'utente l'inserimento di un genere cinematografico tramite console,
     * continuando a mostrare il prompt finchè non viene fornito un input valido.
     * @param messaggio    il testo del prompt da mostrare all'utente
     * @param obbligatorio indica se l'inserimento è obbligatorio od opzionale
     * @return il genere corrispondente alla stringa inserita, null se l'inserimento è opzionale e l'utente ha deciso di saltare il campo
     */
    private Genere leggiGenere(String messaggio, boolean obbligatorio) {
        while (true) {
            System.out.print(messaggio + " (es. Azione, Horror, Commedia)" + (obbligatorio ? ": " : " [Premi Invio per saltare]: "));
            String input = scanner.nextLine().trim();
            if (input.isBlank()) {
                if (!obbligatorio) {
                    return null;
                }
                System.out.println("Errore: Questo campo è obbligatorio. Riprova.");
                continue;
            }
            try {
                return Genere.daStringa(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: Genere non riconosciuto. Riprova.");
            }
        }
    }

    /**
     * Metodo per evitare che l'utente si ritrovi direttamente al menu dopo ogni operazione, richiede interazione per poter concludere il metodo.
     */
    private void tornaAlMenu() {
        System.out.println("\nPremi Invio per tornare al menu...");
        scanner.nextLine();
    }
}
