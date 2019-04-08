package sjoshi11_java_part2;

import java.io.File;

public class assignmentP2 {

	public static void main(String[] args) {
		
		String dirPath = args[0];
		String dict = args[1];
		String post = args[2];
		String docTable = args[3];
		
		Fileprocessor fp = new Fileprocessor();
		File[] listOfFiles = fp.getFiles(dirPath);
		fp.parseFile(listOfFiles, docTable);
		fp.tokenize();
		fp.removeStopWords();
	
		Dictionary d =  new Dictionary(fp);
		d.createDictionary(dict);
		d.createPost(post);
		
		System.out.println("Done Writing to File");
		
	}
	
}