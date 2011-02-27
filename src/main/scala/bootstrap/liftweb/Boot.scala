package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper.{DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, StandardDBVendor}
import _root_.java.sql.{Connection, DriverManager}
import _root_.net.surguy.censord.model._
import net.liftweb.openid.OpenIDUser
import org.h2.engine.User
import net.surguy.censord.{LocalOpenIDVendor, RestApi}

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
			     Props.get("db.url") openOr 
			     "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
			     Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use HTML 5 rendering rather than XHTML rendering - this makes the behaviour of the words list change!
//    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    // Use the RestApi object for REST dispatch - this is stateful, but the "S" object is not populated
    // when using the statelessDispatchTable, so the way we're retrieving parameters doesn't work
//    LiftRules.statelessDispatchTable.append(RestApi)
    LiftRules.dispatch.append(RestApi) // stateful -- associated with a servlet container session

    // where to search for snippets
    LiftRules.addToPackages("net.surguy.censord")

    val loggedIn = If( () => AllowedUser.isAllowed(LocalOpenIDVendor.currentUser),
      () => if (LocalOpenIDVendor.currentUser.isEmpty) RedirectResponse("/login") else RedirectResponse("/static/notAuthorized") )

    val notLoggedIn = If( () => LocalOpenIDVendor.currentUser.isEmpty, RedirectResponse("/") )

    //  Menu.param("Show User", "Show User", s => User.find(s), u => u.name) / "user"
    //  Will match: /user/8

    // Format documented on the Wiki at http://www.assembla.com/wiki/show/liftweb/SiteMap
    def sitemap() = SiteMap(
      Menu(S ? "Check") / "index" >> loggedIn
      , Menu(S ? "Login") / "login" >> notLoggedIn
      , Menu(S ? "Terms") / "phrase/list" >> loggedIn
      , Menu(S ? "Users") / "alloweduser/list" >> loggedIn
      // These entries need to be in the SiteMap, or they cannot be accessed - but should not be directly visible
      , Menu(S ? "Hidden") / "hidden" >> Hidden submenus {
        List(AllowedUser.menus, Phrase.menus).flatten
      }
      , Menu(S ? "Unauthorized") / "static" / "notAuthorized" >> Hidden
    )

    LiftRules.setSiteMapFunc(() => sitemap())

    // Allow OpenID login
    LiftRules.dispatch.append(LocalOpenIDVendor.dispatchPF)

    /* Show the spinny image when an Ajax call starts */
    LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /* Make the spinny image go away when it ends */
    LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    LiftRules.loggedInTest = Full(() => LocalOpenIDVendor.currentUser.isDefined )

    S.addAround(DB.buildLoanWrapper)

    // Fade out lift notices after 2 seconds - don't fade out warnings or errors
    // http://www.assembla.com/wiki/show/liftweb/Lift_Notices_and_Auto_Fadeout
//    LiftRules.noticesAutoFadeOut.default.set( (notices: NoticeType.Value) => notices match {
//      case NoticeType.Notice => Full((2 seconds, 2 seconds))
//      case _ => Empty
//    })

    createDatabaseTables()
    AllowedUser.ensureDefaultUser()
  }

  def createDatabaseTables() = {
    Schemifier.schemify(true, Schemifier.infoF _, Phrase)
    Schemifier.schemify(true, Schemifier.infoF _, AllowedUser)
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }
}
