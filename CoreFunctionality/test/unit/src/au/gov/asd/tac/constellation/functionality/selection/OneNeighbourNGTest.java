/*
 * Copyright 2010-2019 Australian Signals Directorate
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
package au.gov.asd.tac.constellation.functionality.selection;

import au.gov.asd.tac.constellation.functionality.CorePluginRegistry;
import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.graph.GraphElementType;
import au.gov.asd.tac.constellation.graph.StoreGraph;
import au.gov.asd.tac.constellation.schema.visualschema.concept.VisualConcept;
import au.gov.asd.tac.constellation.pluginframework.PluginException;
import au.gov.asd.tac.constellation.pluginframework.PluginExecution;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * One Neighbour Test.
 *
 * @author altair
 */
public class OneNeighbourNGTest {

    private int attrX, attrY, attrZ;
    private int vxId1, vxId2, vxId3, vxId4, vxId5;
    private int txId1, txId2, txId3;
    private int vAttrId, tAttrId;
    private StoreGraph graph;

    public OneNeighbourNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    private void generateData() throws InterruptedException {
        graph = new StoreGraph();
        attrX = graph.addAttribute(GraphElementType.VERTEX, "float", "x", "x", 0.0, null);
        if (attrX == Graph.NOT_FOUND) {
            fail();
        }

        attrY = graph.addAttribute(GraphElementType.VERTEX, "float", "y", "y", 0.0, null);
        if (attrY == Graph.NOT_FOUND) {
            fail();
        }

        attrZ = graph.addAttribute(GraphElementType.VERTEX, "float", "z", "z", 0.0, null);
        if (attrZ == Graph.NOT_FOUND) {
            fail();
        }

        vAttrId = graph.addAttribute(GraphElementType.VERTEX, "boolean", "selected", "selected", false, null);
        if (vAttrId == Graph.NOT_FOUND) {
            fail();
        }

        tAttrId = graph.addAttribute(GraphElementType.TRANSACTION, "boolean", "selected", "selected", false, null);
        if (tAttrId == Graph.NOT_FOUND) {
            fail();
        }

        vxId1 = graph.addVertex();
        graph.setFloatValue(attrX, vxId1, 1.0f);
        graph.setFloatValue(attrY, vxId1, 1.0f);
        graph.setBooleanValue(vAttrId, vxId1, false);
        vxId2 = graph.addVertex();
        graph.setFloatValue(attrX, vxId2, 5.0f);
        graph.setFloatValue(attrY, vxId2, 1.0f);
        graph.setBooleanValue(vAttrId, vxId2, false);
        vxId3 = graph.addVertex();
        graph.setFloatValue(attrX, vxId3, 1.0f);
        graph.setFloatValue(attrY, vxId3, 5.0f);
        graph.setBooleanValue(vAttrId, vxId3, false);
        vxId4 = graph.addVertex();
        graph.setFloatValue(attrX, vxId4, 5.0f);
        graph.setFloatValue(attrY, vxId4, 5.0f);
        graph.setBooleanValue(vAttrId, vxId4, false);
        vxId5 = graph.addVertex();
        graph.setFloatValue(attrX, vxId5, 10.0f);
        graph.setFloatValue(attrY, vxId5, 10.0f);
        graph.setBooleanValue(vAttrId, vxId5, false);

        graph.addTransaction(vxId1, vxId2, false);
        graph.addTransaction(vxId1, vxId3, false);
        graph.addTransaction(vxId2, vxId4, true);
        graph.addTransaction(vxId4, vxId2, true);
    }

    @Test
    public void selectAllTest() throws InterruptedException, PluginException {
        generateData();

        int attrId = graph.getAttribute(GraphElementType.VERTEX, VisualConcept.VertexAttribute.SELECTED.getName());
        assertTrue("Selection column found", (attrId >= 0));

        for (int i = 0; i < graph.getVertexCount(); i++) {
            final int txId = graph.getVertex(i);
            boolean flag = graph.getBooleanValue(attrId, txId);
            assertFalse(String.format("Vertex [%d] should be de-selected", txId), flag);
        }

        PluginExecution.withPlugin(CorePluginRegistry.SELECT_PENDANTS).executeNow(graph);

        assertFalse(String.format("Node [%d] should be de-selected", vxId1), graph.getBooleanValue(vAttrId, vxId1));
        assertFalse(String.format("Node [%d] should be de-selected", vxId2), graph.getBooleanValue(vAttrId, vxId2));
        assertTrue(String.format("Node [%d] should be selected", vxId3), graph.getBooleanValue(vAttrId, vxId3));
        assertTrue(String.format("Node [%d] should be selected", vxId4), graph.getBooleanValue(vAttrId, vxId4));
        assertFalse(String.format("Node [%d] should be de-selected", vxId5), graph.getBooleanValue(vAttrId, vxId5));
    }
}
