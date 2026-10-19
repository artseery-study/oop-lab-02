public class Main {
    public static void main(String[] args) {
        Zebra zebra = new Zebra();
        AnnotationMethodInvoker.invokeAnnotatedMethods(zebra);
    }
}
