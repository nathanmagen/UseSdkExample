package com.example.nmagen.usesdkexample.presenters;



/**
 * Created by nmagen on 29/11/2017.
 */

public class PresentersManager {
    private static PresentersManager presentersManager = null;
    private ClientPresenter clientPresenter = new ClientPresenter();
    private  GroupPresenter groupPresenter = null;
    private CallPresenter callPresenter = null;
    private SOSPresenter sosPresenter = null;
    private MessagePresenter messagePresenter = null;

    public static PresentersManager getInstance() {
        if (presentersManager == null) {
            presentersManager = new PresentersManager();
        }
        return presentersManager;
    }
    // Need to initiate the modules only after the client has started and signed in
    public void initModules() {
        groupPresenter = new GroupPresenter();
        callPresenter = new CallPresenter();
        sosPresenter = new SOSPresenter();
        messagePresenter = new MessagePresenter();
    }

    public ClientPresenter getClientPresenter() {
        return clientPresenter;
    }

    public GroupPresenter getGroupPresenter() {
        return groupPresenter;
    }

    public CallPresenter getCallPresenter() {
        return callPresenter;
    }

    public SOSPresenter getSosPresenter() {
        return sosPresenter;
    }

    public MessagePresenter getMessagePresenter() {
        return messagePresenter;
    }
}
