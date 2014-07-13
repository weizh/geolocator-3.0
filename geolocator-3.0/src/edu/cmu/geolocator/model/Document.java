package edu.cmu.geolocator.model;

import java.util.ArrayList;

public class Document {

	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}

	String did;

	public String getParaString() {
		return paraString;
	}
	public void setParaString(String paraString) {
		this.paraString = paraString;
	}

	ArrayList<Paragraph> p;
	public ArrayList<Paragraph> getP() {
		return p;
	}
	public void setP(ArrayList<Paragraph> p) {
		this.p = p;
	}

	String paraString;
	String headline;
	int headlineStart;
	
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public int getHeadlineStart() {
		return headlineStart;
	}
	public void setHeadlineStart(int headlineStart) {
		this.headlineStart = headlineStart;
	}

	
}
