package eu.bebendorf.rancherapi.models;

import eu.bebendorf.rancherapi.RancherAPI;
import eu.bebendorf.rancherapi.RancherException;
import eu.bebendorf.rancherapi.models.helper.Condition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class User implements Model {

    RancherAPI api;

    String type = "user";
    String id;
    Boolean enabled;
    String description;
    String creatorId;
    String baseType;
    Condition[] conditions;
    Date created;
    Long createdTS;
    Map<String, String> actionLinks;
    Map<String, String> annotations;
    Map<String, String> labels;
    Map<String, String> links;
    String[] principalIds;
    String state;
    String transitioning;
    String transitioningMessage;
    Boolean me;
    Boolean mustChangePassword;
    String name;
    String username;
    String password;
    UUID uuid;

    public void delete() throws RancherException {
        api.deleteUser(this);
    }

    public static User createDefault(){
        User user = new User();
        user.enabled = true;
        user.mustChangePassword = false;
        return user;
    }

}
