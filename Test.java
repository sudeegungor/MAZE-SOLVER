import java.io.*;
import java.util.*;

import ADTPackage.*;
import GraphPackage.*;

public class Test {
	
	public static char[][] toMatrix(String docName) throws IOException
	{
		char[]gameArea = new char[1000];
	

		int j = 0;
		int i=0;
		File file = new File(docName);
		try (Scanner scanner = new Scanner(file)) {

			while(scanner.hasNextLine()) {
				gameArea = scanner.nextLine().toCharArray();
				j=gameArea.length;
				i++;
			}
			
		}
		char[][] maze= new char[i][j];
		try (Scanner scanner2 = new Scanner(file)) {
			int k = 0;
			while(scanner2.hasNextLine()) {
				maze[k] = scanner2.nextLine().toCharArray();
				k++;
			}
		}
		//print initial maze for test
		/*for (int i1 = 0; i1 < maze.length; i1++) {
			for (int j1 = 0; j1 < maze[i1].length; j1++) {
				
				System.out.print(maze[i1][j1]);
			}
			System.out.println(" ");
			}*/
		return maze;
	}
	
	public static UndirectedGraph<String> mazeToGraph(char[][] maze)
	{
	    UndirectedGraph<String> graph= new UndirectedGraph<String>();
	    graph.addVertex("[0,1]");
	    Random rand_1 = new Random(); 
	    int cost_1 = rand_1.nextInt(4) +1;
        graph.addEdge("[0,1]","[1,1]",cost_1);
      
	   for(int i=1;i<maze.length-1;i++)
	   {
		   for(int j=1;j<maze[0].length-1;j++)
		   {
			   if(maze[i][j]!='#')//if current point is not a wall then it is a vertex
				   //if this vertex has another vertex in its left right up or down they have edge
			   {
				   graph.addVertex("["+i+","+j+"]");
			   if(maze[i][j+1]!='#')
			   {
				   if(!graph.hasEdge("["+i+","+(j+1)+"]", "["+i+","+j+"]"))
				   {
					   Random rand = new Random(); 
				       int cost = rand.nextInt(4) +1;
				       graph.addEdge("["+i+","+j+"]", "["+i+","+(j+1)+"]",cost);
				   }
			   }
			   if(maze[i][j-1]!='#')
			   {
				   if(!graph.hasEdge("["+i+","+(j-1)+"]", "["+i+","+j+"]"))
				   {
					   Random rand = new Random(); 
				       int cost = rand.nextInt(4) +1;
					   graph.addEdge("["+i+","+j+"]", "["+i+","+(j-1)+"]",cost);
				   }
				   
			   }
			   if(maze[i+1][j]!='#')
			   {
				   if(!graph.hasEdge("["+(i+1)+","+j+"]", "["+i+","+j+"]"))
				   {
					   Random rand = new Random(); 
				       int cost = rand.nextInt(4) +1;
					   graph.addEdge("["+i+","+j+"]", "["+(i+1)+","+j+"]",cost);
				   }
				   
			   }
			   if(maze[i-1][j]!='#')
			   {
				   if(!graph.hasEdge("["+(i-1)+","+j+"]", "["+i+","+j+"]"))
				   {
					   Random rand = new Random(); 
				       int cost = rand.nextInt(4) +1;
					   graph.addEdge("["+i+","+j+"]", "["+(i-1)+","+j+"]",cost);
				   }
				   
			   }
			   
			  }
			   
		   }
	   }
	   graph.addVertex("["+(maze.length-2)+","+(maze[0].length-1)+"]");
	   Random rand = new Random(); 
       int cost = rand.nextInt(4) +1;
	   graph.addEdge("["+(maze.length-2)+","+(maze[0].length-2)+"]","["+(maze.length-2)+","+(maze[0].length-1)+"]",cost);
	   return graph;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	   char[][] maze=toMatrix("maze1.txt");
	   UndirectedGraph<String> graph= mazeToGraph(maze);
	   System.out.println("Adjacency Lists");
	   graph.displayEdges();  
	   System.out.println(" ");
       System.out.println("Adjacency Matrix");
       System.out.println(" ");
       graph.adjMatrix();
       System.out.println(" ");
       System.out.println("Number of edges found: "+graph.getNumberOfEdges());
       System.out.println(" ");
       System.out.println("BREADTH-FIRST SEARCH:");
	   QueueInterface<String> bfs = new LinkedQueue<>();
	   QueueInterface<String> bfs_temp = new LinkedQueue<>();
	   bfs=graph.getBreadthFirstSearch("[0,1]","["+(maze.length-2)+","+(maze[0].length-1)+"]");
	   int bfs_i=0;
	   while(!bfs.isEmpty())
	   {
		   
		   String step=bfs.getFront();
		   bfs_temp.enqueue(step);
		   bfs.dequeue();
		   bfs_i++;
		   //finding coordinates of the way to put '.'.
		   int c=step.indexOf(',');
		   String step_x= step.substring(1, c);
		   String step_y=step.substring((c+1),(step.length()-1));
		   int x=Integer.parseInt(step_x);
		   int y=Integer.parseInt(step_y);
		   maze[x][y]='.';
	   }
		for (int i1 = 0; i1 < maze.length; i1++) {
			for (int j1 = 0; j1 < maze[i1].length; j1++) {
				
				System.out.print(maze[i1][j1]);
			}
			System.out.println(" ");
			}
		//returning original maze for other paths.
		while(!bfs_temp.isEmpty())
		{
			   String step=bfs_temp.dequeue();
			   int c=step.indexOf(',');
			   String step_x= step.substring(1, c);
			   String step_y=step.substring((c+1),(step.length()-1));
			   int x=Integer.parseInt(step_x);
			   int y=Integer.parseInt(step_y);
			   maze[x][y]=' ';
		}
		System.out.println("The number of visited vertices for BFS: "+bfs_i);
		System.out.println("  ");
		System.out.println("DEPTH-FIRST SEARCH");
	   
	   QueueInterface<String> dfs = new LinkedQueue<>();
	   QueueInterface<String> dfs_temp = new LinkedQueue<>();
	  dfs=graph.getDepthFirstSearch("[0,1]","["+(maze.length-2)+","+(maze[0].length-1)+"]");
	   int dfs_i=0;
	   while(!dfs.isEmpty())
	   {
		   String step=dfs.getFront();
		   dfs_temp.enqueue(step);
		   dfs.dequeue();
		   dfs_i++;
		   int c=step.indexOf(',');
		   String step_x= step.substring(1, c);
		   String step_y=step.substring((c+1),(step.length()-1));
		   int x=Integer.parseInt(step_x);
		   int y=Integer.parseInt(step_y);
		   maze[x][y]='.';
	   }
		for (int i1 = 0; i1 < maze.length; i1++) {
			for (int j1 = 0; j1 < maze[i1].length; j1++) {
				
				System.out.print(maze[i1][j1]);
			}
			System.out.println(" ");
			}
		//returning original maze
		while(!dfs_temp.isEmpty())
		{
			   String step=dfs_temp.dequeue();
			   int c=step.indexOf(',');
			   String step_x= step.substring(1, c);
			   String step_y=step.substring((c+1),(step.length()-1));
			   int x=Integer.parseInt(step_x);
			   int y=Integer.parseInt(step_y);
			   maze[x][y]=' ';
		}
		System.out.println("The number of visited vertices for DFS: "+dfs_i);
		System.out.println(" ");
		System.out.println("SHORTEST PATH:");
	   StackInterface<String> shortestPath= new LinkedStack<>();
	   QueueInterface<String> sp_temp = new LinkedQueue<>();
	   int length=graph.getShortestPath("[0,1]","["+(maze.length-2)+","+(maze[0].length-1)+"]" , shortestPath);
	   
	  while(!shortestPath.isEmpty())
	   {
		   String step=shortestPath.peek();
		   sp_temp.enqueue(step);
		   shortestPath.pop();
		   int c=step.indexOf(',');
		   String step_x= step.substring(1, c);
		   String step_y=step.substring((c+1),(step.length()-1));
		   int x=Integer.parseInt(step_x);
		   int y=Integer.parseInt(step_y);
		   maze[x][y]='.';
	   }
		for (int i1 = 0; i1 < maze.length; i1++) {
			for (int j1 = 0; j1 < maze[i1].length; j1++) {
				
				System.out.print(maze[i1][j1]);
			}
			System.out.println(" ");
			}
		//returning original maze
		while(!sp_temp.isEmpty())
		{
			   String step=sp_temp.dequeue();
			   int c=step.indexOf(',');
			   String step_x= step.substring(1, c);
			   String step_y=step.substring((c+1),(step.length()-1));
			   int x=Integer.parseInt(step_x);
			   int y=Integer.parseInt(step_y);
			   maze[x][y]=' ';
		}
		System.out.println("The number of visited vertices for shortest path: "+(length+1));
		System.out.println("  ");
		System.out.println("CHEAPEST PATH");

	   StackInterface<String> cheapestPath= new LinkedStack<>();
	   QueueInterface<String> cp_temp = new LinkedQueue<>();
	   double cost=graph.getCheapestPath("[0,1]","["+(maze.length-2)+","+(maze[0].length-1)+"]" , cheapestPath);
	   int cp_i=0;
	   while(!cheapestPath.isEmpty())
	   {
		   String step=cheapestPath.peek();
		   cp_temp.enqueue(step);
		   cheapestPath.pop();
		   cp_i++;
		   int c=step.indexOf(',');
		   String step_x= step.substring(1, c);
		   String step_y=step.substring((c+1),(step.length()-1));
		   int x=Integer.parseInt(step_x);
		   int y=Integer.parseInt(step_y);
		   maze[x][y]='.';
	   }
		for (int i1 = 0; i1 < maze.length; i1++) {
			for (int j1 = 0; j1 < maze[i1].length; j1++) {
				
				System.out.print(maze[i1][j1]);
			}
			System.out.println(" ");
			}	   
		//returning original maze
		while(!cp_temp.isEmpty())
		{
			   String step=cp_temp.dequeue();
			   int c=step.indexOf(',');
			   String step_x= step.substring(1, c);
			   String step_y=step.substring((c+1),(step.length()-1));
			   int x=Integer.parseInt(step_x);
			   int y=Integer.parseInt(step_y);
			   maze[x][y]=' ';
		}

		
		System.out.println("The number of visited vertices for shortest path: "+cp_i);
		System.out.println("The cost of the cheapest path: "+cost);
	   


		  
		   
	  
		
}
		

}





