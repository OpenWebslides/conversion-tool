Logger
===================


Logger die gebruikt kan worden voor **Directory Guard** en **Convertor.**

----------

Logs
-------------

De logger kan gebruikt worden voor:

> - Error logs
> - File converted logs
> - File deleted logs
> - File uploaded logs
> - Custom logs

Example Log File
-------------
Serverlog_20170224.log

> 12:44:03 Log 	Some Fault 
> There was a mistake doing that thing
> Fix: Try not doing that thing
> 
> 12:44:03 File Uploaded 	
> Karel uploaded File.txt(0Kb)
> 
> 12:44:03 File Converted 	
> File.txt(0Kb) from Karel was converted
> 
> 12:44:03 File Deleted 	
> File.txt(0Kb) from Karel was deleted
> 
> 12:44:03 Error - Error while saving file
> 	java.lang.NullPointerException 	at
> java.io.Writer.<init>(Writer.java:88) 	at
> java.io.PrintWriter.<init>(PrintWriter.java:113) 	at
> java.io.PrintWriter.<init>(PrintWriter.java:100) 	at
> openwebslidesconverter.OpenWebslidesConverter.logExample(OpenWebslidesConverter.java:35)
> 	at
> openwebslidesconverter.OpenWebslidesConverter.main(OpenWebslidesConverter.java:24)


