package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.IPapyrusModel;
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
import java.io.StringWriter;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class ModelUml {

    abstract class State {
        State   fParent;
        Element fXmlNode;

        public State(Element aXmlNode, State aParent) {
            fXmlNode = aXmlNode;
            fParent = aParent;
        }

        public State(State aParent) {
            this(null, aParent);
        }

        public State appendToParentXml(Element aXmlNode) {
            aXmlNode.appendChild(fXmlNode);
            return this;
        }

        public Element getXmlNode() {
            return fXmlNode;
        }

        public void setName(String aName) {
            getXmlNode().setAttribute("name", aName);
        }

        abstract Element createXmlElement();
    }

    class SimpleState extends State {

        public SimpleState(State aParent) {
            super(aParent);
            fXmlNode = createXmlElement();
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("subvertex");
            lXml.setAttribute("xmi:type", "uml:State");
            lXml.setAttribute("xmi:id", getId());
            return lXml;
        }
    }

    class RegionState extends State {

        public RegionState(State aParent) {
            super(aParent);
            fXmlNode = createXmlElement();
        }

        public PseudoState addPseudoState(IPapyrusModel.PseudoKind aKind, String aName) {
            PseudoState lPseudoState = new PseudoState(aKind, this);
            lPseudoState.appendToParentXml(getXmlNode());
            lPseudoState.setName(aName);
            return lPseudoState;
        }

        public RegionState addRegionState(String aName) {
            RegionState lRegionState = new RegionState(this);
            lRegionState.setName(aName);
            lRegionState.appendToParentXml(getXmlNode());
            return lRegionState;
        }

        public SimpleState addState(String aS) {
            SimpleState lSimpleState = new SimpleState(this);
            lSimpleState.setName(aS);
            lSimpleState.appendToParentXml(getXmlNode());
            return lSimpleState;
        }

        public StateMachineState addSubMachine(String aS) {
            StateMachineState lStateMachineState = new StateMachineState(this);
            lStateMachineState.setName(aS);
            lStateMachineState.appendToParentXml(getXmlNode());
            return lStateMachineState;
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("region");
            lXml.setAttribute("xmi:type", "uml:Region");
            lXml.setAttribute("xmi:id", getId());

            return lXml;
        }

    }

    class PseudoState extends State {

        public PseudoState(IPapyrusModel.PseudoKind aKind, State aParent) {
            super(aParent);
            fXmlNode = createXmlElement(aKind);
        }

        private Element createXmlElement(IPapyrusModel.PseudoKind aKind) {
            Element lElement = createElement("subvertex");
            lElement.setAttribute("xmi:type", "uml:Pseudostate");
            lElement.setAttribute("xmi:id", getId());
            switch (aKind) {
                case SHALLOW_HISTORY:
                    lElement.setAttribute("kind", "shallowHistory");
                    break;
                case FORK:
                    lElement.setAttribute("kind", "fork");
                    break;
                case INITIAL:
//                    lElement.setAttribute("kind","inital");
                    break;
                case CHOICE:
                    lElement.setAttribute("kind", "choice");
                    break;
                case JUNCTION:
                    lElement.setAttribute("kind", "junction");
                    break;
                case JOIN:
                    lElement.setAttribute("kind", "join");
                    break;
                case ENTRY_POINT:
                    lElement.setAttribute("kind", "entryPoint");
                    break;
                case EXIT_POINT:
                    lElement.setAttribute("kind", "exitPoint");
                    break;
                case DEEP_HISTORY:
                    lElement.setAttribute("kind", "deepHistory");
                    break;
                case FINAL:
                    lElement.setAttribute("xmi:type", "uml:FinalState");
                    break;
            }
            return lElement;
        }

        @Override
        Element createXmlElement() {
            return null;
        }
    }

    class StateMachineState extends RegionState {

        public StateMachineState(State aParent) {
            super(aParent);
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("subvertex");
            lXml.setAttribute("xmi:type", "uml:State");
            lXml.setAttribute("xmi:id", getId());
            return lXml;
        }

    }

    class RootStateMachine extends StateMachineState {
        public RootStateMachine(State aParent) {
            super(aParent);
            fXmlNode = createXmlElement();
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("packagedElement");
            lXml.setAttribute("xmi:type", "uml:StateMachine");
            lXml.setAttribute("xmi:id", getId());
            lXml.setAttribute("name", "StateMachine");
            return lXml;
        }

    }

    private static int sfId = 0;
    private Document          fDocument;
    private RootStateMachine fRootState;

    public ModelUml() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder        docBuilder = null;
        docBuilder = docFactory.newDocumentBuilder();
        // root elements
        fDocument = docBuilder.newDocument();
        fDocument.setXmlStandalone(true);
        appendStaticXml(getDocument());
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

    public StateMachineState getRootState() {
        if (fRootState == null) {
            fRootState = new RootStateMachine(null);
        }
        return fRootState;
    }

    public void save(String aFilename) {
    }

    private void appendStaticXml(Document lDoc) {
        Element lRoot = createElementNS("http://www.omg.org/spec/XMI/20131001", "xmi:XMI");
        lDoc.appendChild(lRoot);

        lRoot.setAttribute("xmi:version", "20131001");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xmi", "http://www.omg.org/spec/XMI/20131001");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        lRoot.setAttributeNS(
                "http://www.w3.org/2000/xmlns/", "xmlns:ActionLanguage",
                "http://www.omg.org/spec/ALF/20120827/ActionLanguage-Profile"
        );
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:uml", "http://www.eclipse.org/uml2/5.0.0/UML");
        lRoot.setAttributeNS(
                "http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation",
                "http://www.omg.org/spec/ALF/20120827/ActionLanguage-Profile pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#_Kv8EIKFXEeS_KNX0nfvIVQ"
        );
        Element lUmlModel = createElement("uml:Model");
        lUmlModel.setAttribute("xmi:id", getId());
        lUmlModel.setAttribute("name", "SsmDumper");
        lRoot.appendChild(lUmlModel);

        Element l3 = createElement("packageImport");
        l3.setAttribute("xmi:type", "uml:PackageImport");
        l3.setAttribute("xmi:id", getId());
        lUmlModel.appendChild(l3);

        Element l4 = createElement("importedPackage");
        l4.setAttribute("xmi:type", "uml:Model");
        l4.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#_0");
        l3.appendChild(l4);

        //insert packageElement
        getRootState().appendToParentXml(lUmlModel);

        l3 = createElement("profileApplication");
//        <profileApplication xmi:type="uml:ProfileApplication" xmi:id="_FEd_AGUUEeanQ99zIc7c2Q">
        l3.setAttribute("xmi:id", getId());
        l3.setAttribute("xmi:type", "uml:ProfileApplication");
        lUmlModel.appendChild(l3);
//      <eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_FEf0MGUUEeanQ99zIc7c2Q" source="http://www.eclipse.org/uml2/2.0.0/UML">
        l4 = createElement("eAnnotations");
        l4.setAttribute("xmi:type", "ecore:EAnnotation");
        l4.setAttribute("source", "http://www.eclipse.org/uml2/2.0.0/UML");
        l4.setAttribute("xmi:id", getId());

        l3.appendChild(l4);
        Element l5 = createElement("references");
        l4.appendChild(l5);
        l3.setAttribute("xmi:type", "ecore:EPackage");
        l3.setAttribute(
                "href",
                "pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#_Kv8EIKFXEeS_KNX0nfvIVQ"
        );

//        <references xmi:type="ecore:EPackage" href="pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#_Kv8EIKFXEeS_KNX0nfvIVQ"/>
//      </eAnnotations>


        l4 = createElement("appliedProfile");
        l3.appendChild(l4);
        l4.setAttribute("xmi:type", "uml:Profile");
        l4.setAttribute(
                "href",
                "pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#ActionLanguage"
        );
//      <appliedProfile xmi:type="uml:Profile" href="pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#ActionLanguage"/>
//    </profileApplication>

        lUmlModel = createElement("ActionLanguage:TextualRepresentation");
        lUmlModel.setAttribute("xmi:id", getId());
        lUmlModel.setAttribute("base_Comment", "_FEPVgGUUEeanQ99zIc7c2Q");
        lUmlModel.setAttribute("language", "org.eclipse.papyrus.uml.textedit.transition.xtext.UmlTransition");
        lRoot.appendChild(lUmlModel);
    }

    String getId() {
        return String.valueOf(sfId++);
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
