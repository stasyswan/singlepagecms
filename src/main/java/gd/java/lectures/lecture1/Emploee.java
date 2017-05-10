package gd.java.lectures.lecture1;

/**
 * Created by alebedieva on 13.04.17.
 *
 * Создать абстрактный класс Сотрудник.
 * У него должны быть методы для
 * задания ставки (з/п в месяц при 100% отработки),
 * количества отработанных часов,
 * а также методы для
 * получения процента отработанного времени и
 * зарплаты за месяц.
 Сотрудники могут быть двух типов: Программист и Менеджер. Программист получает зарплату в соответствии с количеством отработанных часов, а менеджер - 100% в случае переработки и соответствующий процент в случае недоработки.
 Класс Бухгалтер принимает множество сотрудников и считает их з/п. Для него должно быть прозрачным, какого типа является сотрудник. Ему важна только зарплата за текущий месяц.


 */
public abstract class Emploee {
    private double salary;
    private int hours;
    private int hoursInMonth;

    public Emploee(double salary, int hours, int hoursInMonth) {
        this.salary = salary;
        this.hours = hours;
        this.hoursInMonth = hoursInMonth;
    }

    public abstract double getSalaryPerHours();

    public double hoursPrecentsge(){
        return hours / hoursInMonth;
    }

//------------------- getters -------------------//

    public double getSalary() {
        return salary;
    }

    public int getHours() {
        return hours;
    }

    public int getHoursInMonth() {
        return hoursInMonth;
    }
}


