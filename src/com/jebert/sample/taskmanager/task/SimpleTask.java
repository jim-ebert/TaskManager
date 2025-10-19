package com.jebert.sample.taskmanager.task;

/**
 * Class SimpleTask
 * @author Jim Ebert
 *
 * This class is a concrete class for managing a simple task.  It simply sleeps the thread for the entire duration of the task (in seconds).
 * 
 */
public class SimpleTask extends AbstractTask {

	/**
	 * Constructor - create a task with a name and duration
	 * @param l_name
	 * @param l_duration
	 */
	public SimpleTask(String l_name, long l_duration)
	{
		super(l_name, l_duration);
	}
	
	/**
	 * Complete the task
	 */
	public void workTask()
	{				
		if(this.getStatus() == TaskStatus.ASSIGNED)
		{	
			this.setStatus(TaskStatus.IN_PROGRESS);
			System.out.println("Task In Progress: " + this.getName());
			
			try {
				Thread.sleep(this.getDuration() * 1000);
			} catch (InterruptedException e) {
				System.out.println("run() failed for task:" + this.getName());
			}
		
			this.setStatus(TaskStatus.COMPLETED);			
			System.out.println("Completed Task: " + this.getName());
		}		
		else
		{
			System.out.println("run() failed for task:" + this.getName() + " task is not yet assigned");			
		}
	}
}
