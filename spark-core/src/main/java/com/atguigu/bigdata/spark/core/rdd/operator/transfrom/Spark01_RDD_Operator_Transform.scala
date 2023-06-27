package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Spark01_RDD_Operator_Transform {
  def main(args: Array[String]): Unit = {
    // 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd")
    // 创建上下文对象
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd = sc.textFile("datas/apache.log")

    rdd.map{
      line => {
        val data: Array[String] = line.split(" ")
        data(6)
      }
    }.collect().foreach(println)

    // 关闭环境
    sc.stop()
  }
}
