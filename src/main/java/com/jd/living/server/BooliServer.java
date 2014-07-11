package com.jd.living.server;


import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import com.jd.living.Search;
import com.jd.living.database.BooliDatabase;
import com.jd.living.model.ListingsResult;
import com.jd.living.model.Result;
import com.jd.living.model.SoldResult;

@EBean(scope = EBean.Scope.Singleton)
public class BooliServer {

    @RestService
    BooliClient restClient;

    public static final String common = "callerId={callerId}&time={time}&unique={unique}&hash={hash}";

    private List<ServerConnectionListener> serverConnectionListeners;

    public interface ServerConnectionListener {
        void onListingsResult(BooliDatabase.ActionCode action, Result result);
    }

    @AfterInject
    public void init(){
        serverConnectionListeners = new ArrayList<ServerConnectionListener>();
    }

    public void addServerConnectionListener(ServerConnectionListener connectionListener){
        serverConnectionListeners.add(connectionListener);
    }

    @Background
    public void getListings(Search search) {
        AuthStore authStore = new AuthStore();

        ListingsResult result = restClient.
                getListings(
                        search.getLocation(),
                        authStore.getCallerId(),
                        authStore.getTime(),
                        authStore.getUnique(),
                        authStore.getHash(),
                        search.getMinRooms(),
                        search.getMaxRooms(true),
                        search.getTypes(),
                        search.getMinAmount(),
                        search.getMaxAmount(),
                        search.getProduction(),
                        search.getMaxRent(),
                        500
                )
                .getBody();

        notifyListeners(BooliDatabase.ActionCode.LISTINGS, result);
    }

    @Background
    public void getObjectsSold(Search search) {
        AuthStore authStore = new AuthStore();

        SoldResult result = restClient.
                getObjectsSold(
                        search.getLocation(),
                        authStore.getCallerId(),
                        authStore.getTime(),
                        authStore.getUnique(),
                        authStore.getHash(),
                        search.getMinRooms(),
                        search.getMaxRooms(true),
                        search.getTypes(),
                        search.getMinAmount(),
                        search.getMaxAmount(),
                        search.getProduction(),
                        search.getMaxRent(),
                        500
                )
                .getBody();

        notifyListeners(BooliDatabase.ActionCode.SOLD, result);
    }

    @Background
    public void getListing(int booliId) {
        if (booliId != 0) {
            AuthStore authStore = new AuthStore();

            ListingsResult result = restClient.
                    getListing(
                            booliId,
                            authStore.getCallerId(),
                            authStore.getTime(),
                            authStore.getUnique(),
                            authStore.getHash()
                    )
                    .getBody();

            notifyListeners(BooliDatabase.ActionCode.FAVORITE, result);
        }

    }

    private void notifyListeners(BooliDatabase.ActionCode actionCode, Result result) {
        for (ServerConnectionListener listener : serverConnectionListeners) {
            listener.onListingsResult(actionCode, result);
        }
    }
}
