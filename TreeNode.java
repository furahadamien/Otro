import java.util.*;

/**
 * @author Furaha Damien
 */
public class TreeNode{
    List<TreeNode> descedants;
    String sequence;

    public TreeNode(List<TreeNode> descendants, String sequence){
        this.descedants = descendants;
        this.sequence = sequence;
    }
}