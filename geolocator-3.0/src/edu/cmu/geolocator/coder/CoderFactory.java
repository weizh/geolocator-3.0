package edu.cmu.geolocator.coder;

import edu.cmu.geolocator.parser.Universal.MTNERParser;

public class CoderFactory {
  
  public static MLGeoCoder getMLGeoCoder() {
    return MLGeoCoder.getInstance();
}
  
  public static MaxPopGeoCoder getMaxPopGeoCoder(){
    return MaxPopGeoCoder.getInstance();
  }
  
  public static AmanalityGeoCoder getAmanalityGeoCoder(){
    return AmanalityGeoCoder.getInstance();
  }
  
  public static EnglishAggGeoCoder getENAggGeoCoder(){
    return EnglishAggGeoCoder.getInstance();
  }
  
}
