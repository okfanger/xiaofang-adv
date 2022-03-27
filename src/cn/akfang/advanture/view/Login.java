package cn.akfang.advanture.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import cn.akfang.advanture.mybatis.MyBatis;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends Application {

    Stage thisStage;

    GridPane gp = new GridPane();
    Label[] labels = new Label[10];
    String[] labelCaption= {"用户名"};
    TextField[] tf = new TextField[10];
    PasswordField pf = new PasswordField();
    Button[] bt = new Button[2];
    String[] btCaption = {"登录","重置"};
    HBox hb = new HBox();

    @Override
    public void start(Stage stage) throws Exception {

        thisStage = stage;

        setLayout();
        addListener();

        stage.setTitle("登录");
        stage.setResizable(false);
        Scene sc= new Scene(gp,300,100);
        stage.setScene(sc);
        stage.show();
    }

    private void addListener(){
        bt[0].setOnAction(event->{

            if(tf[0].getText().isEmpty() || pf.getText().isEmpty()){
                tf[0].setText("admin");
                pf.setText("admin");
                //return;
            }
            /* 数据库里查询 */
            try (SqlSession session = MyBatis.factory.openSession()){

                List<Map> result = session.selectList(
                        "reg.checkUserExists",
                        new HashMap<>(){{
                            put("nick_name",tf[0].getText());
                        }}
                );

                if(result.size()!=1){
                    new Alert(Alert.AlertType.WARNING,"未找到对应的用户名").show();
                } else {
                    String truePassword = (String) result.get(0).get("password");
                    if(truePassword.equals(pf.getText())){
                        thisStage.close();
                        new Game().begin(this.thisStage,result.get(0));
                    } else {
                        new Alert(Alert.AlertType.WARNING,"密码错误！").show();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

        bt[1].setOnAction(event->{
            pf.clear();
            for (int i=0;i<labelCaption.length;i++) {
                tf[i].clear();
            }
        });
    }

    private void setLayout(){

        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(11.5,12.5,13.5,14.5));
        gp.setHgap(5.5);
        gp.setVgap(5.5);

        for (int i=0;i<labelCaption.length;i++){
            labels[i]=new Label(labelCaption[i]);
            tf[i] = new TextField("");
            gp.add(labels[i],0,i);
            gp.add(tf[i],1,i);
        }

        gp.add(new Label("密码"),0,labelCaption.length);
        gp.add(pf,1,labelCaption.length);


        hb.setAlignment(Pos.CENTER);

        for (int i=0;i<btCaption.length;i++){
            bt[i]=new Button(btCaption[i]);
            hb.getChildren().add(bt[i]);
            HBox.setMargin(bt[i],new Insets(0,10,0,0));
        }

        gp.add(hb,0,labelCaption.length+1,2,1);

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

