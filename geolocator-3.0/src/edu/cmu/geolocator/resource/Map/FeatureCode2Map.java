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
import edu.cmu.geolocator.resource.ResourceFactory;

public class FeatureCode2Map {
  HashMap<String,Integer> codemap;
  public FeatureCode2Map() {
    // TODO Auto-generated constructor stub
    codemap = new HashMap<String,Integer>();
  }

  public static void main(String args[]) {
    FeatureCode2Map fm = ResourceFactory.getFeatureCode2Map().getInstance();
    int i = fm.getIndex("ppla");
    int size = fm.size();
    System.out.println(i);
  }

  public int size() {
    // TODO Auto-generated method stub
    return codemap.size();
  }

  /**
   * The string passed into this function should be the feature name, without the single char class.
   * indexed ppl instead of P.PPL.
   * 
   * @param string
   * @return
   */
  public int getIndex(String string) {
    // TODO Auto-generated method stub
    string = string.toLowerCase();
    return codemap.get(string);
  }

  private FeatureCode2Map load(String filename) {
    BufferedReader br = null;
    try {
      br = GetReader.getUTF8FileReader(filename);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String line = null;
    try {
      int count = 0;
      while ((line = br.readLine()) != null) {
        //System.out.println(line.split("\t")[0]);
        String code = line.split("\t")[0].split("\\.")[1].toLowerCase();
        codemap.put(code,count++);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return this;
  }
  private FeatureCode2Map loadSet(String filename) {
    BufferedReader br = null;
    try {
      br = GetReader.getUTF8FileReader(filename);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String line = null;
    try {
      int count = 0;
      while ((line = br.readLine()) != null) {
        String code = line.trim().toLowerCase();
        codemap.put(code,count++);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return this;
  }

  static FeatureCode2Map featurecode2map;

  public static FeatureCode2Map getInstance() {

    // TODO Auto-generated method stub
    if (featurecode2map == null)
//      featurecode2map = new FeatureCode2Map().load("GeoNames/featureCodes_en.txt");
      featurecode2map = new FeatureCode2Map().load("res/geonames/featureCodes_en.txt");
      return featurecode2map;
  }
}
