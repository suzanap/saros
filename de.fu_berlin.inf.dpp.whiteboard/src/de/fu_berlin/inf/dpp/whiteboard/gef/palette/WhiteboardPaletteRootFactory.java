package de.fu_berlin.inf.dpp.whiteboard.gef.palette;

import org.eclipse.gef.palette.PaletteRoot;

/**
 * Factory to create a {@link WhiteboardPaletteRoot}
 * 
 */
public class WhiteboardPaletteRootFactory {

    private WhiteboardPaletteRootFactory() {
        // private constructor to hide the implicit public one
    }

    /**
     * Creates a {@link WhiteboardPaletteRoot PaletteRoot}
     * 
     * @return a new {@link WhiteboardPaletteRoot}
     * 
     */
    public static PaletteRoot createPaletteRoot() {
        return new WhiteboardPaletteRoot();
    }

}
