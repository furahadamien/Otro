import java.util.*;
import java.io.*;

public class Alignmnet {

    public List<String> sequencesList;
    public int[][] scores;
    public List<List<Integer>> sortedScores;
    public String sequences;

    public Alignment(String sequences){
        this.sequences = sequences;
        this.sequencesList = new ArrayList<String>();
        this.sortedScores = new ArrayList<List<Integer>>();
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
            this.sequencesListremove(0);

        }
        catch(FileNotFoundException e){
            System.out.println("no file found");
        }
    }
    

}