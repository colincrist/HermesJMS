//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.03 at 10:11:50 AM BST 
//


package hermes.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hermes.xml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Content_QNAME = new QName("", "content");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hermes.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLTextMessage }
     * 
     */
    public XMLTextMessage createXMLTextMessage() {
        return new XMLTextMessage();
    }

    /**
     * Create an instance of {@link XMLMapMessage }
     * 
     */
    public XMLMapMessage createXMLMapMessage() {
        return new XMLMapMessage();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link Entry }
     * 
     */
    public Entry createEntry() {
        return new Entry();
    }

    /**
     * Create an instance of {@link XMLMessage }
     * 
     */
    public XMLMessage createXMLMessage() {
        return new XMLMessage();
    }

    /**
     * Create an instance of {@link XMLObjectMessage }
     * 
     */
    public XMLObjectMessage createXMLObjectMessage() {
        return new XMLObjectMessage();
    }

    /**
     * Create an instance of {@link MessageSet }
     * 
     */
    public MessageSet createMessageSet() {
        return new MessageSet();
    }

    /**
     * Create an instance of {@link XMLBytesMessage }
     * 
     */
    public XMLBytesMessage createXMLBytesMessage() {
        return new XMLBytesMessage();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "content")
    public JAXBElement<MessageSet> createContent(MessageSet value) {
        return new JAXBElement<MessageSet>(_Content_QNAME, MessageSet.class, null, value);
    }

}
