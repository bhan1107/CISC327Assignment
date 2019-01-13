import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.print.DocFlavor.URL;

public class OperationManagement {
	
	public OperationManagement(int sessionNum) {
		ArrayList<String> inputList = new ArrayList<>();
        ArrayList<Integer> validServicesList = new ArrayList<>();
        Front frontEnd;
        String line = "";
        boolean valid = true;
        
        System.out.println("Start of session " + sessionNum);
        //Read user input from command line
        Scanner scanner = new Scanner(System.in);
        
        line = scanner.nextLine();
        
        if (!line.equals("login")) {
            System.out.println("ERROR: First line of input should be login");
            //scanner.close();
        }
        //If user logs in properly, continue checking input until user logs out
        else {
            line = scanner.nextLine();
            
            //Stop when user logs out
            while (!line.equals("logout")) {
                if (line.equals("login")) { //no further login is accepted once login is first accepted
                    valid = false;
                    break;
                }
                inputList.add(line);
                line = scanner.nextLine();
            }
            
            //scanner.close();
            System.out.println("End of session " + sessionNum);
            
            if (valid) {
                //Read valid services file
                try {                	
                    validServicesList = readValidServiceFile("validservices.txt");
                    frontEnd = new Front(validServicesList, inputList, sessionNum);
                } catch (Exception e) {
                    System.out.println("ERROR: Valid Services File could not be read \n");
                }
            }
            
        }
	}
    
    public ArrayList<Integer> readValidServiceFile(String fileLocation) throws Exception {
        ArrayList<Integer> serviceNumberList = new ArrayList<>(); //This array list will store service numbers in Valid Service File
        File serviceFile = new File(fileLocation);
        Scanner textInput = new Scanner(serviceFile); // textInput reads valid service files
        String currentLine;
        
        while(textInput.hasNextLine()) {
        	currentLine = textInput.nextLine();
            if (!currentLine.equals("")) {
            	serviceNumberList.add(Integer.parseInt(currentLine));
            }
        }
        textInput.close();
        return serviceNumberList;
    }
}
