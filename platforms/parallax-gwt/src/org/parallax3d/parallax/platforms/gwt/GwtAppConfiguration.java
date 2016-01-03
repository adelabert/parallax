/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 *
 * This file is part of Parallax project.
 *
 * Parallax is free software: you can redistribute it and/or modify it
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 *
 * Parallax is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution
 * 3.0 Unported License. for more details.
 *
 * You should have received a copy of the the Creative Commons Attribution
 * 3.0 Unported License along with Parallax.
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package org.parallax3d.parallax.platforms.gwt;

public class GwtAppConfiguration {

    /**
     * whether to use a stencil buffer
     */
    public boolean stencil = false;

    /**
     * whether to enable antialiasing
     */
    public boolean antialiasing = false;

    /**
     * preserve the back buffer, needed if you fetch a screenshot via canvas#toDataUrl, may have performance impact
     */
    public boolean preserveDrawingBuffer = false;

    /**
     * whether to include an alpha channel in the color buffer to combine the color buffer with the rest of the
     * webpage effectively allows transparent backgrounds in GWT, at a performance cost.
     */
    public boolean alpha = false;

    /**
     * whether to use premultipliedalpha, may have performance impact
     */
    public boolean premultipliedAlpha = false;

}