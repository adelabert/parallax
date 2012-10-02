/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

package thothbot.parallax.plugin.scene.postprocessing.client;

import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.plugin.scene.postprocessing.client.shaders.FilmShader;

public class FilmPass extends Pass
{
	private ShaderMaterial material;
	private boolean isRenderToScreen = false;

	public FilmPass( double noiseIntensity, double scanlinesIntensity, int scanlinesCount, boolean grayscale ) 
	{
		this.material = new ShaderMaterial(new FilmShader());

		this.material.getShader().getUniforms().get("grayscale").setValue( grayscale );
		this.material.getShader().getUniforms().get("nIntensity").setValue( noiseIntensity );
		this.material.getShader().getUniforms().get("sIntensity").setValue( scanlinesIntensity );
		this.material.getShader().getUniforms().get("sCount").setValue( scanlinesCount );

		this.setEnabled(true);
		this.setNeedsSwap(true);
	}
	
	public boolean isRenderToScreen() {
		return this.isRenderToScreen;
	}
	
	public void setRenderToScreen(boolean isRenderToScreen) {
		this.isRenderToScreen = isRenderToScreen;
	}
	
	@Override
	public void render(Postprocessing effectCocmposer, double delta, boolean maskActive)
	{
		this.material.getShader().getUniforms().get("tDiffuse").setValue( effectCocmposer.getReadBuffer() );
		this.material.getShader().getUniforms().get( "time" ).setValue( (Double)this.material.getShader().getUniforms().get( "time" ).getValue() + delta );

		effectCocmposer.getQuad().setMaterial(this.material);

		if ( this.isRenderToScreen )
			effectCocmposer.getRenderer().render( 
					effectCocmposer.getScene(), effectCocmposer.getCamera() );

		else
			effectCocmposer.getRenderer().render( 
					effectCocmposer.getScene(), effectCocmposer.getCamera(), effectCocmposer.getWriteBuffer(), false );

	}
}