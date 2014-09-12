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
package org.kie.wires.client.layers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.TextBaseLine;
import com.google.gwt.user.client.ui.Composite;
import org.kie.wires.core.api.events.ShapeSelectedEvent;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.util.ShapeFactoryUtil;

@ApplicationScoped
public class StencilLayerBuilder extends Composite {

    private static final int GLYPH_WIDTH = 25;
    private static final int GLYPH_HEIGHT = 25;

    @Inject
    private Event<ShapeSelectedEvent> shapeSelectedEvent;

    public LayerShape build( final WiresBaseShape shape,
                             final ShapeFactory factory ) {
        final LayerShape layerShape = new LayerShape();
        final Rectangle bounding = drawBoundingBox();
        final ShapeGlyph glyph = factory.getGlyph();
        final Text description = drawDescription( factory.getShapeDescription() );

        //Clicking on the Shape selects it
        layerShape.addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( final NodeMouseClickEvent nodeMouseClickEvent ) {
                shapeSelectedEvent.fire( new ShapeSelectedEvent( shape ) );
            }
        } );

        //Build Layer Shape
        layerShape.setBounding( bounding );
        layerShape.setDescription( description );
        layerShape.setShape( scaleGlyph( glyph ) );

        return layerShape;
    }

    private Shape scaleGlyph( final ShapeGlyph glyph ) {
        final double sx = GLYPH_WIDTH / glyph.getWidth();
        final double sy = GLYPH_HEIGHT / glyph.getHeight();
        final Shape shape = glyph.getShape();
        return shape.setX( ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER / 2 ).setY( ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER / 2 ).setScale( sx,
                                                                                                                                     sy );
    }

    private Rectangle drawBoundingBox() {
        final Rectangle boundingBox = new Rectangle( ShapeFactoryUtil.WIDTH_BOUNDING_LAYER,
                                                     ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER );
        boundingBox.setStrokeColor( ShapeFactoryUtil.RGB_STROKE_BOUNDING )
                .setStrokeWidth( 1 )
                .setFillColor( ShapeFactoryUtil.RGB_FILL_BOUNDING )
                .setDraggable( false );
        return boundingBox;
    }

    private Text drawDescription( final String description ) {
        Text text = new Text( description,
                              ShapeFactoryUtil.FONT_FAMILY_DESCRIPTION,
                              ShapeFactoryUtil.FONT_SIZE_DESCRIPTION );
        text.setFillColor( ShapeFactoryUtil.RGB_TEXT_DESCRIPTION );
        text.setTextBaseLine( TextBaseLine.MIDDLE );
        text.setX( 40 );
        text.setY( 15 );
        return text;
    }

}