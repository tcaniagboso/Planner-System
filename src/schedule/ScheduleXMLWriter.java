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
 * Write schedule info for XML file.
 */
public class ScheduleXMLWriter {
  /**
   * Has the provided schedule object for an XML file at the specific path.
   * @param schedule schedule to write XML
   * @param filePath filepath where XML should be saved
   * @throws Exception if an error happens during doc building
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
      for (String uid : event.getInvitees()) {
        Element uidElement = document.createElement("uid");
        uidElement.setTextContent(uid);
        usersElement.appendChild(uidElement);
      }
      eventElement.appendChild(usersElement);
    }

    //  write XML content
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource domSource = new DOMSource(document);
    StreamResult streamResult = new StreamResult(new File(filePath));
    transformer.transform(domSource, streamResult);
  }

  /**
   * Appends the time details for an event XML element.
   * @param doc XML document
   * @param timeElement XML element for where the details will append
   * @param time time object containing the details
   */
  private static void appendTimeDetails(Document doc, Element timeElement, Time time) {
    Element startDayElement = doc.createElement("start-day");
    startDayElement.setTextContent(time.getStartDay().toString());
    timeElement.appendChild(startDayElement);

    Element startElement = doc.createElement("start");
    startElement.setTextContent(time.getStartTime().toString());
    timeElement.appendChild(startElement);

    Element endDayElement = doc.createElement("end-day");
    endDayElement.setTextContent(time.getEndDay().toString());
    timeElement.appendChild(endDayElement);

    Element endElement = doc.createElement("end");
    endElement.setTextContent(time.getEndTime().toString());
    timeElement.appendChild(endElement);
  }

  /**
   * Appends the location details for an XML file.
   * @param doc the XML file
   * @param locationElement location XML element where details will append
   * @param location location object that has details
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
