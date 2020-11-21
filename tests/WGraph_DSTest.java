package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {
    private static Random _rnd = null;

    @Test
    void getNode() {
        weighted_graph g = new WGraph_DS();
        assertNull(g.getNode(2));
        g.addNode(2);
        node_info a = g.getNode(2);
        assertEquals(g.getNode(2), a);
    }

    @Test
    void hasEdge() {
        int v = 10, e = v * (v - 1) / 2;
        weighted_graph g = graph_creator(v, e, 1);
        for (int i = 0; i < v; i++) {
            for (int j = i + 1; j < v; j++) {
                boolean b = g.hasEdge(i, j);
                assertTrue(b);
                assertTrue(g.hasEdge(j, i));
            }
        }
        g.removeEdge(4, 7);
        assertFalse(g.hasEdge(7, 4));
    }

    @Test
    void getEdge() {
        weighted_graph g = new WGraph_DS();
        g.addNode(4);
        g.addNode(9);
        g.addNode(6);
        g.addNode(30);
        g.connect(9, 4, 6.7);
        g.connect(6, 30, 0);
        assertEquals(g.getEdge(4, 9), 6.7);
        assertEquals(g.getEdge(4, 6), -1);
        assertEquals(4, 4, 0);
    }

    @Test
    void connect() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        assertEquals(g.getEdge(0, 3), 3);
        g.connect(3, 0, 5);
        assertEquals(g.getEdge(3, 0), 5);
        g.removeEdge(0, 1);
        assertFalse(g.hasEdge(1, 0));
        g.removeEdge(2, 1);
        g.connect(0, 1, 1);
        assertEquals(g.getEdge(1, 0), 1);
    }

    @Test
    void getV() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        Iterator<node_info> iter = g.getV().iterator();
        while (iter.hasNext()) {
            node_info n = iter.next();
            assertNotNull(n);
        }
        assertEquals(g.nodeSize(), 4);
    }

    @Test
    void getV_id() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.connect(0, 3, 5);
        Iterator<node_info> it = g.getV(0).iterator();
        while (it.hasNext()) {
            node_info n = it.next();
            assertNotNull(n);
        }
        assertEquals(g.getV(0).size(), 3);
    }

    @Test
    void removeNode() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.removeNode(4);
        g.removeNode(0);
        assertFalse(g.hasEdge(1, 0));
        assertEquals(0, g.edgeSize());
        assertEquals(3, g.nodeSize());
    }

    @Test
    void removeEdge() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.removeEdge(0, 3);
        double w = g.getEdge(3, 0);
        assertEquals(w, -1);
    }

    @Test
    void nodeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(1);
        g.removeNode(2);
        g.removeNode(1);
        g.removeNode(1);
        assertEquals(1, g.nodeSize());
    }

    @Test
    void edgeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.connect(0, 1, 2);
        int e_size = g.edgeSize();
        assertEquals(3, e_size);
        double w03 = g.getEdge(0, 3);
        double w30 = g.getEdge(3, 0);
        assertEquals(w03, w30);
        assertEquals(w03, 3);
    }

    /* creates a graph with 1 million nodes and 10 million random edges
     * checks the runtime - around 30 seconds */
    @Test
    void MillionNodes() {
        long start = new Date().getTime();
        weighted_graph g = graph_creator(1000000, 10000000, 1);
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        boolean a = dt < 40;
        System.out.println("runtime test: " + dt);
        assertTrue(a);
    }


    ///////////////////Functions from the tests on the GitHub

    /**
     * Generate a random graph with v_size nodes and e_size edges
     *
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     */
    public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        // Iterator<node_data> itr = g.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while (g.edgeSize() < e_size) {
            int a = nextRnd(0, v_size);
            int b = nextRnd(0, v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i, j, w);
        }
        return g;
    }

    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0 + min, (double) max);
        int ans = (int) v;
        return ans;
    }

    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max - min;
        double ans = d * dx + min;
        return ans;
    }

    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     *
     * @param g
     * @return
     */
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for (int i = 0; i < size; i++) {
            ans[i] = nodes[i].getKey();
        }
        Arrays.sort(ans);
        return ans;
    }
}