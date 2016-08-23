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
    class MNNode<T extends ModelUml.MUNode, PARENT extends MNNode> {

        Element fDiagramNode;
        T       fLinkedMUNode;
        PARENT  fParentNode;

        public MNNode(PARENT aParentNode, T aLinkedMUNode) {
            fParentNode = aParentNode;
            fLinkedMUNode = aLinkedMUNode;
        }

    }

    class MNState<T extends ModelUml.MUNode, PARENT extends MNState> extends MNNode<T, PARENT> {
        MNState(PARENT aParentNode, T aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }
    }

    class MNSimpleState extends MNState<ModelUml.MUSimpleState, MNRegionMachineShared> {
        MNSimpleState(MNRegionMachineShared aParentNode, ModelUml.MUSimpleState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }
    }

    class MNPseudoState extends MNState<ModelUml.MUPseudoState, MNRegionMachineShared> {

        MNPseudoState(MNRegionMachineShared aParentNode, ModelUml.MUPseudoState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }
    }

    abstract class MNRegionMachineShared<T extends ModelUml.MUNode, PARENT extends MNState> extends MNState<T, PARENT> {

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
    }

    class MNRegionState extends MNRegionMachineShared<ModelUml.MURegionState, MNRegionMachineShared> {
        MNRegionState(MNRegionMachineShared aParentNode, ModelUml.MURegionState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }

    }

    class MNStateMachine extends MNRegionMachineShared<ModelUml.MUStateMachineState, MNRegionMachineShared> {

        MNStateMachine(MNRegionMachineShared aParentNode, ModelUml.MUStateMachineState aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
        }
    }

    class MNRootStateMachine extends MNStateMachine {

        MNRootStateMachine(Element aNotationRoot, ModelUml.MUStateMachineState aLinkedMUNode) {
            super(null, aLinkedMUNode);
            createXml(aNotationRoot);
        }

        private Element createXml(Element aRoot) {
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

//<styles xmi:type="notation:StringValueStyle" xmi:id="_vAu-_WktEeaWtbhQKbDHLA" name="diagram_compatibility_version" stringValue="1.2.0"/>
//    <styles xmi:type="notation:DiagramStyle" xmi:id="_vAu-_mktEeaWtbhQKbDHLA"/>
//    <styles xmi:type="style:PapyrusViewStyle" xmi:id="_vAu-_2ktEeaWtbhQKbDHLA">
//      <owner xmi:type="uml:StateMachine" href="test_model.uml#ee7db9e2-59f2-483c-a1c7-0132d9b0a85f"/>
//    </styles>
            return null;
        }
    }

    class MNTransition extends MNNode<ModelUml.MUTransition, MNRegionState> {

        public MNTransition(MNRegionState aParentNode, ModelUml.MUTransition aLinkedMUNode) {
            super(aParentNode, aLinkedMUNode);
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
        Element lRoot = createElementNS("http://www.omg.org/spec/XMI/20131001", "xmi:XMI");
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

        fDiagram = createElementNS("http://www.eclipse.org/gmf/runtime/1.0.2/notation", "Diagram");
        lRoot.appendChild(fDiagram);
        fDiagram.setAttribute("type", "PapyrusUMLStateMachineDiagram");
        fDiagram.setAttribute("name", "SsmDiagram");
        fDiagram.setAttribute("measurementUnit", "Pixel");

    }
}
