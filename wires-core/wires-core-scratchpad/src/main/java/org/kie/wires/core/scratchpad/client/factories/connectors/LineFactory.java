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

import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.shared.core.types.LineCap;
import org.kie.wires.core.api.factories.ShapeDragContext;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.factories.AbstractBaseFactory;
import org.kie.wires.core.client.factories.categories.ConnectorCategory;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.scratchpad.client.shapes.connectors.WiresLine;

@ApplicationScoped
public class LineFactory extends AbstractBaseFactory<Line> {

    private static final String DESCRIPTION = "Line";

    private static final int SHAPE_SIZE_X = 40;
    private static final int SHAPE_SIZE_Y = 40;

    @Override
    public String getShapeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Category getCategory() {
        return ConnectorCategory.CATEGORY;
    }

    @Override
    public WiresBaseShape getShape( final ShapeDragContext dragContext ) {
        return new WiresLine( makeShape() );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof WiresLine;
    }

    @Override
    protected Line makeShape() {
        final Line line = new Line( 0 - ( SHAPE_SIZE_X / 2 ),
                                    0 - ( SHAPE_SIZE_Y / 2 ),
                                    SHAPE_SIZE_X / 2,
                                    SHAPE_SIZE_Y / 2 );
        line.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( ShapesUtils.RGB_FILL_SHAPE )
                .setLineCap( LineCap.ROUND )
                .setStrokeWidth( 3 )
                .setDraggable( false );
        return line;
    }

    @Override
    protected int getWidth() {
        return SHAPE_SIZE_X;
    }

    @Override
    protected int getHeight() {
        return SHAPE_SIZE_Y;
    }

}