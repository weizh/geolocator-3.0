package edu.cmu.geolocator.coder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import edu.cmu.geolocator.model.CandidateAndFeature;
import edu.cmu.geolocator.model.Tweet;

public class EnglishAggGeoCoder implements GeoCoder {

  static EnglishAggGeoCoder enaggcoder;

  List<CandidateAndFeature> mlresult, amltyresult, maxpopresult, result;

  HashMap<CandidateAndFeature, Double> agg;

  @Override
  public List<CandidateAndFeature> resolve(Tweet example, String mode) throws Exception {
    agg = new HashMap<CandidateAndFeature, Double>();
    mlresult = CoderFactory.getMLGeoCoder().resolve(example, mode);
    amltyresult = CoderFactory.getAmanalityGeoCoder().resolve(example, mode);
    maxpopresult = CoderFactory.getMaxPopGeoCoder().resolve(example, mode);

    for (CandidateAndFeature ml : mlresult) {
      agg.put(ml, ml.getProb());
    }
    for (CandidateAndFeature am : amltyresult) {
      if (agg.containsKey(am))
        agg.put(am, agg.get(am) + am.getProb() * 0.1);
      else
        agg.put(am, am.getProb());
    }
    for (CandidateAndFeature mx : maxpopresult) {
      if (agg.containsKey(mx))
        agg.put(mx, agg.get(mx) + mx.getProb() * 0.1);
      else
        agg.put(mx, mx.getProb());
    }

    result = new ArrayList<CandidateAndFeature>();
    for (Entry<CandidateAndFeature, Double> ag : agg.entrySet()) {
      ag.getKey().setProb(ag.getValue()>0.99?1:ag.getValue());
      result.add(ag.getKey());
    }
    if (result.size() == 0)
      return null;
    else
      return result;
  }

  public static EnglishAggGeoCoder getInstance() {
    // TODO Auto-generated method stub
    if (enaggcoder == null)
      enaggcoder = new EnglishAggGeoCoder();
    return enaggcoder;
  }

}
