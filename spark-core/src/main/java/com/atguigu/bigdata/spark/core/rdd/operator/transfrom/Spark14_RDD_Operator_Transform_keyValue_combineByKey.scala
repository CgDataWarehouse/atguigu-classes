package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark14_RDD_Operator_Transform_keyValue_combineByKey {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    // todo key - value类型

    val rdd = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("b", 3),
      ("b", 4), ("b", 5), ("a", 6)
    ), 2)

    println("---------------------------------------")
    // todo 3.聚合计算时,获取相同key的数据平均值

    // combineByKey :方法共三个参数
    //第一参数:将相同key的第一个数据进行结构的转换
    //第二参数:分区内的计算规则
    //第三参数:分区间的计算规则
    val newRdd = rdd.combineByKey(
      value => {
        (value, 1)
      },
      (t: (Int, Int), v) => {
        (t._1 + v, t._2 + 1)
      },
      (t1: (Int, Int), t2: (Int, Int)) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )


    val result: RDD[(String, Int)] = newRdd.mapValues {
      case (value, cnt) => {
        value / cnt
      }
    }
    result.collect().foreach(println)


    sc.stop()
  }
}
