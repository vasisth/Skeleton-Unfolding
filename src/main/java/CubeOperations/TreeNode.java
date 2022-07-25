package CubeOperations;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String TAG;
    private List<TreeNode> neighbours;
    private TreeNode parent;
    private TreeNode lastAdded;

 public TreeNode(String TAG){
     this.TAG = TAG;
     neighbours = new ArrayList<>(4);
     parent = null;

 }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public List<TreeNode> getNeighbours() {
        return neighbours;
    }
    public void addNeighbour(TreeNode neighbour, String direction){
     int index = getIndex(direction);
     this.neighbours.add(neighbour);
    }
    public void removeNeighbour(TreeNode neighbour){
     try{
         if(this.neighbours.contains(neighbour)){
            int i=0;
             for(TreeNode curr: this.neighbours){
                if(curr==neighbour)break;
                i++;
            }
             this.neighbours.set(i, null);
         }
         else{
             System.out.println("No neighbour exists called "+ neighbour.getTAG());
         }
     }catch(Exception e){
         System.out.println("THe neighbours list  is null. Cannot remove object: "+ neighbour.getTAG());
         }
     }

    public void setNeighbours(List<TreeNode> neighbours) {
        this.neighbours = neighbours;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
    private int getIndex(String direction){
     if(direction=="NORTH"){
         return 0;
     }else if(direction== "WEST"){
         return 1;
     }else if(direction=="EAST") return 2;
     else return 3;
    }
}
