package com.jd.living.server;



import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.List;

import com.jd.living.model.Result;

@EBean(scope = EBean.Scope.Singleton)
public class BooliServer {

    @RestService
    BooliClient restClient;

    private List<ServerConnectionListener> serverConnectionListeners;

    public interface ServerConnectionListener {
        void onResponse(ListingsDatabase.ActionCode action, Result result);
    }

    @AfterInject
    public void init(){
        serverConnectionListeners = new ArrayList<ServerConnectionListener>();
    }

    public void addServerConnectionListener(ServerConnectionListener connectionListener){
        serverConnectionListeners.add(connectionListener);
    }

    @Background
    public void getListings(String search) {

        AuthStore authStore = new AuthStore();

        Result result = restClient.getListings(search,
                authStore.getCallerId(),
                authStore.getTime(),
                authStore.getUnique(),
                authStore.getHash()).getBody();

        notifyListeners(ListingsDatabase.ActionCode.LISTINGS, result);
    }

    @Background
    public void getListing(String booliId) {



    }


    private void notifyListeners(ListingsDatabase.ActionCode actionCode, Result result) {
        for (ServerConnectionListener listener : serverConnectionListeners) {
            listener.onResponse(actionCode, result);
        }
    }
}
