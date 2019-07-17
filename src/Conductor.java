public class Conductor {

    private int tempo;
    private boolean running;
    private Member currMember;

    public Conductor() {
        tempo = 0;
        running = false;
        currMember = null;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        if (!running) {
            running = true;
        }
    }

    public void stop() {
        if (running) {
            running = false;
        }
    }

    public Member getCurrMember() {
        return currMember;
    }

    public void setCurrMember(Member currMember) {
        this.currMember = currMember;
    }
}
