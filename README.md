# LogScrubber 1.0

#### Tool created for scrubbing perf4j logs

At the moment, this tool requires files that exclusively contain perf4j logs. So make sure you only append
log4j.logger.org.perf4j.TimingLogger logs to the file you want to scrub.

#### Preparation

Create a override.properties file and place it in .logscrubber/ in your home folder.

##### In this file you can override:
scrub (Wether or not the tool should look for new logs.  boolean)  default: scrub=true
timeslice (The size of the timeslices in minutes stored by the scrubber.  int)  default: timeslice=5
deleteOnStartup (Wether or not the tool should clear the existing logs folder.  boolean)  default: deleteOnStartup=true

##### Also in this file you must declare the hosts and paths to the logs you want to scrub:
log.locations[x]=hostName/path/to/perf4j.log

#### How it works

For every given location a thread will be started, which will read the file line per line starting from the last line.
The logs will be sliced according to the expected timeslice property and store them in the user home folder.

To view the logs navigate to localhost:8090
