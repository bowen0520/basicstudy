package com.briup.bigdata.day08

class Pair[T](first: T,secord: T){
  def smaller(implicit ord: Ordering[T]): T = {
    if(ord.compare(first,secord)>0){
      first
    }else{
      secord
    }
  }
}
