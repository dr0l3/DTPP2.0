package marker;

import java.util.Comparator;

/**
 * Created by runed on 22-10-2016.
 */
public class MarkerComparator implements Comparator<Marker2> {
    int contextPoint;

    public MarkerComparator(int contextPoint) {
        this.contextPoint = contextPoint;
    }

    @Override
    public int compare(Marker2 o1, Marker2 o2) {
        int o1ToCaret = Math.abs(o1.getStartOffset() - this.contextPoint);
        int o2ToCaret = Math.abs(o2.getStartOffset() - this.contextPoint);
        return o1ToCaret - o2ToCaret;
    }
}
