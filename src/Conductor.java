import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

public class Conductor {

    private int tempo; // tempo in bpm
    private boolean running;
    private int[][] currMember;
    private MidiDevice midiDevice;
    private Player player;

    private int[][] testMember = {
            {1, 0, 0, 1, 0, 0, 1, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 1, 0, 0}
    };

    public Conductor() {
        tempo = 120; // default tempo setting
        running = false;
        currMember = null;
        openMIDIDevice(); // get MIDI device ready for Player
        player = new Player(midiDevice, testMember);
    }

    private void openMIDIDevice() {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        try {
            midiDevice = MidiSystem.getMidiDevice(infos[3]);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
        if (running) {
            player.changeReadInterval(tempo);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        if (!running) {
            running = true;
            player.start(tempo);
        }
    }

    public void stop() {
        if (running) {
            running = false;
            player.stop();
        }
    }

    public int[][] getCurrMember() {
        return currMember;
    }

    public void setCurrMember(Member currMember) {
        this.currMember = currMember.getDna().getChromosome();
        player.setSequence(this.currMember);
    }
}
