package com.example.nmagen.usesdkexample.presenters;


/**
 * Created by nmagen on 29/11/2017.
 */

public class PresentersManager {
    private static PresentersManager presentersManager = null;
    private ClientPresenter clientPresenter = new ClientPresenter();
    private  GroupPresenter groupPresenter = new GroupPresenter();

    public static PresentersManager getInstance() {
        if (presentersManager == null) {
            presentersManager = new PresentersManager();
        }
        return presentersManager;
    }

    public ClientPresenter getClientPresenter() {
        return clientPresenter;
    }

    public GroupPresenter getGroupPresenter() {
        return groupPresenter;
    }
}
