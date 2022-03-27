package cn.akfang.advanture.mybatis;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatis
{
	// 全局实例
	public static SqlSessionFactory factory;
	
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

	public static HashMap<String,String> getAliasFromDatabase(){
		HashMap<String,String> alias = new HashMap<>();
		try (SqlSession session = MyBatis.factory.openSession()) {
			List<Map> resultList = session.selectList("getAliasList");
			resultList.forEach(item -> {
				alias.put(
						(String) item.get("origin_name"),
						(String) item.get("alias_name")
				);
			});
		}
		return alias;
	}

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

	public static void updateObjectAmount(int object_id,int amount){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerBoxItem",new HashMap<>(){{
				put("object_id",object_id);
				put("amount",amount);
			}});
			session.commit();
		}
	}
	public static void updatePlayerConditionHp(int player_id,int hp){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerConditionHp",new HashMap<>(){{
				put("player_id",player_id);
				put("hp",hp);
			}});
			session.commit();
		}
	}
	public static void updatePlayerConditionMp(int player_id,int mp){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerConditionMp",new HashMap<>(){{
				put("player_id",player_id);
				put("mp",mp);
			}});

			session.commit();
		}
	}

	public static void updatePlayerConditionMoney(int player_id,int money){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.update("updatePlayerConditionMoney",new HashMap<>(){{
				put("player_id",player_id);
				put("money",money);
			}});
			session.commit();
		}
	}

	public static int insertItemInPlayerBox(int owner_id, int class_id, int amount){
		HashMap<String,Object> temp = new HashMap<>(){{
			put("owner_id",owner_id);
			put("class_id",class_id);
			put("amount",amount);
		}};
		try (SqlSession session = MyBatis.factory.openSession()){

			session.insert("insertItemInPlayerBox",temp);
			session.commit();

		}
		return Integer.parseInt(String.valueOf(temp.get("object_id")) );
	}

	public static void deleteNullItemInPlayerBox(int object_id,int owner_id){
		try (SqlSession session = MyBatis.factory.openSession()){
			session.delete("removeNullItemInPlayBox",new HashMap<>(){{
				put("object_id",object_id);
				put("owner_id",owner_id);
			}});
			session.commit();
		}
	}
}
