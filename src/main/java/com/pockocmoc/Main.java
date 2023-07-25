package com.pockocmoc;

import com.pockocmoc.exception.AmountException;
import com.pockocmoc.exception.CustomerException;
import com.pockocmoc.exception.ProductException;
import com.pockocmoc.model.Customer;
import com.pockocmoc.model.Order;
import com.pockocmoc.model.Product;

import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static Order makePurchase(Customer[] customers, Product[] products, int[][] orderDetails) {
        int customerIndex = orderDetails[0][0];
        int productIndex = orderDetails[0][1];
        int quantity = orderDetails[0][2];

        try {
            if (customers == null || customerIndex < 0 || customerIndex >= customers.length || customers[customerIndex] == null) {
                throw new CustomerException("Несуществующий покупатель");
            }

            if (products == null || productIndex < 0 || productIndex >= products.length || products[productIndex] == null) {
                throw new ProductException("Несуществующий товар");
            }

            if (quantity <= 0 || quantity > 100) {
                throw new AmountException("Неверное количество товара");
            }
        } catch (CustomerException | ProductException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (AmountException e) {
            System.out.println(e.getMessage());
            quantity = 1; // Покупаем только 1 товар, если количество неверно
        }

        Customer customer = customers[customerIndex];
        Product product = products[productIndex];

        return new Order(customer, product, quantity);
    }

    public static void main(String[] args) {
        Customer[] customers = new Customer[2];
        customers[0] = new Customer("Иванов Иван", 30, "1234567890");
        customers[1] = new Customer("Петров Петр", 25, "0987654321");

        Product[] products = new Product[5];
        products[0] = new Product("Товар 1", 10.99);
        products[1] = new Product("Товар 2", 20.49);
        products[2] = new Product("Товар 3", 15.99);
        products[3] = new Product("Товар 4", 5.99);
        products[4] = new Product("Товар 5", 12.99);

        Order[] orders = new Order[5];

        int[][] orderDetails = {
                {0, 0, 2}, // Иванов Иван покупает 2 штуки Товара 1
                {1, 3, -5}, // Петров Петр пытается купить -5 штук Товара 4
                {2, 1, 100}, // Несуществующий покупатель пытается купить 100 штук Товара 2
                {1, 6, 3}, // Петров Петр пытается купить несуществующий товар
                {0, 4, 7} // Иванов Иван покупает 7 штук Товара 5
        };

        for (int i = 0; i < orderDetails.length; i++) {
            Order order = makePurchase(customers, products, new int[][]{orderDetails[i]});
            if (order != null) {
                orders[i] = order;
            }
        }

        System.out.println("Количество совершенных покупок: " + Arrays.stream(orders).filter(Objects::nonNull).count());
    }
}