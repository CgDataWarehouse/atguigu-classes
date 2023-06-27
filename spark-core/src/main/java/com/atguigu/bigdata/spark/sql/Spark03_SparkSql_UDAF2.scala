package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql._

object Spark03_SparkSql_UDAF2 {
  def main(args: Array[String]): Unit = {

    //TODO 创建SparkSql的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    // 命令行对象 sparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    // TODO 执行逻辑操作
    //如果从内存中获取数据，spark 可以知道数据类型具体是什么。如果是数字，默认作为 Int 处理；但是从文件中读取的数字，不能确定是什么类型，所以用 bigint 接收，可以和
    //Long 类型转换，但是和 Int 不能进行转换
    val df: DataFrame = spark.read.json("datas/user.json")
    df.createOrReplaceTempView("user")


    // spark 3.0 前 spark不能在sql中使用强类型udaf操作 强类型的自定义函数实现 DSL方式
    import spark.implicits._
    val ds: Dataset[User] = df.as[User]

    // 将聚合函数转换成查询的列对象
    val udafColumn: TypedColumn[User, Long] = new MyAvgUDAF().toColumn

    ds.select(udafColumn).show

    // TODO 关闭环境
    spark.close()
  }


  // 自定义聚合函数类:计算年龄平均值
  // 继承   org.apache.spark.sql.expressions.Aggregator,定义泛型
  // IN: 输入的数据类型:User
  // BUF: 缓冲区的数据类型 样例类Buff
  // OUT:输出的数据类型:Long
  // 重写方法(8)

  case class User(username:String,age:Long)

  case class Buff(var total: Long, var count: Long)

  class MyAvgUDAF extends Aggregator[User, Buff, Long] {
    // 初始值(零值)
    // 缓冲区的初始化
    override def zero: Buff = {
      Buff(0L, 0L)
    }

    // 根据输入的数据来更新缓冲区的数据
    override def reduce(buff: Buff, in: User): Buff = {
      buff.total = buff.total + in.age // 旧值 + in新值
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
