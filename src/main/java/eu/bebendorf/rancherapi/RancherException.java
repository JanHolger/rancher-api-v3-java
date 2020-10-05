package eu.bebendorf.rancherapi;

import eu.bebendorf.rancherapi.models.Error;

public class RancherException extends Exception {

    private Error error;

    public RancherException(Error error){
        super(error.getMessage());
        this.error = error;
    }

}
