package com.atguigu.bigdata.spark.core.rdd.part

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object Spark01_RDD_Part {

  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("part")
    // todo 上下文对象
    val sc = new SparkContext(sparkConf)

    val rdd = sc.makeRDD(List(
      ("nba", "xxxxxxxxxx")
      , ("cba", "xxxxxxxxxxxxxxxx")
      , ("nba", "xxxxxxxxxxxxxxxxxxx")
      , ("other", "xxx")
    ), 3)

    val partRDD: RDD[(String, String)] = rdd.partitionBy(new MyPartitioner)
    partRDD.saveAsTextFile("output")

    sc.stop()
  }
}

/**
 * 自定义分区器
 * 1.继承Partitioner
 * 2.重写方法
 */

class MyPartitioner extends Partitioner {
  // 分区数量
  override def numPartitions: Int = 3 //如果需要灵活设置,可以参考 HashPartitioner 自定义分区器

  // 根据数据的key值返回数据所在的分区索引(从0开始)
  override def getPartition(key: Any): Int = {
    key match {
      case "nba" => 0
      case "cba" => 1
      case _ => 2
    }
  }
}
