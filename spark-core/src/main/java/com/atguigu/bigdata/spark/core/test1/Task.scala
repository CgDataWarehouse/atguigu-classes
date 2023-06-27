package com.atguigu.bigdata.spark.core.test1

class Task extends Serializable { //系列化特质
  val datas = List(1, 2, 3, 4)

  //  val logic = (num:Int) =>{
  //    num * 2
  //  }
  val logic: Int => Int = _ * 2

  //计算
  def compute()={
    datas.map(logic)
  }

}
