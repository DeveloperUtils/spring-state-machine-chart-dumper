package net.workingdeveloper.java.spring.statemachine.dumper;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.region.Region;
import org.springframework.statemachine.state.AbstractState;
import org.springframework.statemachine.state.RegionState;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import org.w3c.dom.Comment;
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
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Christoph Graupner on 8/16/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
@Service
public class SsmScxmlDumper<S, E> extends SsmDumper<S, E> {

    private Document fXmlDocument;

    public SsmScxmlDumper(StateMachine<S, E> aStateMachine) {
        super(aStateMachine);
    }

    public String asString() throws TransformerException {
        TransformerFactory tf          = TransformerFactory.newInstance();
        Transformer        transformer = tf.newTransformer();
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
    }

    public Comment createComment(String data) {
        return getXmlDocument().createComment(data);
    }

    public SsmScxmlDumper dump() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder        docBuilder = null;
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

    public SsmScxmlDumper dump(Document aOutputDocument) {
        fXmlDocument = aOutputDocument;
        Element lScxmlRoot = aOutputDocument.createElement("scxml");
        aOutputDocument.appendChild(lScxmlRoot);
        lScxmlRoot.setAttribute("version", "1.0");
        lScxmlRoot.setAttribute("xmlns", "http://www.w3.org/2005/07/scxml");

        processSubMachine(lScxmlRoot, fStateMachine);

        return this;
    }

    public void toFile(File aFile) throws TransformerException, IOException {
        List<String> lList = Arrays.asList(dump().asString());
        Files.write(aFile.toPath(), lList, Charset.forName("UTF-8"));
    }

    protected Element createElement(String aS) throws DOMException {
        return getXmlDocument().createElement(aS);
    }

    protected Document getXmlDocument() {
        return fXmlDocument;
    }

    private Element processState(Element aRoot, State<S, E> lState) {
        Element lXml;
        if (lState.getPseudoState() != null) {
            switch (lState.getPseudoState().getKind()) {
                case END:
                    lXml = createElement("final");
                    break;
                case HISTORY_SHALLOW:
                case HISTORY_DEEP:
                    lXml = createElement("history");
                    break;
                case FORK:
                    lXml = createElement("parallel");
                    break;
                case EXIT:
                    lXml = createElement("state");
                    break;
                case INITIAL:
                case CHOICE:
                case JUNCTION:
                case JOIN:
                case ENTRY:
                default:
                    lXml = createElement("state");
                    break;
            }
            lXml.appendChild(createComment("PseudoState: " + lState.getPseudoState().getKind()));
        } else {
            lXml = createElement("state");
        }

        lXml.setAttribute("id", lState.getId().toString());
        aRoot.appendChild(lXml);

        if (lState.isSimple()) {
        } else {
            if (lState.isSubmachineState()) {
                if (lState instanceof AbstractState) {
                    processSubMachine(lXml, ((AbstractState<S, E>) lState).getSubmachine());
                }
            }
            if (lState.isComposite()) {
                if (lState instanceof RegionState) {
                    processRegionState(lXml, ((RegionState<S, E>) lState));
                }
                if (lState.isOrthogonal()) {
                }
            }
        }
        processTransitions(lXml, lState);
        processActions(lXml, lState);
        return lXml;
    }

    private void processActions(Element aXml, State<S, E> aLState) {
        if (aLState.getEntryActions() != null) {
            for (Action<S, E> lAction : aLState.getEntryActions()) {
                Element lOnentry = createElement("onentry");
                Element lRaise   = createElement("raise");
                lRaise.setAttribute("event", lAction.toString());
                lOnentry.appendChild(lRaise);
                aXml.appendChild(lOnentry);
            }
        }
        if (aLState.getExitActions() != null) {
            for (Action<S, E> lAction : aLState.getExitActions()) {
                Element lOnentry = createElement("onexit");
                Element lRaise   = createElement("raise");
                lRaise.setAttribute("event", lAction.toString());
                lOnentry.appendChild(lRaise);
                aXml.appendChild(lOnentry);
            }
        }
    }

    private void processRegionState(Element aXml, RegionState<S, E> aRegionState) {
        Element lRegionRootXml = createElement("parallel");
        lRegionRootXml.setAttribute("id", aRegionState.getId().toString());
        aXml.appendChild(lRegionRootXml);
        int regCount = 0;

        for (Region<S, E> lRegion : aRegionState.getRegions()) {
            Element lRegionXml = createElement("state");
            lRegionXml.setAttribute("id", aRegionState.getId().toString() + "r" + regCount);
            lRegionRootXml.appendChild(lRegionXml);
            Collection<State<S, E>> lStates = lRegion.getStates();
            processStates(lRegionXml, lStates);
            Collection<Transition<S, E>> lTransitions = lRegion.getTransitions();
            for (Transition<S, E> lTransition : lTransitions) {
                Element lXml = createElement("transition");
                aXml.appendChild(lXml);

                lXml.setAttribute("event", lTransition.getTrigger().getEvent().toString());
                lXml.setAttribute("target", lTransition.getTarget().getId().toString());
            }
            regCount++;
        }
    }

    private void processSubMachine(Element aXml, StateMachine<S, E> aStateMachine) {
        Collection<State<S, E>> lStates = aStateMachine.getStates();
        if (aStateMachine.getInitialState() != null) {
            aXml.setAttribute("initial", aStateMachine.getInitialState().getId().toString());
        }

        processStates(aXml, lStates);
    }

    private void processStates(Element aXml, Collection<State<S, E>> aStates) {
        for (State<S, E> lState : aStates) {
            Element lChildXml = processState(aXml, lState);
        }

    }

    private void processTransitions(Element aXml, State<S, E> aState) {
        Collection<Transition<S, E>> lTransitions = fStateMachine.getTransitions();
        for (Transition<S, E> lTransition : lTransitions) {
            if (aState.equals(lTransition.getSource())) {
                Element lXml = createElement("transition");
                aXml.appendChild(lXml);

                lXml.setAttribute("event", lTransition.getTrigger().getEvent().toString());
                lXml.setAttribute("target", lTransition.getTarget().getId().toString());
            }
        }
    }

}
