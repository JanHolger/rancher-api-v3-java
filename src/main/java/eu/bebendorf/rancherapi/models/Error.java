package eu.bebendorf.rancherapi.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class Error {

    String type = "error";
    String status;
    String message;

}
