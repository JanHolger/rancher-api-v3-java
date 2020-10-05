package eu.bebendorf.rancherapi.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import eu.bebendorf.rancherapi.Query;
import eu.bebendorf.rancherapi.RancherAPI;
import eu.bebendorf.rancherapi.RancherException;
import eu.bebendorf.rancherapi.models.helper.Condition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Cluster implements Model {

    RancherAPI api;

    String type = "cluster";
    String id;
    String name;
    Date created;
    Long createdTS;
    String state;
    String transitioning;
    String transitioningMessage;
    Condition[] conditions;
    UUID uuid;
    String creatorId;
    String dockerRootDir;
    Boolean enableClusterAlerting;
    Boolean enableClusterMonitoring;
    Boolean enableNetworkPolicy;
    Boolean windowsPreferedCluster;
    Boolean appliedEnableNetworkPolicy;
    String baseType;
    String clusterTemplateId;
    String clusterTemplateRevisionId;
    String apiEndpoint;
    Boolean internal;
    Boolean istioEnabled;
    String defaultClusterRoleForProjectMembers;
    String defaultPodSecurityPolicyTemplateId;
    RancherKubernetesEngineConfig rancherKubernetesEngineConfig;
    LocalClusterAuthEndpoint localClusterAuthEndpoint;
    Map<String, String> annotations = new HashMap<>();
    Map<String, String> labels = new HashMap<>();
    Map<String, String> links;
    Map<String, String> actionLinks;
    Integer nodeCount;
    Integer nodeVersion;
    ScheduledClusterScan scheduledClusterScan;

    public void delete() throws RancherException {
        api.deleteCluster(this);
    }

    public String generateKubeconfig() throws RancherException {
        return api.generateClusterKubeconfig(this);
    }

    public ClusterRegistrationToken createRegistrationToken() throws RancherException {
        return api.createClusterRegistrationToken(this);
    }

    public List<Node> getNodes() throws RancherException {
        return getNodes(null);
    }

    public List<Node> getNodes(Query query) throws RancherException {
        return api.getNodes(query);
    }

    public static Cluster createDefault(){
        Cluster cluster = new Cluster();
        cluster.dockerRootDir = "/var/lib/docker";
        cluster.enableClusterAlerting = false;
        cluster.enableClusterMonitoring = false;
        cluster.enableNetworkPolicy = false;
        cluster.windowsPreferedCluster = false;
        cluster.rancherKubernetesEngineConfig = RancherKubernetesEngineConfig.createDefault();
        cluster.localClusterAuthEndpoint = new LocalClusterAuthEndpoint();
        cluster.localClusterAuthEndpoint.enabled = true;
        cluster.scheduledClusterScan = new ScheduledClusterScan();
        cluster.scheduledClusterScan.enabled = false;
        return cluster;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter @Setter
    public static class ScheduledClusterScan {
        Boolean enabled;
        JsonObject scheduleConfig;
        JsonObject scanConfig;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter @Setter
    public static class LocalClusterAuthEndpoint {
        Boolean enabled;
        String type;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter @Setter
    public static class RancherKubernetesEngineConfig {
        Integer addonJobTimeout;
        Boolean ignoreDockerVersion;
        Boolean sshAgentAuth;
        String type = "rancherKubernetesEngineConfig";
        String kubernetesVersion;
        AuthenticationConfig authentication;
        DNSConfig dns;
        NetworkConfig network;
        IngressConfig ingress;
        MonitoringConfig monitoring;
        RKEConfigServices services;
        UpgradeStrategy upgradeStrategy;

        public static RancherKubernetesEngineConfig createDefault(){
            RancherKubernetesEngineConfig config = new RancherKubernetesEngineConfig();
            config.addonJobTimeout = 30;
            config.ignoreDockerVersion = true;
            config.sshAgentAuth = false;
            config.kubernetesVersion = "v1.18.8-rancher1-1";
            config.authentication = new AuthenticationConfig();
            config.authentication.strategy = "x509";
            config.dns = new DNSConfig();
            config.dns.nodeLocal = new DNSConfig.NodeLocal();
            config.dns.nodeLocal.ipAddress = "";
            config.network = new NetworkConfig();
            config.network.mtu = 0;
            config.network.plugin = "calico";
            config.ingress = new IngressConfig();
            config.ingress.provider = "nginx";
            config.monitoring = new MonitoringConfig();
            config.monitoring.provider = "metrics-server";
            config.monitoring.replicas = 1;
            config.services = RKEConfigServices.createDefault();
            config.upgradeStrategy = UpgradeStrategy.createDefault();
            return config;
        }

        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter @Setter
        public static class AuthenticationConfig {
            String type = "authnConfig";
            String strategy;
        }

        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter @Setter
        public static class DNSConfig {
            String type = "dnsConfig";
            @SerializedName("nodelocal")
            NodeLocal nodeLocal;
            public static class NodeLocal {
                String type = "nodelocal";
                @SerializedName("ip_address")
                String ipAddress;
                @SerializedName("node_selector")
                JsonObject nodeSelector;
                @SerializedName("update_strategy")
                JsonObject updateStrategy = new JsonObject();
            }
        }

        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter @Setter
        public static class NetworkConfig {
            String type = "networkConfig";
            Integer mtu;
            String plugin;
            JsonObject options = new JsonObject();
        }

        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter @Setter
        public static class IngressConfig {
            String type = "ingressConfig";
            String provider;
        }

        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter @Setter
        public static class MonitoringConfig {
            String type = "monitoringConfig";
            String provider;
            Integer replicas;
        }

        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter @Setter
        public static class RKEConfigServices {
            String type = "rkeConfigServices";
            KubeAPIService kubeApi;
            ETCDService etcd;

            public static RKEConfigServices createDefault(){
                RKEConfigServices services = new RKEConfigServices();
                services.kubeApi = new KubeAPIService();
                services.kubeApi.alwaysPullImages = false;
                services.kubeApi.podSecurityPolicy = false;
                services.kubeApi.serviceNodePortRange = "30000-32767";
                services.etcd = new ETCDService();
                services.etcd.creation = "12h";
                services.etcd.extraArgs.addProperty("heartbeat-interval", 500);
                services.etcd.extraArgs.addProperty("election-timeout", 5000);
                services.etcd.gid = 0;
                services.etcd.uid = 0;
                services.etcd.retention = "72h";
                services.etcd.snapshot = false;
                services.etcd.backupConfig = new ETCDService.BackupConfig();
                services.etcd.backupConfig.enabled = true;
                services.etcd.backupConfig.intervalHours = 12;
                services.etcd.backupConfig.retention = 6;
                services.etcd.backupConfig.safeTimestamp = false;
                return services;
            }

            @FieldDefaults(level = AccessLevel.PRIVATE)
            @Getter @Setter
            public static class KubeAPIService {
                String type = "kubeAPIService";
                Boolean alwaysPullImages;
                Boolean podSecurityPolicy;
                String serviceNodePortRange;
            }

            @FieldDefaults(level = AccessLevel.PRIVATE)
            @Getter @Setter
            public static class ETCDService {
                String type = "etcdService";
                String creation;
                String retention;
                Boolean snapshot;
                Integer uid;
                Integer gid;
                JsonObject extraArgs = new JsonObject();
                BackupConfig backupConfig;

                @FieldDefaults(level = AccessLevel.PRIVATE)
                @Getter @Setter
                public static class BackupConfig {
                    String type = "backupConfig";
                    Boolean enabled;
                    Integer intervalHours;
                    Integer retention;
                    Boolean safeTimestamp;
                }
            }
        }

        @FieldDefaults(level = AccessLevel.PRIVATE)
        @Getter @Setter
        public static class UpgradeStrategy {
            String maxUnavailableControlplane;
            String maxUnavailableWorker;
            Boolean drain;
            NodeDrainInput nodeDrainInput;
            String maxUnavailableUnit;

            public static UpgradeStrategy createDefault(){
                UpgradeStrategy strategy = new UpgradeStrategy();
                strategy.maxUnavailableControlplane = "1";
                strategy.maxUnavailableWorker = "10%%";
                strategy.drain = false;
                strategy.maxUnavailableUnit = "percentage";
                strategy.nodeDrainInput = new NodeDrainInput();
                strategy.nodeDrainInput.deleteLocalData = false;
                strategy.nodeDrainInput.force = false;
                strategy.nodeDrainInput.gracePeriod = -1;
                strategy.nodeDrainInput.ignoreDaemonSets = true;
                strategy.nodeDrainInput.timeout = 120;
                return strategy;
            }

            @FieldDefaults(level = AccessLevel.PRIVATE)
            @Getter @Setter
            public static class NodeDrainInput {
                String type = "nodeDrainInput";
                Boolean deleteLocalData;
                Boolean force;
                Integer gracePeriod;
                Boolean ignoreDaemonSets;
                Integer timeout;
            }
        }
    }

}
