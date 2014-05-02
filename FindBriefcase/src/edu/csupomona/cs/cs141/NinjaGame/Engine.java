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
package edu.csupomona.cs.cs141.NinjaGame;

import java.io.Serializable;
import java.util.Random;

/**
 * Game's Engine: Provides the mechanics of the game. It contains all of the game's objects, and is what is made
 * serializable so that it can be saved, and later, loaded back to continue from where the player left off.
 */
public class Engine implements Serializable {
    Random rng;
    Grid grid;
    Bullet bullet;
    Shield shield;
    Radar radar;
    Briefcase briefcase;
    Empty empty;
    Room room;
    Ninja ninja;
    Ninja ninja1;
    Ninja ninja2;
    Ninja ninja3;
    Ninja ninja4;
    Ninja ninja5;
    Ninja ninja6;
    Player player;
    Objects objects;
    boolean radarTripped;
    private int playerLives = 3;
    private int shieldTurns = 6;
    private boolean win;
    private boolean gameLoopBool;
    private int numChar;

    /**
     * Creates the objects that are going to be used in this class.
     */
    public Engine() {
        rng = new Random();
        player = new Player();
        grid = new Grid();
        bullet = new Bullet();
        shield = new Shield();
        radar = new Radar();
        briefcase = new Briefcase();
        ninja = new Ninja();
        ninja1 = new Ninja();
        ninja2 = new Ninja();
        ninja3 = new Ninja();
        ninja4 = new Ninja();
        ninja5 = new Ninja();
        ninja6 = new Ninja();
        empty = new Empty();
        room = new Room();
        radarTripped = false;
        gameLoopBool = false;
        numChar = 50;
    }

    /**
     * This method sets the boolean used to determine whether or not the game should continue after the player
     * either wins or dies.
     *
     * @param gameLoop The boolean value determining whether to continue the game or not.
     */
    public void setGameLoopBool(boolean gameLoop) {
        gameLoopBool = gameLoop;
    }

    /**
     * Returns the gameLoopBool value.
     *
     * @return The boolean value determining whether to continue the game or not.
     */
    public boolean getGameLoopBool() {
        return gameLoopBool;
    }

    /**
     * Generates an empty grid to be populated with other objects.
     */
    public void makeEmptyMap() {
        grid.makeEmptyMap();
    }

    /**
     * Sets the X and Y coordinates of the six ninjas at random at the beginning of the game,
     * then places it at those coordinates on the empty grid created by makeEmptyMap() method.
     */
    public void setNinjaCoords() {
        int x;
        int y;
        do {
            x = rng.nextInt(8);
            y = rng.nextInt(8);
        } while (!checkNinja(x, y));
        ninja1.setX(x);
        ninja1.setY(y);
        grid.placeNinja(x, y, ninja1);

        do {
            x = rng.nextInt(8);
            y = rng.nextInt(8);
        } while (!checkNinja(x, y));
        ninja2.setX(x);
        ninja2.setY(y);
        grid.placeNinja(x, y, ninja2);

        do {
            x = rng.nextInt(8);
            y = rng.nextInt(8);
        } while (!checkNinja(x, y));
        ninja3.setX(x);
        ninja3.setY(y);
        grid.placeNinja(x, y, ninja3);

        do {
            x = rng.nextInt(8);
            y = rng.nextInt(8);
        } while (!checkNinja(x, y));
        ninja4.setX(x);
        ninja4.setY(y);
        grid.placeNinja(x, y, ninja4);

        do {
            x = rng.nextInt(8);
            y = rng.nextInt(8);
        } while (!checkNinja(x, y));
        ninja5.setX(x);
        ninja5.setY(y);
        grid.placeNinja(x, y, ninja5);

        do {
            x = rng.nextInt(8);
            y = rng.nextInt(8);
        } while (!checkNinja(x, y));
        ninja6.setX(x);
        ninja6.setY(y);
        grid.placeNinja(x, y, ninja6);

    }

    /**
     * Sets the X and Y coordinates of the bullet at random at the beginning of the game, then
     * places it at those coordinates on the empty grid created by makeEmptyMap() method.
     */
    public void setBulletCoords() {
        int x;
        int y;
        do {
            x = rng.nextInt(8);
            y = rng.nextInt(8);
        } while (!checkConflicts(x, y));
        grid.placeBullet(x, y, bullet);
        bullet.setX(x);
        bullet.setY(y);
    }

    /**
     * Sets the X and Y coordinates of the shield at random at the beginning of the game, then
     * places it at those coordinates on the empty grid created by makeEmptyMap() method.
     */
    public void setShieldCoords() {
        int x;
        int y;
        do {
            x = rng.nextInt(8);
            y = rng.nextInt(8);
        } while (!checkConflicts(x, y));
        grid.placeShield(x, y, shield);
        shield.setX(x);
        shield.setY(y);
    }

    /**
     * Sets the X and Y coordinates of the radar at random at the beginning of the game, then
     * places it at those coordinates on the empty grid created by makeEmptyMap() method.
     */
    public void setRadarCoords() {
        int x;
        int y;
        do {
            x = rng.nextInt(8);
            y = rng.nextInt(8);
        } while (!checkConflicts(x, y));
        grid.placeRadar(x, y, radar);
        radar.setX(x);
        radar.setY(y);
    }

    /**
     * Sets the X and Y coordinates of the briefcase at random at the beginning of the game, then
     * places it at those coordinates on the empty grid created by makeEmptyMap() method.
     */
    public void setBriefcaseCoords() {
        Random rand = new Random();
        int[] coord = new int[3];
        int x;
        int y;

        coord[0] = 1;
        coord[1] = 4;
        coord[2] = 7;
        x = rand.nextInt(3);
        y = rand.nextInt(3);
        grid.placeBriefcase(coord[x], coord[y], briefcase);
    }

    /**
     * Sets the X and Y coordinates of the player according to the input of the player, then
     * places the player on the grid.
     */
    public void setPlayerCoords(int x, int y) {
        player.setX(x);
        player.setY(y);
        grid.placePlayer(x, y, player);
    }

    /**
     * Checks the toStrings of the object being placed against the grid's array.
     * If a non-empty string is returned, then it returns a false boolean that
     * will repeat the index generation until an "empty" space is found.
     *
     * @param x Index number passed to the grid to get the desired space.
     * @param y Index number passed to the grid to get the desired space.
     * @return Returns the result of the check conflicts.
     */

    public boolean checkConflicts(int x, int y) {
        if (grid.getobjAt(x, y).getClass().equals(empty.getClass())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method makes sure that Ninjas will not be randomly placed near the
     * player at the start of the game.
     *
     * @param x The potential X position of a ninja
     * @param y The potential Y position of a ninja
     * @return The boolean returns whether the Ninja can be placed there.
     */

    public boolean checkNinja(int x, int y) {
        if (y > 4 && x > 4
                && grid.getobjAt(x, y).getClass().equals(empty.getClass())) {
            return true;

        } else if (y < 4
                && grid.getobjAt(x, y).getClass().equals(empty.getClass())) {
            return true;
        } else
            return false;
    }

    /**
     * This method is used to set the Debug Mode to either on or off.
     *
     * @param debugB Boolean true for On, and false for off.
     */
    public void debugMode(boolean debugB) {
        checkBriefcase(debugB);
        radar.setPrintBoolean(debugB);
        shield.setPrintBoolean(debugB);
        bullet.setPrintBoolean(debugB);
        ninja1.setPrintBoolean(debugB);
        ninja2.setPrintBoolean(debugB);
        ninja3.setPrintBoolean(debugB);
        ninja4.setPrintBoolean(debugB);
        ninja5.setPrintBoolean(debugB);
        ninja6.setPrintBoolean(debugB);
    }

    /**
     * Checks whether the player can move to the grid space specified by the
     * user. Also checks if the array space contains an item; if there is an
     * item, the method calls for the activation of the item.
     *
     * @param x Potential X coordinate of the player.
     * @param y Potential Y coordinate of the player.
     * @return Boolean value false indicates player can move to those coordinates, while true means
     *         player cannot.
     */
    public boolean checkPlayerMove(int x, int y) {
        if (grid.getobjAt(x, y).getClass().equals(empty.getClass())) {
            return false;
        } else if (grid.getobjAt(x, y).getClass().equals(bullet.getClass())) {
            activateBullet(bullet);
            return false;
        } else if (grid.getobjAt(x, y).getClass().equals(radar.getClass())) {
            activateRadar(radar);
            return false;
        } else if (grid.getobjAt(x, y).getClass().equals(shield.getClass())) {
            activateShield(shield);
            return false;
        } else {
            return true;
        }
    }

    /**
     * The lookPlayer method is going to check if there are any objects two grids ahead of the
     * player in the direction that player inputs, and generates statements about what the player
     * sees.
     *
     * @param direction The direction in which the player chooses to look.
     */
    public void lookPlayer(char direction) {
        int playerX;
        int playerY;
        int plusOne;
        int plusTwo;
        String dir;
        boolean downDir;
        downDir = false;
        playerX = player.getX();
        playerY = player.getY();
        plusOne = 0;
        plusTwo = 0;
        printSymbLine("=");
        switch (direction) {
            case 'W':
                System.out.println("You look UP");
                plusOne = playerY - 1;
                plusTwo = playerY - 2;
                dir = "ahead of";
                checkForYItem(dir, playerX, plusOne, plusTwo, downDir);
                break;
            case 'A':
                System.out.println("You look LEFT");
                plusOne = playerX - 1;
                plusTwo = playerX - 2;
                dir = "left of";
                checkForXItem(dir, playerY, plusOne, plusTwo, downDir);
                break;
            case 'S':
                System.out.println("You look DOWN");
                plusOne = playerY + 1;
                plusTwo = playerY + 2;
                dir = "behind";
                downDir = true;
                checkForYItem(dir, playerX, plusOne, plusTwo, downDir);
                break;
            case 'D':
                System.out.println("You look RIGHT");
                plusOne = playerX + 1;
                plusTwo = playerX + 2;
                dir = "right of";
                checkForXItem(dir, playerY, plusOne, plusTwo, downDir);
                break;
            default:
                break;
        }
        printSymbLine("=");
    }

    /**
     * Used by the lookPlayer() method in order to look for and generate messages about what the player
     * sees relative to the Y coordinates (Up or Down).
     *
     * @param dirName      The direction in which the player looks
     * @param playerXCoord The player's current X coordinate.
     * @param lookYOneSp   One space ahead of the player's look direction.
     * @param lookYTwoSp   Two spaces ahead of the player's look direction.
     * @param downDir      Check if the player is looking down or not, as rooms can only be opened from
     *                     "above"
     */
    public void checkForYItem(String dirName, int playerXCoord, int lookYOneSp,
                              int lookYTwoSp, boolean downDir) {
        int xCoord;
        String dirWord;
        String itemName;
        String singularSp;
        String pluralSp;
        String space;
        int spaceNum;

        dirWord = dirName;
        xCoord = playerXCoord;
        singularSp = "space";
        pluralSp = "spaces";

        int spaces[] = new int[2];
        spaces[0] = lookYOneSp;
        spaces[1] = lookYTwoSp;

        for (int i = 0; i < spaces.length; ++i) {
            spaceNum = i + 1;
            if (spaceNum > 1) {
                space = pluralSp;
            } else {
                space = singularSp;
            }

            if (spaces[i] < 0 || spaces[i] > 8) {
                System.out.println("You can't look " + spaceNum + " " + space
                        + " " + dirWord + " you.");
            } else {
                itemName = checkItemType(xCoord, spaces[i], downDir);
                System.out.println("There is " + itemName + " " + spaceNum
                        + " " + space + " " + dirWord + " you.");
            }
        }

    }

    /**
     * Used by the lookPlayer() method in order to look for and generate messages about what the player
     * sees relative to the Y coordinates (Up or Down).
     *
     * @param dirName      The direction in which the player looks
     * @param playerYCoord The player's current Y coordinate.
     * @param lookXOneSp   One space ahead of the player's look direction.
     * @param lookXTwoSp   Two spaces ahead of the player's look direction.
     * @param downDir      Check if the player is looking down or not, as rooms can only be opened from
     *                     "above"
     */
    public void checkForXItem(String dirName, int playerYCoord, int lookXOneSp,
                              int lookXTwoSp, boolean downDir) {
        int yCoord;
        String dirWord;
        String itemName;
        String singularSp;
        String pluralSp;
        String space;
        int spaceNum;

        dirWord = dirName;
        yCoord = playerYCoord;
        singularSp = "space";
        pluralSp = "spaces";

        int spaces[] = new int[2];
        spaces[0] = lookXOneSp;
        spaces[1] = lookXTwoSp;

        for (int i = 0; i < 2; ++i) {
            spaceNum = i + 1;
            if (spaceNum > 1) {
                space = pluralSp;
            } else {
                space = singularSp;
            }

            if (spaces[i] < 0 || spaces[i] > 8) {
                System.out.println("You can't look " + spaceNum + " " + space
                        + " " + dirWord + " you.");
            } else {
                itemName = checkItemType(spaces[i], yCoord, downDir);
                System.out.println("There is " + itemName + " " + (i + 1) + " "
                        + space + " " + dirWord + " you.");
            }
        }

    }

    /**
     * This method checks for the type of object at a grid position and returns a string indicating
     * what that type is.
     *
     * @param x       X coordinate
     * @param y       Y coordinate
     * @param downDir boolean to see if looking down
     * @return string representing item.
     */
    public String checkItemType(int x, int y, boolean downDir) {
        String bulletName;
        String shieldName;
        String radarName;
        String roomName;
        String doorName;
        String ninjaName;
        String emptyName;
        bulletName = "a bullet";
        shieldName = "a shield";
        radarName = "a radar";
        roomName = "a room";
        doorName = "a door";
        ninjaName = "a ninja";
        emptyName = "nothing";
        if (grid.getobjAt(x, y).getClass().equals(bullet.getClass())) {
            return bulletName;
        } else if (grid.getobjAt(x, y).getClass().equals(shield.getClass())) {
            return shieldName;
        } else if (grid.getobjAt(x, y).getClass().equals(radar.getClass())) {
            return radarName;
        } else if (grid.getobjAt(x, y).getClass().equals(room.getClass())
                || grid.getobjAt(x, y).getClass().equals(briefcase.getClass())) {
            if (downDir) {
                return doorName;
            } else {
                return roomName;
            }
        } else if (grid.getobjAt(x, y).getClass().equals(ninja.getClass())) {
            return ninjaName;
        } else {
            return emptyName;
        }
    }
    /**
     * This method checks to see if the player is near a room and opens room, and whether or not
     * the briefcase is located in that room.
     *
     * @return boolean indicating if player opened a room
     */
    public boolean checkValidRoom() {
        int playerX;
        int playerY;
        int potentialRoomX;
        int potentialRoomY;
        playerX = player.getX();
        playerY = player.getY();
        potentialRoomX = playerX;
        potentialRoomY = playerY + 1;
        if (potentialRoomY > 8) {
            return false;
        } else if (grid.getobjAt(potentialRoomX, potentialRoomY).getClass()
                .equals(room.getClass())) {
            printSymbLine("=");
            System.out
                    .println("You OPEN A DOOR\n\nNothing's in there but some lotion and a tissue\n"
                            + "box surrounded by used tissues covered\nin dried glue.");
            return true;
        } else if (grid.getobjAt(potentialRoomX, potentialRoomY).getClass()
                .equals(briefcase.getClass())) {
            printSymbLine("=");
            System.out
                    .println("You OPEN A DOOR...\n\nAnd found the briefcase!");
            briefcase.setPrintBoolean(false);
            printMap();
            System.out.println("\n\nCongratulations, you win!");
            setGameLoopBool(true);
            printSymbLine("=");
            
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method checks to see if the player can move in the direction that he inputs
     * and moves the player if the check returns true.
     *
     * @param direction The direction input
     * @return true (can move) and false (cannot)
     */
    public boolean movePlayer(char direction) {
        int x;
        int y;
        int tempx;
        int tempy;
        x = player.getX();
        y = player.getY();
        tempx = x;
        tempy = y;
        printSymbLine("=");
        switch (direction) {
            case 'W':
                y--;
                System.out.println("You move UP");
                break;
            case 'A':
                x--;
                System.out.println("You move LEFT");
                break;
            case 'S':
                y++;
                System.out.println("You move DOWN");
                break;
            case 'D':
                x++;
                System.out.println("You move RIGHT");
                break;
            default:

                break;
        }
        printSymbLine("=");
        if (y < 0 || y > 8 || x < 0 || x > 8) {
            return false;
        } else if (checkPlayerMove(x, y)) {
            return false;
        } else {
            setPlayerCoords(x, y);
            grid.placeEmpty(tempx, tempy, empty);
            return true;
        }

    }

    /**
     * This method is used to kill a ninja by shooting a bullet.
     *
     * @param targetedNinja one of the ninjas
     */
    public void killNinja(Ninja targetedNinja) {
        int deadNinjaX;
        int deadNinjaY;
        deadNinjaX = targetedNinja.getX();
        deadNinjaY = targetedNinja.getY();
        grid.placeEmpty(deadNinjaX, deadNinjaY, empty);
        System.out.println("You hit a ninja! DIE YOU FUCKING NINJA SCUM");
        targetedNinja.die();
    }

    /**
     * This method checks to see if that current ninja is alive, and if so, checks that the player is
     * or is not near him and stab is he is. This method also uses the moveNinja() method to move
     * the ninja.
     *
     * @param currentNinja
     */
    public void ninjaTurn(Ninja currentNinja) {
        if (currentNinja.checkIsAlive()) {
            ninjaStab(currentNinja);
            if (!ninjaCheckForPlayer(currentNinja)) {
                moveNinja(currentNinja);
            }
        }
    }

    /**
     * This method generates a random direction for the ninjas to move, checks if that space
     * is empty, then moves them if possible.
     *
     * @param currentNinja Ninjas 1-6
     */
    public void moveNinja(Ninja currentNinja) {
        int x;
        int y;
        int prevNinjaX;
        int prevNinjaY;
        int counter;
        counter = 30;
        do {
            x = currentNinja.getX();
            y = currentNinja.getY();
            prevNinjaX = x;
            prevNinjaY = y;
            int ninjaDir = rng.nextInt(4);

            if (counter <= 30) {
                switch (ninjaDir) {
                    case 0:
                        x++;
                        counter--;
                        break;
                    case 1:
                        x--;
                        counter--;
                        break;
                    case 2:
                        y++;
                        counter--;
                        break;
                    case 3:
                        y--;
                        counter--;
                        break;
                }
            } else {
                break;
            }

        } while (!checkValidNinjaMove(x, y));
        grid.placeNinja(x, y, currentNinja);
        currentNinja.setX(x);
        currentNinja.setY(y);
        grid.placeEmpty(prevNinjaX, prevNinjaY, empty);
    }

    /**
     * Used by moveNinja() method, it checks to see if the grid the ninjas are trying to move
     * to are empty.
     *
     * @param x X coordinate of where the ninja is trying to move to.
     * @param y Y coordinate of where the ninja is trying to move to.
     * @return true (they can) or false (they can't)
     */
    public boolean checkValidNinjaMove(int x, int y) {
        if (y < 0 || y > 8 || x < 0 || x > 8) {
            return false;
        } else if (!grid.getobjAt(x, y).getClass().equals(empty.getClass())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Resets the player's lives if the player decided to start another game.
     */
    public void resetLives() {
        playerLives = 3;
    }

    /**
     * Checks for player and stabs (kills) the player if nearby (next to).
     *
     * @param ninja Ninja 1-6
     */
    public void ninjaStab(Ninja ninja) {
        if (ninjaCheckForPlayer(ninja)) {
            if (!player.getHasShield()) {
                playerLives--;
                playerDie(playerLives);
                checkPlayerDeath(playerLives);
            }
        }
    }

    /**
     * Returns how many turns the shield has remaining.
     *
     * @return
     */
    public int checkShields() {

        if (player.getHasShield() && shieldTurns > 0) {
            shieldTurns--;
            if (shieldTurns == 0) {
                player.setHasShield(false);
            }
            return shieldTurns;
        } else {
            shieldTurns = 6;
            return shieldTurns;
        }
    }

    /**
     * Kills the player, counts down the lives of the player, and places the player back to their
     * original starting place.
     *
     * @param playerLives
     */
    public void playerDie(int playerLives) {
        int x = player.getX();
        int y = player.getY();
        grid.placeEmpty(x, y, empty);
        player.die(playerLives);
        setPlayerCoords(0, 8);
    }

    /**
     * Returns how many lives the player has left.
     *
     * @param playerLives
     */
    public void checkPlayerDeath(int playerLives) {
        this.playerLives = playerLives;
    }

    /**
     * Returns a boolean indicating if player has died or not.
     *
     * @return true (player died) false (player is alive).
     */
    public boolean playerDies() {
        return this.playerLives == 0;
    }

    /**
     * This method checks to see if the player is next to a ninja.
     *
     * @param currentNinja Ninjas 1-6
     * @return true (player is next to a ninja) or false (not)
     */
    public boolean ninjaCheckForPlayer(Ninja currentNinja) {
        int x;
        int y;
        boolean playerNearby;

        x = currentNinja.getX();
        y = currentNinja.getY();

        if ((y + 1 < 9)
                && grid.getobjAt(x, y + 1).getClass().equals(player.getClass())) {
            playerNearby = true;
        } else if ((y - 1 >= 0)
                && grid.getobjAt(x, y - 1).getClass().equals(player.getClass())) {
            playerNearby = true;
        } else if ((x + 1 < 9)
                && grid.getobjAt(x + 1, y).getClass().equals(player.getClass())) {
            playerNearby = true;
        } else if ((x - 1 >= 0)
                && grid.getobjAt(x - 1, y).getClass().equals(player.getClass())) {
            playerNearby = true;
        } else {
            playerNearby = false;
        }
        return playerNearby;
    }

    /**
     * Checks to see if briefcase should be shown, either by radar or by debug mode.
     *
     * @param debugB true (hides) or false (shows)
     */
    public void checkBriefcase(boolean debugB) {
        if (radarTripped) {
            briefcase.setPrintBoolean(false);
        } else {
            briefcase.setPrintBoolean(debugB);
        }
    }

    /**
     * This method activates the radar.
     *
     * @param radar
     */
    public void activateRadar(Radar radar) {
        this.radar = radar;
        radarTripped = true;
        System.out.println("You found the RADAR!");
        briefcase.setPrintBoolean(false);
        System.out.println("There\'s the fucking BRIEFCASE.");
    }

    /**
     * This method activates the bullet.
     *
     * @param bullet
     */
    public void activateBullet(Bullet bullet) {
        this.bullet = bullet;
        System.out.println("You found a BULLET!");
        if (checkBulletItem()) {
            System.out
                    .println("You already have a bullet, dingus.\nYou threw away the one you just picked up.\nRecycle bin, please.");
        } else {
            System.out.println("You picked it up!\nShoot some bitches!!!");
            player.setHasBullet(true);
        }
    }

    /**
     * Checks to see if player has a bullet.
     *
     * @return
     */
    public boolean checkBulletItem() {
        return player.getHasBullet();
    }

    /**
     * This method activates the shield.
     *
     * @param shield
     */
    public void activateShield(Shield shield) {
        this.shield = shield;
        System.out.println("You found a SHIELD!");
        System.out.println("I.....AM.....INVINCIBLE!!!!!!!!");
        player.setHasShield(true);

    }

    /**
     * This method checks to see if you are hitting a room when you shoot (so you don't shoot through).
     *
     * @param x X coordinates of the bullet's path.
     * @param y Y coordinates of the bullet's path.
     * @return true (you hit a room) or false (you didn't)
     */
    public boolean checkShootRoom(int x, int y) {
        switch (x) {
            case 1:
                if (y == 1 || y == 4 || y == 7) {
                    return true;
                }

                break;
            case 4:
                if (y == 1 || y == 4 || y == 7) {
                    return true;
                }
                break;
            case 7:
                if (y == 1 || y == 4 || y == 7) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * This method allows the player to shoot, and checks if the player hit a target.
     *
     * @param direction UP DOWN LEFT RIGHT
     */
    public void shoot(char direction) {
        int playerX;
        int playerY;
        int killCount;
        killCount = 0;
        playerX = player.getX();
        playerY = player.getY();
        switch (direction) {
            case 'W':
                System.out.println("You shoot UP");
                for (int objY = playerY; objY >= 0; objY--) {
                    if (checkShootRoom(playerX, objY)) {
                        System.out.println("You shot at a wall, doofus.");
                        break;
                    } else if (shootNinja(playerX, objY)) {
                        killCount++;
                    }
                }
                break;

            case 'A':
                System.out.println("You shoot LEFT");
                for (int objX = playerX; objX >= 0; objX--) {
                    if (checkShootRoom(objX, playerY)) {
                        System.out.println("You shot at a wall, doofus.");
                        break;
                    } else if (shootNinja(objX, playerY)) {
                        killCount++;
                    }
                }
                break;

            case 'S':
                System.out.println("You shoot DOWN");
                for (int objY = playerY; objY < grid.map[0].length; objY++) {
                    if (checkShootRoom(playerX, objY)) {
                        System.out.println("You shot at a wall, doofus.");
                        break;
                    } else if (shootNinja(playerX, objY)) {
                        killCount++;
                    }
                }
                break;
            case 'D':
                System.out.println("You shoot RIGHT");
                for (int objX = playerX; objX < grid.map.length; objX++) {
                    if (checkShootRoom(objX, playerY)) {
                        System.out.println("You shot at a wall, doofus.");
                        break;
                    } else if (shootNinja(objX, playerY)) {
                        killCount++;
                    }
                }
                break;

            default:
                break;
        }
        player.setHasBullet(false);
        if (killCount > 1) {
            System.out.println("You killed " + killCount + " ninjas!");
        } else if (killCount == 1) {
            System.out.println("You killed " + killCount + " ninja!");
        } else {
            System.out.println("You missed!\nHow could you be so stupid?");
        }
    }

    /**
     * Lets the shoot() method know if you hit a ninja
     *
     * @param x X coordinate of a ninja
     * @param y Y coordinate of a ninja
     * @return true (you killed him) or false (no)
     */
    public boolean shootNinja(int x, int y) {
        if (grid.getobjAt(x, y) == ninja1) {
            killNinja(ninja1);
            return true;
        } else if (grid.getobjAt(x, y) == ninja2) {
            killNinja(ninja2);
            return true;
        } else if (grid.getobjAt(x, y) == ninja3) {
            killNinja(ninja3);
            return true;
        } else if (grid.getobjAt(x, y) == ninja4) {
            killNinja(ninja4);
            return true;
        } else if (grid.getobjAt(x, y) == ninja5) {
            killNinja(ninja5);
            return true;
        } else if (grid.getobjAt(x, y) == ninja6) {
            killNinja(ninja6);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prints the current map for the player to see.
     */
    public void printMap() {
        grid.printMap();
    }

    /**
     * Prints lines of symbols.
     *
     * @param symbol
     */
    public void printSymbLine(String symbol) {
        for (int i = 0; i < numChar; ++i) {
            System.out.print(symbol);
        }
        System.out.println();
    }
}