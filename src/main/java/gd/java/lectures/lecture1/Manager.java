package gd.java.lectures.lecture1;

/**
 * Created by alebedieva on 13.04.17.
 */
public class Manager extends Emploee {
    public Manager(double salary, int hours, int hoursInMonth) {
        super(salary, hours, hoursInMonth);
    }

    @Override
    public double getSalaryPerHours() {
        return getHours() > getHoursInMonth() ? getSalary() :  getSalary() * getHours() / getHoursInMonth();
    }
}
