package schedsim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.NumberFormatException;

public class Inputs {
	
	// this is bad i need to improve this
	Inputs() {
		
	}
	
	int getProcesses(Process[] process_table) {
		Scanner in = new Scanner(System.in);
		
		System.out.println("Please specify the .csv file containing the processes.");
		
		String filename = in.nextLine();
		File file = new File(filename);
		int process_num = 0;
		int error = 0;
		
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine() && process_num < 128 && error == 0) {
				String[] values = scanner.nextLine().split(",");
				if (values.length % 2 == 1 && values.length >= 3) {
					int priority = Integer.parseInt(values[0]);
					int[] times = new int[values.length - 1];
					
					for (int j = 1; j < values.length; j += 1) {
						times[j - 1] = Integer.parseInt(values[j]);
					}
					
					Process p = new Process(times, priority);
					process_table[process_num] = p;
					
					process_num += 1;
				} else {
					System.out.println("Process " + process_num + " has an incorrect number of values. Aborting!");
					error = 1;
				}
			}
			if (scanner.hasNextLine()) {
				System.out.println("File contains greater than 128 processes. This is fine, but excess processes will be omitted.");
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Aborting!");
		} catch (NumberFormatException e) {
			System.out.println("Process " + process_num + " has an invalid value! Aborting!");
		}
		
		in.close();
		return 0;
	}
}
