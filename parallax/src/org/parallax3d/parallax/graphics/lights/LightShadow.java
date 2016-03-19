/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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

import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.LightShadow")
public class LightShadow {

    Camera camera;

    double bias = 0.;
    double radius = 1.;

    Vector2 mapSize = new Vector2( 512, 512 );

    RenderTargetTexture map;
    Matrix4 matrix = new Matrix4();

    public LightShadow(Camera camera) {
        this.camera = camera;
    }

    public LightShadow copy( LightShadow source ) {

        this.camera = source.camera.clone();

        this.bias = source.bias;
        this.radius = source.radius;

        this.mapSize.copy( source.mapSize );

        return this;
    }

    public LightShadow clone() {

        return new LightShadow( this.camera ).copy( this );

    }
}
