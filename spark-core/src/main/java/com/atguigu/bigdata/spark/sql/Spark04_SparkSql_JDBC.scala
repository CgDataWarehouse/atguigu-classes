package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.functions.expr

object Spark04_SparkSql_JDBC {
  def main(args: Array[String]): Unit = {

    //TODO 创建SparkSql的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    import spark.implicits._

    // 读取mysql数据
    val df: DataFrame = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://127.0.0.1:3306/spark-sql")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "root")
      .option("dbtable", "user")
      .load()


    // 保存数据,到mysql spark-sql库下的user1表,append追加模式
//    df.write
//      .format("jdbc")
//      .option("url", "jdbc:mysql://127.0.0.1:3306/spark-sql")
//      .option("driver", "com.mysql.jdbc.Driver")
//      .option("user", "root")
//      .option("password", "root")
//      .option("dbtable", "user1")
//      .mode("append")
//      .save()



    // TODO 关闭环境
    spark.close()
  }


}
