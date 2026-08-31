// Kejsi Xhafaj, 759934, VA
package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.UUID;

/**
 * Contiene al suo interno i metodi usati per la creazione, modifica ed eliminazione delle prenotazioni e tutti quei metodi
 * che servono a operare su di esse.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class GestorePrenotazioni {
    /**
     * L'attributo <code>gestoreDati</code> memorizza il riferimento al gestore principale dei dati, necessario per accedere
     * alle liste e salvare le informazioni sulle prenotazioni.
     */
    private final GestoreDati gestoreDati;

    /**
     * Costruisce un nuovo gestore delle prenotazioni associandolo al gestore dei dati principale del sistema.
     * @param gestoreDati l'istanza di GestoreDati utilizzata per accedere alle liste e gestire il salvataggio
     * @throws IllegalArgumentException se il gestore dati passato risulta nullo
     */
    public GestorePrenotazioni(GestoreDati gestoreDati) {
        Validatore.validaOggetto(gestoreDati);
        this.gestoreDati = gestoreDati;
    }

    /**
     * Restituisce i dettagli di una specifica prenotazione sotto forma di stringa, verificando che l'utente abbia i permessi
     * per visualizzarla.
     * @param utenteCorrente     l'utente che sta richiedendo di visualizzare la prenotazione
     * @param codicePrenotazione il codice univoco della prenotazione da cercare
     * @return una stringa formattata contenente tutti i dettagli della prenotazione richiesta
     * @throws IllegalArgumentException         se il codice della prenotazione risulta nullo o vuoto, oppure se l'utente corrente è nullo
     * @throws PrenotazioneInesistenteException se non viene trovata alcuna prenotazione associata al codice fornito
     * @throws PermessiMancantiException        se l'utente corrente non ha i permessi necessari
     */
    public String visualizzaPrenotazione(Utente utenteCorrente, String codicePrenotazione) {
        Validatore.validaStringa(codicePrenotazione, "Il codice della prenotazione da visualizzare non può essere nullo o vuoto.");
        Validatore.validaOggetto(utenteCorrente);
        Prenotazione daVisualizzare = gestoreDati.cercaPrenotazioneTramiteCodice(codicePrenotazione);
        if (daVisualizzare == null) {
            throw new PrenotazioneInesistenteException("Prenotazione inesistente.");
        }
        if (!utenteCorrente.isBigliettaio() && !utenteCorrente.getUsername().equalsIgnoreCase(daVisualizzare.getUsernameUtente())) {
            throw new PermessiMancantiException("Non puoi visualizzare questa prenotazione.");
        }
        Utente cliente = gestoreDati.cercaUtente(daVisualizzare.getUsernameUtente());
        Validatore.validaOggetto(cliente);
        String nomeCognome = cliente.getNome() + " " + cliente.getCognome();
        Proiezione proiezione = gestoreDati.getPalinsesto().cercaProiezioneTramiteCodice(daVisualizzare.getCodiceProiezione());
        Validatore.validaOggetto(proiezione);
        int numeroBiglietti = daVisualizzare.getNumeroBiglietti();
        double costoUnitario = proiezione.getPrezzoBiglietto();
        double costoTotale = numeroBiglietti * costoUnitario;
        DateTimeFormatter formatoData = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        DateTimeFormatter formatoOra = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        return "--- Dettaglio Prenotazione ---\n" +
                "Codice prenotazione: " +
                daVisualizzare.getCodiceUnivoco() +
                "\n" +
                "Nome e Cognome: " +
                nomeCognome +
                "\n" +
                "Data e Ora: " +
                proiezione.getInizioProiezione().format(formatoData) +
                " dalle " +
                proiezione.getInizioProiezione().format(formatoOra) +
                " alle " +
                proiezione.getFineProiezione().format(formatoOra) +
                "\n" +
                "Numero Biglietti: " +
                numeroBiglietti +
                "\n" +
                "Costo unitario: " +
                String.format("%.2f", costoUnitario) +
                proiezione.getGlifoValuta() +
                "\n" +
                "Costo totale: " +
                String.format("%.2f", costoTotale) +
                proiezione.getGlifoValuta() +
                "\n" +
                "------------------------------";
    }

    /**
     * Crea una nuova prenotazione per una proiezione specifica, verificando i permessi dell'utente, la validità dei dati e l'età minima.
     * Genera un codice univoco per la prenotazione, aggiorna i posti occupati e salva i dati aggiornati sui file CSV.
     * @param utenteCorrente   l'utente che sta tentando di effettuare la prenotazione (deve avere il ruolo di cliente)
     * @param codiceProiezione il codice univoco che identifica la proiezione per cui si vogliono prenotare i posti
     * @param numeroBiglietti  la quantità di biglietti che l'utente desidera prenotare
     * @throws IllegalArgumentException     se l'utente fornito è nullo, o se il codice della proiezione risulta nullo o vuoto, se la proiezione è passata
     * @throws PermessiMancantiException    se l'utente corrente non ha i permessi necessari (non è un cliente registrato)
     * @throws EtaMinimaConsentitaException se l'utente corrente non possiede l'età minima richiesta per visionare il film
     * @throws PostiEsauritiException       se i posti per quella proiezione sono finiti
     * @throws IllegalStateException        se ci sono errori durante il salvataggio sul file CSV
     */
    public void creaPrenotazione(Utente utenteCorrente, String codiceProiezione, int numeroBiglietti) {
        Validatore.validaOggetto(utenteCorrente);
        Validatore.validaStringa(codiceProiezione, "Il codice della proiezione non può essere nullo o vuoto.");
        if (!utenteCorrente.isCliente()) {
            throw new PermessiMancantiException("Solo i clienti registrati possono effettuare prenotazioni.");
        }
        Proiezione proiezioneDaAggiornare = gestoreDati.getPalinsesto().cercaProiezioneTramiteCodice(codiceProiezione);
        Validatore.validaOggetto(proiezioneDaAggiornare);
        if (proiezioneDaAggiornare.getInizioProiezione().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Impossibile prenotare: la proiezione è già passata.");
        }
        int postiLiberi = Proiezione.getPostiSala() - proiezioneDaAggiornare.getPostiPrenotati();
        if (numeroBiglietti > postiLiberi) {
            throw new PostiEsauritiException("Posti insufficienti: ne restano solo " + postiLiberi + ".");
        }
        if (proiezioneDaAggiornare.getFilmProiettato().getEtaMinima() > 0 && utenteCorrente.getDataNascita() == null) {
            throw new EtaMinimaConsentitaException("Questo film ha un'età minima di " + proiezioneDaAggiornare.getFilmProiettato().getEtaMinima() + " anni:" +
                    " inserisci la tua data di nascita per prenotare.");
        }
        if (!possoVisionareFilm(utenteCorrente, proiezioneDaAggiornare.getFilmProiettato())) {
            throw new EtaMinimaConsentitaException("Spiacenti, non hai l'età minima per visionare questo film.");
        }
        String codicePrenotazioneNuovo = null;
        boolean codiceOccupato = true;
        while (codiceOccupato) {
            codicePrenotazioneNuovo = UUID.randomUUID().toString().substring(0, 7);
            if (gestoreDati.cercaPrenotazioneTramiteCodice(codicePrenotazioneNuovo) == null) {
                codiceOccupato = false;
            }
        }
        Prenotazione nuovaPrenotazione = new Prenotazione(codicePrenotazioneNuovo, utenteCorrente.getUsername(), codiceProiezione, numeroBiglietti);
        proiezioneDaAggiornare.aggiornaPostiPrenotati(nuovaPrenotazione.getNumeroBiglietti());
        gestoreDati.aggiungiPrenotazione(nuovaPrenotazione);
        gestoreDati.salvaPrenotazioniSuCSV();
        gestoreDati.salvaProiezioniSuCSV();
    }

    /**
     * Modifica una prenotazione esistente, spostandola su una nuova proiezione, previa verifica dei permessi dell'utente, della validità delle date
     * e della disponibilità dei posti. Salva poi i dati aggiornati sui file CSV.
     * @param utenteCorrente        l'utente che sta tentando di modificare la prenotazione (deve essere il proprietario della stessa)
     * @param codicePrenotazione    il codice univoco della prenotazione da modificare
     * @param nuovoCodiceProiezione il codice della nuova proiezione su cui si desidera spostare la prenotazione
     * @throws IllegalArgumentException         se i parametri sono nulli o vuoti, se la vecchia o la nuova proiezione non sono nel futuro, o se non ci sono posti sufficienti nella nuova proiezione
     * @throws PrenotazioneInesistenteException se non viene trovata alcuna prenotazione associata al codice fornito
     * @throws PermessiMancantiException        se l'utente corrente non corrisponde all'intestatario della prenotazione o se non è un cliente
     * @throws EtaMinimaConsentitaException     se l'utente non ha l'età minima consentita o consigliata per poter visualizzare il film
     * @throws IllegalStateException            se ci sono errori durante il salvataggio sul file CSV
     */
    public void modificaPrenotazione(Utente utenteCorrente, String codicePrenotazione, String nuovoCodiceProiezione) {
        Validatore.validaOggetto(utenteCorrente);
        Validatore.validaStringa(codicePrenotazione, "Il codice prenotazione non può essere nullo o vuoto.");
        Validatore.validaStringa(nuovoCodiceProiezione, "Il nuovo codice proiezione non può essere nullo o vuoto.");
        if (!utenteCorrente.isCliente()) {
            throw new PermessiMancantiException("Solo i clienti possono gestire le proprie prenotazioni.");
        }
        Prenotazione daModificare = gestoreDati.cercaPrenotazioneTramiteCodice(codicePrenotazione);
        if (daModificare == null) {
            throw new PrenotazioneInesistenteException("Prenotazione inesistente.");
        }
        if (!utenteCorrente.getUsername().equalsIgnoreCase(daModificare.getUsernameUtente())) {
            throw new PermessiMancantiException("Puoi modificare solo le tue prenotazioni.");
        }
        Proiezione vecchiaProiezione = gestoreDati.getPalinsesto().cercaProiezioneTramiteCodice(daModificare.getCodiceProiezione());
        Proiezione nuovaProiezione = gestoreDati.getPalinsesto().cercaProiezioneTramiteCodice(nuovoCodiceProiezione);
        Validatore.validaOggetto(vecchiaProiezione);
        Validatore.validaOggetto(nuovaProiezione);
        LocalDate dataOdierna = LocalDate.now();
        if (nuovaProiezione.getFilmProiettato().getEtaMinima() > 0 && utenteCorrente.getDataNascita() == null) {
            throw new EtaMinimaConsentitaException("Questo film ha un'età minima di " + nuovaProiezione.getFilmProiettato().getEtaMinima() + " anni:" +
                    " inserisci la tua data di nascita per prenotare.");
        }
        if (!possoVisionareFilm(utenteCorrente, nuovaProiezione.getFilmProiettato())) {
            throw new EtaMinimaConsentitaException("Spiacenti, non hai l'età minima per poter visionare questo film.");
        }
        if (!vecchiaProiezione.getInizioProiezione().toLocalDate().isAfter(dataOdierna)) {
            throw new IllegalArgumentException("Impossibile modificare: la proiezione originale è già passata o è in data odierna.");
        }
        if (!nuovaProiezione.getInizioProiezione().toLocalDate().isAfter(dataOdierna)) {
            throw new IllegalArgumentException("Impossibile modificare: la nuova proiezione deve essere in una data futura.");
        }
        int numeroBiglietti = daModificare.getNumeroBiglietti();
        vecchiaProiezione.aggiornaPostiPrenotati(-numeroBiglietti);
        try {
            nuovaProiezione.aggiornaPostiPrenotati(numeroBiglietti);
        } catch (PostiEsauritiException e) {
            vecchiaProiezione.aggiornaPostiPrenotati(numeroBiglietti);
            throw new IllegalArgumentException("Non ci sono abbastanza posti liberi nella nuova proiezione scelta.");
        }
        daModificare.setCodiceProiezione(nuovoCodiceProiezione);
        gestoreDati.salvaProiezioniSuCSV();
        gestoreDati.salvaPrenotazioniSuCSV();
    }

    /**
     * Elimina una prenotazione esistente, liberando i posti occupati e aggiornando i file CSV, previa verifica dei permessi e della validità temporale
     * della proiezione.
     * @param utenteCorrente     l'utente che richiede l'eliminazione della prenotazione (deve essere il proprietario e un cliente registrato)
     * @param codicePrenotazione il codice univoco della prenotazione da eliminare
     * @throws IllegalArgumentException         se l'utente è nullo, il codice della prenotazione è nullo o vuoto, oppure se la proiezione associata è già passata
     * @throws PrenotazioneInesistenteException se non esiste alcuna prenotazione associata al codice specificato
     * @throws PermessiMancantiException        se l'utente non è un cliente oppure sta tentando di eliminare la prenotazione di un altro utente
     * @throws IllegalStateException            se ci sono errori durante il salvataggio sul file CSV
     */
    public void eliminaPrenotazione(Utente utenteCorrente, String codicePrenotazione) {
        Validatore.validaOggetto(utenteCorrente);
        Validatore.validaStringa(codicePrenotazione, "Il codice della prenotazione da eliminare non può essere nullo o vuoto.");
        if (!utenteCorrente.isCliente()) {
            throw new PermessiMancantiException("Solo i clienti registrati possono gestire le proprie prenotazioni.");
        }
        Prenotazione prenotazioneTrovata = gestoreDati.cercaPrenotazioneTramiteCodice(codicePrenotazione);
        if (prenotazioneTrovata == null) {
            throw new PrenotazioneInesistenteException("Prenotazione inesistente.");
        }
        if (!utenteCorrente.getUsername().equalsIgnoreCase(prenotazioneTrovata.getUsernameUtente())) {
            throw new PermessiMancantiException("Non puoi eliminare la prenotazione di un altro utente.");
        }
        Proiezione proiezioneDaAggiornare = gestoreDati.getPalinsesto().cercaProiezioneTramiteCodice(prenotazioneTrovata.getCodiceProiezione());
        Validatore.validaOggetto(proiezioneDaAggiornare);
        if (proiezioneDaAggiornare.getInizioProiezione().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Impossibile cancellare: la proiezione è già passata.");
        }
        proiezioneDaAggiornare.aggiornaPostiPrenotati(-prenotazioneTrovata.getNumeroBiglietti());
        gestoreDati.rimuoviPrenotazione(prenotazioneTrovata);
        gestoreDati.salvaPrenotazioniSuCSV();
        gestoreDati.salvaProiezioniSuCSV();
    }

    /**
     * Restituisce la lista di tutte le prenotazioni effettuate da uno specifico utente cliente.
     * @param utenteCorrente l'utente per il quale si desidera ottenere le prenotazioni (l'utente stesso che richiede la lista)
     * @return una lista contenente le prenotazioni associate all'utente specificato
     * @throws IllegalArgumentException  se l'oggetto utente fornito risulta nullo
     * @throws PermessiMancantiException se l'utente non possiede i permessi necessari (non è un cliente)
     */
    public List<Prenotazione> ottieniPrenotazioniUtente(Utente utenteCorrente) {
        Validatore.validaOggetto(utenteCorrente);
        if (!utenteCorrente.isCliente()) {
            throw new PermessiMancantiException("Non puoi avere prenotazioni se non sei un cliente.");
        }
        return gestoreDati.getListaPrenotazioni().stream()
                .filter(prenotazione -> prenotazione.getUsernameUtente().equalsIgnoreCase(utenteCorrente.getUsername()))
                .toList();
    }

    /**
     * Controlla che l'utente passato come parametro possa visualizzare il film comparando l'età minima di visione
     * nel film e l'età dell'utente stesso.
     * @param utenteCorrente  l'utente che vuole visionare il film
     * @param filmDaVisionare il film da visionare
     * @return vero se l'utente ha età uguale o maggiore all'età minima per poter visionare il film, falso altrimenti
     * @throws IllegalArgumentException se l'utente corrente o il film da visionare risultano nulli
     */
    public boolean possoVisionareFilm(Utente utenteCorrente, Film filmDaVisionare) {
        Validatore.validaOggetto(utenteCorrente);
        Validatore.validaOggetto(filmDaVisionare);
        return utenteCorrente.getAnniUtente() >= filmDaVisionare.getEtaMinima();
    }

    /**
     * Esegue una ricerca dettagliata sulle prenotazioni globali filtrandole in base al nome e cognome del cliente, al titolo del film e a un
     * intervallo di date.
     * @param utenteCorrente l'utente che sta eseguendo la ricerca (deve avere il ruolo di bigliettaio)
     * @param nomeCognome    la stringa da cercare all'interno del nome completo del cliente (parametro opzionale)
     * @param titoloFilm     la stringa da cercare all'interno del titolo del film proiettato (parametro opzionale)
     * @param dataInizio     la data minima della proiezione per filtrare i risultati (parametro opzionale)
     * @param dataFine       data massima della proiezione per filtrare i risultati (parametro opzionale)
     * @return una lista contenente tutte le prenotazioni che soddisfano i criteri di ricerca
     * @throws IllegalArgumentException  se l'oggetto utente corrente passato come parametro risulta nullo
     * @throws PermessiMancantiException se l'utente corrente non ha i permessi necessari per effettuare la ricerca (non è un bigliettaio)
     */
    public List<Prenotazione> cercaPrenotazioni(Utente utenteCorrente, String nomeCognome, String titoloFilm, LocalDate dataInizio, LocalDate dataFine) {
        Validatore.validaOggetto(utenteCorrente);
        if (!utenteCorrente.isBigliettaio()) {
            throw new PermessiMancantiException("Non puoi fare una ricerca dettagliata delle prenotazioni globali.");
        }
        return gestoreDati.getListaPrenotazioni().stream()
                .filter(prenotazione -> {
                    if (nomeCognome == null || nomeCognome.isBlank()) return true;
                    Utente u = gestoreDati.cercaUtente(prenotazione.getUsernameUtente());
                    if (u == null) return false;
                    String nomeCompleto = u.getNome() + " " + u.getCognome();
                    return nomeCompleto.toLowerCase().contains(nomeCognome.toLowerCase());
                })
                .filter(prenotazione -> {
                    Proiezione proiez = gestoreDati.getPalinsesto().cercaProiezioneTramiteCodice(prenotazione.getCodiceProiezione());
                    if (proiez == null) return false;
                    boolean checkTitolo = (titoloFilm == null || titoloFilm.isBlank()) ||
                            proiez.getFilmProiettato().getTitolo().toLowerCase().contains(titoloFilm.toLowerCase());
                    boolean checkInizio = (dataInizio == null) || !proiez.getInizioProiezione().toLocalDate().isBefore(dataInizio);
                    boolean checkFine = (dataFine == null) || !proiez.getFineProiezione().toLocalDate().isAfter(dataFine);
                    return checkTitolo && checkInizio && checkFine;
                }).toList();
    }
}
