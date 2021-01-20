package Assignment2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.io.*;


/* PLEASE DO NOT MODIFY A SINGLE STATEMENT IN THE TEXT BELOW.
READ THE FOLLOWING CAREFULLY AND FILL IN THE GAPS

I hereby declare that all the work that was required to 
solve the following problem including designing the algorithms
and writing the code below, is solely my own and that I received
no help in creating this solution and I have not discussed my solution 
with anybody. I affirm that I have read and understood
the Senate Policy on Academic honesty at 
https://secretariat-policies.info.yorku.ca/policies/academic-honesty-senate-policy-on/
and I am well aware of the seriousness of the matter and the penalties that I will face as a 
result of committing plagiarism in this assignment.

BY FILLING THE GAPS,YOU ARE SIGNING THE ABOVE STATEMENTS.

Full Name: Jaideep Kular	
Student Number: 217349549
Course Section: A
 */


/**
 * This class generates a transcript for each student, whose information is in the text file.
 * 
 *
 */

public class Transcript {
	private ArrayList<Object> grade = new ArrayList<Object>();
	private File inputFile;
	private String outputFile;

	/**
	 * This the the constructor for Transcript class that 
	 * initializes its instance variables and call readFie private
	 * method to read the file and construct this.grade.
	 * @param inFile is the name of the input file.
	 * @param outFile is the name of the output file.
	 */
	public Transcript(String inFile, String outFile) {
		inputFile = new File(inFile);	
		outputFile = outFile;	
		grade = new ArrayList<Object>();
		this.readFile();
	}// end of Transcript constructor

	/** 
	 * This method reads a text file and add each line as 
	 * an entry of grade ArrayList.
	 * @exception It throws FileNotFoundException if the file is not found.
	 */
	private void readFile() {
		Scanner sc = null; 
		try {
			sc = new Scanner(inputFile);	
			while(sc.hasNextLine()){
				grade.add(sc.nextLine());
			}      
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			sc.close();
		}		
	} // end of readFile


	/**
	 * this method create an arraylist which contains information about different students
	 * @return ArrayList with elements as students
	 * @throws InvalidTotalException
	 */
	public ArrayList<Student> buildStudentArray() throws InvalidTotalException{


		// Created new arrayList to return and Hashset to check for duplicates
		ArrayList<Student> arr = new ArrayList<Student>();
		Set<String> set = new HashSet<String>();

		// this index assigns new student to arr at index arrayIndex
		// this value increases every time a new student's information is stored
		int arrayIndex = 0;


		// this loop is to iterate through all of the input file
		for(int i = 0; i < grade.size(); i++) {

			// divide method separates the string into different sections by eliminating ","
			String[] info = divide(grade.get(i));

			// Initializes a student to store information
			Student student = new Student();

			/*
			 * Code belows check for duplicates
			 * Every student has unique id so it checks if student is already present in arr by checking if id is already present in HASHSET set. 
			 * If not then it adds the student to arr at index arrayIndex
			 * 
			 * If student is already present in arr, then the initialized student gets referenced to that student in arr so we can add more information to student already in arr
			 * this is done by finding the index of the student in arr with the same id.
			 * So if we are storing information about a student who is already in arr, then we basically find the index of the student and adds more information to it.
			 */
			String id = info[2];


			if(set.add(id)) {
				arr.add(arrayIndex, student);
				student.setName(info[info.length - 1]);
				student.setStudentID(id);	
				arrayIndex ++;
			} 
			
			/*
			 * In the else statement I find the index of the object I want and reference it to student variable
			 */
			else {
				for(int j = 0; j < arr.size(); j++ ) {
					if(arr.get(j).getStudentID().equals(info[2])) {
						student = arr.get(j);
						break;
					}
				}
			}

			// counts the number of assessments each course has
			int numAssessments = info.length - 4;

			// creates a new list to store grades
			ArrayList<Double> grades = new ArrayList<>();

			// this index is the starting point of grades given in the text file.
			// [0] is class code, [1] is credit, [2] is studentID
			int index = 3;
			for(int j = 0; j < numAssessments; j++) {
				grades.add(Double.parseDouble(info[index].substring(4, 6)));
				index++;
			}


			// this list to store the assessments of each course
			ArrayList<Assessment> tests = new ArrayList<Assessment>();

			// this index is the starting point of grades given in the text file.
			// [0] is class code, [1] is credit, [2] is studentID
			index = 3;

			for(int j = 0; j < numAssessments; j++) {
				/*
				 *  creates a new assessment with given type and weight and adds it to tests
				 *  here we convert string to int using parseInt
				 */

				Assessment assessment = Assessment.getInstance( info[index].charAt(0) ,Integer.parseInt(info[index].substring(1, 3)) );
				index++;
				tests.add(assessment);
			}


			// this list stores the weight of each assessment in tests for calculating grade
			ArrayList<Integer> testsWeight = new ArrayList<>();
			for(Assessment a: tests) {
				testsWeight.add(a.getWeight());
			}

			// Creates a new course and adds it to student
			Course course = new Course(info[0], tests, Double.parseDouble(info[1]));
			student.addCourse(course);

			// evaluate the student's grade in the course added
			student.addGrade(grades, testsWeight);

		}

		return arr;
	}

	/**
	 * This methods creates a new file and stores each student's info in it.
	 * @param arr
	 */
	public void printTranscript(ArrayList<Student> arr) {
		
		
/*
 * Creates a file with given name
 * uses enhanced loop to iterate through every student and write their info in the file by using toString() method
 */
		File output = new File(outputFile);
		try {
			output.createNewFile();
			FileWriter writer = new FileWriter(outputFile);
			for(Student a: arr) {			 
				writer.write(a.toString());	
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 * This method separates the values between ","
	 * @param obj
	 * @return an array of String split by a comma (",")
	 */
	private String[] divide (Object obj) {
		String string = (String) obj;
		String[] result = string.split(",");
		return result;
	}



} // end of Transcript

class Student{
	private String studentID;
	private String name;
	private ArrayList<Course> courseTaken;
	private ArrayList<Double> finalGrade;

	// default constructor
	public Student(){
		setStudentID("");
		setName("");
		courseTaken = new ArrayList<>();
		finalGrade = new ArrayList<>();

	}

	// Overloaded constructor 
	public Student(String id, String name, ArrayList<Course> course){
		setStudentID(id);
		this.setName(name);
		courseTaken = new ArrayList<>();
		for(Course a: course) {
			courseTaken.add(new Course(a));
		}
		finalGrade = new ArrayList<>();
	}

	/**
	 * Adds a Course's grade to finalGrade list
	 * @param grade - the grade gotten in assessments
	 * @param weight - the weight of assessments
	 * @throws InvalidTotalException - throws this exception if the grade or the weight of a course exceeds 100
	 */
	public void addGrade(ArrayList<Double> grade, ArrayList<Integer> weight) throws InvalidTotalException{
		// gets added to final grade
		double sum = 0;
		double totalWeight = 0;

		for(int a : weight) {
			totalWeight += a;
		}
		if(totalWeight != 100) throw new InvalidTotalException();

		for(int i = 0; i < grade.size(); i++) {
			sum += (grade.get(i) / 100) * (weight.get(i));
		}
		if(sum > 100) throw new InvalidTotalException();

		finalGrade.add(sum);
	}

	/**
	 * This method returns the final GPA by using every Course's grade and its credits
	 * @return
	 */
	public double weightedGPA() {
		//returns the final gpa
		double sum = 0;
		for(int i = 0; i < finalGrade.size(); i++) {
			sum += convertToGP(finalGrade.get(i)) * courseTaken.get(i).getCredit();
		}
		double totalCredits = 0;
		for(int j = 0; j < courseTaken.size(); j++) {
			totalCredits += courseTaken.get(j).getCredit();
		}

		return sum/totalCredits;

	}

	/**
	 * this method convers a grade to its GP
	 * @param grade
	 * @return Grade Point of given grade
	 */
	private int convertToGP( double grade) {
		if(grade < 47) return 0;
		else if (grade < 50) return 1;
		else if (grade < 55) return 2;
		else if (grade < 60) return 3;
		else if (grade < 65) return 4;
		else if (grade < 70) return 5;
		else if (grade < 75) return 6;
		else if (grade < 80) return 7;
		else if (grade < 90) return 8;
		else return 9;

	}

	// this method adds course to the courseTaken list
	public void addCourse(Course course) {
		courseTaken.add(course);
	}

	/**
	 * This method returns a String containing student's information
	 */
	public String toString() {
		String output = "";
		output += getName() + "\t" + getStudentID() + "\n";
		output += "--------------------" + "\n";
		for(int i = 0; i<courseTaken.size(); i++) {
			output += courseTaken.get(i).getCode() + "\t" + String.format("%.1f", finalGrade.get(i) ) + "\n"; 
		}
		output += "--------------------" + "\n";
		output += "GPA:" + String.format("%.1f", weightedGPA() ) + "\n";
		return output;

	}


	// returns name of student
	public String getName() {
		return name;
	}

	// sets name of student as the given name
	public void setName(String name) {
		this.name = name;
	}

	// returns the studentID
	public String getStudentID() {
		return studentID;
	}

	// sets the student ID
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * Returns a deep copy of courseTaken to prevent privacy leaks
	 * @return list of Courses taken
	 */
	public ArrayList<Course> getCourseTaken (){
		ArrayList<Course> output = new ArrayList<>();
		for(Course a: courseTaken) {
			output.add(new Course(a));
		}
		return output;
	}
	
	public void setCourseTaken(ArrayList<Course> course){
		this.courseTaken = new ArrayList<>();
		for(Course a: course) {
			this.courseTaken.add(new Course (a));
		}
	}

	/**
	 * Returns a deep copy of courseTaken to prevent privacy leaks
	 * @return list of final grades in courses taken
	 */
	public ArrayList<Double> getFinalGrade(){
		ArrayList<Double> output = new ArrayList<>();
		for(Double a: finalGrade) {
			output.add(a);
		}
		return output;
	}
	
	public void setFinalGrade(ArrayList<Double> grades){
		this.finalGrade = new ArrayList<>();
		for(Double a: grades) {
			this.finalGrade.add(a);
		}
	}

}

class InvalidTotalException extends Exception {
	// default constructor
	public InvalidTotalException(){
		super();
		toString();
	}

	// Overriden method
	public String toString() {
		return ("Invalid total Exception");
	}
}


class Course{
	private String code;
	private	ArrayList<Assessment> assignment;
	private double credit;

	// default constructor
	public Course(){
		code = "";
		assignment = new ArrayList<Assessment>();
		credit = 0.0;
	}

	// Overloaded constructor
	public Course(String code, ArrayList<Assessment> assignment, double credit){
		this.code = code;
		this.credit = credit;
		this.assignment = new ArrayList<Assessment>();
		for(Assessment a : assignment) {
			this.assignment.add(Assessment.getInstance(a.getType(), a.getWeight()));
		}
	}

	// Overloaded constructor
	public Course (Course course){
		this(course.code, course.assignment, course.credit);
	}

	// returns credit of the course
	public double getCredit() {
		return credit;
	}
	// returns the code of the course
	public String getCode() {
		return code;
	}
	// sets the credit to the given amount
	public void setCredit(double credit) {
		this.credit = credit;
	}
	// sets the code as the given code
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Creates a deep copy of assignments to prevent privacy leaks
	 * @return list of assessments in the course
	 */
	public ArrayList<Assessment> getAssignment(){
		ArrayList<Assessment> output = new ArrayList<>();
		for(Assessment a: this.assignment) {
			output.add(Assessment.getInstance(a.getType(), a.getWeight()));
		}
		return output;
	}

	// set the assignment same as given assignment list
	public void setAssignment(ArrayList<Assessment> assignment) {
		this.assignment = assignment;
	}

	/**
	 * Compares two different courses
	 * returns true if they are exact same courses
	 */
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Course == false) return false;
		Course a = (Course) obj;
		if(code.equals(a.code) && credit == a.credit) {
			for(int i = 0; i < a.assignment.size(); i++) {
				if(!assignment.get(i).equals(a.assignment.get(i))) return false;
			}
			return true;
		}
		return false;
	}
	// HashCode for the course
	public int hashCode() {
		return Objects.hash(code, credit, assignment);
	}

}

class Assessment{
	private char type;
	private int weight;

	// default constructor
	private Assessment(){
		type = '\u0000';
		weight = 0;
	}

	// Overloaded constructor
	private Assessment(char type, int weight){
		this.type = type;
		this.weight = weight;
	}

	// static factory method
	public static Assessment getInstance(char a, int b){
		return new Assessment(a,b);
	}

	// returns type of assessment
	char getType(){
		return type;
	}

	// set the type of assessment same as given type
	void setType(char type) {
		this.type = type;
	}

	// returns weight of the assessment
	int getWeight() {
		return weight;
	}

	// sets the weight of the assessment same as weight provided
	void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * Checks two assessments for equality
	 * returns true if they are of same type and have same weight
	 */
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Assessment == false) return false;
		Assessment a = (Assessment) obj;
		if(type == a.type && weight == a.weight) return true;
		return false;
	}

	// HashCode method for assessment
	public int hashCode() {
		return Objects.hash(type, weight);
	}

}


