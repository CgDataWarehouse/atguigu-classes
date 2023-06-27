package com.atguigu.bigdata.spark.core.test1

import java.io.{InputStream, ObjectInputStream}
import java.net.{ServerSocket, Socket}

object Executor2 {
  def main(args: Array[String]): Unit = {
    // 开启服务器,开启服务端
    val socket: ServerSocket = new ServerSocket(8888)

    // 等待客户端连接
    println("等待客户端连接")
    val server2: Socket = socket.accept()
    // 开启输入流
    val in: InputStream = server2.getInputStream

    // 使用对象输入流
    val objInstream: ObjectInputStream = new ObjectInputStream(in)

    // 读出数据对象
    val subTask: SubTask = objInstream.readObject().asInstanceOf[SubTask]

    val result1: List[Int] = subTask.compute()

    println("8888服务端2接收到数据" + result1)
    objInstream.close()
    socket.close()

  }
}
