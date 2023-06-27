package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.expr
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object Spark01_SparkSql_Basic {
  def main(args: Array[String]): Unit = {

    //TODO 创建SparkSql的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    // 命令行对象 sparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    // 使用DataFrame进行转换操作时需要引入转换规则
    import spark.implicits._
    // TODO 执行逻辑操作

    // DataFrame
    val df: DataFrame = spark.read.json("datas/user.json")

    //df.na.fill(0).orderBy('age.desc).show() 降序的几种方式
//    na.fill 是对null的补充为0
    df.na.fill(0).orderBy(-'age).show()
    df.withColumnRenamed("age","newAage").show() //改列名
    df.withColumn("age1",'age +18).show() //加一列名,如果列名与旧列名重复,旧的不显示

    //    df.show()

    // DataFrame  => SQL
    df.createOrReplaceTempView("user")

    //    spark.sql("select * from user").show()
    //    spark.sql("select age,username from user").show()
    //    spark.sql("select avg(age) from user").show()
    // DataFrame => DSL


    //    df.select("username","age").show
    //    df.select($"age"+2).show
    //    df.select('age+1).show

    // TODO DataSet
    // DataFrame 是特定泛型的DataSet[Row],所以能用DataFrame的方法
    val seq = Seq(1, 2, 3, 4)
    val ds: Dataset[Int] = seq.toDS()
    //    ds.show()

    // RDD <=> DataFrame
    val rdd = spark.sparkContext.makeRDD(List((1, "cg", 27), (2, "wanglin", 22)))

    val df1: DataFrame = rdd.toDF("id", "name", "age") // rdd -> df 设计表结构

    val rowRDD: RDD[Row] = df1.rdd // Row 是封装了字段的行对象

    // DataFrame <=> DataSet
    val ds1: Dataset[User] = df1.as[User]
    val df2: DataFrame = ds1.toDF()
    //    ds1.show

    // RDD <=> DataSet
    // 理解记忆:RDD里是纯数据,是没有字段类型及结构,所以先map转换
    println("-----------")
    val ds2: Dataset[User] = rdd.map {
      // 模式匹配
      case (id, name, age) => {
        User(id, name, age)
      }
    }.toDS()

//    ds2.show()
//    ds2.select($"age"+2).show

    val rdd1: RDD[User] = ds2.rdd



    // TODO 关闭环境
    spark.close()
  }

  case class User(id: Int, name: String, age: Int)

}
