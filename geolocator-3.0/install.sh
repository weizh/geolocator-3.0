wget -P lib/ https://mate-tools.googlecode.com/files/anna-3.3.jar
wget -P lib/ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.2.0/jackson-databind-2.2.0.jar
wget -P lib/ http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.2.0/jackson-core-2.2.0.jar
wget -P lib/ http://repo1.maven.org/maven2/edu/washington/cs/knowitall/morpha-stemmer/1.0.5/morpha-stemmer-1.0.5.jar

wget -P lib/ http://repo1.maven.org/maven2/edu/stanford/nlp/stanford-corenlp/1.3.5/stanford-corenlp-1.3.5.jar
wget -P lib/ http://repo1.maven.org/maven2/edu/stanford/nlp/stanford-corenlp/1.3.5/stanford-corenlp-1.3.5-models.jar

wget -P lib/ http://repo1.maven.org/maven2/joda-time/joda-time/2.1/joda-time-2.1.jar
wget -P lib/ http://repo1.maven.org/maven2/de/jollyday/jollyday/0.4.6/jollyday-0.4.6.jar

wget -P lib/ http://repo1.maven.org/maven2/org/twitter4j/twitter4j-core/3.0.3/twitter4j-core-3.0.3.jar
wget -P lib/ http://sourceforge.net/projects/minorthird/files/MinorThird%20Jar/minorthird-jar_20080611/minorthird_20080611.jar/download
wget -P lib/ http://repo1.maven.org/maven2/edu/berkeley/nlp/berkeleyparser/r32/berkeleyparser-r32.jar
wget -P lib/ http://repo1.maven.org/maven2/commons-codec/commons-codec/1.8/commons-codec-1.8.jar
wget -P lib/ http://repo1.maven.org/maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.jar
wget -P lib/ http://repo1.maven.org/maven2/org/apache/opennlp/opennlp-tools/1.5.3/opennlp-tools-1.5.3.jar
wget -P lib/ http://repo1.maven.org/maven2/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar
wget -P lib/ http://repo1.maven.org/maven2/com/cybozu/labs/langdetect/1.1-20120112/langdetect-1.1-20120112.jar
wget -P lib/ http://repo1.maven.org/maven2/org/apache/lucene/lucene-core/4.5.1/lucene-core-4.5.1.jar
wget -P lib/ http://repo1.maven.org/maven2/org/apache/lucene/lucene-analyzers-common/4.5.1/lucene-analyzers-common-4.5.1.jar
wget -P lib/ http://repo1.maven.org/maven2/org/apache/lucene/lucene-queries/4.5.1/lucene-queries-4.5.1.jar

wget -P lib/ http://lyle.smu.edu/~tspell/jaws/jaws-bin.jar

wget -P wordnet/ http://www.cs.cmu.edu/~weizhan1/wordnet/wordnet.tar.gz
tar -zxvf wordnet/wordnet.tar.gz

wget -P res/ http://www.cs.cmu.edu/~weizhan1/res/res.tar.gz
tar -zxvf res/res.tar.gz

wget -P GeoNames/ http://download.geonames.org/export/dump/allCountries.zip
wget -P GeoNames/ http://download.geonames.org/export/dump/alternateNames.zip
wget -P GeoNames/ http://download.geonames.org/export/dump/admin1CodesASCII.txt
wget -P Geonames/ http://download.geonames.org/export/dump/admin2Codes.txt
wget -P GeoNames/ http://download.geonames.org/export/dump/iso-languagecodes.txt
wget -P GeoNames/ http://download.geonames.org/export/dump/timeZones.txt

wget -P GeoNames/ http://www.cs.cmu.edu/~weizhan1/geonames/admin1CodesASCII.txt
wget -P GeoNames/ http://www.cs.cmu.edu/~weizhan1/geonames/admin2Codes.txt
wget -P GeoNames/ http://www.cs.cmu.edu/~weizhan1/geonames/iso-languagecodes.txt
wget -P GeoNames/ http://www.cs.cmu.edu/~weizhan1/geonames/timeZones.txt

unzip GeoNames/allCountries.zip 
unzip GeoNames/alternateNames.zip 
mv allCountries.txt GeoNames/
mv alternateNames.txt GeoNames/

rm GeoNames/allCountries.zip
rm GeoNames/alternateNames.zip

wget -P http://www.cs.cmu.edu/~weizhan1/indexer/indexer.jar

java -jar -Xmx2000m indexer.jar GeoNames GazIndex