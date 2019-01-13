import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DailyScript {

//	public static void main(String[] args) {
//		int numSessions = 3;
//		new DailyScript(numSessions);
//	}

	public DailyScript(int numSessions) {
		deleteTSFSessions();
		for (int i = 0; i < numSessions; i++) {
			new OperationManagement(i+1);
		}

		//Merge TSF files into one
		mergeTSF();

		//Instantiate Back to deal with merged transaction summary file
		try {
			new Back();
		} catch (FileNotFoundException e) {
			System.out.println("Error creating Back End");
			e.printStackTrace();
		}
		
		//Delete merged transaction summary file (TSF.txt)
		//deleteTSFSessions();
	}

	public static void mergeTSF(){
		ArrayList<String> mergedTSF = new ArrayList<String>();
		try {
			String currentLine;
			File dir = new File("./TSFolder/");
			for (File file : dir.listFiles()) {
			    Scanner s = new Scanner(file);
			    while(s.hasNextLine()) {
			    	currentLine = s.nextLine();
			    	if (!currentLine.equals("")) {
			    		mergedTSF.add(currentLine);
			    	}
			    }
			    s.close();
			}
			deleteTSFSessions();
			
			BufferedWriter mergedTSFWriter = new BufferedWriter(new FileWriter("./TSFolder/TSF.txt"));
			for(int i=0; i < mergedTSF.size(); i++) {
				if(!mergedTSF.get(i).equals("EOS")) {
					mergedTSFWriter.write(mergedTSF.get(i));
					mergedTSFWriter.newLine();
				}
			}
			mergedTSFWriter.write("EOS");
			mergedTSFWriter.close();
		}
		catch(FileNotFoundException e){
			System.out.println("File does not Exist");
		}
		catch(IOException e) {
			System.out.println("IO Failed");
		}
	}
	
	private static void deleteTSFSessions(){
		
		// list of all tsf text files in TSFolder
		File[] tsfSessionsFolder = new File("./TSFolder/").listFiles();
		// delete all tsf text files
		for (File tsfFile : tsfSessionsFolder) {
		      tsfFile.delete();
		}
	}
}
