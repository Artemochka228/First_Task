import java.io.File

interface IO {
    fun read(): List<String>
    fun write(lines: ArrayList<String>)
}

fun IO.convert(lines: List<Int>): ArrayList<String> {
    val res: ArrayList<String> = arrayListOf()
    for (line in lines){
        if (line >= 0){
            var num = line
            var numstr = ""
            while(num != 0){
                numstr = (num % 4).toString() + numstr
                num /= 4
            }
            res.add(numstr)
        }
        else{
            res.add("Неверный формат выражения")
        }
    }
    return res
}

class ConsoleIO : IO {

    override fun read(): List<String> {
        var input = readlnOrNull().toString()
        var res = ""

        while (input != "stop") {
            if (input.contains(Regex("[^0-9+/*-]"))
                or !(input.contains(Regex("[0-9+*/-]")))
            ) {
                res += "Неверный формат выражения\n"
                input = readlnOrNull()
                    .toString()
                    .replace(" ", "")
                continue
            }
            res += input + "\n"
            input = readlnOrNull()
                .toString()
                .replace(" ", "")
        }

        return res
            .trim()
            .split("\n")
    }
    override fun write(lines: ArrayList<String>) {
        for (line in lines)
        {
            println(line)
        }
    }
}

class FileIO : IO {
    override fun read(): List<String> {
        return File("src/main/kotlin/input.txt").readLines()
    }
    override fun write(lines: ArrayList<String>) {
        val file = File("src/main/kotlin/output.txt")
        file.printWriter().use {out -> out.print("")}
        for (line in lines){
            file.appendText(line.toString()+"\n")
        }
    }
}

fun solve(lines: List<String>): ArrayList<Int> {
    val nums: ArrayList<Int> = arrayListOf()
    for (line in lines) {
        if (line != "Неверный формат выражения"){
            val list = line
                .trim()
                .split(Regex("[+/*-]"))
                .map { it.toInt() }
            val sign = "[+*/-]"
                .toRegex()
                .find(line)
            var res = 0
            if (sign != null) {
                nums.add(when (sign.value) {
                    "+" -> list.sum()
                    "-" -> list[0] - list[1]
                    "*" -> list[0] * list[1]
                    else -> list[0] / list[1]
                })
            }
        }
        else{
            nums.add(-1)
        }
    }
    return nums
}

fun main() {
    print("Ввод данных в консоли или из файла (C/F)?: ")
    val io = when (readlnOrNull()) {
        "C" -> {
            ConsoleIO()
        }

        "F" -> {
            FileIO()
        }

        else -> return
    }
    val result = io.read()
    io.convert(solve(result))

}