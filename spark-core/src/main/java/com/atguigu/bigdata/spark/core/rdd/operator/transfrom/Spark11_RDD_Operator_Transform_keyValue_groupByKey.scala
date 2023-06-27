package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}


object Spark11_RDD_Operator_Transform_keyValue_groupByKey {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    // todo key - value类型

    val rdd = sc.makeRDD(List(
      ("a", 1), ("b", 2), ("a", 2), ("a", 1)
    ))

    //将数据源的数据根据 key 对 value 进行分组

//    rdd.groupByKey(new HashPartitioner(1))  //默认hash分区器
    rdd.groupByKey()
      .map{
        case (key,iter)=>{
          (key,iter.sum)
        }
      }.collect().foreach(println)
    sc.stop()
  }
}
