package com.atguigu.bigdata.spark.core.acc

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark02_Bc {
  def main(args: Array[String]): Unit = {
    // todo  准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Acc")
    // 上下文对象
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(
      List(
        ("a", 1), ("b", 2), ("c", 5)
      )
    )

    //    val list = List( ("a",4), ("b", 5), ("c", 6), ("d", 7) )
    //    val listtomap: Map[String, Int] = list.toMap

    val map = mutable.Map(("a", 1), ("b", 6), ("c", 8))

    // 广播变量的使用
    val bc: Broadcast[mutable.Map[String, Int]] = sc.broadcast(map)

    rdd.map {
      case (word, cnt) => {
        val newCnt = bc.value.getOrElse(word, 0)
        (word, (cnt, newCnt))
      }
    }.collect().foreach(println)


    sc.stop()
  }
}
