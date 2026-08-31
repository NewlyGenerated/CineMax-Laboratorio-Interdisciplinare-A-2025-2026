// Kejsi Xhafaj, 759934, VA
package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contiene al suo interno i metodi usati per la creazione, modifica ed eliminazione delle
 * proiezioni e tutti quei metodi che servono a operare su di esse.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class GestoreProiezioni {
    /**
     * L'attributo <code>gestoreDati</code> memorizza il riferimento al gestore principale dei dati, necessario per accedere
     * al palinsesto e salvare in esso le informazioni sulle proiezioni.
     */
    private final GestoreDati gestoreDati;

    /**
     * Costruisce un nuovo gestore delle proiezioni associandolo al gestore dei dati principale del sistema.
     * @param gestoreDati l'istanza di GestoreDati utilizzata per accedere alle liste e gestire il salvataggio
     * @throws IllegalArgumentException se il gestore dati passato risulta nullo
     */
    public GestoreProiezioni(GestoreDati gestoreDati) {
        Validatore.validaOggetto(gestoreDati);
        this.gestoreDati = gestoreDati;
    }

    /**
     * Aggiunge una proiezione al palinsesto, assicurandosi che l'utente che abbia effettuato l'operazione sia autorizzato a farlo.
     * @param utenteCorrente   l'utente che sta tentando di aggiungere la proiezione
     * @param titolo           il titolo del film da proiettare
     * @param genere           il genere del film
     * @param regista          il nome del regista del film
     * @param anno             l'anno di uscita del film
     * @param durata           la durata del film espressa in minuti
     * @param etaMinima        l'età minima consigliata per visionare il film
     * @param inizioProiezione la data e l'orario di inizio della proiezione
     * @param fineProiezione   la data e l'orario della fine della proiezione
     * @param prezzoBiglietto  il costo del biglietto per questa specifica proiezione
     * @throws IllegalArgumentException           se uno qualsiasi dei parametri inseriti risulta nullo o non valido
     * @throws PermessiMancantiException          se l'utente che tenta l'operazione non è un proiezionista
     * @throws SovrapposizioneProiezioneException se l'orario della nuova proiezione si sovrappone a quello di una proiezione già esistente
     * @throws IllegalStateException              se ci sono errori durante il salvataggio sul file CSV
     */
    public void aggiungiProiezione(Utente utenteCorrente, String titolo, Genere genere, String regista, int anno, int durata, int etaMinima, LocalDateTime inizioProiezione, LocalDateTime fineProiezione, double prezzoBiglietto) {
        Validatore.validaOggetto(utenteCorrente);
        if (!utenteCorrente.isProiezionista()) {
            throw new PermessiMancantiException("Non sei un proiezionista e non puoi aggiungere questa proiezione.");
        }
        Validatore.validaOggetto(inizioProiezione, "L'orario di inizio della proiezione non può essere nullo.");
        if (inizioProiezione.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Non è possibile programmare una proiezione in una data già passata.");
        }
        Film nuovoFilm = new Film(titolo, genere, regista, anno, durata, etaMinima);
        Proiezione nuovaProiezione = new Proiezione(inizioProiezione, fineProiezione, nuovoFilm, prezzoBiglietto, 0);
        controllaSovrapposizione(nuovaProiezione.getInizioProiezione(), nuovaProiezione.getFineProiezione(), null);
        gestoreDati.getPalinsesto().aggiungiProiezione(nuovaProiezione);
        gestoreDati.salvaProiezioniSuCSV();
    }

    /**
     * Modifica una proiezione presente nel palinsesto, assicurandosi che l'utente che abbia effettuato l'operazione sia autorizzato a farlo.
     * @param utenteCorrente          l'utente che sta tentando di modificare la proiezione
     * @param codiceProiezioneVecchia il codice univoco della proiezione che si desidera modificare
     * @param titolo                  il nuovo titolo del film (può essere nullo se non si vuole modificare questo campo)
     * @param genere                  il nuovo genere del film (può essere nullo se non si vuole modificare questo campo)
     * @param regista                 il nuovo regista del film (può essere nullo se non si vuole modificare questo campo)
     * @param anno                    il nuovo anno di uscita del film (può essere 0 se non si vuole modificare questo campo)
     * @param durata                  la nuova durata espressa in minuti del film (può essere 0 se non si vuole modificare questo campo)
     * @param etaMinima               la nuova età minima per poter visionare il film (può essere negativo se non si vuole modificare questo campo)
     * @param nuovoInizio             la nuova data e orario di inizio della proiezione (può essere nullo se non si vuole modificare questo campo)
     * @param nuovaFine               la nuova data e orario della fine della proiezione (può essere nullo se non si vuole modificare questo campo)
     * @param nuovoPrezzo             il nuovo prezzo del biglietto (può essere negativo se non si vuole modificare questo campo)
     * @throws IllegalArgumentException           se l'utente corrente o il codice della proiezione, o qualsiasi altro parametro non nullo risulti non valido
     * @throws PermessiMancantiException          se l'utente che tenta l'operazione non è un proiezionista
     * @throws ProiezioneConPrenotazioniException se si tenta di modificare una proiezione che ha già delle prenotazioni associate
     * @throws SovrapposizioneProiezioneException se il nuovo orario della proiezione si sovrappone a quello di una proiezione già esistente
     * @throws IllegalStateException              se ci sono errori durante il salvataggio sul file CSV
     */
    public void modificaProiezione(Utente utenteCorrente, String codiceProiezioneVecchia, String titolo, Genere genere, String regista, int anno, int durata, int etaMinima, LocalDateTime nuovoInizio, LocalDateTime nuovaFine, double nuovoPrezzo) {
        Validatore.validaOggetto(utenteCorrente);
        Validatore.validaStringa(codiceProiezioneVecchia, "Il codice della proiezione da aggiornare non può essere nullo o vuoto.");
        if (!utenteCorrente.isProiezionista()) {
            throw new PermessiMancantiException("Non sei un proiezionista e non puoi modificare questa proiezione.");
        }
        if (haPrenotazioni(codiceProiezioneVecchia)) {
            throw new ProiezioneConPrenotazioniException("Non puoi modificare una proiezione che ha prenotazioni associate.");
        }
        Proiezione proiezioneVecchia = gestoreDati.getPalinsesto().cercaProiezioneTramiteCodice(codiceProiezioneVecchia);
        Validatore.validaOggetto(proiezioneVecchia);
        Film filmDaAggiornare = proiezioneVecchia.getFilmProiettato();
        LocalDateTime orarioInizioProposto = (nuovoInizio != null) ? nuovoInizio : proiezioneVecchia.getInizioProiezione();
        LocalDateTime orarioFineProposto = (nuovaFine != null) ? nuovaFine : proiezioneVecchia.getFineProiezione();
        if (orarioInizioProposto.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Non è possibile modificare una proiezione in una data già passata.");
        }
        controllaSovrapposizione(orarioInizioProposto, orarioFineProposto, codiceProiezioneVecchia);
        proiezioneVecchia.aggiornaDurataEOrari(durata, nuovoInizio, nuovaFine);
        if (genere != null) {
            if (!genere.equals(filmDaAggiornare.getGenere())) {
                filmDaAggiornare.setGenere(genere);
            }
        }
        if (anno != 0) {
            if (anno != filmDaAggiornare.getAnno()) {
                filmDaAggiornare.setAnno(anno);
            }
        }
        if (etaMinima >= 0) {
            if (etaMinima != filmDaAggiornare.getEtaMinima()) {
                filmDaAggiornare.setEtaMinima(etaMinima);
            }
        }
        if (nuovoPrezzo >= 0) {
            if (nuovoPrezzo != proiezioneVecchia.getPrezzoBiglietto()) {
                proiezioneVecchia.setPrezzoBiglietto(nuovoPrezzo);
            }
        }
        if (titolo != null && !titolo.isBlank()) {
            if (!titolo.equalsIgnoreCase(filmDaAggiornare.getTitolo())) {
                filmDaAggiornare.setTitolo(titolo);
            }
        }
        if (regista != null && !regista.isBlank()) {
            if (!regista.equalsIgnoreCase(filmDaAggiornare.getRegista())) {
                filmDaAggiornare.setRegista(regista);
            }
        }
        gestoreDati.salvaProiezioniSuCSV();
    }

    /**
     * Elimina una proiezione dal palinsesto, assicurandosi che l'utente che abbia effettuato l'operazione sia autorizzato a farlo.
     * @param utenteCorrente   l'utente che ha provato a eseguire l'operazione di eliminazione della proiezione
     * @param codiceProiezione il codice della proiezione da rimuovere
     * @throws IllegalArgumentException           se l'utente corrente o il codice della proiezione risultano nulli o non validi
     * @throws PermessiMancantiException          se l'utente che tenta l'operazione non è un proiezionista
     * @throws ProiezioneConPrenotazioniException se si tenta di eliminare una proiezione che ha già delle prenotazioni associate
     * @throws IllegalStateException              se ci sono errori durante il salvataggio sul file CSV
     */
    public void eliminaProiezione(Utente utenteCorrente, String codiceProiezione) {
        Validatore.validaOggetto(utenteCorrente);
        Validatore.validaStringa(codiceProiezione, "Il codice della proiezione da eliminare non può essere nullo o vuoto.");
        if (!utenteCorrente.isProiezionista()) {
            throw new PermessiMancantiException("Non sei un proiezionista e non puoi eliminare questa proiezione.");
        }
        if (haPrenotazioni(codiceProiezione)) {
            throw new ProiezioneConPrenotazioniException("Non puoi eliminare una proiezione che ha prenotazioni associate.");
        }
        Proiezione daRimuovere = gestoreDati.getPalinsesto().cercaProiezioneTramiteCodice(codiceProiezione);
        if (daRimuovere != null) {
            gestoreDati.getPalinsesto().rimuoviProiezione(daRimuovere);
        }
        gestoreDati.salvaProiezioniSuCSV();
    }

    /**
     * Restituisce una lista di proiezioni che rispettano i filtri impostati dall'utente.
     * @param titolo        il titolo o parte del titolo del film da cercare (può essere nullo per ignorare il filtro)
     * @param genere        il genere del film da cercare (può essere nullo per ignorare il filtro)
     * @param dataInizio    la data di inizio per filtrare le proiezioni (può essere nullo per ignorare il filtro)
     * @param dataFine      la data di fine per filtrare le proiezioni (può essere nullo per ignorare il filtro)
     * @param prezzoMassimo il limite massimo di prezzo del biglietto (può essere nullo per ignorare il filtro)
     * @param prezzoMinimo  il limite minimo di prezzo del biglietto (può essere nullo per ignorare il filtro)
     * @return una lista di proiezioni che rispettano i filtri indicati dall'utente
     */
    public List<Proiezione> cercaProiezione(String titolo, Genere genere, LocalDate dataInizio, LocalDate dataFine, Double prezzoMassimo, Double prezzoMinimo) {
        return gestoreDati.getPalinsesto().getListaProiezioni().stream()
                .filter(p -> titolo == null || titolo.isBlank() || p.getFilmProiettato().getTitolo().toLowerCase().contains(titolo.toLowerCase()))
                .filter(p -> genere == null || p.getFilmProiettato().getGenere() == genere)
                .filter(p -> dataInizio == null || !p.getInizioProiezione().toLocalDate().isBefore(dataInizio))
                .filter(p -> dataFine == null || !p.getFineProiezione().toLocalDate().isAfter(dataFine))
                .filter(p -> prezzoMassimo == null || p.getPrezzoBiglietto() <= prezzoMassimo)
                .filter(p -> prezzoMinimo == null || p.getPrezzoBiglietto() >= prezzoMinimo)
                .toList();
    }

    /**
     * Restituisce una stringa con i dati della proiezione selezionata.
     * @param proiezione la proiezione da visualizzare
     * @return una stringa con i dati della proiezione da visualizzare
     * @throws IllegalArgumentException se la proiezione passata risulta nulla
     */
    public String visualizzaProiezione(Proiezione proiezione) {
        Validatore.validaOggetto(proiezione);
        return proiezione.toString();
    }

    /**
     * Verifica se una specifica proiezione ha delle prenotazioni associate ad essa.
     * @param codiceProiezione il codice della proiezione da verificare
     * @return vero se la proiezione ha prenotazioni, falso altrimenti
     * @throws IllegalArgumentException se il codice della proiezione risulta nullo o vuoto
     */
    public boolean haPrenotazioni(String codiceProiezione) {
        Validatore.validaStringa(codiceProiezione, "Il codice della proiezione non può essere nullo o vuoto.");
        for (Prenotazione p : gestoreDati.getListaPrenotazioni()) {
            if (p.getCodiceProiezione().equalsIgnoreCase(codiceProiezione)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Controlla se un determinato intervallo temporale si sovrappone con le altre proiezioni esistenti.
     * @param nuovoInizio      l'orario di inizio da controllare
     * @param nuovaFine        l'orario di fine da controllare
     * @param codiceDaIgnorare il codice di una proiezione da escludere dal controllo (es. la proiezione stessa che si sta tentando di modificare)
     * @throws IllegalArgumentException           se gli orari di inizio o di fine risultano nulli
     * @throws SovrapposizioneProiezioneException se gli orari inseriti si sovrappongono temporalmente con una proiezione già esistente
     */
    private void controllaSovrapposizione(LocalDateTime nuovoInizio, LocalDateTime nuovaFine, String codiceDaIgnorare) {
        Validatore.validaOggetto(nuovoInizio, "L'orario di inizio per il controllo della sovrapposizione non può essere nullo.");
        Validatore.validaOggetto(nuovaFine, "L'orario di fine per il controllo della sovrapposizione non può essere nullo.");
        for (Proiezione esistente : gestoreDati.getPalinsesto().getListaProiezioni()) {
            if (esistente.getCodiceUnivoco().equalsIgnoreCase(codiceDaIgnorare)) {
                continue;
            }
            LocalDateTime inizioEsistente = esistente.getInizioProiezione();
            LocalDateTime fineEsistente = esistente.getFineProiezione();
            if (nuovoInizio.isBefore(fineEsistente) && nuovaFine.isAfter(inizioEsistente)) {
                throw new SovrapposizioneProiezioneException("Impossibile procedere: l'orario si sovrappone temporalmente con la seguente proiezione: \n" + esistente);
            }
        }
    }

    /**
     * Restituisce le sole proiezioni ancora prenotabili, cioè quelle non ancora iniziate.
     * @param titolo        il titolo o parte del titolo del film da cercare (può essere nullo per ignorare il filtro)
     * @param genere        il genere del film da cercare (può essere nullo per ignorare il filtro)
     * @param dataFine      la data di fine per filtrare le proiezioni (può essere nullo per ignorare il filtro)
     * @param prezzoMassimo il limite massimo di prezzo del biglietto (può essere nullo per ignorare il filtro)
     * @param prezzoMinimo  il limite minimo di prezzo del biglietto (può essere nullo per ignorare il filtro)
     * @return una lista di proiezioni che rispettano i filtri indicati dall'utente
     */
    public List<Proiezione> cercaProiezioniPrenotabili(String titolo, Genere genere, LocalDate dataFine, Double prezzoMassimo, Double prezzoMinimo) {
        LocalDateTime adesso = LocalDateTime.now();
        return cercaProiezione(titolo, genere, adesso.toLocalDate(), dataFine, prezzoMassimo, prezzoMinimo).stream()
                .filter(proiezione -> proiezione.getInizioProiezione().isAfter(adesso))
                .toList();
    }
}
