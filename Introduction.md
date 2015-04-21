# Rationale #
The main goal of this project is to enable the connection of [legacy](http://en.wikipedia.org/wiki/Legacy_system) tasks databases to Mylyn quickly and cleanly. We want to provide Mylyn access to Tasks that are stored in a non-web repository, primarily a database, but this project can be used to hookup anything to Mylyn implementing just one class.


# Example Projects #
This project provides three demo projects to such a repository.

  * **Memory**
    * Just implements the [IPersistor](IPersisor.md) interface and manages an in-memory task database, complete with attachements and multi comments.
  * **Derby and Ibatis**
    * By defining only **12 SQL queries**, you have access to all the Tasks in the database.
    * By adding another **6 SQL queries** you have added support for Comments and Attachments
  * **Derby and JPA**
    * Describes how JPA can be used to connect a database to Mylyn

## Use Case 0: personal cross workspace local task repository ##

When you have multiple workspace and use local tasks, you can only get at the tasks in the current workspace.
There is also no facility for adding attachments or comments. Running a local Derby instance provides all of this in transparent manner.

[Vote when this use case applies to you](https://bugs.eclipse.org/bugs/votes.cgi?action=show_user&bug_id=184532#vote_184532)

## Use Case 1: small development or web shops with home-grown tracker in database ##

Many small web or software development shops have created their own issue trackers or workflow systems that predate the ever wider acceptance of Eclipse as a platform.
These usually have the data stored in a local database, accessible via lan via a custom application (Filemaker, MS Access) or a home built web frontend (LAMP).

It is not worthwile to them to build or purchase a full-fledged connector (yet), but they want access to the tasks in the convenient Mylyn way with lowest possible configuration effort.

[Vote when this use case applies to you](https://bugs.eclipse.org/bugs/votes.cgi?action=show_user&bug_id=184532#vote_184532)

## Use Case 2: provide task like access to logged system/server exceptions stored in a database ##

Webservers and application servers can store their logs in a database instead of in a plain text file.
Example: [Writing Apache's Logs to MySQL](http://www.onlamp.com/pub/a/apache/2005/02/10/database_logs.html)

But who likes going over these logs or doing periodc queries on them.
This connector can look in such log files and and a system operator can thus create easy queries looking at error types, source IP's server id's or string matches in the request URL.

The Mylyn notification pop up window and the task list will indicate where new issues occurred.

[Vote when this use case applies to you](https://bugs.eclipse.org/bugs/votes.cgi?action=show_user&bug_id=184532#vote_184532)

# Architecture #
![http://wiki.eclipse.org/images/8/86/IndustrialArchitecture.png](http://wiki.eclipse.org/images/8/86/IndustrialArchitecture.png)