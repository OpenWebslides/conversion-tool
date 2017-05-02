
## How to use a convertor
By using a convertor, you will get a filled PPT object. You can then convert this PPT object to HTML

        //The original file
        File file = new File("C:\\temp\\testPpts\\4.pptx");

        //Where should images be saved?
        FileOutputStream dest = new FileOutputStream("C:\\temp\\testPpts\\zip.zip");
        ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(dest));
        
        IConverter converter;
        try {
            //The correct converter will automaticly be loaded by the converterfactory
            converter = ConverterFactory.getConverter(file);
            //Set where you want error/info messages to go, default is Std
            converter.setOutput(new StdLogOutput(new Logger("c:\\temp\\log", "logging", "karel")));
            //This PPT object will be filled
            PPT ppt = new PPT();
            //parse the object
            converter.parse(ppt,zip,"images");
            //Dont forget the close the zip
            zip.close();
        } catch (IllegalArgumentException ex) {
        //
        } catch (PDFException ex) {
        //
        } catch (Exception ex) {
        //
        }

