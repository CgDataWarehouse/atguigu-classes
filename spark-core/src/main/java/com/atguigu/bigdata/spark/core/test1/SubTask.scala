package com.atguigu.bigdata.spark.core.test1

class SubTask extends Serializable { //系列化特质
  var datas : List[Int] = _

  var logic: Int => Int = _

  def compute()={
    datas.map(logic)
  }

}
