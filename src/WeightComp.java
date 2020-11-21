package ex1.src;

import java.util.Comparator;

public class WeightComp implements Comparator<node_info> {
    @Override
    public int compare(node_info n1, node_info n2) {
        return Double.compare(n1.getTag(), n2.getTag());
        }
    }