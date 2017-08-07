package gd.java.lectures.lecture1;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by alebedieva on 13.04.17.
 */
public class Accountant {

    public static void main(String[] args) throws Exception {
        DecimalFormat df = new DecimalFormat("0.00");
        Emploee dev1 = new Developer(1500, 175, 170);
        Emploee dev2 = new Developer(2000, 120, 170);
        Emploee dev3 = new Developer(500, 200, 170);
        Emploee man1 = new Manager(2500, 168, 170);
        Emploee man2 = new Manager(1300, 189, 170);

        ArrayList<Emploee> emploees = new ArrayList<Emploee>();
        emploees.add(dev1);
        emploees.add(dev2);
        emploees.add(dev3);
        emploees.add(man1);
        emploees.add(man2);

        emploees.forEach((emploee)->System.out.println(df.format(emploee.getSalaryPerHours())));
    }
}