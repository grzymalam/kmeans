import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int k = 3;
        double e = 0.001;
        Loader loader = new Loader();
        ArrayList<Vector> points = loader.load("src/data.txt");
        KMeans kmeans = new KMeans(k, points, e);
        kmeans.run();
    }
}
