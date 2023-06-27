package com.atguigu.bigdata.spark.core.rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark05_RDD_Operator_Action_foreach {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Action")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    val rdd1 = sc.makeRDD(List(1, 2, 3, 4))

    // foreach 其实是Driver 端内存集合的循环遍历方法
    rdd1.collect().foreach(println)

    println("*******************")
    // foreach 其实是Executor 端内存数据打印
    // rdd的方法可以将计算逻辑发送到Executor分布式节点执行
    // 为了区分rdd方法和sccala集合的方法 => rdd的方法外部操作都是在Driver端执行的,方法内部的逻辑代码是在Executor端执行,取名算子
    rdd1.foreach(println)

    sc.stop()
  }
}
