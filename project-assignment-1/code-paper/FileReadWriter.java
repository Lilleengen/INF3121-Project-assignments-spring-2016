package hangman;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class FileReadWriter {
    private ObjectOutputStream output;
    private ObjectInputStream input;

    ArrayList<Player> myArr = new ArrayList<>();

    public void openFileToWrite() {
        //Try to open file
        try
        {
            output = (new File("players.ser").exists()) ?
                    new AppendingObjectOutputStream(new FileOutputStream("players.ser", true)) :
                    new ObjectOutputStream(new FileOutputStream("players.ser", true));
        } catch (IOException ioException) {
            System.err.println("Error opening file.");
        }
    }

    public void addRecords(int score, String name) {
        //Create object to write to file
        Player player = new Player(name, score);

        //Write values to file
        try {
            output.writeObject(player);
        } catch (IOException ioException) {
            System.err.println("Error writing to file.");
        }
    }

    public void closeFileFromWriting() {
        //Try to close file
        try
        {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioException) {
            //Show error
            System.err.println("Error closing file.");
            System.exit(1);
        }
    }

    public void openFileToRead() {
        //Try to read file
        try {
            input = new ObjectInputStream(new FileInputStream("players.ser"));
        } catch (IOException ioException) {
            System.err.println("Error opening file.");
            System.exit(1);
        }
    }

    public void readRecords() {
        Object obj;
        // Try to input the values from the file
        try
        {
            while ((obj = input.readObject()) != null && obj instanceof Player) {
                myArr.add((Player) obj);
                System.out.printf("DEBUG: %-10d%-12s\n", ((Player) obj).getScore(), ((Player) obj).getName());
            }
        }
        catch (EOFException endOfFileException) {
            // end of file was reached
        } catch (ClassNotFoundException classNotFoundException) {
            System.err.println("Unable to create object.");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.err.println("Error during reading from file.");
        }
    }

    public void closeFileFromReading() {
        tryCloseFileFromReading();
    }

    public void printAndSortScoreBoard() {
        Collections.sort(myArr);

        System.out.println("Scoreboard:");
        for (int i = 0; i < myArr.size(); i++) {
            System.out.printf("%d. %s ----> %d\n", i+1, myArr.get(i).getName(), myArr.get(i).getScore());
        }
    }

    private void tryCloseFileFromReading() {
        //Try to close file from reading
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioException) {
            System.err.println("Error closing file.");
            System.exit(1);
        }
    }
}
