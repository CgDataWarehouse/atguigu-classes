package com.atguigu.bigdata.spark.core.acc

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark01_Bc {
  def main(args: Array[String]): Unit = {
    // todo  准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Acc")
    // 上下文对象
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(
      List(
        ("a",1),("b",2),("c",5)
      )
    )

    val map =mutable.Map(("a",1),("b",6),("c",8))

    // join 性能很差,且可能发生笛卡尔积
    // ("a",(1,1)), ("b",(2,6)) ....

    rdd.map{
      case (word,cnt) =>{
        val newCnt = map.getOrElse(word, 0)
        (word,(cnt,newCnt))
      }
    }.collect().foreach(println)



    sc.stop()
  }
}
