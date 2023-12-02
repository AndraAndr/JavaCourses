package mypkg;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Stack;
import java.util.Objects;

public class Account implements Cloneable{
    private String name;
    private HashMap<String, Integer> money;
    private Stack<Account> undo_st;

    private void set_name(String name) {
        if(name != null && !name.isEmpty())this.name = name;
        else throw new IllegalArgumentException("Имя не может быть пустым!");
    }
    private void setUndo() throws CloneNotSupportedException{
        Account a = this.clone();
        a.money = (HashMap)this.money.clone();
        this.undo_st.push(a);
    }
    public Account(String name) {
        this.money=new HashMap<>();
        this.undo_st = new Stack<>();
        set_name(name);

    }

    public void setName(String name) throws CloneNotSupportedException{
        this.setUndo();
        set_name(name);
    }

    public String getName() {
        return this.name;
    }

    public HashMap<String, Integer> getMoney(){
        HashMap<String, Integer> money ;
        money = (HashMap)this.money.clone();
        return money;
    }

    public Integer getMoney(String val) {
        if (this.money.containsKey(val)) {
            return this.money.get(val);
        }
        throw new IllegalArgumentException("Некорректный код валюты! " + val);
    }

    public void setMoney(String val, Integer m) throws CloneNotSupportedException{
        setUndo();
        if(val == null || val.isEmpty()) throw new IllegalArgumentException("Код валюты не может быть пустым! " + val);
        if(val != "RUB" && val != "USD" && val != "EUR") throw new IllegalArgumentException("Некорректный код валюты! " + val);
        if(m < 0) throw new IllegalArgumentException("Количество не может быть отрицательным! " + m);
        money.put(val, m);
    }

    public Account undo() throws CloneNotSupportedException{
        if(this.check_undo() == false) throw new RuntimeException("Нет информации для отката");
        Account a;
        a = (Account)this.undo_st.pop();
        return a.clone();
    }

    public void undo_() throws CloneNotSupportedException, IllegalAccessException{
        if(this.check_undo() == false) throw new RuntimeException("Нет информации для отката");
        Account a;
        a = (Account)this.undo_st.pop();
        Class from = a.getClass();
        Field[] fields =from.getDeclaredFields();
        for (Field f: fields) {
            f.setAccessible(true);
            f.set(this, f.get(a));
        }
    }





    public boolean check_undo(){
        return !undo_st.empty();
    }

    public SaveAccount saveAccount() throws CloneNotSupportedException{
        Account a = this.clone();
        a.money = (HashMap)this.money.clone();
        SaveAccount s = new SaveAccount(a);
        return s;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", money=" + money +
                '}';
    }
    @Override
    public Account clone() throws CloneNotSupportedException {
        return (Account)super.clone();
    }
}
