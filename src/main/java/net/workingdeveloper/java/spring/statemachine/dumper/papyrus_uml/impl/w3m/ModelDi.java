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

class ModelDi extends ModelXmlBase {
    ModelDi() throws ParserConfigurationException {
        super();
        appendStaticXml(getDocument());
    }

    @Override
    public void save(File aFilename) throws IOException {
        super.save(new File(aFilename.getAbsolutePath() + ".di"));
    }

    private void appendStaticXml(Document aDocument) {
        Element lRoot = createElementNS("http://www.omg.org/spec/XMI/20131001", "xmi:XMI");
        aDocument.appendChild(lRoot);

        lRoot.setAttribute("xmi:version", "20131001");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xmi", "http://www.omg.org/spec/XMI/20131001");
//        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//        lRoot.setAttributeNS(
//                "http://www.w3.org/2000/xmlns/", "xmlns:ActionLanguage",
//                "http://www.omg.org/spec/ALF/20120827/ActionLanguage-Profile"
//        );
//        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
//        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:uml", "http://www.eclipse.org/uml2/5.0.0/UML");
//        lRoot.setAttributeNS(
//                "http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation",
//                "http://www.omg.org/spec/ALF/20120827/ActionLanguage-Profile pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#_Kv8EIKFXEeS_KNX0nfvIVQ"
//        );

    }
}
