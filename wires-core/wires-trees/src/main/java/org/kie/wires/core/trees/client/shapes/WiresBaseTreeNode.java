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
package org.kie.wires.core.trees.client.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.emitrom.lienzo.client.core.animation.AnimationProperties;
import com.emitrom.lienzo.client.core.animation.AnimationTweener;
import com.emitrom.lienzo.client.core.animation.IAnimation;
import com.emitrom.lienzo.client.core.animation.IAnimationCallback;
import com.emitrom.lienzo.client.core.animation.IAnimationHandle;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.types.Point2D;
import org.kie.wires.core.api.shapes.RequiresShapesManager;
import org.kie.wires.core.api.shapes.ShapesManager;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.trees.client.canvas.WiresTreeNodeConnector;
import org.uberfire.mvp.Command;

public abstract class WiresBaseTreeNode extends WiresBaseShape implements RequiresShapesManager {

    private static final int ANIMATION_DURATION = 250;

    private WiresBaseTreeNode parent;
    private List<WiresBaseTreeNode> children = new ArrayList<WiresBaseTreeNode>();
    private List<WiresTreeNodeConnector> connectors = new ArrayList<WiresTreeNodeConnector>();

    private int collapsed = 0;
    private Stack<Point2D> locations = new Stack<Point2D>();

    private ShapesManager shapesManager;

    @Override
    public void setShapesManager( final ShapesManager shapesManager ) {
        this.shapesManager = shapesManager;
    }

    @Override
    public boolean contains( final double cx,
                             final double cy ) {
        return false;
    }

    @Override
    public void init( final double cx,
                      final double cy ) {
        super.init( cx,
                    cy );

        //Update connectors when this Node moves
        addNodeDragMoveHandler( new NodeDragMoveHandler() {

            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                for ( WiresTreeNodeConnector connector : connectors ) {
                    connector.getPoints().getPoint( 0 ).set( getLocation() );
                }
                getLayer().draw();
            }
        } );
    }

    @Override
    public void destroy() {
        //Remove children
        final List<WiresBaseTreeNode> cloneChildren = new ArrayList<WiresBaseTreeNode>( children );
        for ( WiresBaseTreeNode child : cloneChildren ) {
            shapesManager.forceDeleteShape( child );
        }
        children.clear();

        //Remove connectors to children
        final List<WiresTreeNodeConnector> cloneConnectors = new ArrayList<WiresTreeNodeConnector>( connectors );
        for ( WiresTreeNodeConnector connector : cloneConnectors ) {
            getLayer().remove( connector );
        }
        connectors.clear();

        //Remove from its parent
        if ( parent != null ) {
            parent.removeChildNode( this );
        }
        super.destroy();
    }

    public void setParentNode( final WiresBaseTreeNode parent ) {
        this.parent = parent;
    }

    /**
     * TreeNodes can decide to accept child TreeNodes when being dragged from the Palette to a prospective parent
     * @param child TreeNode that will be added to this TreeNode as a child
     * @return true if the child can be added to this TreeNode
     */
    public boolean acceptChildNode( final WiresBaseTreeNode child ) {
        //Accept all types of WiresBaseTreeNode by default
        return true;
    }

    /**
     * Add a TreeNode as a child to this TreeNode. A connector is automatically created and maintained for the child.
     * Connectors are "outgoing" from the parent to a child.
     * @param child
     */
    public void addChildNode( final WiresBaseTreeNode child ) {
        final WiresTreeNodeConnector connector = new WiresTreeNodeConnector();
        connector.getPoints().getPoint( 0 ).set( getLocation() );
        connector.getPoints().getPoint( 1 ).set( child.getLocation() );
        getLayer().add( connector );
        connector.moveToBottom();

        children.add( child );
        connectors.add( connector );
        child.setParentNode( this );

        //Update connectors when child Node moves
        child.addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                connector.getPoints().getPoint( 1 ).set( child.getLocation() );
            }
        } );
    }

    /**
     * Remove a child TreeNode from this TreeNode. Connectors are automatically cleared up.
     * @param child
     */
    public void removeChildNode( final WiresBaseTreeNode child ) {
        child.setParentNode( null );
        final int index = children.indexOf( child );
        final WiresTreeNodeConnector connector = connectors.get( index );
        children.remove( child );
        connectors.remove( connector );
        getLayer().remove( connector );
    }

    private void childMoved( final WiresBaseTreeNode child,
                             final double nx,
                             final double ny ) {
        final int index = children.indexOf( child );
        final WiresTreeNodeConnector connector = connectors.get( index );
        connector.getPoints().getPoint( 1 ).setX( nx );
        connector.getPoints().getPoint( 1 ).setY( ny );
    }

    /**
     * Collapse this TreeNode and all descendants.
     * @param callback The callback is invoked when the animation completes.
     */
    public void collapse( final Command callback ) {
        //This TreeNode is already collapsed
        if ( !hasChildren() || hasCollapsedChildren() ) {
            return;
        }

        final List<WiresBaseTreeNode> descendants = getDescendants( this );
        final Point2D destination = getLocation();

        final AnimationProperties props = new AnimationProperties();

        animate( AnimationTweener.EASE_OUT,
                 props,
                 ANIMATION_DURATION,
                 new IAnimationCallback() {

                     private List<Point2D> origins = new ArrayList<Point2D>();

                     @Override
                     public void onStart( final IAnimation iAnimation,
                                          final IAnimationHandle iAnimationHandle ) {
                         origins.clear();
                         for ( WiresBaseTreeNode descendant : descendants ) {
                             //Record starting point of each descendant
                             origins.add( descendant.getLocation() );

                             //Remember descendants current location (for nested collapses)
                             descendant.locations.push( descendant.getLocation() );
                             descendant.collapsed++;
                         }

                         //Allow subclasses to change their appearance
                         onCollapseStart();
                     }

                     @Override
                     public void onFrame( final IAnimation iAnimation,
                                          final IAnimationHandle iAnimationHandle ) {
                         //Lienzo's IAnimation.getPercent() passes values > 1.0
                         final double pct = iAnimation.getPercent() > 1.0 ? 1.0 : iAnimation.getPercent();

                         for ( int index = 0; index < descendants.size(); index++ ) {
                             //Move each descendant along the line between its origin and the target destination
                             final Point2D descendantOrigin = origins.get( index );
                             final WiresBaseTreeNode descendant = descendants.get( index );
                             final double dx = ( destination.getX() - descendantOrigin.getX() ) * pct;
                             final double dy = ( destination.getY() - descendantOrigin.getY() ) * pct;
                             descendant.setX( descendantOrigin.getX() + dx );
                             descendant.setY( descendantOrigin.getY() + dy );
                             descendant.setAlpha( 1.0 - pct );

                             //Move the Connector end-points to match where the descendant has been moved
                             for ( WiresTreeNodeConnector connector : descendant.connectors ) {
                                 connector.getPoints().getPoint( 0 ).setX( descendant.getX() );
                                 connector.getPoints().getPoint( 0 ).setY( descendant.getY() );
                             }
                             final WiresBaseTreeNode parent = descendant.parent;
                             if ( parent != null ) {
                                 parent.childMoved( descendant,
                                                    descendant.getX(),
                                                    descendant.getY() );
                             }
                         }

                         //Allow subclasses to change their appearance
                         onCollapseProgress( pct );

                         //Without this call Lienzo doesn't update the Canvas for sub-classes of WiresBaseTreeNode
                         WiresBaseTreeNode.this.getLayer().draw();
                     }

                     @Override
                     public void onClose( final IAnimation iAnimation,
                                          final IAnimationHandle iAnimationHandle ) {
                         //Hide connectors, descendants and descendants' connectors when complete
                         for ( WiresTreeNodeConnector connector : connectors ) {
                             connector.setVisible( false );
                         }
                         for ( WiresBaseTreeNode descendant : descendants ) {
                             descendant.setVisible( false );
                             for ( WiresTreeNodeConnector connector : descendant.connectors ) {
                                 connector.setVisible( false );
                             }
                         }

                         //Allow subclasses to change their appearance
                         onCollapseEnd();

                         //Invoke callback if one was provided
                         if ( callback != null ) {
                             callback.execute();
                         }
                     }
                 } );

        getLayer().draw();
    }

    /**
     * Called when the TreeNode is about to be collapsed. Default implementation does nothing.
     */
    public void onCollapseStart() {
        //Do nothing by default
    }

    /**
     * Called while the TreeNode is being collapsed. Default implementation does nothing.
     * @param pct 0.0 to 1.0 where 1.0 is collapsed
     */
    public void onCollapseProgress( final double pct ) {
        //Do nothing by default
    }

    /**
     * Called when the TreeNode has been collapsed. Default implementation does nothing.
     */
    public void onCollapseEnd() {
        //Do nothing by default
    }

    /**
     * Expand this TreeNode and all descendants. Nested collapsed TreeNodes are not expanded.
     * @param callback The callback is invoked when the animation completes.
     */
    public void expand( final Command callback ) {
        //This TreeNode is already expanded
        if ( !hasCollapsedChildren() ) {
            return;
        }

        final List<WiresBaseTreeNode> descendants = getDescendants( this );

        final AnimationProperties props = new AnimationProperties();

        animate( AnimationTweener.EASE_OUT,
                 props,
                 ANIMATION_DURATION,
                 new IAnimationCallback() {

                     private List<Point2D> origins = new ArrayList<Point2D>();

                     @Override
                     public void onStart( final IAnimation iAnimation,
                                          final IAnimationHandle iAnimationHandle ) {
                         //Record starting point of each descendant
                         origins.clear();
                         for ( WiresBaseTreeNode descendant : descendants ) {
                             origins.add( descendant.getLocation() );
                         }

                         //Show connectors to this node's immediate children
                         for ( WiresTreeNodeConnector connector : connectors ) {
                             connector.setVisible( true );
                         }

                         //Show child nodes and connectors if they are not still collapsed
                         for ( WiresBaseTreeNode descendant : descendants ) {
                             descendant.collapsed--;
                             if ( descendant.collapsed == 0 ) {
                                 descendant.setVisible( true );
                             }
                         }
                         for ( WiresBaseTreeNode descendant : descendants ) {
                             for ( WiresTreeNodeConnector connector : descendant.connectors ) {
                                 connector.setVisible( !descendant.hasCollapsedChildren() );
                             }
                         }

                         //Allow subclasses to change their appearance
                         onExpandStart();
                     }

                     @Override
                     public void onFrame( final IAnimation iAnimation,
                                          final IAnimationHandle iAnimationHandle ) {
                         //Lienzo's IAnimation.getPercent() passes values > 1.0
                         final double pct = iAnimation.getPercent() > 1.0 ? 1.0 : iAnimation.getPercent();

                         for ( int index = 0; index < descendants.size(); index++ ) {
                             //Move the Connector end-points to match where the descendant has been moved
                             final Point2D descendantOrigin = origins.get( index );
                             final WiresBaseTreeNode descendant = descendants.get( index );
                             final Point2D destination = descendant.locations.peek();
                             final double dx = ( destination.getX() - descendantOrigin.getX() ) * pct;
                             final double dy = ( destination.getY() - descendantOrigin.getY() ) * pct;
                             descendant.setX( descendantOrigin.getX() + dx );
                             descendant.setY( descendantOrigin.getY() + dy );
                             descendant.setAlpha( pct );

                             //Move the Connector end-points to match where the descendant has been moved
                             for ( WiresTreeNodeConnector connector : descendant.connectors ) {
                                 connector.getPoints().getPoint( 0 ).setX( descendant.getX() );
                                 connector.getPoints().getPoint( 0 ).setY( descendant.getY() );
                             }
                             final WiresBaseTreeNode parent = descendant.parent;
                             if ( parent != null ) {
                                 parent.childMoved( descendant,
                                                    descendant.getX(),
                                                    descendant.getY() );
                             }
                         }

                         //Allow subclasses to change their appearance
                         onExpandProgress( pct );

                         //Without this call Lienzo doesn't update the Canvas for sub-classes of WiresBaseTreeNode
                         WiresBaseTreeNode.this.getLayer().draw();
                     }

                     @Override
                     public void onClose( final IAnimation iAnimation,
                                          final IAnimationHandle iAnimationHandle ) {
                         //Remove the recorded descendants location (for nested collapses)
                         for ( WiresBaseTreeNode descendant : descendants ) {
                             descendant.locations.pop();
                         }

                         //Allow subclasses to change their appearance
                         onExpandEnd();

                         //Invoke callback if one was provided
                         if ( callback != null ) {
                             callback.execute();
                         }
                     }
                 } );

        getLayer().draw();
    }

    /**
     * Called when the TreeNode is about to be expanded. Default implementation does nothing.
     */
    public void onExpandStart() {
        //Do nothing by default
    }

    /**
     * Called while the TreeNode is being expanded. Default implementation does nothing.
     * @param pct 0.0 to 1.0 where 1.0 is expanded
     */
    public void onExpandProgress( final double pct ) {
        //Do nothing by default
    }

    /**
     * Called when the TreeNode has been expanded. Default implementation does nothing.
     */
    public void onExpandEnd() {
        //Do nothing by default
    }

    private List<WiresBaseTreeNode> getDescendants( final WiresBaseTreeNode node ) {
        final List<WiresBaseTreeNode> descendants = new ArrayList<WiresBaseTreeNode>();
        descendants.addAll( node.children );
        for ( WiresBaseTreeNode child : node.children ) {
            descendants.addAll( getDescendants( child ) );
        }
        return descendants;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public boolean hasCollapsedChildren() {
        for ( WiresBaseTreeNode child : children ) {
            if ( child.collapsed > 0 ) {
                return true;
            }
        }
        return false;
    }

}