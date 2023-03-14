package GraphPackage;
import java.util.Iterator;
import ADTPackage.*; // Classes that implement various ADTs
/**
 A class that implements the ADT directed graph.
 @author Frank M. Carrano
 @author Timothy M. Henry
 @version 5.1
 */
public class DirectedGraph<T> implements GraphInterface<T>
{
   private DictionaryInterface<T, VertexInterface<T>> vertices;
   private int edgeCount;
   
   public DirectedGraph()
   {
      vertices = new UnsortedLinkedDictionary<>();
      edgeCount = 0;
   } // end default constructor

   public boolean addVertex(T vertexLabel)
   {
      VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
      return addOutcome == null; // Was addition to dictionary successful?
   } // end addVertex
   
   public boolean addEdge(T begin, T end, double edgeWeight)
   {
      boolean result = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
         result = beginVertex.connect(endVertex, edgeWeight);
      if (result)
         edgeCount++;
      return result;
   } // end addEdge
   
   public boolean addEdge(T begin, T end)
   {
      return addEdge(begin, end, 0);
   } // end addEdge

   public boolean hasEdge(T begin, T end)
   {
      boolean found = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
      {
         Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
         while (!found && neighbors.hasNext())
         {
            VertexInterface<T> nextNeighbor = neighbors.next();
            if (endVertex.equals(nextNeighbor))
               found = true;
         } // end while
      } // end if
      
      return found;
   } // end hasEdge

	public boolean isEmpty()
	{
	  return vertices.isEmpty();
	} // end isEmpty

	public void clear()
	{
	  vertices.clear();
	  edgeCount = 0;
	} // end clear

	public int getNumberOfVertices()
	{
	  return vertices.getSize();
	} // end getNumberOfVertices

	public int getNumberOfEdges()
	{
	  return edgeCount;
	} // end getNumberOfEdges

	protected void resetVertices()
	{
	   Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
	   while (vertexIterator.hasNext())
	   {
	      VertexInterface<T> nextVertex = vertexIterator.next();
	      nextVertex.unvisit();
	      nextVertex.setCost(0);
	      nextVertex.setPredecessor(null);
	   } // end while
	} // end resetVertices
	
	public StackInterface<T> getTopologicalOrder() 
	{
		resetVertices();

		StackInterface<T> vertexStack = new LinkedStack<>();
		int numberOfVertices = getNumberOfVertices();
		for (int counter = 1; counter <= numberOfVertices; counter++)
		{
			VertexInterface<T> nextVertex = findTerminal();
			nextVertex.visit();
			vertexStack.push(nextVertex.getLabel());
		} // end for
		
		return vertexStack;	
	} // end getTopologicalOrder
	
	
   //###########################################################################
      public QueueInterface<T> getBreadthFirstSearch(T origin, T end) {
    	  resetVertices();
    	  QueueInterface<T> traversalOrder = new LinkedQueue<>();
    	  QueueInterface<VertexInterface<T>> vertexQueue =new LinkedQueue<>();
    	  VertexInterface<T> originVertex = vertices.getValue(origin);
    	  originVertex.visit();
    	  traversalOrder.enqueue(origin); // Enqueue vertex label
    	  vertexQueue.enqueue(originVertex); // Enqueue vertex
    	  while (!vertexQueue.isEmpty())
    	  {
    	  VertexInterface<T> frontVertex = vertexQueue.dequeue();
    	  Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
    	  while (neighbors.hasNext())
    	  {
    	  VertexInterface<T> nextNeighbor = neighbors.next();
    	  if (!nextNeighbor.isVisited())
    	  {
    	  nextNeighbor.visit();
    	  traversalOrder.enqueue(nextNeighbor.getLabel());
    	  vertexQueue.enqueue(nextNeighbor);
    	  if(nextNeighbor.getLabel().toString().equals(end))
    		  {
    		  return traversalOrder;
    		  }
    	  } // end if
    	  }//end while  
    	  }//end while
    	  return traversalOrder;
		  
      } // end getBreadthFirstTraversal
    
   //###########################################################################

  
	
	
   //###########################################################################
      public QueueInterface<T> getDepthFirstSearch(T origin,T end)

      {
      resetVertices();
      QueueInterface<T> traversalOrder=new LinkedQueue();
      StackInterface<VertexInterface<T>> vertexStack=new LinkedStack();
      VertexInterface<T> originVertex=vertices.getValue(origin);
      originVertex.visit();
      traversalOrder.enqueue(origin);
      vertexStack.push(originVertex);

      while(!vertexStack.isEmpty())

      {

      // Assign the top most element of the Stack into topVertex but do not delete it from the stack.
      VertexInterface<T> topVertex=vertexStack.peek();
      // Create an Iterator to get the neighbor of the topVertex
      Iterator<VertexInterface<T>>neighbors=topVertex.getNeighborIterator();
      // Check if the top vertex has unvisited vertex
      if(topVertex.getUnvisitedNeighbor()!=null)

      {
      // Assign next unvisited neighbor of topVertex to nextNeighbor
       VertexInterface<T> nextNeighbor=topVertex.getUnvisitedNeighbor();
      nextNeighbor.visit(); // mark the nextNeighbor as visited vertex
      traversalOrder.enqueue(nextNeighbor.getLabel());  // insert the next neighbor into queue     
      vertexStack.push(nextNeighbor); // push the next neighbor vertex into stack
  	   if(nextNeighbor.getLabel().toString().equals(end))
	     {
	        return traversalOrder;
	     }

      }
      // If top vertex does not have unvisited vertex // then poo the value from the stack
      else
      {
      vertexStack.pop();
      }

      }
      return traversalOrder;
      }
   //###########################################################################
		
	
	
	
	//###########################################################################
      public int getShortestPath(T begin, T end, StackInterface<T> path)
      {
      resetVertices();
      boolean done = false;
      QueueInterface<VertexInterface<T>> vertexQueue =  new LinkedQueue<>();	 
      VertexInterface<T> originVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      originVertex.visit(); // Assertion: resetVertices() has executed setCost(0)
   // and setPredecessor(null) for originVertex
      vertexQueue.enqueue(originVertex);
      while (!done && !vertexQueue.isEmpty())
      {
      VertexInterface<T> frontVertex =
      vertexQueue.dequeue();
      Iterator<VertexInterface<T>> neighbors =
      frontVertex.getNeighborIterator();
      while (!done && neighbors.hasNext())
      {
      VertexInterface<T> nextNeighbor =
      neighbors.next();
      if (!nextNeighbor.isVisited())
      {
      nextNeighbor.visit();
      nextNeighbor.setCost(1 +
      frontVertex.getCost());
      nextNeighbor.setPredecessor(frontVertex);
      vertexQueue.enqueue(nextNeighbor);
      } // end if

      if (nextNeighbor.equals(endVertex))
    	  done = true;
    	  } // end while
    	  } // end while
    	  // Traversal ends; construct shortest path
      int pathLength = (int)endVertex.getCost();
      path.push(endVertex.getLabel());
      VertexInterface<T> vertex = endVertex;
      while (vertex.hasPredecessor())
      {
      vertex = vertex.getPredecessor();
      path.push(vertex.getLabel());
      } // end while
      return pathLength;
      } // end getShortestPath


    //###########################################################################
  
   
	
   
    //###########################################################################
      public double getCheapestPath(T begining, T ending, StackInterface<T> path)

      {

      // Initially assign done to false as cheapest path is not found
      boolean done = false;
      // Create an PriorityQueue for storing the object
      PriorityQueueInterface<EntryPQ> priorityQueue = new HeapPriorityQueue<EntryPQ>();
      // Assign the value of beginning vertex to the originVertex
      VertexInterface<T> originVertex = vertices.getValue(begining);
      // Assign the value of ending vertex to the endVertex
      VertexInterface<T> endVertex = vertices.getValue(ending);
      // Add the entry into the priorityQueue
      priorityQueue.add(new EntryPQ(originVertex, 0, null));
      // Traverse while loop until done is true and priority queue is not empty
      while (!done && !priorityQueue.isEmpty())

      {

      // Remove the vertex from the priorityQueue and assign it is to frontEntry
      EntryPQ frontEntry = priorityQueue.remove();
      // get the vertex from the frontEntry and assign it to frontVertex
      VertexInterface<T> frontVertex =frontEntry.getVertex();
      // Check whether frontVertex is visited. If frontVertex is not visited the if condition will execute
      if (!frontVertex.isVisited())

      {
      // Mark frontVertex as visited
      frontVertex.visit();
      // get the cost of the frontEntry and set the cost to the frontVertex
      frontVertex.setCost(frontEntry.getCost());
      // get the predeccessor of the frontEntry and  set the cost to the frontVertex
      frontVertex.setPredecessor(frontEntry.getPredecessor());
      // Check whether frontVertex is equal to endVertex

      if (frontVertex.equals(endVertex))
    	  done = true;
      // Assign done to true if frontVertex is equal to endVertex
      // If frontVertex is not equal to endVertex then visit all the neighbor of the originVertex
      else

      {
      // get the neighbor of the frontVertex and assign the neighbor to neighbors
      Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
      // get the edgeWeight of the neighbors paths and assign it to edgeWeight
      Iterator<Double> edgeWeights = frontVertex.getWeightIterator();
      // Iterate the while loop until neighbors is present
      while (neighbors.hasNext())

       {

       // next neighbors of the vertex is assigned to nextNeighbors
       VertexInterface<T> nextNeighbor = neighbors.next();
       // next edge of the vertex is assign to weighOfEdgeTONeighbors
       Double weightOfEdgeToNeighbor =  edgeWeights.next();
       // Check untill all the neighbors are not visited
       if (!nextNeighbor.isVisited())
 
         {
            // nextCost is calculated by the summation of theweightOfEdgeToNeighbor and the cost of frontVertex
              double nextCost =weightOfEdgeToNeighbor +frontVertex.getCost();
            // Add the entry into the priorityQueue
               priorityQueue.add(new EntryPQ(nextNeighbor, nextCost,frontVertex));
          }
      
       }
      }
      }
      }
      double pathCost= endVertex.getCost();
      path.push(endVertex.getLabel());
      VertexInterface<T> vertex=endVertex;
      while(vertex.hasPredecessor())
      {
    	  vertex=vertex.getPredecessor();
    	  path.push(vertex.getLabel());
      }
      return pathCost;
      
      }
      
    //###########################################################################


	
	protected VertexInterface<T> findTerminal()
	{
		boolean found = false;
		VertexInterface<T> result = null;

		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();

		while (!found && vertexIterator.hasNext())
		{
			VertexInterface<T> nextVertex = vertexIterator.next();
			
			// If nextVertex is unvisited AND has only visited neighbors)
			if (!nextVertex.isVisited())
			{ 
				if (nextVertex.getUnvisitedNeighbor() == null )
				{ 
					found = true;
					result = nextVertex;
				} // end if
			} // end if
		} // end while

		return result;
	} // end findTerminal

	// Used for testing
	public void displayEdges()
	{
		System.out.println("\nEdges exist from the first vertex in each line to the other vertices in the line.");
		System.out.println("(Edge weights are given; weights are zero for unweighted graphs):\n");
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		while (vertexIterator.hasNext())
		{
			((Vertex<T>)(vertexIterator.next())).display();
		} // end while
	} // end displayEdges 
	
	//to create adjacency matrix
	@SuppressWarnings("unchecked")
	public void adjMatrix()
	{
		Vertex[] row= new Vertex[getNumberOfVertices()];
		Vertex[] column= new Vertex[getNumberOfVertices()];
		String[][] matrix= new String[getNumberOfVertices()+1][getNumberOfVertices()+1];
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		int i=0;
		int j=0;
		while (vertexIterator.hasNext())//take vertices and make label row and column for matrix
		{
			row[i]=(Vertex<T>)vertexIterator.next();
			column[j]=row[i];
			i++;
			j++;
		}
		//since label row and column has vertices order in backwards we need to make loops backwards so we can get a correct
		//ordered matrix.
        
		for(int l=i-1;l>=0;l--)
		{
			for(int m=i-1;m>=0;m--)
			{
				if(hasEdge((T) row[l].getLabel(),(T) column[m].getLabel()))
				{
					matrix[l][m]="1";
				}
				else
				{
					matrix[l][m]="0";
				}
			}
		}
		System.out.print("     ");
		for(int k=i-1;k>=0;k--)
		{
			System.out.print(row[k]+" ");
		}
		System.out.println(" ");
		for(int l=i-1;l>=0;l--)
		{
			System.out.print(column[l]+" ");
			for(int m=i-1;m>=0;m--)
			{
                      System.out.print(" "+matrix[l][m]+"    ");
			}
			System.out.println(" ");
		}
	}
	
	private class EntryPQ implements Comparable<EntryPQ>
	{
		private VertexInterface<T> vertex; 	
		private VertexInterface<T> previousVertex; 
		private double cost; // cost to nextVertex
		
		private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex)
		{
			this.vertex = vertex;
			this.previousVertex = previousVertex;
			this.cost = cost;
		} // end constructor
		
		public VertexInterface<T> getVertex()
		{
			return vertex;
		} // end getVertex
		
		public VertexInterface<T> getPredecessor()
		{
			return previousVertex;
		} // end getPredecessor

		public double getCost()
		{
			return cost;
		} // end getCost
		
		public int compareTo(EntryPQ otherEntry)
		{
			// Using opposite of reality since our priority queue uses a maxHeap;
			// could revise using a minheap
			return (int)Math.signum(otherEntry.cost - cost);
		} // end compareTo
		
		public String toString()
		{
			return vertex.toString() + " " + cost;
		} // end toString 
	} // end EntryPQ








} // end DirectedGraph
