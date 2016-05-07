package hangman;

import java.util.Random;
import java.util.Scanner;

public class Game {
    //Declare an array of word to use in game
    private static final String[] wordForGuesing = {"computer", "programmer",
            "software", "debugger", "compiler", "developer", "algorithm",
            "array", "method", "variable"};

    //Declare other global variables
    private String guessWord;
    private String dashedWord;
    private FileReadWriter fileRW;

    //Constructor
    public Game(boolean autoStart) {
        guessWord = getRandWord();
        //Creates a char array with the length of guessWord, creates a string from that
        //and replace null-values with underscore
        dashedWord = new String(new char[guessWord.length()]).replace("\0", "_");
        fileRW = new FileReadWriter();
        if (autoStart) {
            displayMenu();
        }
    }

    //Use Random-class to pick a random word
    private String getRandWord() {
        Random rand = new Random();
        return wordForGuesing[rand.nextInt(9)];
    }

    //Print menu
    public void displayMenu() {
        System.out.println("Welcome to \"Hangman\" game. Please, try to guess my secret word.\n"
                + "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
                + "'HELP' to cheat and 'EXIT' to quit the game.");

        findLetterAndPrintIt();
    }

    private void findLetterAndPrintIt() {
        boolean isHelpUsed = false;
        String letter;
        String dashBuff = dashedWord;
        int mistakes = 0;
        Scanner input = new Scanner(System.in);

        //Run while the answer is not found
        while (!dashBuff.equals(guessWord)) {
            //Print progress
            System.out.println("The secret word is:" + dashBuff.replace("", " "));
            System.out.println("DEBUG " + guessWord);

            letter = input.next();
            //Check input
            if (letter.equals(Command.help.toString())) {
                //Run help
                isHelpUsed = true;
                dashBuff = help(dashBuff);
            //If letter is a single letter
            } else if (letter.matches("[a-z]")) {
                String tmp = checkLetter(letter, dashBuff);

                //If return value from checkLetter is null, increase mistakes
                mistakes += (tmp == null) ? 1 : 0;
                //Else, set dashBuff to the return value
                dashBuff = (tmp != null) ? tmp : dashBuff;
            } else {
                menu(letter);
            }

        }

        if (!isHelpUsed) {
            success(mistakes, dashBuff);
        } else {
            System.out.println("You won with " + mistakes + " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
            System.out.println("The secret word is: " + dashBuff.replace("", " ").trim());
        }

        //Restart game
        new Game(true);
    }

    private void menu(String letter) {
        //Check if letter matches one of commands
        if (letter.equals(Command.restart.toString())) {
            new Game(true);
        } else if (letter.equals(Command.top.toString())) {
            printTop();
            new Game(true);
        } else if (letter.equals(Command.exit.toString())) {
            System.exit(1);
        }
    }

    private String checkLetter(String letter, String dashBuff) {
        int counter = 0;
        //Copy guessWord
        String guessWordCopy = guessWord;
        //Set i to the index of the first occurrence of letter in guessWordCopy
        int i = guessWordCopy.indexOf(letter);

        //While the letter is in guessWordCopy
        while(i != -1) {
            //Replace the letter with underscore in guessWordCopy and replace the underscore with letter in dashBuff
            guessWordCopy = replaceChar(i, guessWordCopy, '_');
            dashBuff = replaceChar(i, dashBuff, letter.charAt(0));

            i = guessWordCopy.indexOf(letter);
            ++counter;
        }

        if (counter == 0) {
            //Return null if there were no occurrence
            System.out.printf("Sorry! There are no unrevealed letters \'%s\'. \n", letter);
            return null;
        } else {
            //Else, return modified dashBuff
            System.out.printf("Good job! You revealed %d letter(s).\n", counter);
            return dashBuff;
        }
    }

    private void success(int mistakes, String word) {
        System.out.println("You won with " + mistakes + " mistake(s).");
        System.out.println("The secret word is: " + word.replace("", " ").trim());

        System.out.println("Please enter your name for the top scoreboard:");
        Scanner input = new Scanner(System.in);
        String playerName = input.next();

        //Write player to scoreboard
        fileRW.openFileToWrite();
        fileRW.addRecords(mistakes, playerName);
        fileRW.closeFileFromWriting();
        printTop();
    }

    private String help (String dashBuff) {
        System.out.println("Enter your guess (1 letter allowed): ");

        //Set i to index first unguessed letter
        int i = dashBuff.indexOf('_');

        //Replace that index with the answer
        return replaceChar(i, dashBuff, guessWord.charAt(i));
    }

    private void printTop() {
        fileRW.openFileToRead();
        fileRW.readRecords();
        fileRW.closeFileFromReading();
        fileRW.printAndSortScoreBoard();
    }

    private String replaceChar(int i, String inpt, char letter) {
        //Convert input to char-array
        char[] temp = inpt.toCharArray();
        //Replace i with letter
        temp[i] = letter;
        //Return string value of the char-array
        return String.valueOf(temp);
    }
}
