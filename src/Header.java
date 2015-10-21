/**
 * Created by Joachim on 20/10/2015.
 */
public class Header {
    private String header;
    private int length;
    private Sequence sequence;

    public Header(String header, Sequence sequence) {
        this.sequence = sequence;
        if(!header.startsWith(">"))
            header = '>' + header;
        this.header = header;
        this.length = header.length();
    }

    public void print() {
        System.out.println(this.header);
    }

    public Sequence getSequence() {
        return sequence;
    }

    public String toString() {
        return this.header;
    }

    public boolean check(Sequence sequence) {
        return this.sequence.equals(sequence) && this.sequence.getHeader().equals(this);
    }
}
