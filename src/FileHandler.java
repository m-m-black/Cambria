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
}
