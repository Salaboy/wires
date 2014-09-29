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
package org.kie.wires.core.scratchpad.client.factories.dynamic;

import javax.enterprise.context.ApplicationScoped;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import org.kie.wires.core.api.factories.FactoryHelper;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.factories.AbstractBaseFactory;
import org.kie.wires.core.client.factories.categories.ShapeCategory;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.scratchpad.client.shapes.dynamic.WiresRectangle;

@ApplicationScoped
public class RectangleFactory extends AbstractBaseFactory<Rectangle> {

    private static final String DESCRIPTION = "Box";

    private static final int SHAPE_SIZE_X = 70;
    private static final int SHAPE_SIZE_Y = 40;

    @Override
    public String getShapeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Category getCategory() {
        return ShapeCategory.CATEGORY;
    }

    @Override
    public WiresBaseShape getShape( final FactoryHelper helper ) {
        return new WiresRectangle( makeShape() );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof WiresRectangle;
    }

    @Override
    protected Rectangle makeShape() {
        final Rectangle rectangle = new Rectangle( SHAPE_SIZE_X,
                                                   SHAPE_SIZE_Y,
                                                   5 );
        rectangle.setOffset( 0 - ( SHAPE_SIZE_X / 2 ),
                             0 - ( SHAPE_SIZE_Y / 2 ) )
                .setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( ShapesUtils.RGB_FILL_SHAPE )
                .setDraggable( false );
        return rectangle;
    }

    @Override
    protected int getWidth() {
        return SHAPE_SIZE_X + ( ShapesUtils.RGB_STROKE_WIDTH_SHAPE * 2 );
    }

    @Override
    protected int getHeight() {
        return SHAPE_SIZE_Y + ( ShapesUtils.RGB_STROKE_WIDTH_SHAPE * 2 );
    }

}