package sjoshi11_java_part3;

import java.io.File;

public class assignmentP3 {

	public static void main(String[] args) {
		
		String dirPath = args[0];
		String dict = args[1];
		String post = args[2];
		String docTable = args[3];
		String fileOp = args[4];
		
		Fileprocessor fp = new Fileprocessor();
		File[] listOfFiles = fp.getFiles(dirPath);
		fp.parseFile(listOfFiles, docTable);
		fp.tokenize();
		fp.removeStopWords();

		Dictionary d =  new Dictionary(fp);
		d.createDictionary(dict);
		d.createPost(post);
		
		System.out.println("Done Writing to File");
		
		DocumentTable documentTable = new DocumentTable();
		QueryProcessor processor = new QueryProcessor(d, documentTable, fp);
		processor.getInput();
		processor.processCSV(docTable);
		processor.processDictCSV(dict);
		processor.process(fileOp);
	
		
	}
	
}