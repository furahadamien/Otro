/**
 * 
 */
public class ScoreSource{
    int pairwiseScore;
    int scoreSource;

    public ScoreSource(int pairwiseScore, int scoreSource){
        this.pairwiseScore = pairwiseScore;
        this. scoreSource = scoreSource;
    }

    public int compareTo(ScoreSource s){
        return this.pairwiseScore - s.pairwiseScore;
    }

}