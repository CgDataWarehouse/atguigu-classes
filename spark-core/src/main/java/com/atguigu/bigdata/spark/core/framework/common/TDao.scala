package com.atguigu.bigdata.spark.core.framework.common

import com.atguigu.bigdata.spark.core.framework.util.EnvUtil

trait TDao {
  def readFile(path:String) ={
    val sc = EnvUtil.take()
    sc.textFile(path)
  }
}
