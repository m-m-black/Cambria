import javax.sound.midi.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Player {

    private int currentStep;
    private ScheduledExecutorService executorService;
    private ScheduledFuture<?> futureTask;
    private Runnable myTask;
    private int[][] sequence;
    private MidiDevice midiDevice;
    private ShortMessage[] messages;
    private Receiver receiver;
    private int n; // MIDI note value to be output


    public Player(MidiDevice midiDevice, int[][] sequence) {
        currentStep = 0;
        executorService = Executors.newSingleThreadScheduledExecutor();
        this.midiDevice = midiDevice;
        this.sequence = sequence;
        messages = new ShortMessage[sequence.length];
        buildMessages();
        n = 0;
    }

    private void buildMessages() {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = new ShortMessage();
        }
    }

    public void start(int tempo) {
        if (executorService.isShutdown()) {
            executorService = Executors.newSingleThreadScheduledExecutor();
        }
        currentStep = 0;
        try {
            midiDevice.open();
            receiver = midiDevice.getReceiver();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
        myTask = new Runnable() {
            @Override
            public void run() {
                outputNotes(currentStep);
                nextStep();
            }
        };
        changeReadInterval(tempo);
    }

    public void changeReadInterval(int tempo) {
        long time = bpmToMS(tempo);
        if (time > 0) {
            if (futureTask != null) {
                futureTask.cancel(true);
            }
            futureTask = executorService.scheduleAtFixedRate(myTask, 0, time, TimeUnit.MILLISECONDS);
        }
    }

    private long bpmToMS(int bpm) {
        double ms = 60000 / bpm;
        return (long) (ms / 4.0);
    }

    private void outputNotes(int currentStep) {
        for (int i = 0; i < messages.length; i++) {
            n = setN(i);
            try {
                // Only send a NOTE_ON message if the note is 1
                if (sequence[i][currentStep] == 1) {
                    messages[i].setMessage(ShortMessage.NOTE_ON, 0, n, 100);
                    receiver.send(messages[i], -1);
                }
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < messages.length; i++) {
            n = setN(i);
            try {
                // Only send a NOTE_OFF message if the note is 1
                if (sequence[i][currentStep] == 1) {
                    messages[i].setMessage(ShortMessage.NOTE_OFF, 0, n, 0);
                    receiver.send(messages[i], -1);
                }
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        }
    }

    private int setN(int n) {
        int returnValue = 0;
        if (n == 0) {
            returnValue = 60;
        } else if (n == 1) {
            returnValue = 62;
        } else if (n == 2) {
            returnValue = 64;
        } else {
            returnValue = 65;
        }
        return returnValue;
    }

    private void nextStep() {
        if (currentStep < sequence[0].length - 1) {
            currentStep++;
        } else {
            currentStep = 0;
        }
    }

    public void stop() {
        // Stop the Player
        futureTask.cancel(true);
        executorService.shutdown();
        receiver.close();
        midiDevice.close();
    }

    public void setSequence(int[][] sequence) {
        this.sequence = sequence;
    }
}
