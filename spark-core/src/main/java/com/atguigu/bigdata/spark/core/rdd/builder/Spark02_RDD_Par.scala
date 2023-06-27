package com.atguigu.bigdata.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Par {
  def main(args: Array[String]): Unit = {
    // TODO 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd")

    //    sparkConf.set("spark.default.parallelism","3")  // 设置分区数
    // 创建上下文对象
    val sc: SparkContext = new SparkContext(sparkConf)

    // TODO 创建rdd
    // 从文件中创建RDD,将文件中的数据作为处理的数据源
    // path路径默认以当前环境的根路径为基准;可以写绝对路径,也可以写相对路径

    //创建rdd
    val list: List[Int] = List(1, 2, 3, 4, 5)
    val rdd = sc.makeRDD(list)

    //将处理的数据保存成分区文件
    rdd.saveAsTextFile("output")
    // TODO 关闭环境
    sc.stop()
  }
}
