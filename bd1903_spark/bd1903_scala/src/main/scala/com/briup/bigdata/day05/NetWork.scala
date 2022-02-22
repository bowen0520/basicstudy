package com.briup.bigdata.day05

import scala.collection.mutable.ArrayBuffer

class NetWork {
  class Member(val name:String){
    val contacts = new ArrayBuffer[Member]
  }

  private val members = new ArrayBuffer[Member]
  def join(name:String): Unit ={
    val member = new Member(name)
    //members  member
  }
}

