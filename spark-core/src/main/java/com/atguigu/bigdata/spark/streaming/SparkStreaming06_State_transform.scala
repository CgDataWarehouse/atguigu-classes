package com.atguigu.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming06_State_transform {
  def main(args: Array[String]): Unit = {
    // TODO 环境准备

    // StreamingContext 创建时 需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期(采集周期)   eg.3s采集周期
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val lines = ssc.socketTextStream("locolhost",9999)

    // transform 将底层的RDD获取到后进行操作
    // Code 1 : Driver端 main方法走一遍
    // Dstream 功能不完事,需要代码周期性的执行
    val newDs: DStream[String] = lines.transform(
      rdd =>{
        rdd.map(
          // Code 2: 算子之外的仍然在Driver端执行 (周期性执行,反复执行)
          //code1和code2虽然都在Driver端执行,但是是有区别的,不能合一起,因为流式处理是按照采集周期执行的,rdd是周期性的执行
          str =>{
            // Code: Executor端
            str
          }
        )
      }
    )

    // Code : Driver 端
    val newDs1: DStream[String] = lines.map(
      data =>{
        // Code : Executor端
        data
      }
    )


    // 1. 启动采集器
    ssc.start()
    // 2. 等待采集器的关闭
    ssc.awaitTermination()
  }

}
