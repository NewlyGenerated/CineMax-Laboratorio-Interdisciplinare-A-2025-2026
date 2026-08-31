Requisiti 
-------------------------------------------------------------------------------------------------
JDK versione 21 o superiore. Nessuna libreria esterna. 


Esecuzione 
-------------------------------------------------------------------------------------------------
Dalla cartella radice del progetto: 

java -jar bin/CineMax.jar 

Oppure dalla cartella bin: 

java -jar CineMax.jar

Inoltre, su Windows se gli accenti non vengono visualizzati correttamente, eseguire il comando "chcp 65001" prima di avviare il programma.


Compilazione 
-------------------------------------------------------------------------------------------------
javac --release 21 -encoding UTF-8 -d build src/cinemax/*.java 
jar --create --file bin/CineMax.jar --main-class cinemax.CineMax -C build . 

L'opzione -enconding UTF-8 è necessaria: i file sorgente contengono caratteri accentati e il glifo dell'euro. 


Documentazione JavaDoc 
-------------------------------------------------------------------------------------------------
javadoc -private -encoding UTF-8 -docencoding UTF-8 -charset UTF-8 -author -version -d doc/javadoc -sourcepath src cinemax


Contenuto 
-------------------------------------------------------------------------------------------------
bin/ il file eseguibile CineMax.jar 
src/ codice sorgente 
doc/ manuale utente, manuale tecnico e documentazione JavaDoc 
data/ file CSV dell'applicazione 
lib/ librerie esterne (nessuna richiesta)

Le credenziali degli utenti predefiniti sono elencate nel manuale utente, paragrafo "Data set di test".