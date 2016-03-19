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

package org.parallax3d.parallax.graphics.lights;

import org.parallax3d.parallax.system.ThreejsObject;

/**
 * This light's color gets applied to all the objects in the scene globally.
 * 
 * <pre>
 * {@code
 * AmbientLight light = new AmbientLight( 0xff0000 ); 
 * getScene().add( light );
 * }
 * </pre>
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.AmbientLight")
public final class AmbientLight extends Light
{
	public AmbientLight() {

	}

	public AmbientLight(int hex) {
		super(hex);
	}

	public AmbientLight(int hex, double intensity) {
		super(hex, intensity);
	}

	public AmbientLight clone() {

		return (AmbientLight) new AmbientLight().copy(this);

	}
}
