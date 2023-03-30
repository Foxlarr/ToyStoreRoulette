package ToyStore;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Optional;
import java.util.stream.*;
import java.nio.file.Paths;





class Toy {
    private int id;
    private String name;
    private int quantity;
    private int maxQuantity;
    public int weight;
    private int numDrawn;

    public Toy(int id, String name, int quantity, int maxQuantity, int weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.maxQuantity = maxQuantity;
        this.weight = weight;
        this.numDrawn = 0;
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

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getWeight() {
        return weight;
    }

    public int getNumDrawn() {
        return numDrawn;
    }

    public void incrementNumDrawn() {
        numDrawn++;
    }

    public boolean canBeDrawnAgain() {
        return numDrawn < maxQuantity;
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

    public void addToy(int id, String name, int quantity, int maxQuantity, int weight) {
        toys.add(new Toy(id, name, quantity, maxQuantity, weight));
    }

    public void changeWeight(int id, int newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == id) {
                toy.weight = newWeight;
                break;
            }
        }
    }

    public void drawPrizes(int numPrizes) {
        Random rand = new Random();
        int numToys = toys.size();
        while (numPrizes > 0 && numToys > 0) {
            int totalWeight = toys.stream().mapToInt(Toy::getWeight).sum();
            int pickedNumber = rand.nextInt(totalWeight) + 1;
            int currentWeight = 0;
            Toy chosenToy = null;
            for (Toy toy : toys) {
                currentWeight += toy.getWeight();
                if (currentWeight >= pickedNumber) {
                    chosenToy = toy;
                    break;
                }
            }
            if (chosenToy == null) {
                continue;
            }
            prizes.add(new Toy(chosenToy.getId(), chosenToy.getName(), 1, 1, chosenToy.getWeight()));
            chosenToy.incrementNumDrawn();
            if (!chosenToy.canBeDrawnAgain()) {
                numToys--;
            }
            numPrizes--;
        }
    }


    public void savePrizes(String fileName) throws IOException {
        this.fileName = fileName;
        FileWriter writer = new FileWriter(fileName);
        for (Toy prize : prizes) {
            writer.write(String.format("%d,%s\n", currentPrizeId++, prize.getName()));
        }
        writer.close();
    }

    public Optional<Toy> getPrize() {
        if (prizeIndex >= prizes.size()) {
            return Optional.empty();
        }
        return Optional.of(prizes.get(prizeIndex++));
    }
}

public class Main {
    public static void main(String[] args) {
        ToyStore store = new ToyStore();
        store.addToy(1, "Barbie", 10, 2, 1);
        store.addToy(2, "Hot Wheels", 20, 3, 2);
        store.addToy(3, "Lego", 20, 4, 3);

        // Изменяем вес id 1
        store.changeWeight(1, 3);

        // Выдаем 10 призов и сохраняем их в файл
        store.drawPrizes(10);
        try {
            store.savePrizes("prizes.txt");
        } catch (IOException e) {
            System.out.println("Не удалось сохранить призы.");
        }

        System.out.println("Сохраненные призы:");
        try (Stream<String> stream = java.nio.file.Files.lines(java.nio.file.Paths.get("prizes.txt"))) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл с призами.");
        }
    }
}


