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

package edu.cmu.geolocator.nlp;

import edu.cmu.geolocator.nlp.lemma.AnnaLemmatizer;
import edu.cmu.geolocator.nlp.lemma.UWMorphaStemmer;
import edu.cmu.geolocator.nlp.pos.ENTweetPOSTagger;
import edu.cmu.geolocator.nlp.pos.ESAnnaPOSTagger;

public class NLPFactory {
  /**
   * @return the enPosTagger
   */
  public static POSTagger getEnPosTagger() {
    return ENTweetPOSTagger.getInstance();
  }

  public static Lemmatizer getEnUWStemmer() {
    return UWMorphaStemmer.getInstance();
  }

  public static POSTagger getEsPosTagger() {
    return ESAnnaPOSTagger.getInstance();
  }

  public static Lemmatizer getEsAnnaLemmatizer() {
    try {
      return AnnaLemmatizer.getInstance("es");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  public static Lemmatizer getEnAnnaLemmatizer() {
    try {
      return AnnaLemmatizer.getInstance("en");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
}
