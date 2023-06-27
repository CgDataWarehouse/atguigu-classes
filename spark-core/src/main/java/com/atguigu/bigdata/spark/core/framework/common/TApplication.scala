package com.atguigu.bigdata.spark.core.framework.common

import com.atguigu.bigdata.spark.core.framework.controller.WordCountController
import com.atguigu.bigdata.spark.core.framework.util.EnvUtil
import org.apache.spark.{SparkConf, SparkContext}

trait TApplication {

  def start(master:String="local[*]",app:String="Application")(op: => Unit): Unit = {
    val sparkConf = new SparkConf().setMaster(master).setAppName(app)
    val sc: SparkContext = new SparkContext(sparkConf)

    EnvUtil.put(sc)

    try {
      op
    } catch {
      case ex => println(ex.getMessage)
    }


    // TODO 关闭连接
    sc.stop()
    // todo 清除
    EnvUtil.clear()
  }
}
