package main.scala

import response.Error
import xml.{Elem, XML}

object Request {
  private final val apiKey = ""
  private final val baseURL = "http://api.flickr.com/services/rest/?api_key=" + apiKey + "&method="

  /** Send REST request to Flickr-server.
    *
    * See http://www.flickr.com/services/api/ for full API documentation.
    *
    * @param method Method to call.
    * @param args   Additional arguments.
    * @return       XML Elem on success or Error on failure.
    */
  def send(method: String, args: Array[(String, String)]): Either[Elem, Error] = {
    val url = baseURL + method + (for ((key, value) <- args) yield "&" + key + "=" + value).mkString
    val response = io.Source.fromURL(url)
    val xmlString = response.getLines().mkString
    val xmlData = XML.loadString(xmlString)
    response.close()

    if ((xmlData \ "@stat").text == "ok") Left(xmlData)
    else Right(Error((xmlData \ "@stat").text, (xmlData \ "err" \ "@msg").text, (xmlData \ "err" \ "@code").text.toInt))
  }
}