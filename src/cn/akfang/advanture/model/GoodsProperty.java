package cn.akfang.advanture.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import java.util.Date;

/** 本类由 POJO生成器 自动生成于 2021-12-17 09:20:56
 作者：阿发你好      官网: http://afanihao.cn
 */

/** INSERT语句 ( 预处理方式 )
 INSERT INTO `object_property`
 (`class_id`, `name`, `cost`, `effect`)
 VALUES(?, ?, ?, ?)
 */

/** INSERT语句 ( MyBatis方式 )
 INSERT INTO `object_property`
 (`class_id`, `name`, `cost`, `effect`)
 VALUES(#{class_id}, #{name}, #{cost}, #{effect})

 自增主键: class_id
 */

public class GoodsProperty
{
    public Integer class_id ;
    public String name ;
    public Integer cost ;
    public String effect ;

    public GoodsProperty(Integer class_id, String name, Integer cost, String effect) {
        this.class_id = class_id;
        this.name = name;
        this.cost = cost;
        this.effect = effect;
    }

    public void setClass_id(Integer class_id)
    {
        this.class_id=class_id;
    }
    public Integer getClass_id()
    {
        return this.class_id;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return this.name;
    }
    public void setCost(Integer cost)
    {
        this.cost=cost;
    }
    public Integer getCost()
    {
        return this.cost;
    }
    public void setEffect(String effect)
    {
        this.effect=effect;
    }
    public String getEffect()
    {
        return this.effect;
    }

}
