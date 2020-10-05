# rancher-api-v3-java
A Java API Client for Rancher API v3 (Rancher 2)

## Usage
```java
RancherAPI api = new RancherAPI("https://example.com", "token-xxxxx", "xxxxxxxxxxxxxxxx");
Cluster cluster = Cluster.createDefault();
cluster.setName("example");
try {
  cluster = api.createCluster(cluster);
  System.out.println(cluster.getId());
} catch(RancherException ex) {
  System.out.println(ex.getMessage());
}
```
