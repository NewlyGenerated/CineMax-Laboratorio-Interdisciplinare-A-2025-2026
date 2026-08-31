// Kejsi Xhafaj, 759934, VA
package cinemax;

/**
 * Rappresenta uno dei generi cinematografici che un film può avere.
 * @author Kejsi Xhafaj
 * @version 2.7
 */
public enum Genere {
    AZIONE("Action"),
    AVVENTURA("Adventure"),
    ANIMAZIONE("Animation"),
    BIOGRAFICO("Biography"),
    COMMEDIA("Comedy"),
    CRIMINE("Crime"),
    DRAMMATICO("Drama"),
    FAMIGLIA("Family"),
    FANTASY("Fantasy"),
    NOIR("Film-Noir"),
    HORROR("Horror"),
    MISTERO("Mystery"),
    THRILLER("Thriller"),
    WESTERN("Western");
    /**
     * L'attributo <code>nomeInglese</code> memorizza la traduzione in lingua inglese del genere cinematografico.
     */
    private final String nomeInglese;

    /**
     * Costruisce un nuovo genere associando il rispettivo nome in inglese.
     * @param nomeInglese il nome in inglese del genere cinematografico
     */
    Genere(String nomeInglese) {
        this.nomeInglese = nomeInglese;
    }

    /**
     * Converte una stringa di testo nel corrispondente valore della classe enumerativa Genere.
     * @param stringa la stringa da convertire
     * @return il Genere corrispondente trovato
     * @throws IllegalArgumentException se il genere non viene riconosciuto o se la stringa passata è nulla o vuota
     */
    public static Genere daStringa(String stringa) {
        Validatore.validaStringa(stringa, "Il genere da convertire non può essere nullo o vuoto.");
        String testoPulito = stringa.trim();
        for (Genere g : Genere.values()) {
            if (g.name().equalsIgnoreCase(testoPulito) || g.nomeInglese.equalsIgnoreCase(testoPulito)) {
                return g;
            }
        }
        throw new IllegalArgumentException("Genere non riconosciuto: " + stringa);
    }

    /**
     * Restituisce il nome del genere in inglese.
     * @return il nome in inglese
     */
    public String getNomeInglese() {
        return nomeInglese;
    }
}
