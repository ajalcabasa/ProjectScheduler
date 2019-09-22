package com.aj.projectscheduler.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.aj.projectscheduler.project.Project;
import com.aj.projectscheduler.task.Task;
import com.aj.projectscheduler.utils.TaskScheduler;

public class ProjectScheduler {

	public static void main(String args[]) {
		Scanner sc = null;
		try {
			Map<String, Project> projects = new HashMap<String, Project>();
			sc = new Scanner(System.in);
			String inputStr = null;
			StringTokenizer st = null;
			Project currentProject = null;
			Task currentTask = null;
			Task tempTask = null;
			String command = null;
			String param1 = null;
			String param2 = null;
			printHelp();
			do {
				inputStr = sc.nextLine();
				st = new StringTokenizer(inputStr);
				command = st.nextToken(" ");

				switch (command) {
				case "help":
					printHelp();
					break;
				case "addproject":
					if (!st.hasMoreTokens()) {
						System.out.println("This command requires a name parameter");
						break;
					}
					param1 = st.nextToken("\n").trim();
					if (param1.equals("")) {
						System.out.println("Cannot create project with empty name");
					} else if (projects.containsKey(param1)) {
						System.out.println("Found project with name: " + param1
								+ ". Selecting existing project instead of creating one.");
						currentProject = projects.get(param1);
					} else {
						currentProject = new Project(param1);
						projects.put(currentProject.getName(), currentProject);
					}
					System.out.println("Current Project: " + currentProject.getName());
					break;
				case "setproject":
					if (!st.hasMoreTokens()) {
						System.out.println("This command requires a name parameter");
						break;
					}
					param1 = st.nextToken("\n");
					if (projects.containsKey(param1)) {
						currentProject = projects.get(param1);
					} else {
						System.out.println("Cannot find project with name: " + param1);
					}
					System.out.println("Current Project: " + currentProject.getName());
					break;
				case "recomputeprojectdates":
					if (currentProject == null) {
						System.out.println("No Project selected");
					} else {
						currentProject.autoComputeDates();
						currentProject.printDetails();
					}
					break;
				case "getprojectdetails":
					if (currentProject == null) {
						System.out.println("No Project selected");
					} else {
						currentProject.printDetails();
					}
					break;
				case "addtask":
					if (!st.hasMoreTokens()) {
						System.out.println("This command requires a name parameter");
						break;
					}
					if (currentProject == null) {
						System.out.println("No Project selected");
						break;
					}

					param1 = st.nextToken("\n").trim();
					tempTask = currentProject.getTask(param1);
					if (tempTask != null) {
						tempTask = currentTask;
						System.out.println("Current Task: " + currentTask.getName());
					} else if (param1.equals("")) {
						System.out.println("Cannot create task with empty name");
					} else {
						currentTask = currentProject.addTask(param1);
						currentTask.setDuration(1);
						currentTask.setStartDate(TaskScheduler.getNextWeekday());
						System.out.println("Current Task: " + currentTask.getName());
					}
					break;
				case "settask":
					if (!st.hasMoreTokens()) {
						System.out.println("This command requires a name parameter");
						break;
					}
					param1 = st.nextToken("\n").trim();
					tempTask = currentProject.getTask(param1);
					if (tempTask == null) {
						System.out.println("Cannot find task with name " + param1);
					} else {
						currentTask = currentProject.getTask(param1);
						System.out.println("Current Task: " + currentTask.getName());
					}
					break;
				case "setdates":
					if (currentTask == null) {
						System.out.println("No Task selected.");
					} else {

						if (!st.hasMoreTokens()) {
							System.out.println("This command requires a start date and an end date");
							break;
						} else {
							param1 = st.nextToken();
						}

						if (!st.hasMoreTokens()) {
							param2 = param1;
						} else {
							param2 = st.nextToken();
						}

						Date start = null;
						Date end = null;
						try {
							start = new SimpleDateFormat("dd/MM/yyyy").parse(param1);
						} catch (ParseException e) {
							System.out.println("Wrong input for start date: " + param1);
						}
						try {
							end = new SimpleDateFormat("dd/MM/yyyy").parse(param2);
						} catch (ParseException e) {
							System.out.println("Wrong input for end date: " + param2);
						}
						if (!(start == null || end == null)) {
							if (end.before(start)) {
								System.out.println("Start date should be before end date");
							} else {
								start = TaskScheduler.getNextWeekday(start);
								end = TaskScheduler.getLastWeekday(end);
								currentTask.setStartDate(start);
								currentTask.setDuration(TaskScheduler.getDuration(start, end));
								currentTask.printTaskDetails();
							}
						}
					}
					break;
				case "adddependency":
					if (!st.hasMoreTokens()) {
						System.out.println("This command requires a name parameter");
						break;
					}
					param1 = st.nextToken("\n").trim();
					if (currentTask == null) {
						System.out.println("No task selected");
						break;
					}
					Task dependency = currentProject.getTask(param1);
					if (dependency != null) {
						currentTask.addDependency(dependency);
						currentTask.printTaskDetails();
					} else {
						System.out.println("Cannot find task with name or id: " + param1);
					}
					break;
				case "getcurrentselection":
					System.out.println("Current Project: " + currentProject.getName());
					System.out.println("Current Task: " + currentTask.getName());
					break;
				case "exit":
					break;
				default:
					System.out.println("Unknown Command: " + command);
				}

			} while (!"exit".equals(command));
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
	}

	public static void printHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------------------\n");
		sb.append("Usage:\n");
		sb.append("addproject [name] - create and select new project with the given name\n");
		sb.append("setproject [name] - find and select project with the given name\n");
		sb.append("recomputeprojectdates -recompute task start and end dates \n");
		sb.append("getprojectdetails -show project details \n");
		sb.append("addtask [name] - create and select a new task with the given name from the current project\n");
		sb.append("settask [name] - find and select task with the given name from the current project\n");
		sb.append(
				"setdates [start date in dd/MM/yyyy] [optional end date in dd/MM/yyyy] - create and select a new task with the given name from the current project.\nAssume Start and end dates are the same if end date is blank\n");
		sb.append(
				"adddependency [taskname] - if a task with the name exists, set it as a dependency of the current task\n");
		sb.append("getcurrentselection - returns currently selected project and task");
		sb.append("help - Show help text\n");
		sb.append("exit - end the application\n");
		sb.append("----------------------------------------\n\n");

		System.out.println(sb.toString());
	}
}
