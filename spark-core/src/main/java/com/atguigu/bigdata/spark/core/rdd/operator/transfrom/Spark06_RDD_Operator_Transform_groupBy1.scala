package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark06_RDD_Operator_Transform_groupBy1 {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)

    val rdd: RDD[String] = sc.makeRDD(List("hello", "scala", "spark","Hi"))

    // 根据首字母分组
    val groupByRdd: RDD[(Char, Iterable[String])] = rdd.groupBy(
      _.charAt(0)
    )

    groupByRdd.collect().foreach(println)


    sc.stop()
  }
}
