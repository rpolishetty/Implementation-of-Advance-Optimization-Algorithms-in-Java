// Four Search Strategies are used to find the solution. These are BreadthFirst Search, DepthFirst Search, Greedy Best First Search
// A* Search Algorithms
// To test different test cases just change the name of test file under main() function
// To implement Different Search algorithm. Uncomment desired algorithm and comment all other search algorithm under main() function
import java.io.*;
import java.util.*;
class maze
{
	private int MaxRow;
	private int MaxCol;
	private char map[][];
	private List<node> nodes;
	private node StartNode;
	private node GoalNode;
	private queue Queue;
	private queue Solution;
	private int no_of_node;
	maze(String filename)
	{
	try{
		File f = new File(filename);
		if(!f.exists()) System.exit(1);
		//Read File
		readFile(f);
		genSearchSpace();
//		for(int i=0;i<nodes.size();i++)
//			map[nodes.get(i).getRow()][nodes.get(i).getCol()]='X';
//		displayMaze();
		
	   }
	catch(Exception e)
	   {
		System.out.println(e);
	   }
	}
	public void displayMaze()
	{
		for(int i=0;i<MaxRow;i++)
			System.out.println(new String(map[i]));
	}
	public static void main(String arg[])
	{
//		System.out.println("Hello");
		maze m = new maze("testcase1.maze"); //Test File Name... All Test cases are text file. testcase1.maze testcase2.maze testcase3.maze
		queueNode sol,p=null;
		long t1,t2;
		m.displayMaze();
		t1 = System.nanoTime();
	//Search Algorithms - Caution Only one algorithm can work in one go.
		//sol = m.getByGreedySearch();
		//sol = m.getByBreadthFirstSearch();
		//sol = m.getByDepthFirstSearch();
		sol = m.getByA_Star_Search();
	//End of Search Algorithms
		t2 = System.nanoTime();
		System.out.println("No Of Nodes Traversed : " + m.no_of_node);
		System.out.println("Path Cost To Reach Solution : " + sol.PathCost);
		System.out.println("Time Elapsed : " + (t2 - t1));
		p = sol;
		while(p!=null && p.parentNode != null)
		{
			m.map[p.item.getRow()][p.item.getCol()] = '+';
			if(p.parentNode.item.getRow() == p.item.getRow())
			{
				if(p.parentNode.item.getCol() < p.item.getCol())
				{
					for(int i=p.parentNode.item.getCol(); i<p.item.getCol(); i++)
						m.map[p.item.getRow()][i] = '+';
				}
				else
				{
					for(int i=p.item.getCol(); i<p.parentNode.item.getCol(); i++)
						m.map[p.item.getRow()][i] = '+';
				}
			}
			if(p.parentNode.item.getCol() == p.item.getCol())
			{
				if(p.parentNode.item.getRow() < p.item.getRow())
				{
					for(int i=p.parentNode.item.getRow(); i<p.item.getRow(); i++)
						m.map[i][p.item.getCol()] = '+';
				}
				else
				{
					for(int i=p.item.getRow(); i<p.parentNode.item.getRow(); i++)
						m.map[i][p.item.getCol()] = '+';
				}
			}
			p = p.parentNode;
		}
		for(int i=0;i<m.MaxRow;i++)
			System.out.println(new String(m.map[i]));
	}
	public void readFile(File f) throws IOException
	{
		Reader fr = new BufferedReader(new FileReader(f));
		MaxCol = 0;
		fr.mark(1000);
		while(fr.read() != '\n')
			MaxCol++;
		fr.reset();
		char arr[] = new char[MaxCol];
		MaxRow = 0;
		while(fr.read(arr)==MaxCol)
				MaxRow++;
		map = new char[MaxRow][MaxCol];
		int i=0,j=0;
		fr.reset();
		while(true)
		{
			char ch;
			ch = (char)fr.read();
			if(ch==(char)-1) break;
			if(ch=='\n')
				{i++;j=0;}
			else
			{
				if(i<MaxRow)
				    map[i][j++]=ch;
			}
		}
	}
	public void genSearchSpace()
	{
		int row,col,degree;
		node up,down,left,right;
		nodes = new ArrayList<node>();
		GoalNode = null;
		StartNode = null;
		for(row=0;row<MaxRow;row++)
		{
			for(col=0;col<MaxCol;col++)
			{
				int cS = 0, rS = 0;
				if(map[row][col]!='%')    //Its Either a Path, Start Pos or End Pos
				{
					if(col-1>=0 && map[row][col-1] != '%')  cS++;
					if(col+1<MaxCol && map[row][col+1] != '%')  cS++;
					if(row-1>=0 && map[row-1][col] != '%')  rS++;
					if(row+1<MaxRow && map[row+1][col] != '%')  rS++;
					degree = cS + rS;
					if(map[row][col]=='.' || map[row][col]=='P' || map[row][col]=='p')
					{
						node n = new node(row,col,degree);
						nodes.add(n);
						continue;
					}
					if((cS==2 && rS==0) || (cS==0 && rS==2)) continue;  //Its a Straight Path
					node n = new node(row,col,degree);
					nodes.add(n);
				}
			}
		}
		System.out.println("No of Nodes : " + nodes.size());
		node p,q;
		for(int i=0;i<nodes.size();i++)
		{
			p = nodes.get(i);
			row = p.getRow();
			col = p.getCol();
			degree = 0;
			up=null;down=null;left=null;right=null;
			for(int j=0;j<nodes.size();j++)
			{
				q = nodes.get(j);
				if(q.getCol()==col)
				{
					if(row-1>=0 && map[row-1][col]!='%' && q.getRow() < row)
					{
						if(up==null) up=q;
						else
						{
							if(up.getRow()<q.getRow()) up=q;
						}
					}
					if(row+1<MaxRow && map[row+1][col]!='%' && q.getRow() > row)
					{
						if(down==null) down=q;
						else
						{
							if(down.getRow()>q.getRow()) down=q;
						}
					}
				}
				if(q.getRow()==row)
				{
					if(col-1>=0 && map[row][col-1]!='%' && q.getCol() < col)
					{
						if(left==null) left=q;
						else
						{
							if(left.getCol()<q.getCol()) left=q;
						}
					}
					if(col+1<MaxCol && map[row][col+1]!='%' && q.getCol() > col)
					{
						if(right==null) right=q;
						else
						{
							if(right.getCol()>q.getCol()) right=q;
						}
					}
				}
			}

			//Debugging Code
			/*
			if(up!=null) degree++;
			if(left!=null) degree++;
			if(right!=null) degree++;
			if(down!=null) degree++;
			if(degree!=p.getDegree())
			{
				System.out.println("false" + degree + " but " + p.getDegree() + "in node " + i + up + down + left + right);
				map[row][col]='P';
			}*/
			//
			if(up!=null) p.setUp(up, Math.abs(p.getRow() - up.getRow()));
			if(left!=null) p.setLeft(left, Math.abs(p.getCol() - left.getCol()));
			if(down!=null) p.setDown(down, Math.abs(down.getRow() - p.getRow()));
			if(right!=null) p.setRight(right, Math.abs(right.getCol() - p.getCol()));
			if(map[row][col]=='P' || map[row][col]=='p') StartNode = p;
			if(map[row][col]=='.') GoalNode = p;
		}
	}
	public queueNode getByBreadthFirstSearch()
	{
		Queue = new queue(StartNode);
		queueNode n;
		List<node> ar;
		List<node> visited = new ArrayList<node>();
		boolean flag = false;
		no_of_node = 0;
		while(true)
		{
			if(Queue.isEmpty()){ /*System.out.println("Queue is Empty");*/return null;}
			n = Queue.removeFront();
			no_of_node++;
			//System.out.println("Processing (" + n.item.getRow() + "," + n.item.getCol() + ")");
			if(n.item==GoalNode) {/*System.out.println("Found");*/return n;}
			ar = n.item.expand();
			visited.add(n.item);
			for(int i=0;i<ar.size();i++)
			{
				//System.out.println("\tExpand to (" + ar.get(i).getRow() + "," + ar.get(i).getCol() + ")");
				flag=false;
				for(int j=0;j<visited.size();j++)
				{
					if(visited.get(j)==ar.get(i)) {flag=true;break;}
				}
				if(flag) continue;
				Queue.enqueueAtEnd(ar.get(i),n);
				//System.out.println("\tEnqueueing (" + ar.get(i).getRow() + "," + ar.get(i).getCol() + ")");
			}
		}
	}
	public queueNode getByDepthFirstSearch()
	{
		Queue = new queue(StartNode);
		queueNode n;
		List<node> ar;
		List<node> visited = new ArrayList<node>();
		boolean flag = false;
		no_of_node = 0;
		while(true)
		{
			if(Queue.isEmpty()){ /*System.out.println("Queue is Empty");*/return null;}
			n = Queue.removeFront();
			no_of_node++;
//			System.out.println("Processing (" + n.item.getRow() + "," + n.item.getCol() + ")");
			if(n.item==GoalNode) {/*System.out.println("Found")*/;return n;}
			ar = n.item.expand();
			visited.add(n.item);
			for(int i=0;i<ar.size();i++)
			{
//				System.out.println("\tExpand to (" + ar.get(i).getRow() + "," + ar.get(i).getCol() + ")");
				flag=false;
				for(int j=0;j<visited.size();j++)
				{
					if(visited.get(j)==ar.get(i)) {flag=true;break;}
				}
				if(flag) continue;
				Queue.enqueueAtBeginning(ar.get(i),n);
//				System.out.println("\tEnqueueing (" + ar.get(i).getRow() + "," + ar.get(i).getCol() + ")");
			}
		}
	 }
	public queueNode getByGreedySearch()
	{
		Queue = new queue(StartNode,GoalNode);
		queueNode n;
		List<node> ar;
		List<node> visited = new ArrayList<node>();
		boolean flag = false;
		no_of_node = 0;
		while(true)
		{
			if(Queue.isEmpty()){ /*System.out.println("Queue is Empty");*/return null;}
			//Queue.displayQueue();
			n = Queue.removeFront();
			no_of_node++;
			//System.out.println("\nProcessing (" + n.item.getRow() + "," + n.item.getCol() + ")");
			if(n.item==GoalNode) {/*System.out.println("Found");*/return n;}
			ar = n.item.expand();
			visited.add(n.item);
			for(int i=0;i<ar.size();i++)
			{
				//System.out.println("\tExpand to (" + ar.get(i).getRow() + "," + ar.get(i).getCol() + ")");
				if(n.parentNode!=null && n.parentNode.item == ar.get(i)) continue;
				Queue.enqueueByHeuristicValue(ar.get(i),n,GoalNode);
				//System.out.println("\tEnqueueing (" + ar.get(i).getRow() + "," + ar.get(i).getCol() + ")");
			}
		}
	}
	public queueNode getByA_Star_Search()
	{
		Queue = new queue(StartNode,GoalNode);
		queueNode n;
		List<node> ar;
		List<node> visited = new ArrayList<node>();
		boolean flag = false;
		no_of_node = 0;
		while(true)
		{
			if(Queue.isEmpty()){ /*System.out.println("Queue is Empty");*/return null;}
			//Queue.displayQueue();
			n = Queue.removeFront();
			no_of_node++;
			//System.out.println("\nProcessing (" + n.item.getRow() + "," + n.item.getCol() + ")");
			if(n.item==GoalNode) {/*System.out.println("Found");*/return n;}
			ar = n.item.expand();
			visited.add(n.item);
			for(int i=0;i<ar.size();i++)
			{
				//System.out.println("\tExpand to (" + ar.get(i).getRow() + "," + ar.get(i).getCol() + ")");
				if(n.parentNode!=null && n.parentNode.item == ar.get(i)) continue;
				Queue.enqueueByTotalCost(ar.get(i),n,GoalNode);
				//System.out.println("\tEnqueueing (" + ar.get(i).getRow() + "," + ar.get(i).getCol() + ")");
			}
		}
	}
}
class node
{
	private int row;
	private int col;
	private int degree;
	public node up;
	public node down;
	public node left;
	public node right;
	public int upPathCost;
	public int downPathCost;
	public int rightPathCost;
	public int leftPathCost;
	node(int X,int Y, int Deg)
	{
		row = X;
		col = Y;
		degree = Deg;
		up = null;
		down = null;
		left = null;
		right = null;
	}
	public int getRow(){return row;}
	public int getCol(){return col;}
	public int getDegree(){return degree;}
	public void setUp(node n,int pathCost){up = n; upPathCost = pathCost;}
	public void setDown(node n,int pathCost){down = n; downPathCost = pathCost;}
	public void setLeft(node n,int pathCost){left = n; leftPathCost = pathCost;}
	public void setRight(node n,int pathCost){right = n; rightPathCost = pathCost;}
	public List<node> expand()
	{
		List<node> n = new ArrayList<node>();
		if(down!=null) n.add(down);
		if(up!=null) n.add(up);
		if(right!=null) n.add(right);
		if(left!=null) n.add(left);
		return n;
	}
}
class queue
{
	private queueNode Front;
	private queueNode Rear;
	queue(node start)
	{
		Front = new queueNode(start,null,null);
		Rear = Front;
	}
	queue(node start, node GoalState)
	{
		int hValue = 0;
		hValue = Math.abs(start.getRow() - GoalState.getRow()) + Math.abs(start.getCol() - GoalState.getCol());		
		Front = new queueNode(start,null,null,0,0,hValue);
		Rear = Front;	
	}
	public void enqueueAtBeginning(node n, queueNode parent)
	{
		int pCost = 0;
		if(n.up == parent.item) pCost = parent.PathCost + n.upPathCost;
		if(n.down == parent.item) pCost = parent.PathCost + n.downPathCost;
		if(n.left == parent.item) pCost = parent.PathCost + n.leftPathCost;
		if(n.right == parent.item) pCost = parent.PathCost + n.rightPathCost;
		
		queueNode q = new queueNode(n,Front,parent,0,pCost,0);
		Front = q;
	}
	public void enqueueAtEnd(node n, queueNode parent)
	{
		int pCost = 0;
		if(n.up == parent.item) pCost = parent.PathCost + n.upPathCost;
		if(n.down == parent.item) pCost = parent.PathCost + n.downPathCost;
		if(n.left == parent.item) pCost = parent.PathCost + n.leftPathCost;
		if(n.right == parent.item) pCost = parent.PathCost + n.rightPathCost;
		
		queueNode q = new queueNode(n,null,parent,0,pCost,0);
		Rear.next = q;
		Rear = q;
		if(Front == null) Front = q;
	}
	public queueNode removeFront()
	{
		queueNode n;
		n = Front;
		Front = Front.next;
		return n;
	}
	public boolean isEmpty()
	{
		if(Front==null) return true;
		else return false;
	}
	public void enqueueByHeuristicValue(node n, queueNode parent, node GoalState)
	{
		int hValue = 0;
		hValue = Math.abs(n.getRow() - GoalState.getRow()) + Math.abs(n.getCol() - GoalState.getCol());
		int pCost = 0;
		if(n.up == parent.item) pCost = parent.PathCost + n.upPathCost;
		if(n.down == parent.item) pCost = parent.PathCost + n.downPathCost;
		if(n.left == parent.item) pCost = parent.PathCost + n.leftPathCost;
		if(n.right == parent.item) pCost = parent.PathCost + n.rightPathCost;
		queueNode q,prev;
		prev = null;
		q = Front;
		while(q!=null)
		{
			if(hValue < q.h_value) break;
			prev = q;
			q = q.next;
		}
		if(prev == null)
		{
			queueNode a = new queueNode(n,Front,parent,0,pCost,hValue);
			Front = a;
		}
		else
		{
			queueNode a = new queueNode(n,q,parent,0,pCost,hValue);
			prev.next = a;			
		}
	}
	public void enqueueByTotalCost(node n, queueNode parent, node GoalState)
	{
		int hValue = 0, pCost = 0, totalCost = 0;
		hValue = Math.abs(n.getRow() - GoalState.getRow()) + Math.abs(n.getCol() - GoalState.getCol());
		if(n.up == parent.item) pCost = parent.PathCost + n.upPathCost;
		if(n.down == parent.item) pCost = parent.PathCost + n.downPathCost;
		if(n.left == parent.item) pCost = parent.PathCost + n.leftPathCost;
		if(n.right == parent.item) pCost = parent.PathCost + n.rightPathCost;
		totalCost = hValue + pCost;
		//if(parent.PathCost > totalCost) totalCost = parent.PathCost;
				
		queueNode q,prev;
		prev = null;
		q = Front;
		while(q!=null)
		{
			if(totalCost < q.TotalCost) break;
			prev = q;
			q = q.next;
		}
		if(prev == null)
		{
			queueNode a = new queueNode(n,Front,parent,totalCost,pCost,hValue);
			Front = a;
		}
		else
		{
			queueNode a = new queueNode(n,q,parent,totalCost,pCost,hValue);
			prev.next = a;			
		}
	}
	public void displayQueue()
	{
		queueNode q;
		q = Front;
		while(q!=null)
		{
			System.out.print("("+q.item.getRow()+","+q.item.getCol()+")=("+(q.TotalCost)+")");
			q=q.next;
		}
	}

}
class queueNode
{
	public queueNode parentNode;
	public node item;
	public queueNode next;
	public int h_value;
	public int TotalCost;
	public int PathCost;
	queueNode(node n, queueNode nxt, queueNode prnt)
	{
		item = n;
		next = nxt;
		parentNode = prnt;
		h_value = 0;
		PathCost = 0;
		TotalCost = 0;
	}
	queueNode(node n, queueNode nxt, queueNode prnt, int tCost,int pCost, int hValue)
	{
		item = n;
		next = nxt;
		parentNode = prnt;
		PathCost = pCost;
		h_value = hValue;
		TotalCost = tCost;
	}
}
