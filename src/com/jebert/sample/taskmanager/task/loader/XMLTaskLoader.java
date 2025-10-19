package com.jebert.sample.taskmanager.task.loader;

import java.util.ArrayList;
import java.util.Iterator;

 import org.apache.commons.configuration.ConfigurationException;
 import org.apache.commons.configuration.HierarchicalConfiguration;
 import org.apache.commons.configuration.XMLConfiguration;
 import org.apache.commons.configuration.HierarchicalConfiguration.Node;	


import com.jebert.sample.taskmanager.task.SimpleTask;
import com.jebert.sample.taskmanager.task.Task;


/**
 * Class XMLTaskLoader
 * @author Jim Ebert
 * 
 * This class is the access point for configuration from an XML file.  This loads the configuration from a file.
 *
 * This loader assumes that the configuration file is created in an order such that any references to dependant tasks will be previously loaded. 
 */
public class XMLTaskLoader implements TaskLoader {
	
	//XML Node names
	public static final String NODE_ID = "ID";
	public static final String NODE_NAME = "Name";
	public static final String NODE_DURATION = "Duration";
	public static final String NODE_DEPENDENCIES = "Dependencies";
	public static final String NODE_DEPENDENCY = "Dependency";
	public static final String ATTR_DEPENDENCY_ID = "id";	
	public static final String NODE_ATTR_DEPENDENCY_ID = "[@" + ATTR_DEPENDENCY_ID + "]";
	private static final String FILE_LOCATION = "task_config.xml";							//File name of the configuration
	
	/**
	 * Return a list of tasks
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Task> load() {
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		XMLConfiguration config;
		try {
			config = new XMLConfiguration(XMLTaskLoader.FILE_LOCATION);			
			
			HierarchicalConfiguration hConfig = new HierarchicalConfiguration();
			hConfig.setRootNode(config.getRootNode());
				
			Iterator<HierarchicalConfiguration.Node> iter = hConfig.getRootNode().getChildren().iterator();
			
			while(iter.hasNext()){
				XMLConfiguration taskNode = new XMLConfiguration();
				HierarchicalConfiguration.Node next = (HierarchicalConfiguration.Node)iter.next();
				taskNode.setRootNode((Node)next);
				
				//Create the Task
				String name = taskNode.getString(XMLTaskLoader.NODE_NAME);
				int duration = taskNode.getInt(XMLTaskLoader.NODE_DURATION);			
				
				Task newTask = new SimpleTask(name, duration);
				
				//Set Dependencies			
				if(!taskNode.getRootNode().getChildren(XMLTaskLoader.NODE_DEPENDENCIES).isEmpty())
				{				
					HierarchicalConfiguration hDependencies = new HierarchicalConfiguration();					
					hDependencies.setRootNode(taskNode.getRootNode().getChild(3));						
					
					Iterator<HierarchicalConfiguration.Node> itDepend = hDependencies.getRootNode().getChildren().iterator();
					
					while(itDepend.hasNext())
					{
						XMLConfiguration dependNode = new XMLConfiguration();
						dependNode.setRootNode((Node) itDepend.next());
						
						int dependID = dependNode.getInt(XMLTaskLoader.NODE_ATTR_DEPENDENCY_ID);												
						if(dependID > 0)
							newTask.AddDependency(tasks.get(dependID - 1));					
					}
				}
						
				//Add the Task
				tasks.add(newTask);		
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		return tasks;
	}

}
