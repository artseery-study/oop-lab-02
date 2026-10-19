public class Zebra {
    @Repeat(2)
    public void greet(String name) {
        System.out.println("Зебру зовут " + name);
    }

    public void walk(int minutes) {
        System.out.println("Зебра гуляет " + minutes + " минут");
    }

    @Repeat(2)
    protected void eat(Food food) {
        System.out.println("Зебра ест: " + food);
    }

    @Repeat(3)
    protected void run(String place, int minutes) {
        System.out.println("Зебра бежит. Место: " + place + ", время: " + minutes + " минут");
    }

    protected void drink(double liters) {
        System.out.println("Зебра пьёт " + liters + " литров воды");
    }

    @Repeat(2)
    private void sleep(int hours, boolean inShade) {
        System.out.println("Зебра спит " + hours + " часов, в тени: " + inShade);
    }

    @Repeat(4)
    private void hide(String place, double distance, char herdMark) {
        System.out.println("Зебра прячется. Место: " + place
                + ", расстояние: " + distance + " м, метка стада: " + herdMark);
    }

    private void rest(int minutes) {
        System.out.println("Зебра отдыхает " + minutes + " минут");
    }
}
