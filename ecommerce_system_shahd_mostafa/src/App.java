import java.time.LocalDateTime;

import classes.Cart;
import classes.CheckoutService;
import classes.Customer;
import classes.Product;

public class App {
    public static void main(String[] args) {
        // if any item provides its weight -> shippable
        // if the item is cheese or biscuits -> it's expirable and the expiry date is determined

        // products
        // name price quantity
        Product cheese = new Product("Cheese", 100, 5);
        cheese.setWeight(200);
        // cheese.setExpiryDate(LocalDateTime.now().plusDays(-1)); // this will give expired product exception
        cheese.setExpiryDate(LocalDateTime.now().plusDays(3));

        Product biscuits = new Product("Biscuits", 150, 6);
        biscuits.setWeight(700);
        biscuits.setExpiryDate(LocalDateTime.now().plusDays(2));

        Product tv = new Product("TV", 100, 5);
        tv.setWeight(100);

        Product scratchCard = new Product("Scratch Card", 10, 10);

        // -------------------------------------------------

        // Customer customer = new Customer("Alice", 1000); // would give insufficien balance exception
        Customer customer = new Customer("Alice", 1000);
        Cart cart = new Cart();

        try {
            // commenting out all cart items will give empty cart exception
            cart.add(cheese,2);
            // cart.add(tv, 6); // would give quantity exceeds available stock exception
            cart.add(tv, 3);
            cart.add(scratchCard, 1);
            cart.add(biscuits, 1);
            CheckoutService.checkout(customer, cart);
        } catch (Exception ex) {
            System.out.println("Checkout failed: " + ex.getMessage());
        }
    }
}
