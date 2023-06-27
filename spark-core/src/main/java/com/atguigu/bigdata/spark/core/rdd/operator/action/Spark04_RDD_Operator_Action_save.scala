package com.atguigu.bigdata.spark.core.rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark04_RDD_Operator_Action_save {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Action")
    // 上下文对象
    val sc = new SparkContext(sparkConf)

    //    val rdd = sc.makeRDD(List(4,1, 2, 3),2)
    val rdd1 = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("b", 1), ("c", 2)
    ))

    rdd1.saveAsTextFile("output")
    rdd1.saveAsObjectFile("output1")
    rdd1.saveAsSequenceFile("output2")  //saveAsSequenceFile 要求数据类型为key-value键值类型
    sc.stop()
  }
}
