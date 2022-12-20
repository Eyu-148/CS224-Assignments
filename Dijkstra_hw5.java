import java.util.*;

// Created by Eyu Chen for cs224 hw5 programing assignment
public class Dijkstra_hw5 {

    // create node class
    static class Node {
        int source;
        int path;
        boolean visited;
        // the last node that offers a smaller path value
        Node parent;
        // vector to store the path
        Vector<Node> pred = new Vector<Node>();

        // constructor
        public Node(int source, int path, boolean visited) {
            this.source = source;
            this.path = path;
            this.visited = visited;
        }

        public void setPath(int path) {
            this.path = path;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }
    }

    // create Edge class to form graph
    static class Edge {
        // source --> the start node of the edge
        // destination --> the next node
        // weight --> weight value on the edge connects this node and the next
        Node source;
        Node destination;
        int weight;

        // Edge constructor
        public Edge(Node source, Node destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    // create directed graph class, using adjacency list as represtation
    static class Graph {
        int nodes;
        LinkedList<Edge>[] adjacencyList;

        // constructor
        Graph(int numNodes) {
            this.nodes = numNodes;
            adjacencyList = new LinkedList[nodes];
            // initialize the adjacency list
            for (int i = 0; i < nodes; ++i) {
                adjacencyList[i] = new LinkedList<>();
            }
        }

        // addEgde method
        public void addEgde(Node source, Node destination, int weight) {
            Edge edge = new Edge(source, destination, weight);
            adjacencyList[source.source - 1].addFirst(edge);
        }

        // printGraph method
        public void Print_Graph() {
            System.out.println("Graphs is represented using an adjacency list.");
            for (int i = 0; i < nodes; ++i) {
                LinkedList<Edge> list = adjacencyList[i];
                for (int j = 0; j < list.size(); ++j) {
                    System.out.println("node " + (i + 1) + " is connected to " +
                            (list.get(j).destination.source) + " with a weight of " +
                            list.get(j).weight);
                }
            }
        }
    }

    // Dijkstra implementation
    static void Dijkstra(Graph graph, Vector<Node> vertices, int sourceCode) {
        // create a priority queue using defined comparator
        PriorityQueue<Node> queue = new PriorityQueue<Node>(graph.nodes, new Comparator<Node>() {
            public int compare(Node a, Node b) {
                if (a.path < b.path)
                    return -1;
                if (a.path > b.path)
                    return 1;
                return 0;
            }
        });

        // initialize the queue
        for (int i = 0; i < graph.nodes; ++i) {
            queue.add(vertices.get(i));
        }
        // initialize the source node is 0
        vertices.get(sourceCode).setPath(0);
        // set the pred vector and its parent as itself
        vertices.get(sourceCode).pred.add(vertices.get(sourceCode));
        vertices.get(sourceCode).setParent(vertices.get(sourceCode));

        // loop until the queue is empty
        while (!queue.isEmpty()) {
            // extract the node with minimum path value
            Node u = extractMin(queue);
            u.visited = true;

            // print message of the extracted node and its shortest path
            System.out.print("Node " + u.source + " included in S with the shortest path length " +
                    u.path + " on the path ");
            for (int i = 1; i < u.pred.size(); ++i) {
                System.out.print(u.pred.get(i).source + " -> ");
            }
            System.out.print(u.source);
            System.out.println(" ");

            // for every node has edge with u, update their keys (path value, parent, pred
            // vector)
            for (Edge e : graph.adjacencyList[u.source - 1]) {
                if (!e.destination.visited) {
                    if (e.source.path + e.weight < e.destination.path) {
                        int newPath = e.source.path + e.weight;
                        Node newParent = u;
                        changeKey(queue, newPath, newParent, e.destination);
                    }
                }
            }
        }
    }

    // method to extract the minimum value in priority queue
    static Node extractMin(PriorityQueue<Node> q) {
        return q.poll();
    }

    // update the node's keys
    static void changeKey(PriorityQueue<Node> q, int p, Node par, Node d) {
        // remove the old Node object
        q.remove(d);
        // create a new node with the original source and new path
        d.setPath(p);
        d.setParent(par);
        q.add(d);
        // reset the shortest path and declare the new --> the shortest path + its
        // parent
        d.pred.clear();
        d.pred.addAll(par.pred);
        d.pred.add(par);
    }

    // main function
    public static void main(String[] args) {
        // create graph and print it
        int numNodes = 8;
        Vector<Node> vertices = new Vector<Node>();
        Node node1 = new Node(1, Integer.MAX_VALUE, false);
        vertices.add(node1);
        Node node2 = new Node(2, Integer.MAX_VALUE, false);
        vertices.add(node2);
        Node node3 = new Node(3, Integer.MAX_VALUE, false);
        vertices.add(node3);
        Node node4 = new Node(4, Integer.MAX_VALUE, false);
        vertices.add(node4);
        Node node5 = new Node(5, Integer.MAX_VALUE, false);
        vertices.add(node5);
        Node node6 = new Node(6, Integer.MAX_VALUE, false);
        vertices.add(node6);
        Node node7 = new Node(7, Integer.MAX_VALUE, false);
        vertices.add(node7);
        Node node8 = new Node(8, Integer.MAX_VALUE, false);
        vertices.add(node8);

        Graph graph = new Graph(numNodes);
        graph.addEgde(node1, node2, 9);
        graph.addEgde(node1, node4, 14);
        graph.addEgde(node1, node3, 15);
        graph.addEgde(node2, node7, 23);
        graph.addEgde(node3, node5, 20);
        graph.addEgde(node3, node8, 44);
        graph.addEgde(node4, node3, 5);
        graph.addEgde(node4, node5, 30);
        graph.addEgde(node4, node7, 18);
        graph.addEgde(node5, node6, 11);
        graph.addEgde(node5, node8, 16);
        graph.addEgde(node6, node7, 6);
        graph.addEgde(node6, node8, 6);
        graph.addEgde(node7, node5, 2);
        graph.addEgde(node7, node8, 19);
        graph.Print_Graph();

        // Dijkstra implementation
        Dijkstra(graph, vertices, 0);
    }
}
