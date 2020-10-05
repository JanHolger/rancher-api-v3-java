package eu.bebendorf.rancherapi;

import com.google.gson.JsonObject;
import eu.bebendorf.rancherapi.models.*;
import eu.bebendorf.rancherapi.models.Error;
import eu.bebendorf.rancherapi.models.helper.Collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RancherAPI extends HttpClient {

    private final String baseUrl;
    private final String apiKey;

    public RancherAPI(String baseUrl, String accessKey, String secretKey){
        this(baseUrl, accessKey + ":" + secretKey);
    }

    public RancherAPI(String baseUrl, String apiKey){
        if(baseUrl.endsWith("/"))
            baseUrl = baseUrl.substring(0, baseUrl.length()-1);
        if(!baseUrl.endsWith("/v3"))
            baseUrl = baseUrl + "/v3";
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public byte[] httpRequest(String method, String url, Map<String, String> header, Map<String, Object> query, byte[] body) {
        if(!url.startsWith("/"))
            url = "/"+url;
        if(header == null)
            header = new HashMap<>();
        header.put("Authorization", "Bearer "+apiKey);
        return super.httpRequest(method, baseUrl + url, header, query, body);
    }

    private <T> T parseOrError(String json, Class<T> clazz) throws RancherException {
        System.out.println(json);
        JsonObject object = GSON.fromJson(json, JsonObject.class);
        if(object.get("type").getAsString().equals("error"))
            throw new RancherException(GSON.fromJson(object, Error.class));
        return GSON.fromJson(object, clazz);
    }

    public <T> T get(String path, Class<T> clazz) throws RancherException {
        return get(path, null, clazz);
    }

    public <T> T get(String path, Map<String,Object> query, Class<T> clazz) throws RancherException {
        String res = httpRequestString("GET", path, null, query, null);
        T object = parseOrError(res, clazz);
        if(object instanceof Model){
            Model model = (Model) object;
            model.setApi(this);
        }
        return object;
    }

    public <T extends Model> List<T> list(String path, Query query, Class<T> clazz) throws RancherException {
        return get(path, query == null ? null : query.toQuery(), Collection.class).getData(clazz);
    }

    public <T> T post(String path, Object body, Class<T> clazz) throws RancherException {
        return post(path, body, null, clazz);
    }

    public <T> T post(String path, Object body, Map<String, Object> query, Class<T> clazz) throws RancherException {
        String res = httpRequestString("POST", path, null, query, body == null ? null : makeJson(body));
        if(clazz == null){
            parseOrError(res, JsonObject.class);
            return null;
        }
        T object = parseOrError(res, clazz);
        if(object instanceof Model){
            Model model = (Model) object;
            model.setApi(this);
        }
        return object;
    }

    public <T> T action(String path, String action, Class<T> clazz) throws RancherException {
        return action(path, null, action, clazz);
    }

    public <T> T action(String path, Object body, String action, Class<T> clazz) throws RancherException {
        return post(path, body, new HashMap<String, Object>(){{
            put("action", action);
        }}, clazz);
    }

    public <T> T delete(String path, Class<T> clazz) throws RancherException {
        return parseOrError(httpRequestString("DELETE", path, null, null, null), clazz);
    }

    public User createUser(String username, String name, String password, Boolean mustChangePassword) throws RancherException {
        User user = User.createDefault();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(password);
        user.setMustChangePassword(mustChangePassword);
        return createUser(user);
    }

    public User createUser(User user) throws RancherException {
        return post("user", user, User.class);
    }

    public List<User> getUsers() throws RancherException {
        return getUsers(null);
    }

    public List<User> getUsers(Query query) throws RancherException {
        return list("user", query, User.class);
    }

    public User getUser(String id) throws RancherException {
        return get("user/"+id, User.class);
    }

    public void deleteUser(User user) throws RancherException {
        deleteUser(user.getId());
    }

    public User deleteUser(String id) throws RancherException {
        return delete("user/"+id, User.class);
    }

    public Cluster createCluster(Cluster cluster) throws RancherException {
        return post("cluster", cluster, Cluster.class);
    }

    public List<Cluster> getClusters() throws RancherException {
        return getClusters(null);
    }

    public List<Cluster> getClusters(Query query) throws RancherException {
        return list("cluster", query, Cluster.class);
    }

    public Cluster getCluster(String id) throws RancherException {
        return get("cluster/"+id, Cluster.class);
    }

    public void deleteCluster(Cluster cluster) throws RancherException {
        deleteCluster(cluster.getId());
    }

    public Cluster deleteCluster(String id) throws RancherException {
        return delete("cluster/"+id, Cluster.class);
    }

    public String generateClusterKubeconfig(Cluster cluster) throws RancherException {
        return generateClusterKubeconfig(cluster.getId());
    }

    public String generateClusterKubeconfig(String id) throws RancherException {
        JsonObject res = action("cluster/"+id, "generateKubeconfig", JsonObject.class);
        return res.get("config").getAsString();
    }

    public ClusterRegistrationToken createClusterRegistrationToken(Cluster cluster) throws RancherException {
        return createClusterRegistrationToken(cluster.getId());
    }

    public ClusterRegistrationToken createClusterRegistrationToken(String clusterId) throws RancherException {
        ClusterRegistrationToken token = new ClusterRegistrationToken();
        token.setClusterId(clusterId);
        return createClusterRegistrationToken(token);
    }

    public ClusterRegistrationToken createClusterRegistrationToken(ClusterRegistrationToken clusterRegistrationToken) throws RancherException {
        return post("clusterregistrationtoken", clusterRegistrationToken, ClusterRegistrationToken.class);
    }

    public List<ClusterRegistrationToken> getClusterRegistrationTokens() throws RancherException {
        return getClusterRegistrationTokens(null);
    }

    public List<ClusterRegistrationToken> getClusterRegistrationTokens(Query query) throws RancherException {
        return list("clusterregistrationtoken", query, ClusterRegistrationToken.class);
    }

    public ClusterRegistrationToken getClusterRegistrationToken(String id) throws RancherException {
        return get("clusterregistrationtoken/"+id, ClusterRegistrationToken.class);
    }

    public void deleteClusterRegistrationToken(ClusterRegistrationToken token) throws RancherException {
        deleteClusterRegistrationToken(token.getId());
    }

    public ClusterRegistrationToken deleteClusterRegistrationToken(String id) throws RancherException {
        return delete("clusterregistrationtoken/"+id, ClusterRegistrationToken.class);
    }

    public List<Node> getNodes() throws RancherException {
        return getNodes(new Query());
    }

    public List<Node> getNodes(Query query) throws RancherException {
        return list("node", query, Node.class);
    }

    public Node getNode(String id) throws RancherException {
        return get("node/"+id, Node.class);
    }

    public void deleteNode(Node node) throws RancherException {
        deleteCluster(node.getId());
    }

    public Node deleteNode(String id) throws RancherException {
        return delete("node/"+id, Node.class);
    }

    public void cordonNode(Node node) throws RancherException {
        cordonNode(node.getId());
    }

    public void cordonNode(String id) throws RancherException {
        action("node/"+id, "cordon", null);
    }

    public void uncordonNode(Node node) throws RancherException {
        cordonNode(node.getId());
    }

    public void uncordonNode(String id) throws RancherException {
        action("node/"+id, "uncordon", null);
    }

    public void drainNode(Node node) throws RancherException {
        drainNode(node.getId());
    }

    public void drainNode(String id) throws RancherException {
        action("node/"+id, "drain", null);
    }

}
