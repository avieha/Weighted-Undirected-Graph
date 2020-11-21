package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable {

    private weighted_graph graph;

    public WGraph_Algo() {
        this.graph = new WGraph_DS();
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.graph = g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return graph;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public weighted_graph copy() {
        if (graph == null)
            return null;
        weighted_graph copy = new WGraph_DS();
        if (graph.nodeSize() == 0)
            return copy;
        Collection<node_info> nodes = graph.getV();
        for (node_info x : nodes) {
            if (copy.getNode(x.getKey()) != null) {
                copy.addNode(x.getKey());
                copy.getNode(x.getKey()).setTag(x.getTag());
                copy.getNode(x.getKey()).setInfo(x.getInfo());
            }
            Collection<node_info> ni = graph.getV(x.getKey());
            for (node_info y : ni) {
                if (!copy.hasEdge(x.getKey(), y.getKey())) {
                    if (copy.getNode(y.getKey()) == null) {
                        copy.addNode(y.getKey());
                        copy.getNode(y.getKey()).setTag(y.getTag());
                        copy.getNode(y.getKey()).setInfo(y.getInfo());
                    }
                    copy.connect(x.getKey(), y.getKey(), graph.getEdge(x.getKey(), y.getKey()));
                }
            }
        }
        return copy;
    }
    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node, in an undirectional graph.
     * @return
     */
    @Override
    public boolean isConnected() {
        if (graph.nodeSize() == 1 || graph.nodeSize() == 0)
            return true;
        if (graph.edgeSize() < graph.nodeSize() - 1)
            return false;
        Collection<node_info> reset = graph.getV();
        for (node_info y : reset) {
            y.setTag(0);
        }
        LinkedList<Integer> queue = new LinkedList<>();
        Iterator<node_info> it = graph.getV().iterator();
        node_info n = it.next();
        n.setTag(1);
        queue.add(n.getKey());
        while (!queue.isEmpty()) {
            int num = queue.remove();
            Collection<node_info> it2 = this.graph.getV(num);
            for (node_info t : it2) {
                int key = t.getKey();
                if (graph.getNode(key).getTag() == 0) {
                    graph.getNode(key).setTag(1);
                    queue.add(key);
                }
            }
        }
        Collection<node_info> check = graph.getV();
        for (node_info y : check) {
            if (y.getTag() == 0)
                return false;
            y.setTag(0);
        }
        return true;
    }
    /**
     * returns the length of the shortest path between src to dest according to the weight of each vertex
     * Note: if no such path --> returns -1
     * Dijkstra's algorithm O(v^2)
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (graph.getNode(src) == null || graph.getNode(dest) == null || graph.nodeSize() == 0)
            return -1;
        Collection<node_info> vertices = graph.getV();
        for (node_info y : vertices) {
            y.setTag(Integer.MAX_VALUE);
            y.setInfo("u");
        }
        PriorityQueue<node_info> queue = new PriorityQueue<>(graph.nodeSize(), new WeightComp());
        graph.getNode(src).setTag(0);
        queue.add(graph.getNode(src));
        while (!queue.isEmpty()) {
            node_info x = queue.poll();
            if (x.getInfo() != "visited") {
                x.setInfo("visited");
                if (x.getKey() == dest)
                    break;
                Collection<node_info> ni = graph.getV(x.getKey());
                for (node_info y : ni) {
                    double distance = x.getTag() + graph.getEdge(x.getKey(), y.getKey());
                    if (distance < y.getTag()) {
                        y.setTag(distance);
                        queue.add(y);
                    }
                }
            }
        }
        return graph.getNode(dest).getTag();
    }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * Dijkstra's algorithm O(v^2)
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (shortestPathDist(src, dest) == -1)
            return null;
        Collection<node_info> vertices = graph.getV();
        for (node_info y : vertices) {
            y.setTag(Integer.MAX_VALUE);
            y.setInfo("u");
        }
        HashMap<node_info, node_info> parent = new HashMap<>();
        PriorityQueue<node_info> queue = new PriorityQueue<>(graph.nodeSize(), new WeightComp());
        graph.getNode(src).setTag(0);
        queue.add(graph.getNode(src));
        parent.put(graph.getNode(src), graph.getNode(src));
        while (!queue.isEmpty()) {
            node_info x = queue.poll();
            if (x.getInfo() != "visited") {
                x.setInfo("visited");
                if (x.getKey() == dest)
                    break;
                Collection<node_info> ni = graph.getV(x.getKey());
                for (node_info y : ni) {
                    double distance = x.getTag() + graph.getEdge(x.getKey(), y.getKey());
                    if (distance < y.getTag()) {
                        y.setTag(distance);
                        parent.put(y, x);
                        queue.add(y);
                    }
                }
            }
        }
        LinkedList<node_info> path = new LinkedList<>();
        path.add(graph.getNode(dest));
        while (parent.get(graph.getNode(dest)) != null && dest != src) {
            path.add(parent.get(graph.getNode(dest)));
            dest = parent.get(graph.getNode(dest)).getKey();
        }
        Collections.reverse(path);
        return path;
    }
    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     * see: https://www.geeksforgeeks.org/serialization-in-java/ */
    @Override
    public boolean save(String file) {
        try {
            // Saving of object in a file
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(f);
            // Method for serialization of object
            out.writeObject(this.graph);
            f.close();
            out.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught at save");
            return false;
        }
        return true;
    }
    /**
     * This method load a graph to this.graph.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        // Deserialization
        try {
            // Reading the object from a file
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fi);
            // Method for deserialization of object
            this.graph = (WGraph_DS) in.readObject();
            in.close();
            fi.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught at load");
            return false;
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
            return false;
        }
        return true;
    }
}