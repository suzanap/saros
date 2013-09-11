package de.fu_berlin.inf.dpp.whiteboard.gef.palette;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;

/**
 * Serves as the root for the palette model.<br>
 * The {@link WhiteboardPaletteRoot} is structured as a tree of labeled entries.
 * 
 */
public class WhiteboardPaletteRoot extends PaletteRoot {

    private ToolEntry defaultColor;

    /**
     * Sets the defaultColor.
     * 
     * @param color
     *            new Color
     * @throws IllegalArgumentException
     *             if color is {@code null}
     */
    public void setDefaultColor(ToolEntry color) {
        if (color == null) {
            throw new IllegalArgumentException("color must not be null");
        }
        defaultColor = color;
    }

    /**
     * Returns the defaultColor.
     * 
     * @return the {@link ToolEntry defaultColor}, if
     *         {@link #setDefaultColor(ToolEntry)} is not called first, it
     *         returns {@code null}.
     */
    public ToolEntry getDefaultColor() {
        return defaultColor;
    }

    /**
     * Searches the tree for an {@link ToolEntry entry}, that matches with the
     * given {@code label}.
     * 
     * @param label
     *            of the {@link ToolEntry}, which is searched.
     * @return a {@link ToolEntry}, that has the given {@code label} or
     *         {@code null}, if no {@link ToolEntry tool} was found with the
     *         {@code label} or {@code label equals null}.
     */
    public ToolEntry getTool(String label) {
        return findTool(this, label);
    }

    private ToolEntry findTool(PaletteEntry currentEntry, String label) {
        if (label == null)
            return null;
        if (currentEntry instanceof PaletteContainer) {
            for (Object child : ((PaletteContainer) currentEntry).getChildren()) {
                ToolEntry tool = findTool((PaletteEntry) child, label);
                if (tool != null)
                    return tool;
            }
        } else {
            if (currentEntry instanceof ToolEntry
                && label.equals(currentEntry.getLabel())) {
                return (ToolEntry) currentEntry;
            }
        }

        return null;
    }
}
