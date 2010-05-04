/*
 * SimKoraView.java
 */

package simkora;

import java.awt.Button;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;

import org.ugr.bluerose.events.Event;
import org.ugr.bluerose.events.Value;
import simkora.device.DeviceChangeListener;
import simkora.device.DeviceListServant;
import simkora.device.DeviceManager;
import simkora.device.DeviceQueryListener;

/**
 * The application's main frame.
 */
public class SimKoraView extends FrameView {

    public static final int TOPIC = 49;
    public static SimKoraView currentWindow;

    public SimKoraView(SingleFrameApplication app) {
        super(app);
        currentWindow = this;

        if (new String("javax.swing.plaf.metal.MetalLookAndFeel").equals(UIManager.getSystemLookAndFeelClassName())) {
            try {
                // Intentar aplicar Gtk+
                System.out.print("Aplicando tema Gtk+... ");
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                System.out.print("Tema aplicado correctamente.\n");
            } catch (Exception ex) {
                Logger.getLogger(SimKoraView.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                // Aplicar tema nativo del sistema
                System.out.print("Aplicando tema nativo del sistema... ");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.out.print("Tema aplicado correctamente.\n");
            } catch (Exception ex) {
                Logger.getLogger(SimKoraView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        initComponents();
        logPane.setBackground(Color.WHITE);

        org.ugr.bluerose.devices.TcpCompatibleDevice device = new org.ugr.bluerose.devices.TcpCompatibleDevice();

        try {
            log("Conectando a BlueRose");
            org.ugr.bluerose.Initializer.initialize(new File("config.xml"));
            org.ugr.bluerose.Initializer.initializeClient(device);
            DeviceListServant sv = new DeviceListServant();
            org.ugr.bluerose.Initializer.initializeServant(sv, device);
            log("Conectado. Servicio iniciado.");
        } catch (Exception ex) {
            log(ex.getMessage());
        }

        org.ugr.bluerose.events.EventHandler.addEventListener(new DeviceQueryListener());
        org.ugr.bluerose.events.EventHandler.addEventListener(new DeviceChangeListener());
    }

    public static SimKoraView getCurrent()
    {
        return currentWindow;
    }

    public static void log(String s)
    {
        currentWindow.appendLog(s);
    }

    public void appendLog(String s)
    {
        logPane.setText( logPane.getText()+"\n"+s);
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SimKora.getApplication().getMainFrame();
            aboutBox = new SimKoraAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SimKora.getApplication().show(aboutBox);
    }

    public void setLight1(boolean on, boolean publish)
    {
        String text, filename;
        URL url;
        if(on){
            text = "Apagar";
            filename = "bombilla100.png";
        } else {
            text = "Encender";
            filename = "bombilla0.png";
        }

        filename = resourceMap.getResourcesDir() + filename;
        light1Button.setText(text);
        url = resourceMap.getClassLoader().getResource(filename);
        light1Icon.setIcon(new ImageIcon(url));

        Value v = new Value();
        v.setBoolean(on);
        DeviceManager.getDevice("bombilla1").setValue(v);
        if(publish){
            Event e = new Event(TOPIC);

            Value deviceName = new Value(),
                    deviceValue = new Value();

            deviceName.setString("bombilla1");

            deviceValue.setBoolean(on);

            e.setMember("name", deviceName);
            e.setMember("value", deviceValue);

            org.ugr.bluerose.events.EventHandler.publish(e, false);
        } else {
            light1Button.getActionListeners();
            log("Evento sobre bombilla1 (bombilla del salón). Nuevo valor: " + (on ? "Encendida" : "Apagada"));
        }
    }

    public void setLight2(float value, boolean publish)
    {
        String filename;
        URL url;

        if(value<=0){
            filename = "bombilla0.png";
        } else if(value<=0.25) {
            filename = "bombilla25.png";
        } else if(value<=0.5) {
            filename = "bombilla50.png";
        } else if(value<=0.75) {
            filename = "bombilla75.png";
        } else {
            filename = "bombilla100.png";
        }

        filename = resourceMap.getResourcesDir() + filename;
        url = resourceMap.getClassLoader().getResource(filename);
        light2Icon.setIcon(new ImageIcon(url));

        Value v = new Value();
        v.setFloat(value);
        DeviceManager.getDevice("bombilla2").setValue(v);
        if(publish){
            Event e = new Event(TOPIC);

            Value deviceName = new Value(),
                  deviceValue = new Value();

            deviceName.setString("bombilla2");
            deviceValue.setFloat(value);

            e.setMember("name", deviceName);
            e.setMember("value", deviceValue);

            org.ugr.bluerose.events.EventHandler.publish(e, false);
        } else {
            ChangeListener[] ls =light2Slider.getChangeListeners();
            light2Slider.removeChangeListener(ls[0]);
            light2Slider.setValue((int)(value*100f));
            light2Slider.addChangeListener(ls[0]);
            log("Evento sobre bombilla2 (flexo). Nuevo valor: " + value);
        }
        
    }

    public void setSunblind(float value, boolean publish)
    {
        String filename;
        URL url;

        if(value<=0){
            filename = "persiana0.png";
        } else if(value<=0.25) {
            filename = "persiana1.png";
        } else if(value<=0.5) {
            filename = "persiana2.png";
        } else if(value<=0.75) {
            filename = "persiana3.png";
        } else {
            filename = "persiana4.png";
        }

        filename = resourceMap.getResourcesDir() + filename;
        url = resourceMap.getClassLoader().getResource(filename);
        sunblindIcon.setIcon(new ImageIcon(url));

        Value v = new Value();
        v.setFloat(value);
        DeviceManager.getDevice("persiana1").setValue(v);

        if(publish){
            Event e = new Event(TOPIC);

            Value deviceName = new Value(),
                    deviceValue = new Value();

            deviceName.setString("persiana1");
            deviceValue.setFloat(value);

            e.setMember("name", deviceName);
            e.setMember("value", deviceValue);

            org.ugr.bluerose.events.EventHandler.publish(e, false);
        } else {
            ChangeListener[] ls =sunblindSlider.getChangeListeners();
            sunblindSlider.removeChangeListener(ls[0]);
            sunblindSlider.setValue((int)(value*100f));
            sunblindSlider.addChangeListener(ls[0]);
            log("Evento sobre persiana (persiana). Nuevo valor: " + value);
        }

    }

    public void setDoor1(boolean open, boolean publish)
    {
        String text, filename;
        URL url;
        if(open){
            text = "Cerrar";
            filename = "puerta1.png";
        } else {
            text = "Abrir";
            filename = "puerta0.png";
        }

        filename = resourceMap.getResourcesDir() + filename;
        door1Button.setText(text);
        url = resourceMap.getClassLoader().getResource(filename);
        door1Icon.setIcon(new ImageIcon(url));

        Value v = new Value();
        v.setBoolean(open);
        DeviceManager.getDevice("puerta1").setValue(v);
        if(publish){
            Event e = new Event(TOPIC);

            Value deviceName = new Value(),
                    deviceValue = new Value();

            deviceName.setString("puerta1");

            deviceValue.setBoolean(open);

            e.setMember("name", deviceName);
            e.setMember("value", deviceValue);

            org.ugr.bluerose.events.EventHandler.publish(e, false);
        } else {
            log("Evento sobre puerta (Puerta 1). Nuevo valor: " + (open ? "Abierta" : "Cerrada"));
        }

    }

    public void setDoor2(boolean open, boolean publish)
    {
        String text, filename;
        URL url;
        if(open){
            text = "Cerrar";
            filename = "puerta1.png";
        } else {
            text = "Abrir";
            filename = "puerta0.png";
        }

        filename = resourceMap.getResourcesDir() + filename;
        door2Button.setText(text);
        url = resourceMap.getClassLoader().getResource(filename);
        door2Icon.setIcon(new ImageIcon(url));

        Value v = new Value();
        v.setBoolean(open);
        DeviceManager.getDevice("puerta2").setValue(v);
        if(publish){
            Event e = new Event(TOPIC);

            Value deviceName = new Value(),
                    deviceValue = new Value();

            deviceName.setString("puerta2");

            deviceValue.setBoolean(open);

            e.setMember("name", deviceName);
            e.setMember("value", deviceValue);

            org.ugr.bluerose.events.EventHandler.publish(e, false);
        } else {
            log("Evento sobre puerta2 (Puerta 2). Nuevo valor: " + (open ? "Abierta" : "Cerrada"));
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        light1Icon = new javax.swing.JLabel();
        light1Button = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        light2Icon = new javax.swing.JLabel();
        light2Slider = new javax.swing.JSlider();
        jLabel7 = new javax.swing.JLabel();
        sunblindSlider = new javax.swing.JSlider();
        sunblindIcon = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        door1Icon = new javax.swing.JLabel();
        door1Button = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logPane = new javax.swing.JTextPane();
        cleanLogButton = new javax.swing.JButton();
        door2Icon = new javax.swing.JLabel();
        door2Button = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(simkora.SimKora.class).getContext().getResourceMap(SimKoraView.class);
        light1Icon.setIcon(resourceMap.getIcon("light1Icon.icon")); // NOI18N
        light1Icon.setName("light1Icon"); // NOI18N

        light1Button.setText(resourceMap.getString("light1Button.text")); // NOI18N
        light1Button.setName("light1Button"); // NOI18N
        light1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                light1ButtonActionPerformed(evt);
            }
        });

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        light2Icon.setIcon(resourceMap.getIcon("light2Icon.icon")); // NOI18N
        light2Icon.setName("light2Icon"); // NOI18N

        light2Slider.setValue(0);
        light2Slider.setName("light2Slider"); // NOI18N
        light2Slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                light2SliderStateChanged(evt);
            }
        });

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        sunblindSlider.setValue(0);
        sunblindSlider.setName("sunblindSlider"); // NOI18N
        sunblindSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sunblindSliderStateChanged(evt);
            }
        });

        sunblindIcon.setIcon(resourceMap.getIcon("sunblindIcon.icon")); // NOI18N
        sunblindIcon.setName("sunblindIcon"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        door1Icon.setIcon(resourceMap.getIcon("door1Icon.icon")); // NOI18N
        door1Icon.setName("door1Icon"); // NOI18N

        door1Button.setText(resourceMap.getString("door1Button.text")); // NOI18N
        door1Button.setName("door1Button"); // NOI18N
        door1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                door1ButtonActionPerformed(evt);
            }
        });

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        logPane.setEditable(false);
        logPane.setName("logPane"); // NOI18N
        jScrollPane1.setViewportView(logPane);

        cleanLogButton.setText(resourceMap.getString("cleanLogButton.text")); // NOI18N
        cleanLogButton.setName("cleanLogButton"); // NOI18N
        cleanLogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanLogButtonActionPerformed(evt);
            }
        });

        door2Icon.setIcon(resourceMap.getIcon("door2Icon.icon")); // NOI18N
        door2Icon.setText(resourceMap.getString("door2Icon.text")); // NOI18N
        door2Icon.setToolTipText(resourceMap.getString("door2Icon.toolTipText")); // NOI18N
        door2Icon.setName("door2Icon"); // NOI18N

        door2Button.setText(resourceMap.getString("door2Button.text")); // NOI18N
        door2Button.setName("door2Button"); // NOI18N
        door2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                door2ButtonActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 927, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(light1Button, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(light1Icon)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(light2Slider, 0, 0, Short.MAX_VALUE)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7)
                                .addComponent(light2Icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sunblindSlider, 0, 0, Short.MAX_VALUE)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel8)
                                .addComponent(sunblindIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9)
                            .addComponent(door1Icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(door1Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(door2Icon, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(door2Button, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))
                    .addComponent(cleanLogButton))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sunblindIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(light2Icon, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(light1Icon, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(door2Icon, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                            .addComponent(door1Icon, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(door1Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(door2Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(light1Button, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(light2Slider, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(sunblindSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(cleanLogButton)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(simkora.SimKora.class).getContext().getActionMap(SimKoraView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void light1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_light1ButtonActionPerformed
        JButton btn = (JButton) evt.getSource();

        if(btn.getText().equals("Encender")){
            setLight1(true, true);
        } else {
            setLight1(false, true);
        }
    }//GEN-LAST:event_light1ButtonActionPerformed

    private void light2SliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_light2SliderStateChanged
        JSlider s = (JSlider)evt.getSource();
        float value = s.getValue()/100f;

        setLight2(value, true);
    }//GEN-LAST:event_light2SliderStateChanged

    private void sunblindSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sunblindSliderStateChanged
        JSlider s = (JSlider)evt.getSource();
        float value = s.getValue()/100f;

        setSunblind(value, true);
    }//GEN-LAST:event_sunblindSliderStateChanged

    private void door1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_door1ButtonActionPerformed
        JButton btn = (JButton) evt.getSource();

        if(btn.getText().equals("Abrir")){
            setDoor1(true, true);
        } else {
            setDoor1(false, true);
        }
    }//GEN-LAST:event_door1ButtonActionPerformed

    private void door2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_door2ButtonActionPerformed
        JButton btn = (JButton) evt.getSource();

        if(btn.getText().equals("Abrir")){
            setDoor2(true, true);
        } else {
            setDoor2(false, true);
        }
    }//GEN-LAST:event_door2ButtonActionPerformed

    private void cleanLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanLogButtonActionPerformed
        logPane.setText("");
    }//GEN-LAST:event_cleanLogButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cleanLogButton;
    private javax.swing.JButton door1Button;
    private javax.swing.JLabel door1Icon;
    private javax.swing.JButton door2Button;
    private javax.swing.JLabel door2Icon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton light1Button;
    private javax.swing.JLabel light1Icon;
    private javax.swing.JLabel light2Icon;
    private javax.swing.JSlider light2Slider;
    private javax.swing.JTextPane logPane;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel sunblindIcon;
    private javax.swing.JSlider sunblindSlider;
    // End of variables declaration//GEN-END:variables

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(simkora.SimKora.class).getContext().getResourceMap(SimKoraView.class);
    private JDialog aboutBox;
}
