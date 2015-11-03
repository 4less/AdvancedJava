package program;

import io.Sequence;

/**
 * Created by Joachim on 02/11/2015.
 * Could provide further functionality if the program is extended
 */
public class Controller {
    private Sequence sequence;

    public Controller() throws Exception {
        this.sequence = new Sequence("","");
    }


    /**
     * getter for sequence
     * @return
     */
    public Sequence getSequence() {
        return this.sequence;
    }

    /**
     * Filter sequence
     */
    public void filter(String input) {
        sequence.setSequence(input);
    }
}
