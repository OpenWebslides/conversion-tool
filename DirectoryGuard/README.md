# Directory guard

## overview
1. What it does
2. How to use
3. Log files
4. Application flow
5. Additional info Threads

## What it does
The directory guard is a **multithreaded** java application. Its purpose is to guard a folder or directory and detect changes. When a new _(.pptx)_ file is detected the guard starts a converter.

The application is multithreaded because every new file starts a new _converterThread_. In addition a single _LogThread_ is created at the start of the application. A queue _(java.util.Queue)_ is passed to both the _LogThread_ and every _ConverterThread_ to register the logging. Finally, the application is responsible to log events with timestamps of the converter. This is done by flushing a log block to a file for every converter when it has finished.

## How to use
Start the application from the command line with the following command:
```
$ java -jar DirectoryGuard.jar [-r] directory
```
with _directory_ the folder that should be monitored.

Flag -r stands for recursive operation: the application is able to monitor multiple folders at the same time. This behaviour is currently experimental.

## Log files
Two log files are created. The first is the log file of the directory guard itself. The second one prints the output that was passed from the converters to the _LogThread_.

## Application flow
There is a detailed sequence diagram for this part of the application. An image of that diagram can be found here: https://www.dropbox.com/sh/08em1i6u87ykc8e/AAC9KZmcW_jQzhvUHdOkc_V3a?dl=0
The file is called Directory Guard.jpg.

## Additional info Threads
As stated above the application creates two threads: _ConverterThreads_ and a single _LogThread_. The _ConverterThread_ holds the converter and communicates with it to get the output. The used entry point of the converter asks a queue of Strings. Every log is stored in a String and that String is passed into the ```Queue<String>```. Before terminating the _LogThread_, after finishing the converter itself (or on a runtime exception), that queue is passed into a ```Queue<Queue<String>>```. That last queue of queues is used by the _LogThread_ to handle the output.
