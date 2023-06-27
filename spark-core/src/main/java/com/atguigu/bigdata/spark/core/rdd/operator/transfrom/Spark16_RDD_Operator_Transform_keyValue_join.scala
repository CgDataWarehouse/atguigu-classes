package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark16_RDD_Operator_Transform_keyValue_join {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    // todo key - value类型

    val rdd1 = sc.makeRDD(List(
      ("a", 1), ("b", 2), ("c", 3),
    ))
    val rdd2 = sc.makeRDD(List(
      ("d", 5), ("c", 6),("a", 4)
    ))

    // todo join,相同的key对应的value连接在一起,形成元组 ,key没有匹配上的不会出现 ,相同的key多次出现结果也会多次出现(发散)
    val result: RDD[(String, (Int, Int))] = rdd1.join(rdd2)
    result.collect().foreach(println)


    sc.stop()
  }
}
