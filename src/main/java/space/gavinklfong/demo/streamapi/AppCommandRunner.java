package space.gavinklfong.demo.streamapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
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

	}

}
