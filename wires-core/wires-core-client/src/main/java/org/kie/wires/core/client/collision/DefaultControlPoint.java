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
package org.kie.wires.core.client.collision;

import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.shape.Circle;
import org.kie.wires.core.api.shapes.ControlPoint;
import org.kie.wires.core.api.shapes.ControlPointMoveHandler;
import org.kie.wires.core.client.util.UUID;

import static org.kie.wires.core.client.util.ShapesUtils.*;

/**
 * Default ControlPoint that informs the registered handler of changes to state
 */
public class DefaultControlPoint extends Circle implements ControlPoint<Circle> {

    private static final int RADIUS = 6;

    private final String id;
    private final ControlPointMoveHandler handler;

    public DefaultControlPoint( final double x,
                                final double y,
                                final ControlPointMoveHandler handler ) {
        super( RADIUS );
        this.id = UUID.uuid();
        this.handler = handler;

        setFillColor( CP_RGB_FILL_COLOR );
        setStrokeWidth( CP_RGB_STROKE_WIDTH_SHAPE );
        setX( x );
        setY( y );
        setDraggable( true );

        setupHandlers( handler );
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ControlPointMoveHandler getHandler() {
        return handler;
    }

    protected void setupHandlers( final ControlPointMoveHandler handler ) {
        addNodeDragMoveHandler( new NodeDragMoveHandler() {

            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                handler.onMove( DefaultControlPoint.this.getX(),
                                DefaultControlPoint.this.getY() );
                getLayer().draw();
            }
        } );
    }

    @Override
    public String toString() {
        return "DefaultControlPoint{" + "id=" + id + "}";
    }

}
