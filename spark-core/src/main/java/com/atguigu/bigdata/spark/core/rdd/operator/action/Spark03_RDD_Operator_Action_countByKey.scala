package com.atguigu.bigdata.spark.core.rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_Operator_Action_countByKey {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Action")
    // 上下文对象
    val sc = new SparkContext(sparkConf)

    //    val rdd = sc.makeRDD(List(4,1, 2, 3),2)
    val rdd1 = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("b", 1), ("c", 2)
    ))

    //countByValue :不是元素key-value的value值,而是rdd的值元素
    //val intToLong: collection.Map[Int, Long] = rdd.countByValue()
    //println(intToLong)
    val stringToLong: collection.Map[String, Long] = rdd1.countByKey()
    println(stringToLong)
    sc.stop()
  }
}
