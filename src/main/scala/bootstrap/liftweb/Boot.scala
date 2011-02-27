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
import net.surguy.censord.RestApi
import net.liftweb.openid.{OpenIDUser, SimpleOpenIDVendor}
import org.h2.engine.User

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
//    Schemifier.schemify(true, Schemifier.infoF _, User)

//    val loggedIn = If( () => AllowedUser.isAllowed(SimpleOpenIDVendor.currentUser),
//      () => if (SimpleOpenIDVendor.currentUser.isEmpty) RedirectResponse("/login") else RedirectResponse("/static/notAuthorized") )

    val loggedIn = If(() => true, () => RedirectResponse("/login"))

    val m : Menu = Menu(Loc("Profile", "/static/protected" :: Nil, "someProfileText", loggedIn))
    val profileMenu = Menu(Loc("static", "/static/index" :: Nil, "/static/index", loggedIn))

    //  Menu.param("Show User", "Show User", s => User.find(s), u => u.name) / "user"
    //  Will match: /user/8

    def sitemap() = SiteMap(
      Menu(S ? "Home") / "index" >> If(() => false, RedirectResponse("/login")),
      Menu(S ? "Login") / "login",
      Menu(S ? "Static") / "static" / "index" >> If(() => false, RedirectResponse("/login")),
      Menu(S ? "Zog") / "static" / "zog",
      Menu(S ? "Admin") / "admin" / "index" >> If(() => false, "You must be logged in")
    )
//
//    submenus (
//        Menu(S ? "Management") / "about" / "management",
//        Menu(S ? "Goals") / "about" / "goals"),
//      Menu("directions", S ? "Directions") / "directions" >> Hidden,
//      Menu(S ? "Admin") / "admin" / "index" >> If(() => loggedIn_?, "You must be logged in"))


    // Build SiteMap
//    def sitemap() = SiteMap(
//      Menu("Home") / "index", // Simple menu form
//      Menu("Login") / "login",
//      Menu("Zog") / "zog",
//      Menu("ThogZog") / "thogzog",
//      Menu(Loc("someThing", Link(List("profile1"), true, "/static/profile2"), "more text")),
//      profileMenu,
//      Menu(Loc("Static", Link(List("static"), true, "/static/index"), "Static Content"))
//    )

    LiftRules.setSiteMapFunc(() => sitemap())

    // Allow OpenID login
    LiftRules.dispatch.append(SimpleOpenIDVendor.dispatchPF)

    /* Show the spinny image when an Ajax call starts */
    LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /* Make the spinny image go away when it ends */
    LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    LiftRules.loggedInTest = Full(() => SimpleOpenIDVendor.currentUser.isDefined )

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
