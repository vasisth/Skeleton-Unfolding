package CubeObjects;

import java.util.ArrayList;
import java.util.List;


public class DualNode extends Vertex{
    List<Face> connectedFaces;


    public DualNode(String TAG) {
        super(TAG);
        this.connectedFaces = new ArrayList<Face>();
        setConnectedDualNodes(new ArrayList<DualNode>());
    }

    public List<Face> getConnectedFaces() {
        return connectedFaces;
    }


}
