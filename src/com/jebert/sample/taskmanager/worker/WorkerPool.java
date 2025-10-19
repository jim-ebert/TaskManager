package com.jebert.sample.taskmanager.worker;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class WorkerPool
 * @author Jim Ebert
 *
 * This class manages a pool of available resources for working on tasks.  It can manage a maximum amount of workers, and manage 
 * handing out newly available workers
 * 
 * Due to the nature of threading, once the thread completes, it should not be used again.  Since each worker is a thread, a new worker 
 * is needing to be created every time getWorker() is called, even if it will return a worker of the same name.  
 */

public class WorkerPool {

	private ArrayList<Worker> workers = new ArrayList<Worker>();	//Array of all the workers
	private int maxNumberOfWorkers;									//Max size of the worker pool
	private int DEFAULT_POOL_SIZE = 99;								//Default if not set by the client
	
	/**
	 * Default Constructor
	 */
	public WorkerPool()
	{
		this.maxNumberOfWorkers = DEFAULT_POOL_SIZE;
	}
	
	/**
	 * Constructor to set the allowable pool size
	 * @param max
	 */
	public WorkerPool(int max)
	{
		this.maxNumberOfWorkers = max;
	}
	
	/**
	 * Access a worker from the pool
	 * @return
	 */
	public Worker getWorker()
	{
		Worker w = null;
		
		//No workers created yet - create one and add to the pool
		if(this.workers.size() == 0)
		{
			w = new SimpleWorker(String.valueOf(this.workers.size()+1), this);
			this.workers.add(w);
		}
		else
		{
			//Check to see if there are any available workers in the pool
			Iterator<Worker> it = this.workers.iterator();
			while(it.hasNext())
			{
				Worker curWorker = (Worker)it.next();
				if(curWorker.getStatus() == WorkerStatus.IDLE)
				{
					//This worker is IDLE, so we can reuse him
					//Always need a new worker, but will keep name same
					w = new SimpleWorker(curWorker.getName(), this);					
					this.workers.remove(curWorker);
					this.workers.add(w);
					break;
				}
			}
		}
		
		//If all workers are currently busy, check to see if we can add workers to the pool. 
		if (w == null)
		{
			if(this.workers.size() < this.maxNumberOfWorkers)
			{
				//number of workers or still workers available in the pool, just not created yet
				w = new SimpleWorker(String.valueOf(this.workers.size()+1), this);
				this.workers.add(w);
			}
		}
		
		return w;
	}
	
	/**
	 * Check to see if there are any available workers
	 * @return
	 */
	public boolean isWorkerAvailable()
	{
		boolean returnVal = false;
		
		if(this.workers.size() < this.maxNumberOfWorkers)
			returnVal = true;
		else
		{	
			Iterator<Worker> itWorker = this.workers.iterator();
			while(itWorker.hasNext())
			{
				Worker cur = (Worker)itWorker.next();
				if(cur.getStatus() == WorkerStatus.IDLE)
					returnVal = true;
			}
		}
		return returnVal;
	}
	
	/**
	 * return how many current workers there are
	 * @return
	 */
	public int getPoolSize()
	{
		return this.workers.size();
	}
}

