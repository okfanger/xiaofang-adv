package cn.akfang.advanture.model;

import cn.akfang.advanture.mybatis.MyBatis;
import javafx.beans.property.*;

public class GameObjectProperty {
    /* 物品品类编号 */
    IntegerProperty classId = new SimpleIntegerProperty();
    IntegerProperty id = new SimpleIntegerProperty();
    StringProperty name = new SimpleStringProperty();
    IntegerProperty cost = new SimpleIntegerProperty();
    IntegerProperty amount = new SimpleIntegerProperty();
    public GameObjectProperty(int id,int classId, String name, int cost,int amount){
        this.id.set(id);
        this.classId.set(classId);
        this.name.set(name);
        this.cost.set(cost);
        this.amount.set(amount);
    }

    public int getClassId() {
        return classId.get();
    }

    public IntegerProperty classIdProperty() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId.set(classId);
    }

    public int getAmount() {
        return amount.get();
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        if(amount<0) return;
        MyBatis.updateObjectAmount(getId(),amount);
        this.amount.set(amount);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getCost() {
        return cost.get();
    }

    public IntegerProperty costProperty() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost.set(cost);
    }
}
