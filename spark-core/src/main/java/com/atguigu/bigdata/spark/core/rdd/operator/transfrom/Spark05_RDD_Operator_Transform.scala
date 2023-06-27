package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark05_RDD_Operator_Transform {
  def main(args: Array[String]): Unit = {

    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)
    // todo rdd 算子
    val rdd = sc.makeRDD(List(List(1, 2), 3, List(4, 5)))

    // 扁平化
    val makeRdd = rdd.flatMap {
      data => {
        data match {
          case list: List[_] => list
          case data => List(data)
        }
      }
    }

    makeRdd.collect().foreach(println)


    // todo 停止环境
    sc.stop()

  }
}
