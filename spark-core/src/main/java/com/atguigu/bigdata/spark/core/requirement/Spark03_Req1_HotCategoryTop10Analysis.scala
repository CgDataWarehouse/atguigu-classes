package com.atguigu.bigdata.spark.core.requirement

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark03_Req1_HotCategoryTop10Analysis {
  def main(args: Array[String]): Unit = {

    // todo 环境准备
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10Analysi")
    // todo 上下文对象
    val sc = new SparkContext(sparkConf)

    //  todo 现在的问题:存在大量的shuffle操作
    // reduceByKey 聚合算子,spark会提供优化,存在缓存操作  对于相同的数据源rdd 多次reduceByKey由于缓存机制,性能影响不是很大
    // 不过! 本案例的 多次reduceByKey都是不同的数据源Rdd,非常影响性能

    // todo 1.数据准备
    val data = sc.textFile("datas/user_visit_action.txt")

    // 2.将数据转换结构
    // 点击的场合 : (品类id,(1,0,0))
    // 下单的场合 : (品类id,(0,1,0))
    // 支付的场合 : (品类id,(0,0,1))

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


    // 3.将相同的品类id的数据进行分组聚合
    //    (品类id,(点击数量,下单数量,支付数量))

    // rdd 降序 取10
    val sortResult = analysisResult.sortBy(_._2, false).take(10)
    sortResult.foreach(println)


    sc.stop()

  }
}
