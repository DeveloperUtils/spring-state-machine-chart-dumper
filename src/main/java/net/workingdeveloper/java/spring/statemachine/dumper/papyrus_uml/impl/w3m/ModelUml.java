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
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class ModelUml {

    abstract class State {
        UUID    fID;
        State   fParent;
        Element fXmlNode;

        public State(UUID aId, State aParent) {
            fParent = aParent;
            fID = aId;
            ModelUml.this.fStateMap.put(aId, this);
        }

        public State appendToParentXml(Element aXmlNode) {
            aXmlNode.appendChild(fXmlNode);
            return this;
        }

        public String getId() {
            return getXmlNode().getAttribute("xmi:id");
        }

        public void setId(String aId) {
            if (getXmlNode() != null) {
                getXmlNode().setAttribute("xmi:id", aId);
            }
        }

        public Element getXmlNode() {
            return fXmlNode;
        }

        public void setName(String aName) {
            getXmlNode().setAttribute("name", aName);
        }

        UUID getUuid() {
            if (fID == null) {
                fID = UUID.randomUUID();
            }
            return fID;
        }

        abstract Element createXmlElement();
    }

    class SimpleState extends State {

        public SimpleState(UUID aID, State aParent) {
            super(aID, aParent);
            fXmlNode = createXmlElement();
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("subvertex");
            lXml.setAttribute("xmi:type", "uml:State");
            lXml.setAttribute("xmi:id", getUuid().toString());
            return lXml;
        }
    }

    class Transition {
        Element fXmlNode;

        public Transition(UUID aSourceStateUuid, UUID aTargetStateUuid) {
            fXmlNode = createElement("transition");
            fXmlNode.setAttribute("xmi:type", "uml:Transition");
            fXmlNode.setAttribute("xmi:id", UUID.randomUUID().toString());
            setSource(aSourceStateUuid.toString());
            setTarget(aTargetStateUuid.toString());
        }

        public void appendToParentXml(Element aNode) {
            aNode.appendChild(fXmlNode);
        }

        public void setSource(String aSource) {
            fXmlNode.setAttribute("source", aSource);
        }

        public void setTarget(String aTarget) {
            fXmlNode.setAttribute("target", aTarget);
        }
    }

    class RegionState extends State {

        public RegionState(UUID aUUID, State aParent) {
            super(aUUID, aParent);
            fXmlNode = createXmlElement();
        }

        public PseudoState addPseudoState(IPapyrusModel.PseudoKind aKind, UUID aUUID, String aName) {
            PseudoState lPseudoState = new PseudoState(aUUID, aKind, this);
            lPseudoState.appendToParentXml(getXmlNode());
            lPseudoState.setName(aName);
            return lPseudoState;
        }

        public RegionState addRegionState(UUID aUUID, String aName) {
            RegionState lRegionState = new RegionState(aUUID, this);
            lRegionState.setName(aName);
            lRegionState.appendToParentXml(getXmlNode());
            return lRegionState;
        }

        public SimpleState addState(UUID aUUID, String aS) {
            SimpleState lSimpleState = new SimpleState(aUUID, this);
            lSimpleState.setName(aS);
            lSimpleState.appendToParentXml(getXmlNode());
            return lSimpleState;
        }

        public StateMachineState addSubMachine(UUID aUUID, String aS) {
            StateMachineState lStateMachineState = new StateMachineState(aUUID, this);
            lStateMachineState.setName(aS);
            lStateMachineState.appendToParentXml(getXmlNode());
            return lStateMachineState;
        }

        public Transition addTransition(UUID aSourceStateUuid, UUID aTargetStateUuid) {
            Transition lTransition = new Transition(aSourceStateUuid, aTargetStateUuid);
            lTransition.appendToParentXml(getXmlNode());
            return lTransition;
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("region");
            lXml.setAttribute("xmi:type", "uml:Region");
            lXml.setAttribute("xmi:id", getUuid().toString());

            return lXml;
        }

    }

    class PseudoState extends State {

        public PseudoState(UUID aUUID, IPapyrusModel.PseudoKind aKind, State aParent) {
            super(aUUID, aParent);
            fXmlNode = createXmlElement(aKind);
        }

        private Element createXmlElement(IPapyrusModel.PseudoKind aKind) {
            Element lElement = createElement("subvertex");
            lElement.setAttribute("xmi:type", "uml:Pseudostate");
            lElement.setAttribute("xmi:id", getUuid().toString());
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

        public StateMachineState(UUID aUUID, State aParent) {
            super(aUUID, aParent);
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("subvertex");
            lXml.setAttribute("xmi:type", "uml:State");
            lXml.setAttribute("xmi:id", getUuid().toString());
            return lXml;
        }

    }

    class RootStateMachine extends StateMachineState {
        public RootStateMachine(UUID aUUID, State aParent) {
            super(aUUID, aParent);
            fXmlNode = createXmlElement();
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("packagedElement");
            lXml.setAttribute("xmi:type", "uml:StateMachine");
            lXml.setAttribute("xmi:id", getUuid().toString());
            lXml.setAttribute("name", "StateMachine");
            return lXml;
        }

    }

    private static int                  sfId      = 0;
    private final  HashMap<UUID, State> fStateMap = new HashMap<>();
    private Document         fDocument;
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
            fRootState = new RootStateMachine(UUID.randomUUID(), null);
            fStateMap.put(fRootState.getUuid(), fRootState);
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
        lUmlModel.setAttribute("xmi:id", UUID.randomUUID().toString());
        lUmlModel.setAttribute("name", "SsmDumper");
        lRoot.appendChild(lUmlModel);

        Element l3 = createElement("packageImport");
        l3.setAttribute("xmi:type", "uml:PackageImport");
        l3.setAttribute("xmi:id", UUID.randomUUID().toString());
        lUmlModel.appendChild(l3);

        Element l4 = createElement("importedPackage");
        l4.setAttribute("xmi:type", "uml:Model");
        l4.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#_0");
        l3.appendChild(l4);

        //insert packageElement
        getRootState().appendToParentXml(lUmlModel);

        l3 = createElement("profileApplication");
//        <profileApplication xmi:type="uml:ProfileApplication" xmi:id="_FEd_AGUUEeanQ99zIc7c2Q">
        l3.setAttribute("xmi:id", UUID.randomUUID().toString());
        l3.setAttribute("xmi:type", "uml:ProfileApplication");
        lUmlModel.appendChild(l3);
//      <eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_FEf0MGUUEeanQ99zIc7c2Q" source="http://www.eclipse.org/uml2/2.0.0/UML">
        l4 = createElement("eAnnotations");
        l4.setAttribute("xmi:type", "ecore:EAnnotation");
        l4.setAttribute("source", "http://www.eclipse.org/uml2/2.0.0/UML");
        l4.setAttribute("xmi:id", UUID.randomUUID().toString());

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
        lUmlModel.setAttribute("xmi:id", UUID.randomUUID().toString());
        lUmlModel.setAttribute("base_Comment", "_FEPVgGUUEeanQ99zIc7c2Q");
        lUmlModel.setAttribute("language", "org.eclipse.papyrus.uml.textedit.transition.xtext.UmlTransition");
        lRoot.appendChild(lUmlModel);
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
