package net.workingdeveloper.java.spring.statemachine.dumper;

import net.workingdeveloper.java.spring.statemachine.dumper.testdata.SMHierarch1;
import net.workingdeveloper.java.spring.statemachine.dumper.testdata.T1;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.test.AbstractStateMachineTests;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {T1.class, SMHierarch1.class})
public class SsmScxmlDumperTest extends AbstractStateMachineTests {
    @Autowired
    private ApplicationContext fApplicationContext;

    @Test
    public void dump() throws Exception {
        SsmScxmlDumper<T1.States, T1.Events> sut        = new SsmScxmlDumper<>((new T1()).buildMachine());
        DocumentBuilderFactory               docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder                      docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        sut.dump(doc);
        System.out.println(sut.asString());
    }

    @Test
    public void dumpHierarchy() throws Exception {
        SsmScxmlDumper<SMHierarch1.States, SMHierarch1.Events> sut = new SsmScxmlDumper<>(
                fApplicationContext.getBean(SMHierarch1.class).buildMachine()
        );
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder        docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document lDocument = docBuilder.newDocument();
        sut.dump(lDocument);
        System.out.println(sut.asString());
        NodeList        lX     = lDocument.getDocumentElement().getElementsByTagName("state");
        HashSet<String> lIdMap = new HashSet<>();
        for (int i = 0; i < lX.getLength(); i++) {
            String lId = ((Element) lX.item(i)).getAttribute("id");
            assertThat(lIdMap, not(hasItem(lId)));
            lIdMap.add(lId);
        }

//        assertThat(lDocument, not(isEmptyOrNullString()));
//        assertThat(lDocument, hasXPath(
//                "/scxml",
//                allOf(
//                        is(not(emptyString())),
//                        is(not(equalTo(EInfoMarker.TEXT_TO_ADD_MANUALLY.toString())))
//                )
//        ));

    }

    @Test
    public void dumpRegion() throws Exception {
        SsmScxmlDumper<SMHierarch1.States, SMHierarch1.Events> sut = new SsmScxmlDumper<>(
                fApplicationContext.getBean(SMHierarch1.class).buildRegionMachine()
        );
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder        docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document lDocument = docBuilder.newDocument();
        sut.dump(lDocument);
        System.out.println(sut.asString());
        NodeList        lX     = lDocument.getDocumentElement().getElementsByTagName("state");
        HashSet<String> lIdMap = new HashSet<>();
        for (int i = 0; i < lX.getLength(); i++) {
            String lId = ((Element) lX.item(i)).getAttribute("id");
            assertThat(lIdMap, not(hasItem(lId)));
            lIdMap.add(lId);
        }
        lX = lDocument.getDocumentElement().getElementsByTagName("final");
        for (int i = 0; i < lX.getLength(); i++) {
            String lId = ((Element) lX.item(i)).getAttribute("id");
            assertThat(lIdMap, not(hasItem(lId)));
            lIdMap.add(lId);
        }
        assertThat(
            lIdMap,
            Matchers.containsInAnyOrder(
                        SMHierarch1.States.S1.toString(), SMHierarch1.States.S21.toString(),
                        SMHierarch1.States.S2.toString(), SMHierarch1.States.S2F.toString(),
                        SMHierarch1.States.S3I.toString(), SMHierarch1.States.S31.toString(),
                        SMHierarch1.States.S3F.toString(), SMHierarch1.States.S2I.toString(),
                        SMHierarch1.States.SE.toString(), "S2r1", "S2r0"
                )
        );
    }
}
