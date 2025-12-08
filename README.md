# comp3659-project-2
 COMP 3659 Operating Systems Fall 2025
 
This project is a simulation that simulates the effectiveness of various scheduling algorithms.

This project is written in Java.

First, the user is prompted to select an algorithm. 
* Any input other than 0, 1, 2, or 3 is not accepted, and the user will be reprompted.
* (0: FIFO, 1: Simple Priority, 2: Shortest Job First, 3: Pre-Emptive Shortest Job First)
* (Round Robin was not implemented due to time constraints.)

Then, the user is prompted to input the location of the csv file that contains the processes to run the algorithm on.
The each line of the file corresponds to a single process, and must be formatted in the folowing manner:

```priority,arrival_time,cpu_burst_length1,io_burst_length1,cpu_burst_length2,...```

Where:
* priority is the assigned priority of the process, where a lower number is higher priority
* arrival_time is the time at which the process arrives in the ready queue for the first time
* cpu_burst_length1 is the length of the process' first cpu burst
* io_burst_lengths and cpu_burst_lengths (except the first) must come in pairs and specify additional io and cpu burst lengths for the process

Requirements:
* The specified file must exist and not be empty
* The file must be in csv format with no spaces
* priority, arrival_time, and cpu_burst_length1 are required for each process
* io_burst_length/cpu_burst_lengths must come in pairs
* arrival_time and all io_burst_lengths must be integers greater than or equal to 0
* all cpu_burst_lengths must be integers greater than 0
* A process may not have greater than 64 cpu_burst_lengths
* The file must not contain greater than 128 processes
* Lines may not be empty

For SJF and PE SJF, the user is prompted to input a weighting co-efficient value.
* Any input that isn't an integer from 0 to 100 is not accepted, and the user will be reprompted.

For SJF and PE SJF, the user is prompted to input a starting guess value.
* Any input that isn't an integer from 0 to the maximum possible int value is not accepted, and the user will be reprompted.

Known Issues:
* Round Robin is not implemented.
* The number of scheduling decisions made statistic is not correct in many cases when using Pre-Emptive Shortest Job First. (It is typically greater than other algorithms, but not necessarily the exact correct value)
* Unintended behaviour if the total amount of time taken by the simulator exceeds the integer limit
