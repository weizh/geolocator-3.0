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

import edu.cmu.geolocator.nlp.ner.FeatureExtractor.FeatureGenerator;
import edu.cmu.geolocator.nlp.ner.FeatureExtractor.FineFeatureGenerator;
import edu.cmu.geolocator.resource.ResourceFactory;

public class NERFeatureFactory {

  private static FeatureGenerator enFeatureGenerator;
  private static FineFeatureGenerator fineenFeatureGenerator;

  private static FeatureGenerator esFeatureGenerator;

  public static FeatureGenerator getInstance(String langCode) throws Exception{
    if (langCode.equalsIgnoreCase("en")){
      if (enFeatureGenerator==null)
        enFeatureGenerator =  new FeatureGenerator("en", ResourceFactory.getClbIndex(), "res/");
      return enFeatureGenerator;
    }
    if (langCode.equalsIgnoreCase("fine-en")){
      if (fineenFeatureGenerator==null)
        fineenFeatureGenerator =  new FineFeatureGenerator();
      return fineenFeatureGenerator;
    }
    if(langCode.equalsIgnoreCase("es")){
      if(esFeatureGenerator==null)
        esFeatureGenerator= new FeatureGenerator("es", ResourceFactory.getClbIndex(), "res/");
      return esFeatureGenerator;
    }
    
    throw new Exception("Language not compatible with NER FeatureGenerator.");
  }
}
