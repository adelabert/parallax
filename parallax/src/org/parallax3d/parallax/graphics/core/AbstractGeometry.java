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

package org.parallax3d.parallax.graphics.core;

import org.parallax3d.parallax.math.Box3;
import org.parallax3d.parallax.math.Sphere;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.AbstractPropertyObject;

public abstract class AbstractGeometry extends AbstractPropertyObject
{
	static int Counter = 0;

	int id = 0;

	String name = "";

	// Bounding box.		
	protected Box3 boundingBox = null;

	// Bounding sphere.
	protected Sphere boundingSphere = null;

	// update flags
	protected boolean verticesNeedUpdate = false;
	protected boolean elementsNeedUpdate = false;
	protected boolean uvsNeedUpdate = false;
	protected boolean normalsNeedUpdate = false;
	protected boolean colorsNeedUpdate = false;
	protected boolean lineDistancesNeedUpdate = false;
	protected boolean groupsNeedUpdate = false;

	public AbstractGeometry()
	{
		this.id = Counter++;
	}

	/**
	 * Name for this geometry. Default is an empty string.
	 *
	 * @return Name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set name for this geometry.
	 *
	 * @param name	Name of the geometry
	 */
	public void setName(String name) {
		this.name = name;
	}


	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the Unique number of this geometry instance
	 */
	public int getId() {
		return id;
	}

	public boolean isVerticesNeedUpdate() {
		return verticesNeedUpdate;
	}

	public void setVerticesNeedUpdate(boolean verticesNeedUpdate) {
		this.verticesNeedUpdate = verticesNeedUpdate;
	}

	public boolean isElementsNeedUpdate() {
		return elementsNeedUpdate;
	}

	public void setElementsNeedUpdate(boolean elementsNeedUpdate) {
		this.elementsNeedUpdate = elementsNeedUpdate;
	}

	public boolean isNormalsNeedUpdate() {
		return normalsNeedUpdate;
	}

	public void setNormalsNeedUpdate(boolean normalsNeedUpdate) {
		this.normalsNeedUpdate = normalsNeedUpdate;
	}

	public boolean isColorsNeedUpdate() {
		return colorsNeedUpdate;
	}

	public void setColorsNeedUpdate(boolean colorsNeedUpdate) {
		this.colorsNeedUpdate = colorsNeedUpdate;
	}

	public boolean isUvsNeedUpdate() {
		return uvsNeedUpdate;
	}

	public void setUvsNeedUpdate(boolean uvsNeedUpdate) {
		this.uvsNeedUpdate = uvsNeedUpdate;
	}

	public boolean isLineDistancesNeedUpdate() {
		return lineDistancesNeedUpdate;
	}

	public void setLineDistancesNeedUpdate(boolean lineDistancesNeedUpdate) {
		this.lineDistancesNeedUpdate = lineDistancesNeedUpdate;
	}

	public boolean isGroupsNeedUpdate() {
		return groupsNeedUpdate;
	}

	public void setGroupsNeedUpdate(boolean groupsNeedUpdate) {
		this.groupsNeedUpdate = groupsNeedUpdate;
	}

	public Box3 getBoundingBox() {
		return this.boundingBox;
	}

	public void setBoundingBox(Box3 boundingBox) {
		this.boundingBox = boundingBox;
	}

	public Sphere getBoundingSphere() {
		return this.boundingSphere;
	}

	public void setBoundingSphere(Sphere boundingSphere) {
		this.boundingSphere = boundingSphere;
	}

	public abstract void computeBoundingBox();

	public abstract void computeBoundingSphere();

	public abstract void computeVertexNormals();

	public abstract AbstractGeometry rotateX( double angle );

	public abstract AbstractGeometry rotateY( double angle );

	public abstract AbstractGeometry rotateZ( double angle );

	public abstract AbstractGeometry translate( double x, double y, double z );

	public abstract AbstractGeometry scale( double x, double y, double z );

	public abstract AbstractGeometry lookAt( Vector3 vector );

	public abstract Vector3 center();

	public String toString() {
		return getClass().getSimpleName();
	}

	public void dispose()
	{

	}
}

