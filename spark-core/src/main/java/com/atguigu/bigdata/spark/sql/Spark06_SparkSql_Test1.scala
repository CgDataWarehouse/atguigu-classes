package com.atguigu.bigdata.spark.sql


import org.apache.spark.SparkConf
import org.apache.spark.sql.{Encoder, _}
import org.apache.spark.sql.expressions.Aggregator

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


object Spark06_SparkSql_Test1 {
  def main(args: Array[String]): Unit = {

    System.setProperty("HADOOP_USER_NAME", "cg")
    //TODO 创建SparkSql的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark = SparkSession.builder().enableHiveSupport().config(sparkConf).getOrCreate()

    // 注册自定义 udaf
    spark.udf.register("myCityCntUADF", functions.udaf(new CityCntUADF()))


    // 执行逻辑
    spark.sql("use atguigu")


    //查询基本数据
    spark.sql(
      """
        | select
        | a.*
        | ,b.product_name
        | ,c.city_name
        | ,c.area
        | from user_visit_action a
        | join product_info b on a.click_product_id= b.product_id
        | join city_info c on a.city_id = c.city_id
        |""".stripMargin).createOrReplaceTempView("t1")

    // t1 根据area,product_name 分组,udaf自定义函数 对city_name进行聚合操作得到想要的结果
    // 自定义函数在此处用,是因为这个聚合操作正好也需要根据area,product_name进行分组,组内根据city_name进行聚合操作
    spark.sql(
      """
        | select
        | area
        | ,product_name
        | ,count(1) as clickCnt
        | ,myCityCntUADF(city_name)
        | from t1
        | group by area,product_name
        |""".stripMargin).createOrReplaceTempView("t2")

    // t2 根据地区分组,组内根据点击量倒叙排序
    spark.sql(
      """
        | select
        | *
        | ,rank(clickCnt) over (partition by area order by clickCnt desc) rk
        | from t2
        |""".stripMargin).createOrReplaceTempView("t3")

    // 取前3

    spark.sql(
      """
        |select
        |*
        |from
        |t3
        |where rk<=3
        |""".stripMargin).show(false) // 展示会将多余的...表示出来,false关闭

    //    spark.sql(
    //      """
    //        |select
    //        |*
    //        |from
    //        |(
    //        |    select
    //        |    *
    //        |    ,rank(clickCnt) over (partition by area order by clickCnt desc) rk
    //        |    from
    //        |    (
    //        |        select
    //        |        area
    //        |        ,product_name
    //        |        ,count(1) as clickCnt
    //        |        from
    //        |        (
    //        |            select
    //        |            a.*
    //        |            ,b.product_name
    //        |            ,c.city_name
    //        |            ,c.area
    //        |            from user_visit_action a
    //        |            join product_info b on a.click_product_id= b.product_id
    //        |            join city_info c on a.city_id = c.city_id
    //        |        )t1
    //        |        group by area,product_name
    //        |    )t2
    //        |)t3
    //        |where rk<=3
    //        |""".stripMargin).show

    // 关闭资源
    spark.close()
  }

  case class Buff(var total: Long, var cityMap: mutable.Map[String, Long])

  /**
   * IN : city_name STRING
   * BUF : finish聚合结果,根据BUF的元素来处理,百分比 <= total/(cicy_name,cnt)
   * 【total,((city_name1,cnt),(city_name2,cnt),...)】
   * total,Map[String,Long]  样例类封装
   * ON : 北京 21.2%，天津 13.2%，其他 65.6%   STRING  取前2,其他累计为其他
   */
  class CityCntUADF extends Aggregator[String, Buff, String] {
    // 初始化缓冲区,给默认值
    override def zero: Buff = {
      Buff(0L, mutable.Map[String, Long]())
    }

    // 缓冲区内计算
    override def reduce(buff: Buff, city_name: String): Buff = {
      // 每计算一个city_name,总量+1,map中对应城市的该数量+1
      buff.total += 1
      val newCnt = buff.cityMap.getOrElse(city_name, 0L) + 1
      buff.cityMap.update(city_name, newCnt)
      buff
    }

    // 合并Map
    override def merge(buff1: Buff, buff2: Buff): Buff = {
      buff1.total += buff2.total
      val map1: mutable.Map[String, Long] = buff1.cityMap
      val map2: mutable.Map[String, Long] = buff2.cityMap
      buff1.cityMap = map1.foldLeft(map2) {
        case (mergeMap, (city_name, cnt)) => {
          var newCnt = mergeMap.getOrElse(city_name, 0L) + cnt
          mergeMap.update(city_name, newCnt)
          mergeMap
        }
      }
      buff1

    }

    // 将统计的结果生成字符串信息输出 eg:  北京 21.2%，天津 13.2%，其他 65.6%
    override def finish(buff: Buff): String = {
      // ListBuffer 缓存结果
      val remarkList = ListBuffer[String]()
      val total = buff.total
      val cityMap = buff.cityMap.toList
      // 转成list后排序取前2
      val twocityList: List[(String, Long)] = cityMap.sortWith(
        (t1, t2) => {
          t1._2 > t2._2 // 根据city_name对应的value数量进行倒叙排序
        }
      ).take(2)

      // 搞一个标记flag 来判断cityMap 城市数量>2
      val hasMore = cityMap.size > 2

      var sum = 0L
      twocityList.foreach {
        case (city_name, cnt) => {
          remarkList.append(s"${city_name} ${cnt * 100 / total}%")
          sum += cnt * 100/ total
        }
      }
      if (hasMore) {
        remarkList.append(s"其他 ${100-sum}%")
      }

      // 结果string 按照,隔开
      remarkList.mkString(",")


    }

    // 固定模式
    override def bufferEncoder: Encoder[Buff] = Encoders.product

    // 根据out给类型
    override def outputEncoder: Encoder[String] = Encoders.STRING
  }


}
