import CubeObjects.*;
import CubeOperations.*;
import com.sun.xml.internal.xsom.XSUnionSimpleType;
//import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSOutput;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.DoubleToIntFunction;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.util.Arrays.asList;

public class UnfoldingATeserract {

		 static List<String> ans = new ArrayList<>();
		 static List<String> validUnfoldings = new ArrayList<>();
		 static String[][] grid;
		 static HashMap<String, Boolean> visitedEdges = new HashMap<>();
		 static HashMap<String, Boolean> visited = new HashMap<>();
		 static HashMap<String, List<String>> edgeToFaces = new HashMap<>();
		 static HashMap<String, List<Integer>> coordinatesOfTheFaces = new HashMap<>();
		 static HashMap<String, HashMap<String, String>> faceAndNeighbours = new HashMap<>();
		 static HashMap<String, String> directionArrows = new HashMap<>();
		 static Set<String> allEncodings = new HashSet<String>();
		 static	HashMap<String, List<String>> faceToEdges = new HashMap<>();
		 static	List<String> edges = new ArrayList<>();
		 static HashMap<String, List<Integer>> directions = new HashMap<>();
		 static int fileid=0;
		 static final Logger logging = Logger.getLogger("MyLog");
		private static String otherFace = "";


		//profiling variables.
		static Map<Integer, Integer> collusion = new HashMap<>();
		static int chainlength=0;
	static int profilingCounter=0;
		 public static void init(){
			 FileHandler fh;
			 try{
				 fh= new FileHandler("/Users/narayanavasisthakandarpa/Desktop/Projects/UnfoldingATeserract-master/MyLogFile.log");
					logging.addHandler(fh);
				 SimpleFormatter formatter = new SimpleFormatter();
				 fh.setFormatter(formatter);


			 } catch (Exception e){
				 logging.log(Level.WARNING, "Initialization ERROR");
			 }
		 }

	public static HashMap<String, HashMap<String, String>> getFaceAndNeighbours() {
		return faceAndNeighbours;
	}




	public static void main(String[] args) {
		init();

		/*step 1. declaring  the grid]
				*/
		//step1
		grid = new String[50][50];
		//step 2 directional vectors to move all directions
		directions.put("north", asList(-1, 0));
		directions.put("south", asList(1, 0));
		directions.put("west", asList(0, -1));
		directions.put("east", asList(0, 1));

		/*Received the bipartite graph
		* Map Between Int
		* */
		HashMap<Integer, List<Face>> bipartiteGraph = CubeOperations.Cube.dualGraphOfTheCube();
		//System.out.println(bipartiteGraph.toString());
		//System.exit(0);

		for (Integer i : bipartiteGraph.keySet()) {
			for (Face face : bipartiteGraph.get(i)) {
				if (!faceToEdges.containsKey(face.getTAG())) faceToEdges.put(face.getTAG(), new ArrayList<>());

				for (DualNode dn : face.getConnectedEdges()) faceToEdges.get(face.getTAG()).add(dn.getTAG());
			}
		}



		for (String face : faceToEdges.keySet()) {
			for (String edge : faceToEdges.get(face)) {
				if (!edgeToFaces.containsKey(edge)) edgeToFaces.put(edge, new ArrayList<>());
				edgeToFaces.get(edge).add(face);
			}
		}

		for (String edge : edgeToFaces.keySet())edges.add(edge);

		HashMap<String, List<String>> edgeToFacesSolution = new HashMap<>();

		for (String edge : edgeToFaces.keySet()) edgeToFacesSolution.put(edge, new ArrayList<>());

		AbstractCube abstractCube = new AbstractCube();
		traversalWithBacktracking(faceToEdges, abstractCube);
		System.out.println("TERMINATION: EXECUTION OF THE PROGRAM IS FINISHED.");
	}



	/*
	 *
	 * 1. created grid and directions.
	 * 2. you received a bipartite graph.
	 * 3. created edgeToFace and faceToEdges.
	 * 4. created the abstract cube.
	 *
	 *
	 *
	 * traversalWithBacktracking.
	 *
	 *
	 * */

	public static void traversalWithBacktracking(HashMap<String, List<String>> faceToEdges, AbstractCube abstractCube) {
			 //variables.
//		List<List<Integer>> tree = new ArrayList<>();
//		HashMap<String, Integer> nodeIndex = new HashMap<>(24);
//
//		//operations.
//		int i=0;
//		for(String face: faceToEdges.keySet()){
//			nodeIndex.put(face, i);
//			tree.add(new ArrayList<>());
//			i++;
//			faceAndNeighbours.put(face, new HashMap<>());
//		}
//
//
//		directionArrows.put("NORTH", "^");
//		directionArrows.put("SOUTH", "v");
//		directionArrows.put("EAST", ">");
//		directionArrows.put("WEST", "<");
//
//
//		for (String face : faceToEdges.keySet()) visited.put(face, false);
//		for(String edge: edgeToFaces.keySet()) visitedEdges.put(edge, false);
//
//
//		String leafFace="FEGH";
//		for (String face : faceToEdges.keySet()) {leafFace = face; break;}
//
//
//		//checks for correctness of leaf face.
//		if (leafFace == "") {
//			System.out.println("Could not find a leafFace.");
//			System.exit(0);
//		}
//
//
//		String centralFace = leafFace;
//		HashMap<String, String> currDirections = new HashMap<>(4);
//
//		// adding the north edge
//		currDirections.put("NORTH", faceToEdges.get(centralFace).get(0));
//		boolean flipSides = true;
//		//assigning directions to the start face
//		for (String edge : faceToEdges.get(centralFace)) {
//			if (edge == currDirections.get("NORTH")) continue;
//
//			if ((edge.charAt(0) != currDirections.get("NORTH").charAt(0) && edge.charAt(0) != currDirections.get("NORTH").charAt(1)) && (edge.charAt(1) != currDirections.get("NORTH").charAt(0) && edge.charAt(1) != currDirections.get("NORTH").charAt(1))) {
//				currDirections.put("SOUTH", edge);}
//
//			else {
//						if (flipSides) {
//							currDirections.put("EAST", edge);
//							flipSides = false;
//						} else
//							currDirections.put("WEST", edge);
//						}
//		}
//
//		String otherFace = "";
//		String parentEdge = "";
//
//		for (String edge : faceToEdges.get(leafFace)) {// removing three connection with the leadFace.
//			if (edgeToFaces.get(edge).contains(centralFace)) {
//				parentEdge = edge;
//				otherFace = leafFace.equals(edgeToFaces.get(edge).get(0))? edgeToFaces.get(edge).get(2)
//						: edgeToFaces.get(edge).get(0);
//			}
//		}
//
//		if (otherFace == "") {
//			System.out.println("Could not find the other face");
//			System.exit(0);
//		}


		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 50; j++) {

				grid[i][j] = "-------";
			}
		}
		List<List<Integer>> tree = new ArrayList<>();
		HashMap<String, Integer> nodeIndex = new HashMap<>(24);
		 String parentEdge = "";

		int i=0;

		for(String face: faceToEdges.keySet()){
			nodeIndex.put(face, i);
			tree.add(new ArrayList<>());
			i++;
			faceAndNeighbours.put(face, new HashMap<>());
		}




		// step1: fix the leaf node and join it with the other face.



		directionArrows.put("NORTH", "^");
		directionArrows.put("SOUTH", "v");
		directionArrows.put("EAST", ">");
		directionArrows.put("WEST", "<");

		/*
		TODO: Fixing Faces and finding configuration wise unfoldings.


		 Regarding fixing the faces:
				 1. the first face is fixed so that we can begin unfolding.(this is done in this function itself. Also its directions are setup in this function)

				 2. the other subsequent fixings:(this is handled in the new function fixFaces()).
				 		2.1 you have to permutate all possible orientations for that face.
				 			 i.e lets say north is fixed via the parent, now there are three things possible
				 					i) North is fixed and no other direction is fixed.
				 					ii) North is fixed and South is fixed
				 					iii) North is fixed and East is fixed
				 					iv) North is fixed, So is south and so is East.
				 					these should cover all possibilities.

		Algorithm:
				 fix the faces and send the last fixed-face to the BFS.
				There it will choose available directions and unfold. (if the direction is not available then it will go with
				the default value 0). So, there are only three loops but 4 directions. Take note that one direction that leads
				to the parent is the primary directions and other directions are secondary. i,j,z are for the secondary directions

		NEW NOTE: When you fix K depth face use that information to check whe you have fixed k-1th face.
		* Inorder to check the kth parent of a leaf, use binary lifting.
			With this you should be able to check the configuration of the kth parent.
			if the kth parent has the same orientation or mirror image then you should not consider it.

		Note: The graph is acyclic. So, there is only one path to move from one leaf to any other leaf.
		*
		Implementing the idea:
		1) add a function that fixes faces and marks them as visited(just don't mark the last face as visited coz we need to
			unfold via that face and step2 can cause premature termination).
		2) Step 1 solution: rather than taking care of not adding the leaf faces to the child queue, we can add a
			condition at the top of the BFS: while(self is visited ) choose next self.
		3) TODO: Binary lifting still pending.
		*
		* */


		// preparing the visited hashmap.
		Stack<String> theQueueParent = new Stack<>();
		Stack<String> theQueueChildren = new Stack<>();
		Map<String, Integer> parentDegree= new HashMap<>();


		for (String face : faceToEdges.keySet()) visited.put(face, false);
		for(String edge: edgeToFaces.keySet()) visitedEdges.put(edge, false);


		//assigning leaf face.
		String leafFace="FGBC";
		for (String face : faceToEdges.keySet()) {
			if(face.equals(leafFace)){leafFace = face;break;}}// here the leaf face ="DdCc". and don't remove this loop code breaks


		//checks for correctness of leaf face.
		if (leafFace == "") {
			System.out.println("Could not find a leafFace.");
			System.exit(0);
		}



		// defining the directions of the leaf node.
		//copied the function from the top.
		String centralFace = leafFace;
		HashMap<String, String> currDirections = new HashMap<>(4);


//	System.out.println(" this is null"+ faceToEdges.get(leafFace).toString());
		//setting up the leafFace(centralFace) with directions
		// adding the north edge

		currDirections.put("NORTH", faceToEdges.get(leafFace).get(0));
		boolean flipSides = true;
		//assigning directions to the start face
		for (String edge : faceToEdges.get(centralFace)) {
			if (edge == currDirections.get("NORTH")) continue;

			if ((edge.charAt(0) != currDirections.get("NORTH").charAt(0)
					&& edge.charAt(0) != currDirections.get("NORTH").charAt(1))
					&& (edge.charAt(1) != currDirections.get("NORTH").charAt(0)
					&& edge.charAt(1) != currDirections.get("NORTH").charAt(1))) {
				// adding the south edge.
				currDirections.put("SOUTH", edge);
			} else {
				// adding the east or west edge
				if (flipSides) {
					currDirections.put("EAST", edge);
					flipSides = false;
				} else
					currDirections.put("WEST", edge);

			}
		}
		//Central face directions have been set up.
		//TODO: paste here the things we need to do so that this face is not used in the BFS traversal.[DONE]

		//Choosing the otherFace as the south one to the centralFace.
		otherFace = edgeToFaces.get(currDirections.get("SOUTH")).get(0)==centralFace?edgeToFaces.get(currDirections.get("SOUTH")).get(1):edgeToFaces.get(currDirections.get("SOUTH")).get(0);
		parentEdge= currDirections.get("SOUTH");
		String parentDir = "SOUTH";

		if (otherFace == "") {
			System.out.println("Could not find the other face");
			System.exit(0);
		}
		parentDegree.put(centralFace, 1);




		//marking the faces(Both leafFace and otherFaces)as visited and parent sharing edge as visited
		visitedEdges.put(parentEdge, true);
		visited.put(leafFace, true);
		visited.put(otherFace, true);
		AbstractCube.addEdgeToParent(parentEdge, leafFace);// this is correct.
		AbstractCube.addToFaceAndItsDirections(centralFace, currDirections);// this is correct.
		AbstractCube.addFaceToParentSharingEdge(otherFace, parentEdge);
		//other face coordinates.
		AbstractCube.addToCoordinatesOfTheFaces(otherFace,
				Arrays.asList(25+ abstractCube.getUnitVectors().get(parentDir).get(0),
						25 + abstractCube.getUnitVectors().get(parentDir).get(1)));

		faceAndNeighbours.get(leafFace).put(parentDir, otherFace);
		faceAndNeighbours.get(otherFace).put(oppositeDirection(parentDir), leafFace);

		// putting the faces on the grid.

		//placing the leaf node at the center.

		grid[25][25] = "( )"+centralFace;

		grid[25 + abstractCube.getUnitVectors().get(parentDir).get(0)][25
				+ abstractCube.getUnitVectors().get(parentDir).get(1)] = "( )"+otherFace;//otherFace;

		//TODO: HERE IS THE CALL TO THE fixFaces.
		tree.get(nodeIndex.get(centralFace)).add(nodeIndex.get(otherFace));
		tree.get(nodeIndex.get(otherFace)).add(nodeIndex.get(centralFace));
	fixFaces(theQueueParent,theQueueChildren,  leafFace, currDirections,  tree, nodeIndex, abstractCube,  parentDegree, 4, otherFace);
		//System.out.println("The end of the problem System exit on line 377"); //System.exit(349);
		//System.out.println("the parent face from main " + leafFace);
		//System.out.println("the other face is: " + otherFace);


		//INITIALIZING THE STACKS

		theQueueParent.add(otherFace);

		//parent Degree tells us what is the degree of  the face.



		//tree operations. TREE IS USED FOR FINDING THE CENTRAL FACES FOR ENCODING.
		//tree.get(nodeIndex.get(centralFace)).add(nodeIndex.get(otherFace));
		//tree.get(nodeIndex.get(otherFace)).add(nodeIndex.get(centralFace));

		//BFS(theQueueParent, theQueueChildren, faceToEdges, abstractCube, tree, nodeIndex, parentDegree,3, otherFace);

		//tree operation post traversal.
		tree.get(nodeIndex.get(centralFace)).remove(nodeIndex.get(otherFace));
		tree.get(nodeIndex.get(otherFace)).remove(nodeIndex.get(centralFace));

	}

/*
*  When the child complains about the parent then don't delete all the nodes at the parent level. i.e. all the parents which were in the same queue should be retained and we can remove the children.
*
*
*
*
* */
	private static Set<String> set = new HashSet<>();
	//TODO: VALIDATION PENDING
	public static void fixFaces(Stack<String> theQueueParent,Stack<String> theQueueChildren, String parentFace, HashMap<String, String>selfDirections, List<List<Integer>> tree, HashMap<String, Integer> nodeIndex,
	AbstractCube abstractCube, Map<String, Integer> parentDegree, int allowedDegree, String otherFace){
		/*
		* here you will get the very first left face and you have to permutate it.
			  I			II	    III    IV       V     VI       VII(F is not a leaf)   VIII
		** 	  f      |     f   | f |  f     | F <-f | f-> F |  f->Fv  |				F
		* f <-F-> f  | f-> F^  | F | ^F <-f |       |       |     f   |	 		  <=f->f
		* 																			f
		* So, F cannot have more than 1 connection to maintain leaf status.
		* II=IV (reflection)
		* I is unique
		* III=V=VI (they are rotated that's it).
		* so unique ones are
		* II, I, III.
		* This means that f choose 0, 1 horizontally or vertically
		* */
		//Just try fixing another face and get the count of each orientation.
		//This is for leaf's parent. The leaf's parent will try all possible arrangements and unfold in one direction.
		Map<String , Integer> map = new HashMap<>();
		map.put("+shape", 0);
		map.put("Lshape", 0);
		map.put("--shape", 0);
		map.put("-shape", 0);
		map.put("|shape", 0);
		int selfDegree=1;
		int k=1;



		List<String> availableDirections = Arrays.asList("NORTH","EAST", "WEST"); //IN the "SOUTH" Direction we will unfold.
		int countI=0;
		//this set will store the strings which indicate whether a shape has been previously seen.
		List<Integer> myCoordinates = Arrays.asList(25,25);
		//Variables
		HashMap<String, List<Integer>> dir = AbstractCube.getUnitVectors();

		/*1st try all 3 combinations
		* Write a function that tries all 3 configs
		* Then try all 2 configs write a function for this too.
		* By now you would have mostly seen all the different leaf shapes.
		* only two patterns are left. this takes the longest time and
		*
		*
		* */
		//Printing time
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		System.out.println();




		System.out.println("Entering tryAllOneConfigs");
		String shape = "+shape";
		set.add(shape);
		shape ="Lshape";
		set.add(shape);
		set.add("--shape");
//
//		tryAllTwoConfigs( theQueueParent, theQueueChildren,  parentFace, selfDirections,  tree, nodeIndex, abstractCube, parentDegree,allowedDegree, otherFace);
//		System.exit(438);
//		System.out.println("ALL UNIQUE UNFOLDING SIZE: "+ allEncodings.size());
//		System.out.println();
//		System.out.println("Entering tryAllThreeConfigs");
		tryAllTwoConfigs( theQueueParent, theQueueChildren,  parentFace, selfDirections,  tree, nodeIndex,
				abstractCube, parentDegree,allowedDegree, otherFace);
		System.out.println("ALL UNIQUE UNFOLDING SIZE: "+ allEncodings.size());
		//Printing time
		 now = LocalDateTime.now();
		System.out.println(dtf.format(now));


		now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		//System.exit(445);

		//This is taking too long.
//		for(int i=2; i>=0; i--){
//			boolean spotFilledByI=false;
//			String face1="";
//			if(i !=0 ){
//				while (countI < 3) {
//					if (edgeToFaces.get(selfDirections.get(availableDirections.get(0))).get(countI) == parentFace) {
//						countI++;
//					} else {
//						face1 = edgeToFaces.get(selfDirections.get(availableDirections.get(0))).get(countI);
//						countI++;
//						break;
//					}
//				}
//				spotFilledByI = fillSpot(myCoordinates, 0, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face1);
//				if(spotFilledByI)selfDegree++;
//				}
//
//			int countJ=0;
//			for(int j=2; j>=0; j--){
//				boolean spotFilledByJ=false;
//				String face2="";
//				if(j !=0 ){
//					while (countJ < 3) {
//						if (edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ) == parentFace) {
//							countJ++;
//						} else {
//							face2 = edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ);
//							countJ++;
//
//							break;
//						}
//
//					}
//					spotFilledByJ = fillSpot(myCoordinates, 1, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face2);
//					if(spotFilledByJ) selfDegree++;
//				}
//
//				int countZ=0;
//				for(int z =2; z>=0; z--){
//					boolean spotFilledByZ=false;
//					String face3="";
//					if(z !=0 ){
//						while (countZ < 3) {
//							if (edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ) == parentFace) {
//								countZ++;
//							} else {
//								face3 = edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ);
//								countZ++;
//								break;
//							}
//
//						}
//						spotFilledByZ = fillSpot(myCoordinates, 2, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face3);
//						if(spotFilledByZ)selfDegree++;
//					}
//					//check for the unique shape of the current fixture. save this in a set .
//						parentDegree.put(parentFace, selfDegree);
//						if(validateTheConfig(i, j, z, availableDirections, map) && !(j==0 && z==0 && i!=0)){
//							//System.out.println(map.toString());
//							System.out.println("EXPLORING "+ k++ +" CONFIG = "+" ith Value= "+face1+" jth face= "+face2+" zth Face= "+face3);
//							String southFace = otherFace;//edgeToFaces.get(selfDirections.get("SOUTH")).get(0)==parentFace?edgeToFaces.get(selfDirections.get("SOUTH")).get(1):edgeToFaces.get(selfDirections.get("SOUTH")).get(0);
//							//add the south face to the queue and call bfs.
//							theQueueParent.add(southFace);
//							//printGrid();
//							BFS(theQueueParent,  theQueueChildren, faceToEdges, abstractCube,  tree,  nodeIndex,  parentDegree, allowedDegree, otherFace);
//							System.out.println("One Config done");
//							System.exit(486);
//						}
//						else{
//							System.out.println("SKIPPING "+ k++ +" CONFIG = "+" ith Value= "+face1+" jth face= "+face2+" zth Face= "+face3);
//						}
//
//
//
//
//
//				if(spotFilledByZ) {unfillSpot(myCoordinates, 2, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face3); selfDegree--;}
//				}
//				if(spotFilledByJ) {unfillSpot(myCoordinates, 1, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face2); selfDegree--;}
//			}
//			if(spotFilledByI) {unfillSpot(myCoordinates, 0, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face1); selfDegree--;}
//		}
//		System.out.println("ALL DONE");
//		System.exit(500);
	}
	//TODO: THREE CONFIG.
	private static void tryAllThreeConfigs(Stack<String> theQueueParent,Stack<String> theQueueChildren, String parentFace, HashMap<String, String>selfDirections, List<List<Integer>> tree, HashMap<String, Integer> nodeIndex,
										   AbstractCube abstractCube, Map<String, Integer> parentDegree, int allowedDegree, String otherFace ){
		Map<String , Integer> map = new HashMap<>();
		map.put("+shape", 0);
		map.put("Lshape", 0);
		map.put("--shape", 0);
		map.put("-shape", 0);
		map.put("|shape", 0);
		int selfDegree=1;
		int k=1;



		List<String> availableDirections= Arrays.asList("NORTH","EAST", "WEST"); //IN the "SOUTH" Direction we will unfold.
		int countI=0;
		//this set will store the strings which indicate whether a shape has been previously seen.
		List<Integer> myCoordinates = Arrays.asList(25,25);
		//Variables
		HashMap<String, List<Integer>> dir = AbstractCube.getUnitVectors();
		for(int i=1; i>=1; i--){
			boolean spotFilledByI=false;
			String face1="";
			if(i !=0 ){
				while (countI < 3) {
					if (edgeToFaces.get(selfDirections.get(availableDirections.get(0))).get(countI) == parentFace) {
						countI++;
					} else {
						face1 = edgeToFaces.get(selfDirections.get(availableDirections.get(0))).get(countI);
						countI++;
						break;
					}
				}
				spotFilledByI = fillSpot(myCoordinates, 0, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face1);
				if(spotFilledByI)selfDegree++;
			}

			int countJ=0;
			for(int j=2; j>=1; j--){
				boolean spotFilledByJ=false;
				String face2="";
				if(j !=0 ){
					while (countJ < 3) {
						if (edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ) == parentFace) {
							countJ++;
						} else {
							face2 = edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ);
							countJ++;

							break;
						}

					}
					spotFilledByJ = fillSpot(myCoordinates, 1, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face2);
					if(spotFilledByJ) selfDegree++;
				}

				int countZ=0;
				for(int z =2; z>=1; z--){
					boolean spotFilledByZ=false;
					String face3="";
					if(z !=0 ){
						while (countZ < 3) {
							if (edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ) == parentFace) {
								countZ++;
							} else {
								face3 = edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ);
								countZ++;
								break;
							}

						}
						spotFilledByZ = fillSpot(myCoordinates, 2, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face3);
						if(spotFilledByZ)selfDegree++;
					}
					//check for the unique shape of the current fixture. save this in a set .
					parentDegree.put(parentFace, selfDegree);
					if(validateTheConfig(i, j, z, availableDirections, map)){
						//System.out.println(map.toString());
						//System.out.println("EXPLORING "+ k++ +" CONFIG = "+" ith Value= "+face1+" jth face= "+face2+" zth Face= "+face3);
						String southFace = otherFace;//edgeToFaces.get(selfDirections.get("SOUTH")).get(0)==parentFace?edgeToFaces.get(selfDirections.get("SOUTH")).get(1):edgeToFaces.get(selfDirections.get("SOUTH")).get(0);
						//add the south face to the queue and call bfs.
						theQueueParent.add(southFace);
						//printGrid();
						BFS(theQueueParent,  theQueueChildren, faceToEdges, abstractCube,  tree,  nodeIndex,  parentDegree, allowedDegree, otherFace);

						System.out.println("Changed the configuration+ encoding set size:"+ allEncodings.size());
						System.out.println();
						//System.exit(486);
					}
					else{
						System.out.println("SKIPPING "+ k++ +" CONFIG = "+" ith Value= "+face1+" jth face= "+face2+" zth Face= "+face3);
					}





					if(spotFilledByZ) {unfillSpot(myCoordinates, 2, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face3); selfDegree--;}
				}
				if(spotFilledByJ) {unfillSpot(myCoordinates, 1, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face2); selfDegree--;}
			}
			if(spotFilledByI) {unfillSpot(myCoordinates, 0, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face1); selfDegree--;}
		}
	}


	//TODO: TWO CONFIG.
	private static void tryAllTwoConfigs(Stack<String> theQueueParent,Stack<String> theQueueChildren, String parentFace, HashMap<String, String>selfDirections, List<List<Integer>> tree, HashMap<String, Integer> nodeIndex,
										   AbstractCube abstractCube, Map<String, Integer> parentDegree, int allowedDegree, String otherFace ){

		Map<String , Integer> map = new HashMap<>();
		map.put("+shape", 0);
		map.put("Lshape", 0);
		map.put("--shape", 0);
		map.put("-shape", 0);
		map.put("|shape", 0);
		int selfDegree=1;
		int k=1;



		List<String> availableDirections= Arrays.asList("NORTH","EAST", "WEST"); //IN the "SOUTH" Direction we will unfold.
		int countI=0;
		//this set will store the strings which indicate whether a shape has been previously seen.
		List<Integer> myCoordinates = Arrays.asList(25,25);
		//Variables
		HashMap<String, List<Integer>> dir = AbstractCube.getUnitVectors();
		for(int i=1; i<=1; i++){
			boolean spotFilledByI=false;
			String face1="";
			if(i !=0 ){
				while (countI < 3) {
					if (edgeToFaces.get(selfDirections.get(availableDirections.get(0))).get(countI) == parentFace) {
						countI++;
					} else {
						face1 = edgeToFaces.get(selfDirections.get(availableDirections.get(0))).get(countI);
						countI++;
						break;
					}
				}
				spotFilledByI = fillSpot(myCoordinates, 0, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face1);
				if(spotFilledByI)selfDegree++;
			}

			int countJ=0;
			for(int j=1; j<=2; j++){
				boolean spotFilledByJ=false;
				String face2="";
				if(j !=0 ){
					while (countJ < 3) {
						if (edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ) == parentFace) {
							countJ++;
						} else {
							face2 = edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ);
							countJ++;

							break;
						}

					}
					spotFilledByJ = fillSpot(myCoordinates, 1, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face2);
					if(spotFilledByJ) selfDegree++;
				}


					//check for the unique shape of the current fixture. save this in a set .
					parentDegree.put(parentFace, selfDegree);
					if(validateTheConfig(i, j, 0, availableDirections, map)){
						//System.out.println(map.toString());
						System.out.println("EXPLORING "+ k++ +" CONFIG = "+" ith Value= "+face1+" jth face= "+face2);
						String southFace = otherFace;//edgeToFaces.get(selfDirections.get("SOUTH")).get(0)==parentFace?edgeToFaces.get(selfDirections.get("SOUTH")).get(1):edgeToFaces.get(selfDirections.get("SOUTH")).get(0);
						//add the south face to the queue and call bfs.
						//theQueueParent.add(face2);
						//theQueueParent.add(face1);
						theQueueParent.add(otherFace);
						BFS(theQueueParent,  theQueueChildren, faceToEdges, abstractCube,  tree,  nodeIndex,  parentDegree, allowedDegree, otherFace);
						System.out.println("One Config done and the the size of all unfoldings is: "+ allEncodings.size());
						System.out.println("chain length=="+chainlength);
						//System.exit(486);
					}
					else{
						System.out.println("SKIPPING "+ k++ +" CONFIG = "+" ith Value= "+face1+" jth face= "+face2);
					}

				if(spotFilledByJ) {unfillSpot(myCoordinates, 1, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face2); selfDegree--;}
			}
			System.gc();
			if(spotFilledByI) {unfillSpot(myCoordinates, 0, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face1); selfDegree--;}
		}


		int countJ=0;
		for(int j=1; j>=1; j--){
			boolean spotFilledByJ=false;
			String face2="";
			if(j !=0 ){
				while (countJ < 3) {
					if (edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ) == parentFace) {
						countJ++;
					} else {
						face2 = edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ);
						countJ++;

						break;
					}

				}
				spotFilledByJ = fillSpot(myCoordinates, 1, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face2);
				if(spotFilledByJ) selfDegree++;
			}

			int countZ=0;
			for(int z =2; z>=1; z--){
				boolean spotFilledByZ=false;
				String face3="";
				if(z !=0 ){
					while (countZ < 3) {
						if (edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ) == parentFace) {
							countZ++;
						} else {
							face3 = edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ);
							countZ++;
							break;
						}

					}
					spotFilledByZ = fillSpot(myCoordinates, 2, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face3);
					if(spotFilledByZ)selfDegree++;
				}
				//check for the unique shape of the current fixture. save this in a set .
				parentDegree.put(parentFace, selfDegree);
				if(validateTheConfig(0, j, z, availableDirections, map)){
					//System.out.println(map.toString());
					//System.out.println("EXPLORING "+ k++ +" CONFIG = "+" jth face= "+face2+" zth Face= "+face3);
					String southFace = otherFace;//edgeToFaces.get(selfDirections.get("SOUTH")).get(0)==parentFace?edgeToFaces.get(selfDirections.get("SOUTH")).get(1):edgeToFaces.get(selfDirections.get("SOUTH")).get(0);
					//add the south face to the queue and call bfs.
					theQueueParent.add(southFace);
					//printGrid();
					//System.out.println("GOING FOR BFS");
					BFS(theQueueParent,  theQueueChildren, faceToEdges, abstractCube,  tree,  nodeIndex,  parentDegree, allowedDegree, otherFace);
					//System.out.println("One Config done and the size of all the unique unfoldings is: "+ allEncodings.size());
					System.out.println("One iteration done all encoding size = "+allEncodings.size());
				}
				else{
					System.out.println("SKIPPING "+ k++ +" CONFIG = "+" jth face= "+face2+" zth Face= "+face3);
				}





				if(spotFilledByZ) {unfillSpot(myCoordinates, 2, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face3); selfDegree--;}
			}
			if(spotFilledByJ) {unfillSpot(myCoordinates, 1, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face2); selfDegree--;}
		}


	}



	private static void tryAllOneConfigs(Stack<String> theQueueParent,Stack<String> theQueueChildren, String parentFace, HashMap<String, String>selfDirections, List<List<Integer>> tree, HashMap<String, Integer> nodeIndex,
										 AbstractCube abstractCube, Map<String, Integer> parentDegree, int allowedDegree, String otherFace ){

		Map<String , Integer> map = new HashMap<>();
		map.put("+shape", 0);
		map.put("Lshape", 0);
		map.put("--shape", 0);
		map.put("-shape", 0);
		map.put("|shape", 0);
		int selfDegree=1;
		int k=1;



		List<String> availableDirections= Arrays.asList("NORTH","EAST", "WEST"); //IN the "SOUTH" Direction we will unfold.
		int countI=0;
		//this set will store the strings which indicate whether a shape has been previously seen.
		List<Integer> myCoordinates = Arrays.asList(25,25);
		//Variables
		HashMap<String, List<Integer>> dir = AbstractCube.getUnitVectors();




		int countZ=0;
		for(int z =2; z>=1; z--){
			boolean spotFilledByZ=false;
			String face3="";
			if(z !=0 ){
				while (countZ < 3) {
					if (edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ) == parentFace) {
						countZ++;
					} else {
						face3 = edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ);
						countZ++;
						break;
					}

				}
				spotFilledByZ = fillSpot(myCoordinates, 2, dir, availableDirections, parentFace, selfDirections, tree, nodeIndex, face3);
				if(spotFilledByZ)selfDegree++;
			}
			//check for the unique shape of the current fixture. save this in a set .
			parentDegree.put(parentFace, selfDegree);
			if(validateTheConfig(0, 0, z, availableDirections, map)){
				//System.out.println(map.toString());
				System.out.println("EXPLORING "+ k++ +" CONFIG = "+" zth Face= "+face3);
				String southFace = otherFace;//edgeToFaces.get(selfDirections.get("SOUTH")).get(0)==parentFace?edgeToFaces.get(selfDirections.get("SOUTH")).get(1):edgeToFaces.get(selfDirections.get("SOUTH")).get(0);
				//add the south face to the queue and call bfs.
				theQueueParent.add(face3);
				//printGrid();
				BFS(theQueueParent,  theQueueChildren, faceToEdges, abstractCube,  tree,  nodeIndex,  parentDegree, allowedDegree, otherFace);
				System.out.println("One Config done");
				System.out.println();
				//System.exit(486);
			}
			else{
				System.out.println("SKIPPING "+ k++ +" CONFIG = "+" zth Face= "+face3);
			}

			if(spotFilledByZ) {unfillSpot(myCoordinates, 2, dir, availableDirections, parentFace, selfDirections, theQueueChildren, tree, nodeIndex, face3); selfDegree--;}
		}


	}



	//TODO: VALIDATION PENDING. CHECK WHETHER YOU ARE COVERING ALL COMBINATIONS
	/*
	* Things to take care of :
	* Write a function that will consider the
	*
	*
	*
	* */

	/*
	*  F -ABCD leafFace
	* 	f- otherface
	*
	*
	* */
	private static boolean validateTheConfig(int iValue, int jValue, int zValue, List<String> availableDirections,  Map<String, Integer> map){
		// "NORTH","EAST", "WEST"
		String shape="";
		System.out.println("THE ORIENTATIONS EXPLORED"+set.toString());
		if(iValue!=0 && jValue!=0 && zValue!=0) {
			shape="+shape";
			map.put(shape, map.get(shape)+1);
			if(map.get(shape)==8){

			return set.add(shape);
			}
			return true;
		}
		else if(iValue!=0 && jValue!=0 && zValue==0) {
			shape = "Lshape";
			map.put(shape, map.get(shape)+1);
			if(map.get(shape)==4){

				return set.add(shape);

			}
			return true;
		}
		else if(jValue!=0 && zValue!=0 && iValue==0){
			shape = "--shape";
			map.put(shape, map.get(shape)+1);
			if(map.get(shape)==4){

				return set.add(shape);

			}
			return true;

		}
		else if(zValue!=0 && jValue==0 && iValue==0){
			shape= "-shape";
			map.put(shape, map.get(shape)+1);
			if(map.get(shape)==2){

				return set.add(shape);

			}
			return true;


		}

		else if(iValue!=0 &&jValue==0 && zValue==0){
			shape ="|shape";
		map.put(shape, map.get(shape)+1);
		if(map.get(shape)==2){

			return set.add(shape);

		}
		return true;}

		return false;

	}

	private static int faceNumber = 1;
	private static int numberofsol=0;
	private static int depth=0;
	static int numberofUniqueSol= 1;



	//public static int index=1;
	private static int callID=0;
	private static Set<String> allLeaves= new HashSet<>();
	public static String BFS(Stack<String> theQueueParent, Stack<String> theQueueChildren, HashMap<String, List<String>> faceToEdges,
						   AbstractCube abstractCube, List<List<Integer>> tree, HashMap<String, Integer> nodeIndex, Map<String, Integer> parentDegree, int allowedDegree, String otherFace) {

		//System.out.println(callID++);
		//termination condition.
		if (theQueueParent.size() == 0) {

			for (String face : visited.keySet()) if (!visited.get(face)) return "";

			profilingCounter++;

			long startTime = System.currentTimeMillis();

			CheckForUniqueShape(tree, nodeIndex);

			long endTime = System.currentTimeMillis();

//			if(allEncodings.size()==398347){
//				printGrid();
//				System.exit(0);
//			}

			//if(profilingCounter%50000==0)System.out.println("FUNCTION CALL TO CheckForUniqueShape TAKES: "+ (endTime-startTime)+" the size of all encodings is:"+allEncodings.size());

				//printGrid();
//				System.out.println("Tree tostring   "+tree.toString());
//				System.out.println("node Idx:      "+ nodeIndex.toString());
//				System.out.println("Parent Degree "+ parentDegree.toString());

				//System.out.println("BREAKING HERE YOO HOO");
				//System.exit(0);
//			if(allEncodings.size()==150){
//				printGrid();
//				System.exit(2);
//			}
			return "";

		}

		String self = theQueueParent.pop();// this is the face we are dealing with rn.x
		//Cloning
		//System.out.println("Face= "+self+"call id= "+callID++);
		Stack<String> localStackParent = (Stack<String>)theQueueParent.clone();
		Stack<String>  localStackChildren = (Stack<String>)theQueueChildren.clone();

        boolean spotFilledByI = false, spotFilledByJ = false, spotFilledByZ = false;
        String parentDirection =""; //north, west, east, south.

        HashMap<String, String> selfDirections = abstractCube.getSelfDirections(self, faceToEdges);// note you have been

        String parentSharingEdge = abstractCube.getParentSharingEdge(self);// store key value as key being the child and
        // the value the edge that the child and
        // parent share.
		String parentName= AbstractCube.getEdgeToParent().get(parentSharingEdge);



		List<Integer> myCoordinates = abstractCube.getCoordinatesOfTheFaces(self);// [x,y] coordinates
		HashMap<String, List<Integer>> dir = abstractCube.getUnitVectors();// this returns a hash table of NORTH ->
		List<String> edgesExploredByTheCurrFace = new ArrayList<>();
		List<String> availableDirections = new ArrayList<>();
		//boolean generationParity= parity;


		for (String direction : selfDirections.keySet()) {
			if (selfDirections.get(direction) == parentSharingEdge) {
				parentDirection = direction;
				continue;
			}
			availableDirections.add(direction);

		}






		// creating combinations of
		int countI = 0;
		int selfDegree=1;
		boolean parityI, parityJ, parityZ;
		for (int i = 2; i >= 0; i--) {
			String face1 = "";
			spotFilledByI = false;
			parityI=false;
			if(i !=0 ){
				while (countI < 3) {
					if (edgeToFaces.get(selfDirections.get(availableDirections.get(0))).get(countI) == self) {
						countI++;
					} else {
						face1 = edgeToFaces.get(selfDirections.get(availableDirections.get(0))).get(countI);
						countI++;
						break;
					}
				}


				spotFilledByI = fillSpot(myCoordinates, 0, dir, availableDirections, self, selfDirections, tree, nodeIndex, face1);
				if(!spotFilledByI) continue;
				else selfDegree++;
				parityI=true;


				/*if(self.equals("DdHh")){
					for (int col = 0; col < 60; col++) {
						for (int row = 0; row < 60; row++) {
							if (col == 0) {
								if (row % 10 == 0) {

									System.out.print(row / 10);
								} else System.out.print(grid[col][row]);
							} else {
								System.out.print(grid[col][row]);
							}
						}
						System.out.println();
						System.out.print(col + 1);
					}
					System.out.println("sport filled by i: "+grid[myCoordinates.get(0) + dir.get(availableDirections.get(0)).get(0)][myCoordinates.get(1)
							+ dir.get(availableDirections.get(0)).get(1)]);
				}*/

			}

			int countJ = 0;
			for (int j = 2; j >= 0; j--) {
				String face2 = "";
				spotFilledByJ = false;
				parityJ= false;
				if(j!=0){
					while (countJ < 3) {
						if (edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ) == self)
							countJ++;
						else {
							face2 = edgeToFaces.get(selfDirections.get(availableDirections.get(1))).get(countJ);
							countJ++;
							break;
						}
					}


					spotFilledByJ = fillSpot(myCoordinates, 1, dir, availableDirections, self, selfDirections, tree, nodeIndex, face2);
					if(!spotFilledByJ)continue;
					else selfDegree++;
					parityJ=true;
				}


				int countZ = 0;
				for (int z = 2; z >= 0; z--) {
					//System.out.println("Entered z: "+ z);
					String face3 = "";
					//if(otherFace.equals(self))face3= face3;
					//if(self.equals(otherFace))//
						// System.out.println("The Z value is "+ z);
					spotFilledByZ = false;
					parityZ=false;

					if(z!=0){
						while (countZ < 3) {
							if (edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ) == self) {
								countZ++;
							} else {
								face3 = edgeToFaces.get(selfDirections.get(availableDirections.get(2))).get(countZ);
								countZ++;
								break;
							}
						}

						//unfolding in z direction.
						spotFilledByZ = fillSpot(myCoordinates, 2, dir, availableDirections, self, selfDirections, tree, nodeIndex, face3);
						if(!spotFilledByZ)continue;
						else selfDegree++;
						parityZ=true;
					}

						parentDegree.put(self, selfDegree);
//					System.out.println(" self at depth "+ depth+"  "+ self);
//					if(self.equals("GHCD")){
//						System.out.println("???");
//						System.out.println("face1: "+ face1);
//						System.out.println("Face2: "+ face2);
//						System.out.println("Face3: "+ face3);
//
//						System.out.println("&&&");
//					}
//					if(self.equals("GFHE")){
//						System.out.println("The parent of GFHE expecting GHCD "+ parentName);
//
//					}
						//if this is a leaf face, and its parent has more than the permissible count we want the parent to change its orientations to facilitate the new answer.
							//if(self.equals("DdHh")) System.out.println("DdHd------"+selfDegree +" i= "+i+ " j= "+j +" z= "+z);

							//if(self.equals("Ae"))
					//System.out.println("6^^^^^");
					//System.out.println("The state of the parent queue is: "+ theQueueParent.toString()+ " and the child queue looks so: "+ theQueueChildren.toString());


					if((self.equals(otherFace)) || !self.equals(otherFace)) {
						//if(depth==1) System.out.println(self+" THIS IS NEW 1 LEVEL CONFIG "+ allEncodings.size()+" "+theQueueParent.toString());
								//System.out.println("I am "+self+", and I am adding faces:");
								if(spotFilledByI && parityI){
									//System.out.println("faceI = "+ face1);
									theQueueChildren.add(face1); parityI=false;}
								if(spotFilledByJ && parityJ){/*System.out.println("faceJ = "+ face2);*/
									theQueueChildren.add(face2); parityJ=false;}
								if(spotFilledByZ && parityZ){ //System.out.println("faceZ = "+ face3);
									theQueueChildren.add(face3); parityZ=false;}
								//System.out.println("The state of the parent queue is: "+ theQueueParent.toString()+ " and the child queue looks so: "+ theQueueChildren.toString());
								//System.out.println();
								String response = "";
								if (selfDegree == 1) {

									/*TODO: Here we know that its a leaf and we want to call the function to check if the parent is realized.*/
									if(isParentConfigCompleted(parentName, faceAndNeighbours, set, self)){
												return parentName;}

									else{
											depth++;
											if (theQueueParent.size() != 0)
												response = BFS(theQueueParent, theQueueChildren, faceToEdges, abstractCube, tree, nodeIndex, parentDegree, allowedDegree, otherFace);
											else
												response = BFS(theQueueChildren, theQueueParent, faceToEdges, abstractCube, tree, nodeIndex, parentDegree, allowedDegree, otherFace );
											depth--;
										}

									}

								else {


									depth++;
									if (theQueueParent.size() != 0)
										response = BFS(theQueueParent, theQueueChildren, faceToEdges, abstractCube, tree, nodeIndex, parentDegree, allowedDegree, otherFace);
									else
										response = BFS(theQueueChildren, theQueueParent, faceToEdges, abstractCube, tree, nodeIndex, parentDegree, allowedDegree, otherFace);
									depth--;
								}
								//if face is returned then the face has to move to the next configuration
								//System.out.println("SELF "+ self+ " Response  "+ response);


								//RESPONSE AREA
								if (response.equals(self)) {
									//cloning
									while(!theQueueChildren.isEmpty())theQueueChildren.pop();
									while(!theQueueParent.isEmpty())theQueueParent.pop();
//									while(!localStackChildren.isEmpty())theQueueChildren.add(localStackChildren.pop());
//									while(!localStackParent.isEmpty())theQueueParent.add(localStackParent.pop());
//									localStackChildren = (Stack<String>)theQueueChildren.clone();
//									localStackParent = 	(Stack<String>)theQueueParent.clone();
									for(int it=0; it<localStackChildren.size(); it++)theQueueChildren.add(localStackChildren.get(it));
									for(int it=0; it<localStackParent.size(); it++) theQueueParent.add(localStackParent.get(it));

									parityI=true; parityJ=true; parityZ=true;

									//System.out.println(self+" here, i am going back. ");

									//System.out.println("Reached the self "+ self+" at depth "+depth+ " self degree "+ selfDegree);
									//printGrid();


									//System.out.println("Reported :"+self);
									//if(self.equals("DdHh"))System.out.println("&&&&&&&&&&&&face is here "+ self+ " depth "+ depth);
									}

								else if (!response.equals("")) {
										//if(parity.get(self)==parity.get(response))theQueueParent.add(self);
										//else parity.remove(self);

									//if(self.equals("DdHh"))System.out.println("&&&&&&&&&&&face is here "+ self+ " depth "+ depth+ " response "+response);
									if (spotFilledByZ) {
										unfillSpot(myCoordinates, 2, dir, availableDirections, self, selfDirections, theQueueChildren, tree, nodeIndex, face3);
										selfDegree--;
									}
									if (spotFilledByJ) {
										unfillSpot(myCoordinates, 1, dir, availableDirections, self, selfDirections, theQueueChildren, tree, nodeIndex, face2);
										selfDegree--;
									}
									if (spotFilledByI) {
										unfillSpot(myCoordinates, 0, dir, availableDirections, self, selfDirections, theQueueChildren, tree, nodeIndex, face1);
										selfDegree--;
									}
//									parentDegree.remove(self, selfDegree);

//									for(String edge: edgesExploredByTheCurrFace)
//									{	//visitedEdges.put(edge,false);
//										AbstractCube.removeEdgeToParent(edge);
//
//									}
//									AbstractCube.removeFaceAndItsDirections(self);
								//	System.out.println(self+" here, i am going back. ");

									return response;
								}

								else {
									//if(theQueueParent.size()!=0 ||theQueueChildren.size()!=0)continue;
								}
							}


					//last condition before z closes.
					if (spotFilledByZ) {

						unfillSpot(myCoordinates, 2, dir, availableDirections, self, selfDirections, theQueueChildren, tree, nodeIndex, face3);
						selfDegree--;
				;
					}
					}//z for termination
					//last condition  before j closes.
					if (spotFilledByJ) {

						//System.out.println("For "+self+" self degree reduced "+ selfDegree+ " to "+ (selfDegree-1));
						unfillSpot(myCoordinates, 1, dir, availableDirections, self, selfDirections, theQueueChildren, tree, nodeIndex, face2);
						selfDegree--;
					}
					}
					//last condition before i closes.
					if (spotFilledByI) {

						//System.out.println("For "+ self+" self degree reduced "+ selfDegree+ " to "+ (selfDegree-1));
						unfillSpot(myCoordinates, 0, dir, availableDirections, self, selfDirections, theQueueChildren, tree, nodeIndex, face1);
						selfDegree--;
					}
					}

		for(String edge: edgesExploredByTheCurrFace)
		{	//visitedEdges.put(edge,false);
			AbstractCube.removeEdgeToParent(edge);

		}
		AbstractCube.removeFaceAndItsDirections(self);
		//System.out.println(self+" exiting from the last return statement "+theQueueChildren.toString()+" Parent queue "+theQueueParent.toString());

		return "";



	}

	/*
	 * TODO: check if a leaf's parent has the same config as that you have seen before.
	 *  Idea:
	 * 		once you reach a leaf face just query whether all the children of the parent are leaves or not:
	 * 			i) This can be determined if you have some data structure that maintains realized leaves.
	 * 			ii) there can be few realized and few which are not. If so then hold back pruning.
	 *		So with this you can now check if a parent is a previously seen config.
	 * 	How to Identify the shape of the parent.
	 * 		i) maintain a map that would tell you for each face which all faces it unfolded and in which direction.(I guess you have this)
	 * 		ii) check in the set, if the config is already seen and check whether the parent config can be mapped to that.
	 * 			return true if the config is already seen or else false if that is not completely realized parent. To do that call only if the parent is completely realized.
	 *
	 * 	TODO: Requirements:
	 *   		1) Find the data structure that is maintaining the structure of the unfolding as it is.
	 * 			2) To check if the parent is FULLY REALIZED maintains a set of all leaves that you have seen.
	 * 				Query in this set if this config is already done hit back the parent.
	 *
	 *
	 *
	 *
	 * */
	//These functions are for checking if the current config is seen.
	//TODO: VALIDATE THIS FUNCTION.
	public static boolean isParentRealized(String parentName, HashMap<String, HashMap<String, String>> faceAndNeighbours, Set<String> allLeaves){
		//face and neighbours cotains all direction nodes.
		int mainbrach=0;

		for(String direction: faceAndNeighbours.get(parentName).keySet()){

			if( mainbrach>1 &&!allLeaves.contains(faceAndNeighbours.get(parentName).get(direction)))return false;
			if(!allLeaves.contains(faceAndNeighbours.get(parentName).get(direction))) mainbrach++;
		}
		if(mainbrach>1)return false;

		return true;

	}

	//TODO: VALIDATE THIS FUNCTION.
	public static boolean isParentConfigCompleted(String parentName, HashMap<String, HashMap<String, String>> faceAndNeighbours, Set<String> completedConfig, String leafFace) {

		Set<String> parentsLeafFacesDirections = new HashSet<>();
		String mainBranchDirection = "";
		String leafFaceDir="";
		for(String direction: faceAndNeighbours.get(parentName).keySet()){

			if(!faceAndNeighbours.get(parentName).get(direction).equals(leafFace)){
				parentsLeafFacesDirections.add(direction);
			}
			else{
				leafFaceDir= direction;
			}

		}
		String config = findTheConfig(parentsLeafFacesDirections, leafFaceDir);
		return completedConfig.contains(config);

	}
	//TODO: VALIDATE THIS FUNCTION.
	public static String findTheConfig(Set<String> directionSet, String leafFaceDir){
		if(directionSet.size()==3)return "+shape";
		if(directionSet.size()==2){
			if((directionSet.contains("NORTH")||directionSet.contains("SOUTH")) && directionSet.contains("EAST")||directionSet.contains("WEST")) return "--shape";
			else return "Lshape";
		}
		if(directionSet.contains(oppositeDirection(leafFaceDir))) return "|shape";
		return "-shape";


	}


	public static void printGrid(){
										for (int col = 0; col < 50; col++) {
												for (int row = 0; row < 50; row++) {
													if (col == 0) {
														if (row % 10 == 0) {

															System.out.print(row / 10);
														} else System.out.print(grid[col][row]);
													} else {
														System.out.print(grid[col][row]);
													}
												}
												System.out.println();
												System.out.print(col + 1);
											}
	}



	private static boolean fillSpot(List<Integer> myCoordinates, int index, HashMap<String, List<Integer>> dir, List<String> availableDirections, String self, HashMap<String, String> selfDirections,
									List<List<Integer>> tree, HashMap<String, Integer> nodeIndex, String face1){



			if (!grid[myCoordinates.get(0) + dir.get(availableDirections.get(index)).get(0)][myCoordinates.get(1)
					+ dir.get(availableDirections.get(index)).get(1)].equals( "-------")) return false;




			if (face1.equals(self) || face1.equals("")) {
				System.out.println(
						"Did not get the correct neighbour face at i= " + index + " the face obtains is: " + face1);
				System.exit(0);
			}

			if (visited.get(face1) || visitedEdges.get(selfDirections.get(availableDirections.get(index)) )) return false;

			AbstractCube.addEdgeToParent(selfDirections.get(availableDirections.get(index)), self);

			// there are some operations thw parent has to do before putting the face in
			// theQueue.
			//remove this as you need to update this at the end of each loop.
			AbstractCube.addFaceToParentSharingEdge(face1, selfDirections.get(availableDirections.get(index)));
			AbstractCube.addToCoordinatesOfTheFaces(face1,
					Arrays.asList(myCoordinates.get(0) + dir.get(availableDirections.get(index)).get(0),
							myCoordinates.get(1) + dir.get(availableDirections.get(index)).get(1)));

			visited.put(face1, true);
			visitedEdges.put(selfDirections.get(availableDirections.get(index)), true);
			//System.out.println("Parent " + self + " submitted this face at i level to the queue " + face1);
			//theQueueChildren.add(face1);

			grid[myCoordinates.get(0) + dir.get(availableDirections.get(index)).get(0)][myCoordinates.get(1)
					+ dir.get(availableDirections.get(index)).get(1)] = depth+face1 +directionArrows.get(availableDirections.get(index))+" ";
			//tree operations.
			tree.get(nodeIndex.get(self)).add(nodeIndex.get(face1));
			tree.get(nodeIndex.get(face1)).add(nodeIndex.get(self));
			faceAndNeighbours.get(self).put(availableDirections.get(index), face1);
			faceAndNeighbours.get(face1).put(oppositeDirection(availableDirections.get(index)), self);

		return true;
	}

	private static void unfillSpot(List<Integer> myCoordinates, int index, HashMap<String, List<Integer>> dir, List<String> availableDirections, String self, HashMap<String, String> selfDirections, Stack<String> theQueueChildren,
								   List<List<Integer>> tree, HashMap<String, Integer> nodeIndex, String face1 ){

		visited.put(face1, false);
		grid[myCoordinates.get(0) + dir.get(availableDirections.get(index)).get(0)][myCoordinates.get(1)
				+ dir.get(availableDirections.get(index)).get(1)] = "-------";
		visitedEdges.put(selfDirections.get(availableDirections.get(index)), false);
		AbstractCube.removeEdgeToParent(selfDirections.get(availableDirections.get(index)));
		AbstractCube.removeFaceToParentSharingEdge(face1);
		AbstractCube.removeCoordinatesOfTheFaces(face1);
		theQueueChildren.remove(face1);
		//tree operations.
		tree.get(nodeIndex.get(self)).remove(nodeIndex.get(face1));

		tree.get(nodeIndex.get(face1)).remove(nodeIndex.get(self));

		faceAndNeighbours.get(self).remove(availableDirections.get(index), face1);
		faceAndNeighbours.get(face1).remove(oppositeDirection(availableDirections.get(index)), self);
	}

private static long repeat=0;
	private static void CheckForUniqueShape( List<List<Integer>> tree, HashMap<String, Integer> nodeIndex){

		//System.out.println("Reached check for unique solutions");


		boolean ans= treesAreIsomorphic(tree, nodeIndex);
		numberofUniqueSol= allEncodings.size();
//
//		if(allEncodings.size()>261){
//			System.out.println("ALL ENCODINGS SIZE IS GREATER THAN 261");
//			System.exit(0);
//		}
	//	System.out.println("ALL ENCODINGS SIZE :"+allEncodings.size());
		if(ans){
			//System.out.println("repeat "+ repeat++);

		}else{
			//System.out.println("Unique "+ numberofUniqueSol);
		}


		return;
	}

	public static boolean treesAreIsomorphic(List<List<Integer>> tree, HashMap<String, Integer> nodeIndex) {

		List<Integer> seen = new ArrayList<>();
		for(int i=0; i<24; i++)seen.add(0);

		HashMap<Integer, String> nodeIndexReverse  = new HashMap<>();
		for(String face: nodeIndex.keySet()){
			nodeIndexReverse.put(nodeIndex.get(face), face);
		}

		//find the center of the tree. this will be a list of nodes.
		// do a dfs on the tree and find the direction in which the number of nodes in all the directions.
		//this will be a list of nodes.
		List<Integer> centers = findTheCenterOfTheTree(tree, nodeIndex);
		HashMap<String,Integer> depths = new HashMap<>(4);
		depths.put("NORTH", 0);
		depths.put("SOUTH", 0);
		depths.put("WEST", 0);
		depths.put("EAST", 0);

		String encoding="";

		for(Integer center: centers){
			seen.set(center, 1);
			HashMap<String, String> centerDirection = AbstractCube.getFaceAndItsDirections().get(nodeIndexReverse.get(center));
			//grid[AbstractCube.getCoordinatesOfTheFaces(nodeIndexReverse.get(center)).get(0)][AbstractCube.getCoordinatesOfTheFaces(nodeIndexReverse.get(center)).get(1)]="C";
			//System.out.println(faceAndNeighbours.toString());//DdCc={SOUTH=DAda}, EeFf={NORTH=aeAE, WEST=fegh, SOUTH=FfBb}, DdHh={NORTH=GHCD, SOUTH=heda}, EeHh={NORTH=GHgh, WEST=DHAE, EAST=heda}, FfBb={NORTH=EeFf, SOUTH=AaBb, EAST=FGBC}, GgCc={EAST=cgbf}, GgFf={NORTH=cgbf}, FGBC={WEST=FfBb}, GHCD={SOUTH=DdHh}, GHgh={SOUTH=EeHh}, DAda={NORTH=DdCc, WEST=heda, SOUTH=aeAE}, aeAE={NORTH=DAda, WEST=aebf, SOUTH=EeFf, EAST=BFAE}, aebf={WEST=cgbf, EAST=aeAE}, cgbf={WEST=GgCc, SOUTH=GgFf, EAST=aebf}, heda={NORTH=DdHh, WEST=EeHh, EAST=DAda}, cdgh={SOUTH=cbda}, fegh={EAST=EeFf}, cbda={NORTH=cdgh, WEST=CcBb}, BFAE={NORTH=ADBC, WEST=aeAE, SOUTH=GFHE}, DHAE={EAST=EeHh}, ADBC={SOUTH=BFAE, EAST=CcBb}, GFHE={NORTH=BFAE}, AaBb={NORTH=FfBb}, CcBb={WEST=ADBC, EAST=cbda}}

			for(String dir: faceAndNeighbours.get(nodeIndexReverse.get(center)).keySet()) {
				if(!faceAndNeighbours.get(nodeIndexReverse.get(center)).containsKey(dir))continue;
				//System.out.println(" neighbouring face in the direction: "+"  "+dir);
				int nodeToExplore = nodeIndex.get(faceAndNeighbours.get(nodeIndexReverse.get(center)).get(dir));

				int nodeVal =0;
				//tree is correct till here.
				//System.out.println("node to exploree :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: "+ nodeToExplore);
				int depth = findNumberOfNodes(tree, center, nodeToExplore, seen);


				depths.put(dir, depth);
			}

			List<String> north= new ArrayList<>();
			List<String> west = new ArrayList<>();
			String maxDir = "";
			int maxNumberOfNodes=-1;
			// the highest number of nodes in a particular direction
			for(String dir: depths.keySet()){
				if(maxNumberOfNodes<depths.get(dir)) maxDir = dir;
			}

			north.add(maxDir);

			for(String dir: depths.keySet()){
				if(depths.get(dir).equals(depths.get(maxDir)) && !maxDir.equals(dir))
					north.add( dir);
			}
			//taken all possible norths.
			//now for each north we have to permute and find the correct west direction and perform the encoding on that.
			//find the west direction.
			Set<String> set;//this set will contain
			for(String dir: north){
				set = new HashSet<String>();
				set.add(dir);
				set.add(oppositeDirection(dir));

				for(String dir2: depths.keySet()){
					if(!set.contains(dir2)){
						if(depths.get(dir2) != depths.get(oppositeDirection(dir2))){
							west.add(depths.get(dir2)>depths.get(oppositeDirection(dir2))?dir2:oppositeDirection(dir2));

						}else {
							west.add(dir2);
							west.add(oppositeDirection(dir2));

						}
						break;
					}

				}
				for(String directionWest: west){
					//code is a will return a string of encoding.
					Set<String> visitedFaces = new HashSet<>();

					encoding = encode(dir, directionWest, nodeIndexReverse.get(center), visitedFaces, 0);


					//System.out.println("NEW ENCODING AND ITS MATRIX BELOW.");
					//System.out.println("THE DEPTH OF TREES IN EACH DIRECTIONS IS: "+depths.toString());
					//System.out.println("The new West is the old "+west.toString()+" new north is the old: "+ north.toString()+" center is"+centers.toString());

					//System.out.println(encoding);
					//System.out.println("The center is: "+ nodeIndexReverse.get(center)+" the new north is: "+ dir+ " the new west is: "+ directionWest+ " FYI: the encoding pattern{north,south,east, west} these. These directions are the new directions");
//					for(int i=0; i<60; i++){
//						for(int j =0; j<60; j++){
//							System.out.print(grid[i][j]);
//						}
//						System.out.println();
//					}

					//create a set of these encodings and check if it is present.\\\


					if(allEncodings.contains(encoding)){
//
//						System.out.println(encoding);
//						for(int i=0; i<60; i++){
//						for(int j =0; j<60; j++){
//							System.out.print(grid[i][j]);
//						}
//						System.out.println();
//					}
//						System.exit(0);
						return true;
					}

				}





			}



			//AbstractCube.getFaceAndItsDirections().toString();

//		String encoding = encodeTree(tree, nodeIndex);
//		if(allTreeEncodings.containsKey(encoding)) return true;
//		allTreeEncodings.put(encoding, numberofUniqueSol++);
//		return false;

		}
		//System.out.println("Adding an encoding "+ encoding);

//		try{
//			File myObj = new File("file"+fileid +".txt");
//		if (myObj.createNewFile()) {
//			System.out.println("File created: " + myObj.getName());
//		} else {
//			System.out.println("File already exists.");
//		}
//			FileWriter myWriter = new FileWriter("file"+fileid++ +".txt");
//			myWriter.write(""+depth);
//			myWriter.close();
//		}
//		catch(IOException e) {
//			System.out.println(e);
//		}
		//logging.log(Level.INFO, encoding);



		boolean bool= allEncodings.add(encoding);

		numberofUniqueSol++;
		if(numberofUniqueSol%100000==0) System.out.println("numberofUniqueSol"+numberofUniqueSol +" "+ allEncodings.size());
		//System.out.println("Returning from the treesAreIsometric function");
		return false;
	}

	public static String encode(String northDir,String westDir, String center, Set<String> visitedFaces, int level){
		//System.out.println("IN ENCODING FUNCTION");
		visitedFaces.add(center);
		HashMap<String, String> resultFromEachDirection = new HashMap<>();
		resultFromEachDirection.put("NORTH", "~");
		resultFromEachDirection.put("SOUTH", "~");
		resultFromEachDirection.put("EAST", "~");
		resultFromEachDirection.put("WEST", "~");
		boolean flag= false;
		for(String direction :faceAndNeighbours.get(center).keySet()){
			if(!visitedFaces.contains(faceAndNeighbours.get(center).get(direction))){

				resultFromEachDirection.put(direction, encode(northDir, westDir, faceAndNeighbours.get(center).get(direction), visitedFaces, level+1));
				flag = true;
			}
		}
		if(flag){
			String s ="("+ resultFromEachDirection.get(northDir)+resultFromEachDirection.get(oppositeDirection(northDir))+resultFromEachDirection.get(oppositeDirection(westDir))+resultFromEachDirection.get(westDir)+")";

			//	System.out.println(s+ level);
			//System.out.println("Returning from the encoding function");
			return s;
		}
	//	System.out.println("Returning from the encoding function");
		//System.out.println(center+level);
		return "()";


	}

	private static String oppositeDirection(String dir){
		if(dir=="NORTH")return "SOUTH";
		else if(dir=="EAST")return "WEST";
		else if(dir=="SOUTH")return "NORTH";
		return "EAST";

	}

	public static int findNumberOfNodes(List<List<Integer>> tree, int parent, int child , List<Integer> seen){
		if(seen.get(child)==1)return 0;
		seen.set(child, 1);
		//System.out.println("-----------------------------------");

		//System.out.println(index++ +" " +parent +": its children "+tree.get(child));
		if(tree.get(child).size() <=1){
			return 1;
		}
		int count=0;
		//System.out.println("visited: "+ child);
		for(int val=0; val<tree.get(child).size(); val++){
			if(parent==tree.get(child).get(val))continue;

			count = count+ findNumberOfNodes(tree, child, tree.get(child).get(val), seen);
		}
		return count+1;
	}

	public static List<Integer> findTheCenterOfTheTree(List<List<Integer>>tree, HashMap<String, Integer>  nodeIndex) {
			final int n = tree.size();
			int[] degree = new int[n];

			// Find all leaf nodes
			List<Integer> leaves = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				List<Integer> edges = tree.get(i);
				degree[i] = edges.size();
				if (degree[i] <= 1) {
					leaves.add(i);
					degree[i] = 0;
				}
			}

			int processedLeafs = leaves.size();

			// Remove leaf nodes and decrease the degree of each node adding new leaf nodes progressively
			// until only the centers remain.
			while (processedLeafs < n) {
				List<Integer> newLeaves = new ArrayList<>();
				for (int node : leaves) {
					for (int neighbor : tree.get(node)) {
						if (--degree[neighbor] == 1) {
							newLeaves.add(neighbor);
						}
					}
					degree[node] = 0;
				}
				processedLeafs += newLeaves.size();
				leaves = newLeaves;
			}

			return leaves;


	}



}
