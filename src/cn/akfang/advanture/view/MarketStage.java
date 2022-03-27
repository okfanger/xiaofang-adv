package cn.akfang.advanture.view;

import cn.akfang.advanture.model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class MarketStage extends Application {


    ObservableList<GoodsProperty> data = FXCollections.observableArrayList();
    ObservableList<GameObjectProperty> playerbox;
    FilteredList<GoodsProperty> filteredData;

    PlayerProperty playerProperty;
    public void begin(Stage stage,PlayerProperty playerProperty,ObservableList<GameObjectProperty> playerbox) throws Exception {
        this.playerProperty=playerProperty;
        this.playerbox = playerbox;
        start(stage);
    }
/*
    public Integer class_id ;
    public String name ;
    public Integer cost ;
    public String effect ;
 */

    @Override
    public void start(Stage stage) throws Exception {

        Game.objectProperty.forEach((key,value)->{
            data.add(new GoodsProperty(key,(String) value.get("name"),(Integer) value.get("cost"),(String) value.get("effect")));
        });

        filteredData = new FilteredList<>(data, obj->true);

        VBox vb= new VBox();

        HBox topPane = new HBox();
        Label nameLabel = new Label("物品名: ");
        TextField tf = new TextField();
        Button search = new Button("查找");

        search.setOnAction(event->{
            filteredData.setPredicate(item->{
                String context = tf.getText();
                if(context.isEmpty()) return true;
                if(context.equals(item.getName()))
                    return true;
                else
                    return false;
            });
        });
        topPane.getChildren().addAll(nameLabel,tf,search);


        TableView tv = new TableView();
        TableColumn<GoodsProperty,Integer> classIdColumn = new TableColumn<>("商品编号");
        TableColumn<GoodsProperty,String> nameColumn = new TableColumn<>("姓名");
        TableColumn<GoodsProperty,Integer> costColumn = new TableColumn<>("价钱");
        TableColumn<GoodsProperty,String> effectColumn = new TableColumn<>("效果");

        TableColumn<GoodsProperty,String> iconColumn = new TableColumn<>();
        TableColumn<GoodsProperty,String> operColumn = new TableColumn<>("操作");

        iconColumn.setCellFactory(col -> new iconColumnForMarketCellFactory(filteredData));
        classIdColumn.setCellValueFactory(new PropertyValueFactory<>("class_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        effectColumn.setCellFactory(col -> new effectColumnForMarketCellFactory(filteredData));

        operColumn.setCellFactory(col-> new operColumnForMarketCellFactory(playerbox,filteredData,playerProperty));

        classIdColumn.setStyle("-fx-alignment: center");
        nameColumn.setStyle("-fx-alignment: center");
        costColumn.setStyle("-fx-alignment: center");
        effectColumn.setStyle("-fx-alignment: center");
        operColumn.setStyle("-fx-alignment: center");


        tv.getColumns().addAll(iconColumn,classIdColumn,nameColumn,costColumn,effectColumn,operColumn);
        tv.setItems(filteredData);

        vb.getChildren().addAll(topPane,tv);

        Scene sc = new Scene(vb,500,500);
        stage.setScene(sc);
        stage.show();




    }
}
