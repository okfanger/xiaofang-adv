package cn.akfang.advanture.model;

import cn.akfang.advanture.view.Game;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;

import java.util.Iterator;
import java.util.Map;

public class effectColumnCellFactory extends TableCell<GameObjectProperty, String> {

    ObservableList<GameObjectProperty> data;

    public effectColumnCellFactory(ObservableList<GameObjectProperty> data){
        this.data=data;
    }
    @Override
    protected void updateItem(String item,boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            //如果此列为空默认不添加元素
            setText(null);
            setGraphic(null);
        } else {
            String ans = "";
            Iterator<Map.Entry<String,Object>> i = Game.objectProperty.get(data.get(getIndex()).getClassId()).entrySet().iterator();
            if (!i.hasNext()) {
                setText(null);
                setGraphic(null);
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (;;) {
                if (! i.hasNext()) {
                    setText(sb.append(' ').toString());
                    setGraphic(null);
                    return;
                }

                Map.Entry<String,Object> e = i.next();

                String key = e.getKey();
                if(key.equals("name")||key.equals("cost")) continue;
                Integer value = Integer.parseInt(String.valueOf(e.getValue()));
                sb.append(Game.alias.get(key));
                if(value<0) sb.append("");
                else sb.append("+");
                sb.append(value);
                if (! i.hasNext()) {
                    setText(sb.append(' ').toString());
                    setGraphic(null);
                    return;
                }
                sb.append(',').append(' ');
            }

        }
    }
}
