package CubeObjects;

import java.util.ArrayList;
import java.util.List;

public class Cube {
    private String TAG;
    private List<Face>  facesOfTheCube;
    private List<Edge>  edgesOfTheCube;
    private List<Vertex> verticesOfTheCube;

    public Cube(String TAG){
        this.TAG= TAG;
        this.facesOfTheCube = new ArrayList<Face>(6);
        this.edgesOfTheCube = new ArrayList<Edge>(12);
        this.verticesOfTheCube = new ArrayList<Vertex>(8);
    }
    public Cube(String TAG, List<Face> facesOfTheCube, List<Edge> edgesOfTheCube, List<Vertex> verticesOfTheCube){
        this.TAG= TAG;
        this.facesOfTheCube = facesOfTheCube;
        this.edgesOfTheCube = edgesOfTheCube;
        this.verticesOfTheCube = verticesOfTheCube;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public List<Face> getFacesOfTheCube() {
        return facesOfTheCube;
    }

    public void setFacesOfTheCube(List<Face> facesOfTheCube) {
        this.facesOfTheCube = facesOfTheCube;
    }

    public List<Edge> getEndgesOfTheCube() {
        return edgesOfTheCube;
    }

    public void setEndgesOfTheCube(List<Edge> endgesOfTheCube) {
        this.edgesOfTheCube = endgesOfTheCube;
    }

    public List<Vertex> getVerticesOfTheCube() {
        return verticesOfTheCube;
    }

    public void setVerticesOfTheCube(List<Vertex> verticesOfTheCube) {
        this.verticesOfTheCube = verticesOfTheCube;
    }

    @Override
    public String toString() {
        return "cube{" +
                "TAG='" + TAG + '\'' +
                ", facesOfTheCube=" + facesOfTheCube +
                ", edgesOfTheCube=" + edgesOfTheCube +
                ", verticesOfTheCube=" + verticesOfTheCube +
                '}';
    }

}
