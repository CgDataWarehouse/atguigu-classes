package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark17_RDD_Operator_Transform_keyValue_cogroup {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    // todo key - value类型

    val rdd1 = sc.makeRDD(List(
      ("a", 1), ("b", 2)
    ))
    val rdd2 = sc.makeRDD(List(
      ("a",4), ("b", 5), ("c", 6), ("c", 7)
    ))


    // cogroup : connect + group
    val result: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)
    result.collect().foreach(println)


    sc.stop()
  }
}
