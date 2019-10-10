import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

public class FileHandler {

    // Write data to file after each call to evolve()
    // Need to decide what data I want to write
    public static void writeDataToFile(String desired, String errors, String filename) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write(LocalDateTime.now() + "\n");
            writer.write("    Desired values\n");
            writer.write(desired + "\n");
            writer.write("    Errors\n");
            writer.write(errors + "\n");
            writer.write("\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogToFile(double[][] errorsPerGen, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("Hocket error;Density error;Syncopation error;Balance error;Downbeat error;Backbeat error;Spread error" + "\n");
            for (int j = 0; j < errorsPerGen[0].length; j++) {
                if ((j+1) % 10 == 0) {
                    for (int i = 0; i < errorsPerGen.length; i++) {
                        String s = Double.toString(errorsPerGen[i][j]);
                        writer.write(s);
                        if (i < errorsPerGen.length-1) {
                            writer.write(";");
                        }
                    }
                    writer.write("\n");
                }
            }
//            for (int i = 0; i < errorsPerGen.length; i++) {
//                if (i == 0) {
//                    writer.write("Hocket error" + "\n");
//                } else if (i == 1) {
//                    writer.write("Density error" + "\n");
//                } else if (i == 2) {
//                    writer.write("Syncopation error" + "\n");
//                } else if (i == 3) {
//                    writer.write("Balance error" + "\n");
//                } else if (i == 4) {
//                    writer.write("Downbeat error" + "\n");
//                } else if (i == 5) {
//                    writer.write("Backbeat error" + "\n");
//                } else if (i == 6) {
//                    writer.write("Spread error" + "\n");
//                }
//                for (int j = 0; j < errorsPerGen[i].length; j++) {
//                    String s = Double.toString(errorsPerGen[i][j]);
//                    writer.write(s + "\n");
//                }
//                writer.write("\n");
//            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
