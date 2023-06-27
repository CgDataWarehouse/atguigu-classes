package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql._

object Spark05_SparkSql_Hive {
  def main(args: Array[String]): Unit = {

    //TODO 创建SparkSql的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark = SparkSession.builder().enableHiveSupport().config(sparkConf).getOrCreate()


    // 使用sparksql连接外置的hive
    // 1. 拷贝Hive-size.xml文件到classpath下
    // 2.启用hive的支持 .enableHiveSupport()
    // 3.增加对应的依赖关系(包含mysql的驱动),resources添加hive-site.xml,同时需要拷贝到target-classes下
    // 否则会报 Unable to instantiate SparkSession with Hive support because Hive classes are not found.
    spark.sql("show tables").show()



    // TODO 关闭环境
    spark.close()
  }


}
