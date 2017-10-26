package trackmodel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Track {
    List<Line> lines;

    public Track(String trackFilename)
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(trackFilename));
            String inBlock = null;

            while ((inBlock = reader.readLine()) != null)
            {

            }
        }
        catch(FileNotFoundException e)
        {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            System.out.println("Current relative path is: " + s);
            e.printStackTrace();
        }
        catch(IOException e)
        {
            System.out.println("Error reading in file");
        }
    }
}
