package org.singular.connect.local;

import org.apache.commons.io.input.TailerListenerAdapter;
import org.singular.scrubber.TailSlicer;

import java.io.IOException;

public class FileTailer extends TailerListenerAdapter {

    private TailSlicer tailSlicer;

    public FileTailer(TailSlicer tailSlicer) {
        this.tailSlicer = tailSlicer;
    }

    @Override
    public void handle(String line) {
        try {
            tailSlicer.process(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
