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
package org.kie.wires.core.scratchpad.client.factories.connectors;

import javax.enterprise.context.ApplicationScoped;

import com.emitrom.lienzo.client.core.shape.BezierCurve;
import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCompleteCallback;
import org.kie.wires.core.api.factories.ShapeDragProxyPreviewCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.factories.categories.ConnectorCategory;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.scratchpad.client.shapes.connectors.WiresBezierCurve;

@ApplicationScoped
public class BezierCurveFactory implements ShapeFactory<BezierCurve> {

    private static final String DESCRIPTION = "Curve";

    private static final int SHAPE_SIZE_X = 50;
    private static final int SHAPE_SIZE_Y = 50;

    @Override
    public ShapeGlyph<BezierCurve> getGlyph() {
        final BezierCurve curve = makeBezierCurve();

        return new ShapeGlyph<BezierCurve>() {
            @Override
            public Shape<BezierCurve> getShape() {
                return curve;
            }

            @Override
            public double getWidth() {
                return ( SHAPE_SIZE_X + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }

            @Override
            public double getHeight() {
                return ( SHAPE_SIZE_Y + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }
        };
    }

    @Override
    public String getShapeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Category getCategory() {
        return ConnectorCategory.CATEGORY;
    }

    @Override
    public ShapeDragProxy<BezierCurve> getDragProxy( final ShapeDragProxyPreviewCallback dragPreviewCallback,
                                                     final ShapeDragProxyCompleteCallback dragEndCallBack ) {
        final BezierCurve curve = makeBezierCurve();

        return new ShapeDragProxy<BezierCurve>() {
            @Override
            public Shape<BezierCurve> getDragShape() {
                return curve;
            }

            @Override
            public void onDragPreview( final double x,
                                       final double y ) {
                dragPreviewCallback.callback( x,
                                              y );
            }

            @Override
            public void onDragComplete( final double x,
                                        final double y ) {
                dragEndCallBack.callback( x,
                                          y );
            }

            @Override
            public int getWidth() {
                return ( SHAPE_SIZE_X + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }

            @Override
            public int getHeight() {
                return ( SHAPE_SIZE_Y + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }

        };
    }

    @Override
    public WiresBaseShape getShape() {
        return new WiresBezierCurve( makeBezierCurve() );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof WiresBezierCurve;
    }

    private BezierCurve makeBezierCurve() {
        final BezierCurve curve = new BezierCurve( 0 - SHAPE_SIZE_X,
                                                   0 - SHAPE_SIZE_Y,
                                                   0 - SHAPE_SIZE_X,
                                                   SHAPE_SIZE_Y,
                                                   SHAPE_SIZE_X,
                                                   0 - SHAPE_SIZE_Y,
                                                   SHAPE_SIZE_X,
                                                   SHAPE_SIZE_Y );
        curve.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setDraggable( false );
        return curve;
    }

}