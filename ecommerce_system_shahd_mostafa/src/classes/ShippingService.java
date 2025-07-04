package classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.IShippable;

public class ShippingService {

    public void ship(List<IShippable> shippableProducts) {
        System.out.println("** Shipment Notice **");
        double totalWeight = 0;

        // mapping to group shippable products by name
        Map<String, ProductSummary> groupedProducts = new HashMap<>();

        for (IShippable product : shippableProducts) {
            String name = product.getName();
            double weight = product.getWeight();

            if (groupedProducts.containsKey(name)) {
                ProductSummary summary = groupedProducts.get(name);
                summary.count++;
            } else {
                groupedProducts.put(name, new ProductSummary(1, weight));
            }

            totalWeight += weight;
        }

        for (Map.Entry<String, ProductSummary> item : groupedProducts.entrySet()) {
            String name = item.getKey();
            ProductSummary summary = item.getValue();
            System.out.printf("%dx %-15s%.2fg%n", summary.count, name, summary.count * summary.weight);
        }

        totalWeight = totalWeight / 1000; // convert grams to kilograms
        System.out.printf("Total package weight: %.2fkg%n%n", totalWeight);
    }

    // gets weight and count of each product
    private static class ProductSummary {
        int count;
        double weight;

        ProductSummary(int count, double weight) {
            this.count = count;
            this.weight = weight;
        }
    }
}