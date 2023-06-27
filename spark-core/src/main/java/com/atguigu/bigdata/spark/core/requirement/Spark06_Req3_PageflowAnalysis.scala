package com.atguigu.bigdata.spark.core.requirement

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark06_Req3_PageflowAnalysis {
  def main(args: Array[String]): Unit = {

    // todo 环境准备
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10Analysi")
    // todo 上下文对象
    val sc = new SparkContext(sparkConf)

    // todo 1.数据准备
    val data = sc.textFile("datas/user_visit_action.txt")

    val actionDataRdd: RDD[UserVisitAction] = data.map(
      actionLine => {
        val strings = actionLine.split("_")

        // case样例类的apply方法,不用new创建对象,直接类名(构造参数)
        UserVisitAction(
          strings(0),
          strings(1).toLong,
          strings(2),
          strings(3).toLong,
          strings(4),
          strings(5),
          strings(6).toLong,
          strings(7).toLong,
          strings(8),
          strings(9),
          strings(10),
          strings(11),
          strings(12).toLong
        )
      }
    )

    actionDataRdd.cache()


    // todo 先对指定的页面连续跳转进行统计过滤
    // 1-2,2-3,3-4,4-5,5-6,6-7
    val ids = List[Long](1, 2, 3, 4, 5, 6, 7)
    val okflowids: List[(Long, Long)] = ids.zip(ids.tail)

    // todo 计算分母


    val fenmuPageIdCountMap: Map[Long, Long] = actionDataRdd.filter(
      userLine => {
        ids.init.contains(userLine.page_id) // 对指定页面id过滤  init是返回list除了最后一个的所有元素,因为跳转率的分母肯定是不包括最后一个
      }
    ).map(
      user => {
        (user.page_id, 1L)
      }
    ).reduceByKey(_ + _).collect().toMap //collect转成Array 但是不方便取分母,转成Map

    // todo 计算分子

    // 根据session分组
    val sessionRdd: RDD[(String, Iterable[UserVisitAction])] = actionDataRdd.groupBy(_.session_id)
    // 分组后的,组内各个user-action根据访问时间排序(升序)

    val mvRdd: RDD[(String, List[((Long, Long), Int)])] = sessionRdd.mapValues(
      iter => {
        val sortList: List[UserVisitAction] = iter.toList.sortBy(_.action_time)
        // 一个分组内排序后就不需要多余信息了,转换剩下page_id就行
        val flowIds: List[Long] = sortList.map(_.page_id)
        // 两两相邻的元素要组合起来,才能表示出页面的跳转 A,B =>(A,B) 从A页面跳转到B页面,因为我们要算<页面单跳转化率>
        // [1,2,3,4]  转换为 [(1,2),(2,3),(3,4)]
        // sliding划窗!!!
        // [1,2,3,4...]
        // [2,3,4...]
        // zip!!!
        val pageflowIds: List[(Long, Long)] = flowIds.init.zip(flowIds.tail) //去掉最后一个和去掉第一个后,两个rdd数量一致可以拉链
        pageflowIds.filter( // 分子部分也需要过滤指定的页面跳转
          t => {
            okflowids.contains(t)
          }
        ).map(
          t => {
            (t, 1)
          }
        )
      }
    )
    //((1,2),1) 第二个元素是一个个List(((1,2),1),((2,3),1),....),需要扁平化打散
    val flatRDD: RDD[((Long, Long), Int)] = mvRdd.map(_._2).flatMap(List => List)
    //  ((1,2),1)  =>  ((1,2),sum)
    val fenmuRDD = flatRDD.reduceByKey(_ + _)

    // TODO 计算单跳转换率
    // 分子除以分母
    fenmuRDD.foreach {
      case ((pageid1, pageid2), sum) => {
        val fenmuValue = fenmuPageIdCountMap.getOrElse(pageid1, 0L)
        println(s"页面${pageid1}跳转到页面${pageid2}单跳转换率 : " + (sum.toDouble / fenmuValue))
      }
    }


    sc.stop()
  }

  //用户访问动作表
  case class UserVisitAction(
                              date: String, //用户点击行为的日期
                              user_id: Long, // 用 户 的 ID
                              session_id: String, //Session 的 ID
                              page_id: Long, // 某 个 页 面 的 ID
                              action_time: String, //动作的时间点
                              search_keyword: String, //用户搜索的关键词
                              click_category_id: Long, // 某 一 个 商 品 品 类 的 ID
                              click_product_id: Long, // 某 一 个 商 品 的 ID
                              order_category_ids: String, //一次订单中所有品类的 ID 集合
                              order_product_ids: String, //一次订单中所有商品的 ID 集合
                              pay_category_ids: String, //一次支付中所有品类的 ID 集合
                              pay_product_ids: String, //一次支付中所有商品的 ID 集合
                              city_id: Long //城市 id
                            )
}
