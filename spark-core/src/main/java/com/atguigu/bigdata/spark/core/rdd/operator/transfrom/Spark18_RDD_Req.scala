package com.atguigu.bigdata.spark.core.rdd.builder.transfrom

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark18_RDD_Req {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // 上下文对象
    val sc = new SparkContext(sparkConf)


    //1.获取原始数据  时间戳,省份,城市,用户,广告
    val datas: RDD[String] = sc.textFile("datas/agent.log")

    //2.原始数据进行转换 => ((省份,广告),1)
    val rdd: RDD[((String, String), Int)] = datas.map(
      line => {
        val strings: Array[String] = line.split(" ")
        ((strings(1), strings(4)), 1)
      }
    )

    // 3.分组聚合,根据(省份,广告) 分组 聚合求sum
    val reduceRdd: RDD[((String, String), Int)] = rdd.reduceByKey(_ + _)

    // 4.聚合结果进行结构转换 (省份,(广告,sum))
    val newRdd: RDD[(String, (String, Int))] = reduceRdd.map {
      case ((pre, ad), sum) => {
        (pre, (ad, sum))
      }
    }
    // 5.转换后的数据根据省份进行分组
    val groupRdd: RDD[(String, Iterable[(String, Int)])] = newRdd.groupByKey()

    //6.分组后数据组内排序,取广告前3
    val result: RDD[(String, List[(String, Int)])] = groupRdd.mapValues(
      iter => {
        //        iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3) // 这里是scala的sortby 不可以false倒序
        iter.toList.sortWith(_._2 > _._2).take(3)
      }
    )

    result.collect().foreach(println)


    sc.stop()
  }
}
