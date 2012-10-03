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

package thothbot.parallax.core.shared.curves;

import thothbot.parallax.core.shared.core.Vector2;

public final class EllipseCurve extends Curve 
{

	private double aX;
	private double aY;

	private double xRadius;
	private double yRadius;

	private double aStartAngle;
	private double aEndAngle;

	private boolean aClockwise;

	public EllipseCurve( double aX, double aY, double xRadius, double yRadius,
			double aStartAngle, double aEndAngle, boolean aClockwise ) 
	{
		this.aX = aX;
		this.aY = aY;

		this.xRadius = xRadius;
		this.yRadius = yRadius;

		this.aStartAngle = aStartAngle;
		this.aEndAngle = aEndAngle;

		this.aClockwise = aClockwise;
	}

	@Override
	public Vector2 getPoint(double t)
	{
		double deltaAngle = this.aEndAngle - this.aStartAngle;

		if ( !this.aClockwise ) 
		{
			t = 1 - t;
		}

		double angle = this.aStartAngle + t * deltaAngle;

		double tx = this.aX + this.xRadius * Math.cos( angle );
		double ty = this.aY + this.yRadius * Math.sin( angle );

		return new Vector2( tx, ty );
	}
}
