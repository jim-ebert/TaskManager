package com.jebert.sample.taskmanager.task;

import java.util.ArrayList;
import java.util.Iterator;

import com.jebert.sample.taskmanager.worker.IllegalAssignment;
import com.jebert.sample.taskmanager.worker.Worker;
import com.jebert.sample.taskmanager.worker.WorkerPool;

/**
 * Class TaskManager
 * @author Jim Ebert
 *
 * This class is the manager of all tasks.  It is responsible for managing the worker pool, the task list,
 * and aggregating the time it took to complete all the tasks
 */

public class TaskManager {

	private ArrayList<Task> tasks;
	private long timeElapsed;
	private WorkerPool workPool;
	
	/**
	 * Constructor with a list of tasks to complete.  Use unlimited number of workers
	 * @param l_tasks
	 */
	public TaskManager(ArrayList<Task> l_tasks)
	{
		this.tasks = l_tasks;
		this.workPool = new WorkerPool();
	}
	
	/**
	 * Alternate constructor - set the maximum number of workers available 
	 * @param l_tasks
	 * @param maxWorkers
	 */
	public TaskManager(ArrayList<Task> l_tasks, int maxWorkers)
	{
		this.tasks = l_tasks;
		this.workPool = new WorkerPool(maxWorkers);
	}
		
	/**
	 * Run through all tasks
	 */
	public void run()
	{ 	
		//Wrap in a shell task
		Task shell = new SimpleTask("Shell", 0);
		
		//Make every task dependent upon the Shell task
		Iterator<Task> it = this.tasks.iterator();
		while(it.hasNext())
		{
			Task curTask = (Task)it.next();
			curTask.AddDependency(shell);
		}
		
		this.tasks.add(shell);
		
		//Run the shell task, which will recursively call all the sub-tasks
		Worker w = this.workPool.getWorker();				

		if (w != null)
		{			
			try {
				w.assignTask(shell);
			} catch (IllegalAssignment e) {
				e.printStackTrace();
				//Future - reassign to another resource
			}						
			w.work();
			w.waitForCompletion();
			this.timeElapsed = w.getDuration();
		}				
	}

	/**
	 * Return the number of workers that were needed to complete the tasks
	 * @return
	 */
	public int getWorkerCount()
	{
		return this.workPool.getPoolSize();
	}
	
	/**
	 * Return the total time taken to complete the tasks
	 * @return
	 */
	public double getTimeElapsed()
	{
		return this.timeElapsed;
	}
}

