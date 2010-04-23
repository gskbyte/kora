/*
 * SimKora.java
 */

package simkora;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class SimKora extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new SimKoraView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SimKora
     */
    public static SimKora getApplication() {
        return Application.getInstance(SimKora.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {

        org.ugr.bluerose.devices.TcpCompatibleDevice device = new org.ugr.bluerose.devices.TcpCompatibleDevice();

        try {
            org.ugr.bluerose.Initializer.initialize(new FileInputStream("config.xml"));
            org.ugr.bluerose.Initializer.initializeClient(device);
            launch(SimKora.class, args);
        } catch (Exception ex) {
            Logger.getLogger(SimKora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
