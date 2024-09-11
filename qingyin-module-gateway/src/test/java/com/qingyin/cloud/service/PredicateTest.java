package com.qingyin.cloud.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * <h1>Java8 谓词 Predicate 使用方法与思想</h1>
 * */
@Slf4j
@SpringBootTest
public class PredicateTest {

    public static List<String> MICRO_SERVICE = Arrays.asList(
            "nacos", "authority", "gateway", "ribbon", "feign", "hystrix", "e-commerce"
    );

    /**
     * <h2>test 方法主要用于参数符不符合规则, 返回值是 boolean</h2>
     * */
    @Test
    public void testPredicateTest() {
        // 过滤出长度大于 5 的单词
        Predicate<String> letterLengthLimit = s -> s.length() > 5;
        // 用在 stream 的 filter 方法下
        MICRO_SERVICE.stream().filter(letterLengthLimit).forEach(System.out::println);
    }

    /**
     * <h2>and 方法等同于逻辑与 &&, 存在短路特性, 需要所有的条件都满足</h2>
     * */
    @Test
    public void testPredicateAnd() {
        Predicate<String> letterLengthLimit = s -> s.length() > 5;
        Predicate<String> letterStartWith = s -> s.startsWith("gate");

        // 长度大于 5 且 gate 开头的单词
        MICRO_SERVICE.stream().filter(
                letterLengthLimit.and(letterStartWith)
        ).forEach(System.out::println);
    }

    /**
     * <h2>or 等同于逻辑或 ||, 多个条件主要一个满足即可</h2>
     * */
    @Test
    public void testPredicateOr() {
        Predicate<String> letterLengthLimit = s -> s.length() > 5;
        Predicate<String> letterStartWith = s -> s.startsWith("gate");

        MICRO_SERVICE.stream().filter(
                letterLengthLimit.or(letterStartWith)
        ).forEach(System.out::println);
    }

    /**
     * <h2>negate 等同于逻辑非 !</h2>
     * */
    @Test
    public void testPredicateNegate() {

        Predicate<String> letterStartWith = s -> s.startsWith("gate");
        MICRO_SERVICE.stream().filter(letterStartWith.negate()).forEach(System.out::println);
    }

    /**
     * <h2>isEqual 类似于 equals(), 区别在于: 先判断对象是否为 NULL,
     * 不为 NULL 再使用 equals 进行比较</h2>
     * */
    @Test
    public void testPredicateIsEqual() {

        Predicate<String> equalGateway = s -> Predicate.isEqual("gateway").test(s);
        MICRO_SERVICE.stream().filter(equalGateway).forEach(System.out::println);
    }


}
