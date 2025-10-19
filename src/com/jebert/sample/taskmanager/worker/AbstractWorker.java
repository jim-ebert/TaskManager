package com.jebert.sample.taskmanager.worker;

import java.util.ArrayList;
import java.util.Iterator;

import com.jebert.sample.taskmanager.task.Task;
import com.jebert.sample.taskmanager.task.TaskStatus;

/**
 * Class AbstractWorker
 * @author Jim Ebert
 *
 * This class manages one full route of a task, including its successors.  Each worker is a unique thread, and may invoke other 
 * workers if successor tasks are needing to be run in parallel.
 * 
 * This is an abstract class that allows concrete workers to determine whether worker is able to accept the assignment
 * 
 */

public abstract class AbstractWorker implements Runnable, Worker{
	private String name;			//Name of the worker
	private Task assignedTo;		//Current assigned Task
	private WorkerStatus status;	//Status of the worker
	private long duration;			//Aggregate total of this workers time worked
	private Thread runner;			//Thread for running
	private WorkerPool pool;		//Pool that this worker belongs to
	
	/**
	 * Constructor for a worker.
	 * 
	 * @param l_name
	 * @param wp
	 */
	public AbstractWorker(String l_name, WorkerPool wp)
	{
		this.status = WorkerStatus.IDLE;
		this.name = l_name;
		this.runner = new Thread(this);
		this.pool = wp;
	}
	
	/**
	 * Get the name of the worker
	 * 
	 * @return
	 */
	public final String getName()
	{
		return this.name;
	}
	
	/**
	 * Receive a new assignment for this worker
	 * 
	 * @param assignment
	 */
	public void assignTask(Task assignment) throws IllegalAssignment
	{
		this.status = WorkerStatus.WORKING;
		this.assignedTo = assignment;		
		boolean validStatus = this.assignedTo.setStatus(TaskStatus.ASSIGNED);
		
		if(!validStatus)
			System.out.println("Assignment failed");		
	} 
	
	/**
	 * return the current assignment for the worker
	 * 
	 * @return
	 */
	public final Task getAssignment()
	{
		return this.assignedTo;		
	}
	
	/**
	 * return the current status of the worker
	 * 
	 * @return
	 */
	public final WorkerStatus getStatus()
	{
		return this.status;
	}
	
	/**
	 * set the status of the worker
	 * 
	 * @param status
	 */
	public final void setStatus(WorkerStatus status)
	{
		this.status = status;
	}
	
	/**
	 * return how long a worker has worked
	 * 
	 * @return
	 */
	public final long getDuration()
	{
		return this.duration;
	}
	
	/**
	 * Initiate the worker to start working on the current assigned task
	 */
	public final void work()
	{
		this.runner.start();
	}
	

	/**
	 * This method completes a specific task, and then proceeds to run any successor tasks.  It uses recursion to manage the successor
	 * tasks, and allow an aggregated rollup of the duration.
	 * 
	 * If there are 0 successor tasks, then the duration of the high-level task is the duration of this worker
	 * 
	 * If there is 1 successor task, then the same worker may (or may not) be assigned the successor task.  The duration is an addition
	 * of the high-level task and the successor.
	 * 
	 * If there are 2 or more successor tasks, then the successor tasks can be done in parallel (depending on the number of available
	 * workers), in which case the duration compares all successor tasks to evaluate the "max" time needed to complete, and then adds that 
	 * to the high-level task
	 */
	public void run()
	{
		System.out.println("Worker " + this.name + " to begin working on task: " + this.assignedTo.getName());
		
		//Work on the assigned task
		long taskDuration = 0;
		if(this.assignedTo.getStatus() == TaskStatus.ASSIGNED)
		{
			this.assignedTo.workTask();			
			taskDuration = this.assignedTo.getDuration();
		}		
		this.status = WorkerStatus.IDLE;		
						
		//Now work on all successor tasks
		//First get all the parallel successor tasks
		ArrayList<Task> parallelTasks = new ArrayList<Task>();			
		Iterator<Task> it = this.assignedTo.getSuccessors().iterator();			
		while(it.hasNext())
		{
			Task curTask = (Task)it.next();
			if (curTask.getStatus() == TaskStatus.READY_TO_START)
			{
				parallelTasks.add(curTask);
			}
		}
		
		//work those parallel successor tasks
		Iterator<Task> itParallel = parallelTasks.iterator();
		ArrayList<Worker> currentWorkers = new ArrayList<Worker>();
		while(itParallel.hasNext())
		{
			Task runCur = (Task)itParallel.next();

			Worker w;			
			try {
			
				synchronized(this.pool)
				{
					//If no workers are available, sit and wait...
					while(!this.pool.isWorkerAvailable())
					{}
					
					w = this.pool.getWorker();
					currentWorkers.add(w);
									
					if (w != null)
					{
						try{
							w.assignTask(runCur);
						}catch(IllegalAssignment e)
						{
							e.printStackTrace();
							//Future - reassign to another resource
						}
						w.work();
					}
				}
			} catch (Exception e) {
				System.out.println("Worker.run() exception during task:" + runCur.getName());					
			}
		}
		
		//Wait for all the workers to complete their work and then calculate the duration
		Iterator<Worker> itWorker = currentWorkers.iterator();
		while(itWorker.hasNext())
		{
			Worker cur = (Worker)itWorker.next();
			cur.waitForCompletion();
		}
		
		//Calculate the duration
		Iterator<Worker> itWorkerCalc = currentWorkers.iterator();		
		long curParallelTaskDuration = 0;
		long maxTaskDuration = 0;
		while(itWorkerCalc.hasNext())
		{
			Worker cur = (Worker)itWorkerCalc.next();
			int pos = currentWorkers.indexOf(cur);
			curParallelTaskDuration = cur.getDuration();
			
			/*
			 * Check if any parallel tasks required the same worker - this would be serial, and therefore must add to the max
			 * If the positions (pos and pos2) are the same, then you are comparing the same objects - dont want this
			 * If the names are the same, then you have the same worker - this means a serial successor task was completed by the same
			 * worker.
			 */			
			Iterator<Worker> itWorkerCalc2 = currentWorkers.iterator();
			while(itWorkerCalc2.hasNext())
			{
				Worker cur2 = (Worker)itWorkerCalc2.next();
				int pos2 = currentWorkers.indexOf(cur2);
				if((pos != pos2) && (pos < pos2) && (cur.getName() == cur2.getName()))
				{
					//Same worker - must be a serial path
					curParallelTaskDuration = curParallelTaskDuration + cur2.getDuration();
				}
			}
			
			//Check to see if current parallel task is greater than the max parallel.  If not, disregard how long this one took
			if(curParallelTaskDuration > maxTaskDuration)
				maxTaskDuration = curParallelTaskDuration;
		}
		
		this.duration = this.duration + maxTaskDuration + taskDuration;	
	}
	
	/**
	 * wait for all other tasks to complete so that duration can be calculated.
	 */
	public final void waitForCompletion()
	{
		try {
			this.runner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
