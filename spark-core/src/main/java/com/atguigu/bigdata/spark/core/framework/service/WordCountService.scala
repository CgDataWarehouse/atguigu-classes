package com.atguigu.bigdata.spark.core.framework.service


import com.atguigu.bigdata.spark.core.framework.common.TService
import com.atguigu.bigdata.spark.core.framework.dao.WordCountDao
import org.apache.spark.rdd.RDD

/**
 * 服务层
 */
class WordCountService extends TService{

  private val wordCountDao = new WordCountDao()


  // 数据分析
  def dataAnalysis() = {
    //1.读取文件,获取一行一行的数据
    val lines = wordCountDao.readFile("datas/word.txt")

    val wordTuples: RDD[(String, Int)] = lines.flatMap(
      _.split(" ")
    ).map((_, 1))

    val value: RDD[(String, Int)] = wordTuples.reduceByKey(_ + _)

    val array: Array[(String, Int)] = value.collect()
    array
  }
}
