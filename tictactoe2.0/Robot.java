import java.util.Random;

public class Robot {
    private Random random;

    public Robot() {
        random = new Random();
    }

    public void makeMove(TicTacToe game) {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!game.placeMark(row, col));
        System.out.println("Robot helyez egy jelet a (" + row + ", " + col + ") pozícióra.");
    }
}
