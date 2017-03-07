# webslides-01

## Gebruik dummyconverter

Ga in een console naar de map waar OpenWebslidesConverter.jar staat.
Voor de voorlopige versie moeten de bestanden "shower.html" en "content.html" in dezelfde directory staan.

**Commando:**

$ java -jar OpenWebslidesConverter.jar -i slides.pptx [optionele vlaggen]

**Opties en vlaggen:**

* -i bestand.pptx **(verplicht)**
* -o outputFolder (default: output)
* -t outputType (raw of shower) (default: shower)
* ---
* -fl (file logging) (default uit)
* -cl (console logging) (default aan)

Voor de logging zijn er dus vier mogelijkheden:
* (leeg) = default naar console
* -fl = enkel naar een logbestand
* -cl = enkel naar de console, zelfde als default
* -fl -cl = zowel naar de console als een logbestand

De console zal enkel de melding of het bericht tonen. Het logbestand zal een uitgebreide stacktrace bevatten.

**voorbeelden:**

$ java -jar OpenWebslidesConverter.jar -i bestand.pptx

$ java -jar OpenWebslidesConverter.jar -t raw -i bestand.pptx

$ java -jar OpenWebslidesConverter.jar -i bestand.pptx -t shower 

$ java -jar OpenWebslidesConverter.jar -i bestand.pptx -o "geconverteerde bestanden"

$ java -jar OpenWebslidesConverter.jar -t raw -i bestand.pptx -o out

$ java -jar OpenWebslidesConverter.jar -i bestand.pptx -fl -cl

$ java -jar OpenWebslidesConverter.jar -i bestand.pptx -fl

## Type output
Er zijn twee types output
* **shower** (default): Geeft een .html terug met de code voor de js-plugin 'shower'.
* **raw**: Geeft een .html met enkel &lt;div&gt;-elementen die slides voorstellen.
