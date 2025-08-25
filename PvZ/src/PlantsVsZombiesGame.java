// Plants vs Zombies - Enhanced with Sun Collection and Shovel Feature
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.sound.sampled.*;
import java.io.*;

/**
 * Audio Manager - Handles background music and sound effects
 */
class AudioManager {
    private Clip backgroundMusic;
    private FloatControl volumeControl;
    private float currentVolume = 0.7f;
    private boolean isMuted = false;
    private float previousVolume = 0.7f;

    public AudioManager() {
        loadBackgroundMusic();
    }

    private void loadBackgroundMusic() {
        try {
            File musicFile = new File("music.wav");
            if (!musicFile.exists()) {
                String[] possibleFiles = {"background.wav", "bgm.wav", "music.au", "music.aiff"};
                boolean found = false;
                for (String filename : possibleFiles) {
                    musicFile = new File(filename);
                    if (musicFile.exists()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("No music file found. Looking for: music.wav, background.wav, or bgm.wav");
                    return;
                }
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);

            if (backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(currentVolume);
            }
            System.out.println("✓ Background music loaded successfully: " + musicFile.getName());
        } catch (Exception e) {
            System.out.println("✗ Could not load background music: " + e.getMessage());
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("♪ Background music started playing");
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            currentVolume = Math.max(0.0f, Math.min(1.0f, volume));
            float minGain = volumeControl.getMinimum();
            float maxGain = volumeControl.getMaximum();
            float gain = currentVolume == 0.0f ? minGain : minGain + (maxGain - minGain) * currentVolume;
            volumeControl.setValue(gain);
        }
    }

    public float getVolume() { return currentVolume; }
    public void volumeUp() { setVolume(currentVolume + 0.1f); }
    public void volumeDown() { setVolume(currentVolume - 0.1f); }

    public void toggleMute() {
        if (isMuted) {
            setVolume(previousVolume);
            isMuted = false;
        } else {
            previousVolume = currentVolume;
            setVolume(0.0f);
            isMuted = true;
        }
    }

    public boolean isMuted() { return isMuted; }

    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.close();
        }
    }
}

/**
 * Collectible Sun class - Suns that fall from sky and from sunflowers
 */
class Sun {
    private float x, y;
    private float targetY;
    protected boolean falling; // Changed from private to protected
    private boolean collectible;
    private long createdTime;
    private long collectibleTime;
    private boolean collected;
    private float pulsePhase;

    // Sky sun constructor
    public Sun(float x, float targetY) {
        this.x = x;
        this.y = -30; // Start above screen
        this.targetY = targetY;
        this.falling = true;
        this.collectible = false;
        this.createdTime = System.currentTimeMillis();
        this.collected = false;
        this.pulsePhase = 0;
    }

    // Sunflower sun constructor
    public Sun(float x, float y, boolean fromSunflower) {
        this.x = x;
        this.y = y;
        this.targetY = y; // Already at target position
        this.falling = false;
        this.collectible = true;
        this.createdTime = System.currentTimeMillis();
        this.collectibleTime = System.currentTimeMillis();
        this.collected = false;
        this.pulsePhase = 0;
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        pulsePhase += 0.2f;

        if (falling) {
            // Sun falls from sky
            y += 2.0f;
            if (y >= targetY) {
                y = targetY;
                falling = false;
                collectible = true;
                collectibleTime = currentTime;
            }
        }

        // Suns disappear after 8 seconds if not collected
        if (collectible && currentTime - collectibleTime > 8000) {
            collected = true; // Mark for removal
        }
    }

    public void render(Graphics g) {
        if (collected) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Pulsing effect - faster pulse when falling for attention
        float pulseSpeed = falling ? 0.4f : 0.2f;
        pulsePhase += pulseSpeed;
        float pulse = 1.0f + 0.15f * (float)Math.sin(pulsePhase);
        int size = (int)(25 * pulse);
        int drawX = (int)(x - size/2);
        int drawY = (int)(y - size/2);

        // Different glow effects for falling vs stationary
        if (falling) {
            // Falling sun - bright animated glow trail
            g2d.setColor(new Color(255, 255, 0, 100));
            g2d.fillOval(drawX - 15, drawY - 15, size + 30, size + 30);

            g2d.setColor(new Color(255, 215, 0, 60));
            g2d.fillOval(drawX - 20, drawY - 20, size + 40, size + 40);

            // Trail effect for falling suns
            for (int i = 1; i <= 3; i++) {
                int trailY = drawY - (i * 8);
                int trailAlpha = 30 / i;
                g2d.setColor(new Color(255, 255, 0, trailAlpha));
                g2d.fillOval(drawX + 5, trailY + 5, size - 10, size - 10);
            }
        } else if (collectible) {
            // Stationary sun - steady glow
            g2d.setColor(new Color(255, 255, 0, 80));
            g2d.fillOval(drawX - 10, drawY - 10, size + 20, size + 20);

            g2d.setColor(new Color(255, 255, 150, 40));
            g2d.fillOval(drawX - 15, drawY - 15, size + 30, size + 30);
        }

        // Main sun body
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(drawX, drawY, size, size);

        // Sun rays - rotate faster when falling
        float rayRotation = falling ? pulsePhase * 0.2f : pulsePhase * 0.1f;
        g2d.setColor(Color.ORANGE);
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4 + rayRotation;
            int rayX = (int)(x + (size/2 + 8) * Math.cos(angle));
            int rayY = (int)(y + (size/2 + 8) * Math.sin(angle));
            g2d.fillOval(rayX - 2, rayY - 2, 4, 4);
        }

        // Sun face
        g2d.setColor(Color.BLACK);
        g2d.fillOval(drawX + size/3, drawY + size/3, 3, 3); // Eye
        g2d.fillOval(drawX + 2*size/3, drawY + size/3, 3, 3); // Eye
        g2d.drawArc(drawX + size/4, drawY + size/2, size/2, size/4, 0, -180); // Smile

        // Value text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String value = "25";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (int)(x - fm.stringWidth(value)/2);
        int textY = (int)(y + 4);
        g2d.drawString(value, textX, textY);

        // Border - thicker when falling to show it's clickable
        g2d.setColor(Color.ORANGE);
        g2d.setStroke(new BasicStroke(falling ? 3 : 2));
        g2d.drawOval(drawX, drawY, size, size);

        // Click hint for falling suns
        if (falling) {
            g2d.setColor(new Color(255, 255, 255, 150));
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("CLICK!", (int)(x - 12), (int)(y - 20));
        }
    }

    public boolean checkClick(int mouseX, int mouseY) {
        if (collected) return false;

        // Allow clicking during fall OR when collectible on ground
        if (!falling && !collectible) return false;

        // Larger click radius for easier collection (especially when falling)
        double distance = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2));
        if (distance <= 40) { // Increased to 40 for better mid-air catching
            collected = true;
            return true;
        }
        return false;
    }

    // Getters
    public boolean isCollected() { return collected; }
    public boolean isCollectible() { return collectible; }
    public float getX() { return x; }
    public float getY() { return y; }
}

/**
 * Abstract base class for all game entities
 */
abstract class GameEntity {
    protected int x, y;
    protected int health;
    protected int maxHealth;
    protected boolean alive;

    public GameEntity(int x, int y, int health) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = health;
        this.alive = true;
    }

    public abstract void update();
    public abstract void render(Graphics g);

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public boolean isAlive() { return alive; }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
    }
}

/**
 * Abstract Zombie class
 */
abstract class Zombie extends GameEntity {
    protected int speed;
    protected int damage;
    protected int lane;
    protected long lastMoveTime;
    protected boolean attacking;

    public Zombie(int x, int y, int health, int speed, int damage, int lane) {
        super(x, y, health);
        this.speed = speed;
        this.damage = damage;
        this.lane = lane;
        this.lastMoveTime = System.currentTimeMillis();
        this.attacking = false;
    }

    @Override
    public void update() {
        if (!attacking) {
            long currentTime = System.currentTimeMillis();
            long moveInterval = 200 - (speed * 20);
            if (currentTime - lastMoveTime >= moveInterval) {
                x -= 2;
                lastMoveTime = currentTime;
            }
        }
    }

    public int getLane() { return lane; }
    public int getDamage() { return damage; }
    public int getSpeed() { return speed; }
    public boolean hasReachedHome() { return x <= 10; }
    public void setAttacking(boolean attacking) { this.attacking = attacking; }
    public boolean isAttacking() { return attacking; }
}

/**
 * Normal Zombie
 */
class NormalZombie extends Zombie {
    public NormalZombie(int x, int y, int lane) {
        super(x, y, 70, 4, 10, lane);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(139, 169, 19));
        g.fillRect(x, y, 40, 60);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 40, 60);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Z", x + 18, y + 35);

        // Health bar
        g.setColor(Color.RED);
        g.fillRect(x, y - 8, 40, 4);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 8, (int)(40.0 * health / maxHealth), 4);
    }
}

/**
 * Flag Zombie
 */
class FlagZombie extends Zombie {
    public FlagZombie(int x, int y, int lane) {
        super(x, y, 70, 3, 10, lane);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(139, 169, 19));
        g.fillRect(x, y, 40, 60);
        g.setColor(Color.RED);
        g.fillRect(x + 35, y, 15, 25);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 40, 60);
        g.drawLine(x + 35, y, x + 35, y + 40);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("F", x + 18, y + 35);

        // Health bar
        g.setColor(Color.RED);
        g.fillRect(x, y - 8, 40, 4);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 8, (int)(40.0 * health / maxHealth), 4);
    }
}

/**
 * Conehead Zombie
 */
class ConeheadZombie extends Zombie {
    public ConeheadZombie(int x, int y, int lane) {
        super(x, y, 70, 2, 8, lane);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(139, 169, 19));
        g.fillRect(x, y, 40, 60);

        // Traffic cone
        g.setColor(Color.ORANGE);
        int[] coneX = {x + 15, x + 25, x + 20};
        int[] coneY = {y, y, y - 15};
        g.fillPolygon(coneX, coneY, 3);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, 40, 60);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("C", x + 18, y + 35);

        // Health bar
        g.setColor(Color.RED);
        g.fillRect(x, y - 8, 40, 4);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 8, (int)(40.0 * health / maxHealth), 4);
    }
}

/**
 * Abstract Plant class
 */
abstract class Plant extends GameEntity {
    protected int cost;
    protected int regenerateRate;
    protected int damage;
    protected int range;
    protected int directDamage;
    protected int speed;
    protected long lastActionTime;
    protected int row, col;

    public Plant(int x, int y, int health, int cost, int regenerateRate, int damage,
                 int range, int directDamage, int speed, int row, int col) {
        super(x, y, health);
        this.cost = cost;
        this.regenerateRate = regenerateRate;
        this.damage = damage;
        this.range = range;
        this.directDamage = directDamage;
        this.speed = speed;
        this.lastActionTime = System.currentTimeMillis();
        this.row = row;
        this.col = col;
    }

    public abstract void performAction(GameManager gameManager);

    public int getCost() { return cost; }
    public int getRegenerateRate() { return regenerateRate; }
    public int getDamage() { return damage; }
    public int getRange() { return range; }
    public int getDirectDamage() { return directDamage; }
    public int getSpeed() { return speed; }
    public int getRow() { return row; }
    public int getCol() { return col; }

    public boolean canPerformAction() {
        return System.currentTimeMillis() - lastActionTime >= (regenerateRate * 1000);
    }
}

/**
 * Sunflower - Generates collectible sun
 */
class Sunflower extends Plant {
    public Sunflower(int x, int y, int row, int col) {
        super(x, y, 50, 50, 10, 0, 0, 0, 1, row, col);
    }

    @Override
    public void update() {
        // Sunflowers generate sun based on regenerate rate
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x + 5, y + 5, 50, 50);

        // Petals
        g.setColor(Color.ORANGE);
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            int petalX = (int)(x + 30 + 20 * Math.cos(angle));
            int petalY = (int)(y + 30 + 20 * Math.sin(angle));
            g.fillOval(petalX - 5, petalY - 5, 10, 10);
        }

        g.setColor(Color.BLACK);
        g.drawOval(x + 5, y + 5, 50, 50);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("S", x + 27, y + 35);

        // Health bar
        g.setColor(Color.RED);
        g.fillRect(x, y - 8, 60, 4);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 8, (int)(60.0 * health / maxHealth), 4);
    }

    @Override
    public void performAction(GameManager gameManager) {
        if (canPerformAction() && gameManager != null) {
            // Create a collectible sun instead of directly adding sun
            gameManager.addSunDrop(x + 30, y + 30, true);
            lastActionTime = System.currentTimeMillis();
        }
    }
}

/**
 * Peashooter
 */
class Peashooter extends Plant {
    public Peashooter(int x, int y, int row, int col) {
        super(x, y, 50, 100, 2, 20, 800, 20, 2, row, col);
    }

    @Override
    public void update() {
        // Peashooters attack automatically
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(34, 139, 34));
        g.fillOval(x + 5, y + 5, 50, 50);

        // Cannon
        g.setColor(new Color(0, 100, 0));
        g.fillRect(x + 50, y + 25, 15, 10);

        g.setColor(Color.BLACK);
        g.drawOval(x + 5, y + 5, 50, 50);
        g.drawRect(x + 50, y + 25, 15, 10);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("P", x + 27, y + 35);

        // Health bar
        g.setColor(Color.RED);
        g.fillRect(x, y - 8, 60, 4);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 8, (int)(60.0 * health / maxHealth), 4);
    }

    @Override
    public void performAction(GameManager gameManager) {
        if (canPerformAction()) {
            List<Zombie> zombiesInLane = gameManager.getZombiesInLane(row);
            for (Zombie zombie : zombiesInLane) {
                if (zombie.getX() > x && zombie.getX() - x <= range) {
                    gameManager.addProjectile(new Pea(x + 65, y + 30, row, damage));
                    lastActionTime = System.currentTimeMillis();
                    break;
                }
            }
        }
    }
}

/**
 * Cherry Bomb
 */
class CherryBomb extends Plant {
    private boolean exploded = false;
    private boolean hasPerformedExplosion = false;
    private long plantTime;
    private int explosionFrames = 0;
    private static final int EXPLOSION_DISPLAY_TIME = 60;

    public CherryBomb(int x, int y, int row, int col) {
        super(x, y, 1, 150, 1, 1800, 150, 1800, 1, row, col);
        this.plantTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        if (!exploded && System.currentTimeMillis() - plantTime > 1500) {
            exploded = true;
            explosionFrames = 0;
        }

        if (exploded) {
            explosionFrames++;
            if (explosionFrames > EXPLOSION_DISPLAY_TIME) {
                alive = false;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (!exploded) {
            // Cherry Bomb countdown
            g.setColor(Color.RED);
            g.fillOval(x + 5, y + 5, 25, 25);
            g.fillOval(x + 30, y + 5, 25, 25);

            g.setColor(new Color(0, 128, 0));
            g.fillRect(x + 27, y, 6, 15);

            g.setColor(Color.BLACK);
            g.drawOval(x + 5, y + 5, 25, 25);
            g.drawOval(x + 30, y + 5, 25, 25);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("B", x + 20, y + 25);

            long timeLeft = 1500 - (System.currentTimeMillis() - plantTime);
            if (timeLeft > 0) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                g.drawString(String.valueOf((timeLeft / 1000) + 1), x + 35, y + 15);
            }
        } else if (explosionFrames <= EXPLOSION_DISPLAY_TIME) {
            // Explosion animation
            int pulseSize = (int)(Math.sin(explosionFrames * 0.3) * 10) + 10;

            g.setColor(Color.ORANGE);
            g.fillOval(x - 60 - pulseSize, y - 60 - pulseSize, 180 + (pulseSize * 2), 180 + (pulseSize * 2));

            g.setColor(Color.RED);
            g.fillOval(x - 40 - pulseSize/2, y - 40 - pulseSize/2, 140 + pulseSize, 140 + pulseSize);

            g.setColor(Color.YELLOW);
            g.fillOval(x - 20, y - 20, 100, 100);

            g.setColor(Color.WHITE);
            for (int i = 0; i < 8; i++) {
                double angle = i * Math.PI / 4;
                int particleX = (int)(x + 30 + (40 + pulseSize) * Math.cos(angle));
                int particleY = (int)(y + 30 + (40 + pulseSize) * Math.sin(angle));
                g.fillOval(particleX - 3, particleY - 3, 6, 6);
            }
        }
    }

    @Override
    public void performAction(GameManager gameManager) {
        if (exploded && !hasPerformedExplosion) {
            gameManager.explodeArea(x + 30, y + 30, range, damage);
            hasPerformedExplosion = true;
        }
    }
}

/**
 * Projectile class for peas
 */
class Pea {
    private int x, y, row, damage;
    private boolean active;
    private static final int SPEED = 8;

    public Pea(int x, int y, int row, int damage) {
        this.x = x;
        this.y = y;
        this.row = row;
        this.damage = damage;
        this.active = true;
    }

    public void update() {
        if (active) {
            x += SPEED;
            if (x > 850) {
                active = false;
            }
        }
    }

    public void render(Graphics g) {
        if (active) {
            g.setColor(new Color(124, 252, 0));
            g.fillOval(x, y, 8, 8);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, 8, 8);
        }
    }

    public boolean checkCollision(Zombie zombie) {
        if (active && zombie.getLane() == row) {
            Rectangle peaRect = new Rectangle(x, y, 8, 8);
            Rectangle zombieRect = new Rectangle(zombie.getX(), zombie.getY(), 40, 60);
            if (peaRect.intersects(zombieRect)) {
                zombie.takeDamage(damage);
                active = false;
                return true;
            }
        }
        return false;
    }

    public boolean isActive() { return active; }
    public int getRow() { return row; }
}

/**
 * Enhanced Game Manager with Sun Collection and Shovel Feature
 */
class GameManager {
    private List<Zombie> zombies;
    private List<Plant> plants;
    private List<Pea> projectiles;
    private List<Sun> suns; // New: List of collectible suns
    private Plant[][] grid;
    private int sunCount; // Changed from sun to sunCount for clarity
    private long gameStartTime;
    private long lastZombieSpawn;
    private long lastSkySunDrop; // New: Track sky sun drops
    private long lastCombatTime;
    private Random random;
    private boolean gameOver;
    private boolean playerWon;
    private int level;
    private int waveZombiesRemaining;
    private AudioManager audioManager;

    private static final int GRID_ROWS = 5;
    private static final int GRID_COLS = 9;
    private static final int TILE_WIDTH = 80;
    private static final int TILE_HEIGHT = 100;
    private static final int GAME_DURATION = 180000;
    private static final int SKY_SUN_DROP_INTERVAL = 8000; // Sky suns drop every 8 seconds

    public GameManager() {
        zombies = new CopyOnWriteArrayList<>();
        plants = new CopyOnWriteArrayList<>();
        projectiles = new CopyOnWriteArrayList<>();
        suns = new CopyOnWriteArrayList<>(); // New: Initialize suns list
        grid = new Plant[GRID_ROWS][GRID_COLS];
        sunCount = 50; // Start with 50 sun as before
        gameStartTime = System.currentTimeMillis();
        lastZombieSpawn = gameStartTime;
        lastSkySunDrop = gameStartTime;
        lastCombatTime = gameStartTime;
        random = new Random();
        gameOver = false;
        playerWon = false;
        level = 1;
        waveZombiesRemaining = 5;

        // Initialize audio
        audioManager = new AudioManager();
        audioManager.playBackgroundMusic();
    }

    public void update() {
        if (gameOver) return;

        long currentTime = System.currentTimeMillis();
        long gameTime = currentTime - gameStartTime;

        // Win condition: game time ends
        if (gameTime >= GAME_DURATION) {
            gameOver = true;
            playerWon = true;
            return;
        }

        // Lose condition: zombie reaches player's home
        for (Zombie zombie : zombies) {
            if (zombie.hasReachedHome()) {
                gameOver = true;
                playerWon = false;
                return;
            }
        }

        // Sky sun drops at constant rate
        if (currentTime - lastSkySunDrop >= SKY_SUN_DROP_INTERVAL) {
            dropSkySun();
            lastSkySunDrop = currentTime;
        }

        spawnZombiesAccordingToSpecs(gameTime);

        // Update all entities
        zombies.removeIf(zombie -> !zombie.isAlive());
        for (Zombie zombie : zombies) {
            zombie.update();
        }

        plants.removeIf(plant -> !plant.isAlive());
        for (Plant plant : plants) {
            plant.update();
            plant.performAction(this);
        }

        projectiles.removeIf(pea -> !pea.isActive());
        for (Pea pea : projectiles) {
            pea.update();
            for (Zombie zombie : zombies) {
                pea.checkCollision(zombie);
            }
        }

        // Update and clean up suns
        suns.removeIf(sun -> sun.isCollected());
        for (Sun sun : suns) {
            sun.update();
        }

        handleCombat();
    }

    /**
     * Drop a sun from the sky at random position
     */
    private void dropSkySun() {
        int randomX = 100 + random.nextInt(600); // Random X between lanes
        int targetY = 400 + random.nextInt(100); // Random Y in bottom area
        suns.add(new Sun(randomX, targetY));
    }

    /**
     * Add a sun drop (from sunflower or other sources)
     */
    public void addSunDrop(float x, float y, boolean fromSunflower) {
        suns.add(new Sun(x, y, fromSunflower));
    }

    /**
     * Handle clicking on suns to collect them - can catch falling or stationary suns
     */
    public boolean collectSun(int mouseX, int mouseY) {
        for (Sun sun : suns) {
            if (sun.checkClick(mouseX, mouseY)) {
                sunCount += 25;
                boolean wasFalling = sun.falling;
                String status = wasFalling ? "MID-AIR" : "GROUND";
                System.out.println("Sun collected " + status + "! Total sun: " + sunCount + " | Suns remaining: " + (suns.size() - 1));
                return true; // Sun was collected
            }
        }
        return false; // No sun was clicked
    }

    private void spawnZombiesAccordingToSpecs(long gameTime) {
        long gameTimeSeconds = gameTime / 1000;

        if (gameTimeSeconds < 30) {
            return;
        }

        long spawnInterval;

        if (gameTimeSeconds >= 30 && gameTimeSeconds <= 80) {
            spawnInterval = 10000;
        } else if (gameTimeSeconds >= 81 && gameTimeSeconds <= 140) {
            spawnInterval = 5000;
        } else if (gameTimeSeconds >= 141 && gameTimeSeconds <= 170) {
            spawnInterval = 3000;
        } else if (gameTimeSeconds >= 171 && gameTimeSeconds <= 180) {
            if (waveZombiesRemaining > 0 && System.currentTimeMillis() - lastZombieSpawn > 300) {
                spawnRandomZombie();
                waveZombiesRemaining--;
                lastZombieSpawn = System.currentTimeMillis();
            }
            return;
        } else {
            return;
        }

        if (System.currentTimeMillis() - lastZombieSpawn >= spawnInterval) {
            spawnRandomZombie();
            lastZombieSpawn = System.currentTimeMillis();
        }
    }

    private void spawnRandomZombie() {
        int lane = random.nextInt(GRID_ROWS);
        int x = 780;
        int y = 50 + lane * TILE_HEIGHT;

        int type = random.nextInt(3);
        Zombie zombie;

        switch (type) {
            case 0:
                zombie = new NormalZombie(x, y, lane);
                break;
            case 1:
                zombie = new FlagZombie(x, y, lane);
                break;
            case 2:
                zombie = new ConeheadZombie(x, y, lane);
                break;
            default:
                zombie = new NormalZombie(x, y, lane);
        }

        zombies.add(zombie);
    }

    private void handleCombat() {
        long currentTime = System.currentTimeMillis();

        // Reset attacking state
        for (Zombie zombie : zombies) {
            zombie.setAttacking(false);
        }

        // Check for zombie-plant encounters
        for (Zombie zombie : zombies) {
            int row = zombie.getLane();
            int col = (zombie.getX() - 50) / TILE_WIDTH;

            if (col >= 0 && col < GRID_COLS && grid[row][col] != null) {
                Plant plant = grid[row][col];
                zombie.setAttacking(true);

                if (currentTime - lastCombatTime >= 1000) {
                    plant.takeDamage(zombie.getDamage());
                    if (!plant.isAlive()) {
                        grid[row][col] = null;
                        plants.remove(plant);
                        zombie.setAttacking(false);
                    }
                    lastCombatTime = currentTime;
                }
            }
        }
    }

    /**
     * Place plant on grid
     */
    public boolean placePlant(int row, int col, Class<? extends Plant> plantType) {
        if (row < 0 || row >= GRID_ROWS || col < 0 || col >= GRID_COLS) {
            return false;
        }

        if (grid[row][col] != null) {
            return false;
        }

        int x = 50 + col * TILE_WIDTH;
        int y = 50 + row * TILE_HEIGHT;

        Plant plant = null;

        if (plantType == Sunflower.class && sunCount >= 50) {
            plant = new Sunflower(x, y, row, col);
            sunCount -= 50;
        } else if (plantType == Peashooter.class && sunCount >= 100) {
            plant = new Peashooter(x, y, row, col);
            sunCount -= 100;
        } else if (plantType == CherryBomb.class && sunCount >= 150) {
            plant = new CherryBomb(x, y, row, col);
            sunCount -= 150;
        }

        if (plant != null) {
            grid[row][col] = plant;
            plants.add(plant);
            return true;
        }

        return false;
    }

    /**
     * Shovel feature - Remove plant from grid
     */
    public boolean shovelPlant(int row, int col) {
        if (row < 0 || row >= GRID_ROWS || col < 0 || col >= GRID_COLS) {
            return false;
        }

        Plant plant = grid[row][col];
        if (plant != null) {
            grid[row][col] = null;
            plants.remove(plant);
            System.out.println("Plant removed from (" + row + ", " + col + ")");
            return true;
        }
        return false;
    }

    public void addProjectile(Pea pea) { projectiles.add(pea); }

    public List<Zombie> getZombiesInLane(int lane) {
        List<Zombie> result = new ArrayList<>();
        for (Zombie zombie : zombies) {
            if (zombie.getLane() == lane) {
                result.add(zombie);
            }
        }
        return result;
    }

    public void explodeArea(int centerX, int centerY, int radius, int damage) {
        for (Zombie zombie : zombies) {
            double distance = Math.sqrt(Math.pow(zombie.getX() + 20 - centerX, 2) +
                    Math.pow(zombie.getY() + 30 - centerY, 2));
            if (distance <= radius) {
                zombie.takeDamage(damage);
            }
        }
    }

    /**
     * Audio control methods
     */
    public void volumeUp() { audioManager.volumeUp(); }
    public void volumeDown() { audioManager.volumeDown(); }
    public void toggleMute() { audioManager.toggleMute(); }
    public float getVolume() { return audioManager.getVolume(); }
    public boolean isMuted() { return audioManager.isMuted(); }

    /**
     * Render game state with suns
     */
    public void render(Graphics g) {
        // Draw background
        g.setColor(new Color(34, 139, 34));
        g.fillRect(0, 0, 800, 600);

        // Draw grid (5 lanes, 9 tiles each)
        g.setColor(new Color(0, 100, 0));
        for (int i = 0; i <= GRID_ROWS; i++) {
            int y = 50 + i * TILE_HEIGHT;
            g.drawLine(50, y, 50 + GRID_COLS * TILE_WIDTH, y);
        }
        for (int i = 0; i <= GRID_COLS; i++) {
            int x = 50 + i * TILE_WIDTH;
            g.drawLine(x, 50, x, 50 + GRID_ROWS * TILE_HEIGHT);
        }

        // Draw all entities
        for (Plant plant : plants) {
            plant.render(g);
        }
        for (Zombie zombie : zombies) {
            zombie.render(g);
        }
        for (Pea pea : projectiles) {
            pea.render(g);
        }

        // Draw suns (render them above other entities so they're clickable)
        for (Sun sun : suns) {
            sun.render(g);
        }

        renderUI(g);

        if (gameOver) {
            renderGameOver(g);
        }
    }

    private void renderUI(Graphics g) {
        // UI background
        g.setColor(new Color(101, 67, 33));
        g.fillRect(0, 0, 800, 50); // Made UI slightly taller

        // Sun counter with better styling
        g.setColor(Color.YELLOW);
        g.fillOval(10, 8, 34, 34);
        g.setColor(Color.ORANGE);
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            int rayX = (int)(27 + 20 * Math.cos(angle));
            int rayY = (int)(25 + 20 * Math.sin(angle));
            g.fillOval(rayX - 2, rayY - 2, 4, 4);
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(String.valueOf(sunCount), 50, 30);

        // Timer
        long gameTime = System.currentTimeMillis() - gameStartTime;
        int secondsLeft = Math.max(0, (int) ((GAME_DURATION - gameTime) / 1000));
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        g.setColor(Color.WHITE);
        g.drawString(String.format("Time: %d:%02d", minutes, seconds), 150, 25);

        // Plant selection
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("1: Sunflower (50)", 350, 15);
        g.drawString("2: Peashooter (100)", 470, 15);
        g.drawString("3: Cherry Bomb (150)", 590, 15);

        // Shovel instruction
        g.setColor(Color.CYAN);
        g.drawString("S: Shovel Mode", 350, 30);

        // Volume control UI
        String volumeText = isMuted() ? "MUTED" : String.format("Vol: %.0f%%", getVolume() * 100);
        g.drawString(volumeText, 250, 25);

        // Instructions with mid-air catching tips
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Catch SUNS mid-air or on ground! | ↑/↓ Volume | M: Mute", 10, 48);

        // Game phase indicator
        long gameTimeSeconds = gameTime / 1000;
        String phase;
        if (gameTimeSeconds < 30) {
            phase = "Preparation Phase";
        } else if (gameTimeSeconds <= 80) {
            phase = "Wave 1 (10s intervals)";
        } else if (gameTimeSeconds <= 140) {
            phase = "Wave 2 (5s intervals)";
        } else if (gameTimeSeconds <= 170) {
            phase = "Wave 3 (3s intervals)";
        } else {
            phase = "FINAL WAVE!";
        }
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Phase: " + phase, 10, 580);

        g.drawString("Zombies: " + zombies.size(), 200, 580);
        g.drawString("Suns on field: " + suns.size(), 320, 580);
    }

    private void renderGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 128));
        g.fillRect(0, 0, 800, 600);

        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.setColor(playerWon ? Color.GREEN : Color.RED);
        String message = playerWon ? "PLANTS WIN!" : "ZOMBIES WIN!";
        FontMetrics fm = g.getFontMetrics();
        int x = (800 - fm.stringWidth(message)) / 2;
        g.drawString(message, x, 300);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        String condition = playerWon ? "Game time ended - You defended successfully!" : "A zombie reached your home!";
        fm = g.getFontMetrics();
        x = (800 - fm.stringWidth(condition)) / 2;
        g.drawString(condition, x, 350);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(Color.YELLOW);
        String restart = "Press R to restart";
        fm = g.getFontMetrics();
        x = (800 - fm.stringWidth(restart)) / 2;
        g.drawString(restart, x, 400);
    }

    // Getters
    public int getSun() { return sunCount; }
    public boolean isGameOver() { return gameOver; }
    public boolean didPlayerWin() { return playerWon; }
    public int getGridRows() { return GRID_ROWS; }
    public int getGridCols() { return GRID_COLS; }
    public int getTileWidth() { return TILE_WIDTH; }
    public int getTileHeight() { return TILE_HEIGHT; }

    public void restart() {
        zombies.clear();
        plants.clear();
        projectiles.clear();
        suns.clear(); // Clear suns on restart
        grid = new Plant[GRID_ROWS][GRID_COLS];
        sunCount = 50;
        gameStartTime = System.currentTimeMillis();
        lastZombieSpawn = gameStartTime;
        lastSkySunDrop = gameStartTime;
        lastCombatTime = gameStartTime;
        gameOver = false;
        playerWon = false;
        level = 1;
        waveZombiesRemaining = 5;

        // Restart background music
        audioManager.stopBackgroundMusic();
        audioManager.playBackgroundMusic();
    }

    public void dispose() {
        if (audioManager != null) {
            audioManager.dispose();
        }
    }
}

/**
 * Enhanced Game Panel with Sun Collection and Shovel Feature
 */
class GamePanel extends JPanel implements KeyListener, MouseListener {
    private GameManager gameManager;
    private javax.swing.Timer gameTimer;
    private Class<? extends Plant> selectedPlant;
    private boolean shovelMode; // New: Track if shovel mode is active

    public GamePanel() {
        gameManager = new GameManager();
        selectedPlant = Sunflower.class;
        shovelMode = false;

        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        // Game loop timer - 60 FPS for smooth gameplay
        gameTimer = new javax.swing.Timer(16, e -> {
            gameManager.update();
            repaint();
        });
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        gameManager.render(g);

        // Show current mode with better visibility
        if (!gameManager.isGameOver()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));

            if (shovelMode) {
                g.setColor(Color.ORANGE);
                g.fillRect(8, 578, 300, 20);
                g.setColor(Color.BLACK);
                g.drawString("🔧 SHOVEL MODE - Click plant to remove", 10, 593);
            } else {
                String plantName = selectedPlant.getSimpleName();
                int cost = getPlantCost(selectedPlant);
                g.setColor(new Color(0, 100, 0));
                g.fillRect(8, 578, 250, 20);
                g.setColor(Color.WHITE);
                g.drawString("🌱 " + plantName + " (" + cost + " sun)", 10, 593);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
                selectedPlant = Sunflower.class;
                shovelMode = false;
                break;
            case KeyEvent.VK_2:
                selectedPlant = Peashooter.class;
                shovelMode = false;
                break;
            case KeyEvent.VK_3:
                selectedPlant = CherryBomb.class;
                shovelMode = false;
                break;
            case KeyEvent.VK_S:
                shovelMode = !shovelMode; // Toggle shovel mode
                System.out.println("Shovel mode: " + (shovelMode ? "ON" : "OFF"));
                break;
            case KeyEvent.VK_R:
                if (gameManager.isGameOver()) {
                    gameManager.restart();
                }
                break;
            // Volume controls
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_EQUALS:
            case KeyEvent.VK_UP:
                gameManager.volumeUp();
                break;
            case KeyEvent.VK_MINUS:
            case KeyEvent.VK_UNDERSCORE:
            case KeyEvent.VK_DOWN:
                gameManager.volumeDown();
                break;
            case KeyEvent.VK_M:
                gameManager.toggleMute();
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameManager.isGameOver()) return;

        int x = e.getX();
        int y = e.getY();

        // PRIORITY 1: Try to collect sun first (falling or stationary)
        if (gameManager.collectSun(x, y)) {
            System.out.println("Sun collected at (" + x + ", " + y + ")");
            return; // Sun was collected, stop here
        }

        // PRIORITY 2: Check if click is in game grid area
        if (y < 50 || y > 550 || x < 50 || x > 770) {
            // Click is outside game area, ignore
            return;
        }

        // Convert pixel coordinates to grid coordinates
        int col = (x - 50) / gameManager.getTileWidth();
        int row = (y - 50) / gameManager.getTileHeight();

        // Validate grid bounds
        if (row < 0 || row >= gameManager.getGridRows() ||
                col < 0 || col >= gameManager.getGridCols()) {
            return;
        }

        // PRIORITY 3: Handle plant placement or shovel
        if (shovelMode) {
            // Shovel mode: remove plant
            if (gameManager.shovelPlant(row, col)) {
                System.out.println("Plant removed from grid (" + row + ", " + col + ")");
            }
        } else {
            // Normal mode: place plant
            boolean placed = gameManager.placePlant(row, col, selectedPlant);
            if (placed) {
                System.out.println("Plant placed at grid (" + row + ", " + col + ")");
            } else {
                System.out.println("Cannot place plant - insufficient sun or tile occupied");
            }
        }
    }

    public void dispose() {
        if (gameManager != null) {
            gameManager.dispose();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    private int getPlantCost(Class<? extends Plant> plantType) {
        if (plantType == Sunflower.class) return 50;
        if (plantType == Peashooter.class) return 100;
        if (plantType == CherryBomb.class) return 150;
        return 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}

public class PlantsVsZombiesGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Plants vs Zombies - Enhanced with Sun Collection & Shovel");
            GamePanel gamePanel = new GamePanel();

            frame.add(gamePanel);
            frame.setSize(800, 650);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            gamePanel.requestFocusInWindow();

            // Add window listener to properly dispose audio resources
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    gamePanel.dispose();
                    System.exit(0);
                }
            });

            // Enhanced project info
            printEnhancedProjectInfo();
        });
    }

    private static void printEnhancedProjectInfo() {
        System.out.println("===============================================================");
        System.out.println("ENHANCED PLANTS VS ZOMBIES - WITH SUN COLLECTION & SHOVEL");
        System.out.println("New Features: Manual Sun Collection + Shovel Feature");
        System.out.println("===============================================================");
        System.out.println();

        System.out.println("NEW FEATURES ADDED:");
        System.out.println("☀️ Mid-Air Sun Collection:");
        System.out.println("   • Click suns while they're falling from the sky!");
        System.out.println("   • Falling suns have bright glowing trails and 'CLICK!' text");
        System.out.println("   • Larger click area (40 pixels) for easier mid-air catching");
        System.out.println("   • Faster animation and rotating rays draw attention to falling suns");
        System.out.println("   • Console shows 'MID-AIR' vs 'GROUND' collection status");
        System.out.println("   • Catch them early for more satisfying gameplay!");
        System.out.println();
        System.out.println("🔧 Shovel Feature:");
        System.out.println("   • Press 'S' to toggle shovel mode");
        System.out.println("   • In shovel mode, click any plant to remove it");
        System.out.println("   • Use to fix placement mistakes or make room for better plants");
        System.out.println("   • Visual indicator shows current mode");
        System.out.println();

        System.out.println("ENHANCED CONTROLS:");
        System.out.println("• 1, 2, 3: Select plants (Sunflower, Peashooter, Cherry Bomb)");
        System.out.println("• S: Toggle shovel mode ON/OFF");
        System.out.println("• Mouse Click: Place plant OR remove plant (shovel mode) OR collect sun");
        System.out.println("• ↑/↓ or =/- keys: Volume control");
        System.out.println("• M: Toggle mute/unmute");
        System.out.println("• R: Restart game (when game over)");
        System.out.println();

        System.out.println("GAMEPLAY CHANGES:");
        System.out.println("• No more automatic sun generation - you must collect suns manually!");
        System.out.println("• Sunflowers create collectible sun drops instead of direct sun");
        System.out.println("• Sky suns provide steady income if you collect them quickly");
        System.out.println("• Strategic plant removal with shovel for better defense layouts");
        System.out.println("• More interactive and engaging resource management");
        System.out.println();

        System.out.println("VISUAL ENHANCEMENTS:");
        System.out.println("• Animated suns with glowing effects and sun rays");
        System.out.println("• Pulsing animation draws attention to collectible suns");
        System.out.println("• Sun counter shows total collected sun");
        System.out.println("• Mode indicator shows plant selection or shovel mode");
        System.out.println("• Enhanced UI with sun icon and better layout");
        System.out.println();

        System.out.println("Starting enhanced game...");
        System.out.println("Remember: You must manually collect suns to gain resources!");
        System.out.println();
    }
}