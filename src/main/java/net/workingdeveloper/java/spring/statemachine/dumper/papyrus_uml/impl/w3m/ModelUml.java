package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.IPapyrusModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
class ModelUml extends ModelXmlBase {

    abstract class MUNode {
        UUID    fID;
        MUNode  fParent;
        Element fXmlNode;

        public MUNode(UUID aId, MUNode aParent) {
            fParent = aParent;
            fID = aId;
            ModelUml.this.fStateMap.put(aId, this);
        }

        public MUNode appendToParentXml(Element aXmlNode) {
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

        public String getName() {
            return getXmlNode().getAttribute("name");
        }

        public void setName(String aName) {
            getXmlNode().setAttribute("name", aName);
        }

        public Element getXmlNode() {
            return fXmlNode;
        }

        UUID getUuid() {
            if (fID == null) {
                fID = UUID.randomUUID();
            }
            return fID;
        }

        abstract Element createXmlElement();
    }

    class SimpleState extends MUNode {

        public SimpleState(UUID aID, MUNode aParent) {
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

    class Transition extends MUNode {
        Element fXmlNode;

        public Transition(UUID aSourceStateUuid, UUID aTargetStateUuid, MUNode aParent) {
            super(UUID.randomUUID(), aParent);
            fXmlNode = createElement("transition");
            fXmlNode.setAttribute("xmi:type", "uml:Transition");
            fXmlNode.setAttribute("xmi:id", getUuid().toString());
            setSource(aSourceStateUuid.toString());
            setTarget(aTargetStateUuid.toString());
        }

        public MUNode appendToParentXml(Element aNode) {
            aNode.appendChild(fXmlNode);
            return this;
        }

        public void setName(String aName) {
            fXmlNode.setAttribute("name", aName);
        }

        public void setSource(String aSource) {
            fXmlNode.setAttribute("source", aSource);
        }

        public void setTarget(String aTarget) {
            fXmlNode.setAttribute("target", aTarget);
        }

        @Override
        Element createXmlElement() {
            return null;
        }
    }

    abstract class MURegionMachineShared extends MUNode {

        public MURegionMachineShared(UUID aId, MUNode aParent) {
            super(aId, aParent);
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
            Transition lTransition = new Transition(aSourceStateUuid, aTargetStateUuid, this);
            lTransition.appendToParentXml(getXmlNode());
            return lTransition;
        }

    }

    class RegionState extends MURegionMachineShared {

        public RegionState(UUID aUUID, MUNode aParent) {
            super(aUUID, aParent);
            fXmlNode = createXmlElement();
        }


        @Override
        Element createXmlElement() {
            Element lXml = createElement("region");
            lXml.setAttribute("xmi:type", "uml:Region");
            lXml.setAttribute("xmi:id", getUuid().toString());

            return lXml;
        }

    }

    class PseudoState extends MUNode {

        public PseudoState(UUID aUUID, IPapyrusModel.PseudoKind aKind, MUNode aParent) {
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

    class StateMachineState extends MURegionMachineShared {

        public StateMachineState(UUID aUUID, MUNode aParent) {
            super(aUUID, aParent);
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

    class RootStateMachine extends StateMachineState {
        public RootStateMachine(UUID aUUID, MUNode aParent) {
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

    private static int                   sfId      = 0;
    private final  HashMap<UUID, MUNode> fStateMap = new HashMap<>();
    private RootStateMachine fRootState;

    public ModelUml() throws ParserConfigurationException {
        appendStaticXml(getDocument());
    }

    public RootStateMachine getRootState() {
        if (fRootState == null) {
            fRootState = new RootStateMachine(UUID.randomUUID(), null);
            fStateMap.put(fRootState.getUuid(), fRootState);
        }
        return fRootState;
    }

    @Override
    public void save(File aFilename) throws IOException {
        super.save(new File(aFilename.getAbsolutePath() + ".uml"));
    }

    private void appendStaticXml(Document lDoc) {
        Element lRoot = createElementNS("http://www.omg.org/spec/XMI/20131001", "xmi:XMI");
        lDoc.appendChild(lRoot);

        lRoot.setAttribute("xmi:version", "20131001");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xmi", "http://www.omg.org/spec/XMI/20131001");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//        lRoot.setAttributeNS(
//                "http://www.w3.org/2000/xmlns/", "xmlns:ActionLanguage",
//                "http://www.omg.org/spec/ALF/20120827/ActionLanguage-Profile"
//        );
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:uml", "http://www.eclipse.org/uml2/5.0.0/UML");
//        lRoot.setAttributeNS(
//                "http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation",
//                "http://www.omg.org/spec/ALF/20120827/ActionLanguage-Profile pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#_Kv8EIKFXEeS_KNX0nfvIVQ"
//        );
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

//        l3 = createElement("profileApplication");
////        <profileApplication xmi:type="uml:ProfileApplication" xmi:id="_FEd_AGUUEeanQ99zIc7c2Q">
//        l3.setAttribute("xmi:id", UUID.randomUUID().toString());
//        l3.setAttribute("xmi:type", "uml:ProfileApplication");
//        lUmlModel.appendChild(l3);
////      <eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_FEf0MGUUEeanQ99zIc7c2Q" source="http://www.eclipse.org/uml2/2.0.0/UML">
//        l4 = createElement("eAnnotations");
//        l4.setAttribute("xmi:type", "ecore:EAnnotation");
//        l4.setAttribute("source", "http://www.eclipse.org/uml2/2.0.0/UML");
//        l4.setAttribute("xmi:id", UUID.randomUUID().toString());
//
//        l3.appendChild(l4);
//        Element l5 = createElement("references");
//        l4.appendChild(l5);
//        l3.setAttribute("xmi:type", "ecore:EPackage");
//        l3.setAttribute(
//                "href",
//                "pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#_Kv8EIKFXEeS_KNX0nfvIVQ"
//        );

//        <references xmi:type="ecore:EPackage" href="pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#_Kv8EIKFXEeS_KNX0nfvIVQ"/>
//      </eAnnotations>


//        l4 = createElement("appliedProfile");
//        l3.appendChild(l4);
//        l4.setAttribute("xmi:type", "uml:Profile");
//        l4.setAttribute(
//                "href",
//                "pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#ActionLanguage"
//        );
//      <appliedProfile xmi:type="uml:Profile" href="pathmap://PAPYRUS_ACTIONLANGUAGE_PROFILE/ActionLanguage-Profile.profile.uml#ActionLanguage"/>
//    </profileApplication>

//        lUmlModel = createElement("ActionLanguage:TextualRepresentation");
//        lUmlModel.setAttribute("xmi:id", UUID.randomUUID().toString());
//        lUmlModel.setAttribute("base_Comment", "_FEPVgGUUEeanQ99zIc7c2Q");
//        lUmlModel.setAttribute("language", "org.eclipse.papyrus.uml.textedit.transition.xtext.UmlTransition");
//        lRoot.appendChild(lUmlModel);
    }
}
