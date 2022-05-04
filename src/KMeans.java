import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KMeans {
    private int k;
    private double e;
    private int maxIterations = 100;
    private List<Vector> vectors;
    private List<Cluster> clusters;

    public KMeans(int k, List<Vector> vectors, double e) {
        this.k = k;
        this.e = e;
        this.vectors = vectors;
        clusters = new ArrayList<>();
    }


    public void run() {
        Random random = new Random();
        double previousError = Double.MAX_VALUE, currentError = 0;
        // Initialize clusters
        ArrayList<Integer> uniqueInts = getXUniqueInts(k, vectors.size());
        for (int i = 0; i < k; i++) {
            int index = random.nextInt(uniqueInts.get(i));
            Cluster cluster = new Cluster(i, vectors.get(index));
            vectors.get(index).setClusterId(i);
            clusters.add(cluster);
        }
        //assigning random clusters to each vector
        for (Vector vector : vectors) {
            if(vector.getClusterId() == -1) {
                int index = random.nextInt(k);
                vector.setClusterId(index);
                clusters.get(index).addPoint(vector);
            }
        }
        boolean changed = true;
        int iteration = 0;
        while (iteration < maxIterations) {
            changed = false;
            iteration++;

            double suma = 0;
            double sumaKlastra = 0;
            for(Cluster cluster : clusters) {
                for(Vector vector : vectors) {
                    if(vector.getClusterId() == cluster.getId()) {
                        suma += vector.distance(cluster.getCenter());
                        sumaKlastra += vector.distance(cluster.getCenter());
                    }
                }
                System.out.println("Cluster " + cluster.getId() + ": " + sumaKlastra);
                sumaKlastra = 0;
            }
            System.out.println("Iteration: " + iteration + " Suma: " + suma);
            calculateEachClustersPurity();

            if (iteration > 1) {
                currentError = calculateQuantizationError();
                double error = Math.abs((previousError - currentError) / currentError);
                System.out.println("Error: " + error);
                if (error < e) {
                    break;
                }
            }
            previousError = currentError;
            for (Vector vector : vectors) {
                int bestCluster = 0;
                double bestDistance = Double.MAX_VALUE;
                for (int i = 0; i < k; i++) {
                    double distance = vector.distance(clusters.get(i).getCenter());
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestCluster = i;
                    }
                }
                if (vector.getClusterId() != bestCluster) {
                    changed = true;
                    vector.setClusterId(bestCluster);
                }
            }
            if (changed) {
                for (int i = 0; i < k; i++) {
                    clusters.get(i).clear();
                }
                for (Vector vector : vectors) {
                    clusters.get(vector.getClusterId()).add(vector);
                }
                for (int i = 0; i < k; i++) {
                    clusters.get(i).updateCenter();
                }
            }
        }
    }
    private void calculateEachClustersPurity() {
        ArrayList<String> classes = vectors.stream().map(Vector::getClassName).distinct().collect(Collectors.toCollection(ArrayList::new));
        for(Cluster cluster : clusters) {
            for (String className : classes) {
                int count = 0;
                double percentage = 0;
                for (Vector vector : cluster.getPoints()) {
                    if (vector.getClassName().equals(className)) {
                        count++;
                    }
                }
                percentage = cluster.getSize() == 0 ? 0 : (double) count / cluster.getSize();
                System.out.println("Cluster: " + cluster.getId() + " Class: " + className + " Percentage: " + percentage);
            }
        }
    }
    //calculate quantization error
    public double calculateQuantizationError() {
        double sum = 0;
        for (Vector vector : vectors) {
            sum += Math.pow(vector.distance(clusters.get(vector.getClusterId()).getCenter()), 2);
        }
        return sum;
    }
    public ArrayList<Integer> getXUniqueInts(int x, int max) {
        Random random = new Random();
        ArrayList<Integer> uniqueInts = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            int randInt = random.nextInt(max);
            while (uniqueInts.contains(randInt)) {
                randInt = random.nextInt(max);
            }
            uniqueInts.add(randInt);
        }
        return uniqueInts;
    }
}