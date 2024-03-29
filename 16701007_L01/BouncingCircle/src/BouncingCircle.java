/***************************************************
 * Copyright 2019 Foysal Hossain                   *
 * ID : 16701007                                   *
 * Department of Computer Science & Engineering,   *
 * University of Chittagong                        *
 **************************************************/
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/** An applet that displays a simple animation */
public class BouncingCircle extends Applet implements Runnable {
  int x = 150, y = 50, r = 50; // Position and radius of the circle

  int dx = 11, dy = 7; // Trajectory of circle

  Thread animator; // The thread that performs the animation

  volatile boolean pleaseStop; // A flag to ask the thread to stop

  /** This method simply draws the circle at its current position */
  public void paint(Graphics g) {
    g.setColor(Color.red);
    g.fillOval(x - r, y - r, r * 2, r * 2);
  }

  /**
   * This method moves (and bounces) the circle and then requests a redraw.
   * The animator thread calls this method periodically.
   */
  public void animate() {
    // Bounce if we've hit an edge.
    Rectangle bounds = getBounds();
    if ((x - r + dx < 0) || (x + r + dx > bounds.width))
      dx = -dx;
    if ((y - r + dy < 0) || (y + r + dy > bounds.height))
      dy = -dy;

    // Move the circle.
    x += dx;
    y += dy;

    // Ask the browser to call our paint() method to draw the circle
    // at its new position.
    repaint();
  }

  /**
   * This method is from the Runnable interface. It is the body of the thread
   * that performs the animation. The thread itself is created and started in
   * the start() method.
   */
  public void run() {
    while (!pleaseStop) { // Loop until we're asked to stop
      animate(); // Update and request redraw
      try {
        Thread.sleep(100);
      } // Wait 100 milliseconds
      catch (InterruptedException e) {
      } // Ignore interruptions
    }
  }

  /** Start animating when the browser starts the applet */
  public void start() {
    animator = new Thread(this); // Create a thread
    pleaseStop = false; // Don't ask it to stop now
    animator.start(); // Start the thread.
    // The thread that called start now returns to its caller.
    // Meanwhile, the new animator thread has called the run() method
  }

  /** Stop animating when the browser stops the applet */
  public void stop() {
    // Set the flag that causes the run() method to end
    pleaseStop = true;
  }
}