package gd.java.lectures.lecture1;

/**
 * Created by alebedieva on 13.04.17.
 */
public class Developer extends Emploee{
    public Developer(double salary, int hours, int hoursInMonth) {
        super(salary, hours, hoursInMonth);
    }

    @Override
    public double getSalaryPerHours() {
        return getSalary() * getHours() / getHoursInMonth();
    }
}

