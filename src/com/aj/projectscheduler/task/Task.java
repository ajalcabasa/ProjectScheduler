package com.aj.projectscheduler.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.aj.projectscheduler.sequence.FakeSequence;
import com.aj.projectscheduler.utils.TaskScheduler;

public class Task implements Comparable<Task> {

	private List<Task> dependencies;
	private String name;
	private Long id;
	// Task duration in Days
	private int duration;
	private Date startDate;

	public Task() {
		// fake ID for now
		this.id = FakeSequence.getNext();
		this.dependencies = new ArrayList<Task>();
	}

	public Task(String name) {
		this();
		this.name = name;
		this.duration = 1;
	}

	public Task(String name, List<Task> dependencies) {
		this(name);
		this.dependencies.addAll(dependencies);
	}

	public List<Task> getDependencies() {
		return dependencies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean validateStartDate(Date startDate) {
		for (Task dependency : this.dependencies) {
			if (!dependency.getEndDate().before(startDate)) {
				return false;
			}
		}
		return true;
	}

	public boolean setStartDate(Date startDate) {
		if (this.getDependencyEndDate() != null
				&& (startDate.before(this.getDependencyEndDate()) || startDate.equals(this.getDependencyEndDate()))) {
			return false;
		}
		this.startDate = startDate;
		return true;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public Date recomputeStartDate() {
		if (this.dependencies.size() > 0) {
			Date finalEndDate = getDependencyEndDate();
			if (finalEndDate != null && (finalEndDate.after(startDate) || startDate.equals(finalEndDate))) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(finalEndDate);
				// Assume tasks end on EOD
				cal.add(Calendar.DAY_OF_MONTH, 1);
				this.startDate = TaskScheduler.getNextWeekday(cal.getTime());
			}
		}
		return this.startDate;
	}

	public Date getDependencyEndDate() {
		Date max = null;

		if (this.dependencies.size() > 0) {
			for (Task dependency : this.dependencies) {
				Date endDate = dependency.getEndDate();
				if (max == null || endDate.after(max)) {
					max = endDate;
				}
			}
		}

		return max;
	}

	public String getDependencyNames() {
		StringBuilder sb = new StringBuilder();
		for (Task dependency : getDependencies()) {
			sb.append(dependency.getName()).append(", ");
		}
		return sb.toString();
	}

	public Date getEndDate() {
		if (this.startDate == null) {
			return null;
		}
		return TaskScheduler.getEndDate(this.startDate, this.duration - 1);
	}

	public Long getId() {
		return this.id;
	}

	public boolean addDependency(Task targetDependency) {
		if (targetDependency == null || this.equals(targetDependency) || targetDependency.isDependency(this)) {
			return false;
		}
		this.dependencies.add(targetDependency);
		this.recomputeStartDate();
		return true;
	}

	public boolean isDependency(Task target) {
		for (Task dependency : this.dependencies) {
			if (dependency.isDependency(target) || dependency.equals(target)) {
				return true;
			}
		}
		return false;
	}

	// Temp compare with IDs for now
	@Override
	public int compareTo(Task o) {
		if (this.isDependency(o)) {
			return 1;
		}

		if (o.getStartDate().before(this.getStartDate())) {
			return 1;
		} else if (o.getStartDate().after(this.getStartDate())) {
			return -1;
		} else {
			return 0;
		}
	}

	public void printTaskDetails() {
		DateFormat df = new SimpleDateFormat("E, MMM dd yyyy");
		
		StringBuilder sb = new StringBuilder();
		sb.append("Task: " + this.getName()).append("\n");
		sb.append("Start: " + df.format(this.getStartDate()) + " to " + df.format(this.getEndDate())).append("\n");
		sb.append("Dependencies: " + this.getDependencyNames()).append("\n");
		System.out.println(sb.toString());
	}
}
