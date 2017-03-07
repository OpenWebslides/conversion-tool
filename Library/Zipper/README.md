Zipper
===================


Een zipper klasse om bestanden aan een .zip toe te voegen. Kan zowel OO gebruikt worden als statisch

----------


OO
-------------
		//Maak zipper object, en waar zip ogeslaan moet worden
        Zipper zipper = new Zipper("C://temp//archive1.zip");
        
        //Voeg een voor een bestanden toe
        zipper.addToZipFile(new File("C://temp//Serverlog_20170228.log"));        
        zipper.addToZipFile(new File("C://temp//Serverlog_20170228Copy.log"));
        
        //Maak zip bestand
        zipper.zipIt();
Static
----------

        //Lijst van bestanden die moeten toegevoegd worden
        ArrayList<File> files = new ArrayList();
        files.add(new File("C://temp//Serverlog_20170228.log"));
        files.add(new File("C://temp//Serverlog_20170228Copy.log"));
        
        //Maak zip
        Zipper.zipIt("C://temp//archive2.zip", files);
