package dungeon_crawl;

public class Player {
    private int health;
    private int x;
    private int y;
    private int damage;
    private int potions; 

    public Player(int startX, int startY) {
        this.health = 100; 
        this.damage = 10; 
        this.x = startX;
        this.y = startY;
        this.potions = 3; 
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println("You have been defeated!");
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void attackEnemy(Enemy enemy) {
        System.out.println("You attack the " + enemy.getName() + " and deal " + damage + " damage!");
        enemy.takeDamage(damage);
        if (!enemy.isAlive()) {
            System.out.println("You have killed the " + enemy.getName() + "!");
        }
    }

    public void drinkPotion() {
        if (health == 100) {
            System.out.println("You are at full health! You cannot drink a potion.");
            return;
        }

        if (potions > 0) {
            health += 20;
            potions--;
            if (health > 100) {
                health = 100;
            }
            System.out.println("You drank a health potion! Your health is now: " + health);
            System.out.println("Potions left: " + potions);
        } else {
            System.out.println("You have no health potions left!");
        }
    }

    public void addPotion() {
        potions++;
        System.out.println("You found a health potion! You now have " + potions + " potion(s).");
    }

    public int getPotions() {
        return potions;
    }
}
