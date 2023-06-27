package com.atguigu.bigdata.spark.streaming

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming05_State {
  def main(args: Array[String]): Unit = {
    // TODO 环境准备

    // StreamingContext 创建时 需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期(采集周期)   eg.3s采集周期
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    // 缓冲区检查点目录,使用有状态操作时,需要指定
    ssc.checkpoint("checkpoint")

    //cmd windows下输入 nc -lp 9999
    // 无状态数据操作,只对当前的采集周期内的数据进行处理
    // 在某些场合下,需要保留数据统计结果(状态),实现数据的汇总
    val datas = ssc.socketTextStream("localhost", 9999)
    val wordToOne: DStream[(String, Int)] = datas.map((_, 1))

    // 这里reduceByKey 不能用了,仍然统计的是当前采集周期
    //    val wordCount: DStream[(String, Int)] = wordToOne.reduceByKey(_ + _)

    //updateStateByKey :根据key对数据的状态进行更新
    // 传递的参数含有两个值
    // 第一个值表示相同的key的value数据
    // 第二个参数表示缓冲区相同key的value数据
    val state: DStream[(String, Int)] = wordToOne.updateStateByKey(
      (seq: Seq[Int], buff: Option[Int]) => {
        val newCount =buff.getOrElse(0)+seq.sum
        Option(newCount)
      }
    )
    state.print()

    // 1. 启动采集器
    ssc.start()
    // 2. 等待采集器的关闭
    ssc.awaitTermination()
  }

}
