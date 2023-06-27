package com.atguigu.bigdata.spark.core.rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Operator_Action_aggregate {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Action")
    // 上下文对象
    val sc = new SparkContext(sparkConf)

    val rdd = sc.makeRDD(List(4,1, 2, 3),2)


    // aggregateByKey : 初始值只会参与分区内计算
    // aggregate: 初始值参与组内计算,并且也参与组间计算
    // fold :折叠操作，aggregate 的简化版操作 分区内和分区间的计算规则相同
    val result: Int = rdd.aggregate(10)(_ + _, _ + _)

    println(result)

    sc.stop()
  }
}
