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

import edu.cmu.geolocator.parser.Universal.MTNERParser;

public class CoderFactory {
  
  public static MLGeoCoder getMLGeoCoder() {
    return MLGeoCoder.getInstance();
}
  
  public static MaxPopGeoCoder getMaxPopGeoCoder(){
    return MaxPopGeoCoder.getInstance();
  }
  
  public static MinimalityGeoCoder getAmanalityGeoCoder(){
    return MinimalityGeoCoder.getInstance();
  }
  
  public static EnglishAggGeoCoder getENAggGeoCoder(){
    return EnglishAggGeoCoder.getInstance();
  }
  
}
