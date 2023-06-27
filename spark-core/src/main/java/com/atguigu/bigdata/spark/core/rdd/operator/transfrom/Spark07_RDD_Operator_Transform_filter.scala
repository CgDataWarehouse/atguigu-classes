package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.text.SimpleDateFormat
import java.util.Date

object Spark07_RDD_Operator_Transform_filter {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 3, 5, 7, 9, 4,6))
    val makeRdd: RDD[Int] = rdd.filter(
      _ % 2 == 0
    )

    makeRdd.collect().foreach(println)
    sc.stop()
  }
}
