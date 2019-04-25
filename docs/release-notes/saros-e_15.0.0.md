---
title: Saros Release Notes
---

The Saros Team is happy to announce Saros for Eclipse 15.0.0 and Saros for IntelliJ 0.1.0.

Since the last release of Saros for Eclipse we mostly restructured, refactored and reworked the project in more than 1000 commits. From now on we want to use a lot shorter release cycles in order to
provide fixes and features faster.

Saros for IntelliJ 0.1.0 is the first alpha release version of our IntelliJ version. Please, see the [Saros for IntelliJ release notes](saros-i_0.1.0.md) before you use the new plug-in.

## Upgrade Instruction
We changed the structure of our configuration files and the location of our update-site.
Therefore, if you used the previuous Saros version, please:
* Remove your saros directory at `$HOME/.saros`
* Change the update site to: <UPDATE-SITE>

## Where is the whiteboard?
You may know the Saros whiteboard from the previous release.
Due to stability issues we did not released the Whiteboard feature of Saros, which allowed to share drawings between Saros users.
We plan to release a new whiteboard as soon as it works reliable.

## JDK requirements
Saros 15.0.0 requires JDK8+, since we switched to JDK8 internally.

## Feedback
If you find any issue with Saros, please report it in our [issue tracker](https://github.com/saros-project/saros/issues).
In case you are not sure whether you found a bug or simply want to talk about Saros, ask us at [Gitter](https://gitter.im/saros-project/saros/user).

