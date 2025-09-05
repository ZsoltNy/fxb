package dungeon_crawl;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        // List available map files
        String[] mapFiles = listAvailableMapFiles();
        if (mapFiles == null || mapFiles.length == 0) {
            System.out.println("No map files found.");
            scanner.close();
            return;
        }

        // Display the available maps and allow the player to choose
        System.out.println("Available maps:");
        for (int i = 0; i < mapFiles.length; i++) {
            System.out.println((i + 1) + ": " + mapFiles[i]);
        }

        System.out.println("Please select a map by entering its number:");
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        if (choice < 1 || choice > mapFiles.length) {
            System.out.println("Invalid choice. Exiting the game.");
            scanner.close();
            return;
        }

        // Load the selected map
        String selectedMap = mapFiles[choice - 1];
        Dungeon dungeon = new Dungeon(selectedMap);

        System.out.println("Welcome to the Dungeon Crawler!");

        while (running) {
            dungeon.printMap(); 
            System.out.println("Move with W (up), A (left), S (down), D (right), P (drink potion), or Q to quit.");
            String input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "W":
                    dungeon.movePlayer(-1, 0); // Move up
                    break;
                case "A":
                    dungeon.movePlayer(0, -1); // Move left
                    break;
                case "S":
                    dungeon.movePlayer(1, 0); // Move down
                    break;
                case "D":
                    dungeon.movePlayer(0, 1); // Move right
                    break;
                case "P":
                    dungeon.drinkPotion(); // Player drinks a potion
                    break;
                case "Q":
                    running = false;
                    System.out.println("Game Over. Thanks for playing!");
                    break;
                default:
                    System.out.println(
                            "Invalid command. Please use W, A, S, D to move, P to drink a potion, or Q to quit.");
                    break;
            }
        }

        scanner.close();
    }

    // Method to list available map files in the resources/maps folder
    private static String[] listAvailableMapFiles() {
        ClassLoader classLoader = Main.class.getClassLoader();
        URL resource = classLoader.getResource("maps");
        if (resource == null) {
            System.out.println("Error: Could not find maps folder.");
            return null;
        }

        File folder = new File(resource.getFile());
        return folder.list((dir, name) -> name.endsWith(".txt"));
    }
}