package cn.akfang.advanture.view;

import cn.akfang.advanture.model.PlayerProperty;
import cn.akfang.advanture.mybatis.MyBatis;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import cn.akfang.advanture.model.GameObjectProperty;
import cn.akfang.advanture.view.*;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game extends Application {

    /* 静态数据域 */
    public static HashMap<String,String> alias = MyBatis.getAliasFromDatabase();
    public static HashMap<Integer,HashMap<String,Object>> objectProperty = MyBatis.getObjectPropertyFromDatabase();
    /* 实例数据域 */

    Map player;
    PlayerProperty playerProperty;
    ObservableList<GameObjectProperty> data = FXCollections.observableArrayList();
    TableView<GameObjectProperty> tableView = new TableView<>();

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void begin(Stage stage, Map player) {
        this.player = player;
        this.playerProperty = new PlayerProperty(
                (Integer) player.get("player_id"),
                (String)player.get("nick_name"),
                (String)player.get("password")
                );
        start(stage);
    }

    @Override
    public void start(Stage stage) {

        getPlayerFromDatabase();  //获取玩家信息

        BorderPane bp =new BorderPane();
        TopPane topPane = new TopPane(data,playerProperty);
        TableViewPane tableViewPane = new TableViewPane(tableView,data,playerProperty);
        bp.setTop(topPane);
        bp.setCenter(tableViewPane);

        Scene sc = new Scene(bp,700,500);
        stage.setScene(sc);
        stage.show();
    }


    public void getPlayerFromDatabase(){
        try (SqlSession session = MyBatis.factory.openSession()){
            {
                List<Map> resultList = session.selectList("getObjectBox", new HashMap<>() {{
                    put("owner_id", player.get("player_id"));
                }});
                resultList.forEach(item -> {
                    int object_id = Integer.parseInt(String.valueOf(item.get("object_id")));
                    int class_id = Integer.parseInt(String.valueOf(item.get("class_id")));
                    String name = (String) (objectProperty.get(class_id).get("name"));
                    int cost = Integer.parseInt(String.valueOf(objectProperty.get(class_id).get("cost")));
                    int amount = Integer.parseInt(String.valueOf(item.get("amount")));
                    data.add(new GameObjectProperty(object_id, class_id, name, cost, amount));
                });
            }
            {
                List<Map> resultList = session.selectList("getPlayerCondition", new HashMap<>() {{
                    put("player_id", player.get("player_id"));
                }});
                resultList.forEach(item->{
                    this.playerProperty.setHp((Integer) item.get("hp"));
                    this.playerProperty.setMp((Integer) item.get("mp"));
                    this.playerProperty.setMoney((Integer) item.get("money"));
                    this.playerProperty.setAttack((Integer) item.get("attack"));
                });
            }
        }
    }
}
