package edu.cmu.geolocator.coder;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.geolocator.model.CandidateAndFeature;
import edu.cmu.geolocator.model.LocGroupFeatures;
import edu.cmu.geolocator.model.Tweet;

/**
 * Pick the one with the largest population for the geocoding.
 * 
 * @author indri
 * 
 */
public class MaxPopGeoCoder {

  private static MaxPopGeoCoder rgcoder;

  public static MaxPopGeoCoder getInstance() {
    if (rgcoder == null) {
      rgcoder = new MaxPopGeoCoder();
    }
    return rgcoder;
  }

  public List<CandidateAndFeature> resolve(Tweet example, String mode,String filter) throws Exception {

    ArrayList<CandidateAndFeature> decoded = new ArrayList<CandidateAndFeature>();

    // copy 2d feature array into feature lists
    LocGroupFeatures feature = new LocGroupFeatures(example, mode,filter).toFeatures();
    ArrayList<ArrayList<CandidateAndFeature>> farrays = feature.getFeatureArrays();

    ArrayList<CandidateAndFeature> maxPopCandidates = getMaxPopulation(farrays);

    int i = 0;
    for (CandidateAndFeature d : maxPopCandidates) {
      // System.out.println("maxPosCandidate " + (i++) + " is " + d.getOriginName());
      if (d.getPopulation() > 999)
        decoded.add(d);
    }

    if (decoded.size() == 0)
      return null;
    else {
      // added the probability output for each result.
      for(CandidateAndFeature de:decoded)
        de.setProb(0.75);
      
      return decoded;
    }
  }

  private ArrayList<CandidateAndFeature> getMaxPopulation(
          ArrayList<ArrayList<CandidateAndFeature>> fa) {
    ArrayList<CandidateAndFeature> maxPop = new ArrayList<CandidateAndFeature>();
    if (fa == null)
      return null;
    for (ArrayList<CandidateAndFeature> word : fa)
      for (CandidateAndFeature cand : word)
        if (cand.getF_PopRank() < 1 && cand.getF_PopRank() > -0.5)
          maxPop.add(cand);
    return maxPop;
  }

}
