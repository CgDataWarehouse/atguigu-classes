package com.atguigu.bigdata.spark.core.rdd.builder

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Memory {
  def main(args: Array[String]): Unit = {
    // 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd")
    // 创建上下文对象
    val sc: SparkContext = new SparkContext(sparkConf)

    //创建rdd
    val list: List[Int] = List(1, 3, 2, 5)

    //    val rdd: RDD[Int] = sc.parallelize(list)
    val rdd: RDD[Int] = sc.makeRDD(list)

//    val rdd2: RDD[Int] = sc.parallelize(list) 避免重复创建同一个rdd

//    rdd.collect().foreach(println(_))
 rdd.collect().foreach(print)
    // 关闭环境

    sc.stop()
  }
}
