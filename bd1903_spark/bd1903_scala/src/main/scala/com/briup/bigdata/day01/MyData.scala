package com.briup.bigdata.day01

class MyData(num:Int=0) {
  def !():Int = {
    var sum:Int = 1;
    for(n <- 1 to num){
      sum *= n
    }
    sum
  }
}
