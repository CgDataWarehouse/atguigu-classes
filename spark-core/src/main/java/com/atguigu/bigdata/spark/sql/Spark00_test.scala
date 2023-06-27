package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.expressions.Window


object Spark00_test {
  def main(args: Array[String]): Unit = {
    // 环境准备
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName(this.getClass.getName)
    // sparkSession
    val spark = SparkSession.builder().enableHiveSupport().config(sparkConf).getOrCreate()


    // ds
    val df = spark.read.format("text").load("datas/word.txt")


    // dsl
    import org.apache.spark.sql.functions._
    // selectExpr 对指定字段调用udf函数
    df.selectExpr("explode(split(value,' ')) as col").groupBy("col").count().orderBy(desc("count"))
    //    df.explode("value","col"){x:String=>x.split(" ")}
    import spark.implicits._
    val orders = Seq(
      ("o1", "s1", "2017-05-01", 100),
      ("o2", "s1", "2017-05-02", 200),
      ("o3", "s2", "2017-05-01", 300)
    ).toDF("order_id", "seller_id", "pay_time", "price")

    // 店铺订单顺序
    val rankSpec = Window.partitionBy("seller_id").orderBy('pay_time)

    val shopOrderRank =
      orders.withColumn("rank", dense_rank.over(rankSpec))

    // 店铺这个订单及前一单的价格和
    val sumSpec = Window.partitionBy("seller_id").orderBy("pay_time")
      .rowsBetween(-1, 0)

    orders.select(
      $"order_id".as("orderID")
      ,'seller_id
      ,to_date('pay_time,"yyyy-MM-dd")
      ,'price
    ).show()

    // selectExpr 对指定字段调用UDF函数，或者指定别名等 传入的是exprs sql表达式,注意表达式里的字段不用$或者'
    orders.selectExpr("concat('test',order_id) as col").show()



    val result: DataFrame = orders.withColumn("cumulative_sum", sum('price).over(sumSpec))

    result.show()

    spark.close()
  }
}
