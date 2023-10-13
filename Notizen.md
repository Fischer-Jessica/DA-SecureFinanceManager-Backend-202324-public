# Notizen zum Backend
[I'm a link to a markdown-cheat-sheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

## Bugs, wie die Error-Message lautet, wie sie aufgetreten sind und wer sie entdeckt hat

------------------------------------------------------------------------------------

## bekannte Bugs und die Erklärung, warum sie noch da sind
1. <p>Wenn man einen Eintrag löscht, wird die Id nicht zurückgesetzt. Beispiele dafür sind:</p>
   <p>Eintrag 1,2,3    --->   ich lösche Eintrag 2   --->   Eintrag 1,3</p>
   <p>Eintrag 1,2,3   --->   ich lösche Eintrag 3   --->   ich erstelle einen neuen Eintrag   --->   Eintrag 1,2,4</p>
   <br>
   <p>Das Verhalten ist auf den AUTO_INCREMENT-Schlüssel zurückzuführen. Wenn du Zeilen aus einer Tabelle löschst, wird der Wert des SERIAL-Schlüssels nicht automatisch neu sortiert oder aktualisiert. Dies führt dazu, dass es Lücken zwischen den Primärschlüsselwerten gibt, nachdem Zeilen gelöscht wurden. Es ist wichtig zu verstehen, dass dies normal und beabsichtigt ist, um die Integrität und Konsistenz der Datenbank zu gewährleisten. Die Datenbank behält die gelöschten Schlüsselwerte nicht wieder, um potenzielle Probleme mit Beziehungen, Fremdschlüsseln und Transaktionen zu vermeiden.</p>
       
       Wenn du jedoch die Präsentation der Daten ohne Lücken im Primärschlüssel bevorzugst, gibt es einige Möglichkeiten, dies zu erreichen:
       * Neuanordnung des Primärschlüssels: Wenn du wirklich keine Lücken im Primärschlüssel haben möchtest, könntest du nach dem Löschen von Zeilen den Primärschlüssel neu anordnen. Dies ist jedoch keine empfohlene Methode, da sie ineffizient sein kann und potenziell Probleme mit anderen Tabellen oder Beziehungen verursachen kann.
       * Verwendung eines anderen eindeutigen Schlüssels: Anstelle eines AUTO_INCREMENT-Schlüssels könntest du einen anderen Datentyp wie UUIDs (Universally Unique Identifier) als Primärschlüssel verwenden. UUIDs sind zufällig generierte Werte und haben normalerweise keine Lücken.
   <p>Es ist jedoch wichtig zu beachten, dass das Vorhandensein von Lücken im Primärschlüssel normalerweise keine Auswirkungen auf die Funktionsweise der Datenbank oder der Anwendung hat. Es ist ein normales Verhalten von Datenbanken, und die meisten Anwendungen sollten in der Lage sein, damit umzugehen. Wenn es keine spezifischen Anforderungen gibt, die das Entfernen von Lücken im Primärschlüssel erfordern, wird empfohlen, das Verhalten so zu belassen, wie es ist.</p>
<br>

2. <p>Wenn man bei einer verschlüsselten Spalte zu wenig Zeichen übergibt, wie in folgendem Beispiel, kommt es zu einem Fehler:</p>
   <p>"a", was kleiner als 2 Byte ist</p>
   <p>Ich hatte bei entry_description einmal den folgenden Fehler: Last unit does not have enough valid bits und habe ein paar Buchstaben hinzufügen müssen</p>

<br>

---------------------------------------------------------------------

## Informationen

<p>Grundsätzlich wäre es üblich, dass man die User-Informationen im Header mit übergibt oder diese durch einen Session-Key ersetzt, welcher eine bestimmte Zeit lang gültig ist.</p>
<p>Bei Post wäre es üblicher ein gesamtes Objekt zu übergeben, nicht nur Parameter</p>
<p>Wenn ich das ganze Objekt neu mache, sollte ich einen put verwenden, bei welchem das ganze Objekt übergeben wird.</p>