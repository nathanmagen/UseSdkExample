package com.example.nmagen.usesdkexample.presenters;

import com.MobileTornado.sdk.TornadoClient;
import com.MobileTornado.sdk.model.SOSModule;

/**
 * Created by nmagen on 04/12/2017.
 */

public class SOSPresenter {
    private SOSModule sosModule = TornadoClient.getInstance().getSOSModule();
    private static final String sosMessage = "Test SOS from Nathan";
    private static final int sosDuration = 2;

    public boolean isAvailable() {
        return sosModule.isAvailable();
    }

    // sends an SOS with the defined sos message, duration and high priority
    public void sendRegularSOS() {
        sosModule.sendSOS(SOSModule.Priority.SOS_PRIORITY_HIGH, sosDuration, sosMessage);
    }

    public void sendSOS(int priority, int duration, String msg) {
        sosModule.sendSOS(priority, duration, msg);
    }

    public boolean isSOSTypeDispatcher() {
        return (sosModule.getSOSType() == SOSModule.SOSType.DISPATCHER);
    }
}
