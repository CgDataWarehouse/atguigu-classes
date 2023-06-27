package com.atguigu.bigdata.spark.core.wordcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_WordCount {
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
    val lines: RDD[String] = sc.textFile("datas/word.txt")
    //2.扁平化操作,形成一个个的单词
    val words: RDD[String] = lines.flatMap(_.split(" "))
    println(words)
    //3.相同单词分组
    val wordGroup: RDD[(String, Iterable[String])] = words.groupBy(word => word)

    //4.分组后的数据进行map转换
    val wordToCount: RDD[(String, Int)] = wordGroup.map {
      kv => {
        (kv._1, kv._2.size)
      }
    }
    //5.将转换结果采集到集中台进行打印
    val array: Array[(String, Int)] = wordToCount.collect()

    //6.数组循环遍历
    array.foreach(
      println
    )
    //TODO 关闭连接
    sc.stop()
  }
}
