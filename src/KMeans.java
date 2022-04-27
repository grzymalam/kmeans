import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KMeans {
    private int k;
    private int maxIterations;
    private List<Vector> vectors;
    private List<Cluster> clusters;

    public KMeans(int k, int maxIterations, List<Vector> vectors) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.vectors = vectors;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void run() {
        clusters = new ArrayList<Cluster>();
        Random random = new Random();
        //generating clusters
        for (int i = 0; i < k; i++) {
            int index = random.nextInt(vectors.size());
            Vector vector = vectors.get(index);
            clusters.add(new Cluster(i, vector));
        }
        //assigning random clusters to each vector
        for (Vector vector : vectors) {
            vector.setCluster(clusters.get(random.nextInt(k)));
        }
        boolean changed = true;
        int iteration = 0;
        while (changed && iteration < maxIterations) {
            changed = false;
            iteration++;
            for (Vector vector : vectors) {
                int bestCluster = clusters.get(0).getId();
                double bestDistance = Double.MAX_VALUE;
                for (int i = 0; i < k; i++) {
                    double distance = vector.dotProduct(clusters.get(i).getCenter());
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestCluster = i;
                    }
                }
                if (vector.getCluster().getId() != bestCluster) {
                    changed = true;
                    vector.setCluster(clusters.get(bestCluster));
                }
            }
            if (changed) {
                for (int i = 0; i < k; i++) {
                    clusters.get(i).clear();
                }
                for (Vector vector : vectors) {
                    clusters.get(vector.getCluster().getId()).add(vector);
                }
                for (int i = 0; i < k; i++) {
                    clusters.get(i).updateCenter();
                }
            }
            //odl od centroidu
            int suma = 0;
            for(Cluster cluster : clusters) {
                for(Vector vector : cluster.getPoints()) {
                    suma += vector.dotProduct(cluster.getCenter());
                }
            }
            System.out.println("Iteration: " + iteration + " Sum: " + suma);
            System.out.println("czystosc: ");
            calculateEachClustersPurity();
        }
    }
    private void calculateEachClustersPurity() {
        ArrayList<String> classes = vectors.stream().map(Vector::getClassName).distinct().collect(Collectors.toCollection(ArrayList::new));
        for(Cluster cluster : clusters) {
            for (String className : classes) {
                int count = 0;
                int percentage = 0;
                for (Vector vector : cluster.getPoints()) {
                    if (vector.getClassName().equals(className)) {
                        count++;
                    }
                }
                percentage = (count * 100) / cluster.getSize();
                System.out.println("Cluster: " + cluster.getId() + " Class: " + className + " Percentage: " + percentage);
            }
        }
    }
}