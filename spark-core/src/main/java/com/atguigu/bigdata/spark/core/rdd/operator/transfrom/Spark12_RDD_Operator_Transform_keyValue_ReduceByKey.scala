package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark12_RDD_Operator_Transform_keyValue_ReduceByKey {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    // todo key - value类型

    val rdd = sc.makeRDD(List(
      ("a", 1), ("b", 2), ("a", 2), ("a", 1)
    ))

    // reduceByKey 基于相同的key进行value的聚合操作
    // scala 一般聚合操作是两两聚合,spark同理

    //reduceByKey 如果key的数据只有一个,不会参与两两运算
    val result: RDD[(String, Int)] = rdd.reduceByKey((x, y) => {
      println(s"x = ${x},y = ${y}")
      x + y
    })

    result.collect().foreach(println)

    sc.stop()
  }
}
