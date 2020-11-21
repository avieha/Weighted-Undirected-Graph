package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    @Test
    void init_getGraph(){
        weighted_graph g = WGraph_DSTest.graph_creator(5,10,1);
        weighted_graph_algorithms g0 = new WGraph_Algo();
        g0.init(g);
        assertEquals(g0.getGraph(),g);
    }

    @Test
    void copy(){
        weighted_graph g = WGraph_DSTest.graph_creator(5,10,1);
        weighted_graph_algorithms g0 = new WGraph_Algo();
        g0.init(g);
        weighted_graph g1 = g0.copy();
        assertEquals(g0.getGraph(),g1);
        assertEquals(g1,g);
        g.addNode(67);
        assertNotEquals(g,g1);
    }

    @Test
    void isConnected() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(0,0,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(1,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertFalse(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,1,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(10,30,1);
        ag0.init(g0);
        assertTrue(ag0.isConnected());
    }

    @Test
    void shortestPathDist() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
        double d = ag0.shortestPathDist(0,10);
        assertEquals(d, 5.1);
    }

    @Test
    void shortestPath() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(0,10);
        //double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for(node_info n: sp) {
            //assertEquals(n.getTag(), checkTag[i]);
            assertEquals(n.getKey(), checkKey[i]);
            i++;
        }
    }

    @Test
    void equals() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        weighted_graph g1 = WGraph_DSTest.graph_creator(10,30,1);
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }

    @Test
    void save_load(){
        weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,100);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        weighted_graph_algorithms ag1 = new WGraph_Algo();
        String str = "g0.obj";
        ag0.init(g0);
        ag0.save(str);
        ag1.load(str);
        assertEquals(ag1.getGraph(),g0);
        String s = "fail.txt";
        ag1.load(s); // not a real graph, shouldn't change a thing
        assertEquals(ag1.getGraph(),g0);
    }

    private weighted_graph small_graph() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(11,0,1);
        g0.connect(0,1,1);
        g0.connect(0,2,2);
        g0.connect(0,3,3);
        g0.connect(1,4,17);
        g0.connect(1,5,1);
        g0.connect(2,4,1);
        g0.connect(3, 5,10);
        g0.connect(3,6,100);
        g0.connect(5,7,1.1);
        g0.connect(6,7,10);
        g0.connect(7,10,2);
        g0.connect(6,8,30);
        g0.connect(8,10,10);
        g0.connect(4,10,30);
        g0.connect(3,9,10);
        g0.connect(8,10,10);
        return g0;
    }
}