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
engaging format.

SchedulePanel: A graphical panel that displays the schedule grid and events, offering a clear and
interactive visualization of users' schedules.

These GUI enhancements not only improve the aesthetics and user experience of the NUPlannerSystem
but also provide intuitive and efficient ways for users to manage their schedules and events.




