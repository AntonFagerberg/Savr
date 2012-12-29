package main.scala.response

import main.scala.Request

case class User(
  nsID: String,
  realName: String,
  username: String,
  alias: String,
  location: String,
  timezone: (String, String),
  profileURL: String,
  mobileURL: String,
  photosURL: String,
  firstDate: String,
  firstDateTaken: String,
  count: Int,
  iconFarm: Int,
  iconServer: Int,
  pro: Boolean
)

object User {
  /** Create a User object from the users NSID.
    *
    * @param NSID NSID-number.
    * @return     User on success or Error on failure.
    */
  def userFromNSID(NSID: String): Either[User, Error] = {
    val xmlData = Request.send(
      "flickr.people.getInfo",
      Array(
        "user_id" -> NSID
      )
    )
    
    xmlData match {
      case Right(error) => Right(error)
      case Left(userXML) => Left(User(
        NSID,
        (userXML \ "person" \ "realname").text,
        (userXML \ "person" \ "username").text,
        (userXML \ "person" \ "@path_alias").text,
        (userXML \ "person" \ "location").text,
        (userXML \ "person" \ "timezone" \ "@label").text -> (userXML \ "person" \ "timezone" \ "@offset").text,
        (userXML \ "person" \ "profileurl").text,
        (userXML \ "person" \ "mobileurl").text,
        (userXML \ "person" \ "photosurl").text,
        (userXML \ "person" \ "photos" \ "firstdate").text,
        (userXML \ "person" \ "photos" \ "firstdatetaken").text,
        (userXML \ "person" \ "photos" \ "count").text.toInt,
        (userXML \ "person" \ "@iconfarm").text.toInt,
        (userXML \ "person" \ "@iconserver").text.toInt,
        (userXML \ "person" \ "@ispro").text.toInt == 1
      ))
    }
  }

  /** Find a users NSID based on the username.
    *
    * @param username Username the NSID belongs to.
    * @return         Username on success or Error on failure.
    */
  def NSIDFromUsername(username: String): Either[String, Error] = {
    val xmlData = Request.send(
      "flickr.people.findByUsername",
      Array(
        "username" -> username
      )
    )

    xmlData match {
      case Right(error) => Right(error)
      case Left(nsIDXML) => Left((nsIDXML \ "user" \ "@nsid").text)
    }
  }
}