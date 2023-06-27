package com.atguigu.bigdata.spark.core.requirement

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark04_Req1_HotCategoryTop10Analysis {
  def main(args: Array[String]): Unit = {

    // todo 环境准备
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10Analysi")
    // todo 上下文对象
    val sc = new SparkContext(sparkConf)


    // todo 1.数据准备
    val data = sc.textFile("datas/user_visit_action.txt")


    // todo 声明累加器
    val acc = new HotCategoryAccumulator
    // sc注册
    sc.register(acc)


    data.foreach(
      action => {
        val datas = action.split("_")
        if (datas(6) != "-1") {
          // 点击的场合
          acc.add((datas(6), "click"))
        } else if (datas(8) != "null") {
          val cids = datas(8).split(",")
          cids.foreach(
            id => {
              acc.add((id, "order"))
            }
          )
        } else if (datas(10) != "null") {
          val cids = datas(10).split(",")
          cids.foreach(
            id => {
              acc.add((id, "pay"))
            }
          )
        }
      }
    )

    val accValue: mutable.Map[String, HotCategory] = acc.value
    val categories: mutable.Iterable[HotCategory] = accValue.map(_._2)

    // 迭代器没法直接使用sort ,先toList,sortWith 自定义排序
    val sort = categories.toList.sortWith(
      (left, right) => {
        if (left.clickCnt > right.clickCnt) {
          true
        } else if (left.clickCnt == right.clickCnt) {
          if (left.orderCnt > right.orderCnt) {
            true
          } else if (left.orderCnt == right.orderCnt) {
            left.payCnt > right.payCnt
          } else {
            false
          }
        } else {
          false
        }
      }
    )

    sort.take(10).foreach(println)


    sc.stop()

  }

  // 声明一个品类类
  case class HotCategory(cid: String, var clickCnt: Int, var orderCnt: Int, var payCnt: Int)

  /**
   * 自定义累加器
   * 1.继承AccumulatorV2
   * 定义泛型
   * IN: (品类id,操作类型)
   * OUT: mutable.Map[String,HotCategory]    String 指品类id,hotcategory是具体品类id的对象
   */

  class HotCategoryAccumulator extends AccumulatorV2[(String, String), mutable.Map[String, HotCategory]] {
    private var resultMap = mutable.Map[String, HotCategory]() // 声明一个累加器的值value,为空Map

    override def isZero: Boolean = {
      resultMap.isEmpty
    }

    override def copy(): AccumulatorV2[(String, String), mutable.Map[String, HotCategory]] = {
      new HotCategoryAccumulator
    }

    override def reset(): Unit = {
      resultMap.clear()
    }

    override def add(v: (String, String)): Unit = {
      // add 添加数据的时候,要判断当前累加器的值resultMap有没有当前v的值
      val cid = v._1 // 品类id
      val action_type = v._2 // 操作类型

      // 判断当前resultMap有无key == cid,若有返回对应value值 即 category对象,若无:返回新的Hotcategory(cid,0,0,0)
      // 结果+1
      val category = resultMap.getOrElse(cid, HotCategory(cid, 0, 0, 0))
      if (action_type == "click") {
        category.clickCnt += 1
      } else if (action_type == "order") {
        category.orderCnt += 1
      } else if (action_type == "pay") {
        category.payCnt += 1
      }
      resultMap.update(cid, category)
    }

    override def merge(other: AccumulatorV2[(String, String), mutable.Map[String, HotCategory]]): Unit = {
      // merge 多个累加器合并,本案例就是 两两 Map合并
      val map1 = this.resultMap
      val map2 = other.value

      map2.foreach {
        case (cid, hotCategory) => {
          val hc1 = map1.getOrElse(cid, HotCategory(cid, 0, 0, 0))
          hc1.clickCnt += hotCategory.clickCnt
          hc1.orderCnt += hotCategory.orderCnt
          hc1.payCnt += hotCategory.payCnt
          map1.update(cid, hc1)
        }
      }
    }

    override def value: mutable.Map[String, HotCategory] = {
      resultMap
    }
  }
}



