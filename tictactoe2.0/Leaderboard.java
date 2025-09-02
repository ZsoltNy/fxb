import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Leaderboard {
    private Map<String, Integer> leaderboard;
    private final String filePath = "leaderboard.txt";

    public Leaderboard() {
        leaderboard = new HashMap<>();
        loadLeaderboard();
    }

    public void updateLeaderboard(String playerName) {
        leaderboard.put(playerName, leaderboard.getOrDefault(playerName, 0) + 1);
        saveLeaderboard();
    }

    public void printLeaderboard() {
        System.out.println("\nLeaderboard:");
        for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " győzelem");
        }
    }

    private void saveLeaderboard() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Nem sikerült menteni a leaderboard-ot: " + e.getMessage());
        }
    }

    private void loadLeaderboard() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int wins = Integer.parseInt(parts[1]);
                    leaderboard.put(name, wins);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nem található leaderboard fájl, új leaderboard létrehozása.");
        } catch (IOException e) {
            System.err.println("Nem sikerült betölteni a leaderboard-ot: " + e.getMessage());
        }
    }
}


