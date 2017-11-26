package com.example.nmagen.usesdkexample;

import android.content.Context;
import android.util.Log;

import com.MobileTornado.sdk.TornadoClient;
import com.MobileTornado.sdk.TornadoConfig;
import com.MobileTornado.sdk.model.Client;
import com.MobileTornado.sdk.model.ClientCallbacks;
import com.MobileTornado.sdk.model.data.UserState;

/**
 * Created by nmagen on 26/11/2017.
 */

class ClientPresenter {
    private static final String LOG_TAG = "Presenter_NatesLog";
    private static final String START_MSG = "Client started";
    private static final String SIGNED_IN_MSG = "Signed in";
    private static final String SIGNED_OUT_MSG = "Signed out";
    private static final String END_MSG  = "Client stopped";

    private Client client =  TornadoClient.getInstance();

    void start(Context context) {
        client.start(new TornadoConfig.Builder(context, clientCallbacks).build());
    }

    void stop() {
        client.stop();
    }

    void signIn(String userName, String password, String serverId) {
        client.signIn(userName, password, serverId, UserState.ONLINE);
    }

    Boolean isSignedIn() {
        return client.isSignedIn();
    }

    private ClientCallbacks clientCallbacks = new ClientCallbacks() {
        @Override
        public void onStarted() {
            Log.d(LOG_TAG, START_MSG);
        }

        @Override
        public void onStopped() {
            Log.d(LOG_TAG, END_MSG);
        }

        @Override
        public void onStateChanged(int state) {
            if (state == Client.State.ERROR_NO_NETWORK) {
                Log.d(LOG_TAG, "No network");
            }
            if (state == Client.State.ERROR_NO_SERVICE) {
                Log.d(LOG_TAG, "No service");
            }
        }

        @Override
        public void onSignedIn() {
            Log.d(LOG_TAG, SIGNED_IN_MSG);
        }

        @Override
        public void onSignedOut(boolean wasSignedOutByUser) {
            Log.d(LOG_TAG, SIGNED_OUT_MSG);
        }

        @Override
        public void onCoreError(int error) {
            String errMsg = Client.CoreError.toString(error);
            Log.d(LOG_TAG, "Core Error: " + errMsg);
        }

        @Override
        public void onDenyError(int error) {
            String errMsg = Client.DenyError.toString(error);
            Log.d(LOG_TAG, "Deny Error: " + errMsg);
        }

        @Override
        public void onStartDormant() {

        }

        @Override
        public void onStopDormant() {

        }
    };


}
