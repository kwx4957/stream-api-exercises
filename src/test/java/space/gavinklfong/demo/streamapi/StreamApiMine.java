package space.gavinklfong.demo.streamapi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    public void getProductByCustomerTier2Btw01F2021and01Ap2021(){
//        List<Product> productList = orderRepo.findAll();
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
//    @Test
//    public void getOrder(){
//        List<Order> result = orderRepo.findAll()
//                .stream()
//                .filter(order -> order.getOrderDate().isEqual(new LocalDAtetime(03,15,2021))
//    }
    //problem 8
    //problem 9
    //problem 10
    //problem 11
    //problem 12
    //problem 13
    //problem 14
    //problem 15
}
