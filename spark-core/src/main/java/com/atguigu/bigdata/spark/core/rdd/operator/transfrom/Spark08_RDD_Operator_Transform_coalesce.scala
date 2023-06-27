package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.text.SimpleDateFormat
import java.util.Date

object Spark08_RDD_Operator_Transform_coalesce {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6), 3)

    // coalesce 减小分区,合并小任务,减小调度成本
    //    val rdd1: RDD[Int] = rdd.coalesce(2)
    //
    //    rdd1.saveAsTextFile("output")

    //    该操作内部其实执行的是 coalesce 操作，参数 shuffle 的默认值为 true。
    //    无论是将分区数多的RDD 转换为分区数少的RDD，还是将分区数少的 RDD 转换为分区数多的RDD，repartition 操作都可以完成，
    //    因为无论如何都会经 shuffle 过程。



    sc.stop()
  }
}
