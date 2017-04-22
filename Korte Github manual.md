##Korte Github manual

###1 Hoofdbranch: Master
###2 Projectbranches: webslides_webapp, open_webslides_converter
###X Gepushte issue-branches

###---Klonen & Setup---

1. Clone
`git clone https://github.ugent.be/iii-vop2017/webslides-01.git`

2. Remote branch binnenhalen & direct tracken
Schema: git checkout --track -b [branchname] origin/[branchname]

VB: voor de 3 hoofdbranches

`git checkout --track -b open_webslides_converter origin/open_webslides_converter`

`git checkout --track -b webslides_webapp origin/webslides_webapp`

###---Setup checken---###

####1.Branches tonen remote & lokaal

`git branch -vv`
output: lokalenaam (huidige branch in groen) | SHA1 start | remotenaam gekoppeld aan lokale naam (donkerblauw) | laatste commit message op die branch

bijkomende opties: git branch -rvv (toont enkel remotes) git branch -lvv (toont enkel lokale)

####2. Andere view met meer info met git remote show origin:
vb: sample output
`git remote show origin`

>\* remote origin  
>  Fetch URL: https://github.ugent.be/iii-vop2017/webslides-01.git  
>  Push  URL: https://github.ugent.be/iii-vop2017/webslides-01.git  
>  HEAD branch: master  

>  Remote branches:  
>    converter             tracked   
>    directory_guard       tracked  
>    logger                tracked  
>    master                tracked  
>    open_webslides_webapp tracked   
>    upload_button         tracked  

>  Local branches configured for 'git pull':   
>     converter             merges with remote converter   
>     directory_guard       merges with remote directory_guard   
>     logger                merges with remote logger   
>     master                merges with remote master   
>     open_webslides_webapp merges with remote open_webslides_webapp   
>     upload_button         merges with remote upload_button    

>  Local refs configured for 'git push': 
>    converter             pushes to converter             (up to date)  
>    directory_guard       pushes to directory_guard       (up to date)  
>    logger                pushes to logger                (up to date)  
>    master                pushes to master                (up to date)  
>    open_webslides_webapp pushes to open_webslides_webapp (up to date)  
>    upload_button         pushes to upload_button         (up to date)  


\* = current branch
fetch & push url = URL vd repo op github.ugent.be

Remote branches:
oplijsting

Local branches configured for git pull: 
inhoud lijkt op de output van git branch -vv alleen krijg je hier meer info
converter           merges with      remote converter
betekent dat je in de branch converter gewoon git pull kan gebruiken zonder argumenten en er zal van de juiste remote branch worden gepulled

Local refs configured for git push
inhoud lijkt sterk op vorige paragraaf, de betekenis van dit blok is als volgt:
er ligt voor elke branch vast naar welke remote gepusht zal worden
laatste kolom: 
(up to date)            remote & local zijn in sync, geen upstream commits die lokaal niet aanwezig zijn & omgekeerd
(X commits ahead of )   local is voor op remote, pushen
(Y commits behind of )  local is achter op remote, pullen
(fast forwardable)      remote / local heeft nieuwe content en er gaan geen conflicten optreden pushen / pullen

###---Nieuwe branches & bewegen tussen branches---###

Verplaatsen van A -> B: `git checkout [branchname]`
####__Let op__: dit moet je doen vooraleer je begint te werken anders zal git denken dat je veranderingen aan files moeten komen in de branch waar je op dat moment in stond

####1.Nieuwe branch: 
simpelste vorm
`git branch [branchname]`

nieuwe branch maken en onmiddellijk naar switchen
`git checkout -b [branchname]`

__OPM:__ beide commando's gaan als startpunt het laatste punt van de branch waarin je stond (toen je een nieuwe branch vroeg) nemen, dus let goed op in welke branch je staat als je een nieuwe maakt

####2.Maneel upstream zetten voor een branch:
`git branch -u origin/[branchname]`

####3.Branches pushen
eigen nieuwe lokale branch pushen (voor de eerste keer)
`git push -u origin [new remote branchname]`

####4.Branches ophalen en tracken: zie Setup

###---Files toevoegen & Commits---###
`git status`

Sample output:
>$ git status

>On branch master
>Your branch is up-to-date with 'origin/master'.
>  (use "git add <file>..." to include in what will be committed)
>
>        DirectoryGuard/conversedFiles/
>        Korte Github manual.md

nothing added to commit but untracked files present (use "git add" to track)`

####untracked files present:
Dit is git die laat weten dat er files zijn die git voorlopig niet meeneemt maar ook niet negeert, je kan ze dus toevoegen als je wil
__Toevoegen is aangewezen voor nieuwe files, klassen, resources__

Toevoegen is __niet__ aangewezen voor build output (DirectoryGuard/conversedFiles bv.), dit zal weggewerkt worden in de .gitignore

`dhoogla@L-M47 MINGW64 ~/Documents/AUGent/3eBach/2/Bachelorproef/webslides-01 (master)`

`$ git add Korte\ Github\ manual.md`

>dhoogla@L-M47 MINGW64 ~/Documents/AUGent/3eBach/2/Bachelorproef/webslides-01 (master)
>$ git status

>On branch master
>Your branch is up-to-date with 'origin/master'.
>Changes to be committed:
>  (use "git reset HEAD <file>..." to unstage)
>
>       new file:   Korte Github manual.md
>
>Untracked files:
>  (use "git add <file>..." to include in what will be committed)
>
>        DirectoryGuard/conversedFiles/`

Na git add komt de nieuwe file bij changes to be committed

_hint_ bij git add kan je na het typen van enkele letters tab gebruiken om aan te vullen

`git commit -m "start Github manual"`
de -m vlag staat voor message, het is handig om je commits nuttige namen te geven als je ooit een commit terug moet vinden

>$ git status
>On branch master
>Your branch is up-to-date with 'origin/master'.
>Changes not staged for commit:
>  (use "git add <file>..." to update what will be committed)
>  (use "git checkout -- <file>..." to discard changes in working directory)
>
>        modified:   Korte Github manual.md



Heb je meerdere files aangepast? gebruik dan
#### `git commit -am "msg"`
de -a vlag zorgt ervoor dat aangepast files & verwijderde files zullen worden gecommit, nieuwe files echter niet, die moet je toevoegen met git add

Als je lokaal hebt gecommit zal git status nu ergens in de output dit opnemen

`Your branch is ahead of 'origin/master' by 1 commit.`

###---Branches mergen & verwijderen---###

Als je branches hebt gemaakt van een van de drie projectbranches komt er een punt waarop je je stabiele subbranch wil samenvoegen met de hoofdbranch.  
Dat kan op meerdere manieren. De keuze hangt af van het aantal veranderingen, of je samengewerkt hebt en of het een remote branch is of niet. 
Pull request (PR) <-> mergen zonder Pull Request

* Het is een remote branch en je hebt samengewerkt (anderen hebben de branch lokaal en eigen changes) => **Pull request**

* Het is een remote branch maar je bent zeker dat je de enige bent die eraan gewerkt heeft => **Pull request of mergen zonder expliciete PR**

* Het is enkel lokale branch => **geen PR**

Pull request is de manier om aan de teamleden te laten weten dat je je branch wil mergen. Als je in team gewerkt hebt op die branch assign dan zeker aan jezelf en aan je teammate.  

####**Pull request stappenplan:** ook op github, maar hier wat meer uitgeschreven

* `git pull` (op de projectbranch)
* `git checkout [issuebranch]`
* `git merge [projectbranch]` 
Hier zal het waarschijnlijk "mislopen" de kans is groot dat je iets zoals dit zal zien:
>  git merge open_webslides_webapp  
> Auto-merging open_webslides_webapp/web/WEB-INF/master_template.xhtml  
> CONFLICT (content): Merge conflict in open_webslides_webapp/web/WEB-INF/  
> master_template.xhtml  
> Removing open_webslides_webapp/src/conf/MANIFEST.MF
> Automatic merge failed; fix conflicts and then commit the result.

Git zegt dat er een conflict is dat het niet automatisch kan oplossen, dus moet je dat manueel doen.  
**Stappenplan Concreet:**  
* Ga naar een bestand in conflict in netbeans / sublime / ...  
* Je zal tags zien staan HEAD <<<<< code ===== code [branchnaam of sha1 commit] >>>>>>  
Die code moet je manueel vergelijken en de juiste zaken behouden en schrappen zodat het eindresultaat is wat je wil.  
* Doe je aanpassingen en sla het bestand op  
* `git status` zal iets van volgende aard tonen  
> Unmerged paths:  
>  (use "git add <file>..." to mark resolution)  
>    
>        both added:      open_webslides_webapp/web/WEB-INF/faces-config.xml  
>        both modified:   open_webslides_webapp/web/WEB-INF/  master_template.xhtml  
>   
> Changes not staged for commit:  
> (use "git add <file>..." to update what will be committed)  
> (use "git checkout -- <file>..." to discard changes in working directory)  
>  
>        modified:   open_webslides_webapp/nbproject/project.properties  

* `git add voor de files die je wil`  
*hint*: als je echt zeker bent dat je alle veranderde / nieuwe files wil toevoegen en je hebt geen files verwijderd dan kan je `git add .` gebruiken.
(let er echt mee op want verwijderde files worden opnieuw toegevoegd met git add .!)

* evt `git status` om te zien of alles OK is  

* `git commit -am "[msg]"`
Je hebt nu al je werk opgeslagen in je feature branch, met de oplossing voor de conflicten.
**Merk op dat je dus eerst de projectbranch zal mergen in je feature branch**

* `git checkout [projectbranch]`  

* `git merge --no-ff [featurebranch]`  
Schrijf een merge commit message of sluit vi af met de default msg (gebruik :q! enter)
Deze stap zal nu geen conflicten opleveren, tenzij iemand op de tijd dat je aan het mergen was nog nieuwe commits heeft toegevoegd aan de projectbranch die gepusht heeft en git ze niet automatisch kan mergen.

* `git push`  
Je stond op de projectbranch dus je pusht de gemergde versie upstream.

* `git push origin --delete [featurename]`  
* `git branch -d [featurename]`
**let op**: als je samengewerkt hebt en je bent niet zeker of je teammate geen nieuw werk meer heeft in de feature branch die hij / zij nog niet gepusht heeft, dan moet je dat eerst vragen!  
* `git fetch -p`  
Verwijdert eventuele lokale referenties naar remote branches die niet meer bestaan. 

**KLAAR**

####*opm*: als je lokaal tracking branches had die niet langer bestaan op de remote, zal je dat zien in de uitvoer van `git remote show origin`, om deze referenties ook lokaal te verwijderen moet je `git remote prune origin`gebruiken.


###---__Conventies__---###

####Conventie: 
- kies een issue van een projectbranch
- maak een lokale branch vanaf de juiste projectbranch 
- werk in die lokale branch
- wil je je werk delen voor review of om samen te werken: push je branch

####Conventie 2:
voor echt kleine changes kan je rechtstreeks op de projectbranch werken: 
de projectbranch mag nooit in een broken state zijn & er horen ook geen onafgewerkte issues in thuis

####Conventie 3:
- de master branch dient voor de logboeken en andere documenten waarvan ons wordt gevraagd om ze daar te zetten
- de master branch dient __niet__ om te werken aan de code
