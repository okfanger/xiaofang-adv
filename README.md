
# 游戏物品管理系统
> @email: lovefyj616@foxmail.com

![image-20211224150159195](https://img-blog.csdnimg.cn/img_convert/309454cbf05a078c67ec42d4f4483fc9.png)

本管理系统可以存储同一类物品的数量，支持出售、使用、移除、购买等物品操作，此外还引入了个人的生命值、法力值、金币、攻击力的属性，用来展示该系统优秀的数据绑定操作。

例如，苹果的效果是 "法力值+10" ，点击使用后，该人物对应的生命值属性+10，同时与该属性绑定的红色进度条（血条）也会同步更新；点击购买炸弹时，系统会首先判断用户是否具有足够的金钱购买，并进行响应...

> 部署该系统时，需要导入项目依赖（在下文会提及），并将src目录里的sql文件导入到数据库中，修改数据库连接的配置文件，方可正常运行！

## 1. 项目依赖(包)及结构

### 1.1 项目依赖

javafx-sdk-11.0.2、mysql-connector-java-8.8.16、mybatis、log4j-1.2.17

### 1.2 项目结构

<img src="https://s2.loli.net/2022/01/11/T9o8dvBPz7IuJ41.png" alt="image-20211224142811672" style="zoom:67%;" />

### 1.3 数据库结构

<img src="https://s2.loli.net/2022/01/11/sUgbkpjaMFW9vC4.png" alt="image-20211224143843672" style="zoom:80%;" />

## 2. 类结构（UML图）

### 2.1 数据bean

用来数据的存储及fx组件之间的动态绑定

![image-20211224141946261](https://img-blog.csdnimg.cn/img_convert/a26967d0cd814e6876ca2a8493757dcf.png)

### 2.2 主界面

#### 2.2.1 游戏主类 (Game.java)

![image-20211224143645633](https://img-blog.csdnimg.cn/img_convert/64e3b3afe52c087d3f2c7f7576a131b8.png)

#### 2.2.2 商店类 (MarketStage.java)

![image-20211224144131029](https://img-blog.csdnimg.cn/img_convert/00b4c8ddeaddf473e18485a706bc7569.png)

#### 2.2.3 登录(Login.java)、注册类(Register.java)

![image-20211224144255099](https://img-blog.csdnimg.cn/img_convert/bf579aaf0f7a3f8b5017fcf9ced91f3d.png)

#### 2.2.4 自定义面板类(TableViewPane.java、TopPane)

![image-20211224144401848](https://img-blog.csdnimg.cn/img_convert/d128d53e8f8082965282e93d59c22217.png)

### 2.3 工具类 （连接数据库）

![image-20211224144949755](https://img-blog.csdnimg.cn/img_convert/a1f9ab4209db9a0adc397a86a9a1f319.png)

## 3. 数据的存储及读入、查询

> 所有数据库操作都以静态方法的形式在MyBatis中定义

### 3.2 引入MyBatis Factory 单例模式

#### 3.2.1 配置数据库地址、用户名、密码

在项目 cn/akfang/advanture/mybatis/mybatis-config.xml 中，**配置数据库信息**

在property标签中，分别填写name为 url, username, password的标签

如下代码，表示 **数据库地址**为 本机(127.0.0.1)，**数据库名**为 xiaofang ，**用户名**为 root，**密码**为 root

```
...
      <dataSource type="POOLED">
        <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://127.0.0.1/xiaofang?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=Asia/Shanghai"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
      </dataSource>
...
```

#### 3.2.2 单例模式

```java
static {
    System.out.println("MyBatis factory 初始化 ...");
    // 静态代码块: 初始化 SqlSessionFactory 实例
    try{
        String resource = "./cn/akfang/advanture/mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
```

### 3.3 在mapper.xml 中定义所有操作

这里篇幅有限，增删改查各选一个展示，详细请自行查看

```sql
<select id="checkUserExists" parameterType="hashmap" resultType="hashmap">
    select * from `player_profile` where nick_name=#{nick_name};
</select>
```

```sql
    <insert id="insertUser"  parameterType="hashmap" useGeneratedKeys="true" keyProperty="player_id" >
        INSERT INTO `player_profile` (`player_id`,`nick_name`,`password`,`registered_date`) VALUES (#{player_id},#{nick_name},#{password},#{registered_date});
    </insert>
```

```sql
    <update id="updatePlayerBoxItem" parameterType="hashmap" >
        update `player_box` set `amount`=#{amount} where object_id=#{object_id}
    </update>
```

```sql
    <delete id="removeNullItemInPlayBox" parameterType="hashmap">
        delete from player_box where object_id=#{object_id} and owner_id=#{owner_id}
    </delete>
```

...

### 3.4 封装Sqlsession

所有session同样以静态方法的形式，存于MyBatis 类中

```Java
public static HashMap<Integer,HashMap<String,Object>> getObjectPropertyFromDatabase(){
		HashMap<Integer,HashMap<String,Object>> objectProperty = new HashMap<>();
		try (SqlSession session = MyBatis.factory.openSession()){
			List<Map> resultList = session.selectList("getObjectClass");
			resultList.forEach(item -> {
				int key = (int) item.get("class_id");
				HashMap<String,Object> value = new HashMap<>();
				value.put("name",item.get("name"));
				value.put("cost",item.get("cost"));
				String[] effect = ((String)item.get("effect")).split(",");
				for (String effects :effect){
					String[] effectMap = effects.split(":");
					value.put(effectMap[0],Integer.parseInt(effectMap[1]));
				}
				objectProperty.put(key,value);
			});
		}
		return objectProperty;
	}
```

```java
	public static void updatePlayerConditionHp(int player_id,int hp){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerConditionHp",new HashMap<>(){{
				put("player_id",player_id);
				put("hp",hp);
			}});
			session.commit();
		}
	}
```

```java
	public static void updatePlayerConditionMoney(int player_id,int money){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerConditionMoney",new HashMap<>(){{
				put("player_id",player_id);
				put("money",money);
			}});
			session.commit();
		}
	}
```

...

## 4. 布局

### 4.1 Game.java

![image-20211224152343606](https://img-blog.csdnimg.cn/img_convert/7a8f62f3ab47f9450d9e3c98d7357f7c.png)

### 4.2 TopPane.java

![image-20211224152626556](https://img-blog.csdnimg.cn/img_convert/9c5357eaca85f8177b03148fd3998a09.png)

### 4.3 TableViewPane.java

![image-20211224153027754](https://img-blog.csdnimg.cn/img_convert/35fa2e3c1669befeeb6f9c0f93bd8416.png)

### 4.4 Market.java

![image-20211224154315482](https://img-blog.csdnimg.cn/img_convert/29a17ca303fd474a3c170b406eeab91b.png)

## 5. 部分关键操作演示

### 5.1 登录及注册

#### 5.1.1 注册

![image-20211224155238234](https://img-blog.csdnimg.cn/img_convert/c5e4b519b2ac0fbc1f3b2bae34b850ee.png)

点击注册按钮后，会向数据库提交一个select

```java
 try (SqlSession session = MyBatis.factory.openSession()){
     List<Map> result = session.selectList(
         "reg.checkUserExists",
         new HashMap<>(){{
             put("nick_name",tf[0].getText());
         }}
     );
 	...
 }
```

若数据库中没有这个昵称的账号，就提交一个insert，并提交事务

```java
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
```


#### 5.1.2 登录

![image-20211224165319056](https://img-blog.csdnimg.cn/img_convert/568bdc8b578e2dbc848da39a12ab916f.png)

点击注册按钮后，会向数据库提交一个select

```java
 try (SqlSession session = MyBatis.factory.openSession()){
     List<Map> result = session.selectList(
         "reg.checkUserExists",
         new HashMap<>(){{
             put("nick_name",tf[0].getText());
         }}
     );
 	...
 }
```

若数据库中没有这个昵称的账号，就弹出提示框

反之，将存有玩家数据的Map，传入Game.java，运行Game类的begin()方法

```java
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
```

### 5.2 物品操作

#### 5.2.1 物品使用

```java
bt[0].setOnAction(event->{
                if(thisItem.getAmount()==0){
                    return;
                }
                HashMap<String, Object> usage = Game.objectProperty.get(thisItem.getClassId());
                for (String key : usage.keySet()) {
                    if (key.equals("attack")) player.setAttack(player.getAttack() + (Integer) usage.get("attack"));
                    if (key.equals("hp")) {
                        int after = player.getHp() + (Integer) usage.get("hp");
                        if (after < 0) {
                            new Alert(Alert.AlertType.WARNING, "生命值不足!").show();
                            return;
                        }
                        player.setHp(after);
                    }
                    if (key.equals("mp")) {
                        int after = player.getMp() + (Integer) usage.get("mp");
                        if (after < 0) {
                            new Alert(Alert.AlertType.WARNING, "法力值不足!").show();
                            return;
                        }
                        player.setMp(player.getMp() + (Integer) usage.get("mp"));
                    }
                }
                thisItem.setAmount(thisItem.getAmount() - 1);
                //System.out.println(player.mpProperty().get());
            });
```

#### 5.2.2 物品出售

```java
bt[1].setOnAction(event->{
    if(thisItem.getAmount()==0){
        return;
    }
    thisItem.setAmount(thisItem.getAmount()-1);
    player.setMoney(player.getMoney()+thisItem.getCost());
});
```

#### 5.2.4 物品移除

```
Button bt33 = new Button("移除");
this.setGraphic(bt33);
bt33.setOnAction(event->{
    MyBatis.deleteNullItemInPlayerBox(thisItem.getId(),player.getPlayer_id());
    data.remove(getIndex());
});
```

#### 5.2.5 物品购买

```java
buyButton.setOnAction(event->{
                int money = player.getMoney();
                if(money<thisItem.getCost()) return;
                player.setMoney(player.getMoney()-thisItem.getCost());
                GameObjectProperty result = checkPlayerBoxExists(thisItem.getClass_id());

                if(result!=null){
                    result.setAmount(result.getAmount()+1);
                    //update
                } else {

                    int feeback_id = MyBatis.insertItemInPlayerBox(player.getPlayer_id(),thisItem.getClass_id(),1);
                    //System.out.println(feeback_id);
                    playerbox.add(new GameObjectProperty(feeback_id,thisItem.getClass_id(),thisItem.getName(),thisItem.getCost(),1));
                }
            });
```

### 5.3 属性增减

#### 5.3.1 血条增减

```java
public void setHp(int hp) {
    if(hp<0) return;
    MyBatis.updatePlayerConditionHp(getPlayer_id(),hp);
    this.hp.set(hp);
}
```

#### 5.3.2 蓝条增减

```java
public void setMp(int mp) {
    if(mp<0) return;
    MyBatis.updatePlayerConditionMp(getPlayer_id(),mp);
    this.mp.set(mp);
}
```

#### 5.3.3 金钱增减

```java
public void setMoney(int money) {
    if(money<0) return;
    MyBatis.updatePlayerConditionMoney(getPlayer_id(),money);
    this.money.set(money);
}
```

# 游戏物品管理系统

> @author: 20级计科三班 方宇杰 2020122104559
> @email: lovefyj616@foxmail.com

![image-20211224150159195](https://img-blog.csdnimg.cn/img_convert/9884931524edd06fa11ca73efabd94b1.png)

本管理系统可以存储同一类物品的数量，支持出售、使用、移除、购买等物品操作，此外还引入了个人的生命值、法力值、金币、攻击力的属性，用来展示该系统优秀的数据绑定操作。

例如，苹果的效果是 "法力值+10" ，点击使用后，该人物对应的生命值属性+10，同时与该属性绑定的红色进度条（血条）也会同步更新；点击购买炸弹时，系统会首先判断用户是否具有足够的金钱购买，并进行响应...

> 部署该系统时，需要导入项目依赖（在下文会提及），并将src目录里的sql文件导入到数据库中，修改数据库连接的配置文件，方可正常运行！

## 1. 项目依赖(包)及结构

### 1.1 项目依赖

javafx-sdk-11.0.2、mysql-connector-java-8.8.16、mybatis、log4j-1.2.17

### 1.2 项目结构

<img src="https://s2.loli.net/2022/01/11/T9o8dvBPz7IuJ41.png" alt="image-20211224142811672" style="zoom:67%;" />

### 1.3 数据库结构

<img src="https://s2.loli.net/2022/01/11/sUgbkpjaMFW9vC4.png" alt="image-20211224143843672" style="zoom:80%;" />

## 2. 类结构（UML图）

### 2.1 数据bean

用来数据的存储及fx组件之间的动态绑定

![image-20211224141946261](https://img-blog.csdnimg.cn/img_convert/c8f35102e24f21f97366b367e64544f5.png)

### 2.2 主界面

#### 2.2.1 游戏主类 (Game.java)

![image-20211224143645633](https://img-blog.csdnimg.cn/img_convert/566ee03c4f49bc76964c26e313e3ee31.png)

#### 2.2.2 商店类 (MarketStage.java)

![image-20211224144131029](https://img-blog.csdnimg.cn/img_convert/56810805f62bc51e7c01bf0eccb6a220.png)

#### 2.2.3 登录(Login.java)、注册类(Register.java)

![image-20211224144255099](https://img-blog.csdnimg.cn/img_convert/1b0a906bffd9424af2b5ff88f2f8f9d4.png)

#### 2.2.4 自定义面板类(TableViewPane.java、TopPane)

![image-20211224144401848](https://img-blog.csdnimg.cn/img_convert/8aea26240e91ecd19436a8de53f4daae.png)

### 2.3 工具类 （连接数据库）

![image-20211224144949755](https://img-blog.csdnimg.cn/img_convert/a9bfe1d64e7424ad9ced3c2cdc6f0577.png)

## 3. 数据的存储及读入、查询

> 所有数据库操作都以静态方法的形式在MyBatis中定义

### 3.2 引入MyBatis Factory 单例模式

#### 3.2.1 配置数据库地址、用户名、密码

在项目 cn/akfang/advanture/mybatis/mybatis-config.xml 中，**配置数据库信息**

在property标签中，分别填写name为 url, username, password的标签

如下代码，表示 **数据库地址**为 本机(127.0.0.1)，**数据库名**为 xiaofang ，**用户名**为 root，**密码**为 root

```
...
      <dataSource type="POOLED">
        <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://127.0.0.1/xiaofang?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=Asia/Shanghai"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
      </dataSource>
...
```

#### 3.2.2 单例模式

```java
static {
    System.out.println("MyBatis factory 初始化 ...");
    // 静态代码块: 初始化 SqlSessionFactory 实例
    try{
        String resource = "./cn/akfang/advanture/mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
```

### 3.3 在mapper.xml 中定义所有操作

这里篇幅有限，增删改查各选一个展示，详细请自行查看

```sql
<select id="checkUserExists" parameterType="hashmap" resultType="hashmap">
    select * from `player_profile` where nick_name=#{nick_name};
</select>
```

```sql
    <insert id="insertUser"  parameterType="hashmap" useGeneratedKeys="true" keyProperty="player_id" >
        INSERT INTO `player_profile` (`player_id`,`nick_name`,`password`,`registered_date`) VALUES (#{player_id},#{nick_name},#{password},#{registered_date});
    </insert>
```

```sql
    <update id="updatePlayerBoxItem" parameterType="hashmap" >
        update `player_box` set `amount`=#{amount} where object_id=#{object_id}
    </update>
```

```sql
    <delete id="removeNullItemInPlayBox" parameterType="hashmap">
        delete from player_box where object_id=#{object_id} and owner_id=#{owner_id}
    </delete>
```

...

### 3.4 封装Sqlsession

所有session同样以静态方法的形式，存于MyBatis 类中

```Java
public static HashMap<Integer,HashMap<String,Object>> getObjectPropertyFromDatabase(){
		HashMap<Integer,HashMap<String,Object>> objectProperty = new HashMap<>();
		try (SqlSession session = MyBatis.factory.openSession()){
			List<Map> resultList = session.selectList("getObjectClass");
			resultList.forEach(item -> {
				int key = (int) item.get("class_id");
				HashMap<String,Object> value = new HashMap<>();
				value.put("name",item.get("name"));
				value.put("cost",item.get("cost"));
				String[] effect = ((String)item.get("effect")).split(",");
				for (String effects :effect){
					String[] effectMap = effects.split(":");
					value.put(effectMap[0],Integer.parseInt(effectMap[1]));
				}
				objectProperty.put(key,value);
			});
		}
		return objectProperty;
	}
```

```java
	public static void updatePlayerConditionHp(int player_id,int hp){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerConditionHp",new HashMap<>(){{
				put("player_id",player_id);
				put("hp",hp);
			}});
			session.commit();
		}
	}
```

```java
	public static void updatePlayerConditionMoney(int player_id,int money){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerConditionMoney",new HashMap<>(){{
				put("player_id",player_id);
				put("money",money);
			}});
			session.commit();
		}
	}
```

...

## 4. 布局

### 4.1 Game.java

![image-20211224152343606](https://img-blog.csdnimg.cn/img_convert/a446b01f62c54b75fe7f48a8251a0a10.png)

### 4.2 TopPane.java

![image-20211224152626556](https://img-blog.csdnimg.cn/img_convert/587e7d28ce5b2bd42d424596d28c618e.png)

### 4.3 TableViewPane.java

![image-20211224153027754](https://img-blog.csdnimg.cn/img_convert/020f7a5e13ecc69c487cabcd437e5664.png)

### 4.4 Market.java

![image-20211224154315482](https://img-blog.csdnimg.cn/img_convert/224ec3121cf906f477e2bca45c782c44.png)

## 5. 部分关键操作演示

### 5.1 登录及注册

#### 5.1.1 注册

![image-20211224155238234](https://img-blog.csdnimg.cn/img_convert/3ee438ae22e5afe04c305f64cd757ed0.png)

点击注册按钮后，会向数据库提交一个select

```java
 try (SqlSession session = MyBatis.factory.openSession()){
     List<Map> result = session.selectList(
         "reg.checkUserExists",
         new HashMap<>(){{
             put("nick_name",tf[0].getText());
         }}
     );
 	...
 }
```

若数据库中没有这个昵称的账号，就提交一个insert，并提交事务

```java
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
```


#### 5.1.2 登录

![image-20211224165319056](https://img-blog.csdnimg.cn/img_convert/863626d8c9f8407f8a3c402cd3251345.png)

点击注册按钮后，会向数据库提交一个select

```java
 try (SqlSession session = MyBatis.factory.openSession()){
     List<Map> result = session.selectList(
         "reg.checkUserExists",
         new HashMap<>(){{
             put("nick_name",tf[0].getText());
         }}
     );
 	...
 }
```

若数据库中没有这个昵称的账号，就弹出提示框

反之，将存有玩家数据的Map，传入Game.java，运行Game类的begin()方法

```java
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
```

### 5.2 物品操作

#### 5.2.1 物品使用

```java
bt[0].setOnAction(event->{
                if(thisItem.getAmount()==0){
                    return;
                }
                HashMap<String, Object> usage = Game.objectProperty.get(thisItem.getClassId());
                for (String key : usage.keySet()) {
                    if (key.equals("attack")) player.setAttack(player.getAttack() + (Integer) usage.get("attack"));
                    if (key.equals("hp")) {
                        int after = player.getHp() + (Integer) usage.get("hp");
                        if (after < 0) {
                            new Alert(Alert.AlertType.WARNING, "生命值不足!").show();
                            return;
                        }
                        player.setHp(after);
                    }
                    if (key.equals("mp")) {
                        int after = player.getMp() + (Integer) usage.get("mp");
                        if (after < 0) {
                            new Alert(Alert.AlertType.WARNING, "法力值不足!").show();
                            return;
                        }
                        player.setMp(player.getMp() + (Integer) usage.get("mp"));
                    }
                }
                thisItem.setAmount(thisItem.getAmount() - 1);
                //System.out.println(player.mpProperty().get());
            });
```

#### 5.2.2 物品出售

```java
bt[1].setOnAction(event->{
    if(thisItem.getAmount()==0){
        return;
    }
    thisItem.setAmount(thisItem.getAmount()-1);
    player.setMoney(player.getMoney()+thisItem.getCost());
});
```

#### 5.2.4 物品移除

```
Button bt33 = new Button("移除");
this.setGraphic(bt33);
bt33.setOnAction(event->{
    MyBatis.deleteNullItemInPlayerBox(thisItem.getId(),player.getPlayer_id());
    data.remove(getIndex());
});
```

#### 5.2.5 物品购买

```java
buyButton.setOnAction(event->{
                int money = player.getMoney();
                if(money<thisItem.getCost()) return;
                player.setMoney(player.getMoney()-thisItem.getCost());
                GameObjectProperty result = checkPlayerBoxExists(thisItem.getClass_id());

                if(result!=null){
                    result.setAmount(result.getAmount()+1);
                    //update
                } else {

                    int feeback_id = MyBatis.insertItemInPlayerBox(player.getPlayer_id(),thisItem.getClass_id(),1);
                    //System.out.println(feeback_id);
                    playerbox.add(new GameObjectProperty(feeback_id,thisItem.getClass_id(),thisItem.getName(),thisItem.getCost(),1));
                }
            });
```

### 5.3 属性增减

#### 5.3.1 血条增减

```java
public void setHp(int hp) {
    if(hp<0) return;
    MyBatis.updatePlayerConditionHp(getPlayer_id(),hp);
    this.hp.set(hp);
}
```

#### 5.3.2 蓝条增减

```java
public void setMp(int mp) {
    if(mp<0) return;
    MyBatis.updatePlayerConditionMp(getPlayer_id(),mp);
    this.mp.set(mp);
}
```

#### 5.3.3 金钱增减

```java
public void setMoney(int money) {
    if(money<0) return;
    MyBatis.updatePlayerConditionMoney(getPlayer_id(),money);
    this.money.set(money);
}
```

# 游戏物品管理系统

> @author: 20级计科三班 方宇杰 2020122104559
> @email: lovefyj616@foxmail.com

![image-20211224150159195](https://img-blog.csdnimg.cn/img_convert/9884931524edd06fa11ca73efabd94b1.png)

本管理系统可以存储同一类物品的数量，支持出售、使用、移除、购买等物品操作，此外还引入了个人的生命值、法力值、金币、攻击力的属性，用来展示该系统优秀的数据绑定操作。

例如，苹果的效果是 "法力值+10" ，点击使用后，该人物对应的生命值属性+10，同时与该属性绑定的红色进度条（血条）也会同步更新；点击购买炸弹时，系统会首先判断用户是否具有足够的金钱购买，并进行响应...

> 部署该系统时，需要导入项目依赖（在下文会提及），并将src目录里的sql文件导入到数据库中，修改数据库连接的配置文件，方可正常运行！

## 1. 项目依赖(包)及结构

### 1.1 项目依赖

javafx-sdk-11.0.2、mysql-connector-java-8.8.16、mybatis、log4j-1.2.17

### 1.2 项目结构

<img src="https://s2.loli.net/2022/01/11/T9o8dvBPz7IuJ41.png" alt="image-20211224142811672" style="zoom:67%;" />

### 1.3 数据库结构

<img src="https://s2.loli.net/2022/01/11/sUgbkpjaMFW9vC4.png" alt="image-20211224143843672" style="zoom:80%;" />

## 2. 类结构（UML图）

### 2.1 数据bean

用来数据的存储及fx组件之间的动态绑定

![image-20211224141946261](https://img-blog.csdnimg.cn/img_convert/c8f35102e24f21f97366b367e64544f5.png)

### 2.2 主界面

#### 2.2.1 游戏主类 (Game.java)

![image-20211224143645633](https://img-blog.csdnimg.cn/img_convert/566ee03c4f49bc76964c26e313e3ee31.png)

#### 2.2.2 商店类 (MarketStage.java)

![image-20211224144131029](https://img-blog.csdnimg.cn/img_convert/56810805f62bc51e7c01bf0eccb6a220.png)

#### 2.2.3 登录(Login.java)、注册类(Register.java)

![image-20211224144255099](https://img-blog.csdnimg.cn/img_convert/1b0a906bffd9424af2b5ff88f2f8f9d4.png)

#### 2.2.4 自定义面板类(TableViewPane.java、TopPane)

![image-20211224144401848](https://img-blog.csdnimg.cn/img_convert/8aea26240e91ecd19436a8de53f4daae.png)

### 2.3 工具类 （连接数据库）

![image-20211224144949755](https://img-blog.csdnimg.cn/img_convert/a9bfe1d64e7424ad9ced3c2cdc6f0577.png)

## 3. 数据的存储及读入、查询

> 所有数据库操作都以静态方法的形式在MyBatis中定义

### 3.2 引入MyBatis Factory 单例模式

#### 3.2.1 配置数据库地址、用户名、密码

在项目 cn/akfang/advanture/mybatis/mybatis-config.xml 中，**配置数据库信息**

在property标签中，分别填写name为 url, username, password的标签

如下代码，表示 **数据库地址**为 本机(127.0.0.1)，**数据库名**为 xiaofang ，**用户名**为 root，**密码**为 root

```
...
      <dataSource type="POOLED">
        <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://127.0.0.1/xiaofang?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=Asia/Shanghai"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
      </dataSource>
...
```

#### 3.2.2 单例模式

```java
static {
    System.out.println("MyBatis factory 初始化 ...");
    // 静态代码块: 初始化 SqlSessionFactory 实例
    try{
        String resource = "./cn/akfang/advanture/mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
```

### 3.3 在mapper.xml 中定义所有操作

这里篇幅有限，增删改查各选一个展示，详细请自行查看

```sql
<select id="checkUserExists" parameterType="hashmap" resultType="hashmap">
    select * from `player_profile` where nick_name=#{nick_name};
</select>
```

```sql
    <insert id="insertUser"  parameterType="hashmap" useGeneratedKeys="true" keyProperty="player_id" >
        INSERT INTO `player_profile` (`player_id`,`nick_name`,`password`,`registered_date`) VALUES (#{player_id},#{nick_name},#{password},#{registered_date});
    </insert>
```

```sql
    <update id="updatePlayerBoxItem" parameterType="hashmap" >
        update `player_box` set `amount`=#{amount} where object_id=#{object_id}
    </update>
```

```sql
    <delete id="removeNullItemInPlayBox" parameterType="hashmap">
        delete from player_box where object_id=#{object_id} and owner_id=#{owner_id}
    </delete>
```

...

### 3.4 封装Sqlsession

所有session同样以静态方法的形式，存于MyBatis 类中

```Java
public static HashMap<Integer,HashMap<String,Object>> getObjectPropertyFromDatabase(){
		HashMap<Integer,HashMap<String,Object>> objectProperty = new HashMap<>();
		try (SqlSession session = MyBatis.factory.openSession()){
			List<Map> resultList = session.selectList("getObjectClass");
			resultList.forEach(item -> {
				int key = (int) item.get("class_id");
				HashMap<String,Object> value = new HashMap<>();
				value.put("name",item.get("name"));
				value.put("cost",item.get("cost"));
				String[] effect = ((String)item.get("effect")).split(",");
				for (String effects :effect){
					String[] effectMap = effects.split(":");
					value.put(effectMap[0],Integer.parseInt(effectMap[1]));
				}
				objectProperty.put(key,value);
			});
		}
		return objectProperty;
	}
```

```java
	public static void updatePlayerConditionHp(int player_id,int hp){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerConditionHp",new HashMap<>(){{
				put("player_id",player_id);
				put("hp",hp);
			}});
			session.commit();
		}
	}
```

```java
	public static void updatePlayerConditionMoney(int player_id,int money){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerConditionMoney",new HashMap<>(){{
				put("player_id",player_id);
				put("money",money);
			}});
			session.commit();
		}
	}
```

...

## 4. 布局

### 4.1 Game.java

![image-20211224152343606](https://img-blog.csdnimg.cn/img_convert/a446b01f62c54b75fe7f48a8251a0a10.png)

### 4.2 TopPane.java

![image-20211224152626556](https://img-blog.csdnimg.cn/img_convert/587e7d28ce5b2bd42d424596d28c618e.png)

### 4.3 TableViewPane.java

![image-20211224153027754](https://img-blog.csdnimg.cn/img_convert/020f7a5e13ecc69c487cabcd437e5664.png)

### 4.4 Market.java

![image-20211224154315482](https://img-blog.csdnimg.cn/img_convert/224ec3121cf906f477e2bca45c782c44.png)

## 5. 部分关键操作演示

### 5.1 登录及注册

#### 5.1.1 注册

![image-20211224155238234](https://img-blog.csdnimg.cn/img_convert/3ee438ae22e5afe04c305f64cd757ed0.png)

点击注册按钮后，会向数据库提交一个select

```java
 try (SqlSession session = MyBatis.factory.openSession()){
     List<Map> result = session.selectList(
         "reg.checkUserExists",
         new HashMap<>(){{
             put("nick_name",tf[0].getText());
         }}
     );
 	...
 }
```

若数据库中没有这个昵称的账号，就提交一个insert，并提交事务

```java
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
```


#### 5.1.2 登录

![image-20211224165319056](https://img-blog.csdnimg.cn/img_convert/863626d8c9f8407f8a3c402cd3251345.png)

点击注册按钮后，会向数据库提交一个select

```java
 try (SqlSession session = MyBatis.factory.openSession()){
     List<Map> result = session.selectList(
         "reg.checkUserExists",
         new HashMap<>(){{
             put("nick_name",tf[0].getText());
         }}
     );
 	...
 }
```

若数据库中没有这个昵称的账号，就弹出提示框

反之，将存有玩家数据的Map，传入Game.java，运行Game类的begin()方法

```java
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
```

### 5.2 物品操作

#### 5.2.1 物品使用

```java
bt[0].setOnAction(event->{
                if(thisItem.getAmount()==0){
                    return;
                }
                HashMap<String, Object> usage = Game.objectProperty.get(thisItem.getClassId());
                for (String key : usage.keySet()) {
                    if (key.equals("attack")) player.setAttack(player.getAttack() + (Integer) usage.get("attack"));
                    if (key.equals("hp")) {
                        int after = player.getHp() + (Integer) usage.get("hp");
                        if (after < 0) {
                            new Alert(Alert.AlertType.WARNING, "生命值不足!").show();
                            return;
                        }
                        player.setHp(after);
                    }
                    if (key.equals("mp")) {
                        int after = player.getMp() + (Integer) usage.get("mp");
                        if (after < 0) {
                            new Alert(Alert.AlertType.WARNING, "法力值不足!").show();
                            return;
                        }
                        player.setMp(player.getMp() + (Integer) usage.get("mp"));
                    }
                }
                thisItem.setAmount(thisItem.getAmount() - 1);
                //System.out.println(player.mpProperty().get());
            });
```

#### 5.2.2 物品出售

```java
bt[1].setOnAction(event->{
    if(thisItem.getAmount()==0){
        return;
    }
    thisItem.setAmount(thisItem.getAmount()-1);
    player.setMoney(player.getMoney()+thisItem.getCost());
});
```

#### 5.2.4 物品移除

```
Button bt33 = new Button("移除");
this.setGraphic(bt33);
bt33.setOnAction(event->{
    MyBatis.deleteNullItemInPlayerBox(thisItem.getId(),player.getPlayer_id());
    data.remove(getIndex());
});
```

#### 5.2.5 物品购买

```java
buyButton.setOnAction(event->{
                int money = player.getMoney();
                if(money<thisItem.getCost()) return;
                player.setMoney(player.getMoney()-thisItem.getCost());
                GameObjectProperty result = checkPlayerBoxExists(thisItem.getClass_id());

                if(result!=null){
                    result.setAmount(result.getAmount()+1);
                    //update
                } else {

                    int feeback_id = MyBatis.insertItemInPlayerBox(player.getPlayer_id(),thisItem.getClass_id(),1);
                    //System.out.println(feeback_id);
                    playerbox.add(new GameObjectProperty(feeback_id,thisItem.getClass_id(),thisItem.getName(),thisItem.getCost(),1));
                }
            });
```

### 5.3 属性增减

#### 5.3.1 血条增减

```java
public void setHp(int hp) {
    if(hp<0) return;
    MyBatis.updatePlayerConditionHp(getPlayer_id(),hp);
    this.hp.set(hp);
}
```

#### 5.3.2 蓝条增减

```java
public void setMp(int mp) {
    if(mp<0) return;
    MyBatis.updatePlayerConditionMp(getPlayer_id(),mp);
    this.mp.set(mp);
}
```

#### 5.3.3 金钱增减

```java
public void setMoney(int money) {
    if(money<0) return;
    MyBatis.updatePlayerConditionMoney(getPlayer_id(),money);
    this.money.set(money);
}
```

