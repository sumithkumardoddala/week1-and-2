import java.util.*;

public class FlashSale {

    HashMap<String, Integer> inventory = new HashMap<>();
    HashMap<String, Queue<Integer>> waitingList = new HashMap<>();

    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }
    public int checkStock(String productId) {

        if (!inventory.containsKey(productId)) {
            return 0;
        }

        return inventory.get(productId);
    }
    public synchronized void purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {

            stock--;
            inventory.put(productId, stock);

            System.out.println("Purchase Success for User " + userId +
                    ". Remaining stock: " + stock);

        } else {

            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);

            System.out.println("Stock unavailable. User " + userId +
                    " added to waiting list. Position #" + queue.size());
        }
    }
    public void showWaitingList(String productId) {

        Queue<Integer> queue = waitingList.get(productId);

        System.out.println("Waiting List for " + productId + ": " + queue);
    }
    public static void main(String[] args) {

        FlashSale store = new FlashSale();

        store.addProduct("IPHONE15_256GB", 3);

        System.out.println("Stock: " + store.checkStock("IPHONE15_256GB"));

        store.purchaseItem("IPHONE15_256GB", 12345);
        store.purchaseItem("IPHONE15_256GB", 67890);
        store.purchaseItem("IPHONE15_256GB", 22222);
        store.purchaseItem("IPHONE15_256GB", 99999);
        store.purchaseItem("IPHONE15_256GB", 88888);

        store.showWaitingList("IPHONE15_256GB");
    }
}
