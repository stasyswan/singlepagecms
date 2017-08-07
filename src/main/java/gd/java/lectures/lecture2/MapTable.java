package gd.java.lectures.lecture2;

import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by alebedieva on 14.04.17.
 *
 * Разработать класс, позволяющий работать с табличными данными.
 Каждая строка таблицы представляется в виде объекта Map.
 Каждая колонка таблицы характеризуется строковым названием (ключ объекта Map)
 Добавлять строки в данную таблицу можно с помощью метода add(Map map)
 Для данной таблицы должно быть возможно указывать порядок сортировки по заданной колонке
 Должен быть реализован механизм разбиения данной таблицы на страницы с заданным количество строк (должно возвращаться общее количество страниц и получение итератора на данную страницу).
 В таблице должны присутствовать данные, представляющие собой денежные суммы. Должна быть обеспечена возможность ввода/вывода этих данных в фотмате xxx.xxx,dd

 */
public class MapTable {

    private String[] columns;
    private List<LinkedHashMap> rows;

    public MapTable(String... columns){
        this.columns = columns;
        this.rows = new ArrayList<LinkedHashMap>();
    }


    public void add(String... values) throws WrongNumberArgsException {
        if(values.length == columns.length) {
            LinkedHashMap<String, String> row = new LinkedHashMap<>();
            for (int i = 0; i < columns.length; i++) {
                row.put(columns[i], values[i]);
            }
            rows.add(row);
        }
        else {
            throw new WrongNumberArgsException("Wrong number of arguments: columns count " + columns.length);
        }
    }

    public void print(){
        for (String column: columns) {
            System.out.print(column + " ");
        }
        System.out.println();
        for (HashMap row: rows) {
            row.values().forEach(((value)->System.out.print(formatPrice((String) value) + " ")));
            System.out.println();
        }
    }

    public static String formatPrice(String price){
        try {
            BigDecimal bd = new BigDecimal(price);
            NumberFormat summeFormat = NumberFormat.getInstance(Locale.GERMANY);
            summeFormat.setMaximumFractionDigits(2);
            summeFormat.setMinimumFractionDigits(2);
            summeFormat.setGroupingUsed(true);

            return summeFormat.format(bd);
        }
        catch (NumberFormatException ex){
            return price;
        }
    }

    public static Comparator compareBy(String key) {
        return (Comparator<Map<String, String>>) (o1, o2) -> {
            String firstValue = o1.get(key);
            String secondValue = o2.get(key);
            return firstValue.compareTo(secondValue);
        };
    }

    public MapTable paginate(int perPage, int page) throws IndexOutOfBoundsException {
        int pages = pagesCount(perPage);
        MapTable paginated = new MapTable(columns);

        for (int i = 0; i < pages; i++ ){
            int from = i * perPage > rows.size() ? rows.size() : i * perPage;
            int to = i * perPage > rows.size() ? rows.size() : i * perPage + perPage - 1;

            if(i == page)
              paginated.setRows(rows.subList(from, to));

        }

        if(page >= 0 && page <= pages && pages != 0){
            return paginated;
        }
        else{
            throw new IndexOutOfBoundsException("Index out of range: pages count " + pages);
        }
    }

    public int pagesCount (int perPage) {
        return rows.size() == 0 ? 0 : (int) Math.round(rows.size() / perPage + 0.5);
    }


    //------------------------- Getters & Setters -------------------------//

    public List<LinkedHashMap> getRows() {
        return rows;
    }

    public void setRows(List<LinkedHashMap> rows) {
        this.rows = rows;
    }

    //---------------------------------------------------------------------//



    public static void main(String[] args) throws Exception {
        MapTable mt = new MapTable("number", "name", "price");
        try {
            mt.add("#1", "cucumber", "1.554");
            mt.add("#2", "macarons", "67.132,00");
            mt.add("#3", "apple", "2100.4");
            mt.add("#4", "carrot", "1.4");
            mt.add("#5", "juice", "25.5");
            mt.add("#6", "tomato", "24.0");
            mt.add("#7", "potato", "41.4");
//            mt.add("#8", "test", "41.4", "123");
        }
        catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
        }

        List<LinkedHashMap> list = mt.getRows();
        Collections.sort(list , compareBy("name"));

        mt.print();

        System.out.println();

        try {
            mt.paginate(2, 3).print();
        }
        catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
        }

    }
}