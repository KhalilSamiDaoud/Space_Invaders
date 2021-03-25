import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

/**
 * SpaceInvaders JFrame Implementation.
 * 
 * This class sets up the tool-bar, window properties, and will let the panel
 * know when pausing needs to take place. It also handles all close operations.
 * 
 * @author Khalil Daoud
 * @version 12/2/2019
 */
@SuppressWarnings("serial")
public class SpaceInvaders extends JFrame {

    private static final String WINDOW_IMG = "img_invaderbottomA.gif";
    private static Panel gamePanel = new Panel();

    /**
     * SpaceInvaders constructor takes no parameters, and sets all the default
     * JFrame properties for the game.
     */
    public SpaceInvaders() {
        super("Space Invaders");
        setSize(500, 450);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // this just sets a window icon using one of the Gifs, thought it would
        // be fun.
        try {
            setIconImage(ImageIO.read(
                    SpaceInvaders.class.getResourceAsStream(WINDOW_IMG)));
        }
        catch (IOException ex) {
        }

        this.buildTaskbar();
        setVisible(true);

    }

    /**
     * This method builds the task-bar, along with their respective functions.
     * layout is explained below.
     */
    public void buildTaskbar() {
        // create and set the task-bar
        JMenuBar taskBar = new JMenuBar();
        this.setJMenuBar(taskBar);

        // create the 2 menus on the task bar
        JMenu game = new JMenu("Game");
        JMenu help = new JMenu("Help");

        // create the menu items under the "game" menu, with separators
        JMenuItem newGame = game.add("New Game");
        game.add(new JSeparator());
        JMenuItem pause = game.add("Pause");
        JMenuItem resume = game.add("Resume");
        game.add(new JSeparator());
        JMenuItem exit = game.add("Exit");

        // creates confirm dialog if user hits "new game"
        newGame.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Start a new game?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                gamePanel.reset();
            }
        });
        // add functionality to "game" menu items (placeholder functionality)
        pause.addActionListener(e -> gamePanel.stopTimer());
        resume.addActionListener(e -> gamePanel.startTimer());

        // creates confirmation dialog if user hits "exit"
        exit.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Dare To Quit?",
                    "Confirm", 0);
            if (result == JOptionPane.YES_OPTION) {
                gamePanel.stopTimer();
                dispose();
            }
        });

        // create the menu item under the "help" menu, with separators
        JMenuItem about = help.add("About...");

        // creates message dialog if user hits "about"
        about.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Space Invaders\n By: Khalil Daoud & Kenneth Sheppard",
                    "About...", 1);
        });

        // add both the menus to the task bar
        taskBar.add(game);
        taskBar.add(help);
    }

    /**
     * The main method will handle all user actions relating to the JFrame, and
     * all the menu options with in tool-bar. It will tell the JPanel when to
     * pause, resume, and reset.
     * 
     * @param args
     */
    public static void main(String[] args) {

        // create a SpaceInvaders object to display
        SpaceInvaders x = new SpaceInvaders();

        // show dialog here as well, when "X" is pressed on the window.
        x.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                int result = JOptionPane.showConfirmDialog(x, "Dare To Quit?",
                        "Confirm", 0);
                if (result == JOptionPane.YES_OPTION) {
                    gamePanel.stopTimer();
                    x.dispose();
                }
            }
        });

        x.add(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.grabFocus();

    }

}