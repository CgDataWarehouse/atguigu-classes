package com.atguigu.bigdata.spark.core.requirement

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark05_Req2_HotCategoryTop10SessionAnalysis {
  def main(args: Array[String]): Unit = {

    // todo 环境准备
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10Analysi")
    // todo 上下文对象
    val sc = new SparkContext(sparkConf)

    // todo 1.数据准备
    val data = sc.textFile("datas/user_visit_action.txt")
    // 缓存
    data.cache()
    // 调热门品类方法 得到前10品类
    val top10Ids: Array[String] = top10Category(data)

    //过滤原始数据,保留点击和前10品类数据
//    val result = data.filter(
    //      actionLine => {
    //        val strings: Array[String] = actionLine.split("_")
    //        strings(6) != "null" && top10Ids.contains(strings(6))
    //      }
    //    )

    val filterActionRdd = data.filter(
      actionLine => {
        val strings: Array[String] = actionLine.split("_")
        if(strings(6)!="null"){
          top10Ids.contains(strings(6))
        }else{
          false
        }
      }
    )

    // 根据品类id和session_id进行点击量统计
    val reduceRdd = filterActionRdd.map(
      actionLine => {
        val strings = actionLine.split("_")
        ((strings(6), strings(2)), 1)
      }
    ).reduceByKey(_ + _)

    // 统计结果转化成(品类id,(session_id,sum))
    //((品类id,session_id),sum)   ===>(品类id,(session_id,sum))
    val mapRDD = reduceRdd.map {
      case ((cid, sid), sum) => {
        (cid, (sid, sum))
      }
    }

    // TODO 相同的品类进行分组
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = mapRDD.groupByKey()

    // TODO 分组后的数据进行点击量的排序,取前10
    val resultRDD: RDD[(String, List[(String, Int)])] = groupRDD.mapValues(
      iter => {
        iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(10)
      }
    )

    resultRDD.collect().foreach(println)

    sc.stop()

  }

  /**
   * 将需求1统计热门top10品类(点击,下单,支付)封装到方法里
   *
   * @param data
   * @return
   */

  def top10Category(data: RDD[String]) = {

    val sourceRdd = data.flatMap(
      action => {
        val datas = action.split("_")
        if (datas(6) != "-1") {
          List((datas(6), (1, 0, 0)))
        } else if (datas(8) != "null") {
          val cids = datas(8).split(",")
          cids.map(cid => (cid, (0, 1, 0)))
        } else if (datas(10) != "null") {
          val cids = datas(10).split(",")
          cids.map(cid => (cid, (0, 0, 1)))
        } else {
          Nil
        }
      }
    )
    val analysisResult = sourceRdd.reduceByKey(
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
      }
    )

    analysisResult.sortBy(_._2, false).take(10).map(_._1) // map 只取热门top10的品类id

  }
}
