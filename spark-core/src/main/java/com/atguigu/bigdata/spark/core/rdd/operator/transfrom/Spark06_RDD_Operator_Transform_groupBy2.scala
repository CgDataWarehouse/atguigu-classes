package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.text.SimpleDateFormat
import java.util.Date

object Spark06_RDD_Operator_Transform_groupBy2 {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)

    val rdd: RDD[String] = sc.textFile("datas/apache.log")



    // 需求: 对日志每个小时段求pv
    val timeRdd: RDD[(String, Int)] = rdd.map {
      line => {
        val datas: Array[String] = line.split(" ")
        //        datas(3).substring(0,)
        val time: String = datas(3)

        val sdf = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
        //SimpleDateFormat类中parse()方法用于将输入的特定字符串转换成Date类的对象
        val date: Date = sdf.parse(time)
        val sdf1 = new SimpleDateFormat("HH")
        // format 对Date对象进行格式改变
        val hour: String = sdf1.format(date)
        (hour, 1)
      }
    }
    //    timeRdd.reduceByKey(_+_)
    val value: RDD[(String, Iterable[(String, Int)])] = timeRdd.groupBy(_._1)

    val result: RDD[(String, Int)] = value.map {
      case (key, iter) => {
        (key, iter.size)
      }
      //      kv => {
      //        (kv._1,kv._2.size)
      //      }
    }


    result.collect().foreach(println)
    sc.stop()
  }
}
