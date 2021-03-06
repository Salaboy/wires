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
package org.kie.wires.core.client.properties;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.kie.wires.core.api.properties.PropertyEditorAdaptor;

/**
 * A cache of PropertyEditorAdaptors
 */
@ApplicationScoped
public class PropertyEditorAdaptorsCache {

    @Inject
    private SyncBeanManager iocManager;

    private Set<PropertyEditorAdaptor> adaptors = new HashSet<PropertyEditorAdaptor>();

    @PostConstruct
    private void setup() {
        this.adaptors = getAvailableAdaptors();
    }

    public Set<PropertyEditorAdaptor> getAdaptors() {
        return adaptors;
    }

    private Set<PropertyEditorAdaptor> getAvailableAdaptors() {
        final Set<PropertyEditorAdaptor> factories = new HashSet<PropertyEditorAdaptor>();
        final Collection<IOCBeanDef<PropertyEditorAdaptor>> factoryBeans = iocManager.lookupBeans( PropertyEditorAdaptor.class );
        for ( IOCBeanDef<PropertyEditorAdaptor> factoryBean : factoryBeans ) {
            factories.add( factoryBean.getInstance() );
        }
        return factories;
    }

}
