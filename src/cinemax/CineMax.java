// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * Questa classe rappresenta il punto di ingresso dell'applicazione.
 * Si occupa di inizializzare tutti i gestori necessari per il funzionamento
 * del sistema e di far partire l'interfaccia utente basata su terminale.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class CineMax {
    /**
     * Metodo principale che avvia l'esecuzione del programma.
     * Crea le istanze fondamentali per la logica del sistema e le passa all'interfaccia
     * terminale per avviare l'interazione con l'utente.
     * @param args argomenti passati da riga di comando all'avvio dell'applicazione
     */
    public static void main(String[] args) {
        try {
            GestoreDati gestoreDati = new GestoreDati();
            GestoreUtenti gestoreUtenti = new GestoreUtenti(gestoreDati);
            GestoreProiezioni gestoreProiezioni = new GestoreProiezioni(gestoreDati);
            GestorePrenotazioni gestorePrenotazioni = new GestorePrenotazioni(gestoreDati);
            InterfacciaTerminale tui = new InterfacciaTerminale();
            tui.avvia(gestoreUtenti, gestoreProiezioni, gestorePrenotazioni);
        } catch (Exception e) {
            System.err.println("Impossibile avviare CineMax: " + e.getMessage());
            System.err.println("Verifica che la cartella 'data' sia raggiungibile e che i file CSV siano leggibili e scrivibili.");
        }
    }
}
