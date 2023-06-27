package com.atguigu.bigdata.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_File {
  def main(args: Array[String]): Unit = {
    // TODO 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd")
    // 创建上下文对象
    val sc: SparkContext = new SparkContext(sparkConf)

    // TODO 创建rdd
    // 从文件中创建RDD,将文件中的数据作为处理的数据源
    // path路径默认以当前环境的根路径为基准;可以写绝对路径,也可以写相对路径

    //    val rdd: RDD[Int] = sc.parallelize(list)
    //    sc.textFile("E:\\SparkProject\\atguigu-classes\\datas\\1.txt")
    // 也可以指定目录,读取多个文件/  path还可以使用通配符
    //    val rdd: RDD[String] = sc.textFile("datas/1.txt")
    //    val rdd = sc.textFile("datas")
    val rdd = sc.textFile("datas/1*.txt")
    // path 还可以是分布式存储系统路径HDFS
    //    sc.textFile("hdfs://hadoop102:8020/test.txt")
    rdd.collect().foreach(println)

    //!!! todo wholeTextFiles(path) :以文件路径为单位读取数据
    // 读取结果为元组,第一个元素表示文件路径,第二个表示文件内容

    // TODO 关闭环境
    sc.stop()
  }
}
