package com.atguigu.bigdata.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_File_Par {
  def main(args: Array[String]): Unit = {
    // TODO 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd")

    //    sparkConf.set("spark.default.parallelism","3")  // 设置分区数
    // 创建上下文对象
    val sc: SparkContext = new SparkContext(sparkConf)

    // TODO 创建rdd

    //创建rdd 从文件中创建RDD,将文件中的数据作为处理的数据源 ,默认也可以设定分区
    // minPartitions : 最小分区数量
    // math.min(defaultKParallelism,2)

    //total_size = 7  --1 2 3 +回车换行
    // goalSize =7 /2 =3 (byte)
    //7 /3 =2...1 (1/3 >1.1) +1 个分区 最终等于 3个分区
    val rdd = sc.textFile("datas/1.txt", 2)

    //将处理的数据保存成分区文件
    rdd.saveAsTextFile("output")
    // TODO 关闭环境
    sc.stop()
  }
}
