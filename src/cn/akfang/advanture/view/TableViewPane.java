package cn.akfang.advanture.view;

import cn.akfang.advanture.model.*;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class TableViewPane extends Pane {

    public TableViewPane(TableView<GameObjectProperty> tableView, ObservableList<GameObjectProperty> data, PlayerProperty player){
        super();

        TableColumn<GameObjectProperty,String> iconColumn = new TableColumn<>("");
        TableColumn<GameObjectProperty,Integer> idColumn = new TableColumn<>("ID");
        TableColumn<GameObjectProperty,String> nameColumn = new TableColumn<>("物品名");
        TableColumn<GameObjectProperty,Integer> costColumn = new TableColumn<>("花费");
        TableColumn<GameObjectProperty,String> effectColumn = new TableColumn<>("效果");
        TableColumn<GameObjectProperty,Integer> amountColumn = new TableColumn<>("数量");
        TableColumn<GameObjectProperty,String> operColumn = new TableColumn<>("操作");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        idColumn.setStyle("-fx-alignment: center");
        nameColumn.setStyle("-fx-alignment: center");
        costColumn.setStyle("-fx-alignment: center");
        effectColumn.setStyle("-fx-alignment: center");
        operColumn.setStyle("-fx-alignment: center");

        effectColumn.setCellFactory(col -> new effectColumnCellFactory(data));
        iconColumn.setCellFactory(col ->new iconColumnCellFactory(data));
        operColumn.setCellFactory((col)->new operColumnCellFactory(data,player));

        tableView.getColumns().addAll(iconColumn,idColumn,nameColumn,costColumn,effectColumn,amountColumn,operColumn);

        tableView.setPrefWidth(750);
        tableView.setPrefHeight(500);
        tableView.setItems(data);

        getChildren().add(tableView);
    }
}