import java.util.*;
import java.io.*;

class SpanningForest
{
	
	private static Graph.Node ancestorOf(final Graph.Node u, HashMap<Graph.Node, Graph.Node> newAncestors)
	{
		Graph.Node newAncestor;
		
		if(u.ancestor != u)
		{
			return u.ancestor;
		}
		else if( (newAncestor =  newAncestors.get(u)) != null )
		{
			return newAncestor;
		}
		
		return u;
	}

	public static void main(String[] args) throws IOException
	{
		Graph g = Graph.readEdgeGraph(args[0]);
		long t1 = System.nanoTime();
		buildSF(g.edges);
		long t2 = System.nanoTime();
		System.out.println("SF build time = " + ((double)(t2-t1))/1000000000 + " s");
		getSF(g);
	}

	private static void buildSF(ArrayList<Graph.Edge> edges_)
	{
		Graph.Edge[] edges = new Graph.Edge[edges_.size()];
		for(int i = 0; i < edges_.size(); i++)
			edges[i] = edges_.get(i);
		Graph.Node[] u_ancestors = new Graph.Node[edges.length];
		Graph.Node[] v_ancestors = new Graph.Node[edges.length];
		HashMap<Graph.Node, Graph.Node> newAncestors = new HashMap<>();
		for(int i = 0; i < edges.length; i++)
		{
			Graph.Edge e = edges[i];		
			Graph.Node u_ancestor = e.u;
			Graph.Node v_ancestor = e.v;
			Graph.Node nextAncestor;
			while( (nextAncestor = ancestorOf(u_ancestor, newAncestors)) != u_ancestor)
				u_ancestor = nextAncestor;
			while( (nextAncestor = ancestorOf(v_ancestor, newAncestors)) != v_ancestor)
				v_ancestor = nextAncestor;
			if(u_ancestor == v_ancestor)
				continue;
			if(u_ancestor.index < v_ancestor.index)
			{
				Graph.Node temp = u_ancestor;
				u_ancestor = v_ancestor;
				v_ancestor = temp;
			}
			u_ancestors[i] = u_ancestor;
			v_ancestors[i] = v_ancestor;
			newAncestors.put(u_ancestor, v_ancestor);
		}
		for(int i = 0; i < edges.length; i++)
		{
			final Graph.Node u_ancestor = u_ancestors[i];
			final Graph.Edge edge = edges[i];
			if(u_ancestor != null)
			{
				u_ancestor.ancestor = v_ancestors[i];
				edge.inSF = true;

			}
		}
	}

	private static void getSF(final Graph g)
	{
		int inSF = 0;
		for(int i = 0; i < g.edges.size(); i++)
		{
			if(g.edges.get(i).inSF)
				inSF++;
		}
		System.out.println(inSF + " edges in spanning forest");
	}

}
