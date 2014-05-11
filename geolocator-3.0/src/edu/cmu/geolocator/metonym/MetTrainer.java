package edu.cmu.geolocator.metonym;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import edu.berkeley.nlp.PCFGLA.BerkeleyParser;
import edu.berkeley.nlp.PCFGLA.SkimBerkeleyParser;
import edu.berkeley.nlp.syntax.Tree;
import edu.cmu.geolocator.io.GetWriter;
import edu.cmu.geolocator.nlp.lemma.AnnaLemmatizer;

/**
 * This class is used to create a features file to be used with libSVM.
 * To create a new model file, it requires both plaintext and tagged versions of the data.
 * To score new data, it requires only the plaintext version.
 * 
 * @author Nate Lyons
 */

public class MetTrainer {
	
	public static int index = 0;
	
	/**
	 * @param parsedTree From Berkeley for the sentence being considered.
	 * @param topo The current toponym for which to search and gather features.
	 * @return MetFeatures object which contains all features for the
	 * toponym in the tree.
	 */
	public static MetFeatures readTree(Tree<String> parsedTree, String topo){
		System.out.println(parsedTree.getLabel() + " ");
		if(parsedTree.isLeaf()){
			return null;
		}
		//System.out.println(parsedTree.toEscapedString());
		if(parsedTree.getChildren().get(0).getLabel().contains(topo)){
			System.out.println("\nFound toponym: " + topo);
			return new MetFeatures(topo);
		}
		MetFeatures temp = null;
		int possessive = 0, plural = 0, determiner = 0,
				noun_phrase = 0, verb_phrase = 0, subject = 0;
		String verb = "";
		for(Tree<String> child : parsedTree.getChildren()){
			//System.out.println(child.toEscapedString());
			if(temp == null){
				temp = readTree(child, topo);
			}
			//System.out.println(child.getLabel());
			if(child.getLabel().equals("DT")){
				determiner = 1;
			}else if(child.getLabel().length() > 3 && child.getLabel().endsWith("S")){
				plural = 1;
			}else if(child.getLabel().equals("POS")){
				possessive = 1;
			}else if(child.getLabel().equals("VP") && temp != null && temp.full == 1){
				verb_phrase = 1;
				verb = findVerb(child);
			}else if(child.getLabel().equals("NP")){
				noun_phrase = 1;
			}else if(child.getLabel().equals("S")){
				subject = 1;
			}
		}
		if(temp != null){
			if(temp.full == MetFeatures.FEAT_DONE){
				return temp;
			}else if(temp.full == 2){ //overwrite level 1's, so long as there is a level 2.
				temp.subject = subject;
			}else if(temp.full == 1){
				temp.noun_phrase = noun_phrase;
				temp.verb_phrase = verb_phrase;
				temp.verb = verb;
			}else if(temp.full == 0){
				temp.determiner = determiner;
				temp.plural = plural;
				temp.possessive = possessive;
			}
			temp.full++;
		}
		return temp;
	}
	
	/**
	 * findVerb is a helper function used in readTree to find verbs associated with the
	 * toponym at the root level of the passed tree.
	 * @param tree Currently considered subtree, root contains a toponym.
	 * @return The verb associated with the current toponym.
	 */
	private static String findVerb(Tree<String> tree) {
		if(tree.isLeaf()){
			return "";
		}
		if(tree.isPreTerminal() && tree.getLabel().startsWith("VB")){
			System.out.println("\nFound verb: " + tree.getChild(0).getLabel());
			return tree.getChild(0).getLabel();
		}
		String str = "";
		for(Tree<String> child : tree.getChildren()){
			if(str.length() == 0)
				str += findVerb(child);
			
		}
		return str;
	}

	/**
	 * getFeatures is used to search all possible BerkeleyData trees for toponyms found in
	 * that sentence.  It then returns a map containing a populated MetFeatures object for
	 * each candidate toponym.
	 * @param data BerkeleyData taken from BerkeleyParser
	 * @param match List of toponyms in the candidate sentence
	 * @return HashMap of MetFeatures for each toponym
	 */
	public static HashMap<String, MetFeatures> getFeatures(BerkeleyData data, HashSet<String> match) {
		HashMap<String, MetFeatures> result = new HashMap<String, MetFeatures>();
		double cur_max = Double.NEGATIVE_INFINITY;
		for(String topo : match){
			if(data != null && data.parseTrees != null)
				//System.out.println(index + " - " + data.parseTrees.size());
			for (Tree<String> parsedTree : data.parseTrees) {
				if(parsedTree == null || data == null || data.parser == null)
					continue;
				try{
					if (data.parser.getLogLikelihood(parsedTree) == Double.NEGATIVE_INFINITY)
						continue;
					if (data.parser.getLogLikelihood(parsedTree) > cur_max && !parsedTree.getChildren().isEmpty()) {
						index++;
						MetFeatures temp = readTree(parsedTree, topo);
						result.put("[" + index + "]" + topo, temp);
						System.out.println("[" + index + "]" + topo);
					}
				}catch(NullPointerException ex){
					System.err.println("Skipping null pointer exception parseTree");
				}
			}
		}
		return result;
	}
	
	/**
	 * Helper function, used to find longest consecutive substring between the two strings.
	 * @param str1
	 * @param str2
	 * @return length of the longest consecutive substring
	 */
	public static int LCS(String str1, String str2){
		if (str1.isEmpty() || str2.isEmpty())
			return 0;
		
		int[][] num = new int[str1.length()][str2.length()];
		int maxlen = 0;

		for (int i = 0; i < str1.length(); i++){
			for (int j = 0; j < str2.length(); j++){
				if (str1.charAt(i) != str2.charAt(j))
					num[i][j] = 0;
				else{
					if ((i == 0) || (j == 0))
						num[i][j] = 1;
					else
						num[i][j] = 1 + num[i - 1][j - 1];

					if (num[i][j] > maxlen){
						maxlen = num[i][j];
					}
				}
			}
		}
		return maxlen;
	}

	public static void main(String argv[]) throws IOException {
		// inFile is plaintext, goldFile is annotated
    String trainFile = "C:/Users/Nate/LTI/workspace/semEval.data/countries.train.A";
		String testFile = "C:/Users/Nate/LTI/workspace/semEval.data/countries.train.fixed.A.txt";
		
		// Create a verb classifier to identify verb lemmas as metonymic or literal
		VerbClassifier vc = new VerbClassifier("metverblemmas", "litverblemmas");

		// initialize a berkeleyParser
    String[] args = {"-gr", "res/eng_sm6.gr", "-tokenize"};
		SkimBerkeleyParser.initialize(args);
		
		// Create a human-readable output file
		Charset charset = Charset.forName("UTF-8");
		BufferedWriter output = GetWriter.getFileWriter("output.csv");

		output.write("Sentence,Toponym,Gold-standard,Posessive,Plural,Determiner,Noun phrase,Verb phrase,Subject,Verb category");
		output.newLine();
		
		// Create a features file to be used with libSVM
		BufferedWriter featOut = GetWriter.getFileWriter("features.out");

		System.out.println("Welcome to MetTrainer! Please wait; loading resources...");
		AnnaLemmatizer enLemma = new AnnaLemmatizer("res/en/CoNLL2009-ST-English-ALL.anna-3.3.lemmatizer.model");

		String text = "";
		
		// BerkeleyData counter
		int bdIdx = 0;

		// Search the data for toponyms and create features for each
		BufferedReader br = new BufferedReader(new FileReader(testFile));
		BufferedReader goldbr = new BufferedReader(new FileReader(trainFile));
		String goldtext = "";
		text = "";
		while((goldtext = goldbr.readLine()) != null){
			int words = 0;
			boolean count = true;
			if(goldtext.length() > 0)
				words = 1;
			// Count length of goldtext excluding tags
			for(int i = 0; i < goldtext.length(); i++){
				if(goldtext.charAt(i) == '<'){
					count = false;
				}else if(goldtext.charAt(i) == '>'){
					count = true;
				}
				if(count && goldtext.charAt(i) == ' ')
					words++;
			}
			// For compatibility with Berkeley parser
			if(words <= 5 || words > 300)
				continue;
			// Skip sentences without locations
			if(!goldtext.contains("location")){
				continue;
			}else{
				//System.out.println("reading gold: " + goldtext);
			}
			int gold = 0;
			if(goldtext.contains("metonymic")){
				gold = 1;
			}
			// Search plaintext data for the line corresponding to the gold
			// location tag just found.  Populate and output associated features.
			while ((text = br.readLine())!=null) {
				System.out.println(text);
				
				words = 0;
				if(text.length() > 0)
					words = 1;
				for(int i = 0; i < text.length(); i++){
					if(text.charAt(i) == ' ')
						words++;
				}

				// For compatibility with Berkeley parser
				if (words <= 3 || words > 300){
					bdIdx++; // skip dummy annotation from Berkeley
					continue;
				}

				HashSet<String> reducedmatch = new HashSet<String>();
				String goldtemp = goldtext, location = "";
				while (goldtemp.contains("location") && goldtemp.contains("\"> ") && goldtemp.contains(" </location>")){
					location = goldtemp.substring(goldtemp.indexOf("\"> ") + 3, goldtemp.indexOf(" </location>"));
					goldtemp = goldtemp.substring(goldtemp.indexOf("</location>") + 10);
					reducedmatch.add(location);
				}
				System.out.println(reducedmatch);
				
				// Find corresponding line in br
				boolean hasMatch = true;
				for(String str : reducedmatch){
					if(!text.toLowerCase().contains(str.toLowerCase())){
						hasMatch = false;
					}
				}
				System.out.println(hasMatch);
				if(!hasMatch && LCS(goldtext, text) <= text.length() / 3){
					bdIdx++;
					continue;
				}

				System.out.println("Found line: " + text);
				
				if (reducedmatch.size() == 0) {
					System.err.println("** No toponyms in text!");
					bdIdx++;
					continue;
				}
				
				System.out.print("Locations found:");
				System.out.println(reducedmatch);
				
				/**
				 * ToDo: This bdArray should be filled in.
				 */
				ArrayList<BerkeleyData> bdArray = new ArrayList<BerkeleyData>();
				
				//This error could only be fixed by looking at the function of the code.
				// Populate and output features
				HashMap<String, MetFeatures> feats = getFeatures(bdArray.get(bdIdx), reducedmatch);
				for(Entry<String, MetFeatures> e : feats.entrySet()){
					String topo = e.getKey().substring(e.getKey().indexOf("]") + 1);
					MetFeatures temp = e.getValue();
					
					// Examine lemmatized verb
					double metVerb = 0;
					List<String> lemma = null;
					if(temp != null && temp.verb != null){
						List<String> verb = new ArrayList<String>();
						verb.add(temp.verb);
						lemma = enLemma.lemmatize(verb);
						System.out.println("Verb lemma: " + lemma);
						metVerb = vc.classify(lemma.get(0));
					}
					
					if(temp != null){
						featOut.write(gold + " 1:" + temp.possessive + " 2:" + temp.plural + " 3:" +
								temp.determiner + " 4:" + temp.noun_phrase + " 5:" + temp.verb_phrase +
								" 6:" + temp.subject + " 7:" + metVerb);
						featOut.newLine();
						output.write("\"" + text + "\",\"" + topo + "\",\"" + gold + "\",\"" + temp.possessive + "\",\"" +
								temp.plural + "\",\"" + temp.determiner + "\",\"" + temp.noun_phrase + "\",\"" + temp.verb_phrase +
								"\",\"" + temp.subject + "\",\"" +
								((lemma != null && lemma.size() > 0)?lemma.get(0) + "("+ metVerb + ")\"," : "\","));
						output.newLine();
					}
				}
				bdIdx++;
				break;
			}
			if(text == null){
				break;
			}
		}
		goldbr.close();
		br.close();
		output.close();
		featOut.close();
	}
}