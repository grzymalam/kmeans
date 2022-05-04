import java.util.ArrayList;
import java.util.List;



public class Cluster implements Comparable<Cluster> {
    private List<Vector> points;
    private Vector centroid;
    private int id;

    public Cluster(int id, Vector centroid) {

        this.id = id;
        this.centroid = centroid;
        this.points = new ArrayList<Vector>();
    }

    public void setCentroid(Vector centroid) {
        this.centroid = centroid;
    }
    public Vector getCenter(){
        return centroid;
    }
    public Vector calcNewCenter() {
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

    public void addPoint(Vector point) {
        points.add(point);
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
        return points.size();
    }
    public void clear(){
        points.clear();
    }
    public void updateCenter(){
        centroid = calcNewCenter();
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
