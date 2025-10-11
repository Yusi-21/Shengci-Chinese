package tm.kumush.shengci.ui

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object ShengCiScreen : Screen("shengci_screen") {
        fun createRoute(unitId: Int) = "shengci_screen/$unitId"
    }
    object FuxiScreen : Screen("fuxi_screen") {
        fun createRoute(unitId: Int) = "fuxi_screen/$unitId"
    }
}