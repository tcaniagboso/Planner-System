package schedule;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;

import javax.xml.transform.OutputKeys;

/**
 * Provides functionality to write schedule information into an XML file.
 * This class enables the serialization of a {@link Schedule} object to XML, preserving
 * event details, including names, times, locations, hosts, and invitees.
 */
public class ScheduleXMLWriter {


  /**
   * Writes the provided {@link Schedule} object to an XML file at the specified file path.
   * This method serializes the entire schedule, including all events and their details,
   * into an XML structure and saves it to a file.
   *
   * @param schedule The {@link Schedule} object to be serialized to XML.
   * @param filePath The file path where the XML file will be saved.
   * @throws Exception if an error occurs during document building, XML serialization,
   *                   or file writing. Specific exceptions might include
   *                   {@link javax.xml.parsers.ParserConfigurationException},
   *                   {@link javax.xml.transform.TransformerConfigurationException},
   *                   {@link javax.xml.transform.TransformerException},
   *                   and others related to IO operations.
   */
  public static void writeScheduleToXML(Schedule schedule, String filePath) throws Exception {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document = documentBuilder.newDocument();

    Element root = document.createElement("schedule");
    root.setAttribute("id", schedule.getUserId());
    document.appendChild(root);

    for (Event event : schedule.getEvents()) {
      Element eventElement = document.createElement("event");
      root.appendChild(eventElement);

      Element nameElement = document.createElement("name");
      nameElement.setTextContent(event.getName());
      eventElement.appendChild(nameElement);

      Element timeElement = document.createElement("time");
      appendTimeDetails(document, timeElement, event.getTime());
      eventElement.appendChild(timeElement);

      Element locationElement = document.createElement("location");
      appendLocationDetails(document, locationElement, event.getLocation());
      eventElement.appendChild(locationElement);

      Element usersElement = document.createElement("users");

      // Add host first
      Element hostElement = document.createElement("uid");
      hostElement.setTextContent(event.getHost());
      usersElement.appendChild(hostElement);

      // Add invitees, avoiding duplication with the host
      for (String invitee : event.getInvitees()) {
        if (!invitee.equals(event.getHost())) { // Avoid duplication
          Element uidElement = document.createElement("uid");
          uidElement.setTextContent(invitee);
          usersElement.appendChild(uidElement);
        }
      }
      eventElement.appendChild(usersElement);
    }

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource domSource = new DOMSource(document);
    StreamResult streamResult = new StreamResult(new File(filePath));
    transformer.transform(domSource, streamResult);
  }

  /**
   * Appends time details of an event to the provided time XML element.
   * This helper method constructs and appends XML elements representing the start and end times
   * (including days and times) of an event to a parent time XML element.
   *
   * @param doc         The XML {@link Document} being constructed.
   * @param timeElement The parent XML {@link Element} to which time details are appended.
   * @param time        The {@link Time} object containing the event's time details.
   */
  private static void appendTimeDetails(Document doc, Element timeElement, Time time) {
    Element startDayElement = doc.createElement("start-day");
    startDayElement.setTextContent(TimeUtilities.formatDay(time.getStartDay()));
    timeElement.appendChild(startDayElement);

    Element startElement = doc.createElement("start");
    startElement.setTextContent(TimeUtilities.formatTime(time.getStartTime()));
    timeElement.appendChild(startElement);

    Element endDayElement = doc.createElement("end-day");
    endDayElement.setTextContent(TimeUtilities.formatDay(time.getEndDay()));
    timeElement.appendChild(endDayElement);

    Element endElement = doc.createElement("end");
    endElement.setTextContent(TimeUtilities.formatTime(time.getEndTime()));
    timeElement.appendChild(endElement);
  }

  /**
   * Appends location details of an event to the provided location XML element.
   * This helper method constructs and appends XML elements representing whether the event is online
   * and its physical location (if applicable) to a parent location XML element.
   *
   * @param doc             The XML {@link Document} being constructed.
   * @param locationElement The parent XML {@link Element} to which location details are appended.
   * @param location        The {@link Location} object containing the event's location details.
   */
  private static void appendLocationDetails(Document doc, Element locationElement,
                                            Location location) {
    Element onlineElement = doc.createElement("online");
    onlineElement.setTextContent(String.valueOf(location.isOnline()));
    locationElement.appendChild(onlineElement);

    Element placeElement = doc.createElement("place");
    placeElement.setTextContent(location.getLocation());
    locationElement.appendChild(placeElement);
  }
}
