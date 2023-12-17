package io.devmike01.compiler.android

import java.util.regex.Pattern

object ChaiCupGenerator {

    private const val SMALL_SPACE = "    "
    private const val PRIVATE_FUNC = "\n\n${SMALL_SPACE}private fun "
    private const val PUBLIC_FUNC = "\n\n${SMALL_SPACE}fun "

    private const val ARGUMENT_ROUTE = PRIVATE_FUNC + "getRoute(route: String, argumentMap: Map<String, String>): String{" +
            "\n        var arguments = \"\" \n" +
            "        argumentMap.forEach{\n" +
            "        arguments += " +
            "\"\${it.key}=\${it.value}&\"\n    }\n" +
            "        return \"\$route?\${arguments.substring(0, arguments.length - 1)}\"\n    }"

    private const val PARSE_VARARG_ARG = PRIVATE_FUNC + "parseArguments(route: String, values: Array<String>): String{\n" +
            "        val arguments = mutableMapOf<String, String>()\n" +
            "        val routeSegments = route.split(\"?\")\n" +
            "        val slicedArg = routeSegments[1]\n" +
            "        slicedArg.split(\"&\").forEachIndexed { index, s ->\n" +
            "            arguments[s.split(\"=\")[0]] = values[index]\n" +
            "        }\n" +
            "        return getRoute(routeSegments[0], arguments)\n" +
            "    }"

    fun generatePrivateFunc( onGenerate : (String) -> Unit){
        onGenerate(ARGUMENT_ROUTE +PARSE_VARARG_ARG)
    }

    fun navigator(funcName: String, hasArg: Boolean,  route: String, onGenerate : (String) -> Unit){
        var funcWithArgument =""
        if (hasArg){
            funcWithArgument = "${PUBLIC_FUNC}NavController.navigateTo$funcName(arguments: Map<String, String>," +
                    "navOptions: NavOptions? = null,\n" +
                    "                       navigatorExtras: Navigator.Extras? = null){\n" +
                    "       this.navigate(${"getRoute($route.split(\"?\")[0], arguments)"}, navOptions, navigatorExtras)\n" +
                    "    }\n"

            funcWithArgument += "${PUBLIC_FUNC}NavController.navigateTo$funcName(navOptions: NavOptions? = null,\n" +
                    "                       navigatorExtras: Navigator.Extras? = null, " +
                    "arguments: Array<String>" +
                    "){\n" +
                    "       this.navigate(${"parseArguments($route, arguments)"}, navOptions, navigatorExtras)\n" +
                    "    }\n"
        }


        val noArgCode = "${PUBLIC_FUNC}NavController.navigateTo$funcName(" +
                "navOptions: NavOptions? = null,\n" +
                "                       navigatorExtras: Navigator.Extras? = null){\n" +
                "       this.navigate($route, navOptions, navigatorExtras)\n" +
                "    }\n"

        onGenerate(noArgCode+ funcWithArgument)
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

    fun parseArguments(route: String, vararg values: String): String{
        val arguments = mutableMapOf<String, String>()
        val slicedArg = route.split("?")[1]
        slicedArg.split("&").forEachIndexed { index, s ->
            arguments[s.split("=")[0]] = values[index]
        }
        return getRoute(route, arguments)
    }

    fun getRoute(route: String, argumentMap: Map<String, String>): String{
        var arguments = ""
        argumentMap.forEach {
            arguments += "${it.key}=${it.value}&"
        }
        return "$route?${arguments.substring(0, arguments.length-1)}";
    }


}