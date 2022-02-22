package com.briup.bigdata.day02

object Exceptiontest {
  def main(args: Array[String]): Unit = {
    val teacher = new Teacher("aaa")
    //teacher.name
    try{
      show("OK")
    }catch {
      case e1:Exception => {

      }
      case _ =>{

      }
    }finally {

    }


  }

  def show(str: String): Unit={
    if(str==null){
      throw new Exception("消息为空")
    }else{
      println(s"消息为：$str")
    }
  }

}
