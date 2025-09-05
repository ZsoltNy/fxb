package dungeon_crawl;

public class Goblin extends Enemy {

    public Goblin(int x, int y) {
        super(x, y, 20, 5, "Goblin");  // Goblin has 20 health and deals 5 damage
    }

    @Override
    public void useSpecialAbility(Player player) {
        System.out.println("The Goblin uses its special ability: Sneaky Stab!");
        attackPlayer(player);  // Special ability deals an extra attack to the player
    }

    @Override
    public void interact(Player player) {
        System.out.println("You encountered a Goblin!");
        attackPlayer(player);
    }
}
