package com.atguigu.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming06_State_join {
  def main(args: Array[String]): Unit = {
    // TODO 环境准备

    // StreamingContext 创建时 需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期(采集周期)   5s采集周期
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val data9999 = ssc.socketTextStream("locolhost",9999)
    val data8888 = ssc.socketTextStream("locolhost",8888)

    val map9999 = data9999.map((_, 9))
    val map8888 = data9999.map((_, 8))

    // DSTream 的 join 其实就是两个rdd的join
    val joinDs: DStream[(String, (Int, Int))] = map9999.join(map8888)

    joinDs.print()

    // 1. 启动采集器
    ssc.start()
    // 2. 等待采集器的关闭
    ssc.awaitTermination()
  }

}
