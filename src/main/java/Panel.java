import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * SpaceInvaders Panel Implementation.
 * 
 * This class sets up the timer, alien ships, and player base. This class also
 * allows the user to interact with the game, as well as handling all the game
 * events.
 * 
 * @author Khalil Daoud
 * @version 12/2/2019
 */
@SuppressWarnings("serial")
public class Panel extends JPanel {

    private boolean mysteryDirection;
    private boolean isGameOver;
    private boolean shiftDirection;
    private boolean aliensMovedLower;
    private Integer score;
    private Integer randInt;
    private Integer mysteryActionCounter;
    private Integer alienActionCounter;
    private Integer alienPulseRate;
    private Timer timer;
    private Random rand;

    private boolean left = false;
    private boolean right = false;

    private Base base;
    private Missile missile;
    private Mystery mystery;
    private ArrayList<ArrayList<Invader>> aliens;
    private ArrayList<Missile> alienMissiles;

    /**
     * Panel constructor takes no parameters and initiates the game panel to its
     * initial state. Handles all game events.
     */
    public Panel() {
        score = 0;
        mysteryActionCounter = 0;
        alienActionCounter = 0;
        alienPulseRate = 40;
        isGameOver = false;
        aliensMovedLower = false;
        shiftDirection = true;
        rand = new Random();
        aliens = new ArrayList<ArrayList<Invader>>();
        alienMissiles = new ArrayList<Missile>();
        createAlienLists();
        populateAliens();

        setFont(new Font("Courier New", Font.BOLD, 20));
        setBackground(Color.BLACK);
        setForeground(Color.GREEN);

        base = new Base(230, 360);
        timer = new Timer(10, e -> {
            // base movement
            if (left) {
                if (base.getX() > 0)
                    base.move(Drawable.Direction.LEFT, 5);
            }
            if (right) {
                if (base.getX() < 460)
                    base.move(Drawable.Direction.RIGHT, 5);
            }
            // Control base missile
            if (missile != null) {
                if (missile.getY() > 0) {
                    missile.move(Drawable.Direction.UP, 5);
                }
                else {
                    base.clipActions("reset");
                    missile = null;
                }
            }
            if (!alienMissiles.isEmpty()) {
                for (int i = 0; i < alienMissiles.size(); i++) {
                    if (base.hitByMissile(alienMissiles.get(i))) {
                        base.setToHit();
                        missile = null;
                        alienMissiles.clear();
                        isGameOver = true;
                    }
                }
            }
            // control mystery ship, moves every 2 pulses, despawn after 40.
            if (mystery != null) {
                mysteryActionCounter++;
                if (missile != null) {
                    if (mystery.hitByMissile(missile)) {
                        missile = null;
                        base.clipActions("reset");
                        mystery.setToHit();
                        increaseScore(mystery.pointValue);
                    }
                }
                if (mysteryDirection && !mystery.isHit) {
                    if (mysteryActionCounter == 2) {
                        mysteryActionCounter = 0;
                        mystery.move(Drawable.Direction.RIGHT, 5);
                    }
                }
                if (!mysteryDirection && !mystery.isHit) {
                    if (mysteryActionCounter == 2) {
                        mysteryActionCounter = 0;
                        mystery.move(Drawable.Direction.LEFT, 5);
                    }
                }
                if (mystery.getX() < 0 || mystery.getX() > 450) {
                    mystery = null;
                }
                if (mystery != null) {
                    if (mysteryActionCounter == 40) {
                        mysteryActionCounter = 0;
                        mystery = null;
                    }
                }
            }
            // control the alien swarm, they move / alternate every 40 pulses,
            // getting faster each time they move down a level.
            // missile moves every 2 pulses.
            alienActionCounter++;
            if (missile != null) {
                aliensAttacked();
            }
            if (aliensMovedLower) {
                calculateAlianCounter();
                aliensMovedLower = false;
            }
            if (alienMissiles.size() < 3) {
                createNewAlienMissile();
            }
            if (alienMissiles.size() >= 1 && (alienActionCounter % 2 == 0)) {
                for (int i = 0; i < alienMissiles.size(); i++) {
                    if (alienMissiles.get(i).getY() < 450) {
                        alienMissiles.get(i).move(Drawable.Direction.DOWN, 5);
                    }
                    else {
                        alienMissiles.remove(i);
                    }
                }
            }
            if (alienActionCounter == alienPulseRate) {
                alienActionCounter = 0;
                removeAliensAttacked();
                moveAliens();
                alternateAliens();

            }
            spawnMysteryHelper();
            repaint();
        });

        timer.start();

        // listener checks from user input (left, right, fire)
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        left = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        if (missile == null) {
                            base.clipActions("play");
                            missile = new Missile((base.getX() + 12),
                                    (base.getY() - 6));
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        left = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        if (missile == null) {
                            base.clipActions("play");
                            missile = new Missile((base.getX() + 12),
                                    (base.getY() - 6));
                        }
                        break;
                }
            }
        });
        setFocusable(true);
    }

    /**
     * Allows creation of 2D objects in the window
     * 
     * @param g
     *            Graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gamePanel = (Graphics2D) g;

        base.draw(gamePanel);
        score(gamePanel);
        drawAliens(gamePanel);
        if (isGameOver) {
            base.isHit = true;
            base.draw(gamePanel);
            gameOver(gamePanel);
        }
        if (missile != null) {
            missile.draw(gamePanel);
        }
        if (!alienMissiles.isEmpty()) {
            for (int i = 0; i < alienMissiles.size(); i++) {
                alienMissiles.get(i).draw(gamePanel);
            }
        }
        if (mystery != null) {
            mystery.draw(gamePanel);
        }
        if (randInt > 999 && mystery == null) {
            spawnMystery();
            mystery.clipActions("play");
        }
    }

    /**
     * Resets the game panel to its initial state
     */
    public void reset() {
        timer.stop();
        this.score = 0;
        this.alienPulseRate = 40;
        this.mysteryActionCounter = 0;
        this.alienActionCounter = 0;
        this.isGameOver = false;
        this.aliensMovedLower = false;
        this.missile = null;
        this.base.isHit = false;
        this.base.clipActions("pause");
        this.base.clipActions("reset");
        if (mystery != null) {
            this.mystery.clipActions("pause");
            this.mystery.clipActions("reset");
        }
        this.base = new Base(230, 360);
        this.mystery = null;
        this.aliens.clear();
        createAlienLists();
        populateAliens();
        timer.start();
    }

    /**
     * Stops the timer, should be called whenever program terminates
     */
    public void stopTimer() {
        this.base.clipActions("pause");
        if (mystery != null)
            mystery.clipActions("pause");
        timer.stop();
    }

    /**
     * Starts the timer
     */
    public void startTimer() {
        if (this.base.getClipPosition() != 0)
            this.base.clipActions("play");
        if (mystery != null)
            mystery.clipActions("play");
        timer.start();
    }

    /**
     * Draws the score
     * 
     * @param g
     *            Graphics object to be passed in
     */
    public void score(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        String scoreString = "Score: " + score.toString();

        switch (scoreString.length()) {
            case 8:
                g2.drawString(scoreString, 385, 18);
                break;
            case 9:
                g2.drawString(scoreString, 375, 18);
                break;
            case 10:
                g2.drawString(scoreString, 365, 18);
                break;
            case 11:
                g2.drawString(scoreString, 353, 18);
                break;
            case 12:
                g2.drawString(scoreString, 343, 18);
                break;
        }
        // 1 score x = 385
        // 10 score x = 375
        // 100 score x = 365
        // 1000 score x = 353?
    }

    /**
     * Helper function to increase the score by n
     * 
     * @param n
     *            the number of paints to be added
     */
    public void increaseScore(int n) {
        this.score = score + n;
    }

    /**
     * draws the game over screen and pauses the game
     * 
     * @param g
     *            the graphics object to be passed in
     */
    public void gameOver(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Courier New", Font.BOLD, 50));
        g2.drawString("GAME OVER", 105, 185);
        stopTimer();
    }

    /**
     * Generates a random number to help determine when to spawn the mystery
     * ship.
     */
    private void spawnMysteryHelper() {
        randInt = rand.nextInt(1001);
    }

    /**
     * Determines where to start the mystery ship based on a random number.
     */
    private void spawnMystery() {
        spawnMysteryHelper();
        if (randInt < 500) {
            mysteryDirection = true;
            mystery = new Mystery(0, 50);
        }
        else {
            mysteryDirection = false;
            mystery = new Mystery(450, 50);
        }
    }

    /**
     * Populate the alien array with top, middle, and bottom aliens. When aliens
     * are populated they spawn at the default center position, aliens will wait
     * to populate until there are no other objects other than the base on
     * screen.
     */
    private void populateAliens() {

        int xGap;

        for (int y = 0; y < 5; y++) {
            xGap = 0;
            for (int x = 0; x < 10; x++) {
                switch (y) {
                    case 0:
                        aliens.get(y).add(new InvaderTop((60 + xGap), 80));
                        xGap = xGap + 35;
                        break;
                    case 1:
                        aliens.get(y).add(new InvaderMiddle((60 + xGap), 105));
                        xGap = xGap + 35;
                        break;
                    case 2:
                        aliens.get(y).add(new InvaderMiddle((60 + xGap), 130));
                        xGap = xGap + 35;
                        break;
                    case 3:
                        aliens.get(y).add(new InvaderBottom((60 + xGap), 155));
                        xGap = xGap + 35;
                        break;
                    case 4:
                        aliens.get(y).add(new InvaderBottom((60 + xGap), 185));
                        xGap = xGap + 35;
                        break;
                }
            }
        }
    }

    /**
     * Draw all non-null aliens in the array
     * 
     * @param g
     *            - graphics object to be passed in
     */
    private void drawAliens(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int y = 0; y < aliens.size(); y++) {
            for (int x = 0; x < aliens.get(y).size(); x++) {
                if (aliens.get(y).get(x) != null) {
                    aliens.get(y).get(x).draw(g2);
                }
            }
        }
    }

    /**
     * Creates all sub-arrayLists within the aliens arraylist. Each row
     * corresponds with the alien that must spawn, with row 1 being the top
     * alien row.
     */
    private void createAlienLists() {
        ArrayList<Invader> row1 = new ArrayList<Invader>();
        ArrayList<Invader> row2 = new ArrayList<Invader>();
        ArrayList<Invader> row3 = new ArrayList<Invader>();
        ArrayList<Invader> row4 = new ArrayList<Invader>();
        ArrayList<Invader> row5 = new ArrayList<Invader>();
        aliens.add(row1);
        aliens.add(row2);
        aliens.add(row3);
        aliens.add(row4);
        aliens.add(row5);
    }

    /**
     * changes the alteration state for all aliens
     */
    private void alternateAliens() {
        for (int y = 0; y < aliens.size(); y++) {
            for (int x = 0; x < aliens.get(y).size(); x++) {
                if (aliens.get(y).get(x) != null) {
                    aliens.get(y).get(x).setAlteration();
                }
            }
        }
    }

    /**
     * Checks if an alien was attacked.
     */
    private void aliensAttacked() {
        for (int y = 0; y < aliens.size(); y++) {
            for (int x = 0; x < aliens.get(y).size(); x++) {
                if (aliens.get(y).get(x) != null && missile != null) {
                    if (aliens.get(y).get(x).hitByMissile(missile)) {
                        aliens.get(y).get(x).setToHit();
                        increaseScore(aliens.get(y).get(x).pointValue);
                        missile = null;
                        break;
                    }
                }
            }
        }

    }

    /**
     * Removed all hit aliens
     */
    private void removeAliensAttacked() {
        for (int y = 0; y < aliens.size(); y++) {
            for (int x = 0; x < aliens.get(y).size(); x++) {
                if (aliens.get(y).get(x).isHit) {
                    aliens.get(y).remove(x);
                }
            }
        }
    }

    /**
     * Move the aliens down if they are touching either of the borders, if they
     * are not, shift them left or right depending on which border they last
     * touched, if all aliens are dead, then re-populate.
     */
    private void moveAliens() {
        String action = "";
        int emptyRows = 0;

        for (int y = 0; y < aliens.size(); y++) {
            if (aliens.get(y).isEmpty()) {
                emptyRows++;
            }
            if (emptyRows == 5) {
                if (mystery == null && missile == null && alienMissiles.isEmpty())
                populateAliens();
            }
        }

        for (int y = 0; y < aliens.size(); y++) {
            if (!aliens.get(y).isEmpty() && aliens.get(y)
                    .get(aliens.get(y).size() - 1).getY() >= 338) {
                isGameOver = true;
                break;
            }
            if (!aliens.get(y).isEmpty() && aliens.get(y).get(aliens.get(y).size() - 1).getX() >= 450
                    && shiftDirection) {
                action = "shiftDown";
                break;
            }
            if (!aliens.get(y).isEmpty() && aliens.get(y).get(0).getX() <= 0 && !shiftDirection) {
                action = "shiftDown";
                break;
            }
            else
                action = "shiftOver";
        }

        switch (action) {
            case "shiftDown":
                for (int y = 0; y < aliens.size(); y++) {
                    for (int x = 0; x < aliens.get(y).size(); x++) {
                        aliens.get(y).get(x).move(Drawable.Direction.DOWN, 12);
                    }
                }
                aliensMovedLower = true;
                shiftDirection = !shiftDirection;
                break;
            case "shiftOver":
                for (int y = 0; y < aliens.size(); y++) {
                    for (int x = 0; x < aliens.get(y).size(); x++) {
                        if (shiftDirection)
                            aliens.get(y).get(x).move(Drawable.Direction.RIGHT,
                                    5);
                        if (!shiftDirection)
                            aliens.get(y).get(x).move(Drawable.Direction.LEFT,
                                    5);
                    }
                }
        }
    }

    /**
     * calculated the new pulse rate for the alien waves whenever they move a
     * level lower.
     */
    private void calculateAlianCounter() {
        alienPulseRate = (int) Math.ceil((alienPulseRate * 0.8));
    }

    /**
     * creates new missiles when there are less than 3 on screen. uses a random
     * number to determines when one will be fired by an alien ship.
     */
    private void createNewAlienMissile() {
        randInt = rand.nextInt(101);
        if (randInt > 20) {
            Invader temp = findRandomAlien();
            if (temp != null)
                alienMissiles
                        .add(new Missile(temp.getX() + 13, temp.getY() + 8));
        }
    }

    /**
     * Helper function for createNewAlienMissile() that finds an eligible alien
     * to fire off a missile.
     */
    private Invader findRandomAlien() {
        int randY = 0;
        int randX = 0;
        boolean found = false;

        while (!found) {
            randY = rand.nextInt(aliens.size());

            if (aliens.get(randY).isEmpty()) {
                return null;
            }
            randX = rand.nextInt(aliens.get(randY).size());

            if (alienIsInFront(randY, randX)) {
                found = true;
            }
        }
        return aliens.get(randY).get(randX);
    }

    /**
     * Determines if the alien chosen can fire off a missile. Alien must not be
     * obstructed by another alien to fire a missile.
     * 
     * @param temp
     *            alien to check
     * @return true if missile can be fired, false otherwise.
     */
    private boolean alienIsInFront(int y, int x) {
        int yPos = y;
        while (yPos < aliens.size() - 1) {
            yPos++;
            for (int i = 0; i < (aliens.get(yPos).size()); i++)
                if (aliens.get(yPos).get(i).getX() == aliens.get(y).get(x)
                        .getX()) {
                    return false;
                }
        }
        return true;
    }
}
