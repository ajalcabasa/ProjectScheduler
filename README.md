# ProjectScheduler

How to Deploy:
Using an IDE, Run ProjectScheduler.java
Use the following commands to create sample projects on memory:


* addproject [name] - create and select new project with the given name
* setproject [name] - find and select project with the given name
* recomputeprojectdates -recompute task start and end dates 
* getprojectdetails -show project details
* addtask [name] - create and select a new task with the given name from the current project
* settask [name] - find and select task with the given name from the current project
* setdates [start date in dd/MM/yyyy] [optional end date in dd/MM/yyyy] - create and select a new task with the given name from the current project. Assume Start and end dates are the same if end date is blank
* adddependency [taskname] - if a task with the name exists, set it as a dependency of the current task
* getcurrentselection - returns currently selected project and task");
* help - Show help text
* exit - end the application


Assumptions:
1) Dates are accepted in dd/MM/yyyy format
2) Weekends are not included in computations
3) If a task starts on a weekend, the next weekday would be considered the Start date of the task
4) If a task ends aon a weekend, the previous weekday would be considered the end date of the task.
5) Project details are printed with the "getprojectdetails" command
This would show the current state of the Project and Tasks
6) "recomputeprojectdates" would recompute start and end dates, when appropriate.
If a task starts before its dependencies end, its start date will be moved to the day after all dependency tasks end.
If a task starts after its dependencies, start dates will not change.
