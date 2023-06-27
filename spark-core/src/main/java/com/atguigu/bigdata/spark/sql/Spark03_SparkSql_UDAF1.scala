package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, Row, SparkSession, functions}

object Spark03_SparkSql_UDAF1 {
  def main(args: Array[String]): Unit = {

    //TODO 创建SparkSql的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    // 命令行对象 sparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    // TODO 执行逻辑操作
    val df: DataFrame = spark.read.json("datas/user.json")
    df.createOrReplaceTempView("user")

    // 自定义函数
    //    spark.udf.register("prefixName", (name: String) => {
    //      "Name:" + name
    //    })

    // 强类型的自定义函数实现,functions.udaf 将强类型转成弱类型
    spark.udf.register("ageAvg",functions.udaf(new MyAvgUDAF()))
    // 使用自定义函数prefixName
    spark.sql("select ageAvg(age) from user").show()

    // TODO 关闭环境
    spark.close()
  }


  // 自定义聚合函数类:计算年龄平均值
  // 继承   org.apache.spark.sql.expressions.Aggregator,定义泛型
  // IN: 输入的数据类型:Long
  // BUF: 缓冲区的数据类型 样例类Buff
  // OUT:输出的数据类型:Long
  // 重写方法(8)

  case class Buff(var total: Long, var count: Long)

  class MyAvgUDAF extends Aggregator[Long, Buff, Long] {
    // 初始值(零值)
    // 缓冲区的初始化
    override def zero: Buff = {
      Buff(0L, 0L)
    }

    // 根据输入的数据来更新缓冲区的数据
    override def reduce(buff: Buff, in: Long): Buff = {
      buff.total = buff.total + in // 旧值 + in新值
      buff.count = buff.count + 1
      buff
    }

    // 合并缓冲区
    override def merge(buff1: Buff, buff2: Buff): Buff = {
      buff1.total = buff1.total + buff2.total
      buff1.count = buff1.count + buff2.count
      buff1
    }

    // 计算结果
    override def finish(buff: Buff): Long = {
      buff.total / buff.count
    }

    // 缓冲区的编码操作 -- 自定义的类 固定写法
    override def bufferEncoder: Encoder[Buff] = Encoders.product

    // 输出的编码操作   -- scala自带的类型 固定模式写法
    override def outputEncoder: Encoder[Long] = Encoders.scalaLong
  }

}
