package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark15_RDD_Operator_Transform_keyValue {
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

    rdd.reduceByKey(_ + _) // wordcount
    rdd.aggregateByKey(0)(_ + _, _ + _) // wordcount
    rdd.foldByKey(0)(_ + _) // wordcount
    rdd.combineByKey(v => v, (x: Int, y) => x + y, (x: Int, y: Int) => x + y) // wordcount

    // 源码解析 4个方法的区别
    //reduceByKey:   combineByKeyWithClassTag[V]((v: V) => v, func, func)
    //foldByKey:      combineByKeyWithClassTag[V]((v: V) => cleanedFunc(createZero(), v), cleanedFunc, cleanedFunc)
    //aggregateByKey:  combineByKeyWithClassTag[U]((v: V) => cleanedSeqOp(createZero(), v), cleanedSeqOp, combOp)
    //combineByKey :    combineByKeyWithClassTag(createCombiner, mergeValue, mergeCombiners)

    //    reduceByKey: 相同 key 的第一个数据不进行任何计算，分区内和分区间计算规则相同
    //    FoldByKey: 相同 key 的第一个数据和初始值进行分区内计算，分区内和分区间计算规则相同
    //    AggregateByKey：相同 key 的第一个数据和初始值进行分区内计算，分区内和分区间计算规则可以不相同
    //    CombineByKey:当计算时，发现数据结构不满足要求时，可以让第一个数据转换结构。分区内和分区间计算规则不相同。




    sc.stop()
  }
}
