###Weighted Undirected Graph Implementation

- This Library contains implementations of Undirected Weighted Graph structure.

- WGraph_DS class:
- I've used  2 HashMap's in the object- 
   the first called graph- is a nodes list of the graph(node key<-->node_info)
   the second called edges- contains edges list for each node(key<-->(target key<-->Edge object)).
- ModeCount - counts the changes made on the graph.
- Edge object- internal class inside WGraph_DS represents an edge in the graph- contains dest node_info and edge weight. 
- Node object- consists of several different variables - it's own unique key, a string to store info, and a tag to use by algorithms.

- WGraph_DSAlgo - has graph para in it, used to apply algorithms on the graph.
Algorithms implemented inside: object initialization, graph copy, connected-graph check, distance between to connected nodes at the graph+the path itself,
export the graph as a file to some folder, loading a file to initialize the graph.

-WeightComp
a comparator ive implemented and built in order to be able to implement PriorityQueue.

ive based my JUnit Tests on the tests who were on our course's gitHub.

I was inspired and used some of these algorithms i've found online: 
https://www.geeksforgeeks.org/shortest-path-unweighted-graph/
https://www.geeksforgeeks.org/serialization-in-java/
https://www.geeksforgeeks.org/priority-queue-class-in-java-2/