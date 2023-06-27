package com.atguigu.bigdata.spark.core.framework.application

import com.atguigu.bigdata.spark.core.framework.common.TApplication
import com.atguigu.bigdata.spark.core.framework.controller.WordCountController
import com.atguigu.bigdata.spark.core.framework.service.WordCountService
import org.apache.spark.{SparkConf, SparkContext}

object WordCountApp extends App with TApplication {
  println("使用App实现入口！")


  // 启动应用程序
  start() {
    val controller = new WordCountController()
    controller.dispatch()
  }

}
