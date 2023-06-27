package com.atguigu.bigdata.spark.core.framework.util

import org.apache.spark.SparkContext

object EnvUtil {

  // TODO TheradLocal 工具类:可以对线程的内存进行控制	,存储数据,共享数据
  private val scLocal =new ThreadLocal[SparkContext]


  def put(sc :SparkContext): Unit ={
    scLocal.set(sc)
  }

  def take() ={
    scLocal.get()
  }

  def clear(): Unit ={
    scLocal.remove()
  }
}
