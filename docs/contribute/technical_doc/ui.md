---
title: Activities
---

# {{ page.title }}
{:.no_toc}

{% include toc.html %}

## Overview of the UI parts

![](http://saros-build.imp.fu-berlin.de/img/technical_doc/ui.svg)

## Widgets

The widgets package contains all custom UI elements that are not part of
the Standard Widget Toolkit
([SWT](http://en.wikipedia.org/wiki/Standard_Widget_Toolkit)). This
covers a wide range from rounded composites to fully functional chat
controls.

It is highly recommended for you to import the Saros Widget Gallery
project (de.fu\_berlin.inf.dpp.ui.widgetGallery) from your Git checkout
in order to get an overview of the various applications of Saros' custom
widgets.

You will find more information on [Usability
Best Practices](../best-practices.md).

## Dialogs

The dialog package contains some dialog classes that will pop up to
inform the user like warning messages or the feedback dialog.

## Viewers and Models

JFace Viewers represent our domain model, namely the

-   buddy list,
-   an integrated view of the buddy list and the session, and
-   the workspace

The actual composites that contain these viewers are located in
the widgets.viewer package. The model package contains the wrapped
domain objects in order to be displayed by the viewers. They encapsulate
the knowledge on what they should look like (especially the caption and
the icon). The most important domain object representatives are
the RosterEntryElement and the UserElement.

## Wizards

A wizard is a collection of several wizard pages and the logic of what
to do if the wizard is finished or canceled. The wizard package itself
contains all wizards whereas the sub package pages contains all wizard
pages. Examples are wizards for creating a new XMPP Account, for adding
buddies to the roster, for the invitation, for sharing projects, and
many more.

Note: WizardUtils contains convenient methods for a simple and
consistent start of wizards.

## Commands and Menus

A command is an abstraction of an action since it allows for multiple
implementations depending on the content (e.g. copy is a command as
multiple implementations exist depending on the currently opened
editor). Another advantage is that commands can be embedded at the same
time in different menus, context menus and toolbars.

Commands, commandHandlers and menus are declared in the plugin.xml file
as the extension of the extension
points org.eclipse.ui.commands, org.eclipse.ui.handlers,
and org.eclipse.ui.menus.

The commandHandlers contain the implementations of commands that can be
triggered by the user.
