package edu.cmu.geolocator.coder;

/**
 * Pick the one with the largest population for the geocoding.
 * 
 * @author indri
 * 
 */
public class RuleGeoCoder {

  private static RuleGeoCoder rgcoder;

  public static RuleGeoCoder getInstance() {
    if (rgcoder == null) {
      rgcoder = new RuleGeoCoder();
    }
    return rgcoder;
  }
}
