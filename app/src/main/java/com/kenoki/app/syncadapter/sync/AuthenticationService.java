package com.kenoki.app.syncadapter.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by aoki on 29/09/2015.
 */
public class AuthenticationService extends Service {

    private ExpenseAuthenticator authenticator;

    @Override
    public void onCreate() {
        authenticator = new ExpenseAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
