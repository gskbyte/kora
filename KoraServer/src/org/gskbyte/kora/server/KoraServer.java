package org.gskbyte.kora.server;

import java.io.File;

import org.gskbyte.kora.server.clients.deviceList.DeviceListServant;
import org.gskbyte.kora.server.clients.events.ClientChangeEventListener;
import org.gskbyte.kora.server.clients.events.ClientQueryEventListener;
import org.gskbyte.kora.server.devices.DeviceManager;
import org.gskbyte.kora.server.util.Log;
import org.ugr.bluerose.devices.TcpCompatibleDevice;

public class KoraServer
{
    public static void main (String [] args)
    {
        TcpCompatibleDevice device = new TcpCompatibleDevice();

        try {
            Log.log("Conectando a BlueRose");
            org.ugr.bluerose.Initializer.initialize(new File("config.xml"));
            org.ugr.bluerose.Initializer.initializeClient(device);
            DeviceListServant sv = new DeviceListServant();
            org.ugr.bluerose.Initializer.initializeServant(sv, device);
            Log.log("Conectado. Servicio iniciado.");
        } catch (Exception ex) {
            Log.log(ex.getMessage());
        }

        org.ugr.bluerose.events.EventHandler.addEventListener(new ClientChangeEventListener());
        org.ugr.bluerose.events.EventHandler.addEventListener(new ClientQueryEventListener());
        Log.log("Conectado. Escuchadores iniciados.");
        

        Log.log("Iniciando gestor de dispositivos.");
        //DeviceManager.instance();
    }
}
