package com.atguigu.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming06_State_Window1 {
  def main(args: Array[String]): Unit = {
    // TODO 环境准备

    // StreamingContext 创建时 需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期(采集周期)   5s采集周期
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    ssc.checkpoint("checkpoint")

    val lines = ssc.socketTextStream("locolhost",9999)

    val wordToOne = lines.map((_, 1))

    // 窗口的范围应该是采集周期的整数倍
    // 窗口可以滑动,默认情况是一个步长也就是一个采集周期进行滑动
    // 滑窗可以会有重复数据,为了避免,改变滑动的步长 ,窗口和步长一致时,不会重复

    // 两个参数:一个窗口时长  一个滑动步长
    val wordCount: DStream[(String, Int)] =
      wordToOne.reduceByKeyAndWindow(
        (x:Int,y:Int) =>{x+y}   // 滑动窗口内处理
        ,(x:Int,y:Int) =>{x-y}  // 滑动窗口重复数据处理
        ,Seconds(15)            // 窗口大小
        ,Seconds(5)             // 步长
      )


    wordCount.print()

    // 1. 启动采集器
    ssc.start()
    // 2. 等待采集器的关闭
    ssc.awaitTermination()
  }

}
