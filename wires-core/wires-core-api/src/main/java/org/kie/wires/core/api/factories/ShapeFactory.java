/*
 * Copyright 2012 JBoss Inc
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
package org.kie.wires.core.api.factories;

import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;

/**
 * Factory for building shapes available for authoring.
 */
public interface ShapeFactory<T extends Shape<T>> {

    /**
     * Get a glyph to represent the Shape. Used by the Palette Screen and Layers Screen
     * @return
     */
    ShapeGlyph<T> getGlyph();

    /**
     * Get a proxy used during and at the end of a drag operation
     * @param dragPreviewCallback
     * @param dragEndCallBack
     * @return
     */
    ShapeDragProxy<T> getDragProxy( final ShapeDragProxyPreviewCallback dragPreviewCallback,
                                    final ShapeDragProxyCompleteCallback dragEndCallBack );

    /**
     * Get a Shape to be created on the Canvas (usually at the end of a drag operation)
     * @return
     */
    WiresBaseShape getShape( final ShapeDragContext dragContext );

    /**
     * Does the Factory build the given shape type
     * @param shapeType
     * @return true is the Factor builds the given type
     */
    boolean builds( final WiresBaseShape shapeType );

    /**
     * Get description of Shape
     * @return
     */
    String getShapeDescription();

    /**
     * Get category to which Shape belongs
     * @return
     */
    Category getCategory();

}
