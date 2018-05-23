package model;

/**
 * Created by luiz on 08/06/17.
 */
public class Parameters {

    public static final String TEMPLATE_FILE_PREFIX = "area-template-";

    public static final String STATS_FILE_EXTENSION = "txt";

    public static final String STATS_FILE_EXTENSION_WITH_DOT = "." + STATS_FILE_EXTENSION;

    public static final String IMAGE_FORMATS_REGEX = "([^\\s]+(\\.(?i)(jpg|png))$)";

    public static final String STATS_FORMATS_REGEX = "([^\\s]+(\\.(?i)(" + STATS_FILE_EXTENSION + "))$)";

    public static final String AREA_TEMPLATE_DIMENSION_REGEX = "([\\d]+)([a-zA-Z]+)";

    public static final String AREA_TEMPLATE_DIMENSION_SEPARATOR = "x";

    public static final double MIN_AREA_THRESHOLD = 1000.0;

    public static final boolean IMAGE_DEBUGGING = true;

}
