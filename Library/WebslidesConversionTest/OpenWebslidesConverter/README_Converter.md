# webslides-01

## Gebruik dummyconverter

Ga in een console naar de map waar OpenWebslidesConverter.jar staat.
Voor de voorlopige versie moeten de bestanden "shower.html" en "content.html" in dezelfde directory staan.

Er zijn drie argumenten
* type output: raw of shower (optioneel, default: shower)
* bestand (.pptx)
* output directory (optioneel, default: output)

**Commando:**

$ java -jar OpenWebslidesConverter.jar [output type] bestand.pptx [output dir]

**voorbeelden:**

$ java -jar OpenWebslidesConverter.jar bestand.pptx

$ java -jar OpenWebslidesConverter.jar raw bestand.pptx

$ java -jar OpenWebslidesConverter.jar shower bestand.pptx

$ java -jar OpenWebslidesConverter.jar bestand.pptx "geconverteerde bestanden"

$ java -jar OpenWebslidesConverter.jar raw bestand.pptx out

$ java -jar OpenWebslidesConverter.jar shower bestand.pptx out

## Type output
Er zijn twee types output
* **shower** (default): Geeft een .html terug met de code voor de js-plugin 'shower'.
* **raw**: Geeft een .html met enkel &lt;div&gt;-elementen die slides voorstellen.
