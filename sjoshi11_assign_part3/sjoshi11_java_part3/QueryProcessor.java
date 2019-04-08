package sjoshi11_java_part3;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class QueryProcessor {

	Scanner scan = null;
	ArrayList<String> queries = new ArrayList<String>();
	public String currentOp = null;
	public DocumentTable dt = null;
	public Dictionary di = null;
	public Fileprocessor filep = null;

	Map<Integer, DocumentTable> map = null;
	Map<String, DictTable> map1 = null;

	public QueryProcessor(Dictionary d, DocumentTable documentTable, Fileprocessor fp) {
		this.di = d;
		this.dt = documentTable;
		this.filep = fp;
	}

	public Map<Integer, DocumentTable> processCSV(String path) {

		File file = new File(path);
		try {
			Reader reader = new FileReader(file);
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
			map = new TreeMap<>();
			int counter = -1;
			for (CSVRecord record : records) {
				DocumentTable docTable = new DocumentTable();
				docTable.filename = record.get(0);
				docTable.title = record.get(1);
				docTable.reviewer = record.get(2);
				docTable.rate = record.get(3);
				docTable.snippet = record.get(4);
				map.put(++counter, docTable);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(map);
		return map;
	}

	public Map<String, DictTable> processDictCSV(String Distpath) {

		File file = new File(Distpath);
		try {
			Reader reader = new FileReader(file);
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
			map1 = new TreeMap<>();

			for (CSVRecord record : records) {
				DictTable dictTable = new DictTable();
				dictTable.token = record.get(0);
				dictTable.docFreq = record.get(1);
				dictTable.offset = record.get(2);
				map1.put(dictTable.token, dictTable);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map1;
	}

	public void getInput() {

		scan = new Scanner(System.in);
		while (true) {
			String q = scan.nextLine();
			queries.add(q);
			if (q.equals("EXIT") || q.equals("exit")) {
				queries.remove(queries.size() - 1);
				break;
			}
		}
		// System.out.println(queries);
	}

	public void process(String fileOp) {

		filep.openOfile(fileOp);
		while (!queries.isEmpty()) {
			filep.writeFile("QUERY :  " + queries.get(0) + "\n\n");
			String text[] = queries.get(0).split(" ");
		
			// ArrayList

			ArrayList<Integer> need = new ArrayList<Integer>();
			ArrayList<Integer> remove = new ArrayList<Integer>();

			for (int i = 0; i < text.length; i++) {
				if (text[i].equals("AND") && !text[i].equals("OR") && !text[i + 1].equals("NOT")) {
					currentOp = "AND";
				} else if (text[i].equals("OR") && !text[i].equals("AND") && !text[i + 1].equals("NOT")) {
					currentOp = "OR";
				} else if (text[i].equals("AND") && text[i + 1].equals("NOT")) {
					currentOp = "ANDNOT";
					i=i+1;
				} else {
					String temp = text[i];
					// System.out.println(text[i]);
					// System.out.println(di.posting);
					DictTable d = map1.get(temp);
					
					Integer offset = Integer.parseInt((d.offset));
				//	System.out.println(offset);
					Integer docfreq = Integer.parseInt((d.docFreq));
				//	System.out.println(docfreq);
					
					if (currentOp.equals("AND")) {
						if (need.isEmpty()) {
							for (int s = offset; s<=docfreq+offset-1; s++) {
							//	System.out.println(di.posting.get(s));
								need.add(di.posting.get(s));
							}
						} else {
							if (!need.isEmpty()) {
								for (int l = offset; l <= docfreq+offset-1; l++) {
									remove.add(di.posting.get(l));
								}
								need.retainAll(remove);
							}
							
						}
					} else if (currentOp.equals("OR")) {
						for (int f = offset; f <= docfreq+offset-1; f++) {
							//System.out.println(di.posting.get(f));
							need.add(di.posting.get(f));
						}
					} else if (currentOp.equals("ANDNOT")) {
						for (int g = offset; g <= docfreq+offset-1; g++) {
							remove.add(di.posting.get(g));
						}
				//		System.out.println(remove);
						need.removeAll(remove);
				//		System.out.println(need);
					}
				}
				
			}
			Collections.sort(need);
			while (!need.isEmpty()) {				
				filep.writeFile("" + (map.get(need.remove(0))) + "\n");
			}
			need.clear();
			remove.clear();
			queries.remove(0);

		}
		filep.closeFile();
	}

}
