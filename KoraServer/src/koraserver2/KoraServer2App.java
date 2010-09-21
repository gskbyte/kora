/*
 * KoraServer2App.java
 */

package koraserver2;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class KoraServer2App extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new KoraServer2View(this));
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
     * @return the instance of KoraServer2App
     */
    public static KoraServer2App getApplication() {
        return Application.getInstance(KoraServer2App.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(KoraServer2App.class, args);
    }
}
