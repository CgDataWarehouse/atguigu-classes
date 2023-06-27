package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_Operator_Transform {
  def main(args: Array[String]): Unit = {

    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)
    // todo rdd 算子
    val rdd = sc.makeRDD(List(1, 3, 5, 7), 2) // 2 分区

    // 1.取各分区最大值
    //    val makeRdd: RDD[Int] = rdd.mapPartitions {
    //      iter => {
    //        List(iter.max).iterator
    //      }
    //    }

    // 2.取第二个分区
    val makeRdd: RDD[Int] = rdd.mapPartitionsWithIndex(
      (index, iter) => {
        if (index == 1) {
          iter
        } else {
          Nil.iterator
        }
      }
    )


    makeRdd.collect().foreach(println)


    // todo 停止环境
    sc.stop()

  }
}
