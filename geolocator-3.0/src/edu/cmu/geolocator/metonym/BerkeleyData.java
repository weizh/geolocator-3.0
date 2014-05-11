package edu.cmu.geolocator.metonym;

import java.util.List;


import edu.berkeley.nlp.PCFGLA.CoarseToFineMaxRuleParser;
import edu.berkeley.nlp.syntax.Tree;


public class BerkeleyData {
	public List<Tree<String>> parseTrees;
	public CoarseToFineMaxRuleParser parser;
	public String line;
	public String sentenceID;
	public BerkeleyData(List<Tree<String>> parseTrees, CoarseToFineMaxRuleParser parser,
			String line, String sentenceID){
		this.parseTrees = parseTrees;
		this.parser = parser;
		this.line = line;
		this.sentenceID = sentenceID;
	}
}
