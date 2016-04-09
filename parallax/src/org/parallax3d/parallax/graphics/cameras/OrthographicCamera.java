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

package org.parallax3d.parallax.graphics.cameras;

import org.parallax3d.parallax.system.ThreejsObject;
//import org.parallax3d.parallax.backends.gwt.client.events.ViewportResizeEvent;

/**
 * Camera with orthographic projection
 * 
 * <pre>
 * {@code
 * OrthographicCamera camera = new OrthographicCamera( width / - 2, width / 2, height / 2, height / - 2, 1, 1000 ); 
 * 
 * // or, simple
 * 
 * OrthographicCamera camera = new OrthographicCamera( width, height, 1, 1000 ); 
 * }
 * </pre>
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.OrthographicCamera")
public class OrthographicCamera extends Camera implements HasNearFar
{

	protected double zoom = 1.0;
	protected double left;
	protected double right;
	protected double top;
	protected double bottom;

	protected double near = 0.1;
	protected double far = 2000.0;

	/**
	 * Orthographic Camera constructor.
	 *
	 * @param width  Camera frustum width plane.
	 * @param height Camera frustum height plane.
	 * @param near   Camera frustum near plane
	 * @param far    Camera frustum far plane.
	 */
	public OrthographicCamera(double width, double height, double near, double far)
	{
		this(width / -2.0, width / 2.0, height / 2.0, height / -2.0, near, far);
	}

	/**
	 * Orthographic Camera constructor.
	 *
	 * @param left   Camera frustum left plane.
	 * @param right  Camera frustum right plane.
	 * @param top    Camera frustum top plane.
	 * @param bottom Camera frustum bottom plane.
	 * @param near   Camera frustum near plane
	 * @param far    Camera frustum far plane.
	 */
	public OrthographicCamera(double left, double right, double top, double bottom, double near, double far)
	{
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;

		this.near = near;
		this.far = far;

		updateProjectionMatrix();
	}

	@Override
	public void onViewportResize(int width, int height)
	{
		setSize( width, height );
	}

	public void setSize(double width, double height)
	{
		this.left = width / -2.0;
		this.right = width / 2.0;
		this.top = height / 2.0;
		this.bottom = height / -2.0;

		updateProjectionMatrix();
	}

	/**
	 * Gets Camera frustum left plane.
	 */
	public double getLeft()
	{
		return left;
	}

	/**
	 * Sets Camera frustum left plane.
	 */
	public void setLeft(double left)
	{
		this.left = left;
	}

	/**
	 * Gets Camera frustum right plane.
	 */
	public double getRight()
	{
		return right;
	}

	/**
	 * Sets Camera frustum right plane.
	 */
	public void setRight(double right)
	{
		this.right = right;
	}

	/**
	 * Gets Camera frustum top plane.
	 */
	public double getTop()
	{
		return top;
	}

	/**
	 * Sets Camera frustum top plane.
	 */
	public void setTop(double top)
	{
		this.top = top;
	}

	/**
	 * Gets Camera frustum bottom plane.
	 */
	public double getBottom()
	{
		return bottom;
	}

	/**
	 * Sets Camera frustum bottom plane.
	 */
	public void setBottom(double bottom)
	{
		this.bottom = bottom;
	}

	/**
	 * Gets Camera frustum near plane.
	 */
	public double getNear()
	{
		return near;
	}

	/**
	 * Sets Camera frustum near plane.
	 */
	public void setNear(double near)
	{
		this.near = near;
	}

	/**
	 * Gets Camera frustum far plane.
	 */
	public double getFar()
	{
		return far;
	}

	/**
	 * Sets Camera frustum far plane.
	 */
	public void setFar(double far)
	{
		this.far = far;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	/**
	 * Updates the camera projection matrix.
	 * <p>
	 * Must be called after change of parameters.
	 */
	public void updateProjectionMatrix() {

		double dx = ( this.right - this.left ) / ( 2.0 * this.zoom );
		double dy = ( this.top - this.bottom ) / ( 2.0 * this.zoom );
		double cx = ( this.right + this.left ) / 2.0;
		double cy = ( this.top + this.bottom ) / 2.0;

		this.projectionMatrix.makeOrthographic( cx - dx, cx + dx, cy + dy, cy - dy, this.near, this.far );

	}

	public OrthographicCamera copy ( OrthographicCamera source ) {

		super.copy( source );

		this.left = source.left;
		this.right = source.right;
		this.top = source.top;
		this.bottom = source.bottom;
		this.near = source.near;
		this.far = source.far;

		this.zoom = source.zoom;

		return this;

	}

	public OrthographicCamera clone() {

		return new OrthographicCamera( 0, 0, 0, 0 ).copy( this );

	}

	public String toString()
	{
		return OrthographicCamera.class.getSimpleName()
				+ " {left: " + this.left
				+ ", right: " + this.right
				+ ", top: " + this.top
				+ ", bottom: " + this.bottom + " }";
	}
}
