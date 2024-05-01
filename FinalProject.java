
//COP 3330 Final Project
//Giullya Santos, Sanjay Sarran, Esteban Venegas
import java.io.File;
import java.io.*;
import java.util.*;
public class FinalProject {
	public static void main(String[] args) {
		LecList lectures = new LecList();
		String[] path = new String[1], instance = new String[1];
		Scanner scan = new Scanner(System.in);
		String file, name = null,id = null,rank = null,location = null, input = null;
		boolean leave = false;
		
		Faculty tempFaculty  = null;
		Student tempStudent;
		
		LinkedList <People> peopleList = new LinkedList<>();
		int numLectures, i, flag;
		String crns = null, tempString, tempStringList[] = new String[100];
		boolean goAgain = false, ask = true, exists = false, deleted = false;
		
		
		System.out.print("Enter the absolute path of the file: ");
		
		//finding file
		while(true) {
			do {
				try {
					goAgain = false;
					path[0] = scan.nextLine();
					lectures = readFile(path);
					if(lectures == null) {
						throw new Exception();
					}
					System.out.println("File Found! Lets proceed...\n");
					break;
				}catch(Exception e){
					System.out.print("Try again: ");
					goAgain = true;
				}	
				
		}while(goAgain);//will go on until file is found
			//lectures.printList();
		
			//menu
			while(ask) {
				System.out.println("\nChoose one of these options: ");
				System.out.println("1- Add a new Faculty to the schedule");
				System.out.println("2- Enroll a Student to a Lecture");
				System.out.println("3- Print the schedule of a Faculty");
				System.out.println("4- Print the schedule of an TA");
				System.out.println("5- Print the schedule of a Student");
				System.out.println("6- Delete a Lecture");
				System.out.println("7- Exit");
				System.out.print("         Enter your choice: ");
				
				int choice = getInt(scan);
				scan.nextLine();
			
			//choices
			LinkedList <Lecture> tempLectureList = new LinkedList<>();
			switch(choice) {
			case 1:
				exists = false;
				
				while(true) {
				System.out.print("Enter UCF ID: ");
				
					//checks to see if id is a valid input
					id = idInput(id, scan, goAgain);
					//searches for id in list
					i = searchId(peopleList, id, instance);
					
					if(instance[0].equals("Faculty") || instance[0].equals("New")) {
					
						if(i > -1) {//if it exists add existing input in
							System.out.println("This person has already been added to the database");
							name = peopleList.get(i).name;
							rank = ((Faculty)(peopleList.get(i))).rank;
							location = ((Faculty)(peopleList.get(i))).officeLocation;
						}
						else {//if it doesn't, get input
							System.out.print("Enter name: ");
							name = scan.nextLine();
					
							while(true) {
							System.out.print("Enter rank: ");
							
								//try catch method to make sure we put the correct rank
								try {
								rank = scan.nextLine();
									if(rank.equalsIgnoreCase("professor") || rank.equalsIgnoreCase("associate professor")
											|| rank.equalsIgnoreCase("assistant professor") || rank.equalsIgnoreCase("adjunct")) {
										break;
										}
									else {
										throw new Exception();
									}	
									} catch(Exception e) {
										System.out.print("Choose a valid rank: Choose between Professor, Associate Professor, Assistant Professor and Adjunct\n");
									}
							}
							
							System.out.print("Enter office location: ");
							location = scan.nextLine();
							}
						
					while(true) {	
					System.out.print("Enter how many lectures: ");
					numLectures = getInt(scan);
						if(numLectures > 0) {
							break;
						}
						else {
							System.out.print("Sorry, you can't add 0 lectures, try again\n");
						}
					}
					
					
					System.out.print("Enter the crns of the lectures: ");
					crns = scan.nextLine();
				
					do {
						try {
							//geting crns
							goAgain = false;
							tempString = scan.nextLine();
							tempStringList = tempString.split(" ");
							
							//if we put less crns than if we put in number
							if(tempStringList.length != numLectures) {
								//scan.nextLine();
								throw new InputMismatchException();
							}
							
								//searching to see if crns exist
								for(i = 0; i < numLectures; i++) {
									if(lectures.search(tempStringList[i]) == null)
										throw new Exception();
								}	
								
								
							}catch(InputMismatchException e) {
								System.out.println("You did not enter the right amount of courses, please try again:  ");
								goAgain = true;
							}catch(Exception e) {
								System.out.println("One of the crns you entered dont exist, please try again: ");
								goAgain = true;
							}
						}while(goAgain);//goes until all the variables are correctly places
					tempFaculty = new Faculty(id, name, tempLectureList, rank, location);
					for(i = 0; i < numLectures; i++) {
						((lectures.search(tempStringList[i])).peopleList).add(tempFaculty);
						
					}
					peopleList.add(tempFaculty);
					LinkedList<String> labs = new LinkedList<>();
					Boolean keepGoing = true;
					//adding lectures
					for(i = 0; i < numLectures; i++) {
						//if it's a lab, making TA for the lab
						if(lectures.search(tempStringList[i]) instanceof Lab) {
							int size = ((Lab)(lectures.search(tempStringList[i]))).getCrnLab().length;//getting #of labs
							
							for(int j = 0; j < size; j++) {
								
								String crnn = ((Lab)(lectures.search(tempStringList[i]))).crnLabPrint(j);//getting labs crn
								
								while(keepGoing) {
									//getting TA's information
									System.out.println("Enter the TA's id for " + crnn + ":");
									id = scan.nextLine();
									String degree;
									int x = searchId(peopleList, id, instance);
									
									
									if(x > -1 && peopleList.get(x) instanceof Student) {
											int z = ((Student)(peopleList.get(x))).findClass(lectures.search(tempStringList[i]).getCrn());
										
											if(z != -1) {
												System.out.println("Sorry, this student can't be added because it's taking the class already, try again...\n");
											}
											else{
												//adding to existing student if found
												System.out.println("TA Found as a student: " +peopleList.get(x).name + "\n");
					
												
												//checking to see if is a TA already
												if(((Student)(peopleList.get(x))).supervisor == null){
													//if not getting info
													System.out.println("TA's supervisor's name:");
													String supervisor = scan.nextLine();
													System.out.println("Degree Seeking:");
													
													while(true) {
													try {
														 degree = scan.nextLine();
															if(degree.equalsIgnoreCase("phd") || degree.equalsIgnoreCase("ms") ){
																break;
																}
															else {
																throw new Exception();
															}	
															} catch(Exception e) {
																System.out.print("Choose a valid degree. Choose between Phd and MS:");
															}
													}
												
													labs.add(crnn);
													((Student)(peopleList.get(x))).makeTA(supervisor, degree, labs);//makeTA FUNCTION
													break;
												}
												else {
													((Student)(peopleList.get(x))).taLabList.add(crnn);
													break;
												}
												
											}
											
									
										}
									
									else {
										System.out.println("Sorry there is no student with this ID, please create a student before making it a TA\n");
										System.out.println("Leaving option... nothing was saved\n");
										leave = true;
										break;
									}
								}
								
								if(keepGoing == false || leave == true) {
									break;
								}
							}
						}
						
						if(keepGoing == false || leave == true) {
							break;
						}
						tempLectureList.add(lectures.search(tempStringList[i]));	//just add lecture to list
					}
					
				if(keepGoing == true || leave == false) {
				//making new faculty
				tempFaculty = new Faculty(id, name, tempLectureList, rank, location);
				tempFaculty.list = tempLectureList;
				
				//adding person to lecture 
				for(i = 0; i < numLectures; i++) {
					((lectures.search(tempStringList[i])).peopleList).add(tempFaculty);
					
				}
			
				//adding person to peopleList
				peopleList.add(tempFaculty);
				break;
				}
			}
				else if(leave == false) {
					System.out.println("Sorry the ID you entered is not a Faculty member, try again");
				}
		}
				if(leave == false) {
				for(int m = 0; m < tempStringList.length; m++) {
					System.out.println("[" + lectures.search(tempStringList[m]).getCrn() + "/" + lectures.search(tempStringList[m]).getPrefix() 
									+ "/" + lectures.search(tempStringList[m]).getTitle() + "]" + " Added!\n");
				}	
				}	
			break;
				
			/* MAKE SURE WE HAVE STUDENTS CLASS BEING ADDED NOT REWRITTEN*/
			case 2:
				flag = 0;
				System.out.print("Enter UCF ID: ");
				
				//checks to see if id is a valid input
				id = idInput(id, scan, goAgain);
				//searches for id in list
				i = searchId(peopleList, id, instance);
				
				//if found, adding info that already exists
				if(i > -1) {
					if(peopleList.get(i) instanceof Student) {
					System.out.println("Record found/Name: " + peopleList.get(i).name + "\n"
							+ "Which lecture to enroll [" + peopleList.get(i).name + "]" + "in? ");
					name = peopleList.get(i).name;
					}
					else {
						System.out.println("Sorry, the ID you input does not belong to a Student, leaving...\n");
						break;
					}
				}
				else { //if not found, getting user input
					System.out.print("Enter name: ");
					name = scan.nextLine();
					System.out.print("Enter the crns of the lectures (zero is an acceptable entry here):  ");
				}
					
					tempLectureList.clear();
					do {
						try {
							//geting crns
							goAgain = false;
							tempString = scan.nextLine();
							if(tempString.equals("0")) {
								tempStudent = new Student(id, name, tempLectureList,"undergraduate");
								peopleList.add(tempStudent);
								flag = 50;
								 continue;
							}
							tempStringList = tempString.split(" ");
							
								//searching to see if crns exist
								for(int j = 0; j < tempStringList.length; j++) {
									if(lectures.search(tempStringList[j]) == null)
										throw new Exception();
								}	
								
							}catch(Exception e) {
								System.out.println("The crns you entered dont exist, please try again: ");
								goAgain = true;
							}
						}while(goAgain);//goes until all the variables are correctly places
			
					if(flag != 50) {
						for(int j = 0; j < tempStringList.length; j++) {
							tempLectureList.add(lectures.search(tempStringList[j]));
						}
					
					//making new student
					if(i > -1) {
						//System.out.println("the index has a student:"+ i);
						for(int j = 0; j < tempStringList.length; j++) {
							peopleList.get(i).list.add(tempLectureList.get(j));
							System.out.println("Enrolled [" + name + "]"+ tempLectureList.get(j));
						}
					}
					else {
						tempStudent = new Student(id, name, tempLectureList,"undergraduate");
						peopleList.add(tempStudent);
						//adding person to lecture 
						for(i = 0; i < tempStringList.length; i++) {
							((lectures.search(tempStringList[i])).peopleList).add(tempStudent);
						
						}
					}
	
					}
					break;
				

				
			case 3:
				System.out.println("Enter UCF ID: ");
				id = scan.nextLine();
				i = searchId(peopleList, id, instance);
				printSchedule(i, peopleList, "Faculty", instance); 
						
					
					break;
			case 4:
				System.out.println("Enter UCF ID: ");
				id = scan.nextLine();
				i = searchId(peopleList, id, instance);
				if(peopleList.get(i) instanceof Student) {
					if(((Student)(peopleList.get(i))).supervisor != null){
						((Student)(peopleList.get(i))).printTaSchedule();
					}
				}
				else {
					System.out.println("Sorry, there is no TA with this ID");
				}
				System.out.println("\n");
				
					break;
			case 5:
				System.out.println("Enter UCF ID: ");
				id = scan.nextLine();
				i = searchId(peopleList, id, instance);
				printSchedule(i, peopleList, "Student", instance); 
				
					break;
			case 6://Delete a scheduled lecture
 
				System.out.print("Enter the crn of the lecture to delete: ");
				crns = scan.nextLine();
				if(lectures.search(crns) != null) {
					for(int m = 0; m < peopleList.size(); m++) {
						peopleList.get(m).deleteFromSchedule(lectures, crns);
					}	
					lectures.delete(crns);
					deleted = true;
					
				}
				else {
					System.out.println("Sorry, the crn you put does not exist!\n");
				}
				
					break;
				
			case 7:

				if(deleted == true) {
					System.out.print("You have made a deletion of at least one lecture. Would you like to\n"
							+ "print the copy of lec.txt? Enter y/Y for Yes or n/N for No: ");	
				do {
					try {
						goAgain = false;
						flag = 0;
						input = scan.nextLine();
						if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n")){
							flag = 1;
						}
						if(flag == 0) {
							throw new Exception();
						}
					}catch (Exception e){
						System.out.print("Please enter either y or n");
						goAgain = true;
					}
				}while(goAgain);
				if(input.equalsIgnoreCase("n")){
					lectures.fileWriting(path, "n");
				}
				else if (input.equalsIgnoreCase("y")) {
					String[] newPath = new String[1];
					newPath[0] = fileCopy(path);
					lectures.fileWriting(newPath, "y");
					
				}
				System.out.println("Your files have been saved!\n Bye!\n");
				}
				else {
					System.out.println("Bye!");
				}
				ask = false;
				break;
				
			}
		}
		
		
	}
		
	}	//END OF MAIN CLASS
	
	private static String fileCopy(String[] path) {
		//we separate the path
		String[] separatePath = null;
		String newPath = null;
		String op = null;
		//if the first item is a /, its mac
		//if not, its windows \\
		
		//find out, then rewrite in way to have the last thing written as "lecUpdated.txt";
				//geting crns
		try {
			
			if(path[0].charAt(0) == '/') {
				op = "m";
				separatePath = path[0].split("/");
			}
			else {
				op = "w";
				separatePath = path[0].split("\\\\");
			}
			
		}catch(Exception e) {
			System.out.print("Sorry, we could not recognize the path\n");
		}
		
		
			if(op != null) {
				
				if(op.equals("w")) {
					newPath = separatePath[0];
					for(int i = 0; i < separatePath.length; i++) {
						if(i == 0 ) {
							newPath = separatePath[i];
						}
						else if(i == separatePath.length - 1){
							newPath += "\\\\";
							newPath += "lecUpdated.txt";
						}
						else {
							newPath += "\\\\";
							newPath += separatePath[i];
						}
					}
				}
				else if(op.equals("m")){
					//newPath = "/";
					for(int i = 0; i < separatePath.length; i++) {
						if(i == 0 ) {
							newPath = "/";
						}
						else if(i == separatePath.length - 1){
							newPath += "lecUpdated.txt";
						}
						else {
							newPath += separatePath[i];
							newPath += "/";
						}
					}
				}
						
			}
			return newPath;
				
					
	}
	
	
	private static void printSchedule(int i, LinkedList <People> peopleList, String instance, String[] inCheck) {

		if(i > -1) {
			if(inCheck[0].equalsIgnoreCase(instance)) {	
			System.out.println(peopleList.get(i));
			peopleList.get(i).printSchedule();
			System.out.println("\n");
			}
			else {
				System.out.println("Sorry ID isn't of type " + instance + " going back to menu...\n\n" );
			}
			
		}
		else {
			System.out.println("Sorry ID not found!\n");
		}
	}
		
	
	private static int searchId(LinkedList <People> peopleList, String id, String[] instance) {
		for(int i = 0; i < peopleList.size(); i++) {
			if(id.equals(peopleList.get(i).id)) {
				if(peopleList.get(i) instanceof Faculty) {
					instance[0] = "Faculty";
				}
				else {
					instance[0] = "Student";
				}
				//System.out.println(i + " this is i that we are returning\n\n");
				return i;
			}
		}
		instance[0] = "New";
		return -1;
		
	}
	
	public static Lecture searchCrn(LecList list, String crn) {
		int num = -1;
		Lecture[] thisList = list.getList();
		int size = (list.getList()).length;
		for(int b = 0; b < size; b++) {
			if (((list.getList())[b]).getCrn().equals(crn)) {
				return (list.getList())[b];
			}	
		}
		return null;

		
	}
		
	
	private static String idInput(String id, Scanner scan, boolean goAgain) {
		do {
			try {
				id = scan.nextLine();
				goAgain = false;
				if(id.length()!= 7 || !id.matches("\\d+")) {
					throw new IdException();
				}
			}catch (IdException e) {
				goAgain = true;
			}
			
		}while(goAgain);
		
		return id;
	}
	
		
		
	//opens file and gets lectures
	private static LecList readFile(String[] path) {
		
		//tries to find file, if there return
		//if not the null
		String input, lab = "";
		int num = 0;
		
		ArrayList<String> lb = new ArrayList<String>();//labs will be here
		LecList lectures = new LecList();//lectures read will be here
		
				
		
		try {
			
			File f = new File(path[0]);
			Scanner file = new Scanner(f);
			
			while(file.hasNextLine()) {
				if (num == 1) {
				input = lab;
				}
				else {
				input = file.nextLine();
				}
				
				if(check(input) > 0) {
					lectures.add(new Lab(input));
					//check for labs
					while(file.hasNextLine()) {
						lab = file.nextLine();
						if(check(lab) == 6) {
							lb.add(lab);
							num = 0;
						}
						else {
							num = 1;
							break;
						}	
					}
				
					lectures.labAdding(lb);
					lb = new ArrayList<String>();
					
				}
				else if(check(input) < 0) {
					num = 0;
					lectures.add(new NoLab(input));
				}
				else {
					num = 0;
					lectures.add(new Online(input));
				}
				
				
			}
			file.close();
		} catch (FileNotFoundException e) {
			System.out.print("Sorry no such file.\n");
			return null;
						
		}
		
	
		return lectures;
	}
	
	//Checks to see the instance we'll be creating
		public static int check(String input) {
			String[] items;
			items = input.split(",");
			if (items.length >= 5) {
				if (items[4].equalsIgnoreCase("Online")) {
					return 0;
				}
				else if(items.length >= 7) {
					if (items[6].equalsIgnoreCase("Yes")) {
						return 1;
					}
				}
				
			}
			else if (items.length == 2)
			{
				return 6;
			}
			
			return -1;
		}
		
		public static int getInt(Scanner scan) {
			int input = 0;
			boolean goAgain = false;
			
			
			do {
				
				try {
					goAgain = false;
					input = scan.nextInt();
					
				}catch(Exception e){
					System.out.print("Please enter a number: ");
					scan.nextLine();
					goAgain = true;
				}
			}while(goAgain);
			return input;
		}

		
		
}	//END OF MAIN METHOD


class IdException extends Exception{
	public IdException() {
		System.out.print("Sorry incorrect format. (Ids are 7 digits): ");
	}
}
	








//id exception here
abstract class Lecture{
	private String crn, prefix, title, unGra, modality, location;
	LinkedList <People> peopleList;

	
	public Lecture(String input) {
		String[] items;
		items = input.split(",");
		
		if (items.length >= 5){
			crn = items[0];
			prefix = items[1];
			title = items[2];
			unGra = items[3];
			modality = items[4];
		}
		
		peopleList = new LinkedList<>();
		
	}
	
	public void deleteClass(String crns) {
		// TODO Auto-generated method stub
		
	}

	abstract public String sort(String classroom);
	abstract public void addLab(ArrayList<String> list);
	public void deleteLabs() {}
	
	
	public String toString() {
		return crn + "," + prefix + "," + title + "," + unGra + "," + modality;
	}
	
	
	public String getCrn() {
		return crn;
	}
	public void setCrn(String crn) {
		this.crn = crn;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUnGra() {
		return unGra;
	}
	public void setUnGra(String unGra) {
		this.unGra = unGra;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}
class Online extends Lecture{
	
	public Online(String input) {
		super(input);
	}
	public String sort(String classroom) {
		return null;
	}
	public void addLab(ArrayList<String> list) {}
	
}
class NoLab extends Lecture{
	private String location, lab;
	
	
	public NoLab(String input) {
		super(input);
		String[] items;
		items = input.split(",");
		
		if (items.length >= 7) {
			location = items[5];
			lab = items[6];
		}
	}
	
	public String toString() {
		return super.toString() + "," + location + "," + lab;
	}
	
	public String sort(String classroom) {
		return null;
	}
	public void addLab(ArrayList<String> list) {}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLab() {
		return lab;
	}
	public void setLab(String lab) {
		this.lab = lab;
	}
}
class Lab extends NoLab {
	private String[] crnLab, locLab;
	
	public Lab(String input) {
		super(input);
		}
	
	public void addLab(ArrayList<String> list) {
		int size = list.size();
		String[] items;
		
			crnLab = new String[size];
			locLab = new String[size];
			
			for (int i = 0; i < size; i++) {
				crnLab[i] = null;
				locLab[i] = null;
			}
		
		
		for(int i = 0; i < size; i++) {
			items = list.get(i).split(",");
			crnLab[i] = items[0];
			locLab[i] = items[1];
			
		}
		
	}
	
	@Override
	public String sort(String crn) {
		String crnnn = null;
		//System.out.println("the lenght is " +crnLab.length);
		for(int i = 0; i < crnLab.length; i++) {
			
			if (crnLab[i] != null && (crnLab[i].equalsIgnoreCase(crn))) {
	
					crnnn = crnLab[i];
					crnnn += ",";
					crnnn += locLab[i];
			}
	
		}
		return crnnn;
				
	}
	
	
	public String[] getCrnLab() {
		return crnLab;
	}
	public void setCrnLab(String[] crnLab) {
		this.crnLab = crnLab;
	}
	public String[] getLocLab() {
		return locLab;
	}
	public void setLocLab(String[] locLab) {
		this.locLab = locLab;
	}
	public String labs() {
		String labs; int i = 0;
		labs = crnLab[i] + "," + locLab[i];
		i++;
		
		for(int j = i; j < crnLab.length; j++) {
			if(crnLab[j] != null) {
			labs += "\n" + crnLab[j] + "," + locLab[j];
			}	
		}
				
		return labs;
	}
	
	public String crnLabPrint(int index) {
		return crnLab[index];
	}
	
	
	@Override
	public String toString() {
		return labs();
	}
	
	
	
}
abstract class People{
	public String name;
	public String id;
	LinkedList <Lecture> list = new LinkedList<>();
	
	public People(String id, String name, LinkedList <Lecture> list) {
		this.name= name;
		this.id = id;
		this.list = list;
	}
	
	public void deleteClass(String crn) {
		int size = list.size();
		int index = 0;
		for(int i = 0; i < size; i++) {
			if(list.get(i).getCrn().equals(crn)) {
				index = i;
				break;
				
			}
		}
		System.out.println("removing "+ list.get(index).getCrn());
		list.remove(index);
		}

	
	public void printSchedule() {
		if(list.size() == 0) {
			System.out.println("There are no lectures\n");
		}
		else {
		for(int i = 0; i < list.size(); i++) {
			
			if(list.get(i) instanceof Online ) {
				System.out.println("[" + list.get(i).getPrefix() + "/" + list.get(i).getTitle() + "][Online]\n");
			}
			else if (list.get(i) instanceof Lab) {
				System.out.println("[" + list.get(i).getCrn() + "/" +  list.get(i).getPrefix() + "/" +
						list.get(i).getTitle() + " with Labs: \n" + list.get(i) + "\n");
			}
			else {
				System.out.println("[" + list.get(i).getCrn() + "/" +  list.get(i).getPrefix() + "/" +
						list.get(i).getTitle() + "]\n" );
			}
			
		}
		}
	}
	
	public void deleteFromSchedule(LecList lecture, String crn) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getCrn().equalsIgnoreCase(crn)) {
				list.remove(i);
			}
		}
	}

	
	
}
class Faculty extends People{
	String rank, officeLocation;
	String type;
	
	public Faculty(String id, String name, LinkedList <Lecture> list, String rank, String location) {
		super(id, name, list);
		this.rank = rank;
		this.officeLocation = location;
		this.type = "faculty";


	}
	
	public String toString() {
		return name + " is teaching the following lectures: \n";
	}
	
	
	
	
}

class Student extends People{
	String type;
	String supervisor = null, degree;//make sure that the advisor doesn't teach a class
	LinkedList <String> studentLabList = new LinkedList<>();
	LinkedList <String> taLabList = new LinkedList<>();
	
	public Student(String id, String name, LinkedList <Lecture> list, String type) {
		super(id, name, list);
		chooseStudentLab(list);
		this.type = type;

	}
	
	public void makeTA(String supervisor, String degree, LinkedList <String> taLabList) {
		this.taLabList = taLabList;
		this.supervisor = supervisor;
		this.degree = degree;

	}
	
	public void chooseStudentLab(LinkedList <Lecture> list) {
		Random rand = new Random(); 
	
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i) instanceof Lab) {
				
				int int_random = rand.nextInt(((Lab)(list.get(i))).getCrnLab().length);
				String crn = ((Lab)(list.get(i))).crnLabPrint(int_random);
				studentLabList.add(crn);
				
				System.out.println("\n[" + name + "]" + " is added to lab: " + crn);
			}
		}
			
	}
	
	
	public boolean chooseTALab(String crn, LecList lectures, String labCrn) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getCrn().equalsIgnoreCase(crn)) {
				System.out.println("Sorry, this TA has this class in their schedule already");
				return false;
			}
		}
		
		if(lectures.searchLab(labCrn) != null) {
			taLabList.add(labCrn);
		}
		
		return true;
		
	}
	
	public void deleteFromSchedule(LecList lectures, String crn) {
		if(lectures.search(crn) instanceof Lab) {
			String[] crns = ((Lab)(lectures.search(crn))).getCrnLab();
				
			if(!studentLabList.isEmpty()){
				for(int i = 0; i < crns.length; i++) {
					for(int j = 0; j < studentLabList.size(); j++) {
						if(crns[i].equals(studentLabList.get(j))){
							studentLabList.remove(j);
						}
					}
				}
			}
			
			if(!taLabList.isEmpty()) {
				for(int i = 0; i < crns.length; i++) {
					for(int j = 0; j < taLabList.size(); j++) {
						if(crns[i].equals(taLabList.get(j))){
							taLabList.remove(j);
						}
					}
				}
			}
		}
		super.deleteFromSchedule(lectures, crn);
		
	}
	
	public int findClass(String crn) {
		
		//System.out.println("Searching...\n");
		for(int i = 0; i < list.size();) {
			if(list.get(i).getCrn().equals(crn)) {
				//System.out.println("looking at " + list.get(i).getCrn() + " is it the same as " + crn );
				//System.out.println("returning " + i);
				return i;
			}
			i++;
		}
		
		return -1;
	}
	
	
	
	
	public String toString() {
		return name + " is enrolled in the following lectures: \n";
	}
	
	public void printSchedule() {		
		
		int j = 0;
		
		if(list.size() == 0) {
			System.out.println("There are no lectures\n");
		}
		else {
			for(int i = 0; i < list.size(); i++) {
				
				if(list.get(i) instanceof Online ) {
					System.out.println("[" + list.get(i).getPrefix() + "/" + list.get(i).getTitle() + "][Online]\n");
				}
				else if (list.get(i) instanceof Lab) {
					System.out.println("[" + list.get(i).getPrefix() + "/" + list.get(i).getTitle() + 
							"/" + "[Lab: " + studentLabList.get(j)+ "]");
					j++;
				}
				else {
					System.out.println("[" + list.get(i).getCrn() + "/" +  list.get(i).getPrefix() + "/" +
							list.get(i).getTitle() + "]\n" );
				}
				
			}
		}
		
		
			
	}
		
	public void printTaSchedule() {
		if(!taLabList.isEmpty()) {
			System.out.println(name + " is a TA at the following labs:\n");
			for(int i = 0; i < taLabList.size(); i++) {
				System.out.println(taLabList.get(i));
			}
		}
		else {
			System.out.println("This student isn't a TA\n");
		}
	}

}


//lectures will be stored here
class LecList {
	private static Lecture[] list;
	static int lecSize = 0;
	
	public LecList() {
		list = new Lecture[1000];
		for (int i = 0; i < 1000; i++) {
			list[i] = null;
		}
	}
	public void printList() {
		for(int i = 0; i < list.length; i++) {
			System.out.println(list[i].getCrn());
		}
	}
	public static Lecture[] getList() {
		return list;
	}
	public void setList(Lecture[] list) {
		this.list = list;
	}
	public void add(Lecture type) {
		
		for(int i = 0; i < 1000; i++) {
			if (list[i] == null) {
				list[i] = type;
				lecSize++;
				break;
			}
		}
	}
	public Lecture search(String crn) {
		
		String temp;
		for(int i = 0; i < lecSize; i++) {
		
			if(list[i] != null) {
			temp = list[i].getCrn();
			//System.out.println(list[i]);
			
			
			if (temp.equalsIgnoreCase(crn) ) {
				return list[i];
			}
			}
		}
		
		return null;
		
	}
	
	public String searchLab(String crn) {
		String comp = null;
		
		for(int i = 0; i < lecSize; i++) {		
			String temp = list[i].getCrn();
		
			if(list[i] instanceof Lab && !temp.equalsIgnoreCase(crn)) {
				comp = list[i].sort(crn);				
				return comp;
			}
		}
		return comp;
	}
	
	public void labAdding(ArrayList<String> labs){
		for(int i = 0; i < 1000; i++) {
			if (list[i] == null && i > 0)
			{
				list[i-1].addLab(labs);
				break;
			}
		}
	}
	
	public void delete(String crn){
		for(int i = 0; i < lecSize; i++) {
			
			if(list[i] != null) {
			String temp = list[i].getCrn();
			
			if (temp.equalsIgnoreCase(crn) ) {
				System.out.println("[" + list[i].getCrn()+ "/" + list[i].getPrefix() + "/" + list[i].getTitle() + "]" + " Deleted");
				list[i] = null;
				for(int j = i; j < lecSize; j++) {
					if(list[j+1] != null) {
					list[j] = list[j+1];
					}
				}
				break;
				
				}
			}
		}
	}
	
	public void fileWriting(String[] period, String yn) {
	
		
			String quick = null;
			try {
				File f = new File(period[0]);
				
					f.createNewFile();
				
				FileWriter file = new FileWriter(f);
				//System.out.print(period[0]);
				
				
				
				for(int i = 0; i < lecSize; i++) {
					if(list[i] != null) {
						if(list[i] instanceof Lab) {
							if(((Lab)(list[i])).getCrnLab().length == 0) {
								
							}
							else if(list[i].getCrn() != null){
							quick = list[i].getCrn() + "," + list[i].getPrefix() + ","
								 + list[i].getTitle() + "," + list[i].getUnGra() + "," +  list[i].getModality() + "," +  list[i].getLocation() + ","
									+ "Yes" + "\n" + list[i].toString();
							}
							//System.out.println(list[i].getModality());
							//crn + "," + prefix + "," + title + "," + unGra + "," + modality;
						 
						}
						else {
							quick = list[i].toString();
							//System.out.println(list[i].getModality());
						}
						
						//System.out.println(list[i]);
						file.write(quick);
						if(i != lecSize - 1) {
						file.write("\n");
						}
					}
				}
				
				file.close();				
			
			}catch (IOException e) {
				System.out.println("Didn't work\n");
			}
			
		
	}
		
	
}
 





