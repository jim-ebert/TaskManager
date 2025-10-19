package com.jebert.sample.taskmanager.task.loader;

import java.util.ArrayList;

import com.jebert.sample.taskmanager.task.SimpleTask;
import com.jebert.sample.taskmanager.task.Task;

/**
 * Class SimpleTaskLoader
 * @author Jim Ebert
 * 
 * This class is the basic configuration access point.  This loads the configuration from java directly.
 *
 */
public class SimpleTaskLoader implements TaskLoader {

	/**
	 * Return a list of tasks
	 */
	public ArrayList<Task> load() {
		ArrayList<Task> newTasks = new ArrayList<Task>();
		
		Task a = new SimpleTask("A", 5);		
		newTasks.add(a);
		
		Task b = new SimpleTask("B", 3);
		b.AddDependency(a);
		newTasks.add(b);
		
		Task c = new SimpleTask("C", 10);
		newTasks.add(c);
		
		Task d = new SimpleTask("D", 1);
		d.AddDependency(c);
		d.AddDependency(b);
		newTasks.add(d);
	
		Task e = new SimpleTask("E", 4);
		e.AddDependency(a);
		newTasks.add(e);	
		
		return newTasks;		
	}

}
