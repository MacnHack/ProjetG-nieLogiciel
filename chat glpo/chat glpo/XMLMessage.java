import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLMessage {
    	private TransformerFactory transformerFactory;
	private Transformer transformer;
	private DocumentBuilderFactory documentFactory;
	private DocumentBuilder documentBuilder;
	
/**
* Le nom du fichier XML qu'on lit
*/
	private static String XML_INPUT_FILE = "message.xml";
/**
* Le nom du fichier XML dans lequel on Ã©crit
*/
	private static String XML_OUTPUT_FILE = "message.xml";

	public XMLMessage() {
		try {
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
		} catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
	}

/**
* La methode qui transforme le document en memoire en fichier XML sur le disque dur
* @param document le document Ã  transformer
* @param filePath le chemin (rÃ©pÃ©rtoire et nom) du fichier XML Ã  crÃ©er 
*/	
	public void createXMLFile(Document document, String filePath)
	{
		try {
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(new File(filePath));

		// If you use
		// StreamResult result = new StreamResult(System.out);
		// the output will be pushed to the standard output ...
		// You can use that for debugging 

        //transform the DOM Object to an XML File
		transformer.transform(domSource, streamResult);
		
		} catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
		System.out.println("Done creating XML File");
	}

/**
* La methode qui crÃ©e le document en memoire
* @return le document crÃ©Ã©
*/	
	public Document createXMLDocument()
	{
		return documentBuilder.newDocument();
	}		

/**
* La methode qui lit un fichier XML et le transforme en liste de noeuds en mÃ©moire
* @param filePath le chemin (rÃ©pÃ©rtoire et nom) du fichier XML Ã  lire 
* @return la liste des noeuds lus
*/	
	public NodeList parseXMLFile (String filePath) {
		NodeList elementNodes = null;
		try {
			Document document= documentBuilder.parse(new File(filePath));
			Element root = document.getDocumentElement();
			
			elementNodes = root.getChildNodes();	
		}
		catch (SAXException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return elementNodes;
	}

/**
* Methode pour dÃ©montrer la lecture d'un fichier XML qui contient plusieurs Ã©lÃ©ments
*/
	public void readXML(List<Message>listMessage) {
		NodeList nodes = this.parseXMLFile(XML_INPUT_FILE);
		Message messageTempo;
		if (nodes == null) return;
		
		for (int i = 0; i<nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element currentElement = (Element) nodes.item(i);
				if (currentElement.getNodeName().equals("message")) 	{
					try {
						String text = currentElement.getElementsByTagName("text").item(0).getTextContent();
						String date = currentElement.getElementsByTagName("date").item(0).getTextContent();
                                                String nom = currentElement.getElementsByTagName("nom").item(0).getTextContent();
						String uuidM = null;
						UUID messageID = null;
                         
						try {
							uuidM = currentElement.getElementsByTagName("messageUUID").item(0).getTextContent();
						}
						catch (Exception ex) {
							System.out.println("Empty UUID, will create a new one");
						}
						if ((uuidM == null)  || (uuidM.isEmpty()))
							messageID = UUID.randomUUID();
						else messageID = UUID.fromString(uuidM);
        
						//verify that I read everything correctly:
						messageTempo = new Message(text,date,nom,messageID);
						listMessage.add(messageTempo);
						System.out.println(date + " " + text + " " + nom + " " + messageID.toString());
					} catch (Exception ex) {
						System.out.println("Something is wrong with the XML Message element");
					}
				}
			}  
		}
	}

/**
* Methode pour dÃ©montrer l'Ã©criture d'un fichier XML avec un seul Ã©lÃ©ment
*/	
	public void writeXML(List<Message>listMessage) {
		Document document = this.createXMLDocument();
		if (document == null) return;
		
 		// create root element
		Element root = document.createElement("messages");
		document.appendChild(root);
		for(Message m: listMessage)
		{
			//save one "client" element; create a loop to save more elements!!
			Element message = document.createElement("message");
			// clientUUID element
			UUID messageID = m.messageId ;
			Element messageMessageUUID = document.createElement("messageUUID");
			messageMessageUUID.appendChild(document.createTextNode(messageID.toString()));
			message.appendChild(messageMessageUUID);
			
			// firstName element
			String text = m.text;
			Element textElement = document.createElement("text");
			textElement.appendChild(document.createTextNode(text));
			message.appendChild(textElement);
					
                        String nom = m.nom;
			Element nomElement = document.createElement("nom");
			nomElement.appendChild(document.createTextNode(nom));
			message.appendChild(nomElement);
                        
			String date = m.date;
			Element dateElement = document.createElement("date");
			dateElement.appendChild(document.createTextNode(date));
			message.appendChild(dateElement);
					
			root.appendChild(message);
		}
		this.createXMLFile(document, XML_OUTPUT_FILE);
	}
    
}
