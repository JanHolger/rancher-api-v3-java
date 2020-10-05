package eu.bebendorf.rancherapi.models.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import eu.bebendorf.rancherapi.HttpClient;
import eu.bebendorf.rancherapi.RancherAPI;
import eu.bebendorf.rancherapi.models.Model;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter
public class Collection implements Model {

    RancherAPI api;

    String type = "collection";
    Map<String, String> links;
    Map<String, String> createTypes;
    Map<String, String> actions;
    Pagination pagination;
    Sort sort;
    Map<String, String> filters;
    String resourceType;
    JsonArray data;

    public <T extends Model> List<T> getData(Class<T> clazz){
        List<T> list = new ArrayList<>();
        for(JsonElement e : data){
            T object = HttpClient.GSON.fromJson(e, clazz);
            object.setApi(api);
            list.add(object);
        }
        return list;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter @Setter
    public static class Pagination {
        Integer limit;
        Integer total;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter @Setter
    public static class Sort {
        String name;
        String order;
        String reverse;
        Map<String, String> links;
    }

}
