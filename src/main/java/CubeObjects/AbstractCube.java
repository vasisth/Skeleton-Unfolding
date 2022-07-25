package CubeObjects;

import com.apple.eawt.Application;

import java.util.*;

public class AbstractCube {
   /*
   * HashMap<String, String> selfDirections = getDirections(self);// note you have been put at your place by your parent and he also  your directions. you are just getting it.
        String parentSharingEdge= getParentSharingEdge(self);//store key value as key being the child and the value the edge that the child and parent share.
         List<Integer> myCoordinates = coordinatesOfTheFaces.get(self);//[x,y] coordinates
         HashMap<String, List<Integer>> dir = getUnitVectors();//this returns a hash table of NORTH -> (0,1).

   *
   * */
    private static HashMap<String, String> edgeToParent;// you will give the edge and it will return the parent or the owner of that edge.
    private static HashMap<String, String> parentSharingEdge;// this tells, given a child face what is its parent edge. having one parent ensures no cycles.
    private static HashMap<String, List<Integer>> coordinatesOfTheFaces;
    private static  HashMap<String, List<Integer>> unitVectors;
    private static HashMap<String, HashMap<String, String>> faceAndItsDirections;// this will store the parents edges and their directions.

        public AbstractCube(){
            edgeToParent = new HashMap<>();
            parentSharingEdge= new HashMap<>();
            coordinatesOfTheFaces= new HashMap<>();
            faceAndItsDirections = new HashMap<>();
            unitVectors = new HashMap<>();

            unitVectors.put("NORTH", Arrays.asList(-1,0));//top corner is 0, 0 going down is south.
            unitVectors.put("SOUTH", Arrays.asList(1,0));
            unitVectors.put("EAST", Arrays.asList(0,1));
            unitVectors.put("WEST", Arrays.asList(0,-1));
        }
        public static HashMap<String, String> getParentSharingEdge(){
            return  parentSharingEdge;
        }

        public static  HashMap<String, HashMap<String, String>> getFaceAndItsDirections(){return faceAndItsDirections;}

        public static HashMap<String, String> getEdgeToParent(){return edgeToParent;}

    public static HashMap<String, String> getSelfDirections(String face, HashMap<String, List<String>> faceToEdges) {
        //System.out.println(face+" passed to getSelfDirections this should be checked with the other face");
            String parentConnectedEdge= parentSharingEdge.get(face);
        //System.out.println("Parent connected edge form the abstract cube class: "+ parentConnectedEdge);
      //  System.out.println("PARENT CONNECTED EDGE TRYING TO PEEK: "+ parentConnectedEdge);
         String parent  = edgeToParent.get(parentConnectedEdge);

        //  System.out.println("*****edge to parent removed from  "+ parentConnectedEdge+" the face "+ parent+ " and the left elements "+edgeToParent.get(parentConnectedEdge));
        //System.out.println("parent gotten in abstract cube"+ parent);
         String parentAssignedDirection ="";


       try{
         for(String direction: faceAndItsDirections.get(parent).keySet()){
             if(faceAndItsDirections.get(parent).get(direction).equals(parentConnectedEdge)) {parentAssignedDirection= direction; break;}
         }}
       catch(NullPointerException e){

          //System.out.println("Kon hai bey yeh face "+ face+" parentConnectedEdge = "+parentConnectedEdge+" parent= "+parent+" faceAndItsDirections.get(parent)= "+faceAndItsDirections.get(parent)+" "+faceAndItsDirections.toString());

          System.exit(10);
        }
         if(parentAssignedDirection.equals("")){
           //  System.out.println("COULD NOT FIND THE DIRECTION OF THE EDGE "+parentConnectedEdge +" IN THE PARENT: ");
             System.exit(0);
         }
        //if(face.equals("fgFG"))System.out.println("the face asking for directions is:    "+ face+"     the parent is       "+parent+"      "+ parentConnectedEdge+"   parent assigned direction:     "+ parentAssignedDirection + "  "+coordinatesOfTheFaces.get(face).toString());
        HashMap<String, String> faceAndItsDir =  provideMyDirections(face,  faceToEdges,  faceAndItsDirections.get(parent),parentAssignedDirection, parentConnectedEdge);
        addToFaceAndItsDirections(face, faceAndItsDir);
//        for(String f: faceAndItsDir.keySet()){
//
//           // System.out.println("INTERNAL SUBMISSION CALL: this is the face that is submitted "+face+ "    "+faceAndItsDir.toString());
//            break;
//        }
         return faceAndItsDir;

    }


    public static void addToFaceAndItsDirections(String face, HashMap<String, String> directions){

            faceAndItsDirections.put(face, directions);

    }
    public static void removeFaceAndItsDirections(String face){
        faceAndItsDirections.remove(face);
    }
    public static void addEdgeToParent(String edge, String self){

           edgeToParent.put(edge, self);
           //edgeToParent.get(edge).add(self);

       // System.out.println("**edge to parent added to "+ edge+" the face "+ self+ " and the elements present after adding is "+edgeToParent.get(edge));

    }

    public static void removeEdgeToParent(String edge){
       // System.out.println("the edge that is requesting the removal of a face: "+ edge);
             edgeToParent.remove(edge);
            return;
    }

    public static String getParentSharingEdge(String face) {
        return parentSharingEdge.get(face);
    }
    public static void addFaceToParentSharingEdge(String childFace, String parentEdge){
           parentSharingEdge.put(childFace, parentEdge);
    }

    public static void removeFaceToParentSharingEdge(String face){
        parentSharingEdge.remove(face);

            return;
    }

    public static List<Integer> getCoordinatesOfTheFaces(String face) {
        return coordinatesOfTheFaces.get(face);
    }
    public static void addToCoordinatesOfTheFaces(String face, List<Integer> coordinates){

        coordinatesOfTheFaces.put(face, coordinates);
    }
    public static void removeCoordinatesOfTheFaces(String face) {
        coordinatesOfTheFaces.remove(face);
        return;
    }

    public static HashMap<String, List<Integer>> getUnitVectors() {
        return unitVectors;
    }



    public static HashMap<String, String> provideMyDirections(String currFace, HashMap<String, List<String>> faceToEdges,HashMap<String, String> parentDefinedDirections,String parentAssignedDirection, String parentSharingEdge){
        HashMap<String, String> answer = new HashMap<>();

        //System.out.println(" ()()()()()() ");
//        System.out.println("Parent Assigned direction :" +parentAssignedDirection);
//        System.out.println("Parent Sharing Edge: "+ parentSharingEdge);
        int valid=0;
        for(String s: parentDefinedDirections.keySet()) {
            valid++;
            // System.out.println(s + " of the parent is the edge: " + parentDefinedDirections.get(s));
        }
        if(valid!=4) return new HashMap<>();
        //System.out.println("Your directions: ");

        if(parentAssignedDirection.equals("NORTH")){

            for(String s: faceToEdges.get(currFace)){

                if(s.equals(parentSharingEdge)) {
                    //System.out.println("Edge Assigned to SOUTH is:"+ s);
                    answer.put("SOUTH", parentSharingEdge);
                }

                else if(parentSharingEdge.charAt(0)!=s.charAt(0)&& parentSharingEdge.charAt(0)!=s.charAt(1)&& parentSharingEdge.charAt(1)!=s.charAt(0)&&parentSharingEdge.charAt(1)!=s.charAt(1)){
                    answer.put("NORTH", s);
                    //System.out.println("Edge Assigned to North is:"+ s);
                }
                else if(s.charAt(0)==parentDefinedDirections.get("EAST").charAt(0) ||s.charAt(1)==parentDefinedDirections.get("EAST").charAt(0) || s.charAt(0)==parentDefinedDirections.get("EAST").charAt(1) || s.charAt(1)==parentDefinedDirections.get("EAST").charAt(1)){
                    answer.put("EAST", s);
                    // System.out.println("Edge Assigned to EAST is"+ s);
                }
                else if(s.charAt(0)==parentDefinedDirections.get("WEST").charAt(0) ||s.charAt(1)==parentDefinedDirections.get("WEST").charAt(0) || s.charAt(0)==parentDefinedDirections.get("WEST").charAt(1) || s.charAt(1)==parentDefinedDirections.get("WEST").charAt(1)){
                    answer.put("WEST", s);
                    //System.out.println("Edge Assigned to WEST is"+ s);
                }
                else{

                    System.out.println("FAULT FOUND with the edge: "+s);
                }
            }
        }

        if(parentAssignedDirection.equals("SOUTH")){

            for(String s: faceToEdges.get(currFace)){
                if(s.equals(parentSharingEdge)) {
                    //System.out.println("edge assigned to north:"+ s);
                    answer.put("NORTH", parentSharingEdge);
                }
                else if(parentSharingEdge.charAt(0)!=s.charAt(0)&& parentSharingEdge.charAt(0)!=s.charAt(1)&& parentSharingEdge.charAt(1)!=s.charAt(0)&&parentSharingEdge.charAt(1)!=s.charAt(1)){
                    answer.put("SOUTH", s);
                    // System.out.println("edge assigned to south:"+ s);
                }
                else if(s.charAt(0)==parentDefinedDirections.get("EAST").charAt(0) ||s.charAt(1)==parentDefinedDirections.get("EAST").charAt(0) || s.charAt(0)==parentDefinedDirections.get("EAST").charAt(1) || s.charAt(1)==parentDefinedDirections.get("EAST").charAt(1)){
                    answer.put("EAST", s);
                    //  System.out.println("edge assigned to east:"+ s);
                }
                else if(s.charAt(0)==parentDefinedDirections.get("WEST").charAt(0) ||s.charAt(1)==parentDefinedDirections.get("WEST").charAt(0) || s.charAt(0)==parentDefinedDirections.get("WEST").charAt(1) || s.charAt(1)==parentDefinedDirections.get("WEST").charAt(1)){
                    answer.put("WEST", s);
                    // System.out.println("edge assigned to west:"+ s);
                }
                else{
                    System.out.println("FAULT FOUND with the edge: "+ s);

                }

            }
        }

        if(parentAssignedDirection.equals("WEST")){


            for(String s: faceToEdges.get(currFace)){
                if(s.equals(parentSharingEdge)) {
                    //System.out.println("edge assigned to East:"+ s);

                    answer.put("EAST", parentSharingEdge);
                }
                else if(parentSharingEdge.charAt(0)!=s.charAt(0)&& parentSharingEdge.charAt(0)!=s.charAt(1)&& parentSharingEdge.charAt(1)!=s.charAt(0)&&parentSharingEdge.charAt(1)!=s.charAt(1)){
                    //  System.out.println("edge assigned to West:"+ s);
                    answer.put("WEST", s);
                }
                else if(s.charAt(0)==parentDefinedDirections.get("NORTH").charAt(0) ||s.charAt(1)==parentDefinedDirections.get("NORTH").charAt(0) || s.charAt(0)==parentDefinedDirections.get("NORTH").charAt(1) || s.charAt(1)==parentDefinedDirections.get("NORTH").charAt(1)){
                    //   System.out.println("edge assigned to north:"+ s);
                    answer.put("NORTH", s);
                }
                else if(s.charAt(0)==parentDefinedDirections.get("SOUTH").charAt(0) ||s.charAt(1)==parentDefinedDirections.get("SOUTH").charAt(0) || s.charAt(0)==parentDefinedDirections.get("SOUTH").charAt(1) || s.charAt(1)==parentDefinedDirections.get("SOUTH").charAt(1)){
                    //   System.out.println("edge assigned to south:"+ s);
                    answer.put("SOUTH", s);
                }
                else{
                    System.out.println("FAULT FOUND with the Edge: "+s);

                }





            }

        }

        if(parentAssignedDirection.equals("EAST")) {

            //  answer.put("WEST", parentSharingEdge);
            for(String s: faceToEdges.get(currFace)){
                if(s.equals(parentSharingEdge)) {
                    // System.out.println("edge assigned to West:"+ s);
                    answer.put("WEST", parentSharingEdge);
                }
                else if(parentSharingEdge.charAt(0)!=s.charAt(0)&& parentSharingEdge.charAt(0)!=s.charAt(1)&& parentSharingEdge.charAt(1)!=s.charAt(0)&&parentSharingEdge.charAt(1)!=s.charAt(1)){
                    // System.out.println("edge assigned to EAST:"+ s);

                    answer.put("EAST", s);
                }
                else if(s.charAt(0)==parentDefinedDirections.get("NORTH").charAt(0) ||s.charAt(1)==parentDefinedDirections.get("NORTH").charAt(0) || s.charAt(0)==parentDefinedDirections.get("NORTH").charAt(1) || s.charAt(1)==parentDefinedDirections.get("NORTH").charAt(1)){
                    //    System.out.println("edge assigned to North:"+ s);

                    answer.put("NORTH", s);
                }
                else if(s.charAt(0)==parentDefinedDirections.get("SOUTH").charAt(0) ||s.charAt(1)==parentDefinedDirections.get("SOUTH").charAt(0) || s.charAt(0)==parentDefinedDirections.get("SOUTH").charAt(1) || s.charAt(1)==parentDefinedDirections.get("SOUTH").charAt(1)){
                    //    System.out.println("edge assigned to South:"+ s);

                    answer.put("SOUTH", s);
                }
                else{
                    System.out.println("FAULT FOUND With the edge: "+ s);


                }





            }

        }

        return answer;
    }

}
