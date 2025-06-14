# CCPROG3 Machine Project (Nats)


## My Overall Strategy

When I first read through the specifications, I realized this wasn't just about making a game that works - it needed to follow the requirements exactly. Like, they specified exact zombie stats and timing intervals, so I couldn't just wing it. I decided to build the core logic first without any graphics, then add the GUI later.

## The Foundation - GameEntity Class

I started with this abstract GameEntity class because I noticed plants and zombies both have health, position, and can die. Instead of duplicating code everywhere, I made this base class:

```java
public abstract class GameEntity {
    protected int x, y;
    protected int health;
    protected int maxHealth;
    protected boolean alive;
}
```

The takeDamage method was important to get right because every entity needs to handle damage consistently. I made it so health never goes negative and automatically sets alive to false when health hits zero. This prevents weird bugs where dead things keep doing stuff.

I used protected fields instead of private because I knew subclasses would need direct access to position and health for movement and combat calculations. Making them private would have meant tons of getter calls which would hurt performance.

## Zombie Implementation Strategy

For zombies, I made an abstract Zombie class that handles all the movement logic, then specific zombie types just define their stats and appearance:

```java
public abstract class Zombie extends GameEntity {
    protected int speed;
    protected int damage;
    protected int lane;
    protected boolean attacking;
}
```

The movement system was tricky. I wanted it to be smooth but also respect the speed attribute from the specs. I ended up using a timer-based approach where each zombie moves based on its speed value:

```java
long moveInterval = 200 - (speed * 20);
if (currentTime - lastMoveTime >= moveInterval && !attacking) {
    x -= 2;
    lastMoveTime = currentTime;
}
```

I tested different formulas before settling on this one. The base 200ms ensures even slow zombies move at a reasonable pace, and subtracting speed times 20 creates meaningful differences between zombie types. Speed 4 zombies move noticeably faster than speed 2 zombies.

The attacking boolean was crucial for the combat system. When a zombie encounters a plant, it stops moving and starts attacking. Without this flag, zombies would keep sliding through plants while damaging them, which looked terrible.

## Plant Architecture

Plants were more complex than zombies because they have so many different attributes according to the specs:

```java
protected int cost;
protected int regenerateRate;
protected int damage;
protected int range;
protected int directDamage;
protected int speed;
```

Even though some plants don't use all attributes, I implemented them all to show I understood the requirements completely. Like, Sunflower has damage set to 0, but the attribute still exists.

The performAction method is where each plant does its thing. I made it abstract so every plant has to implement it, but I also added the canPerformAction helper that uses the regenerate rate to control timing.

## Sunflower - The Economic Engine

Sunflower was actually the simplest plant to implement:

```java
public void performAction(GameManager gameManager) {
    if (canPerformAction() && gameManager != null) {
        gameManager.addSun(25);
        lastActionTime = System.currentTimeMillis();
    }
}
```

I made it generate 25 sun every time based on its regenerate rate. This creates interesting economic decisions - you need multiple sunflower cycles to afford other plants. I playtested different amounts and 25 felt right for the game balance.

## Peashooter - Target Detection and Shooting

The Peashooter was way more complicated because it needs to find targets, shoot projectiles, and manage timing:

```java
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
```

The targeting logic checks for zombies in the same lane, makes sure they're to the right of the peashooter, and verifies they're within range. The break statement is important - without it, the peashooter would shoot multiple peas per action cycle, which would be overpowered.

I spent time tweaking the projectile spawn position. Starting peas at x + 65, y + 30 makes them appear to come from the peashooter's mouth instead of its center. Took some trial and error to make it look natural.

## Cherry Bomb - Area Effect Complexity

Cherry Bomb was definitely the hardest plant to implement correctly. It needs timing, area damage, and visual feedback:

```java
public void performAction(GameManager gameManager) {
    if (exploded && !hasPerformedExplosion) {
        gameManager.explodeArea(x + 30, y + 30, range, damage);
        hasPerformedExplosion = true;
    }
}
```

The hasPerformedExplosion flag prevents multiple explosions. Without it, the explosion would trigger every frame once the bomb went off, which would lag the game and do way too much damage.

I added a countdown timer that shows on the bomb before it explodes. Players need visual feedback about when it's going to go off. The explosion animation lasts exactly 1 second with pulsing effects to make it feel impactful.

## GameManager - The Central Controller

GameManager coordinates everything. I structured the main update loop in priority order:

```java
public void update() {
    if (gameOver) return;
    
    checkWinLoseConditions();
    handleSunGeneration();
    spawnZombiesAccordingToSpecs();
    updateAllEntities();
    handleCombat();
}
```

Win/lose conditions get checked first because the game should end immediately when a condition is met. Then resource generation, then spawning, then updates, then combat last. This order prevents race conditions and ensures consistent game state.

## Timing System - Following Specs Exactly

The specifications demanded exact timing intervals for zombie spawning, so I built a robust timing system:

```java
private void spawnZombiesAccordingToSpecs(long gameTime) {
    long gameTimeSeconds = gameTime / 1000;
    
    if (gameTimeSeconds >= 30 && gameTimeSeconds <= 80) {
        spawnInterval = 10000; // Every 10 seconds
    } else if (gameTimeSeconds >= 81 && gameTimeSeconds <= 140) {
        spawnInterval = 5000;  // Every 5 seconds
    }
    // ... and so on
}
```

I use System.currentTimeMillis() for precision instead of counting frames. Frame-based timing varies depending on computer performance, but millisecond timing is consistent. The specs are very specific about these intervals so I couldn't mess around with approximate timing.

## Combat System - Grid Meets Pixels

I used a hybrid approach for combat - zombies move in pixels for smooth animation, but combat happens on grid coordinates. The tricky part was figuring out how to detect when zombies and plants are on the same tile when they're stored in different data structures.

## My Dual Data Structure Solution

I use two different data structures that work together:

```java
private List<Zombie> zombies;        // All zombies in a list
private List<Plant> plants;          // All plants in a list  
private Plant[][] grid;              // Plants ALSO in a 2D grid
```

The key insight is that plants exist in BOTH a list and a 2D grid. When I place a plant, it goes in both places:

```java
public boolean placePlant(int row, int col, Class<? extends Plant> plantType) {
    Plant plant = new Sunflower(x, y, row, col);
    
    // Put it in BOTH data structures
    grid[row][col] = plant;      // Grid for fast collision detection
    plants.add(plant);           // List for updating/rendering
}
```

## The Combat Detection Process

Here's how I detect when a zombie and plant are on the same tile:

```java
private void handleCombat() {
    for (Zombie zombie : zombies) {
        int row = zombie.getLane();                    // Zombie knows its lane
        int col = (zombie.getX() - 50) / TILE_WIDTH;   // Convert pixel X to grid column
        
        // Check if there's a plant at this grid position
        if (col >= 0 && col < GRID_COLS && grid[row][col] != null) {
            Plant plant = grid[row][col];   // Get the plant from the grid
            zombie.setAttacking(true);
            // damage plant every second
        }
    }
}
```

The magic happens in this formula: `(zombie.getX() - 50) / TILE_WIDTH`
- Zombie X position is in pixels (like 250)
- I subtract 50 because my grid starts at pixel 50
- I divide by TILE_WIDTH (80) to get the column number
- So zombie at pixel 250 becomes column (250-50)/80 = 2

## Example Walkthrough

Let's say a zombie is at pixel position 210 in lane 1, and there's a Sunflower at grid position [1][2]:

1. Zombie lane = 1
2. Zombie column = (210 - 50) / 80 = 2
3. Check grid[1][2] - there's a Sunflower there!
4. Zombie stops moving and attacks the Sunflower

## Why This Approach Works

The dual data structure gives me the best of both worlds:
- **Lists**: Easy iteration over all zombies and plants
- **Grid**: Fast spatial lookup to check "what's at this tile?"

When a plant dies, I clean up both data structures:

```java
if (!plant.isAlive()) {
    grid[row][col] = null;        // Remove from grid
    plants.remove(plant);         // Remove from list
}
```

This hybrid pixel-grid system lets zombies move smoothly in pixels while combat happens on discrete grid tiles. It's way simpler than trying to do pixel-perfect collision detection between every zombie and every plant every frame.

## Projectile System - Simple but Effective

The Pea class is intentionally simple:

```java
public void update() {
    if (active) {
        x += SPEED;
        if (x > 850) {
            active = false;
        }
    }
}
```

Projectiles move at constant speed and automatically deactivate when they leave the screen. I use Java's Rectangle intersection for collision detection because it handles edge cases I might miss with manual calculations.

The collision method returns true when it hits something so the game manager can remove the projectile from the list. Memory management is important with lots of projectiles flying around.

## MCO1 Console Implementation

For the console demonstration requirement, I created a simulation that uses the same core classes but shows text output:

```java
private static void runConsoleDemonstration() {
    GameManager consoleGame = new GameManager();
    
    for (int timeStep = 1; timeStep <= 9; timeStep++) {
        // Simulate game events and print status
        simulateZombieSpawning(currentTime);
        simulateUserInput(currentTime, consoleGame);
    }
}
```

This proves the logic works without GUI complexity. I compress 45 seconds into a 9-step demonstration that shows zombie spawning, plant placement, and progression. It follows the exact format requested in the specifications.

## GUI Implementation

For the GUI, I kept GamePanel simple as a view-controller:

```java
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    gameManager.render(g);
    // show selected plant and other UI
}
```

I enable antialiasing for smoother graphics, then let each entity render itself. This keeps the rendering code modular - each class knows how to draw itself.

Mouse input converts screen coordinates to grid coordinates using the same math as the collision system. This ensures consistency between what players click and where plants actually get placed.

## Debugging and Testing Approach

I added comprehensive debug output for the Cherry Bomb because area effects are hard to debug:

```java
public void explodeArea(int centerX, int centerY, int radius, int damage) {
    System.out.println("Explosion at (" + centerX + ", " + centerY + ")");
    for (Zombie zombie : zombies) {
        double distance = calculateDistance(zombie, centerX, centerY);
        if (distance <= radius) {
            System.out.println("Zombie hit! Distance: " + distance);
            zombie.takeDamage(damage);
        }
    }
}
```

The console output shows explosion coordinates, zombie distances, and which zombies get hit. When area effects don't work as expected, this logging helps identify whether it's a math problem, a timing problem, or a rendering problem.

## Performance Decisions

I chose CopyOnWriteArrayList for entity collections to prevent ConcurrentModificationException when entities are added or removed during iteration. The performance cost is acceptable for the number of entities in this game.

I considered other approaches like synchronized collections or manual iteration controls, but CopyOnWriteArrayList provides the cleanest solution with the least chance of introducing bugs.

## Architecture for Extensions

I designed the system so adding new content is straightforward. New zombie types extend Zombie and implement render(), new plants extend Plant and implement performAction(). The GameManager coordinates everything without needing structural changes.

This modularity was important because the specifications mention that we might need to add more zombie and plant types later. I wanted the architecture to handle that easily.

## Lessons from Implementation

Starting with console logic before adding GUI was the right call. It let me focus on game mechanics without getting distracted by visual details. Many of my classmates struggled because they tried to build everything at once.

Using real-world time instead of frame counting made the game behave consistently across different computers. Frame-based timing would have made zombie spawning inconsistent depending on performance.

The hybrid pixel-grid system for movement and combat gives smooth visuals while maintaining the strategic grid-based gameplay that tower defense requires. Pure pixel collision would be too complex, pure grid movement would look choppy.

Overall, following the specifications exactly while maintaining good software engineering practices taught me a lot about balancing requirements with implementation quality. Every design decision had to be justifiable against both the specs and maintainability concerns.