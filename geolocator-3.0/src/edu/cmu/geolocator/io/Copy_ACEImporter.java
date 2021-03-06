package edu.cmu.geolocator.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import edu.cmu.geolocator.model.ACE_NETag;
import edu.cmu.geolocator.model.Document;
import edu.cmu.geolocator.model.Paragraph;
import edu.cmu.geolocator.model.TagDocument;

public class Copy_ACEImporter {

	HashMap<String, Document> sDocs;
	HashMap<String, TagDocument> tagDoc;

	public Copy_ACEImporter(String filename) {
		sDocs = new HashMap<String, Document>();
		tagDoc = new HashMap<String, TagDocument>();
		try {
			importDocs(new File(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		align();
	}

	public void align() {

	}

	public static void main(String argb[]) throws IOException {
		Copy_ACEImporter importer = new Copy_ACEImporter(
				"C:\\Users\\Wynn\\Documents\\wynnzh\\data\\LDC\\ACE\\ACE2005-TrainingData-V6.0\\English\\nw");
		for (Entry<String, Document> e : importer.sDocs.entrySet()) {
			if (e.getKey() == null)
				continue;
			System.out.println(e.getKey());
			ArrayList<Paragraph> paras = e.getValue().getP();
			for (Paragraph para : paras) {
				System.out.println(para.getParagraphString());
				System.out.println(para.getParaStart());
			}
		}
		for (Entry<String, TagDocument> e : importer.tagDoc.entrySet()) {
			if (e.getKey() == null)
				continue;
			ArrayList<ACE_NETag> a = e.getValue().getTags();

			Document doc = importer.sDocs.get(e.getKey());

			System.out.println(e.getKey());
			for (Paragraph p : doc.getP()) {
				String paraString = p.getParagraphString();
				int start = p.getParaStart();
				int end = start + paraString.length();

				for (ACE_NETag tag : a)
					if (tag.getStart() >= start && tag.getEnd() <= end)
						System.out.println(tag.getStart() + " " + tag.getEnd() + " " + tag.getCoarseNEType() + " "
								+ paraString.substring(tag.getStart() - start, tag.getEnd() - start + 2));
			}
		}

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
				sDocs.put(doc.getDid(), doc);
			}

			if (node.isFile() && node.getAbsolutePath().endsWith(".apf.xml")) {
				TagDocument doc = new TagDocument();
				fillACETagDoc(doc, node.getAbsoluteFile());
				tagDoc.put(doc.getDid(), doc);
			}
		}
	}

	private void fillACETagDoc(TagDocument doc, File absoluteFile) throws IOException {

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
				// String _type = line.split(" ")[2]; System.out.println(_type);
				if (line.split(" ")[2].equals("TYPE=\"NAM\""))
					b_mention = true;
			} else if (line.startsWith("</entity_mention>")) {
				b_mention = false;
			} else if (line.startsWith("<head>")) {
				b_head = true;
			} else if (line.startsWith("</head>")) {
				b_head = false;
			} else if (line.startsWith("<charseq ") && b_head == true && b_mention == true) {
				String[] tokens = line.split(">");
				String mention = tokens[1].split("<")[0];
				String[] nums = tokens[0].split(" ");
				String start = nums[1].split("=\"")[1];
				start = start.substring(0, start.length() - 1);
				String end = nums[2].split("=\"")[1];
				end = end.substring(0, end.length() - 1);
				ACE_NETag tag = new ACE_NETag(mention, Integer.parseInt(start), Integer.parseInt(end), etype, esubtype);

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

		ArrayList<Paragraph> paras = new ArrayList<Paragraph>();

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

			} else if (line.startsWith("<TEXT>")) {
				p = new Paragraph();
				p.setParaStart(lcount);
				lcount++;
				b_content = true;
				paraString.append("\n");
			} else if (line.startsWith("</TEXT>")) {
				lcount++;
				b_content = false;
				p.setParagraphString(paraString.toString());

				paraString = new StringBuilder();
				paras.add(p);

			} else if (line.startsWith("<TURN>")||line.startsWith("<POST>")) {
				lcount++;
				paraString.append("\n");
			} else if (line.startsWith("</TURN>")||line.startsWith("</POST>")) {
				lcount++;
				paraString.append("\n");

			} else if (line.startsWith("<SPEAKER>")) {

				lcount += line.length() - 19 + 1;
				paraString.append(line.split(">")[1].split("<")[0]).append("\n");

			} else if (line.startsWith("<POSTDATE>")) {

				lcount += line.length() - 21 + 1;
				paraString.append(line.split(">")[1].split("<")[0]).append("\n");

			} else if (line.startsWith("<POSTER>")) {

				lcount += line.length() - 17 + 1;
				paraString.append(line.split(">")[1].split("<")[0]).append("\n");

			} else if (line.startsWith("<SUBJECT>")) {

				lcount += line.length() - 19 + 1;
				paraString.append(line.split(">")[1].split("<")[0]).append("\n");

			} else if (b_content == true) {

				if (paraString.toString().length() == 0) {
					paraString.append(line);
				} else {
					paraString.append(" ").append(line);
				}
				lcount = line.length() + 1;

			} else if (b_hline == true) {
				doc.setHeadlineStart(lcount);
				lcount += line.length() + 1;
				headline = line;
			} else
				lcount++;
		}
		doc.setP(paras);
	}
}
