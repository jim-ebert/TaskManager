package com.jebert.sample.taskmanager.task.loader;

import java.util.ArrayList;
import com.jebert.sample.taskmanager.task.Task;

/**
 * Interface TaskLoader
 * @author Jim Ebert
 * 
 * Interface for loading configuration for all tasks
 *
 */
public interface TaskLoader {
	public ArrayList<Task> load();
}
