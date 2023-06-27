package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark09_RDD_Operator_Transform_intersection {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    // todo 双Value类型

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    val rdd1 = sc.makeRDD(List(3, 4, 5, 6))

    //todo 交集
    val rdd2: RDD[Int] = rdd.intersection(rdd1)
    println(rdd2.collect().mkString(","))
    //todo 并集
    val rdd3 = rdd.union(rdd1)
    println(rdd3.collect().mkString(","))
    //todo 差集
    val rdd4: RDD[Int] = rdd.subtract(rdd1)
    println(rdd4.collect().mkString(","))
    //todo 拉链
    //拉链的rdd数据源要求分区和分区中数量保持一致
    val rdd5: RDD[(Int, Int)] = rdd.zip(rdd1)
    println(rdd5.collect().mkString(","))
    sc.stop()
  }
}
