package tsp;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		
		int row = 0; 
		int col = 0;
		System.out.println("Current version only supports square matrices.");
		System.out.println("Input matrix dimension, e.g. 4x4: ");
		Scanner myScanner = new Scanner(System.in);
		String dim = myScanner.nextLine();
		dim = new String(dim.toLowerCase());
		String[] dims = dim.split("x");
		row = Integer.parseInt(dims[0]);
		col = Integer.parseInt(dims[1]);
		
		String matrix[][] = new String[row][];

		
		for(int i = 0; i < row; i++)
		{
			matrix[i] = new String[col];
			System.out.println("Enter numbers seperated by commas only for row #" + (i+1)+ " of "+ row );
			String srow = myScanner.nextLine();
			matrix[i] = srow.split(",");
		}
		int total_distance = findMinTotalDistance(matrix, null, null);
		Node smallest_node = new Node(-1,-1,total_distance, null);
		
		boolean solution = false;
		while(solution == false)
		{
			
		}
		
	}
	

	
	static int findMinTotalDistance(String[][] matrix, ArrayList<Integer> bannedRows, ArrayList<Integer> bannedCols)
	{	
		int sum = 0;
		for(int i = 0; i < matrix.length; i++)
		{
			if(numberFoundInBannedArray(i, bannedRows))
			{
				continue;
			}
			int min = -1;
			for(int j = 0; j < matrix[0].length-1; j++)
			{
				if(numberFoundInBannedArray(j, bannedCols)|| !Character.isDigit(matrix[i][j].charAt(0)))
				{
					continue;
				}
				
				if(min > Integer.parseInt(matrix[i][j]) || min == -1)
				{
					min = Integer.parseInt(matrix[i][j]);
				}
			}
			sum += min;
		}
		
		return sum;
			
	}
	
	static boolean numberFoundInBannedArray(int i, ArrayList<Integer> bannedNumbers)
	{
		if(bannedNumbers != null)
		{
			for(int j : bannedNumbers)
				if(j == i)
					return true;
		}
		
		return false;
	}
	
	

}

class Graph
{
	Node headNode = null;
	
	Graph(Node head)
	{
		headNode = head;

	}
	
	void addChildren(Node n, Node child)
	{
		n.children.add(child);
	}
}

class NodeMatrixPosition
{
	int row;
	int col;
	
}


class Node{
	int total_distance = 0;
	int node_a;
	int node_b;
	ArrayList<Node> children = null;
	Node parent = null;
	
	Node(int a, int b, int d, Node parent)
	{
		node_a = a;
		node_b = b;
		total_distance = d;
		this.parent = parent;
		children = new ArrayList<Node>();
	}
}