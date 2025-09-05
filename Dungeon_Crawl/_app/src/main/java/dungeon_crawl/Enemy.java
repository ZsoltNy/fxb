package dungeon_crawl;

public abstract class Enemy extends NPC {
    protected int damage;
    protected String name;

    public Enemy(int x, int y, int health, int damage, String name) {
        super(x, y, health);
        this.damage = damage;
        this.name = name;
    }

    public void attackPlayer(Player player) {
        System.out.println(name + " attacks the player and deals " + damage + " damage!");
        player.takeDamage(damage);
    }

    public String getName() {
        return name;
    }

    // Check if the enemy is still alive
    public boolean isAlive() {
        return this.health > 0;
    }

    public abstract void useSpecialAbility(Player player);
}
