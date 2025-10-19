package com.jebert.sample.taskmanager.worker;

import com.jebert.sample.taskmanager.task.Task;

/**
 * Class SimpleWorker
 * @author Jim Ebert
 *
 * This class manages one full route of a task, including its successors.  Each worker is a unique thread, and may invoke other 
 * workers if successor tasks are needing to be run in parallel.
 * 
 */

public class SimpleWorker extends AbstractWorker {

	/**
	 * Constructor for a worker.
	 * 
	 * @param l_name
	 * @param wp
	 */
	public SimpleWorker(String l_name, WorkerPool wp)
	{
		super(l_name, wp);
	}
	
	/**
	 * Receive a new assignment for this worker - If there are any criteria that should manage assignment, override here.  
	 * If it isnt an allowable assignment, throw IllegalAssignment 
	 * 
	 * @param assignment
	 * @throws IllegalAssignment 
	 */
	public void assignTask(Task assignment) throws IllegalAssignment {		
		super.assignTask(assignment);
	}
	
}
