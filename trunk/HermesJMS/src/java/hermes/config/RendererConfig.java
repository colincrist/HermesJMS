//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.01.03 at 11:55:46 GMT 
//


package hermes.config;


/**
 * Java content class for RendererConfig complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/work/Workspaces/Hermes/HermesJMS/src/xml/hermes-schema.xsd line 102)
 * <p>
 * <pre>
 * &lt;complexType name="RendererConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="properties" type="{}PropertySetConfig" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="className" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface RendererConfig {


    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getName();

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setName(java.lang.String value);

    /**
     * Gets the value of the className property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getClassName();

    /**
     * Sets the value of the className property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setClassName(java.lang.String value);

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link hermes.config.PropertySetConfig}
     */
    hermes.config.PropertySetConfig getProperties();

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link hermes.config.PropertySetConfig}
     */
    void setProperties(hermes.config.PropertySetConfig value);

}
