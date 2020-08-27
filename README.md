## Process Scheduling Simulation
### (Assignment for an Operating System class)

### Description

Simulates the SJF, RR, SRTF and FCFS algorithms for Process Scheduling. Takes as input a text file (the format, specified in the assignment, is shown in the sample files in input_files) specifying the number of CPUs as well as information relevant to the processes to be scheduled: processID, arrivalTime, totalExecTime, IO_RequestAtTime. An arbitrary number of IO requests can be given for a given process.

The output is in text format and is directed to the console as well as an output file specified by the user. It consists in detailed timelines showing the activity occurring in each CPU for every unit of time. The timelines show the number of the process being processed or a "__" if the device was idle. The status of the ready-queue and the IO processing queue is also shown by listing the processes waiting in the queue for each time unit. 

Statistics for each CPU or process are also shown, as well as averages: 
- CPU usage
- Wait time
- Turnaround time
- Response time

NOTES: 
- For better output legibility, read the output file with non-wrapping lines.
- Java PriorityQueue class does not guarantee ordering of elements of equal priority. That is why elements of equal priority may switch positions from one time unit to next, as modifications are made to the queue. 

### Design notes

See the UML graph for insight into the design.

Although the program uses many features of OOP, the programming style tilts more towards a procedural style. Usual OO principles such as encapsulation were not rigourously followed and classes are used more as C structs.