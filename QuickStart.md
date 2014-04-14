geolocator v3.0
=========

This is 3.0 version of geo-locator, which includes the geoparser, a location entity recognizer, and geocoder, that tries to attach the correct geo-location to the entities.


Install:

run in commandline install.sh, if you have a windows, use cygwin, or if you have linux-like system, go to terminal.

Then, all the resources will be downloaded, the installation will take roughly 1 hour because of the creation of the gazetteer.


How to Run:

See src/edu/cmu/test/PipelineTest.java for the sample code. The code pipeline shows how to readin Tweet JSON file (one line per tweet), perform geoparsing,
and then geocoding. 

Commandline test is also available in CmdLineTest.java. It takes a tweet-like sentence as input, and parse the results. However, it's not possible to use the user location info to disambiguate the locations though.

So please use the twitter JSON file instead of sentence only to make the most of the geolocator.

If you have any question, please email wei.zhang@cs.cmu.edu or gelern@cs.cmu.edu.

Thanks.


