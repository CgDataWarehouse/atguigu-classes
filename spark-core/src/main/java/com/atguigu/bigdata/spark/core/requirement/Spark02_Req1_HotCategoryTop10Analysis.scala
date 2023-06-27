package com.atguigu.bigdata.spark.core.requirement

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_Req1_HotCategoryTop10Analysis {
  def main(args: Array[String]): Unit = {

    // todo 环境准备
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10Analysi")
    // todo 上下文对象
    val sc = new SparkContext(sparkConf)


    // 分析优化问题 : data 重复使用
    // cogroup 存在shuffle 性能很低
    // todo 1.数据准备
    val data = sc.textFile("datas/user_visit_action.txt")

    // dataRdd重复使用多次,cache
    data.cache()

    // todo 2. 一次性统计每个品类点击的次数   注意:过滤后,rdd数据还是原来的样子,不是只剩下datas(6),只是过滤了datas(6) !=-1 这部分
    val clickActionRdd = data.filter(
      action => {
        val datas = action.split("_")
        datas(6) != "-1"
      }
    )

    val clickCount: RDD[(String, Int)] = clickActionRdd.map(
      action => {
        val datas = action.split("_")
        (datas(6), 1)
      }
    ).reduceByKey(_ + _)

    //    clickCount.collect().foreach(println)

    // todo 3.统计每个品类下单的次数
    val orderActionRdd = data.filter(
      action => {
        val datas = action.split("_")
        datas(8) != "null"
      }
    )

    val orderCount: RDD[(String, Int)] = orderActionRdd.flatMap(
      action => {
        val datas = action.split("_")
        val cids: Array[String] = datas(8).split(",") // datas(8) 是cid
        cids.map(id => (id, 1))
      }
    ).reduceByKey(_ + _)

    //        orderCount.collect().foreach(println)

    //todo 4.统计每个品类支付的次数
    val payActionRdd = data.filter(
      action => {
        val datas = action.split("_")
        datas(10) != "null"
      }
    )

    val payCount: RDD[(String, Int)] = payActionRdd.flatMap(
      action => {
        val datas = action.split("_")
        val cids: Array[String] = datas(10).split(",") // datas(10) 是cid
        cids.map(id => (id, 1))
      }
    ).reduceByKey(_ + _)

    //  todo  5.一次性统计每个品类点击的次数，下单的次数和支付的次数：
    //    （品类，（点击总数，下单总数，支付总数））   每个品类对应的点击,下单,支付数都是唯一的
    // 分析一下多个数据源组合的方式 : join zip cogroup leftoutjoin
    //    val cogroupRdd: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] = clickCount.cogroup(orderCount, payCount)
    //
    //    val analysisResult = cogroupRdd.mapValues {
    //      case (iter1, iter2, iter3) => {
    //        var clickCnt = 0
    //        val clickIter = iter1.iterator
    //        if (clickIter.hasNext) {
    //          clickCnt = clickIter.next()
    //        }
    //        var orderCnt = 0
    //        val orderIter = iter2.iterator
    //        if (orderIter.hasNext) {
    //          orderCnt = orderIter.next()
    //        }
    //        var payCnt = 0
    //        val payIter = iter3.iterator
    //        if (payIter.hasNext) {
    //          payCnt = payIter.next()
    //        }
    //        (clickCnt, orderCnt, payCnt)
    //      }
    //    }

    // todo 第二种实现 合并的方式
    val clickRdd = clickCount.map {
      case (cid, cnt) => {
        (cid, (cnt, 0, 0))
      }
    }
    val orderRdd = orderCount.map {
      case (cid, cnt) => {
        (cid, (0, cnt, 0))
      }
    }
    val payRdd = payCount.map {
      case (cid, cnt) => {
        (cid, (0, 0, cnt))
      }
    }

    // 将多个数据源合并
    val sourceRdd: RDD[(String, (Int, Int, Int))] = clickRdd.union(orderRdd).union(payRdd)

    val analysisResult: RDD[(String, (Int, Int, Int))] = sourceRdd.reduceByKey(
      // reduceByKey 相同key分组,对value值 也就是三元tuple 进行 聚合操作, 参数为聚合规则
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
      }
    )

    // rdd 降序 取10
    val sortResult = analysisResult.sortBy(_._2, false).take(10)
    sortResult.foreach(println)


    sc.stop()

  }
}
