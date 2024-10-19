// package top.jpower.core.redis.connection;
//
// import lombok.AllArgsConstructor;
// import org.springframework.dao.DataAccessException;
// import org.springframework.data.redis.connection.RedisClusterConnection;
// import org.springframework.data.redis.connection.RedisConnection;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.connection.RedisSentinelConnection;
// import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
// import org.springframework.data.redis.serializer.RedisSerializer;
// import top.jpower.core.redis.config.RedisPrefixHandler;
// import top.jpower.core.redis.properties.RedisProperties;
//
// /**
//  * @author mr.g
//  * @date 2024-9-10 0:27
//  * @description
//  */
// @AllArgsConstructor
// public class JpowerLettuceConnectionFactory extends LettuceConnectionFactory {
//     private RedisConnectionFactory connectionFactory;
//     private RedisProperties redisProperties;
//     private RedisPrefixHandler redisPrefixHandler;
//     private RedisSerializer<String> serializer;
//
//     public JpowerLettuceConnectionFactory(RedisConnectionFactory connectionFactory, RedisProperties redisProperties, RedisPrefixHandler redisPrefixHandler){
//         this(connectionFactory, redisProperties, redisPrefixHandler, RedisSerializer.string());
//     }
//     @Override
//     public RedisConnection getConnection() {
//         return new JpowerRedisConnection(connectionFactory.getConnection(), redisProperties, redisPrefixHandler, serializer);
//     }
//
//     /**
//      * Provides a suitable connection for interacting with Redis Cluster.
//      *
//      * @return
//      * @throws IllegalStateException if the connection factory requires initialization and the factory was not yet
//      *                               initialized.
//      * @since 1.7
//      */
//     @Override
//     public RedisClusterConnection getClusterConnection() {
//         return connectionFactory.getClusterConnection();
//     }
//
//     /**
//      * Specifies if pipelined results should be converted to the expected data type. If false, results of
//      * {@link RedisConnection#closePipeline()} and {RedisConnection#exec()} will be of the type returned by the underlying
//      * driver This method is mostly for backwards compatibility with 1.0. It is generally always a good idea to allow
//      * results to be converted and deserialized. In fact, this is now the default behavior.
//      *
//      * @return Whether or not to convert pipeline and tx results
//      */
//     @Override
//     public boolean getConvertPipelineAndTxResults() {
//         return connectionFactory.getConvertPipelineAndTxResults();
//     }
//
//     /**
//      * Provides a suitable connection for interacting with Redis Sentinel.
//      *
//      * @return connection for interacting with Redis Sentinel.
//      * @throws IllegalStateException if the connection factory requires initialization and the factory was not yet
//      *                               initialized.
//      * @since 1.4
//      */
//     @Override
//     public RedisSentinelConnection getSentinelConnection() {
//         return connectionFactory.getSentinelConnection();
//     }
//
//     @Override
//     public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
//         return connectionFactory.translateExceptionIfPossible(ex);
//     }
// }
