package com.briup.bigdata.day08

object Test {
  def main(args: Array[String]): Unit = {
    /*val p1 = new Person("zs",23)
    val p2 = new Person("ls",24)
    implicit val ImpPersonOrdering = new PersonOrdering
    val p3 = new Pair(p1,p2).smaller

    print(p3.toString)*/

    implicit def Person2God(per: Person) :God = {
      new God(per.name)
    }

    new Person("zs",23).laySkills
  }
}
