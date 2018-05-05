
import java.io.*;
import java.util.*;

public class Graph {
    
    static class Node {
        final int index;
        Node ancestor;
        
        Node(int index) {
            this.index = index;
            ancestor = this;
        }
    }
    
    static class Edge {
        final Node u;
        final Node v;
        boolean inSF = false;
        
        Edge(Node u, Node v) {
            super();
            this.u = u;
            this.v = v;
        }
    }
    ArrayList<Edge> edges;
    Node[] nodes;
    
    public Graph() {
        super();
        edges = new ArrayList<Graph.Edge>();
        nodes = new Node[0];
    }
    
    static Graph readEdgeGraph(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Graph g = new Graph();
        if (!"EdgeArray".equals(reader.readLine())) {
            throw new IOException("invalid edge graph format");
        }
        while (true) {
            String line;
            try {
                line = reader.readLine();
            } catch (EOFException e) {
                break;
            }
            if (line == null) {
                break;
            }
            String[] words = line.split("[ \t]+");
            if (words.length != 2) throw new IOException("invalid edge graph format");
            int u = Integer.parseInt(words[0]);
            int v = Integer.parseInt(words[1]);
            Node U = g.getVertex(u);
            Node V = g.getVertex(v);
            g.addEdge(U, V);
        }
        return g;
    }
    
    public Node getVertex(int n) {
        if (nodes.length < n) nodes = Arrays.copyOf(nodes, n + 1 + n / 2);
        if (nodes[n] == null) nodes[n] = new Node(n);
        return nodes[n];
    }
    
    public void addEdge(Node u, Node v) {
        edges.add(new Edge(u, v));
    }
}
