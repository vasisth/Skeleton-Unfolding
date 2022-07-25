package CubeOperations;

import CubeObjects.DualNode;
import CubeObjects.Edge;
import CubeObjects.Face;
import CubeObjects.Vertex;

import java.util.*;


public class Cube {

    public static List<List<Edge>> createHypercube(){
        //start form top and proceed down.
        final int UPPER_CAPS_NAMING= 65;
        final int LOWER_CAPS_NAMING= 97;
        List<Vertex> hyperCubeVertices = new ArrayList<Vertex>(16);

        // create a square in the 4th dimension.
        //we will go from top to bottom and then outside to inside.
        // create any two opposite faces and connect them. call this outer cube and similarly create an inner cube and
        // connect their verties one to one.
        //once you create this cube inside cube structure then we will create a dual graph of.

        //create 8 vertices for each of the cubes.
        // we will go from front to back in clockwise order.

        // outer cube vertices
        List<Vertex> outerCubeVertices = nameTheCube(UPPER_CAPS_NAMING);
        //Inner cube vertices.
        List<Vertex> innerCubeVertices = nameTheCube(LOWER_CAPS_NAMING);

        //CHECKPOINT: checked on 6/3/2021 at 9.06pm.


        List<Edge> outerCubeEdges = makeCube(outerCubeVertices);//creates the edges for the vertices
        List<Edge> innerCubeEdges = makeCube(innerCubeVertices);// creates the edges for the vertices
        List<Edge> intermediateEdges = new ArrayList<>(8);

        for(int i=0; i<8; i++){
            intermediateEdges.add(new Edge(outerCubeVertices.get(i).getTAG()+innerCubeVertices.get(i).getTAG(), outerCubeVertices.get(i), innerCubeVertices.get(i)));
        }//CHECKPOINT: Checked till here on 6/4/2021 7:59am


        List<List<Edge>> tesseract = new ArrayList<>();
        tesseract.add(outerCubeEdges);
        tesseract.add(intermediateEdges);
        tesseract.add(innerCubeEdges);

        return tesseract;
    }

    private  static List<Vertex> nameTheCube(int value){
        List<Vertex> cubeVertices = new ArrayList<>(8);
        for(int i=0; i<8; i++){
            char tag =(char) (value+i);
            //System.out.println(tag);

            String TAG = String.valueOf(tag);
            cubeVertices.add(i, new Vertex(TAG));

        }
        return cubeVertices;
    }


    private   static List<Edge>  makeCube(List<Vertex> vertices){
        List<Edge> cubeEdges = new ArrayList<Edge>();
        for(int i=0; i<8; i++){
            if((i+1)%4==0)
            {
                Edge e = new Edge(vertices.get(i).getTAG()+vertices.get(i-3).getTAG(), vertices.get(i), vertices.get(i-3));
                cubeEdges.add(e);
                //continue;
            }
            else{
                Edge e = new Edge(vertices.get(i).getTAG() + vertices.get(i + 1).getTAG(), vertices.get(i), vertices.get(i + 1));
                cubeEdges.add(e);
            }

        if(i<4) cubeEdges.add(new Edge(vertices.get(i).getTAG()+vertices.get(i+4).getTAG(), vertices.get(i), vertices.get(i+4)));

        }
        return cubeEdges;
    }




    public static  HashMap<Integer, List<Face>> dualGraphOfTheCube() {
        List<List<Edge>> tesseract = createHypercube();
//        for (List<Edge> t : tesseract) {
//            System.out.println(t.toString());
//        }
        HashMap<Character, List<Edge>> vertexAndItsEdges = new HashMap<Character, List<Edge>>();

        for(int i=0; i<8; i++){
            char x = (char) (65+i);
            vertexAndItsEdges.put(x, new ArrayList<Edge>());
            char y = (char) (97+i);
            vertexAndItsEdges.put(y, new ArrayList<Edge>());
        }
       HashMap<String, DualNode> edges =new HashMap<>(32);
        for(List<Edge> list: tesseract){
            for(Edge e: list){
                edges.put(e.getTAG(), new DualNode(e.getTAG()));
            }
        }

        //for(String e: edges.keySet()) System.out.println(e);
        for(List<Edge> list: tesseract){
            for(Edge edge: list){

                char c = edge.getTAG().charAt(0);

                vertexAndItsEdges.get(c).add(edge);
                c = edge.getTAG().charAt(1);
                vertexAndItsEdges.get(c).add(edge);
            }
        }//CHECKPOINT: checked till here on 6/4/2021 at 9:46am.
        /*
        * what do i have till here?
        * 1. vertex and the edges coming or going out of it.
        * 2. tesseract: innerCube, outerCube, intermediate edges.
        * what do i need?
        * i need a graph of the edges and its neighbouring edges.
        * how would you like to define a graph?
        * you have two choices one as nodes(Like BST) and as an adjacency list.
        * we will create an adjacency list of each edge and its neighbouring edges.
        *to do this take an edge and add all that meet it at some vertex.
        *  */

        HashMap<String,List<Edge>> edgesAndItsNeighbors = new HashMap<>(32);

        for(String s: edges.keySet()){
            edgesAndItsNeighbors.put(s, new ArrayList<Edge>());

            char c = s.charAt(0);
            List<Edge> list =  vertexAndItsEdges.get(c);

            for(Edge e: list){if(e.getTAG() == s)continue; edgesAndItsNeighbors.get(s).add(e);}
            list = vertexAndItsEdges.get(s.charAt(1));
            for(Edge e: list) {if(e.getTAG()==s)continue; edgesAndItsNeighbors.get(s).add(e);}
        }
        int count = 0;
//        for(String s: edgesAndItsNeighbors.keySet()){
//            System.out.println("*********"+ s);
//            for(Edge e:edgesAndItsNeighbors.get(s)) System.out.println(count++ + e.getTAG());
//            count=0;
//        }
//        for(String s: edgesAndItsNeighbors.keySet()){
//            System.out.println("This is the edge Number and name: "+ count++ +" " + s);
//            int i=1;
//            for(Edge e: edgesAndItsNeighbors.get(s)){
//                //if(e.getTAG()==s)continue;
//                System.out.println(i++ + " "+e.getTAG());
//            }
//
//        }

        //creating a graph out of those nodes.
        //each in its key value contains the node.
        HashMap<String, DualNode> dualNodes = new HashMap<>(32);

        for(String s: edgesAndItsNeighbors.keySet()){
            dualNodes.put(s, new DualNode(s));
        }

        List<DualNode> graph = new ArrayList<DualNode>();
        for(String s: edgesAndItsNeighbors.keySet()){
            for(Edge e: edgesAndItsNeighbors.get(s)){
                dualNodes.get(s).getConnectedDualNodes().add(dualNodes.get(e.getTAG()));

            }
            graph.add(dualNodes.get(s));
        }

    /*
    *  create a bipartite graph between edges and faces.
    *1.  to create a bipartite graph create a list of faces and create a list of edges are coonect to it.
    *2.  each face edge should lead to 3 faces and each face should lead to all the edges that make up the face.
    *3.
    * */

        //1. creating the list of faces.
        // we will go from outside to inside.


        HashMap<DualNode, List<Face>> map= new HashMap<>();
        for (DualNode e: graph){
            Face faceOne = new Face("");
            Face faceTwo = new Face("");
            Face faceThree = new Face("");
        List<Face> faces = findFaces( e.getTAG(),edgesAndItsNeighbors.get(e.getTAG()) ,faceOne, faceTwo, faceThree, dualNodes);

        map.put(e, faces);
       // dualNodes.get(e).setConnectedFaces(faces);
        }



        HashMap<Integer, List<Face>> bipartiteGraph = new HashMap<>();
        for(DualNode node: map.keySet()){
            for(Face face: map.get(node)){
                int value = getFaceValue(face);
                if(bipartiteGraph.containsKey(value)){
                    boolean trigger = true;
                    for(Face fc: bipartiteGraph.get(value)){
                        if(checkForSameFaces(face, fc)){
                            trigger= false;
                            fc.getconnectedEdges().add(node);
                            node.getConnectedFaces().add(fc);
                        }
                    }
                    if(trigger) {
                        bipartiteGraph.get(value).add(face);
                    face.getconnectedEdges().add(node);
                    node.getConnectedFaces().add(face);
                    }


                }
                else{
                    bipartiteGraph.put(value, new ArrayList<Face>());
                    bipartiteGraph.get(value).add(face);

                    face.getconnectedEdges().add(node);
                    node.getConnectedFaces().add(face);

                }

            }
        }





        return bipartiteGraph;
    }
    public static boolean checkForSameFaces(Face face1, Face face2){
        HashSet<Character> set= new HashSet<>();
        for(int i=0; i<face1.getTAG().length(); i++){
            set.add(face1.getTAG().charAt(i));
        }
        for(int i=0; i<face1.getTAG().length(); i++){
            if(!set.contains(face2.getTAG().charAt(i))) return false;
        }

        return true;
    }

    public static int getFaceValue(Face face){
        int sum =0;
        for(int i=0; i<face.getTAG().length(); i++){
               sum += (int) (face.getTAG().charAt(i));
        }
        return sum;
    }

    public static List<Face> findFaces(String edge, List<Edge> neighbours, Face faceOne, Face faceTwo,  Face faceThree, HashMap<String, DualNode> dualNodes){
//        Face faceOne = new Face("");
//        Face faceTwo = new Face("");
//        Face faceThree = new Face("");
        List<Face> faces = new ArrayList<Face>(3);
        List<Edge> charOne= new ArrayList<Edge>(3);
        List<Edge> charTwo= new ArrayList<Edge>(3);
        for(int i=0; i<6; i++){
           if(neighbours.get(i).getTAG().charAt(0) == edge.charAt(0) || neighbours.get(i).getTAG().charAt(1) == edge.charAt(0))
               charOne.add(neighbours.get(i));
           if(neighbours.get(i).getTAG().charAt(0) == edge.charAt(1) || neighbours.get(i).getTAG().charAt(1) == edge.charAt(1))
                charTwo.add(neighbours.get(i));
        }

        Queue<Face> q = new LinkedList<Face>();
        q.add(faceOne); q.add(faceTwo); q.add(faceThree);
        for(int i=0; i<3; i++){

            for(int j=0; j<3; j++){
            String s = charOne.get(i).getTAG().charAt( edge.charAt(0)==charOne.get(i).getTAG().charAt(0)?1:0) + ""+ charTwo.get(j).getTAG().charAt( edge.charAt(1)==charTwo.get(j).getTAG().charAt(0)?1:0);
            String reverseS = charTwo.get(j).getTAG().charAt( edge.charAt(1)==charTwo.get(j).getTAG().charAt(0)?1:0)+ ""+ charOne.get(i).getTAG().charAt( edge.charAt(0)==charOne.get(i).getTAG().charAt(0)?1:0);
            if(dualNodes.containsKey(s) || dualNodes.containsKey(reverseS)){
                Face face = q.remove();
                face.setTAG(s+edge);
                faces.add(face);

                break;
            }

            }
        }


        return faces;
    }

}
