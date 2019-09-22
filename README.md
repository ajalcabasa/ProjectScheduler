# ProjectScheduler

How to Deploy:
Using an IDE, Run ProjectScheduler.java
Use the following commands to create sample projects on memory:

----------------------------------------
addproject [name] - create and select new project with the given name
setproject [name] - find and select project with the given name
recomputeprojectdates -recompute task start and end dates 
getprojectdetails -show project details 
addtask [name] - create and select a new task with the given name from the current project
settask [name] - find and select task with the given name from the current project
setdates [start date in dd/MM/yyyy] [optional end date in dd/MM/yyyy] - create and select a new task with the given name from the current project.
Assume Start and end dates are the same if end date is blank
adddependency [taskname] - if a task with the name exists, set it as a dependency of the current task
getcurrentselection - returns currently selected project and task");
help - Show help text
exit - end the application
----------------------------------------

Assumptions:
1) Dates are accepted in dd/MM/yyyy format
2) Weekends are not included in computations
2.A) If a task starts on a weekend, the next weekday would be considered the Start date of the task
2.B) If a task ends aon a weekend, the previous weekday would be considered the end date of the task.
3)