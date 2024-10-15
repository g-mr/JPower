package top.jpower.core.redis.wrapper;

import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.domain.geo.BoundingBox;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.data.redis.domain.geo.GeoShape;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Geo 操作
 * 
 * @author mr.g
 */
@AllArgsConstructor
public class GeoOperationsWrapper<M> {

    private final GeoOperations<String, Object> delegate;
    private final Class<M> clz;


    /**
     * Add {@link Point} with given member {@literal name} to {@literal key}.
     *
     * @param key must not be {@literal null}.
     * @param point must not be {@literal null}.
     * @param member must not be {@literal null}.
     * @return Number of elements added. {@literal null} when used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     */
    public Long add(String key, Point point, M member) {
        return delegate.add(key, point, member);
    }

    /**
     * Add {@link Point} with given member {@literal name} to {@literal key}.
     *
     * @param key must not be {@literal null}.
     * @param point must not be {@literal null}.
     * @param member must not be {@literal null}.
     * @return Number of elements added. {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     * @deprecated since 2.0, use {@link #add(String, Point, Object)}.
     */
    @Deprecated
    public Long geoAdd(String key, Point point, M member) {
        return add(key, point, member);
    }

    /**
     * Add {@link RedisGeoCommands.GeoLocation} to {@literal key}.
     *
     * @param key must not be {@literal null}.
     * @param location must not be {@literal null}.
     * @return Number of elements added. {@literal null} when used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     */
    public Long add(String key, RedisGeoCommands.GeoLocation<M> location) {
        return delegate.add(key, new RedisGeoCommands.GeoLocation<>(location.getName(), location.getPoint()));
    }

    /**
     * Add {@link RedisGeoCommands.GeoLocation} to {@literal key}. 
     *
     * @param key must not be {@literal null}.
     * @param location must not be {@literal null}.
     * @return Number of elements added. {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     * @deprecated since 2.0, use {@link #add(String, RedisGeoCommands.GeoLocation)}.
     */
    @Deprecated
    public Long geoAdd(String key, RedisGeoCommands.GeoLocation<M> location) {
        return add(key, location);
    }

    /**
     * Add {@link Map} of member / {@link Point} pairs to {@literal key}.
     *
     * @param key must not be {@literal null}.
     * @param memberCoordinateMap must not be {@literal null}.
     * @return Number of elements added. {@literal null} when used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     */
    public Long add(String key, Map<M, Point> memberCoordinateMap) {
        return delegate.add(key, memberCoordinateMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    /**
     * Add {@link Map} of member / {@link Point} pairs to {@literal key}.
     *
     * @param key must not be {@literal null}.
     * @param memberCoordinateMap must not be {@literal null}.
     * @return Number of elements added. {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     * @deprecated since 2.0, use {@link #add(String, Map)}.
     */
    @Deprecated
    public Long geoAdd(String key, Map<M, Point> memberCoordinateMap) {
        return add(key, memberCoordinateMap);
    }

    /**
     * Add {@link RedisGeoCommands.GeoLocation}s to {@literal key}
     *
     * @param key must not be {@literal null}.
     * @param locations must not be {@literal null}.
     * @return Number of elements added. {@literal null} when used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     */
    public Long add(String key, Iterable<RedisGeoCommands.GeoLocation<M>> locations) {
        return delegate.add(key, StreamSupport.stream(locations.spliterator(), false).map(location->new RedisGeoCommands.GeoLocation<Object>(location.getName(), location.getPoint())).collect(Collectors.toList()));
    }

    /**
     * Add {@link RedisGeoCommands.GeoLocation}s to {@literal key}
     *
     * @param key must not be {@literal null}.
     * @param locations must not be {@literal null}.
     * @return Number of elements added. {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     * @deprecated since 2.0, use {@link #add(String, Iterable)}.
     */
    @Deprecated
    public Long geoAdd(String key, Iterable<RedisGeoCommands.GeoLocation<M>> locations) {
        return add(key, locations);
    }

    /**
     * Get the {@link Distance} between {@literal member1} and {@literal member2}.
     *
     * @param key must not be {@literal null}.
     * @param member1 must not be {@literal null}.
     * @param member2 must not be {@literal null}.
     * @return can be {@literal null}.
     * @since 2.0
     * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
     */
    public Distance distance(String key, M member1, M member2) {
        return delegate.distance(key, member1, member2);
    }

    /**
     * Get the {@link Distance} between {@literal member1} and {@literal member2}.
     *
     * @param key must not be {@literal null}.
     * @param member1 must not be {@literal null}.
     * @param member2 must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
     * @deprecated since 2.0, use {@link #distance(String, Object, Object)}.
     */
    @Deprecated
    public Distance geoDist(String key, M member1, M member2) {
        return distance(key, member1, member2);
    }

    /**
     * Get the {@link Distance} between {@literal member1} and {@literal member2} in the given {@link Metric}.
     *
     * @param key must not be {@literal null}.
     * @param member1 must not be {@literal null}.
     * @param member2 must not be {@literal null}.
     * @param metric must not be {@literal null}.
     * @return can be {@literal null}.
     * @since 2.0
     * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
     */
    public Distance distance(String key, M member1, M member2, Metric metric) {
        return delegate.distance(key, member1, member2, metric);
    }

    /**
     * Get the {@link Distance} between {@literal member1} and {@literal member2} in the given {@link Metric}.
     *
     * @param key must not be {@literal null}.
     * @param member1 must not be {@literal null}.
     * @param member2 must not be {@literal null}.
     * @param metric must not be {@literal null}.
     * @return can be {@literal null}.
     * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
     * @deprecated since 2.0, use {@link #distance(String, Object, Object, Metric)}.
     */
    @Deprecated
    public Distance geoDist(String key, M member1, M member2, Metric metric) {
        return distance(key, member1, member2, metric);
    }

    /**
     * Get Geohash representation of the position for one or more {@literal member}s.
     *
     * @param key must not be {@literal null}.
     * @param members must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
     */
    public List<String> hash(String key, M... members) {
        return delegate.hash(key, members);
    }

    /**
     * Get Geohash representation of the position for one or more {@literal member}s.
     *
     * @param key must not be {@literal null}.
     * @param members must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
     * @deprecated since 2.0, use {@link #hash(String, Object[])}.
     */
    @Deprecated
    public List<String> geoHash(String key, M... members) {
        return hash(key, members);
    }

    /**
     * Get the {@link Point} representation of positions for one or more {@literal member}s.
     *
     * @param key must not be {@literal null}.
     * @param members must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
     */
    public List<Point> position(String key, M... members) {
        return delegate.position(key, members);
    }

    /**
     * Get the {@link Point} representation of positions for one or more {@literal member}s.
     *
     * @param key must not be {@literal null}.
     * @param members must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
     * @deprecated since 2.0, use {@link #position(String, Object[])}.
     */
    @Deprecated
    public List<Point> geoPos(String key, M... members) {
        return position(key, members);
    }

    /**
     * Get the {@literal member}s within the boundaries of a given {@link Circle}.
     *
     * @param key must not be {@literal null}.
     * @param within must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(String key, Circle within) {
        return convert(delegate.radius(key, within));
    }

    /**
     * Get the {@literal member}s within the boundaries of a given {@link Circle}.
     *
     * @param key must not be {@literal null}.
     * @param within must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @deprecated since 2.0, use {@link #radius(String, Circle)}.
     */
    @Deprecated
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(String key, Circle within) {
        return convert(delegate.geoRadius(key, within));
    }

    /**
     * Get the {@literal member}s within the boundaries of a given {@link Circle} applying {@link RedisGeoCommands.GeoRadiusCommandArgs}.
     *
     * @param key must not be {@literal null}.
     * @param within must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(String key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return convert(delegate.radius(key, within, args));
    }

    /**
     * Get the {@literal member}s within the boundaries of a given {@link Circle} applying {@link RedisGeoCommands.GeoRadiusCommandArgs}.
     *
     * @param key must not be {@literal null}.
     * @param within must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @deprecated since 2.0, use {@link #radius(String, Circle, RedisGeoCommands.GeoRadiusCommandArgs)}.
     */
    @Deprecated
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(String key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return radius(key, within, args);
    }

    /**
     * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
     * {@literal radius}.
     *
     * @param key must not be {@literal null}.
     * @param member must not be {@literal null}.
     * @param radius
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(String key, M member, double radius) {
        return convert(delegate.radius(key, member, radius));
    }

    /**
     * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
     * {@literal radius}.
     *
     * @param key must not be {@literal null}.
     * @param member must not be {@literal null}.
     * @param radius
     * @return never {@literal null} unless used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @deprecated since 2.0, use {@link #radius(String, Object, double)}.
     */
    @Deprecated
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(String key, M member, double radius) {
        return radius(key, member, radius);
    }

    /**
     * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
     * {@literal radius} applying {@link Metric}.
     *
     * @param key must not be {@literal null}.
     * @param member must not be {@literal null}.
     * @param distance must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(String key, M member, Distance distance) {
        return convert(delegate.radius(key, member, distance));
    }

    /**
     * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
     * {@literal radius} applying {@link Metric}.
     *
     * @param key must not be {@literal null}.
     * @param member must not be {@literal null}.
     * @param distance must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @deprecated since 2.0, use {@link #radius(String, Object, Distance)}.
     */
    @Deprecated
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(String key, M member, Distance distance) {
        return radius(key, member, distance);
    }

    /**
     * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
     * {@literal radius} applying {@link Metric} and {@link RedisGeoCommands.GeoRadiusCommandArgs}.
     *
     * @param key must not be {@literal null}.
     * @param member must not be {@literal null}.
     * @param distance must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.0
     * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(String key, M member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return convert(delegate.radius(key, member, distance, args));
    }

    /**
     * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
     * {@literal radius} applying {@link Metric} and {@link RedisGeoCommands.GeoRadiusCommandArgs}.
     *
     * @param key must not be {@literal null}.
     * @param member must not be {@literal null}.
     * @param distance must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @deprecated since 2.0, use {@link #radius(String, Object, Distance, RedisGeoCommands.GeoRadiusCommandArgs)}.
     */
    @Deprecated
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(String key, M member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return radius(key, member, distance, args);
    }

    /**
     * Remove the {@literal member}s.
     *
     * @param key must not be {@literal null}.
     * @param members must not be {@literal null}.
     * @return Number of elements removed. {@literal null} when used in pipeline / transaction.
     * @since 2.0
     */
    public Long remove(String key, M... members) {
        return delegate.remove(key, members);
    }

    /**
     * Remove the {@literal member}s.
     *
     * @param key must not be {@literal null}.
     * @param members must not be {@literal null}.
     * @return Number of elements removed. {@literal null} when used in pipeline / transaction.
     * @deprecated since 2.0, use {@link #remove(Object, Object[])}.
     */
    @Deprecated
    public Long geoRemove(String key, M... members) {
        return remove(key, members);
    }

    /**
     * Get the {@literal member}s within the boundaries of a given {@link Circle}.
     *
     * @param key must not be {@literal null}.
     * @param within must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> search(String key, Circle within) {
        return search(key, GeoReference.fromCircle(within), GeoShape.byRadius(within.getRadius()),
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs());
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * {@link Distance radius}.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param radius must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> search(String key, GeoReference<M> reference, Distance radius) {
        return search(key, reference, radius, RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs());
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * {@link Distance radius} applying {@link RedisGeoCommands.GeoRadiusCommandArgs}.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param radius must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> search(String key, GeoReference<M> reference, Distance radius,
                                                               RedisGeoCommands.GeoSearchCommandArgs args) {
        return search(key, reference, GeoShape.byRadius(radius), args);
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * bounding box.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param boundingBox must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> search(String key, GeoReference<M> reference,
                                                               BoundingBox boundingBox) {
        return search(key, reference, boundingBox, RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs());
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * bounding box applying {@link RedisGeoCommands.GeoRadiusCommandArgs}.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param boundingBox must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> search(String key, GeoReference<M> reference, BoundingBox boundingBox,
                                                               RedisGeoCommands.GeoSearchCommandArgs args) {
        return search(key, reference, GeoShape.byBox(boundingBox), args);
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * {@link GeoShape predicate} applying {@link RedisGeoCommands.GeoRadiusCommandArgs}.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param geoPredicate must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearch">Redis Documentation: GEOSEARCH</a>
     */
    public GeoResults<RedisGeoCommands.GeoLocation<M>> search(String key, GeoReference<M> reference,
                                                       GeoShape geoPredicate, RedisGeoCommands.GeoSearchCommandArgs args) {
        return convert(delegate.search(key, convertGeoReference(reference), geoPredicate, args));
    }

    /**
     * Get the {@literal member}s within the boundaries of a given {@link Circle} and store results at {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param within must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
     */
    public Long searchAndStore(String key, String destKey, Circle within) {
        return searchAndStore(key, destKey, GeoReference.fromCircle(within), GeoShape.byRadius(within.getRadius()),
                RedisGeoCommands.GeoSearchStoreCommandArgs.newGeoSearchStoreArgs());
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * {@link Distance radius} and store results at {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param radius must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
     */
    public Long searchAndStore(String key, String destKey, GeoReference<M> reference, Distance radius) {
        return searchAndStore(key, destKey, reference, radius, RedisGeoCommands.GeoSearchStoreCommandArgs.newGeoSearchStoreArgs());
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * {@link Distance radius} applying {@link RedisGeoCommands.GeoRadiusCommandArgs} and store results at {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param radius must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
     */
    public Long searchAndStore(String key, String destKey, GeoReference<M> reference, Distance radius,
                                RedisGeoCommands.GeoSearchStoreCommandArgs args) {
        return searchAndStore(key, destKey, reference, GeoShape.byRadius(radius), args);
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * bounding box and store results at {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param boundingBox must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
     */
    public Long searchAndStore(String key, String destKey, GeoReference<M> reference, BoundingBox boundingBox) {
        return searchAndStore(key, destKey, reference, boundingBox, RedisGeoCommands.GeoSearchStoreCommandArgs.newGeoSearchStoreArgs());
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * bounding box applying {@link RedisGeoCommands.GeoRadiusCommandArgs} and store results at {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param boundingBox must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
     */
    public Long searchAndStore(String key, String destKey, GeoReference<M> reference, BoundingBox boundingBox,
                                RedisGeoCommands.GeoSearchStoreCommandArgs args) {
        return searchAndStore(key, destKey, reference, GeoShape.byBox(boundingBox), args);
    }

    /**
     * Get the {@literal member}s using {@link GeoReference} as center of the query within the boundaries of a given
     * {@link GeoShape predicate} applying {@link RedisGeoCommands.GeoRadiusCommandArgs} and store results at {@code destKey}.
     *
     * @param key must not be {@literal null}.
     * @param reference must not be {@literal null}.
     * @param geoPredicate must not be {@literal null}.
     * @param args must not be {@literal null}.
     * @return never {@literal null} unless used in pipeline / transaction.
     * @since 2.6
     * @see <a href="https://redis.io/commands/geosearchstore">Redis Documentation: GEOSEARCHSTORE</a>
     */
    public Long searchAndStore(String key, String destKey, GeoReference<M> reference, GeoShape geoPredicate,
                        RedisGeoCommands.GeoSearchStoreCommandArgs args) {
        return delegate.searchAndStore(key, destKey, convertGeoReference(reference), geoPredicate, args);
    }


    protected GeoResults<RedisGeoCommands.GeoLocation<M>> convert(GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults){
        if (geoResults == null){
            return null;
        }
        List<GeoResult<RedisGeoCommands.GeoLocation<M>>> mappedResults = geoResults.getContent().stream()
                .map(geo -> new GeoResult<>(
                        new RedisGeoCommands.GeoLocation<>(
                                Convert.convert(clz, geo.getContent().getName()),
                                geo.getContent().getPoint()),
                        geo.getDistance()))
                .collect(Collectors.toList());

        return new GeoResults<>(mappedResults, geoResults.getAverageDistance());
    }

    protected GeoReference<Object> convertGeoReference(GeoReference<M> reference) {
        return reference instanceof GeoReference.GeoMemberReference
                ? GeoReference.fromMember(((GeoReference.GeoMemberReference<M>) reference).getMember())
                : (GeoReference<Object>) reference;
    }
}
