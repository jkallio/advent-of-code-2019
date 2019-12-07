/**
 * --- Day 4: Secure Container ---
 * You arrive at the Venus fuel depot only to discover it's protected by a password. The Elves had written the password
 * on a sticky note, but someone threw it out.
 *
 * However, they do remember a few key facts about the password:
 *
 *      - It is a six-digit number.
 *      - The value is within the range given in your puzzle input.
 *      - Two adjacent digits are the same (like 22 in 122345).
 *      - Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
 *
 * Other than the range rule, the following are true:
 *      - 111111 meets these criteria (double 11, never decreases).
 *      - 223450 does not meet these criteria (decreasing pair of digits 50).
 *      - 123789 does not meet these criteria (no double).
 *
 * How many different passwords within the range given in your puzzle input meet these criteria?
 *
 * Your puzzle input is 197487-673251.
 *
 *
 * --- Part Two ---
 * An Elf just remembered one more important detail: the two adjacent matching digits are not part of a larger group of
 * matching digits.
 *
 * Given this additional criterion, but still ignoring the range rule, the following are now true:
 *      - 112233 meets these criteria because the digits never decrease and all repeated digits are exactly two digits long.
 *      - 123444 no longer meets the criteria (the repeated 44 is part of a larger group of 444).
 *      - 111122 meets the criteria (even though 1 is repeated more than twice, it still contains a double 22).
 *
 * How many different passwords within the range given in your puzzle input meet all of the criteria?
 *
 * Your puzzle input is still 197487-673251.
 */

fun checkRules_Part1(value: Int) : Boolean {
    var rule1 = false
    val digits = value.toString()
    for (i in 0 until digits.count()) {
        if (i > 0) {
            // Rule-1: Two adjacent digits are the same (like 22 in 122345).
            if (digits[i] == digits[i-1]) {
                rule1 = true
            }
            // Rule-2: Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
            if (digits[i].toInt() < digits[i-1].toInt() ) {
                return false
            }
        }
    }
    return rule1
}

fun checkRules_Part2(value: Int) : Boolean {
    var sameDigitCount = 0
    var rule1 = false
    val digits = value.toString()
    for (i in 0 until digits.count()) {
        if (i > 0) {
            // Rule-1: Two adjacent digits are the same (like 22 in 122345).
            // BUT the two adjacent matching digits are not part of a larger group of matching digits.
            if (digits[i] == digits[i - 1]) {
                sameDigitCount += 1
            }
            else {
                if (sameDigitCount == 1) {
                    rule1 = true
                }
                sameDigitCount = 0
            }
            // Rule-2: Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
            if (digits[i].toInt() < digits[i - 1].toInt()) {
                return false
            }
        }
    }
    return rule1 || sameDigitCount == 1
}

fun main (args: Array<String>) {
    val min = 197487
    val max = 673251
    var part1Count = 0
    var part2Count = 0
    for (i in min..max) {
        part1Count += if (checkRules_Part1(i)) 1 else 0
        part2Count += if (checkRules_Part2(i)) 1 else 0
    }
    println("Different passwords (Part1) = $part1Count")
    println("Different passwords (Part2) = $part2Count")
}