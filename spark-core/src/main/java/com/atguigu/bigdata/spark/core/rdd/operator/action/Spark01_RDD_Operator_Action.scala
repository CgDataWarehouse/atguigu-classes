package com.atguigu.bigdata.spark.core.rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Operator_Action {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Action")
    // 上下文对象
    val sc = new SparkContext(sparkConf)

    val rdd = sc.makeRDD(List(4,1, 2, 3))


    //todo -行动算子
    //行动算子,是触发作业job执行的方法
    //底层代码调用的是环境对象的runjob方法
    //底层代码中会创建ActiveJob,并提交执行
    //    rdd.collect()

    //    val i = rdd.reduce(_ + _)
    //    println(i)

    val result: Array[Int] = rdd.collect()
    println(result.mkString(","))

    val result1: Array[Int] = rdd.takeOrdered(3)(Ordering.Int.reverse)
    println(result1.mkString(","))

    sc.stop()
  }
}
