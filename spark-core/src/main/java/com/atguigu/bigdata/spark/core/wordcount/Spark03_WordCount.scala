package com.atguigu.bigdata.spark.core.wordcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark03_WordCount {
  def main(args: Array[String]): Unit = {
    // Application
    // Spark框架
    //TODO 建立和spark框架的连接
    //JDBC: Connection
    // 创建 Spark 运行配置对象
    val sparkConf = new SparkConf().setMaster("local").setAppName("WorkCount")
    // 创建 Spark 上下文环境对象（连接对象）
    val sc: SparkContext = new SparkContext(sparkConf)
    //TODO 执行业务操作
    //1.读取文件,获取一行一行的数据
    val lines: RDD[String] = sc.textFile("datas")

    val wordTuples: RDD[(String, Int)] = lines.flatMap(
      _.split(" ")
    ).map((_, 1))

    //    val value: RDD[(String, Int)] = wordTuples.groupBy(_._1).mapValues(
    //      rdd => {
    //        rdd.map(_._2).sum
    //      }
    //    )
    //    val wordGroup: RDD[(String, Iterable[(String, Int)])] = wordTuples.groupBy(_._1)
    //    val value: RDD[(String, Int)] = wordGroup.map {
    //      case (word, list) => {
    //        list.reduce(    //reduce 参数是 op : (A1,A1) => A1  此时每个A1都是list的一个元素tuple
    //          (t1, t2) => {
    //            (t1._1, t1._2 + t2._2)
    //          }
    //        )
    //      }
    //    }

    // spark提供了更多的功能,可以将分组和聚合使用一个方法实现
    // reduceByKey :相同的key数据,对value进行reduce 聚合
    //    wordTuples.reduceByKey((x,y) => {x+y})
    val value: RDD[(String, Int)] = wordTuples.reduceByKey(_ + _)

    val result: Array[(String, Int)] = value.collect()
    result.foreach(println)
    sc.stop()
  }
}
