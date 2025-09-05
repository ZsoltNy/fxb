package dungeon_crawl;

public abstract class NPC {
    protected int health;
    protected int x;
    protected int y;

    public NPC(int x, int y, int health) {
        this.x = x;
        this.y = y;
        this.health = health;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println("NPC defeated!");
        }
    }

    public abstract void interact(Player player);
}
