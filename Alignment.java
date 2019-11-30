import java.util.*;
import java.io.*;
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

        for(int i = 0; i < this.sequencesList.size(); i++){
            List<ScoreSource> currentSequenceScore = new ArrayList<ScoreSource>();
            for(int j = 0; j < this.sequencesList.size(); j++){
                if(i == j) continue;
                PairWiseAligner alignment = new PairWiseAligner(sequencesList.get(i), sequencesList.get(j), scheme);
                alignment.runAnalysis();
                alignment.traceback();
                int score = alignment.alignmentScore;
                ScoreSource currObj = new ScoreSource(score, j);
                currentSequenceScore.add(currObj);
            }
            Collections.sort(currentSequenceScore, getCompByScore());
            this.sortedScores.add(currentSequenceScore );
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

    public static void main(String [] args){

        Alignment align = new Alignment("sequences.fa");
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

