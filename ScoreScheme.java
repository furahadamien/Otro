
/**
 * @author Furaha Damiene
 */
public class ScoreScheme {
    int match;
    int mismatch;
    int indel;
    public ScoreScheme(int match, int mismatch, int indel) {
        this.match = match;
        this.mismatch = mismatch;
        this.indel = indel;
    }
}