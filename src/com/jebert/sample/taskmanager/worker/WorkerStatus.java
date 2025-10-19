package com.jebert.sample.taskmanager.worker;

/**
 * Class WorkerStatus
 * @author Jim Ebert
 *
 * This class manages all available worker statuses.  It does not manage the business rules for allowable 
 * changes between statuses.
 * 
 */

public class WorkerStatus {
	public static final WorkerStatus IDLE = new WorkerStatus(0, "Idle");
	public static final WorkerStatus WORKING = new WorkerStatus(10, "Working");
	
	private int value;		//Numeric value for comparing statuses if needed
	private String name;	//Descriptive value for the status for output
		
	/**
	 * Constructor to create a WorkerStatus
	 * 
	 * @param l_value
	 * @param l_status
	 */
	private WorkerStatus(int l_value, String l_status)
	{
		this.value = l_value;
		this.name = l_status;
	}
	
	/**
	 * Get the name of the status
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

