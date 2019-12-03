import java.util.*;
import java.io.*;
import java.lang.Object;
/**
 * @author Furaha Damien
 */
public class Alignment {

    public List<String> sequencesList;
    public int[][] scores;
    public List<List<ScoreSource>> sortedScores;
    public String sequences;

    public Alignment(String sequences){
        this.sequences = sequences;
        this.sequencesList = new ArrayList<String>();
        this.sortedScores = new ArrayList<List<ScoreSource>>();
    }

    /**
     * parse the independed reads for the FASTA files containing all the reads
     * 
     * @param sequenceReads path to the file containg the FASTA file
     * @return void
     * @throws RuntimeException
     */
    public void parseSequences() throws RuntimeException {
        try{
            Scanner file = new Scanner(new File(this.sequences));
            StringBuilder sb = new StringBuilder();

            while(file.hasNextLine()){
                String currentLine = file.nextLine();
                if(currentLine.contains(">")){
                    this.sequencesList.add(sb.toString());
                     sb = new StringBuilder();
                }
                else{
                    sb.append(currentLine);
                }
            }
            this.sequencesList.add(sb.toString());
            this.sequencesList.remove(0);
            

        }
        catch(FileNotFoundException e){
            System.out.println("no file found");
        }
    }
    /**
     * alignment of each of the pairs using Needlemann-Wusnch
     * obtains and sorts the highest scoring sequences for each sequence
     */
    public void pairwiseAligner(){
        ScoreScheme scheme = new ScoreScheme(2, -1, -2);
        System.out.println(this.sequencesList.get(0).length() + "\n");
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < this.sequencesList.size(); i++){
            List<ScoreSource> currentSequenceScore = new ArrayList<ScoreSource>();
            for(int j = 0; j < this.sequencesList.size(); j++){
                if(i == j) continue;
                PairWiseAligner alignment = new PairWiseAligner(sequencesList.get(i), sequencesList.get(j), scheme);
                alignment.runAnalysis();
                alignment.traceback();
                sb.append(alignment.top + "\n");
                sb.append(alignment.buffer+ "\n");
                sb.append(alignment.bottom + "\n");
                int score = alignment.alignmentScore;
                ScoreSource currObj = new ScoreSource(score, j);
                currentSequenceScore.add(currObj);
            }
            Collections.sort(currentSequenceScore, getCompByScore());
            this.sortedScores.add(currentSequenceScore );
        }
        try(PrintWriter writer = new PrintWriter(new File("alignment.txt"))) {
            writer.write(sb.toString());

        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
        }

    }
    /**
     * compares scores of each of the pairwise alignment
     * @return 1 or -1 or 0
     */
    public static Comparator<ScoreSource> getCompByScore(){   
        Comparator comp = new Comparator<ScoreSource>(){
            @Override
            public int compare(ScoreSource s1, ScoreSource s2)  {
            return s2.compareTo(s1);
            }        
        };
        return comp;
    }  

    public void buildAlignment(){
        List<TreeNode> leaves = new ArrayList<TreeNode>();

        for(String s : this.sequencesList){
            leaves.add(new TreeNode(new ArrayList<TreeNode>(), s));
        }

        //TODO : CREATE THE UPGMA CLASS FOR GENERATING PHLYOGENETIC TREE

       // TreeNode upgmaTree = new Upgma(leaves);



    }

    public static void main(String [] args){

        String path = "../../../Downloads/bb3_release/RV11/BBS11031.tfa";
        Alignment align = new Alignment(path);
        align.parseSequences();
        align.pairwiseAligner();

        for(List<ScoreSource> list : align.sortedScores){
            for(ScoreSource s : list){
                System.out.print(s.scoreSource+1 + " ");
            }
            System.out.println();
        }
    }
    
}

