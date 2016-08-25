package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
class ModelNotation extends ModelXmlBase {
    class MNBounds {
        int height;
        int width;
        int x;
        int y;

        public MNBounds(int aX, int aY, int aWidth, int aHeight) {
            x = aX;
            y = aY;
            width = aWidth;
            height = aHeight;
        }

        public MNBounds(int aWidth, int aHeight) {
            width = aWidth;
            height = aHeight;
        }
    }

    class MNFinalPseudoState extends MNPseudoState {

        MNFinalPseudoState(MNRegionMachineShared aParentNode, ModelUml.MUPseudoState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }

        @Override
        /**
         * <pre>
         <children xmi:type="notation:Shape" xmi:id="_SKUdEGhYEeaSjdksnF_ddQ" type="FinalState_Shape">
         <children xmi:type="notation:DecorationNode" xmi:id="_SKVEIGhYEeaSjdksnF_ddQ" type="FinalState_FloatingNameLabel">
         <layoutConstraint xmi:type="notation:Location" xmi:id="_SKVEIWhYEeaSjdksnF_ddQ" x="25" y="3"/>
         </children>
         <children xmi:type="notation:DecorationNode" xmi:id="_SKVEImhYEeaSjdksnF_ddQ" type="FinalState_StereotypeLabel">
         <layoutConstraint xmi:type="notation:Location" xmi:id="_SKVrMGhYEeaSjdksnF_ddQ" x="25" y="-10"/>
         </children>
         <element xmi:type="uml:FinalState" href="model3.uml#_SKOWcGhYEeaSjdksnF_ddQ"/>
         <layoutConstraint xmi:type="notation:Bounds" xmi:id="_SKUdEWhYEeaSjdksnF_ddQ" x="508" y="133"/>
         </children>
         </pre>
         */
        Element createXml() {
            return super.createXml();
        }
    }

    class MNInitialPseudoState extends MNPseudoState {

        MNInitialPseudoState(MNRegionMachineShared aParentNode, ModelUml.MUPseudoState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }

        @Override
        Element createXml() {
            /*
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

             */
            return super.createXml();
        }
    }

    abstract class MNNode<T extends ModelUml.MUNode, PARENT extends MNNode> {

        Element fDiagramNode;
        T       fLinkedMUNode;
        PARENT  fParentNode;

        MNNode(PARENT aParentNode, T aLinkedMUNode) {
            fParentNode = aParentNode;
            fLinkedMUNode = aLinkedMUNode;
        }

        Element getDiagramNode() {
            return fDiagramNode;
        }

        T getLinkedMUNode() {
            return fLinkedMUNode;
        }

        PARENT getParentNode() {
            return fParentNode;
        }

        abstract Element createXml();
    }

    class MNPseudoState extends MNState<ModelUml.MUPseudoState, MNRegionMachineShared> {

        MNPseudoState(MNRegionMachineShared aParentNode, ModelUml.MUPseudoState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
            aParentNode.appendChildXml(createXml());
        }

        @Override
        MNBounds getBounds() {
            return new MNBounds(0, 0, 0, 0);
//            Element lWhole = fSizeAttrMap.get("whole.size");
//            return new MNBounds(
//                    Integer.valueOf(lWhole.getAttribute("width")),
//                    Integer.valueOf(lWhole.getAttribute("height"))
//            );
        }

        @Override
        void updateShapeInternal() {
        }

        @Override
        Element createXml() {
            /*
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

             */

            return null;
        }
    }

    abstract class MNRegionMachineShared<T extends ModelUml.MUNode, PARENT extends MNRegionMachineShared>
            extends MNState<T, PARENT> {

        List<MNState> fChildren = new ArrayList<>();
        Element fSubElementRoot;

        MNRegionMachineShared(PARENT aParentNode, T aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
            fChildren = new ArrayList<>();
            aParentNode.appendChildXml(createXml());
        }

        MNRegionMachineShared(T aLinkedMUNode) {
            super(aLinkedMUNode);
        }

        @Override
        void updateShapeInternal() {
            MNBounds lBounds = new MNBounds(0, 0);
            if (fChildren.isEmpty()) {
                lBounds.width = 200;
                lBounds.height = 50;
            } else {
                fChildren.forEach(MNState::updateShape);
                for (MNState lChild : fChildren) {

                    MNBounds lChildBounds = lChild.getBounds();
                    if (lChildBounds == null) {
                        System.out.println(lChild.getLinkedMUNode().getName());
                        assert lChildBounds != null;
                    }

                    lBounds.height += lChildBounds.height;
                    lBounds.width += lChildBounds.width;
                }
            }
            setViewSize("whole.size", lBounds.width, lBounds.height);
        }

        MNPseudoState addPseudoState(ModelUml.MUPseudoState aMUNode) {
            MNPseudoState lPseudoState = new MNPseudoState(this, aMUNode);
            fChildren.add(lPseudoState);
            updateShape();
            return lPseudoState;
        }

        MNRegionState addRegionState(ModelUml.MURegionState aMUNode) {
            MNRegionState lRegionState = new MNRegionState(this, aMUNode);
            fChildren.add(lRegionState);
            updateShape();
            return lRegionState;
        }

        MNSimpleState addSimpleState(ModelUml.MUSimpleState aMUNode) {
            MNSimpleState lSimpleState = new MNSimpleState(this, aMUNode);
            fChildren.add(lSimpleState);
            updateShape();
            return lSimpleState;
        }

        MNStateMachine addSubMachine(ModelUml.MUStateMachineState aMUNode) {
            MNStateMachine lStateMachine = new MNStateMachine(this, aMUNode);
            fChildren.add(lStateMachine);
            updateShape();
            return lStateMachine;
        }

        void appendChildXml(Element aXml) {
            if (fSubElementRoot != null && aXml != null)
                fSubElementRoot.appendChild(aXml);
        }

        int getMaxLevel() {
            int maxLevel = 0;
            for (MNState lChild : fChildren) {
                if (lChild instanceof MNRegionMachineShared) {
                    maxLevel = Math.max(maxLevel, 1 + ((MNRegionMachineShared) lChild).getMaxLevel());
                }
            }
            return maxLevel + getLevel();
        }

        List<MNRegionState> getAllRegions() {
            List<MNRegionState> lReturn = new ArrayList<>();
            fChildren.forEach(lChild -> {
                if (lChild instanceof MNRegionState)
                    lReturn.add((MNRegionState) lChild);
            });
            return lReturn;
        }
    }

    class MNRegionState extends MNRegionMachineShared<ModelUml.MURegionState, MNRegionMachineShared> {
        MNRegionState(MNRegionMachineShared aParentNode, ModelUml.MURegionState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
            fDiagramNode = createXml();
            aParentNode.appendChildXml(fDiagramNode);
        }

        void setAlign(String aS) {
            fSizeAttrMap.get("RegionZoneKey").setAttribute("value", aS);
        }

        @Override
        MNBounds getBounds() {
            Element lSize = fSizeAttrMap.get("whole.size");
            MNBounds lBounds = new MNBounds(
                    Integer.valueOf(lSize.getAttribute("width")),
                    Integer.valueOf(lSize.getAttribute("height"))
            );
            return lBounds;
        }

        @Override
        void updateShapeInternal() {
            super.updateShapeInternal();
            List<MNRegionState> lRegions = getParentNode().getAllRegions();
            MNBounds            lNeededBounds = new MNBounds(0, 0, 0, 0);
            lRegions.forEach(lRegion -> {
                MNBounds lBounds = lRegion.getBounds();
                lNeededBounds.height += lBounds.height;
                lNeededBounds.width += lBounds.width;
            });
            int lPosX = 0;
            for (int lI = 0; lI < lRegions.size(); lI++) {
                MNRegionState lRegion = lRegions.get(lI);
                if (this.equals(lRegion)) {
                    this.setViewSize(lNeededBounds.width / lRegions.size(), lRegion.getBounds().height);
                    this.setViewPosition(lPosX, lRegion.getBounds().y);
                    this.setAlign((lRegions.size() > 1) ? (lI == 0 ? "L" : "R") : "");
                }
                lPosX += lRegion.getBounds().width;
            }
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
            fSizeAttrMap.put("RegionZoneKey", lElementECoreDetails);

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
            fSizeAttrMap.put("whole.size", lLayoutConstraint);
            fSizeAttrMap.put("whole.position", lLayoutConstraint);

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

    class MNRootStateMachine extends MNStateMachine {

        MNRootStateMachine(Element aNotationRoot, ModelUml.MUStateMachineState aLinkedMUNode) {
            super(aLinkedMUNode);
            fDiagramNode = createXml(aNotationRoot);
        }

        @Override
        void initState() {
        }

        @Override
        MNBounds getBounds() {
            Element lSize = fSizeAttrMap.get("whole.size");
            MNBounds lBounds = new MNBounds(
                    Integer.valueOf(lSize.getAttribute("width")),
                    Integer.valueOf(lSize.getAttribute("height"))
            );
            return lBounds;
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
            fSizeAttrMap.put("StateMachine_NameLabel.size", lLayoutConstraint);
            fSizeAttrMap.put("StateMachine_NameLabel.position", lLayoutConstraint);

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
            fSizeAttrMap.put("StateMachine_RegionCompartment.size", lLayoutConstraint);
            fSizeAttrMap.put("StateMachine_RegionCompartment.position", lLayoutConstraint);

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
            fSizeAttrMap.put("whole.size", lLayoutConstraint);
            fSizeAttrMap.put("whole.position", lLayoutConstraint);

            return lElement;
        }
    }

    class MNSimpleState extends MNState<ModelUml.MUSimpleState, MNRegionMachineShared> {
        MNSimpleState(MNRegionMachineShared aParentNode, ModelUml.MUSimpleState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
            updateShape();
        }

        @Override
        MNBounds getBounds() {
            Element lSize = fSizeAttrMap.get("whole.size");

            return new MNBounds(
                    Integer.valueOf(lSize.getAttribute("width")),
                    Integer.valueOf(lSize.getAttribute("height"))
            );
        }

        @Override
        void updateShapeInternal() {
            setViewSize("whole.size", 100, 30);
        }

        /**
         * @return nix <pre>
         *           <children xmi:type="notation:Shape" xmi:id="_zDddUGhYEeaSjdksnF_ddQ" type="State_Shape">
         * <children xmi:type="notation:DecorationNode" xmi:id="_zDddUmhYEeaSjdksnF_ddQ" type="State_NameLabel"/>
         * <children xmi:type="notation:DecorationNode" xmi:id="_zDddU2hYEeaSjdksnF_ddQ" type="State_FloatingNameLabel">
         * <layoutConstraint xmi:type="notation:Location" xmi:id="_zDddVGhYEeaSjdksnF_ddQ" x="40"/>
         * </children>
         * <children xmi:type="notation:BasicCompartment" xmi:id="_zDddVWhYEeaSjdksnF_ddQ" type="State_RegionCompartment">
         * <layoutConstraint xmi:type="notation:Bounds" xmi:id="_zDddVmhYEeaSjdksnF_ddQ"/>
         * </children>
         * <element xmi:type="uml:State" href="model3.uml#_zDU6cGhYEeaSjdksnF_ddQ"/>
         * <layoutConstraint xmi:type="notation:Bounds" xmi:id="_zDddUWhYEeaSjdksnF_ddQ" x="56" y="20"/>
         * </children>
         * </pre>
         */
        @Override
        Element createXml() {
            Element lElement = createElement("children");
            lElement.setAttribute("xmi:type", "notation:Shape");
            lElement.setAttribute("xmi:id", getObjectId(null).toString());
            lElement.setAttribute("type", "State_Shape");

            Element lDecoration = createElement("children");
            lElement.appendChild(lDecoration);
            lDecoration.setAttribute("xmi:type", "notation:DecorationNode");
            lDecoration.setAttribute("xmi:id", getObjectId(null).toString());
            lDecoration.setAttribute("type", "State_NameLabel");

            lDecoration = createElement("children");
            lElement.appendChild(lDecoration);
            lDecoration.setAttribute("xmi:type", "notation:DecorationNode");
            lDecoration.setAttribute("xmi:id", getObjectId(null).toString());
            lDecoration.setAttribute("type", "State_FloatingNameLabel");

            Element lLayoutConstraint = createElement("layoutConstraint");
            lDecoration.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Location");
            lLayoutConstraint.setAttribute("xmi:id", getObjectId(null).toString());
            lLayoutConstraint.setAttribute("x", "20");
            lLayoutConstraint.setAttribute("y", "20");
            fSizeAttrMap.put("State_FloatingNameLabel.position", lLayoutConstraint);

/*
        <eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_E9WGgmn6Eeaq15FhJ2U9ZA" source="RegionAnnotationKey">
          <details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_E9WtkGn6Eeaq15FhJ2U9ZA" key="RegionZoneKey" value="R"/>
        </eAnnotations>

 */
            Element fSubElementRoot = createElement("children");
            lElement.appendChild(fSubElementRoot);
            fSubElementRoot.setAttribute("xmi:type", "notation:BasicCompartment");
            fSubElementRoot.setAttribute("xmi:id", new UuidId().toString());
            fSubElementRoot.setAttribute("type", "State_RegionCompartment");

            lLayoutConstraint = createElement("layoutConstraint");
            fSubElementRoot.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Bounds");
            lLayoutConstraint.setAttribute("xmi:id", getObjectId(null).toString());
            fSizeAttrMap.put("State_RegionCompartment.size", lLayoutConstraint);
            fSizeAttrMap.put("State_RegionCompartment.position", lLayoutConstraint);

            Element lE = createElement("element");
            lElement.appendChild(lE);
            lE.setAttribute("xmi:type", "uml:State");
            lE.setAttribute("href", PLACEHOLDER_FILENAME + '#' + fLinkedMUNode.getUuid());
            fChangeOnSave.add(lE.getAttributeNode("href"));

            lLayoutConstraint = createElement("layoutConstraint");
            lElement.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Bounds");
            lLayoutConstraint.setAttribute("xmi:id", getObjectId(null).toString());
            lLayoutConstraint.setAttribute("width", "100");
            lLayoutConstraint.setAttribute("height", "25");
            fSizeAttrMap.put("whole.size", lLayoutConstraint);
            fSizeAttrMap.put("whole.position", lLayoutConstraint);

            return lElement;
        }
    }

    abstract class MNState<T extends ModelUml.MUNode, PARENT extends MNRegionMachineShared> extends MNNode<T, PARENT> {
        boolean fInUpdate;
        Map<String, Element> fSizeAttrMap = new HashMap<>();

        MNState(PARENT aParentNode, T aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
            fInUpdate = false;
            initState();
        }

        MNState(T aLinkedMUNode) {
            super(null, aLinkedMUNode);
            initState();
        }

        public boolean isInUpdate() {
            return fInUpdate;
        }

        public void setInUpdate(boolean aInUpdate) {
            fInUpdate = aInUpdate;
        }

        public void setViewPosition(int aPosX, int aY) {
            setViewPosition("whole.position", aPosX, aY);
        }

        public void setViewSize(int aWidth, int aHeight) {
            setViewSize("whole.size", aWidth, aHeight);
        }

        void initState() {
            fDiagramNode = createXml();
            fParentNode.appendChildXml(fDiagramNode);
        }

        abstract MNBounds getBounds();

        <T2 extends MNState> T2 setViewSize(String aViewName, int aWidth, int aHeight) {
            Element lView = fSizeAttrMap.get(aViewName);
            lView.setAttribute("width", aWidth < 0 ? null : String.valueOf(aWidth));
            lView.setAttribute("height", aHeight < 0 ? null : String.valueOf(aHeight));
            return (T2) this;
        }

        <T2 extends MNState> T2 setViewPosition(String aViewName, int x, int y) {
            Element lView = fSizeAttrMap.get(aViewName);
            lView.setAttribute("x", x < 0 ? null : String.valueOf(x));
            lView.setAttribute("y", y < 0 ? null : String.valueOf(y));
            return (T2) this;
        }

        final <T2 extends MNState> T2 updateShape() {
            if (isInUpdate())
                return (T2) this;
            setInUpdate(true);
            updateShapeInternal();
            if (getParentNode() != null)
                getParentNode().updateShape();
            setInUpdate(false);
            return (T2) this;
        }

        abstract void updateShapeInternal();

        int getLevel() {
            if (getParentNode() == null)
                return 1;
            return getParentNode().getLevel() + 1;
        }
    }

    class MNStateMachine extends MNRegionMachineShared<ModelUml.MUStateMachineState, MNRegionMachineShared> {

        MNStateMachine(MNRegionMachineShared aParentNode, ModelUml.MUStateMachineState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }

        MNStateMachine(ModelUml.MUStateMachineState aLinkedMUNode) {
            super(aLinkedMUNode);
        }

        @Override
        MNBounds getBounds() {
            Element lSize = fSizeAttrMap.get("whole.size");
            MNBounds lBounds = new MNBounds(
                    Integer.valueOf(lSize.getAttribute("width")),
                    Integer.valueOf(lSize.getAttribute("height"))
            );
            return lBounds;
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
            fSizeAttrMap.put("StateMachine_NameLabel.size", lLayoutConstraint);
            fSizeAttrMap.put("StateMachine_NameLabel.position", lLayoutConstraint);

            lDecoration = createElement("children");
            lElement.appendChild(lDecoration);
            lDecoration.setAttribute("xmi:type", "notation:DecorationNode");
            lDecoration.setAttribute("xmi:id", new UuidId().toString());
            lDecoration.setAttribute("type", "State_FloatingNameLabel");

            lLayoutConstraint = createElement("layoutConstraint");
            lDecoration.appendChild(lLayoutConstraint);
            lLayoutConstraint.setAttribute("xmi:type", "notation:Location");
            lLayoutConstraint.setAttribute("xmi:id", getObjectId(null).toString());
            lLayoutConstraint.setAttribute("type", "State_FloatingNameLabel");
            lLayoutConstraint.setAttribute("x", "40");
            fSizeAttrMap.put("State_FloatingNameLabel.position", lLayoutConstraint);

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
            fSizeAttrMap.put("whole.size", lLayoutConstraint);
            fSizeAttrMap.put("whole.position", lLayoutConstraint);

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
    MNRootStateMachine fRootState;
    private Element fDiagram;

    public ModelNotation(ModelUml aModelUml) throws ParserConfigurationException {
        super();
        appendStaticXml(getDocument());
        fModelUml = aModelUml;
    }

    public MNRootStateMachine getRootState(ModelUml.MUStateMachineState aRootState) {
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
