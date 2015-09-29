package com.kenoki.app.syncadapter.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by aoki on 29/09/2015.
 */
public class SyncService extends Service {

    // Instancia del sync adapter
    private static SyncAdapter syncAdapter = null;
    // Objeto para prevenir errores entre hilos
    private static final Object lock = new Object();

    @Override
    public void onCreate() {
        synchronized (lock) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    /**
     * Retorna interfaz de comunicación para que el sistema llame al sync adapter
     */
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

}
