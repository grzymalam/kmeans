import java.util.ArrayList;
import java.util.List;



public class Cluster implements Comparable<Cluster> {
    private List<Vector> points;
    private Vector centroid;
    private int id;
    private int size;

    public Cluster(int id, Vector centroid) {

        this.id = id;
        this.centroid = centroid;
        this.points = new ArrayList<Vector>();
    }

    public void setCentroid(Vector centroid) {
        this.centroid = centroid;
    }
    public Vector getCenter(){
        ArrayList<Double> center = new ArrayList<Double>();
        for(int i = 0; i < centroid.getSize(); i++){
            double sum = 0;
            for(int j = 0; j < points.size(); j++){
                sum += points.get(j).get(i);
            }
            center.add(sum/points.size());
        }
        return new Vector(center);
    }
    public Vector getCentroid() {
        return centroid;
    }

    public void addPoint(Vector point) {
        points.add(point);
        size++;
    }
    public List<Vector> getPoints() {
        return points;
    }
    public Vector get(int index) {
        return points.get(index);
    }
    public void add(Vector point){
        points.add(point);
    }
    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
    public void clear(){
        points.clear();
        size = 0;
    }
    public void updateCenter(){
        centroid = getCenter();
    }
    @Override
    public int compareTo(Cluster o) {
        for (int i = 0; i < this.getSize(); i++) {
            if (this.get(i).compareTo(o.get(i)) != 0) {
                return this.get(i).compareTo(o.get(i));
            }
        }
        return 0;
    }
}
