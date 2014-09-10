/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.wires.core.api.shapes;

import java.util.List;

public interface Magnet {

    static final int MAGNET_START = 0;
    static final int MAGNET_END = 1;

    static final int MAGNET_TOP = 2;
    static final int MAGNET_BOTTOM = 3;
    static final int MAGNET_RIGHT = 4;
    static final int MAGNET_LEFT = 5;

    void placeMagnetPoints();

    double getX();

    double getY();

    void setMagnetActive( final boolean active );

    void attachControlPoint( final ControlPoint controlPoint );

    List<ControlPoint> getAttachedControlPoints();

    String getId();

    HasMagnets getShape();

    int getType();

}
