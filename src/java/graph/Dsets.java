// Disjoint Sets data structure
public class Dsets
{
	public int[] elements;
	
	public Dsets(int num)
	{
		this.elements = new int[num];

		for(int i = 0; i < num; i++)
			this.elements[i] = -1;

	}

	public int find(int elem)
	{
		if(this.elements[elem] < 0)
			return elem;
		
		return elements[elem] = find(elements[elem]);
	}

	public void merge(int a, int b)
	{
		int r1 = find(a);
		int r2 = find(b);
	
		if(r1 == r2)
			return; // already merged

		if(elements[r1] <= elements[r2])
		{
			elements[r1] += elements[r2];
			elements[r2] = r1;
				
		}
		else
		{
			elements[r2] += elements[r1];	
			elements[r1] = r2;

		}
	}

}
