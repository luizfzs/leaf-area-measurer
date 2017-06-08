import org.bytedeco.javacv.JavaCV;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by luiz on 08/06/17.
 */
public class TestJavaCV {

    @Test
    public void shouldLoadJavaCV(){
        assertEquals("Incorrect value for JavaCV.SQRT2", JavaCV.SQRT2, JavaCV.SQRT2, 0.1);
    }
}
