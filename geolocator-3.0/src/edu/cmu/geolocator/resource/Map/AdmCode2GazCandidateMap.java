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

package edu.cmu.geolocator.resource.Map;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import edu.cmu.geolocator.GlobalParam;
import edu.cmu.geolocator.io.GetReader;
import edu.cmu.geolocator.model.CandidateAndFeature;
import edu.cmu.geolocator.model.Country;

public class AdmCode2GazCandidateMap extends Map {

  public static final String name1 = GlobalParam.geoNames+"/admin1CodesASCII.txt";

  public static final String name2 = GlobalParam.geoNames+"/admin2Codes.txt";

  HashMap<String, CandidateAndFeature> map1;

  HashMap<String, CandidateAndFeature> map2;

  static AdmCode2GazCandidateMap a2amap;

  public CandidateAndFeature getValue(String code) {
    code = code.toLowerCase();
    if (map1.containsKey(code))
      return map1.get(code);
    if (map2.containsKey(code))
      return map2.get(code);
    return null;
  }

  public boolean isAdm1(String code) {
    return map1.containsKey(code.toLowerCase());
  }

  public boolean isAdm2(String code) {
    return map2.containsKey(code.toLowerCase());
  }

  @SuppressWarnings("unchecked")
  public static AdmCode2GazCandidateMap getInstance() {
    if (a2amap == null) {
      return new AdmCode2GazCandidateMap().load();
    }
    return a2amap;
  }

  public AdmCode2GazCandidateMap load() {
    map1 = new HashMap<String, CandidateAndFeature>(3900);
    map2 = new HashMap<String, CandidateAndFeature>(35211);
    BufferedReader br = null;
    try {
      br = GetReader.getUTF8FileReader(name1);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String line = null;
    CandidateAndFeature c;
    try {
      while ((line = br.readLine()) != null) {
        String[] toks = line.split("\t");
        String[] codes = toks[0].split("\\.");
        c = new CandidateAndFeature().setCountryCode(codes[0].toLowerCase())
                .setAdm1Code(codes[1].toLowerCase()).setAdm1(toks[1].toLowerCase()).setId(toks[3]).setAsciiName(toks[1].toLowerCase());
        map1.put(toks[0].toLowerCase(), c);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      br.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      br = GetReader.getUTF8FileReader(name2);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      while ((line = br.readLine()) != null) {
        String[] toks = line.split("\t");
        String[] codes = toks[0].split("\\.");
        c = new CandidateAndFeature().setCountryCode(codes[0].toLowerCase())
                .setAdm1Code(codes[1].toLowerCase()).setAdm2Code(codes[2].toLowerCase())
                .setAdm2(toks[1].toLowerCase()).setId(toks[3]).setAsciiName(toks[1].toLowerCase());
        map2.put(toks[0].toLowerCase(), c);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      br.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return this;
  }

  public static void main(String argv[]) throws InterruptedException {
    AdmCode2GazCandidateMap amap = AdmCode2GazCandidateMap.getInstance();
    System.out.println(amap.isAdm1("us.ma"));
  }
}
