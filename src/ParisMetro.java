import java.io.BufferedReader;

import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Edge;
import net.datastructures.Entry;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Map;
import net.datastructures.Vertex;
import net.datastructures.Stack;
import net.datastructures.LinkedStack;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

public class ParisMetro {
	//Graph <Integer, Integer> parisGraph = new AdjacencyMapGraph<Integer, Integer>(false);
	static Graph<Integer, Integer> parisGraph;
	static Hashtable<Integer, Vertex> vertices;
	ArrayList<Vertex<Integer>> nextStations;
	Hashtable<Integer,Boolean> visited;
	int walkTime = 90;

	public ParisMetro(String fileName) throws Exception, IOException {
		parisGraph = new AdjacencyMapGraph<Integer, Integer>(true);
		vertices = new Hashtable<Integer, Vertex>();
		readMetro(fileName);
	}

	protected void readMetro(String fileName) throws Exception, IOException {
		BufferedReader graphFile = new BufferedReader(new FileReader(fileName));

		String line;
		while((line = graphFile.readLine()) != null) {
			if(line.contains("$")) {
				break;
			}
		}
		while ((line = graphFile.readLine()) != null) {
			createMetro(line);
		}
	}

	private void createMetro(String line) {
		StringTokenizer st = new StringTokenizer(line);
		if (st.countTokens() == 3) {
			Integer stationOne = new Integer(st.nextToken());
			Integer stationTwo = new Integer(st.nextToken());
			Integer weight = new Integer(st.nextToken());
			Vertex<Integer> source = vertices.get(stationOne);
			if (source == null) {
				source = parisGraph.insertVertex(stationOne);
				vertices.put(stationOne, source);
			}
			Vertex<Integer> dest = vertices.get(stationTwo);
			if (dest == null) {
				dest = parisGraph.insertVertex(stationTwo);
				vertices.put(stationTwo, dest);
			}
			if (parisGraph.getEdge(source, dest) == null) {
				if(weight == -1) {
					weight = walkTime;
				}
				Edge<Integer> e = parisGraph.insertEdge(source, dest, weight);
			}
		}
	}

	//PART 2 i) : Identifying all stations belonging in the same line.
	
	private void stationsDFS(Integer station) throws Exception {
		System.out.println("Given Station:\nN1 = " + station + "\nStations in this line:");
		Vertex<Integer> v = getVertex(station);
		visited = new Hashtable<Integer,Boolean>();
		printSubwayLine(parisGraph,v);
		return;
	}

	private void printSubwayLine(Graph<Integer,Integer> graph, Vertex<Integer> station) throws Exception {
		
		Iterable<Edge<Integer>> edges = parisGraph.outgoingEdges(station);
		if(visited.get(station.getElement()) != null) {
			return;
		}
		
		visited.put(station.getElement(), Boolean.TRUE);
		System.out.print(station.getElement().toString() + " ");
		
		for(Edge<Integer> e : edges) {
			if(e.getElement() != 90) {
				Vertex<Integer> next = parisGraph.opposite(station,e);
				printSubwayLine(parisGraph,next);
			}
		}
		return;
	}
	
	protected static Vertex<Integer> getVertex(Integer vert) throws Exception {
		for (Vertex<Integer> vs : parisGraph.vertices()) {
			if (vs.getElement().equals(vert)) {
				return vs;
			}
		}
		throw new Exception("Vertex not in graph: " + vert);
	}

	void print() {
		System.out.println("Vertices: " + parisGraph.numVertices() + " Edges: " + parisGraph.numEdges());

		System.out.println("STATION CONNECTIONS (vertices):");
		for (Vertex<Integer> vertex : parisGraph.vertices()) {
			System.out.println(vertex.getElement());
		}
		System.out.println("TIMES(edges):");
		for (Edge<Integer> edge : parisGraph.edges()) {
			System.out.println(edge.getElement());
		}
		return;
	}

	public static void printShortestPath(int v1, int v2) throws Exception{
		Vertex<Integer> vSource = getVertex(v1);
		GraphAlgorithms dj = new GraphAlgorithms();
		
		//create a map of shortest path lengths
		Map<Vertex<Integer>, Integer> forest = dj.shortestPathLengths(parisGraph, vSource);
		
		//create the tree
		Map<Vertex<Integer>, Edge<Integer>> tree = dj.spTree(parisGraph, vSource, forest);

		//if the verticies are the same, there is no path
		if(v1 == v2){
			return;
		}

		System.out.println("\nPaths: "+ v1 +" to " + v2);
		
		Vertex<Integer> v = getVertex(v2);
		
		int totalTime = forest.get(v);
		
		Stack<Integer> stack = new LinkedStack<Integer>();
				
		stack.push(v.getElement());
		
		while(tree.get(v)!=null){
			
			v = parisGraph.opposite(v, tree.get(v));
			//push the found station to the path list in the stack
			stack.push(v.getElement());

		}
		
		System.out.println("Time= "+ totalTime);
		System.out.print("Path= "); 
		while(!stack.isEmpty()){
			
			int qwerty = stack.pop();
			System.out.print(qwerty + " ");
		}
}

	protected static Vertex<Integer> getVertex(int v1) throws Exception {
		// Go through vertex list to find vertex -- why is this not a map
		for (Vertex<Integer> vs : parisGraph.vertices()) {
			if (vs.getElement().equals(v1)) {
				return vs;
				
			}
		}
		throw new Exception("Vertex not in graph: " + v1);
	}
	
	//main program method that accepts inputs, args[0], args[1], and args[2] from command line
	public static void main(String[] args) throws Exception{
		try {
		
			ParisMetro parisMetro = new ParisMetro("metro.txt");
			
			//check that there parameters given to the program
			if (args.length > 0){
				int numArgs = args.length;
				
				//2.i : Find and print all stations on a line given a source station.
				 if (args.length ==1){
						System.out.println(" ");
						parisMetro.stationsDFS(Integer.parseInt(args[0]));
						System.out.println(" ");
					}				
				 
				//2.iii : Find the shortest path and printing the total travel time between two source stations with out-of-service point. 
				//Note: This method is not implemented, so it displays a dummy message to the commandline
				if(args.length == 3){
					System.out.println("This method is not implemented. Please try the other available commands, from 2.i and 2.ii");
				}
				
				//2.ii : Find the shortest path and printing the total travel time between two source stations
				else if (args.length ==2){
					System.out.println(" ");
					printShortestPath(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
					System.out.println(" ");
				}
			}
			else
				System.out.println("No parameters were entered.");
			
			//catch any errors and print them
		} catch (Exception except) {
			System.err.println(except);
			except.printStackTrace();
		}
	}
}