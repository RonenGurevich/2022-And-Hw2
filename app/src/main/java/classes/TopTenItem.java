package classes;

public class TopTenItem {

    private int score;
    private double lat;
    private double lon;

    public TopTenItem(int score, double lat, double lon)
    {
        this.score = score;
        this.lat = lat;
        this.lon = lon;
    }

    public int getScore() {
        return score;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "TopTenItem{" +
                "score=" + score +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
