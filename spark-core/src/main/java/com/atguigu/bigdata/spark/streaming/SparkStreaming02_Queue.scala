package com.atguigu.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object SparkStreaming02_Queue {
  def main(args: Array[String]): Unit = {
    // TODO 环境准备

    // StreamingContext 创建时 需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf =new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期(采集周期)   eg.3s采集周期
    val ssc = new StreamingContext(sparkConf,Seconds(3))

    // TODO 逻辑处理
    //3.创建 RDD 队列
    val rddQueue = new mutable.Queue[RDD[Int]]()
    //4.创建 QueueInputDStream
    val inputStream = ssc.queueStream(rddQueue,oneAtATime = false)
    //5.处理队列中的 RDD 数据
    val mappedStream = inputStream.map((_,1))
    val reducedStream = mappedStream.reduceByKey(_ + _)
    //6.打印结果reducedStream.print()
    reducedStream.print()

    // 由于sparkStreaming是长期执行的任务,所以不能直接关闭
    // 如果main方法执行完毕,应用程序也会自动结束,所以不能让main执行完毕
    // ssc.stop()
    // 1. 启动采集器
    ssc.start()

    //8.循环创建并向 RDD 队列中放入 RDD
    for (i <- 1 to 5) {
      rddQueue += ssc.sparkContext.makeRDD(1 to 300, 10)
      Thread.sleep(2000)
    }

    // 2. 等待采集器的关闭
    ssc.awaitTermination()
  }
}
