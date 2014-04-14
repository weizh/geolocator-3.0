package edu.cmu.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import edu.cmu.geolocator.GlobalParam;
import edu.cmu.geolocator.coder.CoderFactory;
import edu.cmu.geolocator.model.CandidateAndFeature;
import edu.cmu.geolocator.model.LocEntityAnnotation;
import edu.cmu.geolocator.model.Tweet;
import edu.cmu.geolocator.parser.ParserFactory;

public class CmdLineTest {

  public static void main(String argv[]) throws Exception {
    GlobalParam.setGazIndex("/users/indri/GazIndex");
    InputStreamReader isr = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(isr);
    String s = null;
    while ((s = br.readLine()) != null) {
      Tweet tweet = new Tweet(s);
      List<LocEntityAnnotation> topos = ParserFactory.getEnAggrParser().parse(tweet);               
      List<CandidateAndFeature> resolved = CoderFactory.getMLGeoCoder().resolve(tweet, "debug");
      for (LocEntityAnnotation topo : topos) {
        System.out.println(topo.getTokenString() + " " + topo.getNEType());
      }
      for (CandidateAndFeature code : resolved) {
        System.out.println(code.getAsciiName() + "" + code.getCountry() + "" + code.getLatitude()
                + "" + code.getLongitude());
      }
    }
  }

}
