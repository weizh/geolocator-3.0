package edu.cmu.geolocator.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.geolocator.model.ACE_NETag;
import edu.cmu.geolocator.model.Document;
import edu.cmu.geolocator.model.Paragraph;
import edu.cmu.geolocator.model.TagDocument;

public class ACEImporter {

	ArrayList<Document> sDocs;
	ArrayList<TagDocument> tagDoc;

	public ACEImporter() {
		sDocs = new ArrayList<Document>();
	}

	public void align(){
		
	}
	void importDocs(File node) throws IOException {

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				importDocs(new File(node, filename));
			}
		} else {

			if (node.isFile() && node.getAbsolutePath().endsWith(".sgm")) {
				Document doc = new Document();
				fillACEDoc(doc, node.getAbsoluteFile());
				sDocs.add(doc);
			}

			if (node.isFile() && node.getAbsolutePath().endsWith(".apf.xml")) {
				TagDocument doc = new TagDocument();
				fillACETagDoc(doc, node.getAbsoluteFile());
				tagDoc.add(doc);
			}
		}
	}

	private void fillACETagDoc(TagDocument doc, File absoluteFile) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(absoluteFile));

		String line = null;
		String etype = null, esubtype = null;
		boolean b_mention = false, b_head = false;

		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("<document "))
				doc.setDid(line.split("\"")[1]);
			else if (line.startsWith("<entity ")) {
				String[] tokens = line.split(" ");
				etype = tokens[2];
				esubtype = tokens[3];
				b_mention = false;
			} else if (line.startsWith("</entity ")) {
				etype = null;
				esubtype = null;
			} else if (line.startsWith("<entity_mention ")) {
				if (line.split(" ")[1].equals("TYPE=\"NAM\""))
					b_mention = true;
			} else if (line.startsWith("</entity_mention ")) {
				b_mention = false;
			} else if (line.startsWith("<head>")) {
				b_head = true;
			} else if (line.startsWith("</head>")) {
				b_head = false;
			}
			else if (line.startsWith("<charseq ") && b_head==true && b_mention == true){
				String[] tokens = line.split(">");
				String mention = tokens[1].split("<")[0];
				String[] nums = tokens[0].split(" ");
				String start = nums[1].split("=\"")[1]; start = start.substring(0,start.length()-1);
				String end = nums[2].split("=\"")[1]; end = end.substring(0, end.length()-1);
				ACE_NETag tag = new ACE_NETag(mention, Integer.parseInt(start), Integer.parseInt(end), etype,esubtype);
				doc.addTag(tag);
			}
		}
	}

	private void fillACEDoc(Document doc, File file) throws IOException {

		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(file));

		String line = null;
		int lcount = 0;

		String headline = "";
		StringBuilder paraString = new StringBuilder();

		boolean b_hline = false, b_content = false;

		Paragraph p = null;

		while ((line = br.readLine()) != null) {

			if (line.startsWith("<DOC>") || line.startsWith("</DOC>"))
				lcount++;
			else if (line.startsWith("<DOCID>")) {
				String id = line.split(">")[1].split("<")[0];
				lcount += id.length() + 1;
				doc.setDid(id.trim());
			} else if (line.startsWith("<DOCTYPE")) {
				String type = line.split(">")[1].split("<")[0];
				lcount += type.length() + 1;
			} else if (line.startsWith("<DATETIME>")) {
				lcount += line.length() - 21 + 1;
			} else if (line.startsWith("<BODY>") || line.startsWith("</BODY>")) {
				lcount++;
			} else if (line.startsWith("<HEADLINE>")) {
				lcount++;
				b_hline = true;
			} else if (line.startsWith("</HEADLINE>")) {
				lcount++;
				b_hline = false;
				doc.setHeadline(headline);

			} else if (line.startsWith("<TEXT>") || line.startsWith("</TEXT>")) {
				lcount++;
			} else if (line.startsWith("<TURN>")) {
				lcount++;
				b_content = true;
				p = new Paragraph();

			} else if (line.startsWith("</TURN>")) {
				lcount++;
				b_content = false;
				p.setParagraphString(paraString.toString());
				paraString = new StringBuilder();

			} else if (line.startsWith("<SPEAKER>")) {
				lcount += line.length() - 19 + 1;
			} else if (b_content == true) {

				if (paraString.toString().length() == 0) {
					paraString.append(line);
					p.setParaStart(lcount);
				} else {
					paraString.append(" ").append(line);
				}
				lcount = line.length() + 1;

			} else if (b_hline == true) {

				doc.setHeadlineStart(lcount);
				lcount += line.length() + 1;
				headline = line;

			}
		}
	}
}
