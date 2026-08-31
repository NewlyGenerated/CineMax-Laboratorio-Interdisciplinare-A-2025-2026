// Kejsi Xhafaj, 759934, VA
/**
 * Contiene tutte le classi che compongono l'applicazione CineMax, un sistema di gestione per cinema monosala che
 * permette di amministrare il palinsesto delle proiezioni e le prenotazioni dei posti in sala.
 * <p>
 * Il package è organizzato secondo un'architettura a livelli che separata la rappresentazione dei dati, la logica
 * applicativa, la persistenza su file e l'interazione con l'utente.
 * <p>
 * Livello del modello. Raccoglie le entità del dominio e le relative regole di validazione, applicate sia nei costruttori
 * sia nei metodi di modifica, in modo che un oggetto non possa mai trovarsi in uno stato incoerente.
 * <ul>
 *     <li>{@link cinemax.Film} descrive l'opera cinematografica proiettata.</li>
 *     <li>{@link cinemax.Genere} enumera i generi cinematografici ammessi.</li>
 *     <li>{@link cinemax.Proiezione} associa un film a una collocazione temporale, a un prezzo e al numero di posti occupati.</li>
 *     <li>{@link cinemax.Palinsesto} raccoglie l'insieme delle proiezioni programmate.</li>
 *     <li>{@link cinemax.Prenotazione} collega un utente a una proiezione tramite un codice univoco.</li>
 *     <li>{@link cinemax.Utente} rappresenta un utente registrato con le sue credenziali cifrate.</li>
 *     <li>{@link cinemax.Ruolo} enumera i ruolio che determinano i permessi di accesso.</li>
 *     </ul>
 * <p>
 * Livello della logica applicativa. Espone i casi d'uso del sistema, verificando i permessi dell'utente richiedente
 * prima di ogni operazione e delegando la persistenza al gestore dei dati.
 * <ul>
 *     <li>{@link cinemax.GestoreUtenti} gestisce registrazione, autenticazione e modifica dei dati dell'utente.</li>
 *     <li>{@link cinemax.GestoreProiezioni} gestisce aggiunta, cancellazione e modifica dei dati delle proiezioni.</li>
 *     <li>{@link cinemax.GestorePrenotazioni} gestisce aggiunta, cancellazione e modifica dei dati delle prenotazioni.</li>
 *     </ul>
 * <p>
 * Livello di presentazione. {@link cinemax.InterfacciaTerminale} realizza l'interfaccia testuale, occupandosi esclusivamente
 * della raccolta degli input e della visualizzazione dei risultati. {@link cinemax.CineMax} è il punto di ingresso che istanzia
 * i gestori e avvia l'interfaccia.
 * <p>
 * Classi di utilità. {@link cinemax.Sicurezza} implementa l'hashing con l'algoritmo PBKDF2, salt casuale e numero di iterazioni.
 * {@link cinemax.Validatore} raccoglie i controlli ricorrenti su stringhe e riferimenti nulli.
 * <p>
 * Eccezioni. Le condizioni di errore previste dalle regole di dominio sono segnalate da eccezioni dedicate, tutte non controllate:
 *  {@link cinemax.PermessiMancantiException} per le operazioni non consentite dal ruolo,
 *  {@link cinemax.PostiEsauritiException} per l'esaurimento della capienza,
 *  {@link cinemax.EtaMinimaConsentitaException} per il mancato rispetto dell'età minima per la visione del film,
 *  {@link cinemax.PrenotazioneInesistenteException} per le ricerche senza esito,
 *  {@link cinemax.SovrapposizioneProiezioneException} per i conflitti di orario tra le proiezioni nel palinsesto e
 *  {@link cinemax.ProiezioneConPrenotazioniException} per le modifiche a proiezioni che hanno già delle prenotazioni associate.
 *  
 * @author Kejsi Xhafaj
 * @version 2.7
 * @see cinemax.CineMax#main(String[]) 
 * @see cinemax.GestoreDati
 * @see cinemax.InterfacciaTerminale#avvia(GestoreUtenti, GestoreProiezioni, GestorePrenotazioni) 
 */
package cinemax;
