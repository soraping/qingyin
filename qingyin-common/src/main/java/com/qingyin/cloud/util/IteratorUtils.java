package com.qingyin.cloud.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 增加一些Java8中不支持的集合操作方法
 * Created by liuzhaoming on 15/12/2.
 */
@SuppressWarnings({"unused", "unchecked"})
public class IteratorUtils {

    /**
     * 收集对象中的Key，过滤掉重复值
     *
     * @param coll 要收集的对象
     * @param <K>  返回的Key类型
     * @param <V>  要收集对象的对象类型
     * @return Key集合
     */
    public static <K, V> List<K> collectKey(Collection<V> coll, Function<V, K> keyGetter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyList();
        }

        return coll.stream().map(keyGetter).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    /**
     * 收集对象中的Key，过滤掉重复值
     *
     * @param coll 要收集的对象
     * @param <K>  返回的Key类型
     * @param <V>  要收集对象的对象类型
     * @return Key集合
     */
    public static <K, V> List<K> collectKey(Collection<V> coll, Predicate<V> predicate, Function<V, K> keyGetter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyList();
        }

        return coll.stream()
                .filter(predicate)
                .map(keyGetter)
                .filter(k -> k != null)
                .distinct()
                .collect(Collectors.toList());
    }

    public static <K, V> Set<K> collectKeySet(Collection<V> coll, Function<V, K> keyGetter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptySet();
        }

        return coll.stream()
                .map(keyGetter)
                .filter(k -> k != null)
                .collect(Collectors.toSet());
    }

    /**
     * 批量收集对象中的Key，过滤掉重复值
     *
     * @param coll 要收集的对象
     * @param <K>  返回的Key类型
     * @param <V>  要收集对象的对象类型
     * @return Key集合
     */
    public static <K, V> List<K> collectFlatKey(Collection<V> coll, Function<V, List<K>> keyGetter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyList();
        }

        return coll.stream().flatMap(item -> keyGetter.apply(item).stream()).distinct().collect(Collectors.toList());
    }

    public static <K, V> List<K> collectFlatKey(Collection<V> coll, Predicate<V> predicate, Function<V, List<K>>
            keyGetter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyList();
        }

        return coll.stream().filter(predicate).flatMap(item -> keyGetter.apply(item).stream()).distinct().collect
                (Collectors.toList());
    }

    /**
     * 根据指定Key分组
     * 如果存在一个描述人的类Person(id, name, area)，给定3个人(1, "Tom", "南京")，(2, "Cate", "南京")，(3, "Lily", "北京")
     * 那么希望给这三个人根据地域（area）进行分组，只需调用方法
     * groupBy(Arrays.asList(person1, person2, person3), Person::getArea)
     * 返回值为Map:{"南京" : [person1, person2], "北京" : [person3]}
     *
     * @param coll      要进行分组的集合
     * @param keyGetter 根据集合中的元素获取分组的Key
     * @param <K>       分组的Key类型
     * @param <V>       集合元素
     * @return 分好组的Map
     */
    public static <K, V> Map<K, List<V>> groupBy(Collection<V> coll, Function<V, K> keyGetter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyMap();
        }
        return coll.stream().collect(Collectors.groupingBy(keyGetter));
    }

    public static <K, V> Map<K, V> toMap(Collection<V> coll, Function<V, K> keyGetter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyMap();
        }
        return coll.stream().collect(Collectors.toMap(keyGetter, val -> val));
    }

    public static <K, V, R> Map<K, R> toMap(Collection<V> coll, Function<V, K> keyGetter, Function<V, R> mapper) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyMap();
        }
        return coll.stream().collect(Collectors.toMap(keyGetter, mapper));
    }

    /**
     * key冲突时, 后面的覆盖前面的
     */
    public static <K, V> Map<K, V> toMapWithCover(Collection<V> coll, Function<V, K> keyGetter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyMap();
        }
        return coll.stream().collect(Collectors.toMap(keyGetter, val -> val, (prev, cur) -> cur));
    }

    public static <E> List<E> filter(List<E> coll, Predicate<E> predicate) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyList();
        }
        return coll.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <E, R> List<R> mapTo(List<E> coll, Function<E, R> mapper) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyList();
        }
        return coll.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * 根据集合中元素的某个属性进行去重, 如果存在属性值一样的元素, 后面的会覆盖前面的
     *
     * @param coll      要过滤的对象集合
     * @param keyGetter 获取元素属性
     * @param <E>       集合元素类型
     * @param <K>       元素属性类型
     * @return
     */
    public static <E, K> List<E> distinct(List<E> coll, Function<E, K> keyGetter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyList();
        }
        Map<K, E> map = toMapWithCover(coll, keyGetter);
        return new ArrayList<>(map.values());
    }


    /**
     * 对两个集合的对象进行压缩，返回新的对象的集合
     * 商品SPU(id, name, skuList（操作方法有增加单个SKU addSku(SKU), 批量增加SKU batchAddSku(List<SKU>) ）),
     * SKU(id, name, spuId);
     * 两个对象间通过spuID关联
     * 存在如下的SPU: spu1=Spu(1, "iPhone6s", []); spu2=Spu(2, "Huawei Mate8", []); spu1=Spu(3, "三星S5", [])
     * 存在如下的SKU: sku1=Sku(11, "iPhone6s金色", 1); sku2=Sku(12, "iPhone6s灰色", 1); sku3=Sku(31, "三星S5白色", 3)
     * 调用方法 zip(List[spu1,spu2,spu3], List[sku1,sku2,sku3], {spu, sku} -> spu.getId()==sku.getSpuId(),
     * {spu, sku} -> SPU::addSku)
     * 返回结果为List[Spu(1, "iPhone6s", [sku1, sku2]); Spu(2, "Huawei Mate8", []); Spu(3, "三星S5", [sku3])]
     *
     * @param coll    要合并的对象集合
     * @param subColl 被合并的对象集合
     * @param matcher 两个对象匹配函数
     * @param merger  两个对象的合并函数，Consumer类型，无需返回值
     * @param <F>     要合并的对象类型
     * @param <V>     合并后的对象类型
     * @return 合并后的对象集合
     */
    public static <F, V> List<F> zip(Collection<F> coll, Collection<V> subColl, BiFunction<F, V, Boolean> matcher,
                                     BiConsumer<F, V> merger) {
        coll.forEach(item -> CollectionUtils.select(subColl, object -> matcher.apply(item, object)).forEach(subItem
                -> merger.accept(item, subItem)));
        return coll instanceof List ? (List) coll : new ArrayList<>(coll);
    }

    /**
     * 对content进行排序
     *
     * @param orderRefer 用作参考的顺序列表
     * @param content    需要排序的对象集合
     * @param matcher    orderRefer 与 content的匹配函数
     * @param merger     合并函数,用来重组结果result
     * @param <F>        参考顺序的类型
     * @param <V>        需要排序的类型
     * @param <R>        返回的类型
     */
    public static <F, V, R> List<R> order(Collection<F> orderRefer, Collection<V> content,
                                          BiFunction<F, V, Boolean> matcher, BiFunction<F, V, R> merger) {
        return orderRefer.stream().map(
                order -> CollectionUtils.select(content, object -> matcher.apply(order, object))
                        .stream().map(object -> merger.apply(order, object)).collect(Collectors.toList()))
                .reduce(new ArrayList<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * 对两个集合的对象进行压缩，返回新的对象的集合
     * 商品SPU(id, name, skuList（操作方法有增加单个SKU addSku(SKU), 批量增加SKU batchAddSku(List<SKU>) ）),
     * SKU(id, name, spuId);
     * 两个对象间通过spuID关联
     * 存在如下的SPU: spu1=Spu(1, "iPhone6s", []); spu2=Spu(2, "Huawei Mate8", []); spu1=Spu(3, "三星S5", [])
     * 存在如下的SKU: sku1=Sku(11, "iPhone6s金色", 1); sku2=Sku(12, "iPhone6s灰色", 1); sku3=Sku(31, "三星S5白色", 3)
     * 调用方法 zip(List[spu1,spu2,spu3], List[sku1,sku2,sku3], {spu, sku} -> spu.getId()==sku.getSpuId(),
     * {spu, sku} -> {spu.addSku(sku); return spu;})
     * 返回结果为List[Spu(1, "iPhone6s", [sku1, sku2]); Spu(2, "Huawei Mate8", []); Spu(3, "三星S5", [sku3])]
     *
     * @param coll    要合并的对象集合
     * @param subColl 被合并的对象集合
     * @param matcher 两个对象匹配函数
     * @param merger  两个对象的合并函数，需要返回一个合并后的对象
     * @param <F>     要合并的对象类型
     * @param <V>     被合并的对象类型
     * @param <R>     合并后的对象类型
     * @return 合并后的对象集合
     */
    public static <F, V, R> List<R> zip(Collection<F> coll, Collection<V> subColl,
                                        BiFunction<F, V, Boolean> matcher, BiFunction<F, List<V>, R> merger) {
        return coll.stream().map(item -> {
            List<V> matchSubItemList = CollectionUtils.select(subColl, object -> matcher.apply(item, object)).stream
                    ().collect(Collectors.toList());
            return merger.apply(item, matchSubItemList);
        }).collect(Collectors.toList());
    }

    /**
     * 根据Map的Key 压缩集合和Map
     * 商品SPU(id, name, skuList（操作方法有增加单个SKU addSku(SKU), 批量增加SKU batchAddSku(List<SKU>) ）),
     * SKU(id, name, spuId);
     * 两个对象间通过spuID关联
     * 存在如下的SPU: spu1=Spu(1, "iPhone6s", []); spu2=Spu(2, "Huawei Mate8", []); spu1=Spu(3, "三星S5", [])
     * 存在如下的SKU: sku1=Sku(11, "iPhone6s金色", 1); sku2=Sku(12, "iPhone6s灰色", 1); sku3=Sku(31, "三星S5白色", 3)
     * 存在Sku Map 根据spuId对SKU进行分类，skuMap={1:List[sku1,sku2], 3:List[sku3]}
     * 调用方法 zip(List[spu1,spu2,spu3], skuMap, {spu, spuId} -> spu.getId()==spuId,
     * {spu, skuList} -> {spu.batchAddSku(skuList); return spu;})
     * 返回结果为List[Spu(1, "iPhone6s", [sku1, sku2]); Spu(2, "Huawei Mate8", []); Spu(3, "三星S5", [sku3])]
     *
     * @param coll     要合并的对象集合
     * @param childMap 被合并的对象Map
     * @param matcher  要合并的对象和Map Key匹配函数
     * @param merger   要合并的对象和被合并的对象List的合并函数，需要返回一个合并后的对象
     * @param <F>      要合并的对象类型
     * @param <K>      被合并的对象类型Key
     * @param <V>      被合并的对象类型
     * @param <R>      合并后的对象类型
     * @return 合并后的对象集合
     */
    public static <F, K, V, R> List<R> zip(Collection<F> coll, Map<K, V> childMap, BiFunction<F, K, Boolean>
            matcher, BiFunction<F, V, R> merger) {
        return coll.stream().map(item -> {
            Optional<Map.Entry<K, V>> selectedEntry = CollectionUtils.select(childMap.entrySet(),
                    entry -> matcher.apply(item, entry.getKey())).stream().findFirst();
            return merger.apply(item, selectedEntry.isPresent() ? selectedEntry.get().getValue() : null);
        }).collect(Collectors.toList());
    }


    /**
     *  对象去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }


    /**
     * 生成一个序列，类似于python的range函数
     *
     * @param size 范围
     * @return [1, 2, 3, 4, 5, 6, ... , size-1]
     */
    public static List<Long> range(long size) {
        if (size <= 1) {
            return Collections.emptyList();
        }

        List<Long> valueList = new ArrayList<>();
        for (long i = 0; i < size; i++) {
            valueList.add(i);
        }

        return valueList;
    }


    /**
     *  分批工具
     *
     * @param list
     * @param bundle
     * @return
     */
    public static <T> List<List<T>> batchListUtils(List<T> list, Integer bundle) {
        // 实例化返回对象
        List<List<T>> batchResult = new ArrayList<>();

        int size = list.size();
        if (size <= bundle) {
            batchResult.add(list);
            return batchResult;
        }

        for (int i = 0; i < size; i += bundle) {
            batchResult.add(new ArrayList<T> (
                    list.subList(i, Math.min(size, i + bundle)))
            );
        }
        return batchResult;
    }

    /**
     *  map => key list
     * @param map
     * @param value
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
