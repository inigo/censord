package net.surguy.censord.model

import net.liftweb.mapper._
import org.openid4java.discovery.Identifier
import java.util.Date
import net.liftweb.common.{Empty, Logger, Full, Box}
import net.liftweb.sitemap.{Menu, Loc}

/**
 * A user of the system. The "allowed" flag determines whether the user is able to use the system,
 * but every user who logs in via OpenID will be entered into the table.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 08:01
 */
class AllowedUser extends LongKeyedMapper[AllowedUser] {
  def getSingleton = AllowedUser

  def primaryKeyField = id
  object id extends MappedLongIndex(this)
  object username extends MappedString(this, 255)
  object allowed extends MappedBoolean(this)

  object createdAt extends MappedDateTime(this)
}

/**
 * Operations for the AllowedUser table.
 */
object AllowedUser extends AllowedUser with LongKeyedMetaMapper[AllowedUser] with Logger with CRUDify[Long, AllowedUser] {

  // Good information on using CRUD at
  // http://neuralmonkey.blogspot.com/search/label/lift
  override def showAllMenuLoc = Full(Menu(Loc("AllowedUserList", List("alloweduser", "list"), "Users")))

  def isAllowed(username: Box[Identifier]): Boolean = {
    username match {
      case Full(name) => find(By(AllowedUser.username, name.getIdentifier), By(AllowedUser.allowed, true)).isDefined
      case _ => false
    }
  }

  def createIfNew(username: String): AllowedUser = {
    find(By(AllowedUser.username, username)).openOr{
      val isFirstUser = AllowedUser.count==0
      val newUser = AllowedUser.create.username(username).allowed(isFirstUser).createdAt(new Date())
      newUser.save()
      newUser
    }
  }

}

