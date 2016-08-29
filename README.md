# spring-state-machine-chart-dumper
Library to Dump/draw an state machine chart out of a Spring Statemachine

## Status of Development
 + States dumped
 + Transitions with event triggers dumped
 + Sub machines dumped
 + Regions dumped
 + (MDT UML2)[https://wiki.eclipse.org/MDT/UML2] output
 + SCXML (not complete mapping)
  
 - Actions not dumped
 - Guards not dumped
 - Time triggers not dumped
 - Lots of the Pseudostates not dumped with their specialization
 - Tests are not yet real tests

## Usage

1. Put `net.workingdeveloper.java.spring.statemachine.dumper-*.jar` on your classpath
1. add these lines of code to your project source code:
    * Eclipse MDT UML2 dumper:
        ```java
        // StateMachine<S, E> stateMachine is configured before this point
        try {
            SsmDumper dumper = new SsmMdtUml2Dumper<>(stateMachine);
            File outputFile = new File("/path/to/output"); // ".uml" extension will be added by dumper
            dumper.dump().save(outputFile);
        } catch (IOException aE) {
            logger.error(aE);
        }
        ```
    * SCXML dumper:
        ```java
        // StateMachine<S, E> stateMachine is configured before this point
        try {
            SsmDumper dumper = new SsmMdtUml2Dumper<>(stateMachine);
            File outputFile = new File("/path/to/output"); // ".uml" extension will be added by dumper
            dumper.dump().save(outputFile);
        } catch (IOException aE) {
            logger.error(aE);
        }
        ```
1. Run your application code that executes the above lines
1. After the run has completed use (UML Designer)[http://www.umldesigner.org/] to open the generated `/path/to/output.uml` file and add a presentation (if you have choosen the `SsmMdtUml2Dumper` class as dumper):
    1. Create a workspace and project to copy the "/path/to/output" in: (Needs only be done for the first time, if you haven't a UML Designer project)
        1. Start UML Designer
        1. Choose your workspace e.g. `~/workspace/UMLdesigner`
        1. Create a project e.g. `ssm_dumps`
    1. Copy the "/path/to/output.uml" to the project e.g. to `~/workspace/UMLdeisgner/ssm_dumps`
    1. \[...to be continued...\]
