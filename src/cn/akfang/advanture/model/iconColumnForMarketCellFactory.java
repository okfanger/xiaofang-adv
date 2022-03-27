package cn.akfang.advanture.model;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class iconColumnForMarketCellFactory extends TableCell<GoodsProperty, String> {

    ObservableList<GoodsProperty> data;

    public iconColumnForMarketCellFactory(ObservableList<GoodsProperty> data){
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
            ImageView iv = new ImageView(new Image("./"+data.get(getIndex()).getClass_id()+".png"));
            iv.setFitHeight(50);
            iv.setFitWidth(50);
            this.setGraphic(iv);
        }
    }
}
