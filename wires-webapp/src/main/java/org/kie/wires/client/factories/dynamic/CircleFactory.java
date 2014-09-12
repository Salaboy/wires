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
package org.kie.wires.client.factories.dynamic;

import javax.enterprise.context.ApplicationScoped;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.client.factories.categories.ShapeCategory;
import org.kie.wires.core.api.categories.Category;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.shapes.dynamic.WiresCircle;
import org.kie.wires.core.client.util.ShapesUtils;

@ApplicationScoped
public class CircleFactory implements ShapeFactory<Circle> {

    private static final String DESCRIPTION = "Circle";

    private static final int SHAPE_RADIUS = 25;

    @Override
    public ShapeGlyph<Circle> getGlyph() {
        final Circle circle = new Circle( SHAPE_RADIUS );
        setAttributes( circle );

        return new ShapeGlyph<Circle>() {
            @Override
            public Shape<Circle> getShape() {
                return circle;
            }

            @Override
            public double getWidth() {
                return ( SHAPE_RADIUS + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }

            @Override
            public double getHeight() {
                return ( SHAPE_RADIUS + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }
        };
    }

    @Override
    public String getShapeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Category getCategory() {
        return ShapeCategory.CATEGORY;
    }

    @Override
    public ShapeDragProxy<Circle> getDragProxy( final ShapeDragProxyCallback callback ) {
        final Circle proxy = new Circle( SHAPE_RADIUS );
        setAttributes( proxy );

        return new ShapeDragProxy<Circle>() {
            @Override
            public Shape<Circle> getDragShape() {
                return proxy;
            }

            @Override
            public void onDragEnd( final double x,
                                   final double y ) {
                callback.callback( x,
                                   y );
            }

            @Override
            public int getWidth() {
                return ( SHAPE_RADIUS + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }

            @Override
            public int getHeight() {
                return ( SHAPE_RADIUS + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }

        };
    }

    @Override
    public WiresBaseShape getShape() {
        return new WiresCircle( SHAPE_RADIUS );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof WiresCircle;
    }

    private void setAttributes( final Circle circle ) {
        circle.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( "#00ff00" )
                .setDraggable( false );
    }

}
