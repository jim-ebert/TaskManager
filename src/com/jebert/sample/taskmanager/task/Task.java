package com.jebert.sample.taskmanager.task;

import java.util.ArrayList;

/**
 * Interface Task
 * @author Jim Ebert
 *
 * This interface is the contract with all objects wanting to implement or use a task.  
 * 
 */
public interface Task {
	public String getName();
	public long getDuration();
	public TaskStatus getStatus();
	public boolean setStatus(TaskStatus newStatus);
	public ArrayList<Task> getSuccessors();
	public ArrayList<Task> getPredecessors();
	public int getNumberOfSuccessors();
	public int getNumberOfPredecessors();
	public boolean AddDependency(Task dependency);	
	public boolean AddSuccessor(Task successor);	
	public void workTask();
}
