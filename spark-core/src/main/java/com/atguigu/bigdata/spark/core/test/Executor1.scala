package com.atguigu.bigdata.spark.core.test

import java.io.{InputStream, ObjectInputStream}
import java.net.{ServerSocket, Socket}


object Executor1 {
  def main(args: Array[String]): Unit = {
    val server1: ServerSocket = new ServerSocket(7777)
    //服务器启动
    println("服务器启动,等待接收数据")

    //等待客户端连接
    val socket: Socket = server1.accept()
    // 打开输入流
    val in: InputStream = socket.getInputStream

    // 获得对象输入流,接收一个对象
    val objInputStream: ObjectInputStream = new ObjectInputStream(in)

    val data: Task = objInputStream.readObject().asInstanceOf[Task]  //readObject()读出来一个对象,as对象类型
    val ints: List[Int] = data.compute()
    println(ints)
    objInputStream.close()
    socket.close()
    server1.close()

  }
}
