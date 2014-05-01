/**
 * 
 * Copyright (c) 2012 - 2014 Carnegie Mellon University
 * 
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
 * 
 * @author Wei Zhang,  Language Technology Institute, School of Computer Science, Carnegie-Mellon University.
 * email: wei.zhang@cs.cmu.edu
 *
 * 
 */
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
  public List<CandidateAndFeature> resolve(Tweet example, String mode, String filter) throws Exception {
    agg = new HashMap<CandidateAndFeature, Double>();
    mlresult = CoderFactory.getMLGeoCoder().resolve(example, mode,filter);
    amltyresult = CoderFactory.getAmanalityGeoCoder().resolve(example, mode,filter);
    maxpopresult = CoderFactory.getMaxPopGeoCoder().resolve(example, mode,filter);
    if (mlresult!=null)
    for (CandidateAndFeature ml : mlresult) {
      agg.put(ml, ml.getProb());
    }
    if (amltyresult!=null)
    for (CandidateAndFeature am : amltyresult) {
      if (agg.containsKey(am))
        agg.put(am, agg.get(am) + am.getProb() * 0.1);
      else
        agg.put(am, am.getProb());
    }
    if (maxpopresult!=null)
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
