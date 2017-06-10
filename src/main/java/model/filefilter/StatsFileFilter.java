package model.filefilter;

import model.Parameters;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by luiz on 09/06/17.
 */
public class StatsFileFilter implements FileFilter{
    @Override
    public boolean accept(File file) {
        return file.getName().matches(Parameters.STATS_FORMATS_REGEX);
    }
}
