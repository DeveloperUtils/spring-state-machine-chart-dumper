package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
class ModelNotation extends ModelXmlBase {
    abstract class MNNode<T extends ModelUml.MUNode, PARENT extends MNNode> {

        Element fDiagramNode;
        T       fLinkedMUNode;
        PARENT  fParentNode;

        public MNNode(PARENT aParentNode, T aLinkedMUNode) {
            fParentNode = aParentNode;
            fLinkedMUNode = aLinkedMUNode;
        }


        abstract Element createXml();
    }

    abstract class MNState<T extends ModelUml.MUNode, PARENT extends MNState> extends MNNode<T, PARENT> {
        MNState(PARENT aParentNode, T aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }
    }

    class MNSimpleState extends MNState<ModelUml.MUSimpleState, MNRegionMachineShared> {
        MNSimpleState(MNRegionMachineShared aParentNode, ModelUml.MUSimpleState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }

        @Override
        Element createXml() {
            return null;
        }
    }

    class MNPseudoState extends MNState<ModelUml.MUPseudoState, MNRegionMachineShared> {

        MNPseudoState(MNRegionMachineShared aParentNode, ModelUml.MUPseudoState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }

        @Override
        Element createXml() {
            return null;
        }
    }

    abstract class MNRegionMachineShared<T extends ModelUml.MUNode, PARENT extends MNState> extends MNState<T, PARENT> {

        protected Element fSubElementRoot;

        MNRegionMachineShared(PARENT aParentNode, T aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }

        MNPseudoState addPseudoState(ModelUml.MUPseudoState aMUNode) {
            return new MNPseudoState(this, aMUNode);
        }

        MNRegionState addRegionState(ModelUml.MURegionState aMUNode) {
            return new MNRegionState(this, aMUNode);
        }

        MNSimpleState addSimpleState(ModelUml.MUSimpleState aMUNode) {
            return new MNSimpleState(this, aMUNode);
        }

        MNStateMachine addSubMachine(ModelUml.MUStateMachineState aMUNode) {
            return new MNStateMachine(this, aMUNode);
        }

        void appendChildXml(Element aXml) {
            if (fSubElementRoot != null)
                fSubElementRoot.appendChild(aXml);
        }

    }

    class MNRegionState extends MNRegionMachineShared<ModelUml.MURegionState, MNRegionMachineShared> {
        MNRegionState(MNRegionMachineShared aParentNode, ModelUml.MURegionState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
            fDiagramNode = createXml();
            aParentNode.appendChildXml(fDiagramNode);
//            if (aParentNode.fSubElementRoot != null)
//                aParentNode.fSubElementRoot.appendChild(fDiagramNode);
        }

        @Override
        Element createXml() {
            Element lElement = createElement("children");
            lElement.setAttribute("xmi:type", "notation:Shape");
            lElement.setAttribute("xmi:id", getObjectId(null).toString());
            lElement.setAttribute("type", "Region_Shape");

            Element lECore = createElement("eAnnotations");
            lElement.appendChild(lECore);
            lECore.setAttribute("xmi:type", "ecore:EAnnotation");
            lECore.setAttribute("source", "RegionAnnotationKey");
            lECore.setAttribute("xmi:id", getObjectId(null).toString());
            Element lElementECoreDetails = createElement("details");
            lECore.appendChild(lElementECoreDetails);
            lElementECoreDetails.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
            lElementECoreDetails.setAttribute("key", "RegionZoneKey");
            lElementECoreDetails.setAttribute("value", "");
            lElementECoreDetails.setAttribute("xmi:id", getObjectId(null).toString());

/*
        <eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_E9WGgmn6Eeaq15FhJ2U9ZA" source="RegionAnnotationKey">
          <details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_E9WtkGn6Eeaq15FhJ2U9ZA" key="RegionZoneKey" value="R"/>
        </eAnnotations>

 */
            fSubElementRoot = createElement("children");
            lElement.appendChild(fSubElementRoot);
            fSubElementRoot.setAttribute("xmi:type", "notation:BasicCompartment");
            fSubElementRoot.setAttribute("xmi:id", new UuidId().toString());
            fSubElementRoot.setAttribute("type", "Region_SubvertexCompartment");

            Element lE = createElement("element");
            lElement.appendChild(lE);
            lE.setAttribute("xmi:type", "uml:Region");
            lE.setAttribute("href", PLACEHOLDER_FILENAME + '#' + fLinkedMUNode.getUuid());
            fChangeOnSave.add(lE.getAttributeNode("href"));

            Element lLayoutConstraint = createElement("layoutConstraint");
            lElement.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Bounds");
            lLayoutConstraint.setAttribute("xmi:id", getObjectId(null).toString());
            lLayoutConstraint.setAttribute("width", "500");
            lLayoutConstraint.setAttribute("height", "200");

            /*
      <children xmi:type="notation:Shape" xmi:id="_E9U4YGn6Eeaq15FhJ2U9ZA" type="Region_Shape">
        <eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_E9WGgmn6Eeaq15FhJ2U9ZA" source="RegionAnnotationKey">
          <details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_E9WtkGn6Eeaq15FhJ2U9ZA" key="RegionZoneKey" value="R"/>
        </eAnnotations>
        <children xmi:type="notation:BasicCompartment" xmi:id="_E9WGgGn6Eeaq15FhJ2U9ZA" type="Region_SubvertexCompartment">
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_E9WGgWn6Eeaq15FhJ2U9ZA"/>
        </children>
        <element xmi:type="uml:Region" href="model.uml#_E9LHYGn6Eeaq15FhJ2U9ZA"/>
        <layoutConstraint xmi:type="notation:Bounds" xmi:id="_E9U4YWn6Eeaq15FhJ2U9ZA" x="350" width="350" height="287"/>
      </children>
             */

            return lElement;
        }

    }

    class MNStateMachine extends MNRegionMachineShared<ModelUml.MUStateMachineState, MNRegionMachineShared> {

        MNStateMachine(MNRegionMachineShared aParentNode, ModelUml.MUStateMachineState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
            if (aParentNode != null)
                aParentNode.appendChildXml(createXml());
        }

        @Override
        Element createXml() {
            Element lElement = createElement("children");
            lElement.setAttribute("xmi:type", "notation:Shape");
            lElement.setAttribute("xmi:id", new UuidId().toString());
            lElement.setAttribute("type", "State_Shape");
            Element lDecoration = createElement("children");
            lElement.appendChild(lDecoration);
            lDecoration.setAttribute("xmi:type", "notation:DecorationNode");
            lDecoration.setAttribute("xmi:id", new UuidId().toString());
            lDecoration.setAttribute("type", "State_NameLabel");

            Element lLayoutConstraint = createElement("layoutConstraint");
            lDecoration.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Bounds");
            lLayoutConstraint.setAttribute("xmi:id", new UuidId().toString());
            lLayoutConstraint.setAttribute("type", "StateMachine_NameLabel");
            lLayoutConstraint.setAttribute("width", "200");
            lLayoutConstraint.setAttribute("height", "25");

            lDecoration = createElement("children");
            lElement.appendChild(lDecoration);
            lDecoration.setAttribute("xmi:type", "notation:DecorationNode");
            lDecoration.setAttribute("xmi:id", new UuidId().toString());
            lDecoration.setAttribute("type", "State_FloatingNameLabel");

            lLayoutConstraint = createElement("layoutConstraint");
            lDecoration.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Location");
            lLayoutConstraint.setAttribute("xmi:id", getObjectId(null).toString());
            lLayoutConstraint.setAttribute("type", "StateMachine_NameLabel");
            lLayoutConstraint.setAttribute("x", "40");

//            <children xmi:type="notation:DecorationNode" xmi:id="_bYhTIGbFEeaiLfj2KKLRgQ" type="State_NameLabel">
//              <layoutConstraint xmi:type="notation:Bounds" xmi:id="_nkNP8WhREeaSjdksnF_ddQ" width="200" height="25"/>
//            </children>
//            <children xmi:type="notation:DecorationNode" xmi:id="_bYhTIWbFEeaiLfj2KKLRgQ" type="State_FloatingNameLabel">
//              <layoutConstraint xmi:type="notation:Location" xmi:id="_bYhTImbFEeaiLfj2KKLRgQ" x="40"/>
//            </children>
//            <children xmi:type="notation:BasicCompartment" xmi:id="_bYhTI2bFEeaiLfj2KKLRgQ" type="State_RegionCompartment">

            fSubElementRoot = createElement("children");
            lElement.appendChild(fSubElementRoot);
            fSubElementRoot.setAttribute("xmi:type", "notation:BasicCompartment");
            fSubElementRoot.setAttribute("xmi:id", getObjectId(null).toString());
            fSubElementRoot.setAttribute("type", "State_RegionCompartment");

            Element lE = createElement("element");
            lElement.appendChild(lE);
            lE.setAttribute("xmi:type", "uml:State");
            lE.setAttribute("href", PLACEHOLDER_FILENAME + '#' + fLinkedMUNode.getUuid());
            fChangeOnSave.add(lE.getAttributeNode("href"));

            lLayoutConstraint = createElement("layoutConstraint");
            lElement.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Bounds");
            lLayoutConstraint.setAttribute("xmi:id", getObjectId(null).toString());
            lLayoutConstraint.setAttribute("width", "700");
            lLayoutConstraint.setAttribute("height", "300");
            lLayoutConstraint.setAttribute("y", "25");
            lLayoutConstraint.setAttribute("x", "25");

            return lElement;
        }
    }

    class MNRootStateMachine extends MNStateMachine {

        MNRootStateMachine(Element aNotationRoot, ModelUml.MUStateMachineState aLinkedMUNode) {
            super(null, aLinkedMUNode);
            createXml(aNotationRoot);
        }

        private Element createXml(Element aRoot) {
            aRoot.appendChild(createXml());
            Element lStyles = createElement("styles");
            lStyles.setAttribute("xmi:type", "notation:StringValueStyle");
            lStyles.setAttribute("xmi:id", getObjectId(this).toString());
            lStyles.setAttribute("name", "diagram_compatibility_version");
            lStyles.setAttribute("stringValue", "1.2.0");
            aRoot.appendChild(lStyles);
            lStyles = createElement("styles");
            lStyles.setAttribute("xmi:type", "notation:DiagramStyle");
            lStyles.setAttribute("xmi:id", getObjectId(this).toString());
            aRoot.appendChild(lStyles);
            lStyles = createElement("styles");
            lStyles.setAttribute("xmi:type", "style:PapyrusViewStyle");
            lStyles.setAttribute("xmi:id", getObjectId(this).toString());
            aRoot.appendChild(lStyles);
            Element lSubElem = createElement("owner");
            lStyles.appendChild(lSubElem);
            lSubElem.setAttribute("xmi:type", "uml:StateMachine");
            lSubElem.setAttribute("href", PLACEHOLDER_FILENAME + "#" + fLinkedMUNode.getUuid());
            fChangeOnSave.add(lSubElem.getAttributeNode("href"));

            Element lE = createElement("element");
            aRoot.appendChild(lE);
            lE.setAttribute("xmi:type", "uml:StateMachine");
            lE.setAttribute("href", PLACEHOLDER_FILENAME + '#' + fLinkedMUNode.getUuid());
            fChangeOnSave.add(lE.getAttributeNode("href"));

//<styles xmi:type="notation:StringValueStyle" xmi:id="_vAu-_WktEeaWtbhQKbDHLA" name="diagram_compatibility_version" stringValue="1.2.0"/>
//    <styles xmi:type="notation:DiagramStyle" xmi:id="_vAu-_mktEeaWtbhQKbDHLA"/>
//    <styles xmi:type="style:PapyrusViewStyle" xmi:id="_vAu-_2ktEeaWtbhQKbDHLA">
//      <owner xmi:type="uml:StateMachine" href="test_model.uml#ee7db9e2-59f2-483c-a1c7-0132d9b0a85f"/>
//    </styles>
            return lStyles;
        }

        @Override
        Element createXml() {
            Element lElement = createElement("children");
            lElement.setAttribute("xmi:type", "notation:Shape");
            lElement.setAttribute("xmi:id", new UuidId().toString());
            lElement.setAttribute("type", "StateMachine_Shape");
            Element lDecoration = createElement("children");
            lElement.appendChild(lDecoration);
            lDecoration.setAttribute("xmi:type", "notation:DecorationNode");
            lDecoration.setAttribute("xmi:id", new UuidId().toString());
            lDecoration.setAttribute("type", "StateMachine_NameLabel");

            Element lLayoutConstraint = createElement("layoutConstraint");
            lDecoration.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Bounds");
            lLayoutConstraint.setAttribute("xmi:id", new UuidId().toString());
//            lLayoutConstraint.setAttribute("type", "StateMachine_NameLabel");
            lLayoutConstraint.setAttribute("width", "700");
            lLayoutConstraint.setAttribute("height", "100");

            //subelements
            /*
    <children xmi:type="notation:BasicCompartment" xmi:id="_AzFHlGkgEeaWtbhQKbDHLA" type="StateMachine_RegionCompartment">
    //Region:
      <children xmi:type="notation:Shape" xmi:id="_AzFHlWkgEeaWtbhQKbDHLA" type="Region_Shape">
        <eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_AzFHlmkgEeaWtbhQKbDHLA" source="RegionAnnotationKey">
          <details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_AzFHl2kgEeaWtbhQKbDHLA" key="RegionZoneKey" value=""/>
        </eAnnotations>
        <children xmi:type="notation:BasicCompartment" xmi:id="_AzFHmGkgEeaWtbhQKbDHLA" type="Region_SubvertexCompartment">
          <children xmi:type="notation:Shape" xmi:id="_DmfDUGkgEeaWtbhQKbDHLA" type="Pseudostate_InitialShape">
            <children xmi:type="notation:DecorationNode" xmi:id="_DmfqYGkgEeaWtbhQKbDHLA" type="Pseudostate_InitialFloatingNameLabel">
              <layoutConstraint xmi:type="notation:Location" xmi:id="_DmfqYWkgEeaWtbhQKbDHLA" x="25" y="3"/>
            </children>
            <children xmi:type="notation:DecorationNode" xmi:id="_DmgRcGkgEeaWtbhQKbDHLA" type="Pseudostate_InitialStereotypeLabel">
              <layoutConstraint xmi:type="notation:Location" xmi:id="_DmgRcWkgEeaWtbhQKbDHLA" x="25" y="-10"/>
            </children>
            <element xmi:type="uml:Pseudostate" href="model.uml#_DmVSUGkgEeaWtbhQKbDHLA"/>
            <layoutConstraint xmi:type="notation:Bounds" xmi:id="_DmfDUWkgEeaWtbhQKbDHLA" x="244" y="177"/>
          </children>
          <layoutConstraint xmi:type="notation:Bounds" xmi:id="_AzFHmWkgEeaWtbhQKbDHLA"/>
        </children>
        <element xmi:type="uml:Region" href="model.uml#_AzCrUGkgEeaWtbhQKbDHLA"/>
        <layoutConstraint xmi:type="notation:Bounds" xmi:id="_AzFHmmkgEeaWtbhQKbDHLA" width="700" height="287"/>
      </children>
      <layoutConstraint xmi:type="notation:Bounds" xmi:id="_AzFHm2kgEeaWtbhQKbDHLA" y="25" width="700" height="275"/>
    </children>
             */
            fSubElementRoot = createElement("children");
            lElement.appendChild(fSubElementRoot);
            fSubElementRoot.setAttribute("xmi:type", "notation:BasicCompartment");
            fSubElementRoot.setAttribute("xmi:id", new UuidId().toString());
            fSubElementRoot.setAttribute("type", "StateMachine_RegionCompartment");

            lLayoutConstraint = createElement("layoutConstraint");
            fSubElementRoot.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Bounds");
            lLayoutConstraint.setAttribute("xmi:id", new UuidId().toString());
            lLayoutConstraint.setAttribute("width", "700");
            lLayoutConstraint.setAttribute("height", "300");
            lLayoutConstraint.setAttribute("y", "25");


            Element lE = createElement("element");
            lElement.appendChild(lE);
            lE.setAttribute("xmi:type", "uml:StateMachine");
            lE.setAttribute("href", PLACEHOLDER_FILENAME + '#' + fLinkedMUNode.getUuid());
            fChangeOnSave.add(lE.getAttributeNode("href"));

            lLayoutConstraint = createElement("layoutConstraint");
            lElement.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Bounds");
            lLayoutConstraint.setAttribute("xmi:id", new UuidId().toString());
            lLayoutConstraint.setAttribute("width", "700");
            lLayoutConstraint.setAttribute("height", "300");
            lLayoutConstraint.setAttribute("y", "25");
            lLayoutConstraint.setAttribute("x", "25");
            return lElement;
        }
    }

    class MNTransition extends MNNode<ModelUml.MUTransition, MNRegionState> {

        public MNTransition(MNRegionState aParentNode, ModelUml.MUTransition aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }

        @Override
        Element createXml() {
            return null;
        }
    }

    private static final String PLACEHOLDER_FILENAME = "[[[FILENAME]]]";
    private final ModelUml fModelUml;
    List<Attr> fChangeOnSave = new ArrayList<>();
    MNStateMachine fRootState;
    private Element fDiagram;

    public ModelNotation(ModelUml aModelUml) throws ParserConfigurationException {
        super();
        appendStaticXml(getDocument());
        fModelUml = aModelUml;
    }

    public MNStateMachine getRootState(ModelUml.MUStateMachineState aRootState) {
        if (fRootState == null) {
            fRootState = new MNRootStateMachine(fDiagram, aRootState);
        }
        return fRootState;
    }

    @Override
    public void save(File aFilename) throws IOException {
        fChangeOnSave.forEach(lAttr -> {
            lAttr.setValue(lAttr.getValue().replace(PLACEHOLDER_FILENAME, aFilename.getName() + ".uml"));
        });
        super.save(new File(aFilename.getAbsolutePath() + ".notation"));
    }

    private IId getObjectId(Object aMNRootStateMachine) {
        return new UuidId();
    }

    private void appendStaticXml(Document aDocument) {
//        Element lRoot = createElementNS("http://www.omg.org/spec/XMI/20131001", "xmi:XMI");
//        aDocument.appendChild(lRoot);
//
//        lRoot.setAttribute("xmi:version", "2.0");
//        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xmi", "http://www.omg.org/spec/XMI");
//        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
//        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:uml", "http://www.eclipse.org/uml2/5.0.0/UML");
//        lRoot.setAttributeNS(
//                "http://www.w3.org/2000/xmlns/", "xmlns:style",
//                "http://www.eclipse.org/papyrus/infra/viewpoints/policy/style"
//        );
//        lRoot.setAttributeNS(
//                "http://www.w3.org/2000/xmlns/", "xmlns:notation", "http://www.eclipse.org/gmf/runtime/1.0.2/notation");
//        lRoot.setAttributeNS(
//                "http://www.w3.org/2000/xmlns/", "xmlns:style",
//                "http://www.eclipse.org/papyrus/infra/viewpoints/policy/style"
//        );

        Element lRoot = createElementNS("http://www.eclipse.org/gmf/runtime/1.0.2/notation", "notation:Diagram");
        aDocument.appendChild(lRoot);

        lRoot.setAttribute("xmi:version", "2.0");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xmi", "http://www.omg.org/spec/XMI");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
        lRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:uml", "http://www.eclipse.org/uml2/5.0.0/UML");
        lRoot.setAttributeNS(
                "http://www.w3.org/2000/xmlns/", "xmlns:style",
                "http://www.eclipse.org/papyrus/infra/viewpoints/policy/style"
        );
        lRoot.setAttributeNS(
                "http://www.w3.org/2000/xmlns/", "xmlns:notation", "http://www.eclipse.org/gmf/runtime/1.0.2/notation");
        lRoot.setAttributeNS(
                "http://www.w3.org/2000/xmlns/", "xmlns:style",
                "http://www.eclipse.org/papyrus/infra/viewpoints/policy/style"
        );

        fDiagram = lRoot;
//        lRoot.appendChild(fDiagram);
        fDiagram.setAttribute("type", "PapyrusUMLStateMachineDiagram");
        fDiagram.setAttribute("name", "SsmDiagram");
        fDiagram.setAttribute("xmi:id", getObjectId(null).toString());
        fDiagram.setAttribute("measurementUnit", "Pixel");

    }
}
