geolocator v3.0
=========

This is 3.0 version of geo-locator, which includes the geoparser, a location entity recognizer, and geocoder, that tries to attach the correct geo-location to the entities.


Install:

run in commandline install.sh, if you have a windows, use cygwin, or if you have linux-like system, go to terminal.

Then, all the resources will be downloaded, the installation will take roughly 1 hour because of the creation of the gazetteer.


How to Run:

See src/edu/cmu/test/PipelineTest.java for the sample code. The code pipeline shows how to readin Tweet JSON file (one line per tweet), perform geoparsing,
and then geocoding. Please set the memory to -Xmx2000m.

There is something to do before runnning the program.

Note that the default place for Geonames and GazIndex folder will be in the root. However, if you look into install.sh, you can also change the place where it's installed or put by changing the last line running indexer.jar, or moving the index or Geonames to other folders. However, if you move it, you have to do something to tell the program where they are put, by using 

    GlobalParam.setGazIndex("/Your path here/GazIndex");
    GlobalParam.setGeoNames("/Your path here/GeoNames");

In PipelineTest.java, I have shown how to use this in front of all the codes that you run.

However, if you install the project without changing the install.sh or moved the folder, you don't need to do anything about setting those paths.


If you want to use a GUI, there is one in edu/cmu/test/gui/NewDesktop.java. Run this code with Option -Xmx2000m as usual.

So please use the twitter JSON file instead of sentence only to make the most of the geolocator. However, the sentence only will also work because we added comma group geocoder and max population geocoder.

If you have any question, please email wei.zhang@cs.cmu.edu or gelern@cs.cmu.edu.

Thanks.


