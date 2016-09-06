# spring-state-machine-chart-dumper
Library to Dump/draw a state machine chart out of a [Spring Statemachine](http://projects.spring.io/spring-statemachine/)

## Supported Formats

* [Eclipse MDT UML2](https://projects.eclipse.org/projects/modeling.mdt.uml2) (used by [Papyrus UML](https://eclipse.org/papyrus/) and [UML Designer](http://www.umldesigner.org/)) (incomplete, under development)
* [SCXML](https://www.w3.org/TR/scxml/) (incomplete, under development) 

## Status of Development
+ [x] States dumped 
+ [x] Transitions with event triggers dumped
+ [x] Sub machines dumped
+ [x] Regions dumped
+ [x] [MDT UML2](https://projects.eclipse.org/projects/modeling.mdt.uml2) output
+ [x] SCXML (not complete mapping)
+ [x] Guards dumped
+ [x] Pseudostate JOIN dumped
+ [x] Pseudostate FORK dumped
+ [x] Pseudostate CHOICE dumped
+ [x] Pseudostate JUNCTION dumped
+ [x] Actions not dumped

- [ ] Time triggers not dumped
- [ ] Lots of the Pseudostates not dumped with their specialization
- [ ] Tests are not yet real tests

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
            aE.printStackTrace();
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
            aE.printStackTrace();
        }
```
1. Run your application code that executes the above lines
1. After the run has completed use [UML Designer](http://www.umldesigner.org/) to open the generated `/path/to/output.uml` file and add a presentation (if you have choosen the `SsmMdtUml2Dumper` class as dumper):
    1. Create a workspace and project to copy the _/path/to/output_ in: (Needs only be done for the first time, if you haven't a UML Designer project)
        1. Start _UML Designer_
        1. Choose your workspace e.g. `~/workspace/UMLdesigner`
        1. Create a project e.g. `ssm_dumps`
    1. Copy the `/path/to/output.uml` to the project e.g. to `~/workspace/UMLdeisgner/ssm_dumps`
    1. \[...to be continued...\]

## Tools for viewing generated models

* MDT UML2
  * [Papyrus UML](https://eclipse.org/papyrus/)
  * [UML Designer](http://www.umldesigner.org/)
* SCXML
  * [SCXML GUI](https://github.com/fmorbini/scxmlgui)
