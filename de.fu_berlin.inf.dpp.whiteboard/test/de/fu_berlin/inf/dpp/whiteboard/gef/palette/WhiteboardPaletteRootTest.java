package de.fu_berlin.inf.dpp.whiteboard.gef.palette;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.ToolEntry;
import org.junit.BeforeClass;
import org.junit.Test;

public class WhiteboardPaletteRootTest {

    private static WhiteboardPaletteRoot root;
    private static final String PENCIL = "Pencil";
    private static final String RECTANGLE = "Rectangle";

    @BeforeClass
    public static void setup() {

        root = (WhiteboardPaletteRoot) WhiteboardPaletteRootFactory
            .createPaletteRoot();
    }

    @Test
    public void getToolTest() {

        PaletteEntry entry0 = new PaletteEntry(null, null, Boolean.TRUE);
        PaletteEntry entry1 = EasyMock.createNiceMock(ToolEntry.class);
        PaletteEntry entry2 = new PaletteEntry(null, null, Boolean.TRUE);
        PaletteEntry entry3 = EasyMock.createNiceMock(ToolEntry.class);

        EasyMock.expect(entry1.getLabel()).andStubReturn(PENCIL);
        EasyMock.expect(entry3.getLabel()).andStubReturn(RECTANGLE);
        EasyMock.expect(entry1.getType()).andStubReturn(Boolean.TRUE);
        EasyMock.expect(entry3.getType()).andStubReturn(Boolean.TRUE);

        EasyMock.replay(entry1, entry3);

        PaletteContainer container = new PaletteGroup("Group");

        container.add(entry2);
        container.add(entry3);

        root.add(entry0);
        root.add(entry1);
        root.add(container);

        ToolEntry pencilEntry = root.getTool(PENCIL);
        assertNotNull("pencilEntry should be not null", pencilEntry);
        assertEquals(PENCIL, pencilEntry.getLabel());

        ToolEntry rectangleEntry = root.getTool(RECTANGLE);
        assertNotNull("rectangleEntry should be not null", rectangleEntry);
        assertEquals(RECTANGLE, rectangleEntry.getLabel());

    }

    @Test
    public void getToolNegativTest() {
        ToolEntry emptyLabelEntry = root.getTool(null);
        assertNull("emptyLabelEntry should be null", emptyLabelEntry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDefaultColorNegativTest() {

        root.setDefaultColor(null);

    }

    @Test
    public void setDefaultColorPositivTest() {
        ToolEntry colorEntry = EasyMock.createMock(ToolEntry.class);

        root.setDefaultColor(colorEntry);

        assertEquals("Should be equals", colorEntry, root.getDefaultColor());
    }
}
