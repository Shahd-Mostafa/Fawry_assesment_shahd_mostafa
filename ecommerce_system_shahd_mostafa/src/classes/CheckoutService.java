package classes;

import java.util.ArrayList;
import java.util.List;

import interfaces.IShippable;

public class CheckoutService {

    public static void checkout(Customer customer, Cart cart) {
        // make sure the cart is not empty
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Can't checkout.cart is empty.");
        }

        double totalPrice = 0;
        double subTotalPrice = 0;
        double shippingFee = 0;
        double totalWeight = 0;

        List<IShippable> shippableProducts = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            // Check if the product is expired
            if (item.isExpired()) {
                throw new IllegalStateException("Can't checkout." + item.getProduct().getName() + " is expired.");
            }
            // Check if the quantity exceeds available stock
            if (item.getProduct().getQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Quantity exceeds available stock for " +
                        item.getProduct().getName() + ".Only " + item.getProduct().getQuantity() + " available.");
            }

            // Calculate subtotal, shipping fee, and total price
            // subtoal price is the sum of all items' prices multiplied by their quantities in cart
            subTotalPrice += item.getProduct().getPrice() * item.getQuantity();

            // Reduce the product quantity in stock
            item.getProduct().setQuantity(item.getProduct().getQuantity() - item.getQuantity());

            if (item.getProduct() instanceof IShippable) {
                IShippable shippableProduct = (IShippable) item.getProduct();
                if (shippableProduct.getWeight() > 0) {
                    for (int i = 0; i < item.getQuantity(); i++) {
                        shippableProducts.add(shippableProduct);
                    }
                    totalWeight += shippableProduct.getWeight() * item.getQuantity();
                }
            }

        }
        shippingFee = totalWeight * 0.05;  // Assuming a shipping fee of 5% of total weight
        totalPrice = subTotalPrice + shippingFee;

        // Check if the customer has sufficient balance
        if (totalPrice > customer.getBalance()) {
            throw new IllegalStateException("Insufficient balance to complete the purchase.");
        }
        customer.reduceCustomerBalance(totalPrice);

        // Process the shipping if there are shippable products and print the shipping notice
        if (!shippableProducts.isEmpty()) {
            ShippingService shippingService = new ShippingService();
            shippingService.ship(shippableProducts);
        } else {
            System.out.println("No shippable products in the cart.");
        }

        // Print the checkout receipt
        System.out.println("** Checkout receipt **");
        for (CartItem item : cart.getItems()) {
            double total = item.getProduct().getPrice() * item.getQuantity();
            System.out.printf("%dx %-15s%.2f%n", item.getQuantity(), item.getProduct().getName(), total);
        }

        System.out.println("-----------------------");
        System.out.printf("%-15s%.2f%n", "Subtotal", subTotalPrice);
        System.out.printf("%-15s%.2f%n", "Shipping", shippingFee);
        System.out.printf("%-15s%.2f%n", "Paid Amount", totalPrice);
        System.out.printf("%-15s%.2f%n", "Customer Balance After Payment\t", customer.getBalance());

        cart.getItems().clear();
    }
}
