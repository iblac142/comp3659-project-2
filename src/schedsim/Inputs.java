package schedsim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.NumberFormatException;

/**
 * Class which handles input for the simulator
 */
public class Inputs {
	static int MAX_NUM = Integer.MAX_VALUE;
	
	/**
     * Generic Positive Input 
     * continually reprompts until an acceptable input is given
     *
     * @param in		the scanner to use as input 
     *                      
     * @param prompt	the prompt to display to output
     * 
     * @param min		the minimum acceptable value
     * 					if -1, defaults to 0
     * 
     * @param max		the maximum acceptable values
     * 					if -1, defaults to the maximum integer value
     * 
     * @return the integer that the user inputs
     */
	public int intInput(Scanner in, String prompt, int min, int max) {
		System.out.println(prompt);
		
		if (min < 0) {
			min = 0;
		} else if (max < 0) {
			max = MAX_NUM;
		}
		
		int num = -1;
		while (num < 0) {
			try {
				num = Integer.parseInt(in.nextLine());
				if (num > max || num < min) {
					System.out.println("Inputted number is out of bounds. Try again:");
					num = -1;
				}
			} catch (NumberFormatException e) {
				System.out.println("Input is invalid. Try again:");
				num = -1;
			}
		}
		
		return num;
	}
	
	/**
     * Prompts user to specify a filename which contains the processes
     *
     * @param in				the scanner to use as input 
     *                      
     * @param process_table		the table to populate with processes
     * 
     * @return 0 if successful, -1 if not
     * 
     */
	public int getProcesses(Scanner in, Process[] process_table) {
		System.out.println("Please specify the .csv file containing the processes.");
		
		String filename = in.nextLine();
		File file = new File(filename);
		int process_num = 0;
		int error = 0;
		
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine() && process_num < 128 && error == 0) {
				// get next line from file and split it by the comma separator
				String[] values = scanner.nextLine().split(",");
				// must have odd number of values (priority + burst pairs)
				// can't have 0 values or more than 64 processes
				if (values.length % 2 == 1 && values.length >= 3 && values.length <= 129) {
					int priority = Integer.parseInt(values[0]);
					int[] times = new int[values.length - 1];
					
					// populate times table
					for (int j = 1; j < values.length; j += 1) {
						times[j - 1] = Integer.parseInt(values[j]);
					}
					
					// create process and add it to the table
					Process p = new Process(times, priority);
					process_table[process_num] = p;
					
					process_num += 1;
				} else {
					System.out.println("Process " + process_num + " has an incorrect number of values. Aborting!");
					error = -1;
				}
			}
			if (error == 0 && scanner.hasNextLine()) {
				System.out.println("File contains greater than 128 processes. This is fine, but excess processes will be omitted.");
				error = 1;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Aborting!");
			error = -1;
		} catch (NumberFormatException e) {
			System.out.println("Process " + process_num + " has an invalid value. Aborting!");
			error = -1;
		}
		if (error != -1 && process_table[0] == null) {
			System.out.println("File is empty. Aborting!");
			error = -1;
		}
		if (error == 1) {
			error = 0;
		}
		return error;
	}
}
