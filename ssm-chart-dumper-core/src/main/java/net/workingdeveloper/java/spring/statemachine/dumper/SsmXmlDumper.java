package net.workingdeveloper.java.spring.statemachine.dumper;

import org.springframework.statemachine.StateMachine;
import org.w3c.dom.*;

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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Christoph Graupner on 8/20/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
abstract class SsmXmlDumper<S, E> extends SsmDumper<S, E> {

    private Document fXmlDocument;

    public SsmXmlDumper(StateMachine<S, E> aStateMachine) {
        super(aStateMachine);
    }

    public String asString() {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter lWriter = new StringWriter();
            transformer.transform(
                new DOMSource(getXmlDocument()),
                new StreamResult(lWriter)
            );
            return lWriter.toString();
        } catch (TransformerException aE) {
            aE.printStackTrace();
        }
        return null;
    }

    public Comment createComment(String data) {
        return getXmlDocument().createComment(data);
    }

    public SsmXmlDumper dump() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document lDocument = docBuilder.newDocument();
            dump(lDocument);
        } catch (ParserConfigurationException aE) {
            aE.printStackTrace();
        }
        return this;
    }

    public abstract <T extends SsmXmlDumper<S, E>> T dump(Document aOutputDocument);

    public void toFile(File aFile) throws TransformerException, IOException {
        List<String> lList = Arrays.asList(dump().asString());
        Files.write(aFile.toPath(), lList, Charset.forName("UTF-8"));
    }

    protected Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return getXmlDocument().createAttributeNS(namespaceURI, qualifiedName);
    }

    protected Element createElement(String aS) throws DOMException {
        return getXmlDocument().createElement(aS);
    }

    protected Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return getXmlDocument().createElementNS(namespaceURI, qualifiedName);
    }

    protected Document getXmlDocument() {
        return fXmlDocument;
    }

    protected void setXmlDocument(Document aXmlDocument) {
        fXmlDocument = aXmlDocument;
    }

}
