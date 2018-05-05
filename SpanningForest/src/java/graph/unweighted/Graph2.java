import java.io.*;
import java.util.*;

// uses arrays of integers to represent Graph objects
public class Graph2
{

	int[] ancestors;
	int[][] edges;
	boolean[] inSF;
	int num_edges;

	public Graph2()
	{

		this.ancestors = new int[0];
		this.edges = new int[0][0];
		this.num_edges = 0;
		this.inSF = new boolean[0];

	}


	static Graph2 readEdgeGraph(String file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		Graph2 g = new Graph2();
		
		if(!"EdgeArray".equals(reader.readLine()))
			throw new IOException("invalid edge graph format");
		

		while(true)
		{
			String line;

			try
			{
			     line = reader.readLine();
			}catch(EOFException e){ break;}
			

			if(line == null)
				break;
			
			String[] words = line.split("[ \t]+");

			if(words.length != 2)
				throw new IOException("invalid edge graph format");
			
			int u = Integer.parseInt(words[0]);
			int v = Integer.parseInt(words[1]);
			
		
			g.init_ancestor(u);
			g.init_ancestor(v);
		
			g.add_edge(u,v);
			g.num_edges++;
			
		}
		g.inSF = new boolean[g.num_edges];
		for(int i = 0; i < g.num_edges; i++)
			g.inSF[i] = false;
		return g;
	
	}

	void add_edge(int u, int v)
	{
		/*
		if(edges.length <= u)
			edges = Arrays.copyOf(edges,u+1);
		if(edges[u] == null)
			edges[u] = new int[0];
		if(edges[u].length <= v)
			edges[u] = Arrays.copyOf(edges[u],v+1);
		if(edges[u][v] != 1)
			edges[u][v] = 1;
		*/
		if(edges.length <= u)
			edges = Arrays.copyOf(edges,u+1);
		if(edges[u] == null)
		{
			edges[u] = new int[1];
			edges[u][0] = -1;
		}
		if(edges[u][edges[u].length-1] != -1)
			edges[u] = Arrays.copyOf(edges[u],edges[u].length+1);
		if(edges[u][edges[u].length-1] != v)
			edges[u][edges[u].length-1] = v;
	}
	void init_ancestor(int n)
	{
		if(ancestors.length <= n)
			ancestors = Arrays.copyOf(ancestors, n+1);
		
		if(ancestors[n] != n)
			ancestors[n] = n;
	}
}
