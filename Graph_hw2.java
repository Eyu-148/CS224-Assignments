import java.util.*;

// Created by Eyu Chen for CS224 programming assignment2
// This program should traverse a tree in BFS
// and output messages to show concept of the tree

public class Graph_hw2 {
	// fields
	static int numEdge;
	static LinkedList<Integer> adjList[];

	// constructor
	Graph_hw2(int numEdge) {
		this.numEdge = numEdge;
		// create the adjancency list
		adjList = new LinkedList[numEdge + 1];
		for (int i = 0; i <= numEdge; ++i) {
			// add one row of the adjancency list
			// each row is a linked list
			adjList[i] = new LinkedList<Integer>();
		}
	}

	// methods to add egdes into the graph
	// explanation: there exists an bi-direction edge between
	// node a and b
	void addEgde(int a, int b) {
		adjList[a].addLast(b);
		adjList[b].addLast(a);
	}

	// this method should create a adjancency list
	static void createAdjList() {
		// create a adajancency list using class
		Graph_hw2 graph = new Graph_hw2(11);
		// Inserting nodes and edges
		graph.addEgde(1, 2);
		graph.addEgde(1, 3);
		graph.addEgde(2, 3);
		graph.addEgde(2, 4);
		graph.addEgde(2, 5);
		graph.addEgde(3, 5);
		graph.addEgde(3, 7);
		graph.addEgde(3, 8);
		graph.addEgde(4, 5);
		graph.addEgde(5, 6);
		graph.addEgde(7, 8);
	}

	// BFS traversal construction
	static void bfs(int start) {
		// create a array to store the status of nodes
		// set the status for start node is true
		// and store it at L[0]
		// listBFS is an array of linked lists
		boolean discovered[] = new boolean[numEdge];
		discovered[start - 1] = true;
		LinkedList<Integer> listBFS[] = new LinkedList[6];
		for (int i = 0; i < 6; ++i) {
			listBFS[i] = new LinkedList<Integer>();
		}
		listBFS[0].add(start);
		// set the layer and the tree
		int layer = 0;
		String treeStructure = "";

		while (!listBFS[layer].isEmpty()) {
			// create a new empty linked list for the next layer
			listBFS[layer + 1] = new LinkedList<Integer>();
			// read through the layer i
			for (int i = 0; i < listBFS[layer].size(); ++i) {
				// find the node n in the layer i
				int n = listBFS[layer].get(i);
				// go back to adjancency list and find the list linked after n
				// read through that linked list
				for (int v : adjList[n]) {
					// set v discovered
					if (discovered[v - 1] == false) {
						discovered[v - 1] = true;
						System.out.println("Discovered:" + v);
						listBFS[layer + 1].add(v);
						treeStructure += "(" + n + ", " + v + ")  ";
					}

				}
			}
			++layer;
		}
		System.out.println("{ " + treeStructure + " }");
	}

	public static void main(String args[]) {
		createAdjList();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a start node (1-8): ");
		int startNode = scanner.nextInt();
		while (startNode > 0) {
			bfs(startNode);
			System.out.println("Enter a start node (1-8): ");
			startNode = scanner.nextInt();
		}

	}

}
