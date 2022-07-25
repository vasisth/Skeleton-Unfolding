package CubeObjects;

import java.util.ArrayList;
import java.util.List;

public class Edge {
    private String TAG;
    private List<Face> connectedFaces;

    private Vertex start;
    private Vertex end;
    private Edge twin;


    public Edge(String tag, List<Face> connectedFaces){
        this.TAG = tag;
        this.connectedFaces = connectedFaces;
    }
    public Edge(String TAG){
        this.TAG = TAG;
    }
    public Edge(String TAG, Vertex start, Vertex end){
        this.TAG = TAG;
        this.start = start;
        this.end = end;
    }


    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public List<Face> getConnectedFaces() {
        return connectedFaces;
    }

    public void setConnectedFaces(ArrayList<Face> connectedFaces) {
        this.connectedFaces = connectedFaces;
    }

    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    public Vertex getEnd() {
        return end;
    }

    public void setEnd(Vertex end) {
        this.end = end;
    }

    public Edge getTwin() {
        return twin;
    }

    public void setTwin(Edge twin) {
        this.twin = twin;
    }
}
