package CubeObjects;

import java.util.ArrayList;
import java.util.List;

public class Face {
    private String TAG;

    public List<DualNode> getConnectedEdges() {
        return connectedEdges;
    }

//    public void setConnectedEdges(List<DualNode> connectedEdges) {
//        this.connectedEdges = connectedEdges;
//    }
//
//    public List<Face> getConnectedFaces() {
//        return connectedFaces;
//    }
//
//    public void setConnectedFaces(List<Face> connectedFaces) {
//        this.connectedFaces = connectedFaces;
//    }

    private List<DualNode> connectedEdges;
   // private List<Face> connectedFaces;
     public Face(String tag){
        this.TAG = tag;
        connectedEdges = new ArrayList<DualNode>(4);
        // connectedFaces = new ArrayList<>();

     }
    public Face(String tag, List<DualNode> edges){
        this.TAG = tag;
        connectedEdges = edges;
        //connectedFaces = new ArrayList<>();
    }
    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public List<DualNode> getconnectedEdges() {
        return connectedEdges;
    }

}
