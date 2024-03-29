package com.gonulcei.assignment.q2.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.gonulcei.assignment.q2.exception.UserAlreadyExistsException;
import com.gonulcei.assignment.q2.model.User;
import com.gonulcei.assignment.q2.service.UserService;

/*
 * This is class here acts as an interface between the user and the services
 */
public class UserUtil {
	
	static Scanner sc = new Scanner(System.in);
	
	/*
	 * Object of userService, basically to call the services of users
	 */
	static UserService userService = new UserService();
	
	/*
	 * This method shows the main menu
	 */
	public static void showMenu() {
		System.out.println("------------------------Menu--------------------------");
		System.out.println("1. Add User Details");
		System.out.println("2. Display User Details");
		System.out.println("3. Delete User Details");
		System.out.println("4. Save User Details in Memory");
		System.out.println("5. Exit");
	}
	
	/*
	 * when someone wants to add a user, this method takes input for the
	 * courses of the user.
	 */
	public static List<String> getUserCoursesInput(){

		List<String> allCourses = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F"));
		System.out.println("Choose any 4 courses out of 6 [A, B, C, D, E, F]");
		List<String> courseList = new ArrayList();
		int i = 4;
		sc.nextLine();
		while(i>0) {
			String course = sc.nextLine().toUpperCase();
			if(allCourses.contains(course) && !courseList.contains(course)) {
				i--;
				courseList.add(course);
				if(i>0)
					System.out.println(i + " more to go..");
			}
			else
				System.out.println("You have already taken the course / Invalid course input.. Please try Again");
		}
		return courseList;
	}
	
	/*
	 * This method prints the userList, it takes the userList as parameter
	 * and print each user. User toString method is overridden in the User Class
	 */
	public static void printUserDetails(List<User> userList) {
		System.out.println("-----------------------------------------------------------------------------------------");
		String heading = String.format("%1$-15s %2$-15s %3$-15s %4$-20s %5$-10s", "Name", "Roll Number", "Age", "Address", "Courses");
		System.out.println(heading);
		System.out.println("-----------------------------------------------------------------------------------------");
		for(User user: userList) {
			System.out.println(user);
		}	
		System.out.println();
	}
	/*
	 * when someone wants to add a user, this method takes input for the
	 * user. It includes all the input details with validations
	 */
	public static User getUserInput() {
		User user = new User();
		int validInput = 1;
		System.out.println("Enter the Details of the User");
		sc.nextLine();
		System.out.print("-Fullname ");
		String fullname =  sc.nextLine();
		while(fullname.equals("")) {
			System.out.println("Please try again");
			fullname = sc.nextLine();
		}
		user.setFullname(fullname);
		
		System.out.print("-age [Integer] ");
		try {
			user.setAge(sc.nextInt());
		} catch (Exception e) {
			validInput = 0;
		}
		
		System.out.print("-address ");
		sc.nextLine();
		String address = sc.nextLine();
		while(address.equals("")) {			
			System.out.println("Please try again");
			address = sc.nextLine();
		}
		user.setAddress(address);
		
		System.out.print("-Roll Number [Integer] ");
		try {
			user.setRollNo(sc.nextInt());
		} catch (Exception e) {
			validInput = 0;
		}
		
		user.setCourses(getUserCoursesInput());
		if(validInput == 0) return null;
		return user;
	}
	
	/*
	 * This method shows the menu on which one can sort the user list.
	 * It returns the attribute on which sorting has to be done.
	 */
	private static int sortingMenu() {
		
		int choice =0;
		System.out.println("Select an attribute based on which you wanna sort");
		System.out.println("1. Name");
		System.out.println("2. Roll Number");
		System.out.println("3. Age");
		System.out.println("4. Address");
		try {
			choice = sc.nextInt();
		}catch(Exception e) {
			System.out.println("Invalid Choice");
		}
		if(choice==0)return sortingMenu();
		else return choice;
	}
	
	/*
	 * This is the method that uses to userService object and calls the
	 *  services based on option chosen by the user.
	 */
	private static void performAction(int option) {
		switch(option) {
		case 1:
			User user = null;
			user = getUserInput();
			try {
				if(user==null) 
					System.out.println("Invalid Details. Check values of age and Roll number");
				else userService.addUser(user);
			} catch (UserAlreadyExistsException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			List<User> userList = userService.getUserDetails();
			printUserDetails(userList);
			sc.nextLine();
			System.out.println("Press 's' for sorting options else press any other key to continue");
			char check = sc.nextLine().toLowerCase().charAt(0);
			
			while(check=='s') {
				int choice = sortingMenu();
				sc.nextLine();
				System.out.println("Press --> 'a' for Ascending | 'd' for Descending");
				char sortOrder = sc.nextLine().toLowerCase().charAt(0);
				userList = userService.getSortedUserDetails(choice, sortOrder);
				printUserDetails(userList);
				System.out.println("Press 's' for sorting options else press any other key to continue");
				check = sc.nextLine().toLowerCase().charAt(0);
			}
			break;
		case 3:
			int rollNumber;
			System.out.println("Enter Roll Number of the User to be deleted");
			try {
				rollNumber = sc.nextInt();
				System.out.println(userService.deleteUser(rollNumber));
			}catch(Exception e) {
				System.out.println("Not an Integer");
				return;
			}
			break;
		case 4:
			userService.saveDetailsIntoDisk();
			break;
		case 5:
			sc.nextLine();
			System.out.println("Do you want to save the latest changes [y/n]");
			char ch = sc.nextLine().toLowerCase().charAt(0);
			if(ch=='y') userService.saveDetailsIntoDisk();
			break;
		default:
			System.out.println("Invalid Choice Please Try Again");
		}
	}
	
	/*
	 * This is begins the interaction between the user and system
	 */
	public static void run() {
		userService.bringDataInMemory();
		while(true) {
			showMenu();
			int option = -1;
			try {
				option = sc.nextInt();
				performAction(option);
			} catch (Exception e) {
				System.out.println("Not an Integer");
			}
			if(option==5) break;
		}
	}
	
}
