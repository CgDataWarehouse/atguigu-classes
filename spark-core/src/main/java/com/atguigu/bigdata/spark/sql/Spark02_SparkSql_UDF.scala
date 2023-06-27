package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object Spark02_SparkSql_UDF {
  def main(args: Array[String]): Unit = {

    //TODO 创建SparkSql的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    // 命令行对象 sparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    import spark.implicits._
    // TODO 执行逻辑操作
    val df: DataFrame = spark.read.json("datas/user.json")
    df.createOrReplaceTempView("user")

    // 自定义函数
    spark.udf.register("prefixName",(name:String)=>{"Name:" + name})
    // 使用自定义函数prefixName
    spark.sql("select age,prefixName(username) from user").show()



    // TODO 关闭环境
    spark.close()
  }

  case class User(id: Int, name: String, age: Int)

}
