package cn.akfang.advanture.model;

import cn.akfang.advanture.mybatis.MyBatis;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayerProperty {
    IntegerProperty player_id = new SimpleIntegerProperty();
    StringProperty nick_name = new SimpleStringProperty();
    StringProperty password = new SimpleStringProperty();
    IntegerProperty hp = new SimpleIntegerProperty();
    IntegerProperty mp = new SimpleIntegerProperty();
    IntegerProperty money = new SimpleIntegerProperty();
    IntegerProperty attack = new SimpleIntegerProperty();

    public int getAttack() {
        return attack.get();
    }

    public IntegerProperty attackProperty() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack.set(attack);
    }

    public int getMoney() {
        return money.get();
    }

    public IntegerProperty moneyProperty() {
        return money;
    }

    public void setMoney(int money) {
        if(money<0) return;
        MyBatis.updatePlayerConditionMoney(getPlayer_id(),money);
        this.money.set(money);
    }

    public int getPlayer_id() {
        return player_id.get();
    }

    public IntegerProperty player_idProperty() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id.set(player_id);
    }

    public String getNick_name() {
        return nick_name.get();
    }

    public StringProperty nick_nameProperty() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name.set(nick_name);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public int getHp() {
        return hp.get();
    }

    public IntegerProperty hpProperty() {
        return hp;
    }

    public void setHp(int hp) {
        if(hp<0) return;
        MyBatis.updatePlayerConditionHp(getPlayer_id(),hp);
        this.hp.set(hp);
    }

    public int getMp() {
        return mp.get();
    }

    public IntegerProperty mpProperty() {
        return mp;
    }

    public void setMp(int mp) {
        if(mp<0) return;
        MyBatis.updatePlayerConditionMp(getPlayer_id(),mp);
        this.mp.set(mp);
    }

    public PlayerProperty(int player_id, String nick_name, String password) {
        this.player_id.set(player_id);
        this.nick_name.set(nick_name);
        this.password.set(password);
    }
}
