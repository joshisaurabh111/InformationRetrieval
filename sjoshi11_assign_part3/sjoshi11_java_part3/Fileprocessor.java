package sjoshi11_java_part3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Fileprocessor {

	public ArrayList<String> documents = null;
	public Map<String, HashMap<Integer, Integer>> tokens = null;
	public ArrayList<String> toks = null;
	public StringTokenizer st = null;
	public HashSet<String> hs = null;
	public BufferedWriter bw = null;
	public BufferedReader bufferedReader = null;
	public String rate1 = "";

	public Fileprocessor() {
		documents = new ArrayList<String>();
		tokens = new HashMap<String, HashMap<Integer, Integer>>();
		toks = new ArrayList<String>();
	}

	File[] getFiles(String dirPath) {
		File folder = new File(dirPath);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);
		return listOfFiles;
	}

	void parseFile(File[] listOfFiles, String fName) {

		openOfile(fName);
		String head = "filename , title , reviewer , rate , summary \n";
		writeFile(head);
		for (File file : listOfFiles) {
			if (file.isFile()) {
				try {
					Document doc = Jsoup.parse(file, "UTF-8");
					String fileName = file.getName();
					
					//Title
					String title = doc.select("title").text();
					// Reviwer
					String review = doc.select("h3").text().replace("reviewed by", "");
					String a = doc.text();
					trial();
					// Filename
					Elements snippets = null;
					String snippet = null;
					String check = "Capsule review";
						
					if(a.contains(check)) {
						snippets = doc.select("p:contains(Capsule review)");
						//System.out.println(snippet);
					}
					else{
						snippet = doc.selectFirst("p").text();
					}
					
					String temp = doc.selectFirst("p").text();
					
//					String temp = null;
//					if(snippet.text().isEmpty()) {
//						temp = snippets.text();
//					}else {
//						temp = snippet.text();
//					}


//					
//					for(Element e : r) {
//						String newTrial = e.getElementsContainingText("scale").toString();
//							System.out.println(newTrial);
//						}
//						
					
					String rate = "";
					if (a.matches("\\-\\d+")) {
						rate = "N";
					} else {
						rate = "P";
					}

					String comb = fileName.replaceAll(","," ") + 
							"," + title.replaceAll(","," ") + 
							"," + review.replaceAll(","," ") +
							"," + rate1.replaceAll(","," ") +
							"," + temp.replaceAll(","," ").replaceAll("\"", "") + "\n";
					writeFile(comb);

					documents.add(Jsoup.parse(file, "UTF-8").text());
					snippets = null;
					snippet = null;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		closeFile();
	}

	void tokenize() {

		for (int i = 0; i < documents.size(); i++) {
			st = new StringTokenizer(documents.get(i));
			
			
			
			while (st.hasMoreTokens()) {
				String temp = st.nextToken().replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "").toLowerCase();
				if (temp.endsWith("ies") && !temp.endsWith("aies") && !temp.endsWith("eies"))
					temp = temp.substring(0, temp.length() - 3) + "y";
				if (temp.endsWith("es") && !temp.endsWith("aes") && !temp.endsWith("ees") && !temp.endsWith("oes"))
					temp = temp.substring(0, temp.length() - 2) + "e";
				if (temp.endsWith("s") && !temp.endsWith("as") && !temp.endsWith("us") && !temp.endsWith("as"))
					temp = temp.substring(0, temp.length() - 1);
				if (temp.endsWith("'"))
					temp = temp.substring(0, temp.length() - 1);
				temp.replaceAll("\\s+", "").replaceAll("'", "");
				if (temp.length() > 2) {
					if (tokens.containsKey(temp)) {
						if (tokens.get(temp).containsKey(i + 1)) {
							int counter = tokens.get(temp).get(i + 1);
							counter++;
							tokens.get(temp).replace(i + 1, counter);
							toks.add(temp.toString());
						} else {
							tokens.get(temp).put(i + 1, 1);
						}
					} else {
						HashMap<Integer, Integer> listOfFiles = new HashMap<Integer, Integer>();
						listOfFiles.put(i + 1, 1);
						tokens.put(temp, listOfFiles);
						toks.add(temp.toString());

					}
				}
			}
		}
	}

	void removeStopWords() {

		ArrayList<String> delete = new ArrayList<String>();
		delete.add("and");
		delete.add("the");
		delete.add("an");
		delete.add("by");
		delete.add("from");
		delete.add("for");
		delete.add("hence");
		delete.add("of");
		delete.add("with");
		delete.add("in");
		delete.add("within");
		delete.add("who");
		delete.add("when");
		delete.add("why");
		delete.add("how");
		delete.add("whom");
		delete.add("have");
		delete.add("had");
		delete.add("has");
		delete.add("not");
		delete.add("but");
		delete.add("do");
		delete.add("does");
		delete.add("done");

		tokens.remove("and");
		tokens.remove("the");
		tokens.remove("an");
		tokens.remove("by");
		tokens.remove("from");
		tokens.remove("for");
		tokens.remove("hence");
		tokens.remove("of");
		tokens.remove("with");
		tokens.remove("in");
		tokens.remove("within");
		tokens.remove("who");
		tokens.remove("when");
		tokens.remove("why");
		tokens.remove("how");
		tokens.remove("whom");
		tokens.remove("have");
		tokens.remove("had");
		tokens.remove("has");
		tokens.remove("not");
		tokens.remove("but");
		tokens.remove("do");
		tokens.remove("does");
		tokens.remove("done");

		toks.removeAll(delete);

	}

	void openOfile(String file) {
		try {
			File f = new File(file);
			if (!f.exists()) {
				f.createNewFile();
			}

			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
		} catch (IOException e) {
			System.out.println("File Opening Error");
		}

	}

	void writeFile(String toWrite) {

		try {
			bw.write(toWrite);
		} catch (IOException e) {
			System.out.println("File Writing Error");
		}
	}

	void closeFile() {
		try {
			bw.close();
		} catch (Exception e) {
			System.out.println("FileClosing error");
		}
	}

	void trial() {
		if(Math.random() < 0.5)
		    rate1 = "N";
		 else if(Math.random() > 0.5)
		    rate1 = "P";
		 else
			 rate1 = "NA";
	}

}
