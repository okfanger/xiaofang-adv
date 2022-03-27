package cn.akfang.advanture.view;

import cn.akfang.advanture.model.GameObjectProperty;
import cn.akfang.advanture.model.PlayerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TopPane extends VBox {
    PlayerProperty player;
    ObservableList<GameObjectProperty> data;
    HBox hb1 = new HBox();
    ProgressBar hpBar = new ProgressBar();
    ProgressBar mpBar = new ProgressBar();
    SimpleDoubleProperty hpFloatProperty = new SimpleDoubleProperty();
    SimpleDoubleProperty mpFloatProperty = new SimpleDoubleProperty();
    Text moneyText = new Text();
    Label attackLabel = new Label("攻击力 ");
    Text attackText = new Text();

    public TopPane(ObservableList<GameObjectProperty> list,PlayerProperty player){
        super();
        this.data = list;
        this.player=player;

        hpFloatProperty.bind(player.hpProperty());
        mpFloatProperty.bind(player.mpProperty());

        hpBar.progressProperty().bind(hpFloatProperty.divide(100));
        hpBar.setStyle("-fx-accent: red");

        mpBar.progressProperty().bind(mpFloatProperty.divide(100));
        mpBar.setStyle("-fx-accent: blue");

        attackText.textProperty().bind(player.attackProperty().asString());
        moneyText.textProperty().bind(player.moneyProperty().asString().concat("$"));

        hb1.getChildren().addAll(hpBar,mpBar,moneyText,attackLabel,attackText);

        hb1.setStyle("-fx-background-color: white");
        hb1.setPrefHeight(30);
        hb1.setAlignment(Pos.CENTER_LEFT);
        hb1.setPrefWidth(750-250);
        hb1.setSpacing(10);

        Button marketEntry = new Button("商店");
        marketEntry.setOnAction(event->{

            try {
                new MarketStage().begin(new Stage(),player,data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        hb1.getChildren().add(marketEntry);
        this.getChildren().addAll(hb1);
        this.setAlignment(Pos.CENTER);
    }
}
