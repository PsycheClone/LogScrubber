# LogScrubber

#### Tool created for scrubbing perf4j logs

At the moment, this tool requires files that exclusively contain perf4j logs. So make sure you only append
log4j.logger.org.perf4j.TimingLogger logs to the file you want to scrub.

# 1.1
D

#### Developers

Checkout project.
Install bower.
Navigate to src/main/resources/static and execute "bower install"
Make sure you create an override.properties file and place it in .logscrubber/ in your home folder.
At the root of the project: mvn package && java -jar target/LogScrubber-1.1-SNAPSHOT.jar

# 1.0

#### Preparation

Download the LogScrubber jar located in the /target folder.

Create a override.properties file and place it in .logscrubber/ in your home folder.

##### In this file you can override:

scrub (Wether or not the tool should look for new logs.  boolean)  default: scrub=true 

timeslice (The size of the timeslices in minutes stored by the scrubber.  int)  default: timeslice=5 

deleteOnStartup (Wether or not the tool should clear the existing logs folder.  boolean)  default: deleteOnStartup=true

##### Also in this file you must declare the hosts and paths to the logs you want to scrub:

log.locations[x]=hostName/path/to/perf4j.log

##### And provide your username and password:

melexis.username=xxx
melexis.password=xxx

#### How it works

For every given location a thread will be started, which will read the file line per line starting from the last line.
The logs will be sliced according to the expected timeslice property and store them in the user home folder.

To view the logs navigate to localhost:8090

Choose a host.
Choose a start and select the range you want to view.
