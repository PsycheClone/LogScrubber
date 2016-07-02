# LogScrubber

#### Tool created for scrubbing perf4j logs

At the moment, this tool requires files that exclusively contain perf4j logs.

Use this next config snippet in your application.  What is important here is that ONLY perf4j logging is appended to a single file.  And the Conversion pattern, because these lines will be parsed and will expect this particular pattern.

  ```
  # perf4j appender
  log4j.appender.perf=org.apache.log4j.FileAppender
  log4j.appender.perf.File=${karaf.data}/log/perf4j.log
  log4j.appender.perf.layout=org.apache.log4j.PatternLayout
  log4j.appender.perf.layout.ConversionPattern=%d | %-5.5p | %-16.16t | %-32.32c{1} | %X{bundle.id} - %X{bundle.name} - %X{bundle.version} | %m%n
  log4j.appender.perf.append=true
  log4j.additivity.org.perf4j = false
  log4j.logger.org.perf4j = INFO, perf
  ```

#### Developers

Checkout project.

Install bower.

Navigate to src/main/resources/static and execute "bower install"

Make sure you create an override.properties file and place it in .logscrubber/ in your home folder.

At the root of the project: mvn package && java -jar target/LogScrubber-1.1-SNAPSHOT.jar

#### Preparation

Create an override.properties file and place it in .logscrubber/ in your home folder.

##### In this file you can override:

scrub (Wether or not the tool should look for new logs.  boolean)  default: scrub=true 

timeslice (The size of the timeslices in minutes stored by the scrubber.  int)  default: timeslice=5 

deleteOnStartup (Wether or not the tool should clear the existing logs folder.  boolean)  default: deleteOnStartup=true

##### Also in this file you must declare the hosts and paths to the logs you want to scrub:

log.locations[x]=AppName:/path/to/perf4j.log

#### How it works

For every given location two things will happen.

Firstly, a FileTailer will open the logfile and start tailing it, saving slices every interval of the desired "timeslice" parameter and store them in the user home /logs folder.

Secondly, a thread will be started, which will read the file line per line backwards, starting from the last line.

The logs will be sliced according to the expected timeslice property and store them in the user home /logs folder.

#### Using the UI

To view the logs navigate to localhost:8090

Choose an app.

Choose a range and how big the timeslices should be.

And click Get.

Enjoy.
