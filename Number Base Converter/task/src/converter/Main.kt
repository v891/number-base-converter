package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

val intRange = '0'..'9'
val stringRange = 'a'..'z'
val list = intRange + stringRange
fun main() {
    println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
    val readLine = readLine()!!

    if (readLine == "/exit") {
        return
    }

    val (sBase, tBase) = readLine.split(" ").map(String::toInt)

    runConversionLogic(sBase, tBase)
}

fun runConversionLogic(sBase: Int, tBase: Int) {
    println("Enter number in base $sBase to convert to base $tBase (To go back type /back): ")
    var input = readLine()!!
    if (input == "/back") {
        main()
        return
    }

    var fractionOut = ""
    if (input.contains(".")) {
        val fraction = input.split(".")[1]
        input = input.split(".")[0]
        val baseToDecimalFraction = baseToDecimalFraction(fraction, sBase)

        fractionOut = decimalToBaseFraction(baseToDecimalFraction, tBase.toBigDecimal()).padEnd(5, '0')
    }

    val reverseConversion = baseToDecimal(input, sBase)

    val baseConversion = decimalToBase(reverseConversion, tBase.toBigInteger())

    println("Conversion result: $baseConversion${if (fractionOut.isNotEmpty()) ".$fractionOut" else ""}")

    runConversionLogic(sBase, tBase)

}

private fun baseToDecimal(input: String, base: Int): BigInteger {

    var out = BigInteger.ZERO;
    for (i in input.indices) {
        out += (base.toBigInteger().pow(i) * (list.indexOf(input[input.length - 1 - i])).toBigInteger())
    }

    return out
}

private fun baseToDecimalFraction(input: String, base: Int): BigDecimal {

    var out = BigDecimal.ZERO;
    for (i in input.indices) {
        out += ((list.indexOf(input[i])).toBigDecimal().divide(base.toBigDecimal().pow(i + 1), 6, RoundingMode.HALF_DOWN))

    }

    return out
}

private fun decimalToBase(number: BigInteger, base: BigInteger): String {

    var q = number

    var out = ""
    while (q >= base) {
        out += convertReminder(q % base)
        q /= base
    }
    out += convertReminder(q)

    return out.reversed()
}

private fun isIntegerValue(bd: BigDecimal): Boolean {
    return bd.stripTrailingZeros().scale() <= 0
}

private fun decimalToBaseFraction(number: BigDecimal, base: BigDecimal): String {
    var q = number
    var out = ""
    for (i in 1..5) {
        q = q.multiply(base)

        out += convertReminder(q.toBigInteger())
        if (isIntegerValue(q)) {
            return out
        }
        q = q.remainder(BigDecimal.ONE)

    }
    return out
}

fun convertReminder(num: BigInteger): String {
    return list[num.toInt()].toString()
}