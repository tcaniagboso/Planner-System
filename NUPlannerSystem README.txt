Overview
The NUPlannerSystem codebase is designed to provide a comprehensive solution for managing event
schedules for users. It enables the creation, modification, and deletion of events within
user-specific schedules, ensuring no time overlaps between events. The system assumes familiarity
with basic scheduling concepts and is built to cater to environments where managing multiple events
efficiently is crucial. It assumes that users will have unique identifiers and that events can span
across different times and potentially locations, both physical and online.

Quick Start
To get started with NUPlannerSystem, you'll first need to create a PlannerSystem instance.
Then, copy the XML files in the 'test' folder and paste them outside the 'src' folder.
You can read schedules from XML files, create new events, and save schedules back to XML.

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
Key Components
PlannerSystem Interface: Serves as the primary entry point for interacting with user schedules.
It defines methods for reading, creating, modifying, and displaying schedules.
NUPlannerSystem Class: Implements the PlannerSystem interface, providing functionality for managing
schedules across multiple users.
Schedule Class: Represents a user's schedule, encapsulating a list of events.
Event Class: Represents a single event, containing details such as name, timing, location, and
participants.
Time and Location Classes: Utility classes used within Event to represent the time and location of
an event, respectively.
Key Subcomponents
TimeUtilities and ValidationUtilities: Provide helper methods for time format conversion and
input validation across the system.
ScheduleXMLWriter: Facilitates writing a Schedule object back to an XML file, allowing persistence
of schedule data.
Source Organization
schedule package: Contains core classes like Event, Schedule, and utility classes for managing
schedules (TimeUtilities).
plannersystem package: Contains the NUPlannerSystem class and the PlannerSystem interface,
orchestrating the overall functionality.

validationutilities package: Provides validation methods used throughout the system to ensure data
integrity.

XML Parsing and Writing: The ScheduleXMLWriter class in the schedule package handles XML output.
Input parsing and object construction from XML are managed within NUPlannerSystem.

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

Changes for Part 2
In this update, significant enhancements and bug fixes have been implemented to improve the
functionality and usability of the NUPlannerSystem. Here's a summary of the key changes:

Constructor Enhancement
A new constructor has been added to the NUPlannerSystem class, allowing the initialization of the
system with a valid list of schedules. This enhancement facilitates the setup of the system with
predefined user schedules, enhancing the system's flexibility and ease of use right from the start.

Bug Fixes in Event Management
All outstanding bugs in the removeEvent and modifyEvent methods have been addressed. These fixes
ensure more robust and error-free event management, allowing users to modify and remove events from
schedules without encountering unexpected behavior.

Streamlined Event Modification
The overloading of the modifyEvent method has been eliminated in favor of a single, comprehensive
method that accepts all relevant fields for modifying an event. This change was motivated by the
recognition that when users modify an event through a graphical user interface (GUI), it is
necessary to validate all fields again, even if some were unchanged. The streamlined approach
reduces complexity and enhances the system's efficiency in handling event modifications.

Introduction of ReadonlyPlannerSystem Interface
The PlannerSystem interface now extends a newly introduced ReadonlyPlannerSystem interface.
This new interface encapsulates the methods that provide a read-only view of the planner system,
distinguishing between methods that modify the system's state and those that only retrieve data.
This separation of concerns supports the development of components that interact with the planner
system without the risk of inadvertently altering its state.

New GUI Classes
A suite of GUI classes has been added, significantly enhancing the system's user interface.
These classes include:

EventClickListener: A listener for mouse click events on the schedule panel, enabling the selection
and interaction with events displayed on the schedule.

EventView: A comprehensive GUI for viewing and interacting with events. Users can create, modify,
and remove events through a user-friendly interface.

PlannerSystemView: The main GUI for the planner system, providing functionalities for managing
events and schedules, loading and saving calendar data, and displaying user schedules in a visually
engaging format. NOTE: if the view is bigger than the screen, adjust the frame size in this class.

SchedulePanel: A graphical panel that displays the schedule grid and events, offering a clear and
interactive visualization of users' schedules.

These GUI enhancements not only improve the aesthetics and user experience of the NUPlannerSystem
but also provide intuitive and efficient ways for users to manage their schedules and events.

Changes for Part 3:
Interface and GUI Enhancements
PlannerSystemView Interface
The PlannerSystemView interface defines essential operations for the planner system's graphical user
interface (GUI). It includes methods for setting action and mouse listeners, displaying error
messages, loading and saving files, refreshing the view, and updating user and schedule information.
This interface serves as a contract for implementing various GUI components that interact with the
user, facilitating a dynamic and responsive user experience.

Implementation and GUI Components
PlannerSystemViewImpl Class: Implements the PlannerSystemView interface, representing the main
window of the Planner System application. It offers functionalities for event creation, scheduling,
loading/saving calendars, and displaying schedules, integrating these operations into a cohesive
user interface.

EventView Interface: Defines operations for an event-specific view within the GUI, supporting
actions such as refreshing the view, displaying errors, and managing event details. It ensures that
GUI components handling event data provide a consistent set of functionalities.

EventViewImpl and ScheduleEventView Classes: Provide concrete implementations of the EventView
interface for different contexts—EventViewImpl for general event management and ScheduleEventView
for scheduling specific events. These classes enable detailed interactions with individual events,
including creating, modifying, and scheduling operations.

SchedulePanel Class: A specialized JPanel subclass designed to graphically display a user's
schedule within the Planner System. It draws a schedule grid, represents events, and allows for
interactive schedule visualization, enhancing the overall user interaction with the system's
scheduling functionalities.

Observer Pattern Integration
Observer Interface: Introduced as part of implementing the Observer design pattern, the Observer
interface allows for a subscription mechanism where multiple objects can listen and react to events
or changes in another object, known as the subject. This integration facilitates communication
between the model (or controller) and the views or other components that need to react to changes
within the system.

Controller Enhancements
PlannerSystemController Interface: Defines the essential operations that a planner system controller
should support, acting as an intermediary between the view and the model. It handles user actions,
updates the model, or view accordingly, and integrates with the Observer pattern to respond to model
updates.

ScheduleViewController Class: Manages user actions and mouse events within the Planner System
application, serving as a bridge between the view (PlannerSystemView) and the model (PlannerSystem).
It listens for GUI actions, processes mouse clicks on the schedule panel, and updates the model or
view based on user interactions.

Command Pattern Utilization
Command Interface: Part of the Command design pattern, encapsulating a request as an object.
This allows for dynamic operation execution, including creating, modifying, and scheduling events,
as well as saving calendars and selecting users.

Concrete Commands: Including AddCalendar, CreateEvent, ModifyEvent, RemoveEvent, ScheduleEvent,
SaveCalendars, and SelectUser. These commands encapsulate specific actions related to event and
schedule management within the planner system, offering flexibility in operation execution.

ScheduleStrategy Interface
The ScheduleStrategy interface defines a contract for auto-scheduling events. Implementations of
this interface are responsible for scheduling an event at an appropriate time slot, considering the
duration and avoiding conflicts with existing schedules.

Method:
scheduleEvent(Event event, int duration, List<Schedule> scheduleList): Schedules an event based on
its duration and a list of existing schedules, returning the event with updated scheduling details
if successful.

AnyTimeScheduleStrategy Class
Implements the ScheduleStrategy interface to provide a scheduling strategy that attempts to find the
earliest possible time for an event within a week, ensuring there is no overlap with existing
events.

Key Features:
Schedules events at the earliest possible time within the week.
Ensures the scheduled event does not conflict with existing scheduled events.

WorkHourScheduleStrategy Class
Extends AnyTimeScheduleStrategy to specifically target standard work hours
(09:00 to 17:00, Monday to Friday) for scheduling events. This class represents a more restrictive
scheduling strategy, focusing on business hours.

Key Features:
Schedules events within standard work hours and workdays.
Adheres to a more structured scheduling framework suitable for professional settings.

LenientScheduleStrategy Class
Extends WorkHourScheduleStrategy to introduce a lenient approach to scheduling. This strategy aims
to schedule events even if not all invitees are available, requiring only the host and at least one
other invitee to be present.

Key Features:
Adopts a flexible scheduling strategy, prioritizing the host and at least one other invitee's
availability.Useful in scenarios where full attendance is not critical, allowing for greater
flexibility in scheduling.

ScheduleStrategyCreator Class
A factory class designed to generate instances of scheduling strategies based on specified types.
This class supports the creation of different scheduling strategies, facilitating dynamic strategy
selection based on application needs.

Functionality:

Provides a method to create instances of AutoSchedule according to the specified strategy type.
Supports various scheduling strategies, including 'Anytime', 'WorkHours', and 'Lenient',
accommodating a wide range of scheduling requirements and preferences.

Through these additions, the scheduling component now boasts enhanced flexibility,
offering multiple strategies to cater to diverse scheduling needs. Whether the requirement is for
strict adherence to work hours, a lenient approach to invitee availability, or any other
scheduling constraint, these new classes and interfaces provide the necessary tools to implement
efficient and effective scheduling solutions within your application.

NUPLANNERSYSTEM CLASS:
Observer Pattern Implementation
Fields Added: private final List<Observer> observers = new ArrayList<>();
Purpose: This field supports the Observer design pattern, allowing the system to maintain a list of
observers that should be notified upon certain changes, such as updates to schedules or events.
Implications: The inclusion of this pattern facilitates a more modular and decoupled design.
Components of the system can now "listen" to changes without being directly linked to the source of
these changes, enhancing the system's maintainability and scalability.

Scheduling Strategy Flexibility
Field Added: private ScheduleStrategy scheduleStrategy;
Purpose: This field allows the system to utilize different strategies for scheduling events
dynamically. It represents a shift towards a strategy pattern, enabling the selection and
application of various scheduling algorithms at runtime.
Implications: This change introduces greater flexibility in how events are scheduled within
the system. By abstracting the scheduling logic into separate strategy implementations,
the system can easily adapt to different scheduling requirements and constraints without
modifying the core logic.

New Methods Overview
ScheduleEvent: Implements automatic scheduling of events based on the current scheduling
strategy. It demonstrates the system's ability to dynamically schedule events, considering the
availability of invitees and the specifics of the scheduling strategy in use.
addObserver and removeObserver: Facilitate the dynamic management of observers. These methods
allow the system to add or remove components interested in being notified about changes, supporting
a dynamic and responsive architecture.
setScheduleStrategy: Enables the dynamic selection of a scheduling strategy. This method allows the
system to change its scheduling behavior at runtime, adapting to different needs or preferences.
checkEventConflict: Offers a utility method to check for potential conflicts when scheduling an
event, enhancing the system's robustness and reliability in managing event schedules.
notifyObservers: Encapsulates the logic for notifying all registered observers about changes,
ensuring that all interested parties are informed about updates in a consistent manner.

Conclusion
These enhancements significantly improve the NUPlannerSystem's architecture by incorporating
design patterns that promote flexibility, modularity, and decoupling. The Observer pattern
allows for efficient notification mechanisms, while the strategy pattern enables flexible
scheduling algorithms. Together, these changes make the system more adaptable, maintainable,
and capable of meeting diverse scheduling needs.

MAIN CLASS:
The Main class in the Planner System application serves a crucial role as the entry point where the
application's core functionalities are demonstrated and initiated. This class showcases how the
Planner System can be utilized, from creating events and schedules to initializing the view and
selecting scheduling strategies based on input arguments. Here’s a detailed look into its
functionalities and the design decisions it illustrates:

Core Functionalities
Event and Schedule Creation: The class starts by creating sample events and assigning them to user
schedules. This demonstrates the fundamental operations of the Planner System - managing events and
their allocation to different users.
Scheduling Strategy Assignment: It further allows for the dynamic selection of scheduling strategies
through command-line arguments. This flexibility shows the application's adaptability to different
scheduling needs, such as "Any time," "Work hours," and "Lenient" scheduling strategies.
Initialization of the View and Controller: It initializes the user interface (PlannerSystemViewImpl)
and links it with the model (NUPlannerSystem) through a controller (ScheduleViewController),
demonstrating the application's adherence to the Model-View-Controller (MVC) architectural pattern.
Design Decisions
Strategy Pattern for Scheduling: The dynamic selection of scheduling strategies showcases the
Strategy pattern, where the scheduleStrategy can be switched at runtime depending on the user's
needs. This design choice enhances the system's flexibility and extensibility.
Model-View-Controller (MVC) Architecture: By separating concerns among the model (planner system),
view (system view), and controller (schedule view controller), the application promotes modularity,
scalability, and maintainability. This architectural pattern facilitates easier updates and
modifications to the system.
Command-line Interface for Strategy Selection: Utilizing command-line arguments for the selection of
scheduling strategies illustrates a simple yet effective way to interact with the system.
This choice underscores the application's potential for customization and adaptability to user
preferences or operational requirements.
Implications
The Main class not only kick-starts the Planner System application but also encapsulates several key
software design principles and patterns, making the system more robust, adaptable, and
user-friendly. By demonstrating the system's core functionalities and its architectural choices,
the Main class serves as an essential component that ties together the different aspects of the
Planner System, providing a comprehensive overview of how the system operates and interacts with
its users.

EXTRA CREDIT:

Resizable View:
In the latest update to the Planner System, significant enhancements were made to the user
interface's adaptability and user experience. A key improvement is the transition from hardcoded
values for component sizes and positions within the PlannerSystemView and SchedulePanel to dynamic
calculations based on the frame or panel's current dimensions. This change makes the Planner System
view resizable, offering a more flexible and user-friendly interface. Here's an overview of the
implications of these adjustments:

Dynamic Sizing and Positioning
Adaptive Layout: By calculating component sizes and positions using getWidth() and getHeight(),
the layout now adapts seamlessly to changes in the window size. This adaptability ensures that the
user interface remains coherent and visually appealing across a wide range of window sizes and
screen resolutions.
Enhanced User Experience: Users can now resize the application window according to their preference
or the needs of their display setup, making the Planner System more versatile and accessible.
This responsiveness contributes to a more personalized and comfortable user experience.
Scalability: The dynamic approach to layout management lays a solid foundation for future
enhancements and features. As new components are added or existing ones are modified, the
application can easily accommodate these changes without the need for extensive redesigns or
adjustments to hardcoded values.

LenientSchedule Class
Extends WorkHourSchedule to introduce a lenient approach to scheduling. This strategy aims
to schedule events even if not all invitees are available, requiring only the host and at least one
other invitee to be present.

Key Features:

Adopts a flexible scheduling strategy, prioritizing the host and at least one other invitee's
availability.Useful in scenarios where full attendance is not critical, allowing for greater
flexibility in scheduling.

TUTORIAL:
Command Line Arguments: Enter "Anytime" anytime scheduling, "Work-hours" for scheduling work hours,
and "Lenient" for lenient scheduling. The default scheduling strategy is set to anytime scheduling.
Enter "Home" as a second command line argument or no additional argument to launch the home planner
system and enter "Provider" to launch the provider's planner system.

Entering Event Details:
Event name: Enter the name of the event as a non-empty and non-blank string.
Location:
    isOnline: Select "Yes", if it is online or "No", if it isn't online.
    Place: Enter the physical location of the event as a non-empty and non-blank string.
Start Day: Choose any of the days in the combo box.
Start-Time: Start time must be entered exactly in the format "HH:mm".
End-Day: Choose any of the days in the combo box.
End-Time: End time must be entered exactly in the format "HH:mm".
Available users: Every user must be a non-empty and non-blank string. Every user should be written
on a new line.
Duration(Schedule event only): Enter a whole number that is greater than 0.

Features:
Select user: You can select a user's schedule to display by expanding the user options box, at the
bottom left of the planner system schedule, and selecting a user. NOTE: Every feature, barring
"Add Calendar" requires a user to be selected.

Save Calendar: Click on "File" in the menu bar then Click on "Save calendars" and enter the name of
the file or file path then press "Save" to save the current user's schedule as an xml file with that
file name or path. NOTE: An available user must be selected. If successful the new xml file will
exist in it's desired directory, otherwise the system would display an error message.

Add Calendar: Click on "File" in the menu bar then Click on the "Add calendar" and select the file
you want to load into the planner system and click on "Open". If successful, the change would be
reflected on the planner, otherwise the system would display an error message.

Modify Event: To modify an event, you have to select an available user and click on an existing
event in their schedule and the event frame would pop up. Enter the new details of the event and
click on the "Modify event" button. If successful, the change would be reflected on the planner,
otherwise the system would display an error message.

Create Event: To create an event, you have to select an available user and click on the
"Create event" button and a new event frame would pop up. Enter the details of the event and click
on the "Create event" button and the system would attempt to create the event. If successful, the
change would be reflected on the planner, otherwise the system would display an error message.

Schedule Event: To schedule an event, you have to select an available user and click on the
"Schedule event" button and a new schedule event frame would pop up. Enter the details of the event
and click on the "Schedule event" button and the system would attempt to schedule an event.
If successful, the change would be reflected on the planner, otherwise the system would display an
error message.

Remove Event: To remove an event, you have to select an available user and click on an existing
event in their schedule and the existing event frame would pop up. Click on the "Remove event"
button and the system would attempt to remove the event using its defined logic. If successful, the
change would be reflected on the planner, otherwise the system would display an error message.
























