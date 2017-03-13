# OpenWebslidesConverter

## overview
1. What it does
2. Parameters
3. converter.properties file
4. Template
5. Use as a command line tool
   1. Logging
   2. Command
6. Use inside/with another java application
   1. As library
   2. Using reflection

## What it does
The OpenWebslidesConverter is a tool to convert .pptx files into html. The converter does not rebuild the slides with each element on the same place or same size. It is the meaning to interpret the content and create generic code. The information stays the same but it is possible to use another template without changing any code.

You have two choices as output. The first is just raw, plain html code. Secondly, it is also possible to generate code for the shower and a template.

## Parameters
There are three parameters.

1. input file: must be of the type .pptx
2. output folder: where the converted file(s) should be placed
3. output type: convert to plain code (raw) or with a template and shower (shower)

Values can be set on the command line or within the _converter.properties_ file. If not, default values are used.

A short overview:

|parameter  |command line|value            |default|             |
|---	    |:---:       |---              |---    |---          |
|input file |-i          |*.pptx           |   	   |**required**|
|output dir |-o          |   	           |output/|optional     |
|output type|-t          |raw &#124; shower|raw    |optional     |

The default values can be changed: _converter.properties_ overwrites the default values, the command line arguments overwrites the properties value.

## _converter.properties_ file
One way to set the values is to create the _converter.properties_ file in the same directory as the converter jar.
An example:
```
#directory for the log file: must end with "\\"
LOG_FILE=c:\\temp\\log\\

#output type: raw or shower
OUTPUT_TYPE=shower

#directory for the converted files
OUTPUT_DIR=converted files
```

## Template

When using ```shower``` as output type, there should be a folder ```Template/_shared/``` in the same directory as the converter jar. That folder includes the subfolders ```scripts```, ```fonts``` and ```style```. The folder can be found in the repository.

## Use as a command line tool

### Logging
There are 2 ways of logging: _-cl_ (console log) and _-fl_ (file log). Console logging shows a short message in case of an error while the log file also contains the stack trace. The default location for the log file is ```c:\temp\```.

There are four possible ways to set the logging:

|flag(s)|logging        |
|---    |---            |
|_none_ |console logging|
|-cl    |console logging|
|-fl    |file logging   |
|-cl -fl|console and file logging|

Adjusting ```LOG_FILE=...``` in _converter.properties_ is the only way to change the directory where the log files are created.

### Command

```
$ java -jar OpenWebslidesConverter.jar -i file.pptx [-o outputDir] [-t shower] [-fl] [-cl]
```

Examples:

```
$ java -jar OpenWebslidesConverter.jar -i file.pptx -t shower -fl
```

```
$ java -jar OpenWebslidesConverter.jar -i file.pptx -o "converted files"
```

```
$ java -jar OpenWebslidesConverter.jar -i file.pptx -o out -fl -cl
```

## Use inside/with another java application
The starting point of the tool is the _OpenWebslidesConverter_ class. It has two entry points:
* ```+ static main(args : String[]) : void```
* ```+ static queueEntry(args : String[], queue : Queue<String>, id : long) : void```

_queueEntry_ is a method to get control over the logging inside another project. An empty Queue _(java.util.Queue)_ will be filled in with logging lines. id is used as a unique identification in the logs in case multiple converters are running parallel. The String array args represent in both cases the command line arguments.

### As library
Just include the .jar file as a dependency or library inside your project. Use one of the above static methods.

### Using reflection
Another way is to load the converter dynamically at runtime using reflection. You can use following code as inspiration.

```java
try {
    Queue<String> queue = new ConcurrentLinkedDeque<>();

    // Getting the jar URL which contains target class
    URL[] classLoaderUrls = ...

    // Create a new URLClassLoader
    URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);

    // Load the target class
    Class<?> OpenWebslidesConverter = urlClassLoader.loadClass("openwebslidesconverter.OpenWebslidesConverter");

    // Getting a method from the loaded class and invoke it
    Method method = OpenWebslidesConverter.getMethod("queueEntry", String[].class, Queue.class, long.class);
            
    final Object[] param = new Object[3];
    param[0] = args;
    param[1] = queue;
    param[2] = id;
 
    method.invoke(null, param);

    ... //read the queue or do other work
} catch (...) {
    ...
}
```
