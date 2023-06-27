package com.atguigu.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext, StreamingContextState}

object SparkStreaming08_Resume {
  def main(args: Array[String]): Unit = {
    // TODO 从检查点恢复数据
   val ssc= StreamingContext.getOrCreate(
      "checkpoint", ()=>{
        // StreamingContext 创建时 需要传递两个参数
        // 第一个参数表示环境配置
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
        // 第二个参数表示批量处理的周期(采集周期)   5s采集周期
        val ssc = new StreamingContext(sparkConf, Seconds(5))

        ssc.checkpoint("checkpoint")

        val lines = ssc.socketTextStream("locolhost",9999)

        val wordToOne = lines.map((_, 1))

        wordToOne.print()

        ssc
      }
    )


    ssc.checkpoint("checkpoint")

    // 1. 启动采集器
    ssc.start()
    // 2. 等待采集器的关闭  等待会阻塞当前运行的线程,也就不能执行后面的stop,所以需要一个新的线程(非当前主线程)负责关闭采集器
    ssc.awaitTermination()


  }

}
