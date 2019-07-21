import javax.sound.midi.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Player {

    private double ms;
    private long subMS;
    private int currentStep;
    private ScheduledExecutorService executorService;
    private int[][] sequence;
    private MidiDevice midiDevice;
    private ShortMessage[] messages;


    public Player(MidiDevice midiDevice, double bpm, int[][] sequence) {
        ms = 60000 / bpm;
        subMS = (long) (ms / 4.0);
        executorService = Executors.newSingleThreadScheduledExecutor();
        this.midiDevice = midiDevice;
        this.sequence = sequence;
        messages = new ShortMessage[sequence.length];
        buildMessages();
    }

    private void buildMessages() {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = new ShortMessage();
        }
    }

    public void start() {
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Output notes from sequence to device at fixed rate
                try {
                    midiDevice.open();
                    Receiver receiver = midiDevice.getReceiver();
                    // Loop over sequence and output MIDI messages
                    int n = 0;
                    for (int i = 0; i < sequence[0].length; i++) {
                        for (int j = 0; j < messages.length; j++) {
                            if (j == 0) {
                                n = 60;
                            } else if (j == 1) {
                                n = 62;
                            } else if (j == 2) {
                                n = 64;
                            } else if (j == 3) {
                                n = 65;
                            }
                            // Set messages for each track
                            if (sequence[j][i] == 1) {
                                // Send a note
                                messages[j].setMessage(ShortMessage.NOTE_ON, j, n, 100);
                                receiver.send(messages[j], -1);
                            }
                        }
                        Thread.sleep(subMS);
                        for (int y = 0; y < messages.length; y++) {
                            receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, y, 0, 0), -1);
                        }
                    }
                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                } catch (InvalidMidiDataException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, subMS, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        // Stop the Player
        executorService.shutdown();
        // Send NOTE_OFF messages on all tracks
    }

    public void setTempo(int bpm) {
        ms = 60000 / bpm;
        subMS = (long) (ms / 4.0);
    }

    public void setSequence(int[][] sequence) {
        this.sequence = sequence;
    }
}
