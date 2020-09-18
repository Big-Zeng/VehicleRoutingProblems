package common;

/**
 * Created by Administrator on 2020/8/13.
 */




import org.slf4j.LoggerFactory;
import pojo.OrderInformation;
import redis.clients.jedis.*;

import java.util.*;

public class RedisClient {


    public static String HOST = "125.221.225.135";
    public static String PORT = "6379";
    public static String POINT_2_WAREHOUSE = "point2house";
    public static String B2B = "B2B";
    public static String VIRTUAL_POINT2WAREHOUSE = "p2w";
    private org.slf4j.Logger logger= LoggerFactory.getLogger(RedisClient.class);

    private Jedis jedis;//非切片额客户端连接
    private JedisPool jedisPool;//非切片连接池
    private ShardedJedis shardedJedis;//切片额客户端连接
    private ShardedJedisPool shardedJedisPool;//切片连接池

    public RedisClient() {
        initialPool();
        initialShardedPool();
        shardedJedis = shardedJedisPool.getResource();
        jedis = jedisPool.getResource();

    }

    /**
     * 初始化非切片池
     */
    private void initialPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxIdle(0);

        config.setTestOnBorrow(false);


        jedisPool = new JedisPool(config, HOST, Integer.valueOf(PORT));
    }

    /**
     * 初始化切片池
     */
    private void initialShardedPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxIdle(5);

        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo(HOST, Integer.valueOf(PORT), "master"));

        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }


    /**
     * redis中放置key,value
     * @param key
     * @param value
     */
    public void put(String key,String value){
        shardedJedis.set(key,value);
    }

    /**
     * redis中放置key,key,value
     * @param areaId
     * @param clientId
     * @param value
     */
    public void put(String areaId,String clientId,String value){
        shardedJedis.hset(areaId, clientId, value);
    }

    /**
     * 根据key,key获取value
     * @param areaId
     * @param clientId
     * @return
     */
    public String get(String areaId,String clientId){
        return shardedJedis.hget(areaId,clientId);
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    public String get(String key){
        return shardedJedis.get(key);
    }

    /**
     * 是否存在这样的key,key
     * @param areaId
     * @param clientId
     * @return
     */
    public boolean isExitClient(String areaId,String clientId){
        return shardedJedis.hexists(areaId, clientId);
    }

    public boolean isExitPoint2Wareho(String key){
        return shardedJedis.exists(key);
    }

    /**
     * 根据key获取所有key
     * @param aredId
     * @return
     */
    public Set<String> getClients(String aredId){
        return  shardedJedis.hkeys(aredId);
    }


    /**
     * 根据areaId和ClientId获取与ClientId相关的ClientId集合
     * @param areaId
     * @param clientId
     * @return
     */
    public List<String> getClientIdListByClientId(String areaId,String clientId) {
        List<String> clientIdList = new ArrayList<>();
        Set<String> clients = getClients(areaId);
        for (String string : clients) {
            if (string.contains(clientId)) {
                string = string.replace(clientId, "").replace("*", "");
                clientIdList.add(string);
            }
        }
        return clientIdList;
    }

    /**
     *根据areaId查询客户集合
     * @param areaId
     * @return
     */
    public  List<String> getClientIdListByArea(String areaId) {
        List<String> clientIdList = new ArrayList<>();
        Set<String> clients = getClients(areaId);
        for (String string : clients) {
            clientIdList.add(string);
        }
        return clientIdList;
    }



    /**
     * 根据区域id获取区域内客户点间的相互距离
     * @param areaId
     * @return
     */
    public Map<String,String> getPathByAreaId(String areaId) {
        Map<String, String> areaPath = new HashMap<>();
        Set<String> clients = getClients(areaId);
        StringBuilder path = new StringBuilder();
        for (String clientId : clients) {
            String dis = get(areaId, clientId);
            String client = clientId.replace('*', ',');
            path.append(client + "," + dis + ";");
        }
        areaPath.put(areaId, path.toString());
        return areaPath;
    }

    /**
     * 获取各个区域所有点到仓库点的距离
     * @return
     */
    public Map<String,Double> getPoint2WareHouse(){
        Map<String,Double> point2wareHousePath=new HashMap<>();
        Set<String > clients=getClients(VIRTUAL_POINT2WAREHOUSE);
        StringBuilder path=new StringBuilder();
        for (String clientId:clients){
            String dis=get(VIRTUAL_POINT2WAREHOUSE, clientId);
            point2wareHousePath.put(clientId,Double.valueOf(dis));
        }
        return point2wareHousePath;
    }


    /**
     * get dis between one client to other clients
     * @param orders
     * @return
     */
    public int[][] getPathByAreaAndOrders(List<OrderInformation> orders ) {

        int length = orders.size();
        int[][] dis = new int[length + 1][length + 1];
        for (int i = 0; i < length; i++) {
            orders.get(i).setId(i);
            for (int j = 0; j < length; j++) {
                String joinClient = orders.get(i).getClientId().toString() + "*" + orders.get(j).getClientId().toString();
                Integer value
                        = Integer.valueOf(get(orders.get(0).getAreaId(), joinClient));
                dis[i][j] = value;
            }
        }

        for (int i = 0; i < orders.size(); i++) {
            String path1 = get(POINT_2_WAREHOUSE, B2B + "*" + orders.get(i).getClientId());
            String path2 = get(POINT_2_WAREHOUSE, orders.get(i).getClientId() + "*" + B2B);
            dis[length][i]
                    = Double.valueOf(path1).intValue();
            dis[i][length]
                    = Double.valueOf(path2).intValue();
        }


        return dis;



    }




}
