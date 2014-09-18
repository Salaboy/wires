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
package org.kie.wires.core.scratchpad.client.shapes.fixed;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.TextAlign;
import org.kie.wires.core.api.containers.ContainerManager;
import org.kie.wires.core.api.containers.RequiresContainerManager;
import org.kie.wires.core.api.containers.WiresContainer;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.util.UUID;

public class WiresFixedText extends WiresBaseShape implements RequiresContainerManager {

    private static final int BOUNDARY_SIZE = 10;

    private final Text text;
    private final Text bounding;

    private WiresContainer boundContainer;

    protected ContainerManager containerManager;

    public WiresFixedText( final Text shape ) {
        id = UUID.uuid();
        text = shape;

        bounding = new Text( text.getText(),
                             text.getFontFamily(),
                             text.getFontSize() );
        bounding.setTextBaseLine( text.getTextBaseLine() );
        bounding.setTextAlign( text.getTextAlign() );
        bounding.setStrokeWidth( BOUNDARY_SIZE );
        bounding.setAlpha( 0.1 );

        add( text );
    }

    @Override
    public void setContainerManager( final ContainerManager containerManager ) {
        this.containerManager = containerManager;
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
    public void init( double cx,
                      double cy ) {
        super.init( cx,
                    cy );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {

            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                boundContainer = containerManager.getContainer( WiresFixedText.this.getX(),
                                                                WiresFixedText.this.getY() );
                if ( boundContainer != null ) {
                    boundContainer.detachShape( WiresFixedText.this );
                }

                getLayer().draw();
            }
        } );

        addNodeDragEndHandler( new NodeDragEndHandler() {

            @Override
            public void onNodeDragEnd( final NodeDragEndEvent nodeDragEndEvent ) {
                if ( boundContainer != null ) {
                    boundContainer.attachShape( WiresFixedText.this );
                    boundContainer.setHover( false );
                }

                getLayer().draw();
            }
        } );
    }

    @Override
    public boolean contains( final double cx,
                             final double cy ) {
        return false;
    }

    public String getText() {
        return text.getText();
    }

    public void setText( final String text ) {
        this.text.setText( text );
        this.bounding.setText( text );
    }

    public TextAlign getTextAlign() {
        return text.getTextAlign();
    }

    public void setTextAlign( final TextAlign align ) {
        text.setTextAlign( align );
        bounding.setTextAlign( align );
    }

    public String getStrokeColour() {
        return text.getStrokeColor();
    }

    public void setStrokeColour( final String strokeColour ) {
        text.setStrokeColor( strokeColour );
    }

    public String getFillColour() {
        return text.getFillColor();
    }

    public void setFillColour( final String fillColour ) {
        text.setFillColor( fillColour );
    }

    public double getFontSize() {
        return text.getFontSize();
    }

    public void setFontSize( final double fontSize ) {
        text.setFontSize( fontSize );
        bounding.setFontSize( fontSize );
    }

}
