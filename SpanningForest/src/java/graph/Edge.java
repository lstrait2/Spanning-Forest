class Edge implements Comparable<Edge>
{
		final Node u, v;
		boolean inSF = false;
		double weight;
		
		Edge(Node u, Node v, double w)
		{
			this.u = u;
			this.v = v;
			this.weight = w;
		}

		public int compareTo(Edge other)
		{
			// return 0 if equal, < 0 if other is greater, and > 0 if this is greater
			if(this.weight > other.weight)
				return 1;
			if(this.weight < other.weight)
				return -1;
			return 0;

		}
}
