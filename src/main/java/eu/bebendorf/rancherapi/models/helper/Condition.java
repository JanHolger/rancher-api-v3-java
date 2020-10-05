package eu.bebendorf.rancherapi.models.helper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Condition {
    String type;
    String status;
    Date lastUpdateTime;
}