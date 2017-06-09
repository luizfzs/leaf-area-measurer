package model.filefilter;

import model.Parameters;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by luiz on 09/06/17.
 */
public class AreaTemplateFileFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        return !file.isDirectory() && file.getName().startsWith(Parameters.TEMPLATE_FILE_PREFIX);
    }
}
