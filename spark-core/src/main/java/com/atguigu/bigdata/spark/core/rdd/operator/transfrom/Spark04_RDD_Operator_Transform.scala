package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark04_RDD_Operator_Transform {
  def main(args: Array[String]): Unit = {

    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)
    // todo rdd 算子
    val rdd = sc.makeRDD(List(1, 3, 5, 7)) // 没有指定分区

    // 求每个元素对应的分区 结果形式为 (index,元素)
    val makeRdd = rdd.mapPartitionsWithIndex(
      (index, iter) => {
        iter.map(
          //          num => (index,num)
          (index, _)
        )
      }
    )

    makeRdd.collect().foreach(println)


    // todo 停止环境
    sc.stop()

  }
}
