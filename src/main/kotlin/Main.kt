import java.io.File

interface IO {
    fun read(): Any
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
    while(true) {
        print("Ввод данных в консоли или из файла (C/F)?: ")
        when (readlnOrNull()) {
            "C" -> {
                val console: ConsoleIO = ConsoleIO()
                val result = console.read()
                console.write(console.convert(solve(result)))
                break
            }

            "F" -> {
                val file: FileIO = FileIO()
                val result: List<String> = file.read()
                file.write(file.convert(solve(result)))
                break
            }
            else -> println("Повторите ввод")
        }
    }

}