/*
 * Copyright 2010-2020 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.layers.utilities;

import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.graph.schema.visual.concept.VisualConcept;
import au.gov.asd.tac.constellation.plugins.PluginException;
import au.gov.asd.tac.constellation.plugins.PluginInteraction;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.plugins.templates.SimpleEditPlugin;

/**
 *
 * @author aldebaran30701
 */
public final class UpdateGraphBitmaskPlugin extends SimpleEditPlugin {

    /**
     * Write the given bitmask to the active graph.
     */
    private final int bitmask;

    public UpdateGraphBitmaskPlugin(final int bitmask) {
        this.bitmask = bitmask;
    }

    @Override
    public void edit(final GraphWriteMethods graph, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        int bitmaskAttributeId = VisualConcept.GraphAttribute.SELECTEDFILTERMASK.ensure(graph);
        graph.setIntValue(bitmaskAttributeId, 0, bitmask);
    }

    @Override
    protected boolean isSignificant() {
        return true;
    }

    @Override
    public String getName() {
        return "Layers View: Update Graph Bitmask";
    }
}
