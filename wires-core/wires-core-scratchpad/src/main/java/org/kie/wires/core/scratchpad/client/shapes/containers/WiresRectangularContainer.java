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
package org.kie.wires.core.scratchpad.client.shapes.containers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.types.Point2D;
import org.kie.wires.core.api.containers.WiresContainer;
import org.kie.wires.core.api.controlpoints.ControlPoint;
import org.kie.wires.core.api.controlpoints.ControlPointMoveHandler;
import org.kie.wires.core.api.shapes.RequiresShapesManager;
import org.kie.wires.core.api.shapes.ShapesManager;
import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.controlpoints.DefaultControlPoint;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.client.util.UUID;
import org.uberfire.commons.data.Pair;

public class WiresRectangularContainer extends WiresBaseDynamicShape implements WiresContainer,
                                                                                RequiresShapesManager {

    private static final int BOUNDARY_SIZE = 10;

    private final Rectangle rectangle;
    private final Rectangle bounding;
    private final String rectangleFillColour;
    private final String rectangleStokeColour;

    private final ControlPoint controlPoint1;
    private final ControlPoint controlPoint2;
    private final ControlPoint controlPoint3;
    private final ControlPoint controlPoint4;

    private List<WiresBaseShape> children = new ArrayList<WiresBaseShape>();
    private List<Pair<WiresBaseShape, Point2D>> dragStartLocations = new ArrayList<Pair<WiresBaseShape, Point2D>>();

    private ShapesManager shapesManager;

    public WiresRectangularContainer( final Rectangle shape ) {
        this( shape,
              shape.getOffset().getX(),
              shape.getOffset().getY(),
              shape.getOffset().getX() + shape.getWidth(),
              shape.getOffset().getY() + shape.getHeight() );
    }

    public WiresRectangularContainer( final Rectangle shape,
                                      final double x1,
                                      final double y1,
                                      final double x2,
                                      final double y2 ) {
        final double width = Math.abs( x2 - x1 );
        final double height = Math.abs( y2 - y1 );

        this.id = UUID.uuid();
        this.rectangle = shape;
        this.rectangleFillColour = shape.getFillColor();
        this.rectangleStokeColour = shape.getStrokeColor();

        rectangle.setX( x1 );
        rectangle.setY( y1 );
        rectangle.setOffset( 0,
                             0 );

        bounding = new Rectangle( width + BOUNDARY_SIZE,
                                  height + BOUNDARY_SIZE,
                                  rectangle.getCornerRadius() );
        bounding.setX( x1 - ( BOUNDARY_SIZE / 2 ) );
        bounding.setY( y1 - ( BOUNDARY_SIZE / 2 ) );
        bounding.setStrokeWidth( BOUNDARY_SIZE );
        bounding.setAlpha( 0.1 );

        add( rectangle );

        magnets.clear();
        controlPoints.clear();
        final double px1 = rectangle.getX();
        final double py1 = rectangle.getY();
        controlPoint1 = new DefaultControlPoint( px1,
                                                 py1,
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( final double x,
                                                                         final double y ) {
                                                         controlPoint2.setY( controlPoint1.getY() );
                                                         controlPoint3.setX( controlPoint1.getX() );
                                                         rectangle.setX( x );
                                                         rectangle.setY( y );
                                                         rectangle.setWidth( controlPoint2.getX() - controlPoint1.getX() );
                                                         rectangle.setHeight( controlPoint3.getY() - controlPoint1.getY() );
                                                         bounding.setX( x - ( BOUNDARY_SIZE / 2 ) );
                                                         bounding.setY( y - ( BOUNDARY_SIZE / 2 ) );
                                                         bounding.setWidth( rectangle.getWidth() + BOUNDARY_SIZE );
                                                         bounding.setHeight( rectangle.getHeight() + BOUNDARY_SIZE );
                                                     }
                                                 }
        );

        final double px2 = rectangle.getX() + rectangle.getWidth();
        final double py2 = rectangle.getY();
        controlPoint2 = new DefaultControlPoint( px2,
                                                 py2,
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( double x,
                                                                         double y ) {
                                                         controlPoint1.setY( controlPoint2.getY() );
                                                         controlPoint4.setX( controlPoint2.getX() );
                                                         rectangle.setY( y );
                                                         rectangle.setWidth( controlPoint2.getX() - controlPoint1.getX() );
                                                         rectangle.setHeight( controlPoint3.getY() - controlPoint1.getY() );
                                                         bounding.setY( y - ( BOUNDARY_SIZE / 2 ) );
                                                         bounding.setWidth( rectangle.getWidth() + BOUNDARY_SIZE );
                                                         bounding.setHeight( rectangle.getHeight() + BOUNDARY_SIZE );
                                                     }
                                                 }
        );

        final double px3 = rectangle.getX();
        final double py3 = rectangle.getY() + rectangle.getHeight();
        controlPoint3 = new DefaultControlPoint( px3,
                                                 py3,
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( double x,
                                                                         double y ) {
                                                         controlPoint1.setX( controlPoint3.getX() );
                                                         controlPoint4.setY( controlPoint3.getY() );
                                                         rectangle.setX( x );
                                                         rectangle.setWidth( controlPoint2.getX() - controlPoint1.getX() );
                                                         rectangle.setHeight( controlPoint3.getY() - controlPoint1.getY() );
                                                         bounding.setX( x - ( BOUNDARY_SIZE / 2 ) );
                                                         bounding.setWidth( rectangle.getWidth() + BOUNDARY_SIZE );
                                                         bounding.setHeight( rectangle.getHeight() + BOUNDARY_SIZE );
                                                     }
                                                 }
        );

        final double px4 = rectangle.getX() + rectangle.getWidth();
        final double py4 = rectangle.getY() + rectangle.getHeight();
        controlPoint4 = new DefaultControlPoint( px4,
                                                 py4,
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( double x,
                                                                         double y ) {
                                                         controlPoint2.setX( controlPoint4.getX() );
                                                         controlPoint3.setY( controlPoint4.getY() );
                                                         rectangle.setWidth( controlPoint2.getX() - controlPoint1.getX() );
                                                         rectangle.setHeight( controlPoint3.getY() - controlPoint1.getY() );
                                                         bounding.setWidth( rectangle.getWidth() + BOUNDARY_SIZE );
                                                         bounding.setHeight( rectangle.getHeight() + BOUNDARY_SIZE );
                                                     }
                                                 }
        );
        addControlPoint( controlPoint1 );
        addControlPoint( controlPoint2 );
        addControlPoint( controlPoint3 );
        addControlPoint( controlPoint4 );
    }

    @Override
    public void setShapesManager( final ShapesManager shapesManager ) {
        this.shapesManager = shapesManager;
    }

    @Override
    public void setSelected( final boolean isSelected ) {
        if ( isSelected ) {
            add( bounding );
        } else {
            remove( bounding );
        }
    }

    @Override
    public void init( final double cx,
                      final double cy ) {
        super.init( cx,
                    cy );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            @Override
            public void onNodeDragStart( final NodeDragStartEvent nodeDragStartEvent ) {
                dragStartLocations.clear();
                for ( WiresBaseShape shape : children ) {
                    dragStartLocations.add( new Pair( shape,
                                                      new Point2D( shape.getLocation().getX(),
                                                                   shape.getLocation().getY() ) ) );
                }
            }
        } );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                final double deltaX = nodeDragMoveEvent.getDragContext().getDx();
                final double deltaY = nodeDragMoveEvent.getDragContext().getDy();
                final Point2D delta = new Point2D( deltaX,
                                                   deltaY );
                for ( Pair<WiresBaseShape, Point2D> dragStartLocation : dragStartLocations ) {
                    dragStartLocation.getK1().setLocation( dragStartLocation.getK2().plus( delta ) );
                }
                getLayer().draw();
            }
        } );
    }

    @Override
    public boolean contains( final double cx,
                             final double cy ) {
        final double _x = cx - getX();
        final double _y = cy - getY();
        if ( _x < rectangle.getX() ) {
            return false;
        } else if ( _x > rectangle.getX() + rectangle.getWidth() ) {
            return false;
        } else if ( _y < rectangle.getY() ) {
            return false;
        } else if ( _y > rectangle.getY() + rectangle.getHeight() ) {
            return false;
        }
        return true;
    }

    @Override
    public void attachShape( final WiresBaseShape shape ) {
        children.add( shape );
    }

    @Override
    public void detachShape( final WiresBaseShape shape ) {
        children.remove( shape );
    }

    @Override
    public List<WiresBaseShape> getContainedShapes() {
        return Collections.unmodifiableList( children );
    }

    @Override
    public void setHover( final boolean isHover ) {
        if ( isHover ) {
            rectangle.setFillColor( ShapesUtils.RGB_FILL_HOVER_CONTAINER );
            rectangle.setStrokeColor( ShapesUtils.RGB_STROKE_HOVER_CONTAINER );
        } else {
            rectangle.setFillColor( rectangleFillColour );
            rectangle.setStrokeColor( rectangleStokeColour );
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        for ( WiresBaseShape shape : children ) {
            shapesManager.forceDeleteShape( shape );
        }
    }

    @Override
    public String toString() {
        return "WiresRectangularContainer{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + "}";
    }

}