package gd.java.lectures.lecture2;

import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;
import org.junit.jupiter.api.*;

import static gd.java.lectures.lecture2.MapTable.formatPrice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Created by alebedieva on 18.04.17.
 */

@DisplayName("Unit tests for MapTable class")
class MapTableTest implements TimeExecutionLogger {
    private MapTable mt;

    @BeforeEach
    @DisplayName("Initialize new MapTable object")
    void setUp(TestInfo testInfo, TestReporter testReporter) {
        mt = new MapTable("number", "name", "price");
    }

    @Test
    @DisplayName("it adds new row with correct number of params to MapTable object")
    void testAddSuccsess() throws WrongNumberArgsException {
        assertEquals(0, mt.getRows().size(), "mt.getRows() must be 0");
        mt.add("#1", "cucumber", "1.554");
        assertEquals(1, mt.getRows().size(), "mt.getRows() must be 1");

    }

    @Test
    @DisplayName("it doesn't add new row with incorrect number of params to MapTable object")
    void testAddFailure() throws WrongNumberArgsException {
        assertEquals(0, mt.getRows().size(), "mt.getRows() must be 0");
        assertThrows(WrongNumberArgsException.class, () -> mt.add("#1", "cucumber", "1.554", "extra param"));
        assertEquals(0, mt.getRows().size(), "mt.getRows() must be 0");
    }

    @Test
    @DisplayName("it formats numbers in GERMANY locale")
    void testFormatPriceSuccsess() {
        assertEquals("10.000,02",formatPrice("10000.023"));
    }

    @Test
    @DisplayName("it returns string without changes if can't to cast string to BigDecimal")
    void testFormatPriceFailure() {
        assertEquals("test123",formatPrice("test123"));
    }


    @Test
    @DisplayName("it paginates MapTable object and returns current page")
    void testPaginateSuccsess() throws WrongNumberArgsException, IndexOutOfBoundsException {
        mt.add("#1", "cucumber", "1.554");
        mt.add("#2", "apple", "12.59");
        mt.add("#3", "banana", "16.14");

        MapTable paginatedMt = new MapTable("number", "name", "price");
        paginatedMt.add("#3", "banana", "16.14");

        assertEquals( mt.paginate(2, 1).getRows(), paginatedMt.getRows());
    }

    @Test
    @DisplayName("it paginates MapTable object and throws IndexOutOfBoundsException if current page not in allowed bounds")
    void testPaginateFailure() throws WrongNumberArgsException, IndexOutOfBoundsException {
        assertThrows(IndexOutOfBoundsException.class, () -> mt.paginate(2, -7));
        assertThrows(IndexOutOfBoundsException.class, () -> mt.paginate(2, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> mt.paginate(2, 7));
    }

    @Test
    @DisplayName("it returns pages count")
    void testPagesCount() throws WrongNumberArgsException {
        assertEquals( mt.pagesCount(2), 0);
        mt.add("#1", "cucumber", "1.554");
        mt.add("#2", "apple", "12.59");
        mt.add("#3", "banana", "16.14");
        assertEquals( mt.pagesCount(2), 2);
    }
}

