import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

/**
 * The Abstract class containing the common functionality of invaders and base
 * 
 * This class defines whether a ship has been hit by a missile, one tracking an
 * audio clip with the sound the ship makes when hit, and a third referring to
 * the image when it is hit. It has a method that determines if the ship has
 * been hit by a given missile, and another that changes the state of the ship
 * to hit. All ships use the same sound ("aud_hit.wav").
 * 
 * @author Kenneth Sheppard
 * @version 11-24-19
 */
public abstract class Ship extends Drawable {

    /** isHit is TODO write a brief description. */
    protected boolean isHit = false;
    private Clip hitSound = getAudioClip("aud_hit.wav");
    // TODO Store image when hit here
    /** hitImage is TODO write a brief description. */
    protected Image hitImage;

    /**
     * Create a new Ship object.
     * 
     * @param x
     *            the X position of the ship
     * @param y
     *            the Y position of the ship
     */
    public Ship(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * This method determines if a missile has struck the ship
     * 
     * @param m
     *            The missile to be checked
     * @return True if the missile has hit, false otherwise
     */
    public boolean hitByMissile(Missile m) {
        // Starting by checking x value
        if (getX() <= m.getX()
                && getX() + getWidth() >= m.getX() + m.getWidth()) {
            // Now check the y value
            if ((getY() < m.getY() && getY() + getHeight() > m.getY())
                    || (getY() < m.getY() + m.getHeight() && getY()
                            + getHeight() > m.getY() + m.getHeight()))
                return true;
        }

        return false;
    }

    /**
     * setToHit sets the state of hit to true and plays the corresponding sound
     */
    public void setToHit() {
        isHit = true;
        hitSound.setFramePosition(0);
        hitSound.start();
    }

    /**
     * getImage allows the getting of an Image to be used by subclasses of this
     * object
     * 
     * @param filename
     *            the location of the image to get
     * @return the Image
     */
    protected Image getImage(String filename) {
        URL url = getClass().getResource(filename);
        ImageIcon icon = new ImageIcon(url);
        return icon.getImage();
    }

    /**
     * getAudioClip will return the clip of the audio passed into the method
     * 
     * @param filename
     *            the location of the audio file
     * @return the Clip of audio
     */
    protected Clip getAudioClip(String filename) {
        Clip clip = null;
        try {
            InputStream in = getClass().getResourceAsStream(filename);
            InputStream buf = new BufferedInputStream(in);
            AudioInputStream stream = AudioSystem.getAudioInputStream(buf);
            clip = AudioSystem.getClip();
            clip.open(stream);
        }
        catch (UnsupportedAudioFileException | IOException
                | LineUnavailableException e) {
            e.printStackTrace();
        }
        return clip;
    }
    
}
