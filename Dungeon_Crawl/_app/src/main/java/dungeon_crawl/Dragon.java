package dungeon_crawl;

public class Dragon extends Enemy {

    public Dragon(int x, int y) {
        super(x, y, 70, 15, "Dragon");
    }

    @Override
    public void useSpecialAbility(Player player) {
        System.out.println("The Dragon breathes fire, dealing massive damage!");
        attackPlayer(player);
    }

    @Override
    public void interact(Player player) {
        System.out.println("You encountered a Dragon!");
        attackPlayer(player);
    }
}
