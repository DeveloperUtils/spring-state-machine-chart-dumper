package net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.impl.w3m;

import net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.IId;
import net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.IMdtUml2Model;
import net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.UuidId;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class ModelUml extends ModelXmlBase {
    class MXUAction extends MXUNode {

        MXUAction(IId aId, String aName, MXUNode aParent) {
            super(aId, aParent);
            fXmlNode = createXmlElement();
            setName(aName);
            addLanguage("bean");
        }

        public MXUAction addBody(String aBody) {
            Element lBody = createElement("body");
            getXmlNode().appendChild(lBody);
            lBody.appendChild(createTextNode(aBody));
            return this;
        }

        public MXUAction addLanguage(String aLanguageName) {
            Element lLanguage = createElement("language");
            getXmlNode().appendChild(lLanguage);
            lLanguage.appendChild(createTextNode(aLanguageName));
            return this;
        }

        @Override
        Element createXmlElement() {
            Element lElement = createElement("effect");
            lElement.setAttribute("xmi:id", getXmiId().toString());
            lElement.setAttribute("xmi:type", "uml:FunctionBehavior");
            return lElement;
        }
    }

    class MXUActionEntry extends MXUAction {

        MXUActionEntry(IId aId, String aName, MXUNode aParent) {
            super(aId, aName, aParent);
        }

        @Override
        Element createXmlElement() {
            Element lElement = createElement("entry");
            lElement.setAttribute("xmi:id", getXmiId().toString());
            lElement.setAttribute("xmi:type", "uml:FunctionBehavior");
            return lElement;
        }
    }

    class MXUActionExit extends MXUAction {

        MXUActionExit(IId aId, String aName, MXUNode aParent) {
            super(aId, aName, aParent);
        }

        @Override
        Element createXmlElement() {
            Element lElement = createElement("exit");
            lElement.setAttribute("xmi:id", getXmiId().toString());
            lElement.setAttribute("xmi:type", "uml:FunctionBehavior");
            return lElement;
        }
    }

    class MXUGuard extends MXUNode {
        Element fSpecificationXml;

        MXUGuard(IId aIid, MXUNode aParent) {
            super(aIid, aParent);
            fGuardMap.put(aIid, this);
            fXmlNode = createXmlElement();
        }

        public MXUGuard addBody(String aBody) {
            Element lBody = createElement("body");
            fSpecificationXml.appendChild(lBody);
            lBody.appendChild(createTextNode(aBody));
            return this;
        }

        public MXUGuard addLanguage(String aLanguageName) {
            Element lLanguage = createElement("language");
            fSpecificationXml.appendChild(lLanguage);
            lLanguage.appendChild(createTextNode(aLanguageName));
            return this;
        }

        @Override
        public void setName(String aName) {
            super.setName(aName);
            fSpecificationXml.setAttribute("name", "spec#" + aName);
        }

        void setParent(MXUNode aParent) {
            fParent = aParent;
        }

        @Override
        Element createXmlElement() {
    /*
        <ownedRule xmi:id="_T53EcHKSEea1hrJUR6Bqog" name="thisIsAGuard">
          <specification xmi:type="uml:OpaqueExpression" xmi:id="_XOT3wHKSEea1hrJUR6Bqog" name="myOne">
            <language>Bean</language>
            <language>Java</language>
            <body>if x = y</body>
          </specification>
        </ownedRule>

     */
            Element lRule = createElement("ownedRule");
            lRule.setAttribute("xmi:id", getXmiId().toString());
            fSpecificationXml = createElement("specification");
            lRule.appendChild(fSpecificationXml);
            fSpecificationXml.setAttribute("xmi:type", "uml:OpaqueExpression");
            fSpecificationXml.setAttribute("xmi:id", new UuidId().toString());
            fSpecificationXml.setAttribute("name", "spec");
            addLanguage("bean");
            return lRule;
        }
    }

    abstract class MXUNode {
        IId     fID;
        MXUNode fParent;
        Element fXmlNode;

        MXUNode(IId aId, MXUNode aParent) {
            fParent = aParent;
            fID = aId;
            ModelUml.this.fStateMap.put(aId, this);
        }

        public MXUNode appendToParentXml(Element aXmlNode) {
            aXmlNode.appendChild(fXmlNode);
            return this;
        }

        public String getName() {
            return getXmlNode().getAttribute("name");
        }

        public void setName(String aName) {
            getXmlNode().setAttribute("name", aName);
        }

        Element getXmlNode() {
            return fXmlNode;
        }

        MXUNode getParent() {
            return fParent;
        }

        IId getXmiId() {
            if (fID == null) {
                fID = new UuidId();
            }
            return fID;
        }

        abstract Element createXmlElement();

        /*
        <ownedComment xmi:id="_d0GIsHKBEea1hrJUR6Bqog" annotatedElement="4e4a4981-dd3d-4273-93d6-39e84ab04bb5">
          <body>this is a comment</body>
        </ownedComment>
         */
        void addComment(String aCommentText) {
            Element lCommentElement = createElement("ownedComment");
            lCommentElement.setAttribute("xmi:id", new UuidId().toString());
            lCommentElement.setAttribute("annotatedElement", getXmiId().toString());
            fXmlNode.appendChild(lCommentElement);
            Element lCommentText = createElement("body");
            lCommentElement.appendChild(lCommentText);
            lCommentText.appendChild(createTextNode(aCommentText));
        }
    }

    class MXUPseudoState extends MXUStateBase {

        MXUPseudoState(IId aIid, IMdtUml2Model.PseudoKind aKind, MXUStateBase aParent) {
            super(aIid, aParent);
            fXmlNode = createXmlElement(aKind);
        }

        private Element createXmlElement(IMdtUml2Model.PseudoKind aKind) {
            Element lElement = createElement("subvertex");
            lElement.setAttribute("xmi:type", "uml:Pseudostate");
            lElement.setAttribute("xmi:id", getXmiId().toString());
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

    abstract class MXURegionMachineShared extends MXUStateBase {

        MXURegionMachineShared(IId aIid, MXUStateBase aParent) {
            super(aIid, aParent);
        }

        public MXUPseudoState addPseudoState(IMdtUml2Model.PseudoKind aKind, IId aIid, String aName) {
            MXUPseudoState lMUPseudoState = new MXUPseudoState(aIid, aKind, this);
            lMUPseudoState.appendToParentXml(getXmlNode());
            lMUPseudoState.setName(aName);
            return lMUPseudoState;
        }

        public MXURegionState addRegionState(IId aIid, String aName) {
            MXURegionState lMURegionState = new MXURegionState(aIid, this);
            lMURegionState.setName(aName);
            lMURegionState.appendToParentXml(getXmlNode());
            return lMURegionState;
        }

        public MXUSimpleState addState(IId aIid, String aName) {
            MXUSimpleState lMUSimpleState = new MXUSimpleState(aIid, this);
            lMUSimpleState.setName(aName);
            lMUSimpleState.appendToParentXml(getXmlNode());
            return lMUSimpleState;
        }

        public MXUStateMachineState addSubMachine(IId aIid, String aName) {
            MXUStateMachineState lMUStateMachineState = new MXUStateMachineState(aIid, this);
            lMUStateMachineState.setName(aName);
            lMUStateMachineState.appendToParentXml(getXmlNode());
            return lMUStateMachineState;
        }

        public MXUTransition addTransition(IId aSourceStateUuid, IId aTargetStateUuid, MXUTrigger aMUTrigger) {
            MXUTransition lMUTransition = new MXUTransition(aSourceStateUuid, aTargetStateUuid, aMUTrigger, this);
            lMUTransition.appendToParentXml(getXmlNode());
            return lMUTransition;
        }
    }

    class MXURegionState extends MXURegionMachineShared {

        MXURegionState(IId aIid, MXUStateBase aParent) {
            super(aIid, aParent);
            fXmlNode = createXmlElement();
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("region");
            lXml.setAttribute("xmi:type", "uml:Region");
            lXml.setAttribute("xmi:id", getXmiId().toString());

            return lXml;
        }
    }

    class MXURootStateMachine extends MXUStateMachineState {
        public MXURootStateMachine(IId aIid, MXUStateBase aParent) {
            super(aIid, aParent);
            fXmlNode = createXmlElement();
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("packagedElement");
            lXml.setAttribute("xmi:type", "uml:StateMachine");
            lXml.setAttribute("xmi:id", getXmiId().toString());
            lXml.setAttribute("name", "StateMachine");
            return lXml;
        }
    }

    class MXUSimpleState extends MXUStateBase {

        MXUSimpleState(IId aIid, MXUStateBase aParent) {
            super(aIid, aParent);
            fXmlNode = createXmlElement();
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("subvertex");
            lXml.setAttribute("xmi:type", "uml:State");
            lXml.setAttribute("xmi:id", getXmiId().toString());
            return lXml;
        }
    }

    abstract class MXUStateBase extends MXUNode {

        MXUStateBase(IId aIid, MXUStateBase aParent) {
            super(aIid, aParent);
        }

        MXUAction addEntryAction(String aName) {
            MXUActionEntry lActionEntry = new MXUActionEntry(new UuidId(), aName, this);
            lActionEntry.appendToParentXml(getXmlNode());
            return lActionEntry;
        }

        MXUAction addExitAction(String aName) {
            MXUActionExit lActionExit = new MXUActionExit(new UuidId(), aName, this);
            lActionExit.appendToParentXml(getXmlNode());
            return lActionExit;
        }
    }

    class MXUStateMachineState extends MXURegionMachineShared {

        MXUStateMachineState(IId aIid, MXUStateBase aParent) {
            super(aIid, aParent);
            fXmlNode = createXmlElement();
        }

        @Override
        Element createXmlElement() {
            Element lXml = createElement("subvertex");
            lXml.setAttribute("xmi:type", "uml:State");
            lXml.setAttribute("xmi:id", getXmiId().toString());
            return lXml;
        }
    }

    class MXUTransition extends MXUNode {
        private final MXUTrigger fTrigger;

        MXUTransition(IId aSourceStateUuid, IId aTargetStateUuid, MXUTrigger aMUTrigger, MXUNode aParent) {
            super(new UuidId(), aParent);
            fTrigger = aMUTrigger;
            fXmlNode = createXmlElement();
            setSource(aSourceStateUuid);
            setTarget(aTargetStateUuid);
        }

        public MXUAction addAction(String aName) {
            MXUAction lActionEntry = new MXUAction(new UuidId(), aName, this);
            lActionEntry.appendToParentXml(getXmlNode());
            return lActionEntry;
        }

        public MXUNode appendToParentXml(Element aNode) {
            aNode.appendChild(fXmlNode);
            return this;
        }

        public void setName(String aName) {
            fXmlNode.setAttribute("name", aName);
        }

        public void setSource(IId aSource) {
            fXmlNode.setAttribute("source", aSource.toString());
        }

        public void setTarget(IId aTarget) {
            fXmlNode.setAttribute("target", aTarget.toString());
        }

        @Override
        Element createXmlElement() {
            Element lRet = createElement("transition");
            lRet.setAttribute("xmi:type", "uml:Transition");
            lRet.setAttribute("xmi:id", getXmiId().toString());
            if (fTrigger != null) {
                Element lTrigger = createElement("trigger");
                lRet.appendChild(lTrigger);
                lTrigger.setAttribute("xmi:id", new UuidId().toString());
                lTrigger.setAttribute("name", fTrigger.getName());
                lTrigger.setAttribute("event", fTrigger.getXmiId().toString());
            }
            return lRet;
        }

        MXUTransition setGuard(MXUGuard aGuard) {
            aGuard.appendToParentXml(fXmlNode);
            aGuard.setParent(this);
            fXmlNode.setAttribute("guard", aGuard.getXmiId().toString());
            return this;
        }
    }

    class MXUTrigger extends MXUNode {

        private final IMdtUml2Model.IMUTrigger.Type fType;
        private       String                        fEvent;
        private       Element                       fSignal;

        public MXUTrigger(IId aId, Element aUmlModel, String aEvent, IMdtUml2Model.IMUTrigger.Type aType) {
            super(aId, null);
            fEvent = aEvent;
            fType = aType;
            createXmlElement(aUmlModel);
        }

        @Override
        public String getName() {
            return fEvent;
        }

        @Override
        public void setName(String aName) {
            fEvent = aName;
            fXmlNode.setAttribute("name", fEvent + "Event");
            fSignal.setAttribute("name", fEvent);
        }

        private void createXmlElement(Element aModel) {
            fSignal = createElement("packagedElement");
            fSignal.setAttribute("xmi:type", "uml:Signal");
            String lValue = new UuidId().toString();
            fSignal.setAttribute("xmi:id", lValue);
            fSignal.setAttribute("name", fEvent);
            aModel.appendChild(fSignal);
            fXmlNode = createElement("packagedElement");
            fXmlNode.setAttribute("xmi:type", "uml:SignalEvent");
            fXmlNode.setAttribute("xmi:id", getXmiId().toString());
            fXmlNode.setAttribute("name", fEvent + "Event");
            fXmlNode.setAttribute("signal", lValue);
            aModel.appendChild(fXmlNode);
        }

        @Override
        Element createXmlElement() {
            return null;
        }
    }

    private final Map<IId, MXUGuard> fGuardMap = new HashMap<>();

    private final Map<IId, MXUNode> fStateMap = new HashMap<>();
    private MXURootStateMachine fRootState;
    private Element             fUmlModel;

    public ModelUml() throws ParserConfigurationException {
        appendStaticXml(getDocument());
    }

    public MXUTrigger addTrigger(IId aId, String aEvent, IMdtUml2Model.IMUTrigger.Type aType) {
        return new MXUTrigger(aId, fUmlModel, aEvent, aType);
    }

    public MXUGuard createGuard(IId aGuardId) {
        return new MXUGuard(aGuardId, null);
    }

    public MXUGuard findGuard(IId aId) {
        return fGuardMap.get(aId);
    }

    public MXURootStateMachine getRootState() {
        if (fRootState == null) {
            fRootState = new MXURootStateMachine(new UuidId(), null);
            fStateMap.put(fRootState.getXmiId(), fRootState);
        }
        return fRootState;
    }

    @Override
    public void save(File aFilename) throws IOException {
        super.save(new File(aFilename.getAbsolutePath() + ".uml"));
    }

    Text createTextNode(String data) {
        return getDocument().createTextNode(data);
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
        fUmlModel = createElement("uml:Model");
        fUmlModel.setAttribute("xmi:id", (new UuidId()).toString());
        fUmlModel.setAttribute("name", "SsmDumper");
        lRoot.appendChild(fUmlModel);

        Element l3 = createElement("packageImport");
        l3.setAttribute("xmi:type", "uml:PackageImport");
        l3.setAttribute("xmi:id", (new UuidId()).toString());
        fUmlModel.appendChild(l3);

        Element l4 = createElement("importedPackage");
        l4.setAttribute("xmi:type", "uml:Model");
        l4.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#_0");
        l3.appendChild(l4);

        //insert packageElement
        getRootState().appendToParentXml(fUmlModel);

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
