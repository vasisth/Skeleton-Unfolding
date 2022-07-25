import CubeObjects.AbstractCube;
import CubeObjects.DualNode;
import CubeObjects.Face;

import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

import CubeObjects.*;
import org.w3c.dom.ls.LSOutput;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class NaiveSolution {


    private static final List<String> ans = new ArrayList<>();
    private static final List<String> validUnfoldings = new ArrayList<>();
    private static String[][] grid;
    private static final HashMap<String, Boolean> visitedEdges = new HashMap<>();
    private static HashMap<String, Boolean> visited = new HashMap<>();
    private static final HashMap<String, List<String>> edgeToFaces = new HashMap<>();
    private static final HashMap<String, List<Integer>> coordinatesOfTheFaces = new HashMap<>();
    private static final Queue<String> colorPallet = new LinkedList<>();
    private static final HashMap<String, String> directionArrows = new HashMap<>();


    public static void main(String[] args) {

        grid = new String[60][60];// grid to unfold onto
        List<Integer> startLoc = asList(30, 30);// start location
        // data given to edge to faces is wrong.
        HashMap<Integer, List<Face>> bipartiteGraph = CubeOperations.Cube.dualGraphOfTheCube();// edge->faces

        // creating an edge to face and vice verse.

        HashMap<String, List<String>> faceToEdges = new HashMap<>();
        List<String> edges = new ArrayList<>();
        // via these two we will traverse the cube.
        HashMap<String, List<Integer>> directions = new HashMap<>();// directions from the current face.

        directions.put("north", asList(-1, 0));
        directions.put("south", asList(1, 0));
        directions.put("west", asList(0, -1));
        directions.put("east", asList(0, 1));

        for (Integer i : bipartiteGraph.keySet()) {
            for (Face face : bipartiteGraph.get(i)) {
                if (!faceToEdges.containsKey(face.getTAG())) {
                    faceToEdges.put(face.getTAG(), new ArrayList<>());
                }
                for (DualNode dn : face.getConnectedEdges())
                    faceToEdges.get(face.getTAG()).add(dn.getTAG());
            }
        }

        for (String face : faceToEdges.keySet()) {
            for (String edge : faceToEdges.get(face)) {
                if (!edgeToFaces.containsKey(edge)) {
                    edgeToFaces.put(edge, new ArrayList<>());
                }
                edgeToFaces.get(edge).add(face);
            }
        }

        for (String edge : edgeToFaces.keySet())
            edges.add(edge);

        HashMap<String, List<String>> edgeToFacesSolution = new HashMap<>();

        for (String edge : edgeToFaces.keySet())
            edgeToFacesSolution.put(edge, new ArrayList<>());
        AbstractCube abstractCube = new AbstractCube();

        traverseTheCube(edgeToFaces, edges, 0, edgeToFacesSolution, faceToEdges);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = "-";
            }
        }


        colorPallet.add("\u001B[0m");
        colorPallet.add("\u001B[30m");
        colorPallet.add("\u001B[31m");
        colorPallet.add("\u001B[32m");
        colorPallet.add("\u001B[34m");
        colorPallet.add("\u001B[35m");


    }

    /* Function number 1 */
    public static HashMap<String, List<String>> findValidUnfoldings(
            HashMap<String, List<String>> faceToEdges, HashMap<String, List<String>> edgeToFacesSolution) {


//                 for(String s: ans){
//                    s = s.substring(1, s.length() - 2);
//                    HashMap<String,List<String>> edgeToFacesSolution = convertToStringToHashMap(s);

        System.out.println(" NEW CASE");
        // System.out.println(edgeToFacesSolution.toString());

        String startEdge = "";

        for (String edge : edgeToFacesSolution.keySet()) {// getting an edge to start with.
            if (edgeToFacesSolution.get(edge).size() == 2) {
                startEdge = edge;
                break;
            }
        }
        if (startEdge == "")
            return new HashMap<>();


        if (validationCheck(edgeToFacesSolution, startEdge, faceToEdges)) {
            int num = 1;
            for (int i = 0; i < 60; i++) {
                for (int j = 0; j < 60; j++) {
                    if (grid[i][j].length() == 4)
                        System.out.print("f");
                    else
                        System.out.print("-");
                }
                System.out.println();
            }
            System.exit(0);
            return edgeToFacesSolution;

        }
        // }
        return new HashMap<>();
    }

    /* Function number 2 */
    public static boolean validationCheck(HashMap<String, List<String>> edgeToFacesSolution,
                                          String startEdge, HashMap<String, List<String>> faceToEdges) {
        // This function makes a starting face and call on its edges to open the
        // edgeToFacesSolution.
        System.out.println("Edge to face solution "+edgeToFacesSolution.toString());
        System.out.println("face To Edges "+ faceToEdges.toString());
        System.out.println("StartEdge"+ startEdge);
        String centralFace = edgeToFacesSolution.get(startEdge).get(0);

        HashMap<String, String> currDirections = new HashMap<>(4);

        // adding the north edge

        currDirections.put("NORTH", faceToEdges.get(centralFace).get(0));
        boolean flipSides = true;
        for (String edge : faceToEdges.get(centralFace)) {
            if(edgeToFacesSolution.get(edge).size()==0)continue;
            if (edge == currDirections.get("NORTH"))
                continue;


            if (       edge.charAt(0) != currDirections.get("NORTH").charAt(0)
                    && edge.charAt(0) != currDirections.get("NORTH").charAt(1)
                    && edge.charAt(1) != currDirections.get("NORTH").charAt(0)
                    && edge.charAt(1) != currDirections.get("NORTH").charAt(1)) {
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
        System.out.println("Reached here checkpoint 2");
        // preping the grid to open on.
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 60; j++) {
                grid[i][j] = "-";
            }
        }
        visited = new HashMap<>();
        for (String face : faceToEdges.keySet())
            visited.put(face, false);

        grid[30][30] = centralFace;
        visited.put(centralFace, true);
        // assigning direction and their effect.
        HashMap<String, List<Integer>> dir = new HashMap<>();// direction vector value
        dir.put("NORTH", Arrays.asList(0, 1));
        dir.put("SOUTH", Arrays.asList(0, -1));
        dir.put("EAST", Arrays.asList(1, 0));
        dir.put("WEST", Arrays.asList(-1, 0));

        for (String direction : currDirections.keySet()) {
             if (!edgeToFacesSolution.get(currDirections.get(direction)).contains(centralFace))
                continue;
            // finding the neighbouring face.
            String neighbouringFace = edgeToFacesSolution.get(currDirections.get(direction)).get(0) == centralFace
                    ? edgeToFacesSolution.get(currDirections.get(direction)).get(1)
                    : edgeToFacesSolution.get(currDirections.get(direction)).get(0);
            // checking if that face was marked true then there is a cycle in the output.
            if (visited.get(neighbouringFace))
                return false;
            if (grid[30 + dir.get(direction).get(0)][30 + dir.get(direction).get(1)].length() == 4)
                return false; // edge

            // System.out.println("Exploring central face edge: "+
            // currDirections.get(direction));
            System.out.println("Reached here checkpoint 3");
            if (!validationCheckHelperDFS(currDirections, edgeToFacesSolution, faceToEdges, direction,
                    currDirections.get(direction), centralFace, 30 + dir.get(direction).get(0),
                    30 + dir.get(direction).get(1))) {
                return false;
            }

        }

        // check if you have visited all the faces on these solutions. write a for loop.
        for (String face : faceToEdges.keySet()) {
            if (visited.get(face) == false) {
                System.out.println("PITCHHHHHHHHHHHHHHHHHHHHH some face not visited ");

                return false;
            } // check with an integer rather than looping so much.
        }
        // System.exit(0);
        return true;
    }

    /* Function number 3 */
    public static boolean validationCheckHelperDFS(
            HashMap<String, String> parentDefinedDirections, HashMap<String, List<String>> edgeToFacesSolution,
            HashMap<String, List<String>> faceToEdges, String parentAssignedDirection, String parentSharingEdge,
            String parentFace, int yourX, int yourY) {
        String currFace = edgeToFacesSolution.get(parentSharingEdge).get(0) == parentFace
                ? edgeToFacesSolution.get(parentSharingEdge).get(1)
                : edgeToFacesSolution.get(parentSharingEdge).get(0);

        if (visited.get(currFace) == true) {

             return false;
        }

        if (grid[yourX][yourY].length() == 4) {
            return false;
        }

        // you will not explore any edge if your are not there in that edge.

        visited.put(currFace, true);
        grid[yourX][yourY] = currFace;

        HashMap<String, List<Integer>> dir = new HashMap<>();
        dir.put("NORTH", Arrays.asList(0, 1));
        dir.put("SOUTH", Arrays.asList(0, -1));
        dir.put("EAST", Arrays.asList(1, 0));
        dir.put("WEST", Arrays.asList(-1, 0));

        // direction edge
        HashMap<String, String> myDirections = provideMyDirections(currFace, faceToEdges, parentDefinedDirections,
                parentAssignedDirection, parentSharingEdge);
        if (myDirections.size() == 0) {
            return false;
        }
        for (String s : myDirections.keySet()) {

            if (parentSharingEdge == myDirections.get(s))
                continue;// don't go towards parent's direction.

            if (!edgeToFacesSolution.get(myDirections.get(s)).contains(currFace))
                continue;
            // System.out.println("going on the edge "+ myDirections.get(s));
            if (!validationCheckHelperDFS(myDirections, edgeToFacesSolution, faceToEdges, s, myDirections.get(s),
                    currFace, yourX + dir.get(s).get(0), yourY + dir.get(s).get(1))) {
                return false;
            }
        }

        return true;
    }

    /* Function number 4 */
    public static HashMap<String, String> provideMyDirections(String currFace,
                                                              HashMap<String, List<String>> faceToEdges, HashMap<String, String> parentDefinedDirections,
                                                              String parentAssignedDirection, String parentSharingEdge) {
        HashMap<String, String> answer = new HashMap<>();

        // System.out.println(" ()()()()()() ");
//        System.out.println("Parent Assigned direction :" +parentAssignedDirection);
//        System.out.println("Parent Sharing Edge: "+ parentSharingEdge);
        int valid = 0;
        for (String s : parentDefinedDirections.keySet()) {
            valid++;
            // System.out.println(s + " of the parent is the edge: " +
            // parentDefinedDirections.get(s));
        }
        if (valid != 4)
            return new HashMap<>();
        // System.out.println("Your directions: ");

        if (parentAssignedDirection == "NORTH") {

            for (String s : faceToEdges.get(currFace)) {

                if (s == parentSharingEdge) {
                    // System.out.println("Edge Assigned to SOUTH is:"+ s);
                    answer.put("SOUTH", parentSharingEdge);
                } else if (parentSharingEdge.charAt(0) != s.charAt(0) && parentSharingEdge.charAt(0) != s.charAt(1)
                        && parentSharingEdge.charAt(1) != s.charAt(0) && parentSharingEdge.charAt(1) != s.charAt(1)) {
                    answer.put("NORTH", s);
                    // System.out.println("Edge Assigned to North is:"+ s);
                } else if (s.charAt(0) == parentDefinedDirections.get("EAST").charAt(0)
                        || s.charAt(1) == parentDefinedDirections.get("EAST").charAt(0)
                        || s.charAt(0) == parentDefinedDirections.get("EAST").charAt(1)
                        || s.charAt(1) == parentDefinedDirections.get("EAST").charAt(1)) {
                    answer.put("EAST", s);
                    // System.out.println("Edge Assigned to EAST is"+ s);
                } else if (s.charAt(0) == parentDefinedDirections.get("WEST").charAt(0)
                        || s.charAt(1) == parentDefinedDirections.get("WEST").charAt(0)
                        || s.charAt(0) == parentDefinedDirections.get("WEST").charAt(1)
                        || s.charAt(1) == parentDefinedDirections.get("WEST").charAt(1)) {
                    answer.put("WEST", s);
                    // System.out.println("Edge Assigned to WEST is"+ s);
                } else {

                    System.out.println("FAULT FOUND with the edge: " + s);
                }
            }
        }

        if (parentAssignedDirection == "SOUTH") {

            for (String s : faceToEdges.get(currFace)) {
                if (s == parentSharingEdge) {
                    // System.out.println("edge assigned to north:"+ s);
                    answer.put("NORTH", parentSharingEdge);
                } else if (parentSharingEdge.charAt(0) != s.charAt(0) && parentSharingEdge.charAt(0) != s.charAt(1)
                        && parentSharingEdge.charAt(1) != s.charAt(0) && parentSharingEdge.charAt(1) != s.charAt(1)) {
                    answer.put("SOUTH", s);
                    // System.out.println("edge assigned to south:"+ s);
                } else if (s.charAt(0) == parentDefinedDirections.get("EAST").charAt(0)
                        || s.charAt(1) == parentDefinedDirections.get("EAST").charAt(0)
                        || s.charAt(0) == parentDefinedDirections.get("EAST").charAt(1)
                        || s.charAt(1) == parentDefinedDirections.get("EAST").charAt(1)) {
                    answer.put("EAST", s);
                    // System.out.println("edge assigned to east:"+ s);
                } else if (s.charAt(0) == parentDefinedDirections.get("WEST").charAt(0)
                        || s.charAt(1) == parentDefinedDirections.get("WEST").charAt(0)
                        || s.charAt(0) == parentDefinedDirections.get("WEST").charAt(1)
                        || s.charAt(1) == parentDefinedDirections.get("WEST").charAt(1)) {
                    answer.put("WEST", s);
                    // System.out.println("edge assigned to west:"+ s);
                } else {
                    System.out.println("FAULT FOUND with the edge: " + s);

                }

            }
        }

        if (parentAssignedDirection == "WEST") {

            for (String s : faceToEdges.get(currFace)) {
                if (s == parentSharingEdge) {
                    // System.out.println("edge assigned to East:"+ s);

                    answer.put("EAST", parentSharingEdge);
                } else if (parentSharingEdge.charAt(0) != s.charAt(0) && parentSharingEdge.charAt(0) != s.charAt(1)
                        && parentSharingEdge.charAt(1) != s.charAt(0) && parentSharingEdge.charAt(1) != s.charAt(1)) {
                    // System.out.println("edge assigned to West:"+ s);
                    answer.put("WEST", s);
                } else if (s.charAt(0) == parentDefinedDirections.get("NORTH").charAt(0)
                        || s.charAt(1) == parentDefinedDirections.get("NORTH").charAt(0)
                        || s.charAt(0) == parentDefinedDirections.get("NORTH").charAt(1)
                        || s.charAt(1) == parentDefinedDirections.get("NORTH").charAt(1)) {
                    // System.out.println("edge assigned to north:"+ s);
                    answer.put("NORTH", s);
                } else if (s.charAt(0) == parentDefinedDirections.get("SOUTH").charAt(0)
                        || s.charAt(1) == parentDefinedDirections.get("SOUTH").charAt(0)
                        || s.charAt(0) == parentDefinedDirections.get("SOUTH").charAt(1)
                        || s.charAt(1) == parentDefinedDirections.get("SOUTH").charAt(1)) {
                    // System.out.println("edge assigned to south:"+ s);
                    answer.put("SOUTH", s);
                } else {
                    System.out.println("FAULT FOUND with the Edge: " + s);

                }

            }

        }

        if (parentAssignedDirection == "EAST") {

            // answer.put("WEST", parentSharingEdge);
            for (String s : faceToEdges.get(currFace)) {
                if (s == parentSharingEdge) {
                    // System.out.println("edge assigned to West:"+ s);
                    answer.put("WEST", parentSharingEdge);
                } else if (parentSharingEdge.charAt(0) != s.charAt(0) && parentSharingEdge.charAt(0) != s.charAt(1)
                        && parentSharingEdge.charAt(1) != s.charAt(0) && parentSharingEdge.charAt(1) != s.charAt(1)) {
                    // System.out.println("edge assigned to EAST:"+ s);

                    answer.put("EAST", s);
                } else if (s.charAt(0) == parentDefinedDirections.get("NORTH").charAt(0)
                        || s.charAt(1) == parentDefinedDirections.get("NORTH").charAt(0)
                        || s.charAt(0) == parentDefinedDirections.get("NORTH").charAt(1)
                        || s.charAt(1) == parentDefinedDirections.get("NORTH").charAt(1)) {
                    // System.out.println("edge assigned to North:"+ s);

                    answer.put("NORTH", s);
                } else if (s.charAt(0) == parentDefinedDirections.get("SOUTH").charAt(0)
                        || s.charAt(1) == parentDefinedDirections.get("SOUTH").charAt(0)
                        || s.charAt(0) == parentDefinedDirections.get("SOUTH").charAt(1)
                        || s.charAt(1) == parentDefinedDirections.get("SOUTH").charAt(1)) {
                    // System.out.println("edge assigned to South:"+ s);

                    answer.put("SOUTH", s);
                } else {
                    System.out.println("FAULT FOUND With the edge: " + s);

                }

            }

        }

        return answer;
    }



    /* Function number 5 */
    public static HashMap<String, List<String>> convertToStringToHashMap(String text) {
        HashMap<String, List<String>> data = new HashMap<>();
//        Pattern p = Pattern.compile("a-zA-Z", 2-4);//[\{\}\=\, ]++
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m1 = p.matcher(text);
        String[] split = p.split(text);
        String edge = "";
        while (m1.find()) {
            if (m1.group().length() == 2) {
                edge = m1.group();
                data.put(m1.group(), new ArrayList<>());
            } else {
                data.get(edge).add(m1.group());
            }
        }
        return data;
    }

    public static long totalNumber = 1;
    public static boolean notFound = true;

    /* Function number 6 */
    public static void traverseTheCube(HashMap<String, List<String>> edgeToFaces,
                                       List<String> edges, int edgeNo, HashMap<String, List<String>> edgeToFacesSolution,
                                       HashMap<String, List<String>> faceToEdges) {
//
//        if(ans.size()==10000000){
//            return;
//        }

        if (edgeNo == 32) {
//            for(String s: faceToEdges.keySet()){
//                System.out.println(s);
//            }

            HashMap<String, List<String>> sol = findValidUnfoldings(faceToEdges, aValidSolution(edgeToFacesSolution));// edgeToFacesSolution

            if (!notFound)
                if (sol.size() == 0)
                    System.out.println(totalNumber++ + " FOUND A SOLUTION ");

            if (notFound)
                if (sol.size() == 0)
                    System.out.println(totalNumber++ + " NOT A SOLUTION ");
                else {
                    notFound = false;
                    int count = 1;
//            for(String s: sol.keySet()) {
//              //  System.out.println(count++ +"Edge " + s + " connects the faces: ");
//            for(String str: sol.get(s)) System.out.println(str);
//            }
                    // System.out.println(validUnfoldings.size());
                }
            return;
//            ans.add(edgeToFacesSolution.toString());
//            return;
        }
        // System.out.println("The edge being processed "+edges.get(edgeNo)+ " and the
        // edgeNo. is"+edgeNo);
        for (int i = 0; i <= edgeToFaces.get(edges.get(edgeNo)).size(); i++) {

            if (i == edgeToFaces.get(edges.get(edgeNo)).size()) {
                traverseTheCube(edgeToFaces, edges, edgeNo + 1, edgeToFacesSolution, faceToEdges);
                break;
            }

            for (int j = i + 1; j < edgeToFaces.get(edges.get(edgeNo)).size(); j++) {

                edgeToFacesSolution.get(edges.get(edgeNo)).add(edgeToFaces.get(edges.get(edgeNo)).get(i));
                edgeToFacesSolution.get(edges.get(edgeNo)).add(edgeToFaces.get(edges.get(edgeNo)).get(j));
                // System.out.println("connecting Faces:
                // "+edgeToFacesSolution.get(edges.get(edgeNo)).get(0)+" and "+
                // edgeToFacesSolution.get(edges.get(edgeNo)).get(1));
                traverseTheCube(edgeToFaces, edges, edgeNo + 1, edgeToFacesSolution, faceToEdges);
                // System.out.println("came back from the recurive call of "+edgeNo+ 1);
                edgeToFacesSolution.get(edges.get(edgeNo)).removeAll(edgeToFacesSolution.get(edges.get(edgeNo)));

            }
        }

    }

    public static HashMap<String, List<String>> aValidSolution(HashMap<String, List<String>> edgeToFaceSolution) {
        HashMap<String, List<String>> ans = new HashMap<>();
        ans.put("bc", Arrays.asList("adbc", "BCbc"));
        ans.put("fg", Arrays.asList("gfhe", "fgFG"));
        ans.put("bf", Arrays.asList("bfae", "bfBF"));
        ans.put("BF", Arrays.asList("bfBF", "BFCG"));
        ans.put("dh", Arrays.asList("cdgh", "dhDH"));
        ans.put("GH", Arrays.asList("FEGH", "CDGH"));
        ans.put("EF", Arrays.asList("AEBF", "FEGH"));
        ans.put("ef", Arrays.asList("bfae", "gfhe"));
        ans.put("CD", Arrays.asList("CDGH", "CBDA"));
        ans.put("AB", Arrays.asList("CBDA", "AaBb"));
        ans.put("cd", Arrays.asList("cdgh", "adbc"));
        ans.put("gh", Arrays.asList("gfhe", "cdgh"));
        ans.put("ae", Arrays.asList("bfae", "AaEe"));
        ans.put("cg", Arrays.asList("cdgh", "cgCG"));
        ans.put("ab", Arrays.asList("AaBb", "bfae"));
        ans.put("AE", Arrays.asList("AaEe", "AEDH"));
        ans.put("he", Arrays.asList("gfhe", "HhEe"));
        ans.put("da", Arrays.asList("adbc", "daDA"));
        for (String s : edgeToFaceSolution.keySet()) {
            if (!ans.containsKey(s)) {
                ans.put(s, new ArrayList<String>());
            }
        }

        return ans;
    }


}





