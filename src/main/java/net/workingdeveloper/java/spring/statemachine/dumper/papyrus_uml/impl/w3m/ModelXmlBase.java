package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;

/**
 * Created by Christoph Graupner on 8/23/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
abstract class ModelXmlBase {
    protected Document fDocument;

    public ModelXmlBase() throws ParserConfigurationException {
        initDocument();
    }

    public String asString() {
        TransformerFactory tf          = TransformerFactory.newInstance();
        Transformer        transformer = null;
        try {
            transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter lWriter = new StringWriter();
            transformer.transform(
                    new DOMSource(getDocument()),
                    new StreamResult(lWriter)
            );
            return lWriter.toString();
        } catch (TransformerException aE) {
            aE.printStackTrace();
        }
        return null;
    }

    public void save(File aFilename) throws IOException {
        aFilename = aFilename.getAbsoluteFile();
        Files.write(aFilename.toPath(), asString().getBytes());
    }

    protected void initDocument() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder        docBuilder = docFactory.newDocumentBuilder();
        // root elements
        fDocument = docBuilder.newDocument();
        getDocument().setXmlStandalone(true);
    }

    Element createElement(String tagName) throws DOMException {
        return getDocument().createElement(tagName);
    }

    Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return getDocument().createElementNS(namespaceURI, qualifiedName);
    }

    Document getDocument() {
        return fDocument;
    }
}
