package com.atguigu.bigdata.spark.core.framework.controller

import com.atguigu.bigdata.spark.core.framework.common.TController
import com.atguigu.bigdata.spark.core.framework.service.WordCountService
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * 控制层
 */
class WordCountController extends TController{

  private val wordCountService = new WordCountService()

  // 调度
  // 每一个控制器都应该有一个调度
  def dispatch(): Unit = {

    //todo 调用service的数据操作方法
    val result = wordCountService.dataAnalysis()
    result.foreach(println)
  }

}
