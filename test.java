package Assignment2;

import java.io.File;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String input = "input.txt";
		String output ="output";
		Transcript t = new Transcript(input, output);
		
		try {
			t.printTranscript(t.buildStudentArray());
		} catch (InvalidTotalException e) {
			
			e.printStackTrace();
		}
	}

	
	
	
}
