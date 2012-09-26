/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.plugin.lensflare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLTexture;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.GLEnum;
import thothbot.parallax.core.client.gl2.enums.TextureUnit;
import thothbot.parallax.core.client.renderers.Plugin;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.shaders.Attribute;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.FastMap;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.scenes.Scene;
import thothbot.parallax.plugin.lensflare.shaders.LensFlareShader;
import thothbot.parallax.plugin.lensflare.shaders.LensFlareVertexTextureShader;

import com.google.gwt.core.client.GWT;

public final class LensFlarePlugin extends Plugin
{

	public class LensFlareGeometry 
	{
		Float32Array vertices;
		Uint16Array faces;
		
		WebGLBuffer vertexBuffer;
		WebGLBuffer elementBuffer;
		
		WebGLTexture tempTexture;
		WebGLTexture occlusionTexture;
		
		LensFlareShader shader;
		
		boolean hasVertexTexture;
		boolean attributesEnabled;
	}

	private LensFlareGeometry lensFlare;
	private List<LensFlare> objects;
	
	public LensFlarePlugin(WebGLRenderer renderer, Scene scene) 
	{
		super(renderer, scene);
		
		this.lensFlare = new LensFlareGeometry();
		
		WebGLRenderingContext gl = getRenderer().getGL();

		lensFlare.vertices = Float32Array.create( 8 + 8 );
		lensFlare.faces = Uint16Array.create( 6 );

		int i = 0;
		lensFlare.vertices.set( i++, -1); lensFlare.vertices.set( i++, -1);	// vertex
		lensFlare.vertices.set( i++, 0);  lensFlare.vertices.set( i++, 0);	// uv... etc.

		lensFlare.vertices.set( i++, 1);  lensFlare.vertices.set( i++, -1);
		lensFlare.vertices.set( i++, 1);  lensFlare.vertices.set( i++, 0);

		lensFlare.vertices.set( i++, 1);  lensFlare.vertices.set( i++, 1);
		lensFlare.vertices.set( i++, 1);  lensFlare.vertices.set( i++, 1);

		lensFlare.vertices.set( i++, -1); lensFlare.vertices.set( i++, 1);
		lensFlare.vertices.set( i++, 0);  lensFlare.vertices.set( i++, 1);

		i = 0;
		lensFlare.faces.set( i++, 0); lensFlare.faces.set( i++, 1); lensFlare.faces.set( i++, 2);
		lensFlare.faces.set( i++, 0); lensFlare.faces.set( i++, 2); lensFlare.faces.set( i++, 3);

		// buffers

		lensFlare.vertexBuffer     = gl.createBuffer();
		lensFlare.elementBuffer    = gl.createBuffer();

		gl.bindBuffer( BufferTarget.ARRAY_BUFFER, lensFlare.vertexBuffer );
		gl.bufferData( GLEnum.ARRAY_BUFFER.getValue(), lensFlare.vertices, GLEnum.STATIC_DRAW.getValue() );

		gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, lensFlare.elementBuffer );
		gl.bufferData( GLEnum.ELEMENT_ARRAY_BUFFER.getValue(), lensFlare.faces, GLEnum.STATIC_DRAW.getValue() );

		// textures

		lensFlare.tempTexture      = gl.createTexture();
		lensFlare.occlusionTexture = gl.createTexture();

		gl.bindTexture( GLEnum.TEXTURE_2D.getValue(), lensFlare.tempTexture );
		gl.texImage2D( GLEnum.TEXTURE_2D.getValue(), 0, GLEnum.RGB.getValue(), 16, 16, 0, GLEnum.RGB.getValue(), GLEnum.UNSIGNED_BYTE.getValue(), null );
		gl.texParameteri( GLEnum.TEXTURE_2D.getValue(), GLEnum.TEXTURE_WRAP_S.getValue(), GLEnum.CLAMP_TO_EDGE.getValue() );
		gl.texParameteri( GLEnum.TEXTURE_2D.getValue(), GLEnum.TEXTURE_WRAP_T.getValue(), GLEnum.CLAMP_TO_EDGE.getValue() );
		gl.texParameteri( GLEnum.TEXTURE_2D.getValue(), GLEnum.TEXTURE_MAG_FILTER.getValue(), GLEnum.NEAREST.getValue() );
		gl.texParameteri( GLEnum.TEXTURE_2D.getValue(), GLEnum.TEXTURE_MIN_FILTER.getValue(), GLEnum.NEAREST.getValue() );

		gl.bindTexture( GLEnum.TEXTURE_2D.getValue(), lensFlare.occlusionTexture );
		gl.texImage2D( GLEnum.TEXTURE_2D.getValue(), 0, GLEnum.RGBA.getValue(), 16, 16, 0, GLEnum.RGBA.getValue(), GLEnum.UNSIGNED_BYTE.getValue(), null );
		gl.texParameteri( GLEnum.TEXTURE_2D.getValue(), GLEnum.TEXTURE_WRAP_S.getValue(), GLEnum.CLAMP_TO_EDGE.getValue() );
		gl.texParameteri( GLEnum.TEXTURE_2D.getValue(), GLEnum.TEXTURE_WRAP_T.getValue(), GLEnum.CLAMP_TO_EDGE.getValue() );
		gl.texParameteri( GLEnum.TEXTURE_2D.getValue(), GLEnum.TEXTURE_MAG_FILTER.getValue(), GLEnum.NEAREST.getValue() );
		gl.texParameteri( GLEnum.TEXTURE_2D.getValue(), GLEnum.TEXTURE_MIN_FILTER.getValue(), GLEnum.NEAREST.getValue() );

		if ( gl.getParameteri( GLEnum.MAX_VERTEX_TEXTURE_IMAGE_UNITS.getValue() ) <= 0 ) 
		{
			lensFlare.hasVertexTexture = false;
			lensFlare.shader = new LensFlareShader();
		} 
		else 
		{
			lensFlare.hasVertexTexture = true;
			lensFlare.shader = new LensFlareVertexTextureShader();
		}

		Map<String, Attribute> attributes = GWT.isScript() ? 
				new FastMap<Attribute>() : new HashMap<String, Attribute>();
		attributes.put("position", new Attribute(Attribute.TYPE.V3, null));
		attributes.put("uv", new Attribute(Attribute.TYPE.V3, null));
		lensFlare.shader.setAttributes(attributes);
		lensFlare.shader.buildProgram(gl);
	}
	
	@Override
	public Plugin.TYPE getType()
	{
		return Plugin.TYPE.POST_RENDER;
	}

	public List<LensFlare> getObjects() 
	{
		if(this.objects == null)
		{
			this.objects = (List<LensFlare>)(ArrayList)getScene().getChildrenByClass(LensFlare.class, true);
		}
		
		return (List<LensFlare>)(ArrayList)this.objects;
	}

	/**
	 * Render lens flares
	 * Method: renders 16x16 0xff00ff-colored points scattered over the light source area,
	 *         reads these back and calculates occlusion.
	 *         Then _lensFlare.update_lensFlares() is called to re-position and
	 *         update transparency of flares. Then they are rendered.
	 *
	 */
	@Override
	public void render( Camera camera, int viewportWidth, int viewportHeight) 
	{
		List<LensFlare> flares = getObjects();
		int nFlares = flares.size();

		if ( nFlares == 0 ) return;

		WebGLRenderingContext gl = getRenderer().getGL();

		Vector3 tempPosition = new Vector3();

		double invAspect = (double)viewportHeight / viewportWidth;
		double halfViewportWidth = viewportWidth * 0.5;
		double halfViewportHeight = viewportHeight * 0.5;

		double size = 16.0 / viewportHeight;
		Vector2 scale = new Vector2( size * invAspect, size );

		Vector3 screenPosition = new Vector3( 1, 1, 0 );
		Vector2 screenPositionPixels = new Vector2( 1, 1 );

		Map<String, Uniform> uniforms = this.lensFlare.shader.getUniforms();
		Map<String, Integer> attributesLocation = this.lensFlare.shader.getAttributesLocations();

		// set _lensFlare program and reset blending

		gl.useProgram( lensFlare.shader.getProgram() );

		if ( ! lensFlare.attributesEnabled ) 
		{
			gl.enableVertexAttribArray( attributesLocation.get("position") );
			gl.enableVertexAttribArray( attributesLocation.get("uv") );

			lensFlare.attributesEnabled = true;
		}

		// loop through all lens flares to update their occlusion and positions
		// setup gl and common used attribs/unforms

		gl.uniform1i( uniforms.get("occlusionMap").getLocation(), 0 );
		gl.uniform1i( uniforms.get("map").getLocation(), 1 );

		gl.bindBuffer( BufferTarget.ARRAY_BUFFER, lensFlare.vertexBuffer );
		gl.vertexAttribPointer( attributesLocation.get("position"), 2, GLEnum.FLOAT.getValue(), false, 2 * 8, 0 );
		gl.vertexAttribPointer( attributesLocation.get("uv"), 2, GLEnum.FLOAT.getValue(), false, 2 * 8, 8 );

		gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, lensFlare.elementBuffer );

		gl.disable( GLEnum.CULL_FACE.getValue() );
		gl.depthMask( false );

		for ( int i = 0; i < nFlares; i ++ ) 
		{
			size = 16.0 / viewportHeight;
			scale.set( size * invAspect, size );

			// calc object screen position

			LensFlare flare = flares.get( i );

			tempPosition.set( 
					flare.getMatrixWorld().getArray().get(12), 
					flare.getMatrixWorld().getArray().get(13), 
					flare.getMatrixWorld().getArray().get(14) );

			camera.getMatrixWorldInverse().multiplyVector3( tempPosition );
			camera.getProjectionMatrix().multiplyVector3( tempPosition );

			// setup arrays for gl programs

			screenPosition.copy( tempPosition );

			screenPositionPixels.setX( screenPosition.getX() * halfViewportWidth + halfViewportWidth);
			screenPositionPixels.setY( screenPosition.getY() * halfViewportHeight + halfViewportHeight);

			// screen cull

			if ( lensFlare.hasVertexTexture || (
					screenPositionPixels.getX() > 0 &&
					screenPositionPixels.getX() < viewportWidth &&
					screenPositionPixels.getY() > 0 &&
					screenPositionPixels.getY() < viewportHeight ) 
			) {

				// save current RGB to temp texture

				gl.activeTexture( TextureUnit.TEXTURE1 );
				gl.bindTexture( GLEnum.TEXTURE_2D.getValue(), lensFlare.tempTexture );
				gl.copyTexImage2D( GLEnum.TEXTURE_2D.getValue(), 0, GLEnum.RGB.getValue(), (int)screenPositionPixels.getX() - 8, (int)screenPositionPixels.getY() - 8, 16, 16, 0 );

				// render pink quad

				gl.uniform1i( uniforms.get("renderType").getLocation(), 0 );
				gl.uniform2f( uniforms.get("scale").getLocation(), scale.getX(), scale.getY() );
				gl.uniform3f( uniforms.get("screenPosition").getLocation(), screenPosition.getX(), screenPosition.getY(), screenPosition.getZ() );

				gl.disable( GLEnum.BLEND.getValue() );
				gl.enable( GLEnum.DEPTH_TEST.getValue() );

				gl.drawElements( GLEnum.TRIANGLES.getValue(), 6, GLEnum.UNSIGNED_SHORT.getValue(), 0 );

				// copy result to occlusionMap

				gl.activeTexture( TextureUnit.TEXTURE0 );
				gl.bindTexture( GLEnum.TEXTURE_2D.getValue(), lensFlare.occlusionTexture );
				gl.copyTexImage2D( GLEnum.TEXTURE_2D.getValue(), 0, GLEnum.RGBA.getValue(), (int)screenPositionPixels.getX() - 8, (int)screenPositionPixels.getY() - 8, 16, 16, 0 );

				// restore graphics

				gl.uniform1i( uniforms.get("renderType").getLocation(), 1 );
				gl.disable( GLEnum.DEPTH_TEST.getValue() );

				gl.activeTexture( TextureUnit.TEXTURE1 );
				gl.bindTexture( GLEnum.TEXTURE_2D.getValue(), lensFlare.tempTexture );
				gl.drawElements( GLEnum.TRIANGLES.getValue(), 6, GLEnum.UNSIGNED_SHORT.getValue(), 0 );

				// update object positions

				flare.getPositionScreen().copy( screenPosition );

				flare.getUpdateCallback().update();

				// render flares

				gl.uniform1i( uniforms.get("renderType").getLocation(), 2 );
				gl.enable( GLEnum.BLEND.getValue() );

				for ( int j = 0, jl = flare.getLensFlares().size(); j < jl; j ++ ) 
				{
					LensFlare.LensSprite sprite = flare.getLensFlares().get( j );

					if ( sprite.opacity > 0.001 && sprite.scale > 0.001 ) 
					{
						screenPosition.setX( sprite.x );
						screenPosition.setY( sprite.y );
						screenPosition.setZ( sprite.z );

						size = sprite.size * sprite.scale / viewportHeight;

						scale.setX( size * invAspect );
						scale.setY( size );

						gl.uniform3f( uniforms.get("screenPosition").getLocation(), screenPosition.getX(), screenPosition.getY(), screenPosition.getZ() );
						gl.uniform2f( uniforms.get("scale").getLocation(), scale.getX(), scale.getY() );
						gl.uniform1f( uniforms.get("rotation").getLocation(), sprite.rotation );

						gl.uniform1f( uniforms.get("opacity").getLocation(), sprite.opacity );
						gl.uniform3f( uniforms.get("color").getLocation(), sprite.color.getR(), sprite.color.getG(), sprite.color.getB() );

//						renderer.setBlending( sprite.blending, sprite.blendEquation, sprite.blendSrc, sprite.blendDst );
						getRenderer().setBlending( sprite.blending );
						getRenderer().setTexture( sprite.texture, 1 );

						gl.drawElements( GLEnum.TRIANGLES.getValue(), 6, GLEnum.UNSIGNED_SHORT.getValue(), 0 );
					}
				}
			}
		}

		// restore gl

		gl.enable( GLEnum.CULL_FACE.getValue() );
		gl.enable( GLEnum.DEPTH_TEST.getValue() );
		gl.depthMask( true );
	}
}
