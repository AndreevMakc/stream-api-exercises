package space.gavinklfong.demo.streamapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AppCommandRunner implements CommandLineRunner {

	@Autowired
	private CustomerRepo customerRepos;
	
	@Autowired
	private OrderRepo orderRepos;
	
	@Autowired
	private ProductRepo productRepos;

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		log.info("Customers:");
		customerRepos.findAll()
				.forEach(c -> log.info(c.toString()));

		log.info("Orders:");
		orderRepos.findAll()
				.forEach(o -> log.info(o.toString()));

		log.info("Products:");
		productRepos.findAll()
				.forEach(p -> log.info(p.toString()));

		log.info("Exercise 1:");
		log.info("-----------------------------------");
		List<Product> result = productRepos.findAll().stream()
				.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
				.filter(product -> product.getPrice() > 100)
				.collect(Collectors.toList());
		for (Product product : result){
			log.info(product.toString());
		}

		log.info("Exercise 2");
		log.info("-----------------------------------");
		List<Order> resultEx2 = orderRepos.findAll().stream()
				.filter(o -> o.getProducts().stream()
						.anyMatch(p -> p.getCategory().equalsIgnoreCase("Baby")))
				.collect(Collectors.toList());

		for (Order order : resultEx2) {
			log.info(order.getId() + ": " + order.getProducts());
		}

		log.info("Exercise 3");
		log.info("-----------------------------------");
		List<Product> resultEx3 = productRepos.findAll().stream()
				.filter(p -> p.getCategory().equalsIgnoreCase("Toys"))
				.map(p -> p.withPrice(p.getPrice() * 0.9))
				.collect(Collectors.toList());
		for (Product product : resultEx3) {
			log.info(product.toString());
		}

		log.info("Exercise 4");
		log.info("-----------------------------------");
		List<Product> resultEx4 = orderRepos.findAll().stream()
				.filter(o -> o.getCustomer().getTier() == 2)
				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 4, 1)) <= 0)
				.flatMap(o -> o.getProducts().stream())
				.distinct()
				.sorted((a, b) -> a.getId().compareTo(b.getId()))
				.collect(Collectors.toList());
		for (Product product : resultEx4) {
			log.info(product.toString());
		}
		log.info("Exercise 5");
		log.info("-----------------------------------");
		Optional<Product> resultEx5 = productRepos.findAll().stream()
				.filter(p -> p.getCategory().equalsIgnoreCase("Books"))
				.min(Comparator.comparing(Product::getPrice));
		if (resultEx5.isPresent()){
			log.info(resultEx5.toString());
		}
		log.info("Exercise 6");
		log.info("-----------------------------------");
		List<Order> resultEx6 = orderRepos.findAll().stream()
				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.limit(3)
				.collect(Collectors.toList());
		for (Order order : resultEx6) {
			log.info(order.toString());
		}
		log.info("Exercise 7");
		log.info("-----------------------------------");
		List<Product> resultEx7 = orderRepos.findAll().stream()
				.filter(p -> p.getOrderDate().compareTo(LocalDate.of(2021, 3, 15)) == 0)
				.peek(o -> System.out.println(o.toString()))
				.flatMap(o -> o.getProducts().stream())
				.distinct()
				.collect(Collectors.toList());
		for (Product product : resultEx7) {
			log.info(product.toString());
		}
		log.info("Exercise 8");
		log.info("-----------------------------------");
		Double resultEx8 = orderRepos.findAll().stream()
				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 3, 1)) < 0)
				.flatMap(o -> o.getProducts().stream())
				.mapToDouble(p -> p.getPrice())
				.sum();
		log.info(resultEx8.toString());
		log.info("Exercise 9");
		log.info("-----------------------------------");
		OptionalDouble resultEx9 = orderRepos.findAll().stream()
				.filter(o -> o.getOrderDate().isEqual(LocalDate.of(2021, 3, 14)))
				.flatMap(o -> o.getProducts().stream())
				.mapToDouble(p -> p.getPrice())
				.average();
		if (resultEx9.isPresent()){
			log.info(resultEx9.toString());
		}
		log.info("Exercise 10");
		log.info("-----------------------------------");
		DoubleSummaryStatistics statistics = productRepos.findAll().stream()
				.filter(p -> p.getCategory().equalsIgnoreCase("Books"))
				.mapToDouble(p -> p.getPrice())
				.summaryStatistics();
		System.out.println(String.format("count = %1d, average = %2$f, max = %3$f, min = %4$f, sum = %5$f",
				statistics.getCount(),
				statistics.getAverage(),
				statistics.getMax(),
				statistics.getMin(),
				statistics.getSum()));
		log.info("Exercise 11");
		log.info("-----------------------------------");
		Map<Long, Integer> resultEx11 = orderRepos.findAll().stream()
				.collect(Collectors.toMap(
						order -> order.getId(),
						order -> order.getProducts().size()
				));
		log.info(resultEx11.toString());
		log.info("Exercise 12");
		log.info("-----------------------------------");
		Map<Customer, List<Order>> resultEx12 = orderRepos.findAll().stream()
				.collect(Collectors.groupingBy(Order::getCustomer));
		log.info(resultEx12.toString());
		log.info("Exercise 13");
		log.info("-----------------------------------");
		Map<Order, Double> resultEx13 = orderRepos.findAll().stream()
				.collect(Collectors.toMap(
						Function.identity(),
						order -> order.getProducts().stream()
								.mapToDouble(p -> p.getPrice()).sum()
				));
		log.info(resultEx13.toString());
		log.info("Exercise 14");
		log.info("-----------------------------------");
		Map<String, List<String>> resultEx14 = productRepos.findAll()
				.stream()
				.collect(Collectors.groupingBy(
						Product::getCategory,
						Collectors.mapping(product -> product.getName(), Collectors.toList())
				));
		log.info(resultEx14.toString());
		log.info("Exercise 15");
		log.info("-----------------------------------");
		Map<String, Optional<Product>> resultEx15 = productRepos.findAll()
				.stream()
				.collect(Collectors.groupingBy(
						Product::getCategory,
						Collectors.maxBy(Comparator.comparing(Product::getPrice))
				));
		log.info(resultEx15.toString());
	}

}
