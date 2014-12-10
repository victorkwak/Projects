/**
 * CS 141: Introduction to Programming and Problem Solving
 * Professor: Edwin Rodr&iacute;guez
 *
 * Programming Assignment Ninja Game
 *
 * Develop a text based game for finding a briefcase in a dark
 * house while dodging Ninjas
 *
 * Team Half & Half
 *   Karen Cheung
 *   Victor Kwak
 *   Serenity Waits
 *   Tristan Warner
 */

import java.io.*;
import java.util.Scanner;

/**
 * The UI. The part of the program that directly interacts with the player.
 */
public class UI {
    private Scanner keyboard = new Scanner(System.in);
    private Engine engine = new Engine();
    private String input;
    private char ch;
    private boolean debugBoolean;

    /**
     * Prints the current grid.
     */
    public void printMap() {
        engine.printMap();
    }

    /**
     * Returns whether or not to run through the UI.
     *
     * @return
     */
    public boolean getGameLoopBool() {
        return engine.getGameLoopBool();
    }

    /**
     * Methods to call in the event of a new game.
     */
    public void newGame() {
        engine.makeEmptyMap();
        engine.setPlayerCoords(0, 8);
        engine.setBriefcaseCoords();
        engine.setBulletCoords();
        engine.setNinjaCoordinates();
        engine.setRadarCoords();
        engine.setShieldCoords();
        engine.radarTripped = false;
        engine.setGameLoopBool(false);
        debugBoolean = false;
    }

    /**
     * Prints the starting menu and takes input from the player.
     */
    public void printPlayerMenu() {
        System.out.println("To Start New Game enter N.");
        System.out.println("To continue saved game enter C.");
        System.out.println("To Exit enter E.");
        input = keyboard.next().toUpperCase();
        ch = input.charAt(0);

        switch (ch) {
            default:
                System.out.println("Choice unrecognized. Starting New Game.");
                newGame();
                break;

            case 'N':
                newGame();
                engine.radarTripped = false;
                break;
            case 'C':
                loadMap();
                engine.setGameLoopBool(false);
                engine.debugMode(true);
                break;
            case 'E':
                System.exit(0);
        }
    }

    /**
     * Prints the debug menu.
     */
    public void printDebugMenu() {
        System.out.println("To toggle Debug Mode, enter Z at any time.");
    }

    /**
     * Prints the menu to ask if the player would like to start another game after he
     * has either won or lost.
     */
    public void continueMenu() {
        do {
            engine.printSymbLine("*");
            System.out.println("Do you want to play another game?");
            engine.printSymbLine("-");
            System.out.println(" Y to continue Main Menu.");
            System.out.println(" N to exit.");
            engine.printSymbLine("*");
            input = keyboard.next().toUpperCase();
            ch = input.charAt(0);
        } while (!checkContinue(ch));
    }

    /**
     * Checks the player's input from the continuMenu() method.
     *
     * @param ch Player's input
     * @return true (continue) or false (no)
     */
    public boolean checkContinue(char ch) {
        if (ch == 'Y') {
            engine.resetLives();
            playerTurn();
            return true;
        } else if (ch == 'N') {
            return false;
        } else {
            System.out.println("Invalid choice. Continuing to Main Menu.\n\n");
            engine.resetLives();
            playerTurn();
            return true;
        }
    }

    /**
     * Turns the debug mode on or off, and prints the status of debug mode.
     *
     * @param debugChar
     * @return
     */
    public boolean checkDebugMode(char debugChar) {
        if (!debugBoolean) {
            debugBoolean = true;
            engine.debugMode(false);
            engine.printSymbLine("=");
            System.out.println("Game in DEBUG MODE");
            engine.printSymbLine("=");
        } else {
            debugBoolean = false;
            engine.debugMode(true);
            engine.printSymbLine("=");
            System.out.println("Game in REGULAR MODE");
            engine.printSymbLine("=");
        }
        return true;
    }

    /**
     * The menu which the player sees every turn. Acts  as an intermediary between the player and
     * the rest of the game by taking input and reacting accordingly.
     */
    public void playerTurn() {

        printPlayerMenu();
        engine.debugMode(true);
        printDebugMenu();

        while (!getGameLoopBool()) {
            printMap();

            do {
                engine.printSymbLine("*");
                System.out.println("Which direction would you like to look?");
                System.out.println("(W = up, A = left S = down, D = right)");
                engine.printSymbLine("-");
                System.out.println("To save, press P");
                engine.printSymbLine("-");
                System.out.println("To check a room, press O");
                engine.printSymbLine("-");
                System.out.println("To exit, enter E.");
                engine.printSymbLine("*");

                input = keyboard.next().toUpperCase();
                ch = input.charAt(0);

                while (ch == 'P') {
                    saveMap();
                    engine.printSymbLine("*");
                    System.out.println("Which direction would you like to look?");
                    System.out.println("(W = up, A = left S = down, D = right)");
                    engine.printSymbLine("-");
                    System.out.println("To check a room, press O");
                    engine.printSymbLine("-");
                    System.out.println("To save, press P");
                    engine.printSymbLine("-");
                    System.out.println("To exit, enter E.");
                    engine.printSymbLine("*");

                    input = keyboard.next().toUpperCase();
                    ch = input.charAt(0);
                }
            }
            while (!checkInputWithoutShoot(ch));

            engine.lookPlayer(ch);

            if (!getGameLoopBool()) {
                do {
                    printMap();
                    engine.printSymbLine("*");
                    System.out.println("To pop a cap in a Ninja's ass, enter R.");
                    engine.printSymbLine("-");
                    System.out.println("To move:\n " +
                            "(W = up, A = left S = down, D = right)");

                    System.out.println("To exit, enter E.");
                    engine.printSymbLine("*");

                    input = keyboard.next().toUpperCase();
                    ch = input.charAt(0);
                } while (!checkInput(ch));
                if (ch == 'W' || ch == 'A' || ch == 'S' || ch == 'D') {
                    engine.movePlayer(ch);
                } else if (ch == 'R' && engine.player.getHasBullet()) {
                    System.out.println("In which direction would you like to shoot?");
                    System.out.println("(W = up, A = left S = down, D = right)");
                    input = keyboard.next().toUpperCase();
                    ch = input.charAt(0);
                    engine.shoot(ch);
                } else if (ch == 'R' && !engine.player.getHasBullet()) {
                    engine.printSymbLine("=");
                    System.out.println("You don't have a bullet, smart one.");
                    engine.printSymbLine("=");
                }

                engine.ninjaTurn(engine.ninja1);
                engine.ninjaTurn(engine.ninja2);
                engine.ninjaTurn(engine.ninja3);
                engine.ninjaTurn(engine.ninja4);
                engine.ninjaTurn(engine.ninja5);
                engine.ninjaTurn(engine.ninja6);

                int turns = engine.checkShields();
                if (turns < 6 && turns >= 0) {
                    System.out.println("You have " + turns + " turns left with the shield.");
                    engine.printSymbLine("-");
                }

                if (engine.playerDies()) {
                    engine.setGameLoopBool(true);
                }
            }
        }
        continueMenu();
    }

    /**
     * Checks the player's input to see where to continue in the game.
     *
     * @param ch Player's input
     * @return
     */
    public boolean checkInput(char ch) {
        char testChar;
        testChar = ch;
        if (testChar == 'W' || testChar == 'A' || testChar == 'S'
                || testChar == 'D' || testChar == 'R') {
            return true;
        } else if (testChar == 'O') {
            if (engine.checkValidRoom()) {
                return true;
            } else {

                engine.printSymbLine("=");
                System.out.println("You try to OPEN A DOOR\n\nWho are you kidding?\nThere's no door there.");
                engine.printSymbLine("=");
                return false;
            }
        } else if (testChar == 'Z') {
            checkDebugMode(testChar);
            return false;
        } else if (testChar == 'E') {
            System.exit(0);
            return true;
        } else {
            engine.printSymbLine("=");
            System.out.println("Who are you kidding?\n\"" + testChar
                    + "\" is not a valid direction.");
            engine.printSymbLine("=");
            return false;
        }
    }

    /**
     * Checks what the player's input is and acts accordingly.
     *
     * @param ch
     * @return
     */
    public boolean checkInputWithoutShoot(char ch) {
        char testChar;
        testChar = ch;
        if (testChar == 'W' || testChar == 'A' || testChar == 'S'
                || testChar == 'D') {
            return true;
        } else if (testChar == 'O') {
            if (engine.checkValidRoom()) {
                return true;
            } else {
                engine.printSymbLine("=");
                System.out.println("You try to OPEN A DOOR\n\nWho are you kidding?\nThere's no door there.");
                engine.printSymbLine("=");
                return false;
            }
        } else if (testChar == 'Z') {
            checkDebugMode(testChar);
            printMap();

            return false;
        } else if (testChar == 'E') {
            System.exit(0);
            return true;
        } else {
            engine.printSymbLine("=");
            System.out.println("Who are you kidding?\n\"" + testChar
                    + "\" is not a valid direction.");
            engine.printSymbLine("=");
            return false;
        }
    }

    /**
     * Saves the current state of the game.
     */
    public void saveMap() {
        System.out.println("~~~~~~~~~~~~~~~~~");
        System.out.println("Game Saved");
        System.out.println("~~~~~~~~~~~~~~~~~");
        try {
            FileOutputStream fos = new FileOutputStream("Save.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(engine);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a previously saved game.
     */
    public void loadMap() {
        try {
            FileInputStream fis = new FileInputStream("Save.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            engine = (Engine) ois.readObject();
            ois.close();
            fis.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}