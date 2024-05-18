# Planner-System
Introduction to the Planner System
Welcome to the Planner System, a versatile scheduling tool designed to help you manage your events
with ease and precision. Whether you are coordinating personal activities, managing work-related
meetings, or organizing social events, this system provides a robust platform to ensure that all
your plans are well-organized and easily accessible.

The Planner System is equipped with a range of features that allow users to create, modify,
schedule, and remove events. It supports various scheduling strategies such as "Anytime",
"Work-hours", and "Lenient", giving you the flexibility to choose the one that best fits your needs.
Additionally, the system offers the capability to handle multiple users, making it perfect for teams
and groups who need to synchronize their schedules.

This guide will provide detailed instructions on how to utilize the Planner System, from launching
the application with specific command line arguments to entering event details and navigating
through the system's features. Whether you are a first-time user or looking to deepen your
understanding of the system's capabilities, this guide aims to equip you with all the necessary
information to maximize your scheduling efficiency.

Let's get started on your journey to better time management and organized scheduling with the
Planner System!

Invariants
The NUPlannerSystem maintains several critical invariants to ensure the integrity and consistency of
schedule data:

Unique User Identifiers: Each user within the system is identified by a unique identifier, ensuring
that schedules are correctly associated with individual users.

No Overlapping Events: The system guarantees that no two events within the same schedule overlap in
time, preventing scheduling conflicts.

Consistent Event Details: Once an event is created or modified, its details
(including time, location, and invitees) remain consistent across all user schedules that include
the event, unless explicitly changed through the system's modification functionalities.

Integrity of Event Participants: An event's list of invitees, including the host, is maintained
accurately across all operations, ensuring that participant information is correctly reflected in
event details.

This outline provides a high-level understanding of the NUPlannerSystem, guiding users through its
primary functionalities and organizational structure. For detailed method usage and system
capabilities, refer to the Javadoc comments within each class and interface.

PLANNER SYSTEM PACKAGE:
ReadonlyPlannerSystem Interface Description:
The ReadonlyPlannerSystem interface provides a secure, read-only view of the planner system,
ensuring that data can only be retrieved, not modified. It is designed for use cases where
information needs to be displayed or analyzed without the risk of altering the underlying data.
This interface includes methods for:

Displaying a user's schedule: Allows retrieval of a formatted string representation of a user's
schedule.
Showing specific event details: Facilitates querying for specific events based on time and day,
useful for checking schedules without making modifications.
Getting a user's schedule: Provides non-modifiable access to a user's schedule.
Listing all users: Returns a set of all user IDs that have schedules in the system, which is useful
for administrative or overview purposes.
Checking event conflicts: Assists in determining if a proposed event conflicts with the schedules
of any invited users, enhancing the system's capability to manage scheduling conflicts efficiently.

PlannerSystem Interface Description:
The PlannerSystem interface extends ReadonlyPlannerSystem and includes capabilities for modifying
and managing the planner system. It supports a comprehensive set of functionalities necessary for
maintaining an interactive, dynamic scheduling system. Key functionalities include:

Reading and saving schedules from/to XML: Methods to read schedules from XML files and save them
back, facilitating persistence and data exchange.
Creating and managing events: Provides methods to create new events, modify existing ones, and
remove them from schedules, including validation to avoid scheduling conflicts.
Automatic scheduling: Offers the capability to automatically find suitable times for new events
based on a set scheduling strategy, which can dynamically adapt to changing conditions and
requirements.
Observer management: Implements observer registration and removal functionalities, allowing other
components to stay updated with changes within the planner system.
User management: Methods to add and remove users, ensuring that the system can dynamically adjust to
changes in its user base.
Setting scheduling strategies: Allows the system's scheduling behavior to be customized or altered
by setting different scheduling strategies according to the current needs.

These interfaces collectively form the core of a robust planning system, where ReadonlyPlannerSystem
ensures data integrity and safe access while PlannerSystem provides the tools necessary for active
management and dynamic response to scheduling requirements.

NUPlannerSystem Class Description
The NUPlannerSystem class is a comprehensive implementation of the PlannerSystem interface, designed
to manage users and their event schedules within a planner system. This class offers robust features
to interact with schedules through various means including XML file operations, ensuring a flexible
and dynamic scheduling environment. Key features of this class include:

XML File Integration: Facilitates reading and writing of user schedules to and from XML files,
allowing persistent storage and easy data exchange.
Event Management: Supports creating, modifying, and removing events, providing tools to manage user
schedules effectively. It includes checks to prevent scheduling conflicts and ensures all
modifications adhere to system rules.
Schedule Visualization: Implements functionality to display a user's schedule in a formatted string,
making it easy to view and verify schedule details at a glance.
Observer Pattern: Utilizes observers to notify changes within the planner system, supporting a
responsive and interconnected system architecture where changes are propagated efficiently to
interested components.
Scheduling Strategy: Employs a scheduling strategy to automate event planning based on predefined
criteria and constraints, enhancing the system's ability to manage resources and timings
dynamically.
User and Event Validation: Ensures data integrity and correctness by validating user existence,
event details, and schedule conflicts before performing operations, thereby maintaining system
reliability and user trust.
Backup and Restore: Provides mechanisms to backup and restore event details during modifications,
safeguarding against data loss during update operations and ensuring system stability.

Here's a simple example:

java
Copy code
File xmlFile = new File("userSchedule.xml");
PlannerSystem system = new NUPlannerSystem();

// Read a user's schedule from an XML file
system.readUserSchedule(xmlFile);

// Create a new event
system.createEvent("userId", "Event Name", "Monday", "0900", "Monday", "1000", false, "Library");

// Save the updated schedule to an XML file
system.saveUserSchedule("userId");

This class is central to the planner system, integrating various components like event management,
data persistence, and system notifications into a cohesive unit that manages complex scheduling
tasks efficiently and reliably.

EventBackup Class
The EventBackup class is designed to capture and store the state of an event at a specific point in
time, useful for rollback operations during failed updates. It includes:

State Capture: Instantly backs up crucial event details like time, date, location, and participant
list upon instantiation.
Data Integrity: Ensures that the backup remains unaffected by subsequent changes to the event,
providing a reliable base for restoration if needed.

ScheduleXMLWriter Class
The ScheduleXMLWriter class handles the serialization of schedule data into XML format, facilitating
data storage and exchange. Key functionalities include:

XML Writing: Converts Schedule objects into well-formed XML documents, encapsulating all event
details such as timing, location, and participants.
File Management: Saves the serialized XML to specified file paths, supporting data persistence and
archival.
Detail Handling: Includes methods to append detailed time and location information to the XML
structure, ensuring comprehensive data representation.

SCHEDULE PACKAGE:
ISchedule Interface
The ISchedule interface defines a framework for managing a user's schedule within a scheduling
system. It provides methods to add, remove, and query events, ensuring no time overlaps and
offering functionalities such as:

Getting the user ID associated with the schedule.
Retrieving a list of events, ensuring immutability by returning a copy.
Adding and removing events with validation against overlaps and presence.
Checking for event overlaps and presence.
Sorting events by day and time.
Finding and retrieving specific events based on specified criteria.

Schedule Class
The Schedule class implements the ISchedule interface, managing a list of events for a specific
user. Key features include:

Initialization: Constructs with an empty event list for a specified user.
Event Management: Adds events after checking for time conflicts, removes events directly, and checks
for their existence.
Schedule Queries: Provides sorted views of events, searches for events by day and time, and verifies
event presence using defined criteria.
Utility Functions: Includes private methods to validate the existence of events, enhancing
robustness and data integrity.

ReadOnlyEvent Interface
The ReadOnlyEvent interface provides a read-only access to event details within a scheduling system.
It allows retrieval of key event attributes such as the event's name, time, location, and
participant list without modifying any underlying data. Essential methods include:

Fetching basic event details like name, time, and location.
Checking event timing conflicts and specific occurrences.
Identifying if an event spans multiple weeks and retrieving participant details.

IEvent Interface
The IEvent interface extends ReadOnlyEvent to include functionalities for modifying event details.
It allows users to set or update the event's name, time, location, and participant list, supporting
dynamic changes to the event's attributes. Key functionalities include:

Setting and updating event details such as name, timing, and location.
Managing participants by adding or removing invitees and setting the event host.

Event Class
The Event class implements the IEvent interface, encapsulating comprehensive details about an event
including its name, timing, location, host, and invitees. This class ensures that all event details
are managed consistently and provides mechanisms to check for time overlaps and validate participant
information. Highlights include:

Event creation and management with capabilities to modify all aspects of an event.
Validation of event and user details to ensure data integrity.
Overlap and occurrence checks to maintain scheduling constraints.
Custom methods for equality and hashing to facilitate comparisons and storage in data structures.

TimeUtilities Class
The TimeUtilities class offers utility methods for formatting time-related information in a
scheduling system. It provides:

Time Formatting: Converts LocalTime objects into a string representation without colons
(e.g., 0930 for 9:30).
Day Formatting: Transforms DayOfWeek objects into properly capitalized strings
(e.g., Monday for MONDAY).

SCHEDULE VIEW PACKAGE:
ScheduleView Interface
The ScheduleView interface defines the functionality for rendering a user's schedule as a readable
string. It emphasizes presenting schedule details in a structured format, including:

Rendering Function: Converts a schedule into a string that organizes events by day of the week,
showing details like names, times, locations, and participants.

ScheduleViewModel Class
The ScheduleViewModel class implements the ScheduleView interface, facilitating the visual
representation of a schedule from the PlannerSystem. It structures the schedule in a weekly view
with clear demarcations for each day and detailed event information. Key features include:

Detailed Event Representation: Lists events under the day they occur, with comprehensive details
such as time, location (noting online status), and list of invitees.
Utilizes Planner System: Fetches and sorts the schedule from the PlannerSystem, ensuring that the
schedule data is current and well-organized.
Time Formatting: Leverages TimeUtilities to format time and day information, enhancing readability
and consistency across presentations.

SCHEDULE STRATEGY PACKAGE:
ScheduleStrategy Interface
The ScheduleStrategy interface defines the framework for auto-scheduling events within a planner
system. It supports:

Event Scheduling: Automatically determines appropriate time slots for events based on their duration
and existing schedules, ensuring no conflicts with other events.

AnyTimeScheduleStrategy Class
The AnyTimeScheduleStrategy implements the ScheduleStrategy to schedule events at the earliest
possible time within a week, avoiding overlaps. Key functionalities include:

Earliest Time Scheduling: Finds the earliest time slot within a week that does not conflict with
existing events.
Duration Validation: Ensures that the event's duration is feasible within the week's time
constraints.

WorkHourScheduleStrategy Class
Building on AnyTimeScheduleStrategy, the WorkHourScheduleStrategy schedules events specifically
within standard work hours (09:00 to 17:00, Monday to Friday). It ensures:

Work Hour Constraints: Events are scheduled only during work hours, enhancing compatibility with
typical business operations.
Day and Time Management: Manages scheduling to fit within the constraints of workday hours,
preventing scheduling outside these hours.

LenientScheduleStrategy Class
The LenientScheduleStrategy extends WorkHourScheduleStrategy by adopting a more flexible approach to
scheduling, requiring availability from the host and at least one invitee, rather than all.
It provides:

Flexible Participant Requirements: Schedules events even if not all invitees are available,
requiring only the host and one other participant to be free.
Adaptive Scheduling: Adjusts invitee lists based on availability, fostering higher scheduling
success rates under constrained conditions.

ScheduleStrategyCreator Class
The ScheduleStrategyCreator facilitates the creation of different scheduling strategies
(Anytime, WorkHours, Lenient) based on specified needs. It features:

Strategy Instantiation: Dynamically creates instances of different scheduling strategies.
Flexible Strategy Options: Supports various scheduling preferences, from strict to lenient,
accommodating diverse scheduling requirements.

VALIDATION UTILITIES PACKAGE:
ValidationUtilities Class
The ValidationUtilities class provides essential utility methods for validating object states within
a system, ensuring that objects and their fields meet specific criteria before proceeding with
operations. Key functionalities include:

Null Checks: Offers methods to assert that an object or field is not null.
validateNull(Object o): Throws an IllegalArgumentException if the passed object is null, ensuring
parameters are properly initialized before use.
validateGetNull(Object obj): Throws an IllegalStateException if an object field accessed is null,
typically used for fields that are expected to be set post-construction or initialized lazily.

These utilities are crucial for maintaining robustness and preventing errors due to invalid state or
improper initialization of objects within the application.

VIEW PACKAGE:
PlannerSystemView Interface
Defines essential GUI operations for the Planner System, such as setting action listeners,
displaying errors, handling file operations, and updating the user interface.

PlannerSystemViewImpl Class
Implements the PlannerSystemView interface, providing a comprehensive graphical user interface for
interacting with the Planner System. It includes functionality for creating and scheduling events,
managing calendar files, and displaying schedules.

SchedulePanel Class
A specialized JPanel that visually represents user schedules. It supports rendering schedules with
detailed event information, such as timings and participants, arranged by day and time.

EventClickListener Class
Handles mouse events on the SchedulePanel, allowing users to interact with specific events displayed
on the panel. It processes selections to trigger appropriate actions based on the user's input.

EventView Interface
Defines the core functionalities for an event view in the UI. This includes operations like making
the view visible, setting action listeners, and retrieving event details like the event name, time,
location, and participant details.

EventViewImpl Class
Implements the EventView interface, providing a graphical interface for viewing and interacting with
event details. This class allows for operations such as viewing and editing event details,
scheduling events, and managing event participants.

ScheduleEventView Class
Extends EventViewImpl to provide specific functionalities for scheduling events. It includes
additional UI elements to handle the scheduling aspects, such as setting the event duration and
managing a list of invitees. This class adapts the general event view to cater specifically to event
scheduling needs.

These components collectively support a dynamic and interactive environment for users to manage and
view schedules within the Planner System, enhancing usability through a graphical interface.

CONTROLLER PACKAGE:
Observer Interface
This interface is part of the Observer design pattern. It's used for creating a subscription
mechanism to allow objects to listen and react to events or changes in another object, known as the
subject.

PlannerSystemController Interface
Defines the operations that a planner system controller should support, acting as an intermediary
between the view and the model. It handles user actions and updates the model or view accordingly.

ScheduleViewController Class
This class implements the PlannerSystemController interface and manages user actions and mouse
events in the Planner System application. It handles interactions between the view
(PlannerSystemView) and the model (PlannerSystem), responding to GUI actions and updating the
system state as necessary.

Key Functionalities:
Event Management: Opens event frames for creating, modifying, or scheduling events based on user
actions.
User Interaction: Processes button presses and mouse clicks, executing corresponding commands like
AddCalendar, CreateEvent, ModifyEvent, etc.
Error Handling: Displays error messages through the system view when operations fail.
State Updates: Refreshes the view to reflect changes in the system state.
Commands Used:
AddCalendar: Adds a new calendar by loading it from a file.
CreateEvent: Creates a new event.
ModifyEvent: Modifies an existing event.
RemoveEvent: Removes an existing event.
SaveCalendars: Saves the current state of a user's calendar to a file.
ScheduleEvent: Schedules an event automatically based on availability and constraints.
SelectUser: Updates the view to display the schedule of a selected user.
This structure enables the ScheduleViewController to act dynamically in response to user inputs,
maintaining separation of concerns by delegating specific actions to command objects and using the
observer pattern to manage updates and state synchronization between the model and the view.

Command Package:
Command Interface
This interface forms the basis of the Command design pattern, encapsulating an operation as an
executable command, providing a method execute() that implements specific logic associated with the
command.

CreateEvent Class
Implements the Command interface. It handles the creation of new events in the planner system.
Validates input fields, constructs an event from the inputs provided through an EventView, and adds
it to the PlannerSystem.

ModifyEvent Class
Extends CreateEvent to modify existing events. It uses the same validation and setup process but
updates existing events in the planner system rather than creating new ones.

RemoveEvent Class
Also extends CreateEvent, tailored for removing an event from the planner system. It ensures the
event exists and is valid before attempting to remove it, leveraging similar validation processes.

ScheduleEvent Class
Another extension of CreateEvent, this command schedules an event by calculating an optimal time
based on availability and other constraints, using the planner system's scheduling functionalities.

SaveCalendars Class
Implements the Command interface to save a user's calendar state to a file. It interacts with the
PlannerSystemView to get a file path and uses the PlannerSystem to save the calendar data.

SelectUser Class
This command updates the planner system view to display the schedule of a selected user,
demonstrating interaction between the command and the view layers for user-driven events.

AddCalendar Class
Enables adding a calendar by loading it from a file. It opens a file dialog for user interaction and
loads the selected calendar into the planner system, showcasing integration of file handling within
command structures.

These commands provide a modular and flexible way to handle various operations within the planner
system, each encapsulating specific logic to execute distinct actions, from creating and modifying
events to saving and loading calendar data.

MAIN CLASS:

The Main class in the controller package serves as the entry point for the Planner System
application. It demonstrates how the system can be set up and run, offering the flexibility to
select different scheduling strategies based on command-line arguments. Here’s a breakdown of its
functionalities and flow:

Main Functionalities
Event and Schedule Creation: The class creates sample events and schedules for demonstration
purposes.
Command-Line Strategy Selection: Based on the command-line arguments, the class selects a scheduling
strategy to demonstrate different behaviors such as "Anytime", "Work-hours", or "Lenient".
Additionally, a provider's planner system can be launched if "Provider" is specified.
System Initialization and View Setup: Initializes the planner system with schedules and sets up the
user interface for interaction.
Provider System Launch: An adapter pattern is used to adapt the planner system for use with a
provider-specific controller if required.

Detailed Flow
Event Initialization:
Three events (event1, event2, event3) are created with various details such as names, times,
locations, and invitees.

Schedule Creation:
Two schedules (tobeSchedule and karinaSchedule) are created and populated with the events.

System and View Setup:
Depending on the command-line arguments, the planner system (NUPlannerSystem) is initialized with
the list of schedules. If a specific provider is indicated with "Provider", it launches the
provider’s planner system using an adapter (NUPlannerSystemAdapter).

Strategy Selection:
A scheduling strategy is selected based on the command-line input using a ScheduleStrategyCreator
that supports "Anytime", "Work-hours", and "Lenient". If an unsupported strategy is specified, an
exception is thrown.

Controller and View Initialization:
For the default system (not the provider), a PlannerSystemViewImpl is created, and a
ScheduleViewController is instantiated with this view. The controller then launches the system,
displaying the GUI and allowing user interaction.

Use of Design Patterns
Command Pattern: Used in handling system operations like creating, modifying, and deleting events.
Observer Pattern: Implemented in the system to update the view when the underlying model changes.
Adapter Pattern: Demonstrated in integrating an external provider’s system with the main planner
system.

This class provides a comprehensive demonstration of integrating various design patterns and system
components to create a flexible and extensible planner system. It shows effective use of
object-oriented principles to facilitate ease of maintenance, extension, and integration with
external systems.

PLANNER SYSTEM USER GUIDE:
Welcome to the Planner System User Guide! This guide will help you navigate the use of the Planner
System application effectively, from setting up and launching the application to managing events and
schedules. Here's everything you need to know to get started.

Command Line Arguments
When starting the Planner System, you can specify the scheduling strategy via command line
arguments:
First Command Line Argument:
"Anytime": Launches with anytime scheduling (default).
"Work-hours": Configures the system for scheduling during standard work hours.
"Lenient": Adopts a lenient approach to scheduling, allowing for greater flexibility.

Second Command Line Argument:
This tells the model and the view the first day of the week.
This should be a day of the week. any capitalization is accepted e.g "SUNDAY", "Sunday", "sunday".

Entering Event Details:
When creating or modifying an event, you will need to input the following details:
Event Name:
Input: Enter a valid string that is neither empty nor blank.
Location:
isOnline: Choose "Yes" if the event is online, otherwise select "No".
Place: Provide the physical location of the event as a non-empty and non-blank string.
Time Details:
Start Day and End Day: Select the appropriate day from the dropdown menu.
Start Time and End Time: Input time in the "HH:mm" format (e.g., "13:45").
Available Users:
List all participants, one per line. Ensure each name is a non-empty and non-blank string.
Duration (For Scheduled Events Only):
Enter the duration of the event in minutes as a whole number greater than zero.

Features Overview
Select User
Navigate to the user options box located at the bottom left of the schedule. Expand and select a
user to view their schedule. This action is necessary for most functionalities, except
"Add Calendar".

Save Calendar
Navigate to "File" in the menu bar, select "Save calendars", specify a file name or path, and click
"Save". This action saves the current user's schedule in XML format. Ensure a user is selected;
otherwise, an error will be displayed.

Add Calendar
Under "File" in the menu bar, select "Add calendar", choose a file, and click "Open". If successful,
updates will reflect immediately; otherwise, an error message will appear.

Modify Event
After selecting a user, click on an event in their schedule to open the event frame. Modify details
as needed and press "Modify event". Changes will appear on the planner if successful.

Create Event
Select a user and click "Create event". Fill out the event details in the new frame and click
"Create event". If successful, the new event will be added to the planner.

Schedule Event
Select a user and click "Schedule event". Enter the event details in the new frame and click
"Schedule event". The system attempts to find a suitable time slot; if successful, the event is
added.

Remove Event
Select a user and click an existing event to open the event frame. Click "Remove event" to delete
it. If successful, the event will disappear from the planner. By following these instructions,
you can maximize the efficiency and functionality of your Planner System. Should you encounter
any issues, the system will provide error messages to assist with troubleshooting.

Changes for Customers
Consolidation of Controllers
In our previous architecture, the system employed two separate controllers, each tasked with
managing different aspects of the application: one for the schedule view and another for the event
view. These controllers were not only responsible for handling the business logic but also extended
mouse adapters and implemented action listeners to manage user interactions directly. This approach
led to a complex and tightly coupled system where controllers handled both logic and user
interaction, making the code harder to maintain and extend.

To streamline our application and enhance its maintainability, we made significant changes to how
controllers and views interact. In our revised design, the views themselves now directly handle
user interactions by extending mouse adapters and implementing action listeners. This shift allows
views to manage all user interactions internally and only call the controller when an action needs
to be processed or business logic needs to be executed. This change is in line with the command
callback design pattern, where views listen for actions within themselves and utilize callbacks to
communicate with the controller.

Consequently, our unified controller no longer extends mouse adapters or implements action
listeners. Instead, it focuses solely on handling the commands issued by the views. This separation
of concerns reduces the complexity of the controller, making the system simpler for our customers
to use and adapt. It also reduces the coupling between the user interface and the business logic,
enhancing the system's flexibility and making it more robust for integration with other systems or
adaptations for different use cases.

Introduction of Interfaces for Schedules and Events
To enhance the flexibility and adaptability of our Planner System for different providers, we
introduced interfaces for our schedule and event classes. Specifically, we created ISchedule
for the schedule class and ReadOnlyEvent and IEvent for the event class. These interfaces allow for
a more flexible system that can be easily adapted or extended according to the specific needs of our
customers and providers.

The use of interfaces segregates the implementation details from the usage, allowing other systems
or modules to interact with our Planner System without depending on concrete implementations. This
abstraction facilitates easier modifications and integrations with other systems, making our
solution more versatile and accommodating to various use cases.

Refinement of View Code
As part of our ongoing commitment to provide clean, maintainable, and easy-to-adapt code to our
customers, we conducted a thorough review and refactoring of our view code. Specifically, we
identified and addressed areas where methods in the view layer were directly calling external
validation classes or non-common interfaces, which could potentially hinder adaptability and
increase the complexity for customers trying to integrate our system into their environments.

To resolve these issues, we replaced these external calls with private methods within the view
classes themselves. These private methods encapsulate the necessary validation logic internally,
reducing dependencies on external components and simplifying the code structure. This adjustment
makes it easier for customers to modify or extend the view logic to fit their specific requirements
without the need to untangle external dependencies.

These changes collectively aim to make the Planner System not only more user-friendly but also
more adaptable and easier to integrate with other systems, providing our customers with a solution
that is both powerful and flexible.

Changes for provider:
We were able to get everything working based on the capability of the model interfaces, controller
interfaces and view interfaces and implementations given to us by our providers. Our system now
fully supports the execution of our provider's code directly from our main class. For instructions
on how to run it, please refer to the 'PLANNER SYSTEM USER GUIDE' section earlier in this document.

EXTRA CREDIT:

Here's an enhanced version of your description:

Changing the Color of Events

Files Affected:
PlannerSystemViewImpl class
SchedulePanel class
ScheduleViewController class

New Files Added:
ColorEvent interface
BasicColorEvent class
RedDecorator
BlueHostDecorator

The decorator pattern is employed to instruct the SchedulePanel on the appropriate color to use for
painting events based on whether the current user is the host. Initially, the SchedulePanel utilizes
the RedDecorator, which dictates that all events should be colored red. Additionally, the
SchedulePanel includes a boolean field that tracks whether the color decorator has been toggled.
When toggled, the panel switches between the RedDecorator and the BlueHostDecorator.

Furthermore, the PlannerSystemViewImpl class has been updated to include a button that controls the
toggling of event colors. Pressing this button triggers an action command that is handled by the
ScheduleViewController, which in turn manages the subsequent changes in the decorator state,
ensuring dynamic updates to event presentation based on user interaction.

When color is toggled. Event in dark blue are events the current user is hosting while the events
in red are the events the user isn't hosting. When color is not toggled, all events are in red.

Week Starting on Saturday

Files Affected:
Planner System interfaces and implementations
Event interfaces and implementations
Schedule interfaces and implementations
Time interfaces and implementations

Files Added:
No new files added
The planner system robustly synchronizes the selected start day of the week across all relevant
classes. This synchronization allows these components to accurately perform time-related
calculations with reference to the designated start day. This capability ensures the system's
versatility, enabling it to adapt seamlessly to any day of the week as the starting point.

Week Beginning on Saturday Visualization
Files Modified: PlannerSystemViewImpl, SchedulePanel class, and EventClickListener.
Files Added: No new files added.

The planner system seamlessly integrates the designated start day of the week across various
components, including the view, schedule panel, and event click listener. This ensures that these
elements adjust dynamically to the selected first day of the week, enhancing the system's
adaptability and user experience."


Here's a revised and clearer version of your description:

Strategizing Starting on Saturday:

Files Modified:
Schedule strategy interface and implementations

Files Added:
No new files added

We have enhanced the schedule strategy implementations by introducing a "first day of the week"
field. This new field allows for external configuration by the planner system or the client,
enabling the strategy to base its calculations on this specified starting day. The adjustments
include accurately determining the event's start day, start time, end day, and end time in relation
to the selected first day of the week. As a result, the Anytime Scheduling strategy now initiates
scheduling from 00:00 of the provided first day, ensuring all time calculations align with this
new start point.

