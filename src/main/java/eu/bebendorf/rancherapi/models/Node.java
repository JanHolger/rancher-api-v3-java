package eu.bebendorf.rancherapi.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import eu.bebendorf.rancherapi.RancherAPI;
import eu.bebendorf.rancherapi.RancherException;
import eu.bebendorf.rancherapi.models.helper.Condition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Node implements Model {

    RancherAPI api;

    public void delete() throws RancherException {
        api.deleteNode(this);
    }

    public void cordon() throws RancherException {
        api.cordonNode(this);
    }

    public void uncordon() throws RancherException {
        api.uncordonNode(this);
    }

    public void drain() throws RancherException {
        api.drainNode(this);
    }

    String type = "node";
    String id;
    String name;
    UUID uuid;
    String baseType;
    Date created;
    Long createdTS;
    String creatorId;
    String clusterId;
    Condition[] conditions;
    Boolean worker;
    Boolean controlPlane;
    Boolean etcd;
    Boolean unschedulable;
    Boolean imported;
    Integer appliedNodeVersion;
    String hostname;
    String externalIpAddress;
    String ipAddress;
    String namespaceId;
    String nodePoolId;
    String nodeTemplateId;
    String podCidr;
    String[] podCidrs;
    String requestedHostname;
    String state;
    String transitioning;
    String transitioningMessage;
    String nodeName;
    String sshUser;
    Map<String, String> actionLinks;
    Map<String, String> links;
    Map<String, String> labels = new HashMap<>();
    Map<String, String> annotations = new HashMap<>();
    Allocatable allocatable;
    Capacity capacity;
    Limits limits;
    Requested requested;
    NodePlan nodePlan;
    JsonObject customConfig;
    DockerInfo dockerInfo;
    Info info;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class Info {
        CPU cpu;
        Kubernetes kubernetes;
        Memory memory;
        OS os;
        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter
        @Setter
        public static class CPU {
            Integer count;
        }
        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter
        @Setter
        public static class Kubernetes {
            String kubeProxyVersion;
            String kubeletVersion;
        }
        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter
        @Setter
        public static class Memory {
            Integer memTotalKiB;
        }
        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter
        @Setter
        public static class OS {
            String dockerVersion;
            String kernelVersion;
            String operatingSystem;
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class NodePlan {
        String type = "nodePlan";
        Plan plan;
        Integer agentCheckInterval;
        Integer version;
        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter
        @Setter
        public static class Plan {
            String type = "rkeConfigNodePlan";
            JsonObject processes;
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class DockerInfo {
        String type = "dockerInfo";
        Boolean debug;
        Boolean experimentalBuild;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class Allocatable {
        String cpu;
        String memory;
        String pods;
        @SerializedName("ephemeral-storage")
        String ephemeralStorage;
        @SerializedName("hugepages-1Gi")
        String hugepages1Gi;
        @SerializedName("hugepages-2Mi")
        String hugepages2Mi;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class Capacity {
        String cpu;
        String memory;
        String pods;
        @SerializedName("ephemeral-storage")
        String ephemeralStorage;
        @SerializedName("hugepages-1Gi")
        String hugepages1Gi;
        @SerializedName("hugepages-2Mi")
        String hugepages2Mi;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class Limits {
        String cpu;
        String memory;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class Requested {
        String cpu;
        String memory;
        String pods;
    }

}
