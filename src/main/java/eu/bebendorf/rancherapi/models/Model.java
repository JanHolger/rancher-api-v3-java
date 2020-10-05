package eu.bebendorf.rancherapi.models;

import eu.bebendorf.rancherapi.RancherAPI;

public interface Model {

    RancherAPI getApi();
    void setApi(RancherAPI api);

}
