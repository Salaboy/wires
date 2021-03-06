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
package com.bayesian.network.client.perspectives;

import javax.enterprise.context.ApplicationScoped;

import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.PanelDefinition;
import org.uberfire.workbench.model.PanelType;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.Position;
import org.uberfire.workbench.model.impl.PanelDefinitionImpl;
import org.uberfire.workbench.model.impl.PartDefinitionImpl;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;

import static org.uberfire.workbench.model.PanelType.*;

/**
 * A Perspective to show Bayesian related panels
 */
@ApplicationScoped
@WorkbenchPerspective(identifier = "WiresBayesianPerspective")
public class WiresBayesianPerspective {

    private static final String WIRES = "Bayesian Networks";

    private static final String BAYESIAN_SCREEN = "BayesianScreen";
    private static final String WIRES_LAYERS_SCREEN = "WiresLayersScreen";
    private static final String WIRES_TEMPLATE_SCREEN = "BayesianTemplatesScreen";
    private static final String BAYESIAN_SOUTH_SCREEN = "BayesianVariablesScreen";

    private static final int MIN_WIDTH_PANEL = 200;
    private static final int WIDTH_PANEL = 300;

    private PerspectiveDefinition perspective;

    @Perspective
    public PerspectiveDefinition buildPerspective() {
        this.perspective = new PerspectiveDefinitionImpl( ROOT_STATIC );
        perspective.setName( WIRES );

        perspective.getRoot().addPart( new PartDefinitionImpl( new DefaultPlaceRequest( BAYESIAN_SCREEN ) ) );

        this.createPanelWithChild( perspective,
                                   Position.EAST );
        this.drawPanel( perspective,
                        Position.SOUTH,
                        BAYESIAN_SOUTH_SCREEN );

        perspective.setTransient( true );

        return perspective;
    }

    private void drawPanel( final PerspectiveDefinition p,
                            final Position position,
                            final String identifierPanel ) {
        p.getRoot().insertChild( position,
                                 newPanel( p,
                                           position,
                                           identifierPanel ) );
    }

    private void createPanelWithChild( final PerspectiveDefinition p,
                                       final Position position ) {
        final PanelDefinition templatePanel = newPanel( p,
                                                        position,
                                                        WIRES_TEMPLATE_SCREEN );
        templatePanel.setHeight( 380 );
        templatePanel.setMinHeight( 250 );

        final PanelDefinition parentPanel = newPanel( p,
                                                      position,
                                                      WIRES_LAYERS_SCREEN );
        parentPanel.setHeight( 180 );
        parentPanel.setMinHeight( 150 );
        parentPanel.appendChild( Position.SOUTH,
                                 templatePanel );

        p.getRoot().insertChild( position,
                                 parentPanel );
    }

    private PanelDefinition newPanel( final PerspectiveDefinition p,
                                      final Position position,
                                      final String identifierPanel ) {
        final PanelDefinition panel = new PanelDefinitionImpl( PanelType.MULTI_LIST );
        panel.setWidth( WIDTH_PANEL );
        panel.setMinWidth( MIN_WIDTH_PANEL );
        panel.addPart( new PartDefinitionImpl( new DefaultPlaceRequest( identifierPanel ) ) );
        return panel;
    }
}
