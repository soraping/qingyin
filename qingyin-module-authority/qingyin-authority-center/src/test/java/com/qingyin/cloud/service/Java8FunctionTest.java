package com.qingyin.cloud.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * java8 stream function 相关函数测试
 * Function 类型转换
 *  	compose、andThen、identity
 * Consumer 消费者
 * Supplier 生产者
 * Predicate 参数断言
 */
@Slf4j
@SpringBootTest
public class Java8FunctionTest {

	/**
	 * compose 函数组合
	 * f2.compose(f1).apply(data)
	 * <==>
	 * f2(f1(data))
	 */
	@Test
	public void composeTest() {
		String str = "1q-2w-3e";
		Integer sum = sumValue(str,  // 源数据
                (strType) -> strType.split("[^0-9]"),   // 拿到所有数字，实现方法把所有非数字进行分割，但是可能会有空字符串 "" 存在
                // 第三个参数就是先把空字符串 "" 过滤掉，然后把所有的 String型的数字转成 Integer 型的，最后对其进行归约求和操作
                // 归约操作会得到一个 Optional 型的容器对象，所以调用其 get() 就可以拿到里面的值啦
                (strArrType) -> Arrays.stream(strArrType).filter(ObjectUtil::isNotEmpty).map(Integer::valueOf)
                        .reduce(Integer::sum).get());
		System.out.println("compose sum value => " + sum);
	}
	
	Integer sumValue(String data, Function<String, String[]> function1, Function<String[], Integer> function2) {
        return function2.compose(function1).apply(data);
    }
	
	
	/**
	 * andThen 和 compose 执行顺序相反
	 * f2.andThen(f1).apply(data)
	 * <==>
	 * f1(f2(data))
	 */
	Integer composeValue(Integer num, Function<Integer, Integer> f1, Function<Integer, Integer> f2) {
		return f2.compose(f1).apply(num);
	}
	Integer andThenValue(Integer num, Function<Integer, Integer> f1, Function<Integer, Integer> f2) {
		return f2.andThen(f1).apply(num);
	}
	@Test
	public void andThen() {
		Integer num1 = composeValue(2, (param1) -> param1 + 1, (param2) -> param2 * 3);
		System.out.println("compose result => " + num1);
		Integer num2 = andThenValue(2, (param1) -> param1 + 1, (param2) -> param2 * 3);
		System.out.println("andThen result => " + num2);
	}

	/**
	 * identity 同一性，输入什么参数就返回什么结果。常用在 List 转 Map 中
	 * users.stream().collect(toMap(User::getName, Function.identity()))
	 */
	@Test
	public void identityTest() {
		List<String> arr = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
        	arr.add("cctv" + i);
        }
        // 无序是因为 toMap() 底层是用 HashMap::new 创建的 hashMap，hashMap 本身就是无序的。
        Map<String, String> map = arr.stream().collect(java.util.stream.Collectors.toMap(String::toUpperCase, Function.identity()));
        map.forEach((k, v) -> System.out.println(k + "-" + v));
	}
	
	/**
	 * Consumer 消费者，把传进来的参数消费掉，没有返回结果。
	 * 由它的抽象方法 accept() 接受一个参数，并由实现 Consumer 接口的操作来处理。
	 * 
	 * 最常见的 forEach() 传的就是 Consumer 接口，
	 * 其作用是接收集合中的每个元素，然后交给 Consumer 的操作来处理。
	 */
	@Test
	public void consumerTest() {
		List<String> arr = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
        	arr.add("翻斗花园" + i + "号");
        }
        arr.forEach(System.out::println);
	}
	
	/**
	 * Consumer 的 andThen 方法同Function 一样，
	 * 也是先执行外边的，再执行里面的，由外到里，从左到右，由 accept() 方法来接收一个源数据。
	 * 因为都没有返回值，所以这俩 Consumer 接口彼此之间是没有啥关联的，只是一个执行顺序的不同而已。
	 */
	void andThen2(String data, Consumer<String> f1, Consumer<String> f2) {
		f1.andThen(f2).accept(data);
	}
	@Test
	public void consumerAndThenTest() {
		String str = "aBc";
		andThen2(str,
                (strData)-> System.out.println(strData.toUpperCase()), // 输出全大写
                (strData)-> System.out.println(strData.toLowerCase())); // 输出全小写
	}
	
	
	/**
	 * Supplier 供应者，只有一个抽象方法 get(),不需要入参
	 * 
	 * 和 Consumer 相反，Supplier 只管生产东西，
	 * 比如创建一个对象、生成一个随机数、生成一个UUID这种无中生有的操作都可以交给 Supplier 来处理实现，然后用它的 get() 方法来拿
	 */
	@Test
	public void supplierTest() {
        Supplier<Integer> intSupplier = () -> new Random().nextInt(100);
        Supplier<UUID> uuidSupplier = UUID::randomUUID;
 
        System.out.println(intSupplier.get());
        System.out.println(uuidSupplier.get());
	}
	

}
