package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.uml2;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import java.io.IOException;

/**
 * Created by Christoph Graupner on 8/21/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class ModelUml {

    org.eclipse.uml2.uml.UMLFactory fFactory;
    Model                           fModel;
    ResourceSet resourceSet;

    public ModelUml() {
        fFactory = UMLFactory.eINSTANCE;
        fModel = fFactory.createModel();
        fModel.setName("SsmDumper");
        resourceSet = new ResourceSetImpl();
        // Initialize registrations of resource factories, library models,
        // profiles, Ecore metadata, and other dependencies required for
        // serializing and working with UML resources. This is only necessary in
        // applications that are not hosted in the Eclipse platform run-time, in
        // which case these registrations are discovered automatically from
        // Eclipse extension points.
        UMLResourcesUtil.init(resourceSet);
        org.eclipse.uml2.uml.Package umlLibrary = (org.eclipse.uml2.uml.Package) load(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
        fModel.createPackageImport(umlLibrary);
    }

    public void save(String Filename) {
        save(fModel, URI.createFileURI("./").appendSegment(Filename).appendFileExtension(UMLResource.FILE_EXTENSION));
    }

    protected void save(org.eclipse.uml2.uml.Package package_, URI uri) {

        // Create the output resource and add our model package to it.
        Resource resource;
        resource = resourceSet.createResource(uri);
        resource.getContents().add(package_);
        // And save
        try {
            resource.save(null); // no save options needed
            System.out.println("Done.");
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    protected PrimitiveType importPrimitiveType(org.eclipse.uml2.uml.Package package_, String name) {
        org.eclipse.uml2.uml.Package umlLibrary = load(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));

        PrimitiveType primitiveType = (PrimitiveType) umlLibrary.getOwnedType(name);

        package_.createElementImport(primitiveType);

        System.out.println("Primitive type '%s' imported." + primitiveType.getQualifiedName());

        return primitiveType;
    }

    protected org.eclipse.uml2.uml.Package load(URI uri) {
        org.eclipse.uml2.uml.Package package_ = null;

        try {
            // Load the requested resource
            Resource resource = resourceSet.getResource(uri, true);

            // Get the first (should be only) package from it
            package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
        } catch (WrappedException we) {
            System.err.println(we.getMessage());
        }

        return package_;
    }
}
