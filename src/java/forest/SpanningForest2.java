import java.util.*;
import java.io.*;

public class SpanningForest2
{

	private static int ancestorOf(Graph2 g, int u, HashMap<Integer, Integer> newAncestors)
	{
		int newAncestor;
		
		if(g.ancestors[u] != u)
		{
			return g.ancestors[u];
		}
		else if( newAncestors.get(u) != null )
		{
			newAncestor = newAncestors.get(u);
			return newAncestor;
		}
		
		return u;
	}

	public static void main(String[] args) throws IOException
	{
		Graph2 g = Graph2.readEdgeGraph(args[0]);
		long t1 = System.nanoTime();
		buildSF(g);
		long t2 = System.nanoTime();
		System.out.println("SF build time = " + ((double)(t2-t1))/1000000000 + " s");
		getSF(g);
	}
	
	private static void buildSF(Graph2 g)
	{

		int[] u_ancestors = new int[g.num_edges];
		int[] v_ancestors = new int[g.num_edges];
		for(int i = 0; i < u_ancestors.length; i++)
		{
			u_ancestors[i] = -1;
			v_ancestors[i] = -1;

		}
		HashMap<Integer, Integer> newAncestors = new HashMap<>();
		
		int curr = 0; // keep track of current position in num_edges
		for(int i = 0; i < g.edges.length; i++)
		{
			if(g.edges[i] == null) // node w/ index i has no edges
				continue;
			for(int j = 0; j < g.edges[i].length; j++)
			{
				if(g.edges[i][j] == -1) // i,j is not an edge
					continue;
				int u_ancestor = i;
				int v_ancestor = g.edges[i][j];
		
	
				int nextAncestor;
			
				while( (nextAncestor = ancestorOf(g,u_ancestor, newAncestors)) != u_ancestor)
					u_ancestor = nextAncestor;

				while( (nextAncestor = ancestorOf(g,v_ancestor, newAncestors)) != v_ancestor)
					v_ancestor = nextAncestor;

				if(u_ancestor == v_ancestor)
					continue;
		
				if(u_ancestor < v_ancestor)
				{
					int temp = u_ancestor;
					u_ancestor = v_ancestor;
					v_ancestor = temp;
				}
			
				u_ancestors[curr] = u_ancestor;
				v_ancestors[curr] = v_ancestor;
				newAncestors.put(u_ancestor, v_ancestor);
				curr++;
			}
		}
		
		for(int i = 0; i < g.num_edges; i++)
		{
			int u_ancestor = u_ancestors[i];
			if(u_ancestor != -1)
			{
				g.ancestors[u_ancestor] = v_ancestors[i];
				g.inSF[i] = true;
			}
		}
	}

	private static void getSF(final Graph2 g)
	{
		int inSF = 0;
		for(int i = 0; i < g.inSF.length; i++)
		{
			if(g.inSF[i])
				inSF++;
		}

		System.out.println(inSF + " edges in spanning forest");
	}

}
