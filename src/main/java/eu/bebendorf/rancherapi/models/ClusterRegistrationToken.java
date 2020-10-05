package eu.bebendorf.rancherapi.models;

import eu.bebendorf.rancherapi.RancherAPI;
import eu.bebendorf.rancherapi.RancherException;
import eu.bebendorf.rancherapi.models.helper.TaintEffect;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ClusterRegistrationToken implements Model {

    RancherAPI api;

    String type = "clusterRegistrationToken";
    Map<String, String> annotations = new HashMap<>();
    Map<String, String> labels = new HashMap<>();
    Map<String, String> links;
    String baseType;
    String clusterId;
    Date created;
    Long createdTS;
    String creatorId;
    String id;
    UUID uuid;
    String transitioning;
    String transitioningMessage;
    String token;
    String state;
    String name;
    String command;
    String insecureCommand;
    String manifestUrl;
    String namespaceId;
    String nodeCommand;
    String windowsNodeCommand;

    public void delete() throws RancherException {
        api.deleteClusterRegistrationToken(this);
    }

    public String getNodeCommand(NodeConfig nodeConfig){
        return nodeCommand + " " + nodeConfig.toArgs();
    }

    public String getWindowsNodeCommand(NodeConfig nodeConfig){
        return windowsNodeCommand + " " + nodeConfig.toArgs();
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter @Setter
    @NoArgsConstructor
    public static class NodeConfig {
        boolean worker;
        boolean etcd;
        boolean controlPlane;
        String nodeName;
        String address;
        String internalAddress;
        Map<String, String> labels = new HashMap<>();
        Map<String, String> taints = new HashMap<>();
        public NodeConfig(Boolean etcd, Boolean controlPlane, Boolean worker){
            this.etcd = etcd;
            this.controlPlane = controlPlane;
            this.worker = worker;
        }
        public void addLabel(String key, String value){
            labels.put(key, value);
        }
        public void addTaint(String key, String value, TaintEffect effect){
            taints.put(key, value + ":" + effect.name());
        }
        public String toArgs(){
            List<String> args = new ArrayList<>();
            if(worker)
                args.add("--worker");
            if(controlPlane)
                args.add("--controlplane");
            if(etcd)
                args.add("--etcd");
            if(nodeName != null)
                args.add("--node-name "+nodeName);
            if(address != null)
                args.add("--address "+address);
            if(internalAddress != null)
                args.add("--internal-address "+internalAddress);
            for(String key : labels.keySet())
                args.add("--label "+key+"="+labels.get(key));
            for(String key : taints.keySet())
                args.add("--taints "+key+"="+taints.get(key));
            return String.join(" ", args);
        }
    }

}
