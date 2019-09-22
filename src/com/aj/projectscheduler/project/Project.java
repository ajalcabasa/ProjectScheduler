package com.aj.projectscheduler.project;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.aj.projectscheduler.sequence.FakeSequence;
import com.aj.projectscheduler.task.Task;

public class Project {

	Map<Long, Task> tasks;
	private String name;
	private Long id;

	public Project(String name) {
		this.id = FakeSequence.getNext();
		this.name = name;
		this.tasks = new HashMap<Long, Task>();
	}

	public List<Task> getTaskList() {
		List<Task> sortedTasks = tasks.values().stream().collect(Collectors.toList());
		Collections.sort(sortedTasks);
		return sortedTasks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public Task addTask() {
		Task task = new Task();
		this.tasks.put(task.getId(), task);
		return task;
	}

	public Task addTask(String name) {
		Task task = this.addTask();
		task.setName(name);
		return task;
	}

	public Task getTask(String name) {
		Long nameAsLong = null;
		try {
			nameAsLong = Long.parseLong(name);
		} catch (NumberFormatException e) {

		}
		if (nameAsLong != null && this.tasks.containsKey(nameAsLong)) {
			return this.tasks.get(nameAsLong);
		}
		for (Task task : this.getTaskList()) {
			if (task.getName().equals(name)) {
				return task;
			}
		}
		return null;
	}

	public void autoComputeDates() {
		this.getTaskList().forEach(new Consumer<Task>() {
			public void accept(Task task) {
				task.recomputeStartDate();
			}
		});
	}

	public Date getProjectEndDate() {
		List<Task> tasks = this.getTaskList();

		Date max = null;
		for (Task task : tasks) {
			Date taskEndDate = task.getEndDate();
			if (max == null || (taskEndDate != null && max.before(taskEndDate))) {
				max = taskEndDate;
			}
		}
		return max;
	}

	public void printDetails() {
		DateFormat df = new SimpleDateFormat("E, MMM dd yyyy");
		StringBuilder sb = new StringBuilder();
		sb.append("-------------------------\n");
		sb.append("Project: " + getName()).append("\n");
		sb.append("Projected end date: " + df.format(getProjectEndDate())).append("\n");
		sb.append("Tasks:\n");
		for (Task task : this.getTaskList()) {
			sb.append("-------------------------\n");
			sb.append("Task: " + task.getName()).append("\n");
			sb.append("Start: " + df.format(task.getStartDate()) + " to " + df.format(task.getEndDate())).append("\n");
			sb.append("Dependencies: " + task.getDependencyNames()).append("\n");
		}
		sb.append("-------------------------\n");

		System.out.println(sb.toString());
	}

}
