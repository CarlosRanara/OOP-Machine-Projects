import java.util.Scanner;

/**
 * Main class for Fatal Fantasy: Tactics game
 * Entry point for the text-based turn-based fighting game
 *
 * @author Your Name
 * @version 1.0
 */
public class Main {
    /**
     * Main method to start the game
     * @param args command line arguments
     */
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.startGame();
    }
}