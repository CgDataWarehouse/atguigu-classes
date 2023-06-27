package com.atguigu.bigdata.spark.core.rdd.serial

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Serializable_function {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("serial")
    // todo 上下文对象
    val sc = new SparkContext(sparkConf)


    //创建一个 RDD
    val rdd: RDD[String] = sc.makeRDD(Array("hello world", "hello spark", "hive", "atguigu"))

    val search = new Search("h")
    search.getMatch1(rdd).collect().foreach(println)
    search.getMatch2(rdd).collect().foreach(println)


    sc.stop()
  }
}

//extends Serializable
// case class
class Search(query: String) extends Serializable{
  def isMatch(s: String): Boolean = {
    s.contains(query)
  }

  // 函数序列化案例
  def getMatch1(rdd: RDD[String]): RDD[String] = {
    //rdd.filter(this.isMatch)
    rdd.filter(isMatch)
  }

  // 属性序列化案例
  def getMatch2(rdd: RDD[String]): RDD[String] = {
    //rdd.filter(x => x.contains(this.query))
    //    rdd.filter(x => x.contains(query))
    val q = query
    rdd.filter(x => x.contains(q))
  }
}
