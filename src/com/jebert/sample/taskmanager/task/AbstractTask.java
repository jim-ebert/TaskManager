package com.jebert.sample.taskmanager.task;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class AbstractTask
 * @author Jim Ebert
 *
 * This class is the container for managing most of the functionality of a single task.  It tracks a task's duration, its relationship to other tasks, status, and
 * completes the task by calling workTask on the concrete class.
 * 
 */

public abstract class AbstractTask implements Task{
	private String name;
	private long duration;
	private ArrayList<Task> predecessors = new ArrayList<Task>();
	private ArrayList<Task> successors = new ArrayList<Task>();
	private TaskStatus status;	
	
	/**
	 * Constructor - create a task with a name and duration
	 * @param l_name
	 * @param l_duration
	 */
	public AbstractTask(String l_name, long l_duration)
	{
		this.name = l_name;
		this.duration = l_duration;
		this.status = TaskStatus.NOT_STARTED;
	}
	
	/**
	 * Return the name of the task
	 * @return
	 */
	public final String getName()
	{
		return this.name;
	}
	
	/**
	 * Return the length of time it takes to complete this task
	 * @return
	 */
	public final long getDuration()
	{
		return this.duration;
	}
	
	/**
	 * Return the current status of the task
	 * @return
	 */
	public final TaskStatus getStatus()
	{
		if(this.status == TaskStatus.NOT_STARTED && this.isPredecessorsComplete())
			this.status = TaskStatus.READY_TO_START;
		
		return this.status;
	}
	
	/**
	 * Set the status of the task.  Tasks can only move forward in status.
	 * @param newStatus
	 * @return
	 */
	public final boolean setStatus(TaskStatus newStatus)
	{
		if(newStatus.getValue() > this.status.getValue())
			this.status = newStatus;
		else
			return false;
		
		return true;
	}
	
	/**
	 * Return the list of successors (Tasks dependant upon this one)
	 * @return
	 */
	public final ArrayList<Task> getSuccessors()
	{
		return this.successors;
	}
	
	/**
	 * Return the list of predecessors (Tasks that this one is dependant upon)
	 * @return
	 */
	public final ArrayList<Task> getPredecessors()
	{
		return this.predecessors;
	}
	
	/**
	 * Return the number of successors
	 * @return
	 */
	public final int getNumberOfSuccessors()
	{
		return this.successors.size();
	}
	
	/**
	 * Return the number of predecessors
	 * @return
	 */
	public final int getNumberOfPredecessors()
	{
		return this.predecessors.size();
	}
	
	/**
	 * Add a predecessor 
	 * @param dependency
	 * @return
	 */
	public final boolean AddDependency(Task dependency)
	{
		boolean addPred = this.predecessors.add(dependency);
		boolean addSuc = dependency.AddSuccessor(this);
		
		return(addSuc && addPred);
	}
	
	/**
	 * Add a successor
	 * @param successor
	 * @return
	 */
	public final boolean AddSuccessor(Task successor)
	{
		boolean addSuc = this.successors.add(successor);
		return addSuc;
	}
	
	/**
	 * Complete the task
	 */
	public abstract void workTask();		

	/**
	 * Return if all dependencies are completed so this task can now be run.
	 * @return
	 */
	private boolean isPredecessorsComplete()
	{
		Iterator<Task> iTaskIterator = this.predecessors.iterator();
			
		while(iTaskIterator.hasNext())
		{
			Task curTask = (Task)iTaskIterator.next();			
			if (curTask.getStatus() != TaskStatus.COMPLETED)
				return false;
		}
		
		return true;
	}
}
