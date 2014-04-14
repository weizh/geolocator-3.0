package edu.cmu.geolocator.coder;

import edu.cmu.geolocator.parser.Universal.MTNERParser;

public class CoderFactory {
  
  public static MLGeoCoder getMLGeoCoder() {
    return MLGeoCoder.getInstance();
}
  
  public static RuleGeoCoder getRuleGeoCoder(){
    return RuleGeoCoder.getInstance();
  }
}
