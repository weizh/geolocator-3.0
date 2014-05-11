package edu.cmu.geolocator.metonym;


public class MetFeatures {
	public static final int FEAT_DONE = 3;
	public int metonym, possessive, plural, determiner,
		noun_phrase, verb_phrase, subject;
	public int full;
	public String toponym, verb;
	public MetFeatures(String toponym){
		this.toponym = toponym;
		full = 0;
	}
	public MetFeatures(String toponym, int metonym, int possessive, int plural, int determiner,
			int noun_phrase, int verb_phrase, String verb){
		this.toponym = toponym;
		this.metonym = metonym;
		this.possessive = possessive;
		this.plural = plural;
		this.determiner = determiner;
		this.noun_phrase = noun_phrase;
		this.verb_phrase = verb_phrase;
		this.verb = verb;
		full = 2;
	}
}
