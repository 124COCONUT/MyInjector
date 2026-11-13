package com.example.coconut;

import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.content.Intent;

public class MyVpnService extends VpnService {
    private ParcelFileDescriptor vpnInterface = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (vpnInterface == null) {
            Builder builder = new Builder();
            builder.addAddress("10.0.0.2", 32);
            builder.addRoute("0.0.0.0", 0); 
            builder.setSession("COCONUT VPN");
            
            try {
                vpnInterface = builder.establish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            if (vpnInterface != null) {
                vpnInterface.close();
                vpnInterface = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
