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

public class Register extends Application {

    GridPane gp = new GridPane();
    Label[] labels = new Label[10];
    String[] labelCaption= {"昵称"};
    TextField[] tf = new TextField[10];
    PasswordField pf = new PasswordField();
    Button[] bt = new Button[2];
    String[] btCaption = {"注册","重置"};
    HBox hb = new HBox();

    @Override
    public void start(Stage stage) throws Exception {

        setLayout();
        addListener();

        stage.setTitle("注册");
        stage.setResizable(false);
        Scene sc= new Scene(gp,300,100);
        stage.setScene(sc);
        stage.show();
    }

    private void addListener(){
        bt[0].setOnAction(event->{

            if(tf[0].getText().isEmpty() || pf.getText().isEmpty()){
                return;
            }

            /* 数据库里查询 */
            try (SqlSession session = MyBatis.factory.openSession()){

                List<Map> result = session.selectList(
                        "reg.checkUserExists",
                        new HashMap<>(){{
                            put("nick_name",tf[0].getText());
                        }}
                );
                
                if(result.isEmpty()){
                    /* 玩家的账户信息 */
                    HashMap<String,Object> profileRow = new HashMap<>(){{
                        put("nick_name",tf[0].getText());
                        put("password",pf.getText());
                        put("registered_date",new java.util.Date());
                    }};

                    /* 先把这个commit一下，因为一会儿要拿自增的player_id，给其他表插数据 */
                    session.insert("reg.insertUser",profileRow);
                    session.commit();

                    HashMap<String,Object> initNewUserConditionRow = new HashMap<>(){{
                        put("player_id",profileRow.get("player_id")); //拿到唯一id
                        put("hp",100); // 初始血量
                        put("mp",100); // 初始蓝量
                        put("money",1000); // 初始金钱
                        put("attack",100); // 初始攻击力
                    }};

                    session.insert("reg.initNewUserCondition",initNewUserConditionRow);
                    session.commit();

                    
                } else {
                    new Alert(Alert.AlertType.WARNING,"用户名已存在！").show();
                }
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

