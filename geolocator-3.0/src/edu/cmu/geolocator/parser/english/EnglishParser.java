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

package edu.cmu.geolocator.parser.english;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.cmu.geolocator.model.LocEntityAnnotation;
import edu.cmu.geolocator.model.Tweet;
import edu.cmu.geolocator.nlp.ner.FeatureExtractor.FeatureGenerator;
import edu.cmu.geolocator.parser.NERTagger;
import edu.cmu.geolocator.parser.ParserFactory;
import edu.cmu.geolocator.parser.STBDParser;
import edu.cmu.geolocator.parser.TPParser;
import edu.cmu.geolocator.parser.utils.ParserUtils;
import edu.cmu.geolocator.resource.gazindexing.Index;

/**
 * The aggregation of all the parsers for English.
 * @author indri
 *
 */
public class EnglishParser {

  private NERTagger ner,finener;
  private STBDParser stbd;
  private TPParser tp;
	
	ArrayList<LocEntityAnnotation> match;
	public EnglishParser(boolean misspell){
		ner = ParserFactory.getEnNERParser();
		finener = ParserFactory.getFineEnNERParser();
		stbd = ParserFactory.getEnSTBDParser();
		tp = ParserFactory.getEnToponymParser();
	}
	public List<LocEntityAnnotation> parse(Tweet t){
		match = new ArrayList<LocEntityAnnotation>();
		List<LocEntityAnnotation> nerresult = ner.parse(t);
		List<LocEntityAnnotation> stbdresult = stbd.parse(t);
		List<LocEntityAnnotation> toporesult = tp.parse(t);
		List<LocEntityAnnotation> finenerresult = finener.parse(t);
		match.addAll(nerresult);
		match.addAll(stbdresult);
		match.addAll(toporesult);
		match.addAll(finenerresult);
		return ParserUtils.ResultReduce(match,true);	
		
	}
}
