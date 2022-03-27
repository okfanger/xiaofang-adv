package cn.akfang.advanture;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new cn.akfang.advanture.view.Login().start(stage);
    }
}
