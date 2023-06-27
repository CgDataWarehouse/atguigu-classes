package com.atguigu.bigdata.spark.core.acc

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark03_Acc_WordCount {
  def main(args: Array[String]): Unit = {
    // todo  准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Acc")
    // 上下文对象
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List("hello","hello","world"))

    // 累加器实现 wordcount
    // 需要自定义累加器

    // 创建累加器对象
    val wcAcc = new MyAccumulator()
    // 注册自定义累加器对象
    sc.register(wcAcc)


    // 在行动算子中使用自定义累加器
    rdd.foreach(
      num => {
        wcAcc.add(num)
      }
    )

    println(wcAcc.value)


    sc.stop()
  }
}

/**
 *  自定义数据累加器: WordCount   类型为Map[String,Long]
 *
 *  1. 继承AccmulatorV2,定义泛型
 *  IN: 累加器输入的数据类型 String
 *  OUT:累加器返回的数据类型  Map[String,Long]
 *
 *  2. 重写方法
 */
class MyAccumulator() extends AccumulatorV2[String,mutable.Map[String,Long]]{
  private var wcMap =mutable.Map[String,Long]()  // 声明一个累加器的值,为空Map

  // 判断是否为初始状态
  override def isZero: Boolean = {
    wcMap.isEmpty  // 如果为空,表示初始状态
  }

  // 复制累加器
  override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = {
    new MyAccumulator()
  }

  // 重置累加器
  override def reset(): Unit = {
    wcMap.clear()
  }

  // 获取累加器需要计算的值  累加器传入num这个word参数,计算出此时累加器的值
  override def add(word: String): Unit = {
    val newCnt = wcMap.getOrElse(word,0L) + 1  // 累加器key无这个word,则0+1,有则 +1

    //更新累加器的值,这个值是一个Map[String,Long]
    wcMap.update(word,newCnt)
  }

  // Driver端合并多个累加器
  override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]): Unit = {
    // 多个累加器合并,本案例就是 两两 Map合并
    val map1 =this.wcMap
    val map2 =other.value

    map2.foreach{
      case (word,count) =>{
        val newCount = map1.getOrElse(word, 0L) + count
        map1.update(word,newCount)
      }
    }
  }

  // 获取累加器结果
  override def value: mutable.Map[String, Long] = {
    wcMap  // 直接返回结果值
  }
}
