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

/**
 * The Grid class makes and draws the map of the game.
 */
public class Grid{
    Briefcase briefcase = new Briefcase();
    Bullet bullet = new Bullet();
    Empty empty = new Empty();
    Ninja ninja = new Ninja();
    Player player = new Player();
    Radar radar = new Radar();
    Room room = new Room();
    Shield shield = new Shield();

    Objects map[][] = new Objects[9][9];

    /**
     * Generates the initial blank map
     */
    public void makeEmptyMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = empty;
            }
        }

        for (int i = 1; i < map.length - 1; i = i + 3) {
            for (int j = 1; j < map.length - 1; j = j + 3) {
                map[i][j] = room;
            }
        }
    }

    public Objects getobjAt(int x, int y) {
        return map[y][x];
    }

    public void placeEmpty(int x, int y, Empty empty) {
        map[y][x] = empty;
    }

    public void placeNinja(int x, int y, Ninja ninja) {
        this.ninja = ninja;
        map[y][x] = this.ninja;
    }

    public void placeBullet(int x, int y, Bullet bullet) {
        this.bullet = bullet;
        map[y][x] = this.bullet;
    }

    public void placeBriefcase(int x, int y, Briefcase briefcase) {
        this.briefcase = briefcase;
        map[y][x] = this.briefcase;
    }

    public void placeRadar(int x, int y, Radar radar) {
        this.radar = radar;
        map[y][x] = this.radar;
    }

    public void placeShield(int x, int y, Shield shield) {
        this.shield = shield;
        map[y][x] = this.shield;
    }

    public void placePlayer(int x, int y, Player assface) {
        this.player = assface;
        map[y][x] = this.player;
    }


    public void printMap() {

        for (Objects[] x : map) {
            for (Objects y : x) {
                System.out.print(y);
            }
            System.out.println();
        }
    }
}