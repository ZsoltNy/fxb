package dungeon_crawl;

import java.net.URL;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Dungeon {
    private int width;
    private int height;
    private char[][] map;
    private Player player;
    private List<NPC> npcs;
    private static List<String> completedMaps = new ArrayList<>();
    private String currentMap;

    public Dungeon(String mapFileName) {
        npcs = new ArrayList<>();
        currentMap = mapFileName;
        loadMapFromResources(mapFileName);
    }

    private void loadMapFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("maps/" + fileName);

        if (resource == null) {
            System.out.println("Error: Could not find the map file: " + fileName);
            return;
        }

        File mapFile = new File(resource.getFile());
        List<String> mapLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(mapFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                mapLines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading map file: " + e.getMessage());
            return;
        }

        height = mapLines.size();
        width = mapLines.get(0).length();
        map = new char[height][width];

        for (int row = 0; row < height; row++) {
            map[row] = mapLines.get(row).toCharArray();
            for (int col = 0; col < width; col++) {
                if (map[row][col] == '@') {
                    player = new Player(row, col); // Player starts at '@'
                } else if (map[row][col] == 'G') {
                    npcs.add(new Goblin(row, col)); // Add Goblin at 'G'
                } else if (map[row][col] == 'D') {
                    npcs.add(new Dragon(row, col)); // Add Dragon at 'D'
                }
            }
        }
    }

    public void printMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                boolean npcPresent = false;
                for (NPC npc : npcs) {
                    if (npc.getX() == i && npc.getY() == j) {
                        if (npc instanceof Goblin) {
                            System.out.print('G');
                        } else if (npc instanceof Dragon) {
                            System.out.print('D');
                        }
                        npcPresent = true;
                        break;
                    }
                }
                if (!npcPresent) {
                    if (player.getX() == i && player.getY() == j) {
                        System.out.print('@');
                    } else {
                        System.out.print(map[i][j]);
                    }
                }
            }
            System.out.println();
        }
    }

    public void movePlayer(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        if (map[newX][newY] == '.' || isEnemyAt(newX, newY)) {
            if (isEnemyAt(newX, newY)) {
                for (NPC npc : npcs) {
                    if (npc.getX() == newX && npc.getY() == newY) {
                        if (npc instanceof Enemy) {
                            Enemy enemy = (Enemy) npc;

                            System.out.println("You encountered a " + enemy.getName() + "!");

                            while (player.isAlive() && enemy.isAlive()) {
                                System.out.println("Do you want to attack the " + enemy.getName() + "? (Y/N)");
                                Scanner scanner = new Scanner(System.in);
                                String choice = scanner.nextLine().toUpperCase();

                                if (choice.equals("Y")) {
                                    player.attackEnemy(enemy);
                                    if (!enemy.isAlive()) {
                                        System.out.println("You have defeated the " + enemy.getName() + "!");
                                        map[enemy.getX()][enemy.getY()] = '.';
                                        npcs.remove(npc);

                                        if (enemy instanceof Dragon) {
                                            handleDragonDefeat();
                                        } else if (!areEnemiesLeft()) {
                                            handleMapCompletion();
                                        }
                                        break;
                                    }
                                }

                                if (enemy.isAlive()) {
                                    enemy.attackPlayer(player);
                                    System.out.println("Your remaining health: " + player.getHealth());

                                    if (!player.isAlive()) {
                                        endGame("Game Over! The " + enemy.getName() + " defeated you.");
                                        return;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            } else {
                map[player.getX()][player.getY()] = '.';
                player.move(newX, newY);
                map[newX][newY] = '@';
            }
        } else {
            System.out.println("You can't move there!");
        }
    }

    private boolean areEnemiesLeft() {
        for (NPC npc : npcs) {
            if (npc instanceof Enemy) {
                return true;
            }
        }
        return false;
    }

    private void handleDragonDefeat() {
        System.out.println("You have defeated the Dragon!");
        if (areEnemiesLeft()) {
            System.out.println("There are still goblins on the map. Do you want to move to the next map or stay?");
            System.out.println("Enter 'M' to move to the next map or 'S' to stay on this map.");
        } else {
            handleMapCompletion();
        }

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().toUpperCase();

        if (choice.equals("M")) {
            loadNextRandomMap();
        } else if (choice.equals("S")) {
            System.out.println("You chose to stay on the current map.");
        } else {
            System.out.println("Invalid input. Staying on the current map.");
        }
        scanner.close();
    }

    private void handleMapCompletion() {
        System.out.println("All enemies have been defeated!");
        System.out.println("Enter 'M' to move to the next map or 'Q' to quit the game.");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().toUpperCase();

        if (choice.equals("M")) {
            loadNextRandomMap();
        } else if (choice.equals("Q")) {
            endGame("Thanks for playing! You have completed this map.");
        } else {
            System.out.println("Invalid input. Exiting the game.");
            endGame("Game Over.");
        }
        scanner.close();
    }

    private void loadNextRandomMap() {
        completedMaps.add(currentMap); // Mark current map as completed

        String[] availableMaps = listAvailableMapFiles();
        if (availableMaps == null || availableMaps.length == 0) {
            System.out.println("No more maps available. You have completed the game!");
            System.exit(0);
        }

        List<String> remainingMaps = new ArrayList<>();
        for (String map : availableMaps) {
            if (!completedMaps.contains(map)) {
                remainingMaps.add(map);
            }
        }

        if (remainingMaps.isEmpty()) {
            System.out.println("You have completed all the available maps. Game Over!");
            System.exit(0);
        }

        Random random = new Random();
        String nextMap = remainingMaps.get(random.nextInt(remainingMaps.size()));

        System.out.println("Loading next map: " + nextMap);
        npcs.clear(); // Clear current NPCs
        loadMapFromResources(nextMap);
        currentMap = nextMap;
    }

    private String[] listAvailableMapFiles() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("maps");

        if (resource == null) {
            System.out.println("Error: Could not find maps folder.");
            return null;
        }

        File folder = new File(resource.getFile());
        return folder.list((dir, name) -> name.endsWith(".txt")); // Return only .txt files
    }

    private void endGame(String message) {
        System.out.println(message);
        System.exit(0);
    }

    private boolean isEnemyAt(int x, int y) {
        for (NPC npc : npcs) {
            if (npc.getX() == x && npc.getY() == y && npc instanceof Enemy) {
                return true;
            }
        }
        return false;
    }

    public void drinkPotion() {
        if (player.getPotions() > 0) {
            if (player.getHealth() == 100) {
                System.out.println("You are at full health! You cannot drink a potion.");
            } else {
                player.drinkPotion(); // Player drinks the potion
            }
        } else {
            System.out.println("You don't have any health potions left!");
        }
    }
}
