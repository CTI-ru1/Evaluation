This repository is part of the [codebender.cc](http://www.codebender.cc) maker and artist web platform.

## And what's that?

codebender comes to fill the need for reliable and easy to use tools for makers. A need that from our own experience could not be totally fulfilled by any of the existing solutions. Things like installing libraries, updating the software or installing the IDE can be quite a painful process.

In addition to the above, the limited features provided (e.g. insufficient highlighting, indentation and autocompletion) got us starting building codebender, a completely web-based IDE, that requires no installation and offers a great code editor. It also stores your sketches on the cloud.

That way, you can still access your sketches safely even if your laptop is stolen or your hard drive fails! codebender also takes care of compilation, giving you extremely descriptive warnings on terrible code. On top of that, when you are done, you can upload your code to your Arduino straight from the browser without installing anything.

Currently codebender.cc is running its beta and we are trying to fix issues that may (will) come up so that we can launch and offer our services to everyone!
If you like what we do you can also support our campaign on [indiegogo](http://www.indiegogo.com/codebender) to also get early access to codebender! 

## How do integrationTests come into the picture?

integrationTests repository is a java based application used to check the performance of severa aspects of codebender.cc Web Based Arduino IDE by simulating the operation of users on different browsers and platforms.

Currently we cannot provide you with insights to the tests we are running, but a hint is that we use this system to automatically register new users ;)

## Interested in more technical stuff?

integrationTests are based on Selenium for Java.

Tests are executed using Maven 2 and the Surefire Plugin for running integration Tests.

Some other technologies and libraries we use during the implementation and testing are the following:

* [Apache Maven](http://maven.apache.org/)
* [Apache Log4j](http://logging.apache.org/log4j/1.2/manual.html)
* [Selenium](http://seleniumhq.org/) to simulate user behaviour
* JUnit and Surefire for running tests on Maven
* Java [Checkstyle](http://checkstyle.sourceforge.net/), [Pmd](http://pmd.sourceforge.net/pmd-5.0.0/), and [Findbugs](http://findbugs.sourceforge.net/) for code maintenance
* and the [Hudson Continuous Integration Server](http://java.net/projects/hudson/) for executing the tests.
