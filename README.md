### Planner System

A multi-user scheduling application for creating, managing, and automatically scheduling events while preventing time conflicts.

Built using a modular MVC architecture with multiple design patterns for flexibility and scalability.

### Features

- Multi-user calendar management
- Conflict-free event creation, modification, and removal
- Automatic scheduling with multiple strategies
- XML read/write for saving and loading calendars
- GUI for viewing schedules and editing events
- Optional event color highlighting (host vs non-host)

### Scheduling Strategies

- **Anytime**: schedules events at the earliest available time
- **Work-hours**: schedules only during business hours (Mon–Fri, 09:00–17:00)
- **Lenient**: schedules when the host and at least one invitee are available

### Architecture & Design Patterns

- MVC (Model–View–Controller)
- **Command** pattern for user actions (create/modify/remove/schedule/save/load)
- **Observer** pattern to refresh the view when the model changes
- **Strategy** pattern for scheduling logic
- **Adapter** pattern for provider integration
- **Decorator** pattern for event color toggling

### How to Run

```bash
java Main [Anytime | Work-hours | Lenient] [firstDayOfWeek]
```

## Example
```bash
java Main Work-hours Monday
```
