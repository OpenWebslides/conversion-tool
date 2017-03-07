package openwebslides.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PPT {

    //metadata
    private List<Slide> slides;
    private File ppt;

    public PPT(File file) {
        ppt = file;
        slides = new ArrayList<>();
    }

    public String toHTML() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
