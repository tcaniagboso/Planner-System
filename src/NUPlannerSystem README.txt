NUPlannerSystem README
Overview
The NUPlannerSystem codebase is designed to provide a comprehensive solution for managing event
schedules for users. It enables the creation, modification, and deletion of events within
user-specific schedules, ensuring no time overlaps between events. The system assumes familiarity
with basic scheduling concepts and is built to cater to environments where managing multiple events
efficiently is crucial. It assumes that users will have unique identifiers and that events can span
across different times and potentially locations, both physical and online.

Quick Start
To get started with NUPlannerSystem, you'll first need to create a PlannerSystem instance.
Then, copy the xml files in the 'test' folder and paste it outside the 'src' folder. You can read
schedules from XML files, create new events, and save schedules back to XML.
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

NUPlannerSystem Class: Implements the PlannerSystem interface, providing functionality for
managing schedules across multiple users.

Schedule Class: Represents a user's schedule, encapsulating a list of events.

Event Class: Represents a single event, containing details such as name, timing, location,
and participants.

Time and Location Classes: Utility classes used within Event to represent the time and location of
an event, respectively.

Key Subcomponents
TimeUtilities and ValidationUtilities: Provide helper methods for time format conversion and input
validation across the system.

ScheduleXMLWriter: Facilitates writing a Schedule object back to an XML file, allowing persistence
of schedule data.

Source Organization
schedule package: Contains core classes like Event, Schedule, and utility classes for managing
schedules (TimeUtilities).

plannersystem package: Contains the NUPlannerSystem class and the PlannerSystem interface,
orchestrating the overall functionality.

validationutilities package: Provides validation methods used throughout the system to ensure
data integrity.

XML Parsing and Writing: The ScheduleXMLWriter class in the schedule package handles XML output.
Input parsing and object construction from XML are managed within NUPlannerSystem.

This outline provides a high-level understanding of the NUPlannerSystem, guiding users through
its primary functionalities and organizational structure. For detailed method usage and system
capabilities, refer to the Javadoc comments within each class and interface.