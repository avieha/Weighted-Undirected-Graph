package ex1.src;

import java.io.Serializable;
import java.util.*;

public class WGraph_DS implements weighted_graph, Serializable {

    private HashMap<Integer, node_info> graph;
    private HashMap<Integer, HashMap<Integer, Edge>> edges;
    private int numofedges;
    private int ModeCount;

    public WGraph_DS() {
        this.graph = new HashMap<>();
        this.edges = new HashMap<>();
        this.numofedges = 0;
        this.ModeCount = 0;
    }

    /**
     * return the node_info by the node_id,
     *
     * @param key - the node_id
     * @return the node_info by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        if (graph.containsKey(key))
            return graph.get(key);
        return null;
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (node1 != node2 && graph.containsKey(node1) && graph.containsKey(node2)) {
            if (edges.get(node1).containsKey(node2) && edges.get(node2).containsKey(node1))
                return true;
        }
        return false;
    }

    /**
     * return the weight of the edge (node1, node2). In case
     * there is no such edge - should return -1
     * runtime O(1).
     *
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2))
            return edges.get(node1).get(node2).getWeight();
        return -1;
    }

    /**
     * adds a new node to the graph with the given key.
     * runtime O(1).
     * Note2: if there is already a node with such a key -> no action should be performed.
     *
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (!graph.containsKey(key)) {
            graph.put(key, new Node(key));
            edges.put(key, new HashMap<>());
            ModeCount++;
        }
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * runtime O(1).
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (w >= 0) {
            if (node1 != node2 && graph.containsKey(node1) && graph.containsKey(node2)) {
                if (!edges.get(node1).containsKey(node2)) {
                    edges.get(node1).put(node2, new Edge(getNode(node2), w));
                    edges.get(node2).put(node1, new Edge(getNode(node1), w));
                    numofedges++;
                } else {
                    edges.get(node1).get(node2).setWeight(w);
                    edges.get(node2).get(node1).setWeight(w);
                }
                ModeCount++;
            }
        }
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: runtime O(1)
     *
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV() {
        return graph.values();
    }

    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * runtime O(k), k - being the degree of node_id.
     *
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (edges.containsKey(node_id)) {
            LinkedList<node_info> ni = new LinkedList<>();
            for (int x : edges.get(node_id).keySet()) {
                ni.add(graph.get(x));
            }
            return ni;
        }
        return null;
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * runtime O(n), |V|=n.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_info removeNode(int key) {
        if (graph.containsKey(key)) {
            Collection<node_info> ni = getV(key);
            for (node_info n : ni) {
                edges.get(n.getKey()).remove(key);
                numofedges--;
                ModeCount++;
            }
            edges.remove(key);
            ModeCount++;
            return graph.remove(key);
        }
        return null;
    }

    /**
     * Delete the edge from the graph,
     * runtime O(1).
     *
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            edges.get(node1).remove(node2);
            edges.get(node2).remove(node1);
            numofedges--;
            ModeCount++;
        }
    }

    @Override
    public int nodeSize() {
        return graph.size();
    }

    @Override
    public int edgeSize() {
        return numofedges;
    }

    @Override
    public int getMC() {
        return ModeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o.getClass() != WGraph_DS.class)
            return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        if (nodeSize() != wGraph_ds.nodeSize() || edgeSize() != wGraph_ds.edgeSize())
            return false;
        Iterator<node_info> nodes = getV().iterator();
        Iterator<node_info> nodesobj = wGraph_ds.getV().iterator();
        while (nodes.hasNext() && nodesobj.hasNext()) {
            node_info x = nodes.next();
            node_info y = nodesobj.next();
            if (!x.equals(y))
                return false;
            Iterator<node_info> ni = getV(x.getKey()).iterator();
            Iterator<node_info> niobj = wGraph_ds.getV(y.getKey()).iterator();
            while (ni.hasNext() && niobj.hasNext()) {
                node_info t = ni.next();
                node_info w = niobj.next();
                if (!t.equals(w))
                    return false;
                if (getEdge(x.getKey(), t.getKey()) != getEdge(y.getKey(), w.getKey()))
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, edges, numofedges);
    }


    private class Node implements node_info, Serializable {

        private String info;
        private int key;
        private double tag;

        public Node(int key) {
            this.info = "";
            this.key = key;
            this.tag = 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o.getClass() != Node.class)
                return false;
            Node node = (Node) o;
            return key == node.key &&
                    (Double.compare(node.tag, tag) == 0) &&
                    (info.equals(node.info));
        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public double getTag() {
            return tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }
    }

    private class Edge implements Serializable {

        private node_info dest;
        private double weight;

        /* represents an edge in the graph, with node_info of the dest, and weight*/
        public Edge(node_info dest, double weight) {
            this.dest = dest;
            this.weight = weight;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o.getClass() != Edge.class)
                return false;
            Edge edge = (Edge) o;
            return Double.compare(edge.weight, weight) == 0 && (dest.equals(edge.dest));
        }
    }
}