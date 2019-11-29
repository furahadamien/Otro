import java.util.*;

/**@author Furaha Damien
 * most of the code in this class was adopted from user najm-sheikh's
 * question on codereview.stackexchange on Needlemann-Wusnch optimization
 */
public class PairWiseAligner {
    protected final String seq_1, seq_2; 
    public int alignmentScore;              
    protected Node[][] matrix;              

    protected final int matchScore, mismatchScore, indel;
    String top = "";    
    String buffer = "";   
    String bottom = "";    

    public PairWiseAligner(String s1, String s2, ScoreScheme s) {
        seq_1 = "\u25A0" + s1;
        seq_2 = "\u25A0" + s2;
        matchScore = s.match;
        mismatchScore = s.mismatch;
        indel = s.indel;
        matrix = new Node[seq_1.length()][seq_2.length()];
        for (int i = 0; i < seq_1.length(); i++)
            matrix[i][0] = new Node(i * indel, i, 0);

        for (int i = 0; i < seq_2.length(); i++)
            matrix[0][i] = new Node(i * indel, 0, i);
    }

    protected int score(int i, int j) {
        if (seq_1.charAt(i) == seq_2.charAt(j))
            return matchScore;
        else
            return mismatchScore;
    }
    protected Node match(int i, int j) {
        Node s1,s2,s3;
        s1 = new Node(matrix[i-1][j-1].score + score(i, j), i, j);
        s2 = new Node(matrix[i-1][j].score + indel, i, j);
        s3 = new Node(matrix[i][j-1].score + indel, i, j);
        Node largest = new Node(Math.max(s1.score, Math.max(s2.score, s3.score)), i, j);
        if (s1.compareTo(largest) == 0)
            largest.prev = matrix[i-1][j-1];
        else if(s2.compareTo(largest) == 0)
            largest.prev = matrix[i-1][j];
        else
            largest.prev = matrix[i][j-1];

        return largest;
    }

    public Node runAnalysis() {
        for (int i = 1; i < seq_1.length(); i++) {
            for (int j = 1; j < seq_2.length(); j++){
                matrix[i][j] = match(i, j);
            }
        }
        alignmentScore = matrix[seq_1.length()-1][seq_2.length()-1].score;
        return matrix[seq_1.length()-1][seq_2.length()-1];
    }

    protected Node traceHelper(Node curr) {
        while (curr.prev != null) {
            if (curr.i - curr.prev.i == 1 && curr.j - curr.prev.j == 1){   
                boolean x = seq_1.charAt(curr.i) == seq_2.charAt(curr.j) ? true : false;
                if(x){
                    top = seq_1.charAt(curr.i) + " " + top;
                    buffer = "|" + " " + buffer;
                    bottom = seq_2.charAt(curr.j) + " " + bottom;
                }else{
                    top = seq_1.charAt(curr.i) + " " + top;
                    buffer = " " + " " + buffer;
                    bottom = seq_2.charAt(curr.j) + " " + bottom;
                }
            }else if (curr.i - curr.prev.i == 1){  
                top = seq_1.charAt(curr.i) + " " + top;
                buffer = " " + " " + buffer;
                bottom = "-" + " " + bottom;                           
            }else if (curr.j - curr.prev.j == 1){
                top = "-" + " " + top;
                buffer = " " + " " + buffer;
                bottom = seq_2.charAt(curr.j) + " " + bottom;
            }

            curr = curr.prev;
        }

        return curr;
    }

    public void traceback() {
        Node curr = matrix[seq_1.length()-1][seq_2.length()-1];

        curr = traceHelper(curr);
        while (curr.i != 0 || curr.j != 0) {
            if (curr.i != 0 && curr.j == 0){
                curr.prev = matrix[curr.i-1][curr.j];
                curr = traceHelper(curr);
            }else if (curr.i == 0 && curr.j != 0) {
                curr.prev = matrix[curr.i][curr.j-1];
                curr = traceHelper(curr);
            }
        }

        System.out.println(top);
        System.out.println(buffer);
        System.out.println(bottom);
    }


    public static void main(String[] args) {

        String seq_1 = "CATTAATTACACTCTCGCACTCACCACCAAACATCCTAAACCCAGACAGGCCTCGACTCC";
        String seq_2 = "ACTAAACAAGACTCGCCTGTCTAACTAGGGAGTTTATAATGAACCGTGGCGTAGACCA";

        ScoreScheme s = new ScoreScheme(2, -1, -2);
        PairWiseAligner dna = new PairWiseAligner(seq_1, seq_2, s);     

       dna.runAnalysis();

        dna.traceback();
        System.out.println("The alignment score is " + dna.alignmentScore);

    }
}

class Node implements Comparable<Node>{
    int i, j;
    int score;
    Node prev;

    public Node(int score, int x, int y) {
        this.i = x;
        this.j = y;
        this.score = score;
        this.prev = null;
    }

    public int compareTo(Node n) {
        return this.score - n.score;
    }

    public String toString() {
        return ""+score;
    }
}