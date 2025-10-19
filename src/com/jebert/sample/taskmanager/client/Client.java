package com.jebert.sample.taskmanager.client;

import com.jebert.sample.taskmanager.task.Task;
import com.jebert.sample.taskmanager.task.TaskManager;
import com.jebert.sample.taskmanager.task.loader.SimpleTaskLoader;
import com.jebert.sample.taskmanager.task.loader.TaskLoader;
//import com.jebert.sample.taskmanager.task.loader.XMLTaskLoader;

/**
 * Class Client
 * @author Jim Ebert
 * 
 * This class is the test stub for testing any configuration of tasks.  This loads the configuration
 * and runs the tasks to completion
 *
 */
public class Client {

	public Client(){}
		
	/**
	/**
	 * Load the configuration and return the list of tasks
	 * @return
	 */
	public java.util.ArrayList<Task> getConfiguration()
	{		
		TaskLoader simple = new SimpleTaskLoader();
		return simple.load();
		
		//TaskLoader tl = new XMLTaskLoader();
		//return tl.load();
	}
	/**
	 * Test client
	 * @param args
	 */
	public static void main(String[] args)
	{
		Client runner = new Client();
		java.util.ArrayList<Task> taskList = runner.getConfiguration();
		/*
		 * Test 1 - Setting the Task Manager to have an unlimited size pool of workers
		 */
		//TaskManager manager = new TaskManager(taskList);
		
		/*
		 * Test 2 - Setting the Task Manager to explicitly set a maximum amount of workers
		 */
		TaskManager manager = new TaskManager(taskList, 3);
		
		//Complete the Tasks
		manager.run();
		
		//Output Results
		System.out.println("Total # of workers needed:" + manager.getWorkerCount());
		System.out.println("Most efficient time: " + manager.getTimeElapsed());
	}

}
