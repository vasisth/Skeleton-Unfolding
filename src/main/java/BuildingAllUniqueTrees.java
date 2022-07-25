import CubeObjects.Face;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BuildingAllUniqueTrees {

    public static void main(String[] args) {
        /*
         * The idea is to build structurally unique trees incrementally so that we will have a set of 24 faced edge unfoldings.
         * If you can check each shape in the unique set and tell is that unfolding possible, in some said time then we have a very fast
         * algorithm.
         *  time required to do this is T(n) = T(n-1)*(n-1). these many variations we will try.
         *
         * */
        BuildingAllUniqueTrees program = new BuildingAllUniqueTrees();
        program.run();


    }
    public void run(){

       Set<String> ans= buildAllUnfoldingsTrees(24);
       //Set<String> validans = verify(ans);


    }


    public Set<String> buildAllUnfoldingsTrees(int size){
        if(size==1){
            Set<String> ans = new HashSet<>();
            ans.add("{}");
            return ans;
        }
        Set<String> ans = buildAllUnfoldingsTrees(size-1);
        /*
        * how can we tell if an encoding is possible or not.
        *
        * */

        return ans;
    }
}
