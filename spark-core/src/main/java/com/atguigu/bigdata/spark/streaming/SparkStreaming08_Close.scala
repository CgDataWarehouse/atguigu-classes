package com.atguigu.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext, StreamingContextState}

object SparkStreaming08_Close {
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

    wordToOne.print()

    // 1. 启动采集器
    ssc.start()

    // 创建新的线程
    // 一个新的线程(非当前主线程)负责关闭采集器
    // 需要在第三方程序中增加关闭状态
    new Thread(
      new Runnable {
        override def run(): Unit = {
          // 3.stop 优雅关闭采集器
          // 当关闭的时候不是强制关闭,计算节点不再接受新的数据,将当前节点数据处理完了再关闭
          // mysql Table(stopSpark) =>Row =>data 类似到达标志
          // redis Data(K-V)
          // zk   /stopSpark
          // hdfs /stopSpark
          while(true){
            if(true){ // 伪代码, 具体可以参考sparkStreaming笔记
              // 获取SparkStreaming 的状态
              val state: StreamingContextState = ssc.getState()
              if(state == StreamingContextState.ACTIVE){
                ssc.stop(true,true)
              }
            }
            // 取不到先睡眠
            Thread.sleep(5000)
          }

        }
      }
    ).start()

    // 2. 等待采集器的关闭  等待会阻塞当前运行的线程,也就不能执行后面的stop,所以需要一个新的线程(非当前主线程)负责关闭采集器
    ssc.awaitTermination()


  }

}
