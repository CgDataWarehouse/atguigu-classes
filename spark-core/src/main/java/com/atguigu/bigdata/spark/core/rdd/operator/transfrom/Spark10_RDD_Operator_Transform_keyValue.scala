package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object Spark10_RDD_Operator_Transform_keyValue {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    // todo key - value类型

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    val mapRdd = rdd.map((_, 1))
    // partitionBy 根据指定的分区规则对数据进行重分区
    val newRdd = mapRdd.partitionBy(new HashPartitioner(2))
    newRdd.saveAsTextFile("output")
    sc.stop()
  }
}
