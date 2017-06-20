/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reactnativenavigation.views.utils;

/**
 * A class that holds information about a location and how the path should get to that
 * location from the previous path location (if any). Any PathPoint holds the information for
 * its location as well as the instructions on how to traverse the preceding interval from the
 * previous location.
 */
public class PathPoint {
    /**
     * The possible path operations that describe how to move from a preceding PathPoint to the
     * location described by this PathPoint.
     */
    public static final int MOVE = 0;
    public static final int LINE = 1;
    public static final int CURVE = 2;
    /**
     * The location of this PathPoint
     */
    public float mX;
    public float mY;

    /**
     * The first control point, if any, for a PathPoint of type CURVE
     */
    float mControl0X, mControl0Y;
    /**
     * The second control point, if any, for a PathPoint of type CURVE
     */
    float mControl1X, mControl1Y;
    /**
     * The motion described by the path to get from the previous PathPoint in an AnimatorPath
     * to the location of this PathPoint. This can be one of MOVE, LINE, or CURVE.
     */
    int mOperation;
    /**
     * Line/Move constructor
     */
    private PathPoint(int operation, float x, float y) {
        mOperation = operation;
        mX = x;
        mY = y;
    }
    /**
     * Curve constructor
     */
    private PathPoint(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
        mControl0X = c0X;
        mControl0Y = c0Y;
        mControl1X = c1X;
        mControl1Y = c1Y;
        mX = x;
        mY = y;
        mOperation = CURVE;
    }
    /**
     * Constructs and returns a PathPoint object that describes a line to the given xy location.
     */
    public static PathPoint lineTo(float x, float y) {
        return new PathPoint(LINE, x, y);
    }
    /**
     * Constructs and returns a PathPoint object that describes a curve to the given xy location
     * with the control points at c0 and c1.
     */
    public static PathPoint curveTo(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
        return new PathPoint(c0X,  c0Y, c1X, c1Y, x, y);
    }

    /**
     * Constructs and returns a PathPoint object that describes a discontinuous move to the given
     * xy location.
     */
    public static PathPoint moveTo(float x, float y) {
        return new PathPoint(MOVE, x, y);
    }
}

