package cn.akfang.advanture.model;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class iconColumnCellFactory extends TableCell<GameObjectProperty, String> {

    ObservableList<GameObjectProperty> data;

    public iconColumnCellFactory(ObservableList<GameObjectProperty> data){
        this.data=data;
    }
    @Override
    protected void updateItem(String item,boolean empty) {
        super.updateItem(item,empty);
        if (empty) {
            //如果此列为空默认不添加元素
            setText(null);
            setGraphic(null);
        } else {
            ImageView iv = new ImageView(new Image("./"+data.get(getIndex()).getClassId()+".png"));
            iv.setFitHeight(50);
            iv.setFitWidth(50);
            this.setGraphic(iv);
        }
    }
}
