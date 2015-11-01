package tsp;
import java.util.*;

public class Main {
	
	public static String matrix[][] = null;
	public static int sumOfIdentifiers = 0;
	public static Node smallest_node;
	public static Node head;

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
		
		matrix = new String[row][];

		
		for(int i = 0; i < row; i++)
		{
			System.out.println("Enter numbers seperated by commas only for row #" + (i+1)+ " of "+ row );
			String srow = myScanner.nextLine();
			matrix[i] = srow.split(",");
		}
		
		for(int i = 0; i < row; i++)
		{
			sumOfIdentifiers += i;
		}
		
		int total_distance = findMinTotalDistance(matrix, null);
		smallest_node = new Node(total_distance, null, -1, -1, row);
		head = smallest_node;
		head.currentPath = null;
		Graph graph = new Graph(smallest_node);
		while(smallest_node.solution == false)
		{			
			findChildren(smallest_node, graph);
			smallest_node = null;
			findSmallestNode(head);
		}
		
		System.out.print("Solution path: ");
		for(Integer p : smallest_node.feasableSolution)
		{
			System.out.print(p+ " ");
		}
	}
	
	static void findChildren(Node head, Graph graph)
	{


			if(head.siblings - 1 >= 2)
			{
				ArrayList<NodeMatrixPosition> children_edges = findValidNextEdges(head.currentPath, head);
				addChildrenToParent(children_edges, head);
			}
			else
			{
				findFeasablePath(head);
			}

			
	}
	
	static void findFeasablePath(Node end)
	{
		
		ArrayList<NodeMatrixPosition> proposed_path = new ArrayList<>(end.currentPath);
		
		ArrayList<NodeMatrixPosition> feasable_path = new ArrayList<>();
		
		feasable_path.add(proposed_path.get(0));
		proposed_path.remove(0);
		
		while(proposed_path.size() > 0)
		{
			boolean found_match = false;
			for(int i = 0; i < proposed_path.size(); i++)
			{
				found_match = false;
				NodeMatrixPosition lastEdge = feasable_path.get(feasable_path.size() - 1);
				if(lastEdge.vB == proposed_path.get(i).vA)
				{
					feasable_path.add(proposed_path.get(i));
					proposed_path.remove(i);
					found_match = true;
					break;
				}
			}
			
			if(found_match != true)
			{
				feasable_path.add(proposed_path.get(0));
				proposed_path.remove(0);
			}
		}
		end.solution = true;
		end.feasableSolution = parseToValidSolution(feasable_path);
		end.total_distance = calculateFeasablePathDistance(end);
	}
	
	static int calculateFeasablePathDistance(Node end)
	{
		int total_distance = 0;
		for(int i = 0; i < end.feasableSolution.size() -1; i++)
		{
			total_distance += Integer.parseInt(matrix[end.feasableSolution.get(i)][end.feasableSolution.get(i+1)]);
		}
		
		return total_distance;
	}
	
	static ArrayList<Integer> parseToValidSolution(ArrayList<NodeMatrixPosition> unparsed_path)
	{
		ArrayList<Integer> feasable_solution = new ArrayList<>();
		feasable_solution.add(new Integer(unparsed_path.get(0).vA));
		feasable_solution.add(new Integer(unparsed_path.get(0).vB));
		
		for(int i = 1; i < unparsed_path.size(); i++)
		{
			if(feasable_solution.get(feasable_solution.size() -1).intValue() != unparsed_path.get(i).vA)
			{
				feasable_solution.add(new Integer(unparsed_path.get(i).vA));
			}
			
			
			
			feasable_solution.add(new Integer(unparsed_path.get(i).vB));
		}
		int current_solution_sum = 0;
		for(Integer i : feasable_solution)
		{
			current_solution_sum += i.intValue();
		}
		
		if((sumOfIdentifiers - current_solution_sum) != 0)
			feasable_solution.add(new Integer(sumOfIdentifiers - current_solution_sum));
		feasable_solution.add(new Integer(0));
		
		return feasable_solution;
	}
	
	static void addChildrenToParent(ArrayList<NodeMatrixPosition> children_edges, Node parent)
	{

			for(NodeMatrixPosition child: children_edges)
			{
				Node childNode = new Node(-1, parent, child.vA, child.vB, parent.siblings-1);
				childNode.total_distance = findMinTotalDistance(matrix, childNode.currentPath);
				parent.children.add(childNode);		
			}

	}
	
	
	static ArrayList<NodeMatrixPosition> findValidNextEdges(ArrayList<NodeMatrixPosition> previous_edges, Node parent)
	{
		ArrayList<NodeMatrixPosition> returnValue = new ArrayList<>();
		

			for(int i = 0; i < Main.matrix.length; i++)
			{	
				boolean found_row = false;
				if(numberFoundInBannedArray(i, parent.currentPath, 0, -1))
					continue;
				for (int j = 0; j < Main.matrix.length; j++)
				{
					if(numberFoundInBannedArray(j, parent.currentPath, 2, i) || j == i)
					{
						continue;
					}
					
					if(parent!=null)
					if(parent.currentPath != null && parent.currentPath.get(parent.currentPath.size()-1).vB == j)
					{
						continue;
					}
					found_row = true;
					returnValue.add(new NodeMatrixPosition(i, j));
				}
			
				if(found_row = true)
					break;
			}
			
			System.out.println(returnValue.size());
			
		if(returnValue.size() != (parent.siblings - 1))
		{
			System.out.println(returnValue.size());
			System.out.println(parent.siblings);
			System.out.println("You fucked up");
		}
		return returnValue;
		
	}
	

	
	static void findSmallestNode(Node head)
	{
		if(head.children.size() == 0)
		{
			if(smallest_node == null || head.total_distance < smallest_node.total_distance)
			{
				smallest_node = head;
			}
			
			return;
		}
		
		for(Node children : head.children)
		{
			findSmallestNode(children);
		}
	}
	

	
	static int findMinTotalDistance(String[][] matrix, ArrayList<NodeMatrixPosition> current_path)
	{	
		int sum = 0;
		for(int i = 0; i < matrix.length; i++)
		{
			if(numberFoundInBannedArray(i, current_path, 0, -1))
			{
				continue;
			}
			int min = -1;
			for(int j = 0; j < matrix[0].length; j++)
			{
				if(numberFoundInBannedArray(j, current_path, 1, -1)|| !Character.isDigit(matrix[i][j].charAt(0)))
				{
					continue;
				}
				
				if(numberFoundInBannedArray(j, current_path, 2, i))
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
		if(current_path != null)
		for(NodeMatrixPosition p : current_path)
		{
			sum += Integer.parseInt(matrix[p.vA][p.vB]);
		}
		return sum;
			
	}
	
	static boolean numberFoundInBannedArray(int j, ArrayList<NodeMatrixPosition> bannedNumbers, int rowColTag, int i)
	{
		if(bannedNumbers != null)
		{
			if(rowColTag < 2)
			for(NodeMatrixPosition b : bannedNumbers)
			{
				int value;
				if(rowColTag == 0)
					value = b.vA;
				else
					value = b.vB;
				
				if(value == j)
					return true;
			}
			
			else
			{
				for(NodeMatrixPosition b : bannedNumbers)
				{
					if(b.vA == i && b.vB == j)
						return true;
					if (b.vB == i && b.vA == j)
						return true;
				}
			}
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
	int vA;
	int vB;

	NodeMatrixPosition(int a, int b)
	{
		vA = a;
		vB = b;
	}
	
}


class Node{
	boolean solution = false;
	ArrayList<Integer> feasableSolution = null;
	int total_distance = 0;
	int vA, vB;
	ArrayList<Node> children = null;
	ArrayList<NodeMatrixPosition> currentPath = new ArrayList<>();
	Node parent = null;
	int siblings = 0;
	
	Node(int d, Node parent, int vA, int vB, int siblings)
	{
		this.vA = vA;
		this.vB = vB;
		this.siblings = siblings;
		total_distance = d;
		this.parent = parent;
		if(parent != null && parent.currentPath != null)
			currentPath = new ArrayList<>(parent.currentPath);

		currentPath.add(new NodeMatrixPosition(vA, vB));
		children = new ArrayList<Node>();
		
	}
}