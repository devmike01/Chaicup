package io.devmike01.compiler.android

import java.util.regex.Pattern

object ChaiCupGenerator {

    fun navigator(funcName: String, route: String, onGenerate : (String) -> Unit){
        onGenerate("fun NavController.navigateTo$funcName(route: String," +
                "navOptions: NavOptions? = null,\n" +
                "                       navigatorExtras: Navigator.Extras? = null){\n" +
                "    this.navigate($route, navOptions, navigatorExtras)\n" +
                "}")
    }

    fun computeRouteName(functionName: String, simpleName: String): String{

        val upperCase = "(.*[A-Z].*)"

        var func : String =""

        (functionName.ifBlank { simpleName })
            .forEachIndexed { index, letter ->

                val matchesUpper =
                    Pattern.matches(upperCase, letter.toString())
                func += if(matchesUpper && index > 0){
                    "_$letter"
                }else{
                    letter.toString()
                }
            }
        return "${func.uppercase()}_ROUTE"

    }


    fun generateRoutes(){

    }
}