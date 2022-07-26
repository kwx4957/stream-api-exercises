package space.gavinklfong.demo.streamapi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// https://blog.devgenius.io/15-practical-exercises-help-you-master-java-stream-api-3f9c86b1cf82
@Slf4j
@DataJpaTest
public class StreamApiMine {
    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    // 문제 1번
    @Test
    public void obtainProductBelongsCategoryOver100(){
        List<Product> productList =productRepo.findAll()
                .stream()
                .filter(product -> product.getCategory().equals("Books"))
                .filter(product -> product.getPrice() > 100)
                .collect(Collectors.toList());

        productList.forEach(p-> log.info(p.toString()));
    }

    //문제 2번
    @Test
    public void getOrderBelongCategoryBaby(){
        List<Order> orderList = orderRepo.findAll()
                .stream()
                .filter(order -> order.getProducts()
                        .stream()
                        .anyMatch(product -> product.getCategory().equals("baby"))
                )
                .collect(Collectors.toList());
    }

    // 문제 3번
    @Test
    public void getProductCategorytoysAndDisccount10(){
        List<Product> productList = productRepo.findAll()
                .stream()
                .filter(product -> product.getCategory().equals("Baby"))
                .map(product -> product.withPrice(product.getPrice()*0.9))
                .collect(Collectors.toList());

        productList.forEach(product -> log.info(product.toString()));
    }

    //문제 4번
    @Test
    public void getProductsByCustomerT2btwFebtoApr(){
        List<Product> re = orderRepo.findAll().stream()
                .filter(order -> order.getCustomer().getTier()==2)
                .filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021,2,1)))
                .filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021,4,1)))
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .collect(Collectors.toList());

        re.forEach(z->log.info(z.toString()));
    }

    //problem 5
    @Test
    public void getCheapestProuctCategoryInBooks(){
        Optional<Product> cheapestProduct = productRepo.findAll().stream()
                .filter(product -> product.getCategory().equals("Books"))
                .min(Comparator.comparing(Product::getPrice));
        log.info(cheapestProduct.toString());
    }

    //problem 6
    @Test
    public void getThreeMostRecentPlacedOrder(){
        List<Order> result = orderRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .collect(Collectors.toList());

        result.forEach(order -> log.info(order.toString()));
    }

    //problem 7
    @Test
    public void getOrder(){
        List<Product> result = orderRepo.findAll()
                .stream()
                .filter(order -> order.getOrderDate().isEqual(LocalDate.parse("2021-03-15")))
                .peek(order -> log.info(order.toString()))
                .flatMap(order -> order.getProducts().stream())
                .collect(Collectors.toList());

        result.forEach(product -> log.info(product.toString()));

    }

    //problem 8
    @Test
    public void getOrderCalculateTotalLumpSum(){
        Double result = orderRepo.findAll()
                .stream()
                .filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021,2,1)))
                .filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021,3,1)))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .sum();
        System.out.println(result);
    }

    //problem 9
    @Test
    public void getAvgPrice(){
        Double result = orderRepo.findAll()
                .stream()
                .filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021,3,15)))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .average().getAsDouble();

        System.out.println(result);

    }

    //problem 10
    @Test
    public void get(){
        DoubleSummaryStatistics result = productRepo.findAll().stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Books"))
                .mapToDouble(Product::getPrice)
                .summaryStatistics();

        System.out.printf("count = %1$d, average = %2$f, max = %3$f, min = %4$f, sum = %5$f%n",
                result.getCount(), result.getAverage(), result.getMax(), result.getMin(), result.getSum());
    }

    //problem 11
    @Test
    public void getMapOredrIdandProductCount(){
        Map<Long,Integer> re = orderRepo.findAll()
                .stream()
                .collect(
                        Collectors.toMap(Order::getId,
                                order -> order.getProducts().size()));

        System.out.println(re.toString());
    }

    //problem 12
    @Test
    public void getOrderGroupByCustomer(){
        Map<Customer,List<Order>> re = orderRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(Order::getCustomer));

        System.out.println(re.toString());
    }

    //problem 13
    @Test
    public void getMapOrderRecordAndProductTotleSum(){
        Map<Object,Double> re = orderRepo.findAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                z->z, order -> order.getProducts().stream()
                                        .mapToDouble(Product::getPrice)
                                        .sum())
                );

        System.out.println(re.toString());
    }

    //problem 14
    @Test
    public void getListProductNameByCategory(){
        Map<String,List<String>> re= productRepo.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Product::getCategory,
                                Collectors.mapping(Product::getName, Collectors.toList())));

        System.out.println(re.toString());
    }

    //problem 15
    @Test
    public void getMostExpensiveProductByCategory(){
        Map<String, Optional<Product>> result = productRepo.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Product::getCategory,
                                Collectors.maxBy(
                                        Comparator.comparingDouble(Product::getPrice))));
        System.out.println(result.toString());
    }
}
