package eu.bebendorf.rancherapi;

import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HttpClient {

    public static final Gson GSON = new GsonBuilder().create();

    private int timeout = 5000;

    public void setTimeout(int timeout){
        this.timeout = timeout;
    }

    protected byte[] httpRequest(String method, String url, Map<String, String> header, Map<String, Object> query, byte[] body){
        HttpURLConnection conn = null;
        try{
            URL theUrl = new URL(url+makeQuery(query));
            conn = (HttpURLConnection) theUrl.openConnection();
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            if(header != null){
                for(String k : header.keySet()){
                    conn.setRequestProperty(k, header.get(k));
                }
            }
            if(body!=null){
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(body);
                os.flush();
                os.close();
            }
            int responseCode = conn.getResponseCode();
            if(responseCode>299){
                return readAll(conn.getErrorStream());
            }else{
                return readAll(conn.getInputStream());
            }
        }catch(Exception e){
            try {
                return readAll(conn.getErrorStream());
            }catch(IOException | NullPointerException ex){}
        }
        return new byte[0];
    }

    public String httpRequestString(String method, String url, Map<String, String> header, Map<String, Object> query, String body) {
        return new String(httpRequest(method, url, header, query, body == null ? null : body.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    private static byte[] readAll(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int r = 0;
        while (r != -1){
            r = is.read(data);
            if(r != -1)
                baos.write(data, 0, r);
        }
        is.close();
        return baos.toByteArray();
    }

    protected static String makeQuery(Object object){
        String form = makeForm(object);
        if(form.length() == 0)
            return "";
        return "?"+form;
    }

    public static <K,V> Map<K, V> map(Object... data){
        if(data.length%2!=0)
            return null;
        Map<K, V> map = new HashMap<>();
        for(int i=0; i<data.length; i+=2){
            map.put((K) data[i], (V)data[i+1]);
        }
        return map;
    }

    public static <T> List<T> list(T... data){
        List<T> list = new ArrayList<>();
        for(T o : data)
            list.add(o);
        return list;
    }

    protected static String makeForm(Object object){
        if(object == null)
            return "";
        return queryParams(makeFormRecursive(GSON.toJsonTree(object), true));
    }

    protected static String makeJson(Object object){
        if(object instanceof String)
            return (String) object;
        return GSON.toJson(object);
    }

    public static <T> Map<String, T> toMap(JsonObject src, Class<T> clazz){
        Map<String, T> map = new HashMap<>();
        for(String key : src.keySet())
            map.put(key, HttpClient.GSON.fromJson(src.get(key), clazz));
        return map;
    }

    private static Map<String, String> makeFormRecursive(JsonElement data, boolean root){
        Map<String, String> form = new HashMap<>();
        if(data instanceof JsonArray){
            JsonArray array = data.getAsJsonArray();
            for(int i=0; i<array.size(); i++){
                Map<String, String> value = makeFormRecursive(array.get(i), false);
                for(String k : value.keySet()){
                    form.put("["+i+"]"+k, value.get(k));
                }
            }
            return form;
        }
        if(data instanceof JsonObject){
            JsonObject object = data.getAsJsonObject();
            for(String key : object.keySet()){
                Map<String, String> value = makeFormRecursive(object.get(key), false);
                for(String k : value.keySet()){
                    if(root){
                        form.put(key+k, value.get(k));
                    }else{
                        form.put("["+key+"]"+k, value.get(k));
                    }
                }
            }
            return form;
        }
        if(data instanceof JsonPrimitive){
            JsonPrimitive primitive = data.getAsJsonPrimitive();
            if(primitive.isBoolean())
                form.put("", String.valueOf(primitive.getAsBoolean()));
            if(primitive.isNumber())
                form.put("", String.valueOf(primitive.getAsInt()));
            if(primitive.isString())
                form.put("", primitive.getAsString());
            return form;
        }
        form.put("", data.toString());
        return form;
    }

    private static String urlEncode(String value){
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static String queryParams(Map<String, String> params){
        List<String> p = new ArrayList<>();
        for(String key : params.keySet()){
            p.add(key+"="+urlEncode(params.get(key)));
        }
        return String.join("&", p);
    }

}
