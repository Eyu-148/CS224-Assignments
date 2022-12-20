import java.util.*;

public class Max_Flow_hw10 {
    // create Edge class to form graph
    static class Edge {
        // in the original graph, it equals to capacity
        // in residual graph, it equals to flow
        int flow;

        // Edge constructor
        public Edge(int flow) {
            this.flow = flow;
        }
    }

    // create directed graph class, using adjacency matrix as represtation
    static class Graph {
        // fields
        int nodes;
        Edge[][] graph;

        // constructor
        Graph(int numNodes) {
            this.nodes = numNodes;
            graph = new Edge[nodes][nodes];
            // initialize the adjacency matrix
            for (int i = 0; i < nodes; ++i) {
                for (int j = 0; j < nodes; ++j) {
                    graph[i][j] = new Edge(0);
                }
            }
        }

        // addEgde methods
        public void addEdge(int i, int j, int flow) {
            // update the elements in matrix. note: index should be -1
            graph[i - 1][j - 1].flow = flow;
        }

        // print method
        public void printGraph() {
            for (int i = 0; i < nodes; ++i) {
                System.out.print((i + 1) + ")  ");
                for (int j = 0; j < nodes; ++j) {
                    System.out.print(graph[i][j].flow + "   ");
                }
                System.out.print('\n');
            }
        }
    }

    // method to check if a path exists from s to t, and store the path in parent[]
    // modified from
    // https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
    // note: s & t passed in is 1 and 8 --> need to be -1 during process when s/t
    // represents indices
    static boolean isPath(Graph rg, int s, int t, int numNodes, int parents[]) {
        // initialize a vistied list
        boolean visited[] = new boolean[numNodes];
        for (int i = 0; i < numNodes; ++i) {
            visited[i] = false;
        }

        // enqueue start node and mark it as visited
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s - 1] = true;
        parents[s - 1] = -1; // start node

        // BFS loop
        while (queue.size() != 0) {
            int u = queue.poll();
            // search for the connected nodes to u
            for (int i = 0; i < numNodes; ++i) {
                if (!visited[i] && (rg.graph[u - 1][i].flow > 0)) {
                    // reach to sink
                    if (i + 1 == t) {
                        parents[i] = u;
                        return true;
                    }
                    queue.add(i + 1);
                    parents[i] = u;
                    visited[i] = true;
                }
            }
        }
        return false;
    }

    // method to find the bottleneck value in this path
    static int Augment(Graph gf, int parents[], int s, int t) {
        // search for the minimum flow value in the path
        int bottleneck = Integer.MAX_VALUE;
        for (int i = t; i != s; i = parents[i - 1]) {
            int capacity = gf.graph[parents[i - 1] - 1][i - 1].flow;
            bottleneck = Math.min(capacity, bottleneck);
        }
        // update flow value
        for (int i = t; i != s; i = parents[i - 1]) {
            // normal path
            gf.graph[parents[i - 1] - 1][i - 1].flow -= bottleneck;
            // residual path
            gf.graph[i - 1][parents[i - 1] - 1].flow += bottleneck;
        }
        // print the augmented path
        System.out.print("Augmented Path: ");
        for (int i = t; i != s; i = parents[i - 1]) {
            System.out.print(i + "<- ");
        }
        System.out.print(s + "    Flow Value: " + bottleneck + "\n");

        return bottleneck;
    }

    static int Max_Flow(Graph g, Graph gf, int s, int t) {
        // initialize a path list
        int parents[] = new int[8];
        int max_flow = 0;
        while (isPath(gf, s, t, 8, parents)) {
            int fv = Augment(gf, parents, s, t);
            max_flow += fv;
        }
        return max_flow;
    }

    public static void main(String[] args) {
        System.out.println("Using adjancency matrix to represent data >>>");
        // create the graph
        Graph G = new Graph(8);
        G.addEdge(1, 2, 10);
        G.addEdge(1, 3, 5);
        G.addEdge(1, 4, 15);
        G.addEdge(2, 3, 4);
        G.addEdge(2, 5, 9);
        G.addEdge(2, 6, 15);
        G.addEdge(3, 4, 4);
        G.addEdge(3, 6, 8);
        G.addEdge(4, 7, 30);
        G.addEdge(5, 6, 15);
        G.addEdge(5, 8, 10);
        G.addEdge(6, 7, 15);
        G.addEdge(6, 8, 10);
        G.addEdge(7, 3, 6);
        G.addEdge(7, 8, 10);

        // residual graph
        Graph Gf = new Graph(8);
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                Gf.graph[i][j] = G.graph[i][j];
            }
        }
        // G.printGraph();
        System.out.println("========================================================================\n");
        // Gf.printGraph();

        int mf = Max_Flow(G, Gf, 1, 8);
        System.out.println("Max Flow: " + mf);
    }
}
