package cn.akfang.advanture.model;

import cn.akfang.advanture.mybatis.MyBatis;
import cn.akfang.advanture.view.Game;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

import java.util.HashMap;

public class operColumnForMarketCellFactory extends TableCell<GoodsProperty, String> {

    ObservableList<GoodsProperty> data;
    ObservableList<GameObjectProperty> playerbox;
    PlayerProperty player;

    public operColumnForMarketCellFactory(ObservableList<GameObjectProperty> playerbox,ObservableList<GoodsProperty> data, PlayerProperty player){
        this.playerbox = playerbox;
        this.data=data;
        this.player=player;
    }

    GameObjectProperty checkPlayerBoxExists(int class_id){
        for (GameObjectProperty item:playerbox){
            if(item.getClassId()==class_id){
                return item;
            }
        }
        return null;
    }
    @Override
    protected void updateItem(String item,boolean empty) {
        super.updateItem(item, empty);
        HBox hb = new HBox();
        Button buyButton = new Button("购买");
        hb.getChildren().add(buyButton);
        if (empty) {
            //如果此列为空默认不添加元素
            setText(null);
            setGraphic(null);
        } else {

            GoodsProperty thisItem = data.get(getIndex());

            buyButton.setOnAction(event->{
                int money = player.getMoney();
                if(money<thisItem.getCost()) return;
                player.setMoney(player.getMoney()-thisItem.getCost());
                GameObjectProperty result = checkPlayerBoxExists(thisItem.getClass_id());

                if(result!=null){
                    result.setAmount(result.getAmount()+1);
                    //update
                } else {

                    int feeback_id = MyBatis.insertItemInPlayerBox(player.getPlayer_id(),thisItem.getClass_id(),1);
                    //System.out.println(feeback_id);
                    playerbox.add(new GameObjectProperty(feeback_id,thisItem.getClass_id(),thisItem.getName(),thisItem.getCost(),1));
                }
            });

            this.setGraphic(hb);
        }
    }
}

