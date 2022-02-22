package com.briup.bigdata.day07

class Employ(name: String ,age: Int,salay:Double) extends Person (name,age){
  override def toString: String = {
    super.toString + " 工资：" + salay
  }
  def test = (x: Int) => {x*x}


}



