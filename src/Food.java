// Класс, который используется для параметра-объекта
public class Food {
    private final String name;
    private final int weight;

    public Food(int weight) {
        name = "трава";
        this.weight = weight;
    }

    @Override
    public String toString() {
        return name + ", вес: " + weight + " кг";
    }
}
