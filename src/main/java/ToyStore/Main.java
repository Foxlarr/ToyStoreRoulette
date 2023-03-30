package ToyStore;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private int weight;

    public Toy(int id, String name, int quantity, int weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

class ToyStore {
    private List<Toy> toys;
    private List<Toy> prizes;

    public ToyStore() {
        toys = new ArrayList<>();
        prizes = new ArrayList<>();
    }

    public void addToy(int id, String name, int quantity, int weight) {
        toys.add(new Toy(id, name, quantity, weight));
    }

    public void changeWeight(int id, int newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == id) {
                toy.setWeight(newWeight);
                break;
            }
        }
    }

    public void drawPrizes(int numPrizes) {
        Random rand = new Random();
        while (numPrizes > 0) {
            int totalWeight = toys.stream().mapToInt(Toy::getWeight).sum();
            int pickedNumber = rand.nextInt(totalWeight) + 1;
            int currentWeight = 0;
            for (Toy toy : toys) {
                currentWeight += toy.getWeight();
                if (currentWeight >= pickedNumber && toy.getQuantity() > 0) {
                    prizes.add(toy);
                    toy.setQuantity(toy.getQuantity() - 1);
                    numPrizes--;
                    break;
                }
            }
        }
    }

    public void getPrize() {
        if (prizes.isEmpty()) {
            System.out.println("No prizes left!");
            return;
        }
        Toy prize = prizes.remove(0);
        try (FileWriter writer = new FileWriter("prizes.txt", true)) {
            writer.write(prize.getName() + "\n");
        } catch (IOException e) {
            System.out.println("Failed to write prize to file!");
            return;
        }
        System.out.println("Congratulations! You won a " + prize.getName());
    }
}

public class Main {
    public static void main(String[] args) {
        ToyStore store = new ToyStore();
        store.addToy(1, "Teddy bear", 10, 10);
        store.addToy(2, "Doll", 5, 20);
        store.addToy(3, "Car", 3, 5);

        store.changeWeight(2, 30);

        store.drawPrizes(2);

        store.getPrize();  // Congratulations! You won a Doll

        store.getPrize();  // Congratulations! You won a Teddy bear

        store.drawPrizes(1);

        store.getPrize();  // Congratulations! You won a Doll

        store.getPrize();  // No prizes left!
    }
}
