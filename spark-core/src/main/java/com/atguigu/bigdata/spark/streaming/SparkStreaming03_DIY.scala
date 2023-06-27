package com.atguigu.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

import java.util.Random
import scala.collection.mutable

object SparkStreaming03_DIY {
  def main(args: Array[String]): Unit = {
    // TODO 环境准备

    // StreamingContext 创建时 需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf =new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期(采集周期)   eg.3s采集周期
    val ssc = new StreamingContext(sparkConf,Seconds(3))

    // TODO 逻辑处理

    // receiverStream() 调用自定义采集器
    val messageDs: ReceiverInputDStream[String] = ssc.receiverStream(new MyReceiver())
    messageDs.print()


    // 1. 启动采集器
    ssc.start()
    // 2. 等待采集器的关闭
    ssc.awaitTermination()
  }


  /**
   *  自定义数据采集器
   *  1.继承Revevier,定义泛型
   *  2.显示参数:存储级别
   *  3.重写方法
   */
  class MyReceiver extends Receiver[String](StorageLevel.MEMORY_ONLY){

    private var flg = true
    override def onStart(): Unit = {

      // 启动的时候应该有一个线程独立于当前运行的线程
      new Thread(new Runnable {
        override def run(): Unit = {
          // run方法干嘛? 就是不断产生数据
          while(flg){

            // nextInt会随机生成一个整数，这个整数的范围就是int类型的范围-2^31 ~ 2^31-1
            // ,但是如果在nextInt()括号中加入一个整数a那么，这个随机生成的随机数范围就变成[0,a)
            val message ="采集的数据为:" + new Random().nextInt(10).toString

            store(message)
            // 数据产生不用那么快,每500ms暂停一下
            Thread.sleep(500)
          }
        }
      }).start()
    }

    override def onStop(): Unit = {
      flg = false
    }
  }
}
