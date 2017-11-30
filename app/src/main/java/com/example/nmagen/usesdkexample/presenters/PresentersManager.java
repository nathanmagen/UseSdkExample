package com.example.nmagen.usesdkexample.presenters;


/**
 * Created by nmagen on 29/11/2017.
 */

public class PresentersManager {
    private static PresentersManager presentersManager = null;
    private ClientPresenter clientPresenter = new ClientPresenter();
    private  GroupPresenter groupPresenter = null;

    public static PresentersManager getInstance() {
        if (presentersManager == null) {
            presentersManager = new PresentersManager();
        }
        return presentersManager;
    }
    // Need to initiate the modules only after the client has started and signed in
    public void initModules() {
        groupPresenter = new GroupPresenter();
    }

    public ClientPresenter getClientPresenter() {
        return clientPresenter;
    }

    public GroupPresenter getGroupPresenter() {
        return groupPresenter;
    }
}
