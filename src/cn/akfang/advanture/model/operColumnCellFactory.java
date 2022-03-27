package cn.akfang.advanture.model;

import cn.akfang.advanture.mybatis.MyBatis;
import cn.akfang.advanture.view.Game;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class operColumnCellFactory extends TableCell<GameObjectProperty, String> {

    ObservableList<GameObjectProperty> data;
    PlayerProperty player;
    public operColumnCellFactory(ObservableList<GameObjectProperty> data,PlayerProperty player){
        this.data=data;
        this.player=player;
    }
    @Override
    protected void updateItem(String item,boolean empty) {
        super.updateItem(item, empty);
        VBox vb = new VBox();

        HBox hb1 = new HBox();
        HBox hb2 = new HBox();
        Button plus = new Button("+");
        Button minus = new Button("-");

        vb.getChildren().addAll(hb1,hb2);
        if (empty) {
            //如果此列为空默认不添加元素
            setText(null);
            setGraphic(null);
        } else {

//
//            TextField tf = new TextField();
//            IntegerProperty thisItem = data.get(getIndex()).amountProperty();
//
//            //tf.textProperty().bind(thisItem.asString());
//            tf.textProperty().set("0");
//            tf.setPrefWidth(30);
//            hb.getChildren().addAll(minus,tf,plus);
//
//            minus.setOnAction(event->{
//            });
//            plus.setOnAction(event->{
//            });


            GameObjectProperty thisItem = data.get(getIndex());

            /* hb1 */

            Button[] bt = new Button[10];
            String[] names = {"使用","出售"};

            for (int i=0;i<names.length;i++){
                bt[i]=new Button(names[i]);
                hb1.getChildren().add(bt[i]);
            }

            bt[0].setOnAction(event->{

                if(thisItem.getAmount()==0){
                    return;
                }
                HashMap<String, Object> usage = Game.objectProperty.get(thisItem.getClassId());
                for (String key : usage.keySet()) {
                    if (key.equals("attack")) player.setAttack(player.getAttack() + (Integer) usage.get("attack"));
                    if (key.equals("hp")) {
                        int after = player.getHp() + (Integer) usage.get("hp");
                        if (after < 0) {
                            new Alert(Alert.AlertType.WARNING, "生命值不足!").show();
                            return;
                        }
                        player.setHp(after);
                    }
                    if (key.equals("mp")) {
                        int after = player.getMp() + (Integer) usage.get("mp");
                        if (after < 0) {
                            new Alert(Alert.AlertType.WARNING, "法力值不足!").show();
                            return;
                        }
                        player.setMp(player.getMp() + (Integer) usage.get("mp"));
                    }
                }
                thisItem.setAmount(thisItem.getAmount() - 1);
                //System.out.println(player.mpProperty().get());
            });

bt[1].setOnAction(event->{
    if(thisItem.getAmount()==0){
        return;
    }
    thisItem.setAmount(thisItem.getAmount()-1);
    player.setMoney(player.getMoney()+thisItem.getCost());
});

            /* hb2 */
            Button bt33 = new Button("移除");
            this.setGraphic(bt33);
            bt33.setOnAction(event->{
                MyBatis.deleteNullItemInPlayerBox(thisItem.getId(),player.getPlayer_id());
                data.remove(getIndex());
            });

            hb2.getChildren().addAll(bt33);
            this.setGraphic(vb);
        }
    }
}

