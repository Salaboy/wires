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
package org.kie.wires.core.trees.client.canvas;

import org.kie.wires.core.api.factories.ShapeDropContext;
import org.kie.wires.core.trees.client.shapes.WiresBaseTreeNode;

public class WiresTreeNodeDropContext implements ShapeDropContext<WiresBaseTreeNode> {

    private WiresBaseTreeNode context;

    @Override
    public WiresBaseTreeNode getContext() {
        return context;
    }

    @Override
    public void setContext( final WiresBaseTreeNode context ) {
        this.context = context;
    }

}