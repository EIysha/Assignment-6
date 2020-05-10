import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class TownGraph implements GraphInterface<Town, Road> {

    /**
     * The towns in the graph
     */
    private Set<Town> twns = new HashSet<>();
    
    /**
     * Roads in the graph based on the towns
     */
    private Set<Road> rds = new HashSet<>();
    
    /**
     * List of shortest path from town A to town B
     */    
    private ArrayList<String> shortestPath = new ArrayList<>();
    private int finishedTown;
    private Town destination;
    
    private void pathArrayList(int currentVertex, int[] parents) {
        
        if (currentVertex == -1) { 
            return; 
        } 
        pathArrayList(parents[currentVertex], parents); 
        int townIndex = 0;
        for (Town t : twns) {
            if (townIndex == currentVertex) {
                shortestPath.add(t.getName()); 
            }
            townIndex++;
        }
    }
 
    @Override
    public Road getEdge(Town sourceVertex, Town destinationVertex) {
        Road road = null;
        for (Road r : rds) {
            if (r.contains(sourceVertex) && r.contains(destinationVertex)) {
                road = r;
           }
        }
        return road;
    }

    @Override
    public boolean addVertex(Town v) {
        
        if (v == null) {
            throw new NullPointerException();
        }
        
        if (!twns.contains(v)) {
            twns.add(v);
            return true;
        }
        
        return false;
    }

    @Override
    public boolean containsEdge(Town sourceVertex, Town destinationVertex) {
        for (Road r : rds) {
            if (r.contains(sourceVertex) && r.contains(destinationVertex)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Road addEdge(Town sourceVertex, Town destinationVertex, 
            int weight, String description) {
        
        if (sourceVertex == null || destinationVertex == null) {
            throw new NullPointerException();
        }
        
        if (!twns.contains(sourceVertex) || !twns.contains(destinationVertex)) { 
            throw new IllegalArgumentException();
        }
        
        Road road = new Road(sourceVertex, destinationVertex, weight, description);
        rds.add(road);
        
        return road;
    }

    @Override
    public boolean containsVertex(Town v) {
        return twns.contains(v);
    }

    @Override
    public Set<Road> edgeSet() {
        return rds;
    }
    
    @Override
    public Set<Road> edgesOf(Town vertex) {
        Set<Road> edges = new HashSet<>();
        for (Road r : rds) {
            if (r.contains(vertex)) {
                edges.add(r);
            }
        }
        return edges;
    }
    
    @Override
    public Road removeEdge(Town sourceVertex, Town destinationVertex, 
            int weight, String description) {
        
        if (sourceVertex == null || destinationVertex == null || description == null) {
            throw new NullPointerException();
        }
        
        if (!twns.contains(sourceVertex) || !twns.contains(destinationVertex)) { 
            throw new IllegalArgumentException();
        }
        
        Road road = null;
        for (Road r : rds) {
            if (r.contains(sourceVertex) && r.contains(destinationVertex) &&
                    r.getWeight() == weight && r.getName().equals(description)) {
                road = r;
            }
        }
        return rds.remove(road) ? road : null;
    }
    
    @Override
    public boolean removeVertex(Town v) {
        return twns.remove(v);
    }
    
    @Override
    public Set<Town> vertexSet() {
        return twns;
    }
    
    @Override
    public ArrayList<String> shortestPath(Town sourceVertex, Town destinationVertex) {
        destination = destinationVertex;
        dijkstraShortestPath(sourceVertex);
        String shortPath = "";
        int totalMiles = 0;
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Town source = new Town(shortestPath.get(i));
            Town destiny = new Town(shortestPath.get(i + 1));
            Road road = getEdge(source, destiny);
            totalMiles = road.getWeight() + totalMiles;
            shortPath += source + " via " + road.getName() + " to " + destiny 
                    + " " + road.getWeight() + " miles;";
        }
        shortestPath.clear();
        for (String s : shortPath.split(";")) {
            shortestPath.add(s);
        }
        shortestPath.add("Total miles: " + totalMiles + " miles");
        return shortestPath;
    } 
    
    @Override
    public void dijkstraShortestPath(Town sourceVertex) {
        shortestPath.clear();
        Town[] rowsAndCols = new Town[twns.size()];
        int s = 0;
        for (Town t : twns) {
            rowsAndCols[s] = new Town(t);
            s++;
        }        
        int[][] adjacencyMatrix = new int[twns.size()][twns.size()];       
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                if (i == j || !containsEdge(rowsAndCols[i], rowsAndCols[j])) {
                    adjacencyMatrix[i][j] = 0;
                } else {
                    int weight = getEdge(rowsAndCols[i], rowsAndCols[j]).getWeight();
                    adjacencyMatrix[i][j] = adjacencyMatrix[j][i] = weight;
                }
            }
        }
        
        int startTown = 0;
        for (Town t : rowsAndCols) {
            if (!t.equals(sourceVertex)) {
                startTown++;
            } else {
                break;
            }
        }
        
        finishedTown = 0;
        for (Town t : rowsAndCols) {
            if (!t.equals(destination)) {
                finishedTown++;
            } else {
                break;
            }
        }
        
        int numTowns = adjacencyMatrix[0].length; 
        
        int[] smallestWeights = new int[numTowns];
        
        boolean[] added = new boolean[numTowns];
        
        for (int townIdx = 0; townIdx < numTowns; townIdx++) {
            smallestWeights[townIdx] = Integer.MAX_VALUE;
            added[townIdx] = false;
        }
        
        smallestWeights[startTown] = 0;
        
        int[] parents = new int[numTowns];
        
        parents[startTown] = -1;
        
        for (int i = 1; i < numTowns; i++) {
            int nearestTown = -1;
            int smallestWeight = Integer.MAX_VALUE;
            for (int townIdx = 0; townIdx < numTowns; townIdx++) {
                if (!added[townIdx] && 
                        smallestWeights[townIdx] < smallestWeight) {
                    nearestTown = townIdx;
                    smallestWeight = smallestWeights[townIdx];
                }
            }
            added[nearestTown] = true;
            for (int townIndex = 0; townIndex < numTowns; townIndex++) {
                int roadDist = adjacencyMatrix[nearestTown][townIndex]; 
                if (roadDist > 0 && ((smallestWeight + roadDist) < smallestWeights[townIndex])) {
                    parents[townIndex] = nearestTown;
                    smallestWeights[townIndex] = smallestWeight + roadDist;
                }
            }           
        }
        pathArrayList(finishedTown, parents); 
    }

}