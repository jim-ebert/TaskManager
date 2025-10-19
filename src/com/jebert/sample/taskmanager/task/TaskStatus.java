package com.jebert.sample.taskmanager.task;

/**
 * Class TaskStatus
 * @author Jim Ebert
 *
 * This class manages all available task statuses.  It does not manage the business rules for allowable 
 * changes between statuses.
 * 
 */

public class TaskStatus {
	public static final TaskStatus NOT_STARTED = new TaskStatus(0, "Not Started");
	public static final TaskStatus READY_TO_START = new TaskStatus(10, "Ready to Start");
	public static final TaskStatus ASSIGNED = new TaskStatus(15, "Assigned");
	public static final TaskStatus IN_PROGRESS = new TaskStatus(20, "In Progress");
	public static final TaskStatus COMPLETED = new TaskStatus(30, "Completed");
	
	private int value;		//Numeric value for comparing statuses if needed
	private String name;	//Descriptive value for the status for output
		
	/**
	 * Constructor of a TaskStatus
	 * 
	 * @param l_value
	 * @param l_status
	 */
	private TaskStatus(int l_value, String l_status)
	{
		this.value = l_value;
		this.name = l_status;
	}
	
	/**
	 * get the descriptive name of status
	 *  
	 * @return
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Get the numeric value of the status
	 * 
	 * @return
	 */
	public int getValue()
	{
		return this.value;
	}
}
