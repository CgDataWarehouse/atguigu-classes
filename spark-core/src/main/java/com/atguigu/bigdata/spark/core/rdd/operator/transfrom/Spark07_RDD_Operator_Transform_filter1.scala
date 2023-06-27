package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.text.SimpleDateFormat
import java.util.Date

object Spark07_RDD_Operator_Transform_filter1 {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)

    val rdd: RDD[String] = sc.textFile("datas/apache.log")

    val makeRdd = rdd.map(
      line => {
        val datas = line.split(" ")
        val time: String = datas(3)
        val sdf = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
        val date: Date = sdf.parse(time)
        val sdf1 = new SimpleDateFormat("dd/MM/yyyy")
        val yMd: String = sdf1.format(date)
        (yMd, datas(6))
      }
    )
    // 需求: 对日志数据 取2015年5月17日的请求路径
    val result: RDD[(String, String)] = makeRdd.filter {
      case (a, b) => {
        a == "17/05/2015"
      }
    }

    result.collect().foreach(println)

    sc.stop()
  }
}
