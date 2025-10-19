package com.jebert.sample.taskmanager.worker;

import com.jebert.sample.taskmanager.task.Task;

/**
 * Interface Worker
 * @author Jim Ebert
 *
 * This interface is the contract with all objects wanting to implement or use a worker.  
 * 
 */
public interface Worker {
	public String getName();
	public void assignTask(Task assignment) throws IllegalAssignment;
	public Task getAssignment();
	public WorkerStatus getStatus();
	public void setStatus(WorkerStatus status);
	public long getDuration();
	public void work();
	public void waitForCompletion();
}
