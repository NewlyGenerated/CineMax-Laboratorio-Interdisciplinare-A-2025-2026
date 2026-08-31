// Kejsi Xhafaj, 759934, VA
package cinemax;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * La classe Palinsesto rappresenta il palinsesto, cioè la programmazione delle proiezioni indicandone il giorno
 * e l'orario per ciascuna di esse.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public class Palinsesto {
    /**
     * L'attributo <code>listaProiezioni</code> memorizza la collezione di tutte le proiezioni pianificate all'interno del palinsesto.
     */
    private final List<Proiezione> listaProiezioni;

    /**
     * Inizializza un nuovo palinsesto vuoto.
     */
    public Palinsesto() {
        this.listaProiezioni = new LinkedList<>();
    }

    /**
     * Restituisce la lista delle proiezioni in modalità di sola lettura, impedendo che possa venir modificata erroneamente.
     * @return una lista non modificabile delle proiezioni
     */
    public List<Proiezione> getListaProiezioni() {
        return Collections.unmodifiableList(listaProiezioni);
    }

    /**
     * Aggiunge una nuova proiezione al palinsesto.
     * @param proiezione la proiezione da aggiungere
     * @throws IllegalArgumentException se la proiezione passata è nulla
     */
    public void aggiungiProiezione(Proiezione proiezione) {
        Validatore.validaOggetto(proiezione);
        listaProiezioni.add(proiezione);
    }

    /**
     * Rimuove una proiezione dal palinsesto.
     * @param proiezione la proiezione da rimuovere
     * @throws IllegalArgumentException se la proiezione passata è nulla
     */
    public void rimuoviProiezione(Proiezione proiezione) {
        Validatore.validaOggetto(proiezione);
        listaProiezioni.remove(proiezione);
    }

    /**
     * Restituisce la proiezione identificata dal suo codice univoco, se non esiste viene restituito null.
     * @param codiceUnivoco il codice univoco della proiezione ricercata
     * @return la proiezione ricercata, null se non viene trovata
     * @throws IllegalArgumentException se il codice univoco passato è nullo o vuoto
     */
    public Proiezione cercaProiezioneTramiteCodice(String codiceUnivoco) {
        Validatore.validaStringa(codiceUnivoco, "Il codice univoco della proiezione non può essere nullo o vuoto");
        for (Proiezione p : this.listaProiezioni) {
            if (p.getCodiceUnivoco().equalsIgnoreCase(codiceUnivoco)) {
                return p;
            }
        }
        return null;
    }
}
