
package com.github.mikephil.charting.aaa.renderer;

import com.github.mikephil.charting.aaa.utils.ViewPort;

/**
 * Abstract baseclass of all Renderers.
 *
 */
public abstract class Renderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected ViewPort mViewPort;

    public Renderer(ViewPort viewPort) {
        this.mViewPort = viewPort;
    }
}
