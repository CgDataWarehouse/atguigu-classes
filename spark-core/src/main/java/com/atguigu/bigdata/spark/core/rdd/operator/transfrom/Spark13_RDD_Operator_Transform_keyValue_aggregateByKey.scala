package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark13_RDD_Operator_Transform_keyValue_aggregateByKey {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("rdd_transform")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    // todo key - value类型

    val rdd = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("b", 3), ("b", 4), ("b", 5), ("a", 6)
    ), 2)

    // todo 1.取分区最大的值进行分区间的求和
    //函数柯里化
    //一共两个参数列表
    //第一个参数列表 :需要传递一个参数,表示为初始值,主要用于碰见第一个key时,和value进行分区内计算
    //第二个参数列表
    //        第一个参数表示分区内的计算规则
    //        第二个参数表示分区间的计算规则
    rdd.aggregateByKey(5)(
      (x, y) => math.max(x, y),  // math.max(x,y) 取最大值   =>0分区 (a,2),(b,3) 1分区 (b,5),(a,6)
      (x, y) => x + y            // =>(a,8),(b,8)
    ).collect().foreach(println)

    println("---------------------------------------")
    // todo 2.聚合计算时,分区内和分区间的计算规则相同
    rdd.foldByKey(0)(_ + _).collect().foreach(println)

    println("---------------------------------------")
    // todo 3.聚合计算时,获取相同key的数据平均值
    val newRdd: RDD[(String, (Int, Int))] = rdd.aggregateByKey((0, 0))( // 第一个参数初始值为(0,0) _1为value和,_2为相同key出现次数
      (tuple, v) => { //第一个参数为(初始值tuple,组内的各个value)
        (tuple._1 + v, tuple._2 + 1) //值两两相加,次数0+1开始累计
      },
      (tuple1, tuple2) => { // 组间规则
        (tuple1._1 + tuple2._1, tuple1._2 + tuple2._2)
      }
    )
    val result: RDD[(String, Int)] = newRdd.mapValues {
      case (value, cnt) => {
        value / cnt
      }
    }
    result.collect().foreach(println)


    sc.stop()
  }
}
