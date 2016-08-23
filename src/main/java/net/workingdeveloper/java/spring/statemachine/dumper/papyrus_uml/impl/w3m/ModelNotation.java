package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
class ModelNotation extends ModelXmlBase {
    class State {
    }

    public ModelNotation() throws ParserConfigurationException {
        super();
        appendStaticXml(getDocument());
    }

    @Override
    public void save(File aFilename) throws IOException {
        super.save(new File(aFilename.getAbsolutePath() + ".notation"));
    }

    private void appendStaticXml(Document aDocument) {
        Element lRoot = createElementNS("http://www.omg.org/spec/XMI/20131001", "xmi:XMI");
        aDocument.appendChild(lRoot);

        lRoot.setAttribute("xmi:version", "20131001");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xmi", "http://www.omg.org/spec/XMI/20131001");
    }
}
