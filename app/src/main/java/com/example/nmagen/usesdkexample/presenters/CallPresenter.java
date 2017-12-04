package com.example.nmagen.usesdkexample.presenters;

import android.util.Log;

import com.MobileTornado.sdk.TornadoClient;
import com.MobileTornado.sdk.model.CallCallbacks;
import com.MobileTornado.sdk.model.CallModule;
import com.MobileTornado.sdk.model.RequestCallback;
import com.MobileTornado.sdk.model.data.CallInfo;
import com.example.nmagen.usesdkexample.activities.MainActivity;
import com.example.nmagen.usesdkexample.data.AppGroup;

/**
 * Created by nmagen on 30/11/2017.
 */

public class CallPresenter {
    private CallModule callModule = TornadoClient.getInstance().getCallModule();
    private boolean isCallSucceeded = true;
    private RequestCallback outgoingCallRequestCallback = new RequestCallback<CallInfo,Integer>() {
        @Override
        public void onSuccess(CallInfo callInfo) {
            Log.d(MainActivity.LOG_TAG, "CallPresenter: call succeeded - " + callInfo);
            isCallSucceeded = true;
        }

        @Override
        public void onError(Integer reason) {
            Log.d(MainActivity.LOG_TAG, "CallPresenter: call failed - " + CallModule.CallError.toString(reason));
            isCallSucceeded = false;
        }
    };

    public boolean callGroup(AppGroup group) {
        callModule.startCall(group.getGroup(), outgoingCallRequestCallback);
        return isCallSucceeded;
    }

    public void setCallCallbacks(CallCallbacks callCallbacks) {
        callModule.addCallCallbacks(callCallbacks);
    }

    public void endCall() {
        callModule.endCall();
    }

    public void startTalking() {
        callModule.startTalking();
    }

    public void stopTalking() {
        callModule.stopTalking();
    }

    public boolean isAbleToStartTalking() {
        return callModule.isAbleToStartTalking();
    }

    public boolean isAbleToEndCall() {
        return callModule.isAbleToEndCall();
    }

    public boolean isCallSucceeded() {
        return  isCallSucceeded;
    }
}
