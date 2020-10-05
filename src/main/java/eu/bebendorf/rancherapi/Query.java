package eu.bebendorf.rancherapi;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Query {

    Map<String, Object> filters = new HashMap<>();

    public Query filter(String key, Object value){
        filters.put(key, value);
        return this;
    }

    public Map<String, Object> toQuery(){
        Map<String, Object> query = new HashMap<>();
        if(filters.size() > 0)
            query.put("filters", filters);
        return query;
    }

}
