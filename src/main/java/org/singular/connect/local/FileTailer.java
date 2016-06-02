package org.singular.connect.local;

import org.apache.commons.io.input.TailerListenerAdapter;
import org.singular.scrubber.Slicer;

import java.io.IOException;

public class FileTailer extends TailerListenerAdapter {

    private Slicer slicer;

    public FileTailer(Slicer slicer) {
        this.slicer = slicer;
    }

    @Override
    public void handle(String line) {
        try {
            slicer.process(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
