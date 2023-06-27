package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object Spark03_SparkSql_UDAF {
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

    // 弱类型自定义函数实现
    spark.udf.register("ageAvg", new MyAvgUDAF())
    // 使用自定义函数prefixName
    spark.sql("select ageAvg(age) from user").show()



    // TODO 关闭环境
    spark.close()
  }


  // 自定义聚合函数类:计算年龄平均值
  // 继承  UserDefinedAggregateFunction
  // 重写方法(8)
  class MyAvgUDAF extends UserDefinedAggregateFunction {
    // 输入数据的结构: in
    override def inputSchema: StructType = {
      StructType(
        Array(
          StructField("age", LongType)
        )
      )
    }

    // 缓冲区数据的结构 :Buffer
    override def bufferSchema: StructType = {
      StructType(
        Array(
          StructField("total", LongType),
          StructField("count", LongType)
        )
      )
    }

    // 函数计算结果的数据类型 Out
    override def dataType: DataType = LongType

    // 函数的稳定性
    override def deterministic: Boolean = true

    // 缓冲区初始化
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      //      buffer(0)=0L
      //      buffer(1)=0L

      buffer.update(0, 0L)
      buffer.update(1, 0L)
    }

    // 根据输入的值更新缓冲区数据
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      // input 是输入的值,输入的结构是inputSchema里定义的一个结构,取0的话其实取的就是年龄
      // buffer 取的是缓冲区的结构,有0,1的两个结构buffer.getLong(0)表示total;buffer.getLong(1)表示count
      buffer.update(0, buffer.getLong(0) + input.getLong(0))
      buffer.update(1, buffer.getLong(1) + 1)
    }

    // 缓冲区数据合并,分布式计算缓冲区是有多个的
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      // 后进的buffer2反复+buffer1的值赋给buffer1
      buffer1.update(0, buffer1.getLong(0) + buffer2.getLong(0))
      buffer1.update(1, buffer1.getLong(1) + buffer2.getLong(1))
    }

    //计算平均值
    override def evaluate(buffer: Row): Any = {
      buffer.getLong(0) / buffer.getLong(1)
    }
  }

}
