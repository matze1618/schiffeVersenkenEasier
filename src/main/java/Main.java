import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    Scanner scan = new Scanner(System.in);
    enum Phase {
        START, PICKPHASE1, PICKPHASE2, ATCKPLAYER1, ATCKPLAYER2
    }
    static Phase phase = Phase.START;
    int width = 10;
    int height = 10;
    String p1Name; //Playername
    String p2Name; //Playername
    String [][] mapLayoutArray = {
            {" ", "|", "1", "2", "3", "4", "5", "6", "7", "8", "9", "z", "|"},
            {"=", "+", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "|"},
            {"A", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"B", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"C", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"D", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"E", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"F", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"G", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"H", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"I", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"J", "|", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "|"},
            {"=", "+", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=", "|"}};

    String [][] mapShips1Array = new String[13][13];
    String [][] mapShips2Array = new String[13][13];
    String [][] mapATCK1Array = new String[13][13];
    String [][] mapATCK2Array = new String[13][13];
    String [][] mapArray = new String[13][13];
    boolean gameOver = false;
    int[] lengthByID = {1, 2, 2, 2, 3, 3, 3, 4, 4, 5};
    String[] id = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    int[] livesByID1 = new int[10];
    int[] livesByID2 = new int[10];

    int p1counter = 10;
    int p2counter = 10;

    public static void main(String[] args) throws InterruptedException {
        Main game = new Main();
        game.copyTwoDimensional(game.mapShips1Array, game.mapLayoutArray);
        game.copyTwoDimensional(game.mapShips2Array, game.mapLayoutArray);
        game.copyTwoDimensional(game.mapATCK1Array, game.mapLayoutArray);
        game.copyTwoDimensional(game.mapATCK2Array, game.mapLayoutArray);
        System.arraycopy(game.lengthByID, 0, game.livesByID1, 0, 10);
        System.arraycopy(game.lengthByID, 0, game.livesByID2, 0, 10);
        while(!game.gameOver) {
            if (phase == Phase.START) {
                game.getName();
//                game.test();
            } else {
                game.draw();
                game.input();
                if(phase == Phase.ATCKPLAYER1 || phase == Phase.ATCKPLAYER2) {
                    TimeUnit.MILLISECONDS.sleep(1500);
                }
            }
        }
        System.out.println("Du hast gewonnen!");
    }

    void draw(){
        switch(phase){
            case ATCKPLAYER1:
                copyTwoDimensional(mapArray, mapATCK1Array);
                break;
            case ATCKPLAYER2:
                copyTwoDimensional(mapArray, mapATCK2Array);
                break;
            case PICKPHASE1:
                copyTwoDimensional(mapArray, mapShips1Array);
                break;
            case PICKPHASE2:
                copyTwoDimensional(mapArray, mapShips2Array);
                break;
        }

        for(int j = 0; j < height + 3; j++){
            for(int i = 0; i < width + 3; i++){
                switch(mapArray[j][i]){
                    case "=":
                        System.out.print("---");
                        break;
                    case "z":
                        System.out.print(" 10");
                        break;
                    case "|":
                        System.out.print("\u007C");
                        break;
                    case "+":
                        System.out.print("+");
                        break;
                    case "0":
                        System.out.print("~~~");
                        break;
                    case "n":
                        System.out.print("~~~");
                        break;
                    case "a", "b", "c", "d", "e", "f", "g", "h", "i", "j":
                        System.out.print(" X ");
                        break;
                    default:
                        System.out.print(" " + mapArray[j][i] + " ");
                        break;
                }
            }
            System.out.println();
        }
    }

    void copyTwoDimensional(String[][]array1, String[][]array2){
        for(int j=0; j < array2.length; j++){
            System.arraycopy(array2[j], 0, array1[j], 0, array2.length);
        }
    }

    void getName(){
        System.out.print("Player1 Name: ");
        p1Name = ANSI_RED + scan.next() + ANSI_RESET;

        System.out.print("Player2 Name: ");
        p2Name = ANSI_GREEN + scan.next() + ANSI_RESET;
        phase = Phase.PICKPHASE1;
        System.out.println();
    }

    void input(){
        String inputHV;
        int xCord;
        String yCord;

        switch(phase){
            case ATCKPLAYER1:
                System.out.println(p1Name + ", wähle eine Position, an die Du schießen möchtest (X Y)");
                xCord = scan.nextInt();
                xCord++;
                yCord = scan.next();
                if (!shoot(xCord, abcToInt(yCord), mapShips2Array)) {
                    phase = Phase.ATCKPLAYER2;
                }
                break;
            case ATCKPLAYER2:
                System.out.println(p2Name + ", wähle eine Position, an die Du schießen möchtest (X Y)");
                xCord = scan.nextInt();
                xCord++;
                yCord = scan.next();

                if (!shoot(xCord, abcToInt(yCord), mapShips1Array)) {
                    phase = Phase.ATCKPLAYER1;
                }
                break;
            case PICKPHASE1:
                System.out.println(p1Name + ", wähle die Position für dein Schiff der Länge " + lengthByID[p1counter - 1] + "\nVertikal oder Horizontal (V/H) und X Y");
                inputHV = scan.next();
                xCord = scan.nextInt();
                xCord++;
                yCord = scan.next();
                if(placeShip(lengthByID[p1counter-1], xCord, yCord, inputHV, mapShips1Array, p1counter)){p1counter--;}
                if(p1counter == 0){ draw(); phase = Phase.PICKPHASE2;}
                break;
            case PICKPHASE2:
                System.out.println(p2Name + ", choose a Position for your ship length " + lengthByID[p2counter - 1] + "\nVertikal oder Horizontal (V/H) und X Y");
                inputHV = scan.next();
                xCord = scan.nextInt();
                xCord++;
                yCord = scan.next();
                if(placeShip(lengthByID[p2counter-1], xCord, yCord, inputHV, mapShips2Array, p2counter)){p2counter--;}
                if(p2counter == 0){
                    phase = Phase.ATCKPLAYER1;}
                break;
        }
    }

    int abcToInt(String yString) {
        switch(yString.toUpperCase()){
            case "A":
                return 2;
            case "B":
                return 3;
            case "C":
                return 4;
            case "D":
                return 5;
            case "E":
                return 6;
            case "F":
                return 7;
            case "G":
                return 8;
            case "H":
                return 9;
            case "I":
                return 10;
            case "J":
                return 11;
            default:
                return 0;
        }
    }

    boolean placeShip(int length, int x, String yString, String orientation, String[][] map, int counter){
        int y = abcToInt(yString);
        if (orientation.equals("V") || orientation.equals("v")) {
            for (int i = 0; i < length; i++) {
                if (!Objects.equals(map[y + i][x], "0")) {
                    System.out.println("Position nicht möglich. Wähle eine neue.");
                    return false;
                }
            }
            for (int i = 0; i < length; i++) {
                map[y + i][x] = id[counter-1]; //Schiff
                placeBlock(y + i, x - 1, map); //Peripherie
                placeBlock(y + i, x + 1, map); //Peripherie
            }
            placeBlock(y - 1, x, map); //Peripherie
            placeBlock(y + length, x, map); //Peripherie
            return true;
        } else if (orientation.equals("H") || orientation.equals("h")) {
            for (int i = 0; i < length; i++) {
                if (!(mapArray[y][x + i]).equals("0")) {
                    System.out.println("Position nicht möglich. Wähle eine neue.");
                    return false;
                }
            }
            for (int i = 0; i < length; i++) {
                mapShips1Array[y][x + i] = id[counter-1];
                placeBlock(y - 1, x + i, map);
                placeBlock(y + 1, x + i, map);
            }
            placeBlock(y, x - 1, map);
            placeBlock(y, x + length, map);
            return true;
        } else {
            System.out.println("Die Eingabe ist ungültig.");
            return false;
        }
    }

    void placeBlock(int y, int x, String[][] map){
        if(Objects.equals(map[y][x], "0")) {
            map[y][x] = "n";
        }
    }

    boolean shoot(int x, int y, String[][] map){
        List letters = Arrays.asList(new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"});
        if(letters.contains(map[y][x])) {
            if(phase == Phase.ATCKPLAYER2) {
                livesByID1[abcToInt(map[y][x].substring(0, 1)) - 2]--;
                mapATCK2Array[y][x] = ANSI_RED + "X" + ANSI_RESET;
            }
            else if(phase == Phase.ATCKPLAYER1){
                livesByID2[abcToInt(map[y][x].substring(0, 1)) - 2]--;
                mapATCK1Array[y][x] = ANSI_RED + "X" + ANSI_RESET;
            }
            map[y][x] = "X";
            System.out.println("Hit!");
            checkForLife();
            return true;
        } else {
            if(phase == Phase.ATCKPLAYER2) {
                mapATCK2Array[y][x] = "X";
            }
            else if(phase == Phase.ATCKPLAYER1){
                mapATCK1Array[y][x] = "X";
            }
            System.out.println("Miss!");
            return false;
        }
    }

    void checkForLife() {
        if (phase == Phase.ATCKPLAYER1) {
            for (int i = 0; i < 10; i++) {
                if (livesByID2[i] == 0) {
                    System.out.println("Du hast Schiff Nummer " + i + " zerstört!");
                    livesByID2[i]--;
                    checkGameOver();
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                if (livesByID1[i] == 0) {
                    System.out.println("Du hast Schiff Nummer " + i + " zerstört!");
                    livesByID1[i]--;
                    checkGameOver();
                }
            }
        }
    }
    
    void checkGameOver() {
        boolean result = true;
        if (phase == Phase.ATCKPLAYER1) {
            for (int i = 0; i < 10; i++) {
                if (livesByID2[i] >= 0) {
                    result = false;
                }
            }
        } else  {
            for (int i = 0; i < 10; i++) {
                if (livesByID1[i] >= 0) {
                    result = false;
                }
            }
        }
        gameOver = result;
    }










    void test(){
        placeShip(5, 2, "c", "v", mapShips1Array, p1counter);
        p1counter--;
        placeShip(4, 4, "a", "v", mapShips1Array, p1counter);
        p1counter--;
        placeShip(4, 6, "a", "v", mapShips1Array, p1counter);
        p1counter--;
        placeShip(3, 8, "a", "v", mapShips1Array, p1counter);
        p1counter--;
        placeShip(3, 10, "a", "v", mapShips1Array, p1counter);
        p1counter--;
        placeShip(3, 11, "g", "v", mapShips1Array, p1counter);
        p1counter--;
        placeShip(2, 9, "g", "v", mapShips1Array, p1counter);
        p1counter--;
        placeShip(2, 7, "g", "v", mapShips1Array, p1counter);
        p1counter--;
        placeShip(2, 5, "g", "v", mapShips1Array, p1counter);
        p1counter--;
        placeShip(1, 3, "i", "v", mapShips1Array, p1counter);
        p1counter--;

        placeShip(5, 2, "c", "v", mapShips2Array, p2counter);
        p2counter--;
        placeShip(4, 4, "a", "v", mapShips2Array, p2counter);
        p2counter--;
        placeShip(4, 6, "a", "v", mapShips2Array, p2counter);
        p2counter--;
        placeShip(3, 8, "a", "v", mapShips2Array, p2counter);
        p2counter--;
        placeShip(3, 10, "a", "v", mapShips2Array, p2counter);
        p2counter--;
        placeShip(3, 11, "g", "v", mapShips2Array, p2counter);
        p2counter--;
        placeShip(2, 9, "g", "v", mapShips2Array, p2counter);
        p2counter--;
        placeShip(2, 7, "g", "v", mapShips2Array, p2counter);
        p2counter--;
        placeShip(2, 5, "g", "v", mapShips2Array, p2counter);
        p2counter--;
        placeShip(1, 3, "i", "v", mapShips2Array, p2counter);
        p2counter--;
        phase = Phase.ATCKPLAYER1;
    }
}
