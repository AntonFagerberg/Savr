package main.scala

import response.User

object Savr {
  def main(args: Array[String]) {
    val userNSID = User.NSIDFromUsername("AntonFagerberg")

    println("User NSID: " + userNSID)

    if (userNSID.isLeft) {
      val user = User.userFromNSID(userNSID.left.get)
      println("User: " + user)
    }
  }
}
