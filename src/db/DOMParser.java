package db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class DOMParser {

	  public static Configuration loadConfig(String configName) throws SAXException, IOException, ParserConfigurationException {
	    //Get the DOM Builder Factory
	    DocumentBuilderFactory factory = 
	        DocumentBuilderFactory.newInstance();
	    
	    //Get the DOM Builder
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    //Load and Parse the XML document
	    //document contains the complete XML as a Tree.
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    Document document = 
	      builder.parse(
	        classLoader.getResourceAsStream("/xml/config.xml"));
	    
	    List<Configuration> configList = new ArrayList<Configuration>();
	    //Iterating through the nodes and extracting the data.
	    NodeList nodeList = document.getDocumentElement().getChildNodes();

	    for (int i = 0; i < nodeList.getLength(); i++) {

	      //We have encountered an <configuration> tag.
	      Node node = nodeList.item(i);
	      if (node instanceof Element) {
	        Configuration config = new Configuration();
	        config.name = node.getAttributes().
	            getNamedItem("name").getNodeValue();

	        NodeList childNodes = node.getChildNodes();
	        for (int j = 0; j < childNodes.getLength(); j++) {
	          Node cNode = childNodes.item(j);

	          //Identifying the child tag of configuration encountered. 
	          if (cNode instanceof Element) {
	            String content = cNode.getLastChild().
	                getTextContent().trim();
	            switch (cNode.getNodeName()) {
	              case "userid":
	                config.userid = content;
	                break;
	              case "password":
	                config.password = content;
	                break;
	              case "url":
	                config.url = content;
	                break;
	              case "driver":
	            	  config.driver = content;
	            }
	          }
	        }
	        configList.add(config);
	      }

	    }

	    //Printing the Configuration list populated.
	    for (Configuration config : configList) {
	      System.out.println(config);
	      if (config.name == configName) {
	    	  	return config;	  
	      }
	    }
	    return null;
	  }
	}

	class Configuration{
	  String name, userid, password, url, driver;
	  
	  @Override
	  public String toString() {
	    return name+" "+userid+"("+password+")"+url+" "+driver;
	  }
	}
