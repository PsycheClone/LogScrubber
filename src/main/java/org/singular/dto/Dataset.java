package org.singular.dto;

import java.util.ArrayList;
import java.util.List;

public abstract class Dataset<N extends Data> {

    protected List<N> dataset = new ArrayList<N>();

    public List<N> getDataset() {
        return dataset;
    }

    public void setDataset(List<N> dataset) {
        this.dataset = dataset;
    }

    public void addToDataset(N dataset) {
        this.dataset.add(dataset);
    }
}
