package CubeObjects;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private String TAG;
    private static String convention; //this string with tell the convention
    private List<DualNode> connectedDualNodes;
    private List<Double> coordinates;

    public List<DualNode> getConnectedDualNodes() {
        return connectedDualNodes;
    }

    public void setConnectedDualNodes(List<DualNode> connectedDualNodes) {
        this.connectedDualNodes = connectedDualNodes;
    }


    public Vertex(String TAG){
        this.TAG = TAG;
        this.coordinates = new ArrayList<Double>(4);
        convention = "xyzk";
    }


    public String getTAG() {
        return TAG;
    }

//    public void setTAG(String TAG) {
//        this.TAG = TAG;
//    }
//
//    public static String getConvention() {
//        return convention;
//    }
//
//    public static void setConvention(String convention) {
//        Vertex.convention = convention;
//    }
//
//    public List<Edge> getConnectedEdges() {
//        return connectedEdges;
//    }
//
//    public void setConnectedEdges(List<Edge> connectedEdges) {
//
//        this.connectedEdges = connectedEdges;
//    }
//    public void addConnectedEdge(Edge e){
//        connectedEdges.add(e);
//    }
//    public void removeConnectedEdge(Edge e){
//        connectedEdges.remove(e);
//    }
//
//    public List<Double> getCoordinates() {
//        return coordinates;
//    }
//
//    public void setCoordinates(List<Double> coordinates) {
//        this.coordinates = coordinates;
//    }
}
