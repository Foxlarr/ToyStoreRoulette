package ToyStore;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Optional;

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
    private int currentPrizeId;
    private String fileName;
    private int prizeIndex;

    public ToyStore() {
        toys = new ArrayList<>();
        prizes = new ArrayList<>();
        currentPrizeId = 1;
        fileName = "";
        prizeIndex = 0;
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
        Optional<Toy> optionalPrize = prizes.stream()
                .skip(prizeIndex)
                .filter(p -> p.getQuantity() > 0)
                .findFirst();
        if (optionalPrize.isPresent()) {
            Toy prize = optionalPrize.get();
            prizes.remove(prize);
            try (FileWriter writer = new FileWriter("prizes_" + currentPrizeId + ".txt", false)) {
                writer.write(prize.getName() + "\n");
                fileName = "prizes_" + currentPrizeId + ".txt";
                currentPrizeId++;
                prizeIndex++;
            } catch (IOException e) {
                System.out.println("Failed to write prize to file!");
                return;
            }
            System.out.println("Congratulations! You won a " + prize.getName());
        } else {
            System.out.println("You have already won all available prizes!");
        }
    }

    public String getFileName() {
        return fileName;
    }
}

public class Main {
    public static void main(String[] args) {
        ToyStore store = new ToyStore();
        store.addToy(1, "Teddy bear", 5, 3);
        store.addToy(2, "Race car", 3, 2);
        store.addToy(3, "Doll", 2, 1);
        store.addToy(4, "LEGO", 4, 4);
        store.addToy(5, "Action figure", 3, 3);
        store.addToy(6, "Puzzle", 2, 2);
        store.addToy(7, "Board game", 5, 4);
        store.addToy(8, "Art set", 4, 3);
        store.addToy(9, "Transformer", 4, 4);

        store.changeWeight(1, 5);

        store.drawPrizes(10);
        for (int i = 0; i < 10; i++) {
            store.getPrize();
            System.out.println("File name: " + store.getFileName());
        }
    }
}
