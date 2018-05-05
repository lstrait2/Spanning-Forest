import java.util.*;
import java.io.*;
public class Kruskal
{

	public static void buildMSF(WeightedGraph g)
        {
		// Init disjoint sets structure
		Dsets dsets = new Dsets(g.nodes.length);
		// Init Priority Queue of all edges in graph
		PriorityQueue<Edge> pq = new PriorityQueue<>(g.edges.size());
		for(int i = 0; i < g.edges.size(); i++)
			pq.add(g.edges.get(i));

		int MSF_size = 0;
		System.out.println(g.nodes.length);
		while(pq.size() != 0 && MSF_size < g.edges.size() - 1)
		{
			//System.out.println(MSF_size);
			Edge e = pq.poll();
			Node u = e.u;
			Node v = e.v;
			// vertices are in same set, continue to prevent cycle
			if(dsets.find(u.index) == dsets.find(v.index))
				continue;
			// add e to MSF
			e.inSF = true;
			// merge two vertice from e in dsets
			dsets.merge(u.index, v.index);
			MSF_size++;
		}
	}
     
	public static void main(String[] args) throws IOException
	{
		WeightedGraph g = WeightedGraph.readWeightedEdgeGraph(args[0]);
		long t1 = System.nanoTime();
		buildMSF(g);
		long t2 = System.nanoTime();
		System.out.println("SF build time = " + ((double)(t2-t1))/1000000000 + " s");
		getMSF(g);

	}

	private static void getMSF(final WeightedGraph g)
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
