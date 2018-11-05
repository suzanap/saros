---
title: Saros Testing Framework (STF)
---

# {{ page.title }}
{:.no_toc}

{% include toc.html %}

For further information
about STF take a look at the [STF User
Manual](http://saros-build.imp.fu-berlin.de/stf/STF_Manual.pdf).

## Eclipse

### Prerequisites

* Development environment with **Eclipse 4.6**. It is also possible to use newer version, but some buttons are renamed in newer version which leads to a few error.

### Configuration

* Before you can run test tests you need to create accounts as described in the [user documentation](../../documentation/getting-started.html).
* Then you have to create the file `configuration.properties` in directory `de.fu_berlin.inf.dpp/test/framework/stf/src/de/fu_berlin/inf/dpp/stf/client`.
* Add the following lines to the config and **replace the placeholders with your credentials** (make sure **every tester has an unique JID**)

```properties
ALICE_JID = <insert user here>@saros-con.imp.fu-berlin.de/Saros
ALICE_PASSWORD = <insert password here>
ALICE_HOST = localhost
ALICE_PORT = 12345

BOB_JID = <insert user here>@saros-con.imp.fu-berlin.de/Saros
BOB_PASSWORD = <insert password here>
BOB_HOST = localhost
BOB_PORT = 12346

CARL_JID = <insert user here>@saros-con.imp.fu-berlin.de/Saros
CARL_PASSWORD = <insert password here>
CARL_HOST = localhost
CARL_PORT = 12347

DAVE_JID = <insert user here>@saros-con.imp.fu-berlin.de/Saros
DAVE_PASSWORD = <insert password here>
DAVE_HOST = localhost
DAVE_PORT = 12348
```

### Prepare dependencies

Eclipse uses the `META-INF/MANIFEST.MF` in order to resolve dependencies inside the launch config.
Therefore the current workaround is to execute the gradle task `generateLibAll` in order to generate a directory `lib` that contains the required dependencies.

### Run tests

1.  Start the **requiered launch configurations** which are located
    in the directory `test/resources/launch` by **right clicking the
    launch file** and chose **Run As > Saros\_STF\_\<name\>**. This
    will start a new Eclipse instance with the selected launch
    configuration.
    You only need to run the appropriate launch file(s). E.g. the
    test `AccountPreferenceTest.java` in the package
    `de.fu_berlin.inf.dpp.stf.test.account` only needs the tester
    *ALICE*. In this case you only need to start *Saros\_STF\_Alice*.
2.  **Right click** on the test you want to run and select **Run As > JUnit test**

## Intellij
We are currently working on an version of STF that work with the HTML-GUI and therefore with both IDEs, but for now it is not possible to run STF tests for Intellij.
However Intellij/Gradle supports to run multiple intellij instances from within intellij in order to test Saros by hand.

### Run multiple Intellij instances for testing
* Open the `Gradle` tool window
* Open the task directory `de.fu_berlin.inf.dpp.intellij > Tasks > intellij`
* Execute task `runIDE` for each IntelliJ IDEA instance you need.