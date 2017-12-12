package ru.kontur.postavki

import junit.framework.Assert.*
import org.junit.Test

/**
 * Created by skorokhodov on 08.12.2017.
 */

class Day8 {

    companion object {

        fun parseData(str: String)= str.split("\n")
                .map (String::trim).filter(String::isNotBlank).map { Operation.fromStr(it) }

        fun calculateRegisters(l : List<Operation>): Pair<Int, Int> {
            val registerValues: MutableMap<String, Int> = mutableMapOf()
            var maxValue = 0
            l.forEach { operation ->
                val registerVal = registerValues.getOrDefault(operation.condition.register,0)
                if(operation.condition.isValid(registerVal)) {
                    val reg = registerValues.getOrDefault(operation.action.register,0)
                    val res = operation.action.calculate(reg)
                    if(res> maxValue) maxValue = res
                    registerValues[operation.action.register]= res
                }
            }
            return Pair(registerValues.values.max()!!, maxValue)
        }
    }
}

class Operation(val action: Condition, val condition: Condition) {

    companion object {
        fun fromStr(str: String) : Operation
        {
            val parts = str.split("if").map { it.trim() }
            return Operation(Condition.fromStr(parts[0]), Condition.fromStr(parts[1]))
        }
    }

    override fun toString(): String {
        return "$action if $condition"
    }
}

open class Condition(val register: String, val operation: String, val value: Int) {

    fun isValid(registerValue: Int): Boolean {
        return when (operation) {
            ">" ->  registerValue > value
            ">=" ->  registerValue >= value
            "<" ->  registerValue < value
            "<=" ->  registerValue <= value
            "==" ->  registerValue == value
            "!=" ->  registerValue != value
            else ->  throw IllegalArgumentException("unknown operation $operation")
        }
    }

    fun calculate(reg: Int): Int {
        return when(operation) {
            "inc" -> reg + value
            "dec" -> reg - value
            else -> throw IllegalArgumentException("unknown operation $operation")
        }
    }

    companion object {
        fun fromStr(str: String) :Condition {
            val parts = str.split(" ").map{ it.trim() }
            return Condition(parts[0], parts[1], parts[2].toInt())
        }
    }

    override fun toString(): String {
        return "[$register $operation $value]"
    }

}


class Day8Test {

    @Test
    fun testCalculate() {
        assertEquals(Pair(1,10),Day8.calculateRegisters(Day8.parseData(data1)))
        assertEquals(Pair(5143, 6209),Day8.calculateRegisters(Day8.parseData(data)))
    }

    @Test
    fun test1Parse() {
        val op = Day8.parseData(data1)
//        println(op)
    }

    @Test
    fun testOperationCalculate() {
        var op = Operation.fromStr("ev dec 705 if cag != 2")
        assertTrue(op.condition.isValid(0))
        assertFalse(op.condition.isValid(2))
        assertEquals(-705, op.action.calculate(0))

        op = Operation.fromStr("ev dec -705 if cag >= 2")
        assertFalse(op.condition.isValid(1))
        assertTrue(op.condition.isValid(2))
        assertTrue(op.condition.isValid(3))
        assertEquals(705, op.action.calculate(0))

        op = Operation.fromStr("ev inc -705 if cag >= 2")
        assertEquals(-705, op.action.calculate(0))

        op = Operation.fromStr("ev inc 705 if cag >= 2")
        assertEquals(705, op.action.calculate(0))

        op = Operation.fromStr("ev inc 705 if cag >= 2")
        assertEquals(0, op.action.calculate(-705))
    }


    val data1 = "b inc 5 if a > 1\n" +
            "a inc 1 if b < 5\n" +
            "c dec -10 if a >= 1\n" +
            "c inc -20 if c == 10\n"

    val data = """
ev dec -705 if cag != 2
b dec -195 if ty >= -8
red dec -404 if v > -7
d inc -971 if k >= -9
k inc 303 if k > -8
cag inc 539 if ev > 697
ev inc -11 if d <= -963
alf inc -634 if xoc < -2
eb dec 927 if b <= 201
e dec 67 if eb >= -930
he dec -29 if qby < 8
cox inc 193 if b >= 188
red dec 340 if alf >= -7
k dec 8 if red > 57
eb inc -606 if e <= -75
xoc inc 337 if red == 64
xoc inc 186 if mk == 0
cox inc 840 if npo >= -5
s inc -449 if anb != -3
eb inc -233 if npo < 2
npo inc -808 if mk <= 9
eb dec 139 if eb > -1161
ty inc -929 if ty > -7
mk inc 789 if d > -977
red dec 915 if k < 299
anb inc 852 if mk == 789
red dec 242 if swy == -6
e inc -29 if cag < 546
alf dec -219 if cag > 535
red dec -298 if ty < -921
tm dec 175 if obo <= 2
alf dec 553 if eb != -1298
alf inc -870 if s != -449
v dec 744 if cox < 1036
d dec -720 if he > 24
ty inc -201 if k <= 301
xoc inc 956 if v > -744
s dec 539 if e >= -100
mk dec -37 if anb != 852
mk dec 833 if tm < -167
ev dec -410 if ev < 686
s dec 77 if cag != 539
anb inc 917 if swy >= -2
obo dec -5 if s >= -997
qby inc -231 if tm == -175
ev dec 378 if tm <= -167
b dec -106 if s < -985
s dec 301 if e < -89
npo inc 232 if b == 301
red dec -960 if mqk < -9
he inc -789 if k == 300
npo dec 826 if cox < 1033
e inc 101 if k > 291
v inc -898 if he > 27
alf inc 141 if anb > 1767
red inc -613 if e < -2
tm inc -151 if cag >= 530
k inc -346 if b != 305
he dec 967 if cag <= 546
ev dec -931 if cag < 543
swy dec 309 if qby < -228
d inc 437 if cox > 1030
ty dec 586 if ev <= 1249
alf dec 299 if e <= -2
d inc 977 if xoc == 523
d inc 594 if s <= -1282
ev inc 21 if swy < -306
qby dec -722 if cag < 543
k inc -381 if ev < 1259
alf inc 847 if anb > 1764
k inc 126 if s <= -1292
eb inc -109 if mqk > -4
obo dec 900 if swy < -300
obo dec 364 if mk <= -35
swy inc -211 if eb >= -1416
ty dec -680 if mqk <= 1
ty dec -235 if cox > 1024
cag dec -261 if red <= -559
obo dec -897 if mqk == 0
tm inc -981 if mk < -49
ty inc -805 if eb >= -1402
mk inc 278 if red < -552
ev inc 63 if v > -1645
swy dec -388 if xoc <= 530
mqk inc 187 if obo != -357
s inc 416 if b < 309
qby inc 399 if mqk >= 185
red inc -680 if mqk != 188
he inc -981 if eb >= -1407
d inc 225 if tm >= -327
he dec 158 if d <= 1991
d dec 74 if qby == 890
k dec -930 if anb <= 1770
s dec -546 if mqk == 179
ty dec 733 if k >= 874
ev dec 581 if obo == -362
cox dec -922 if obo == -362
obo dec 922 if e == 5
d inc -948 if tm == -326
cag inc 161 if mk <= 241
eb inc -580 if e != -1
anb inc -632 if qby != 889
red dec -285 if mk <= 239
qby inc 550 if eb >= -1997
e dec 930 if k > 869
eb inc 604 if swy > -138
obo inc 383 if he != -1100
s dec -89 if eb <= -1377
qby dec -821 if ty < -1532
ty inc 940 if qby >= 2257
alf inc -421 if swy != -123
qby inc 787 if b != 293
alf inc -80 if b < 301
b dec 205 if eb <= -1378
qby dec 870 if obo != -892
d inc 183 if swy == -132
s dec -81 if mqk != 187
swy inc 976 if d >= 1138
red dec 281 if anb == 1138
xoc inc 681 if d < 1141
v dec 14 if obo > -902
he dec -525 if xoc <= 530
v inc -866 if red >= -955
cag inc -835 if mk != 240
qby inc -549 if s <= -781
eb dec 584 if mk >= 229
qby dec -711 if obo >= -904
ev inc -400 if anb <= 1144
k inc 82 if swy >= 840
e dec -415 if mk != 229
red dec -923 if anb > 1128
v dec 602 if b != 86
ty dec -699 if ev > 346
xoc dec 549 if k > 968
k inc 317 if qby < 2343
npo dec 919 if d >= 1137
e inc 421 if e > -519
eb dec -104 if ev <= 351
b inc 368 if qby != 2342
mk dec -987 if ev == 357
eb dec -509 if alf <= 236
obo dec -380 if v != -3127
npo dec -790 if b < 455
xoc inc 405 if k < 1287
obo dec -56 if b < 465
swy dec 194 if k != 1281
red inc -562 if tm <= -326
swy dec 75 if e <= -86
obo dec -4 if ev == 350
e inc 533 if xoc < 931
cox inc -584 if d < 1146
swy inc 506 if eb >= -1354
obo dec 458 if k >= 1279
swy inc 447 if e > 447
qby dec 847 if cox <= 1377
xoc inc 225 if xoc < 931
obo dec 301 if b > 462
cag inc -740 if e > 438
tm dec -83 if e <= 447
tm dec -151 if cag <= -866
npo inc 26 if s < -784
v inc 12 if cox != 1368
eb inc -399 if ev > 340
eb dec 780 if d == 1143
e dec 87 if tm == -91
k dec -13 if anb <= 1139
v dec 829 if swy >= 567
k dec -229 if ev < 351
qby inc 972 if xoc == 1143
s inc 609 if s >= -789
mk dec -931 if ev > 346
mqk inc -605 if cag > -880
v inc 302 if anb <= 1139
mk dec 342 if v > -3647
xoc inc -905 if k <= 1523
qby inc -412 if qby == 1493
v dec -548 if d > 1142
xoc inc 311 if obo >= -771
npo inc -955 if xoc < 551
swy inc -885 if mk > 821
ty dec 91 if ty >= 100
s dec -840 if npo <= -1492
qby inc 158 if ev > 342
ev inc 227 if cag >= -878
alf dec 612 if tm <= -92
ev inc 395 if he <= -572
s inc 570 if swy > -311
s inc 283 if alf > -382
xoc inc 297 if tm >= -94
v dec 100 if d != 1153
cag inc 928 if ty <= 19
tm inc 829 if obo > -765
alf inc -94 if ty >= 8
ty inc -224 if cox <= 1367
obo dec -352 if d > 1137
mqk inc 223 if ev <= 580
cag inc 55 if eb >= -2541
red dec 969 if alf > -474
alf inc -564 if e != 447
qby inc -97 if obo < -400
red dec -530 if cag > 107
ev inc -579 if he > -574
tm dec -788 if eb <= -2528
alf inc -217 if v > -3191
tm inc 635 if cox >= 1362
tm inc -217 if mqk < -189
cag dec -547 if v != -3191
red dec -241 if alf > -1045
red inc -492 if b <= 473
he inc 46 if b > 454
xoc inc 86 if ty > 8
d dec -755 if qby < 1140
mk inc -347 if obo <= -405
v dec 870 if npo >= -1497
npo inc 216 if red != -1287
anb inc -574 if obo > -414
ev inc -364 if eb == -2534
anb dec -825 if npo < -1277
k dec 587 if cox < 1372
anb inc -888 if obo > -401
swy dec -435 if npo != -1283
e dec 551 if e != 444
swy dec 772 if alf == -1036
tm dec -339 if e == 444
swy dec 259 if mk == 470
xoc inc -224 if ev >= -359
ty inc 265 if red >= -1279
obo dec 449 if mqk > -202
d inc 891 if e >= 438
cag dec 606 if b >= 461
mk inc -894 if v != -4064
cag dec -128 if d != 2034
eb dec 723 if eb <= -2528
swy inc -12 if s < 1525
s inc -569 if d == 2032
npo inc 326 if alf >= -1038
cox dec -213 if anb == 1379
obo dec -222 if d > 2026
ev dec 809 if anb == 1393
s inc -637 if alf != -1037
s dec -892 if xoc == 942
eb inc -248 if cag != -491
b dec 499 if qby != 1142
b dec 543 if d <= 2033
mk inc 345 if mk < -411
ty dec -284 if npo != -960
d dec -327 if eb < -3498
cag inc -743 if mqk > -196
cag dec 708 if ev > -374
eb inc 935 if swy == 113
d dec -908 if swy == 113
npo inc -753 if swy == 113
cag dec -227 if alf > -1045
red dec -658 if v <= -4061
cox inc -66 if anb < 1397
cag dec -209 if b > 463
cox inc -245 if alf != -1037
alf dec -212 if s >= 2402
d inc 590 if d >= 3273
xoc inc -325 if xoc > 935
red inc 935 if he >= -519
tm dec 332 if ev >= -366
cag inc 583 if xoc <= 615
e inc -892 if e >= 439
npo inc -628 if obo > -629
red inc -819 if tm <= 1958
qby dec -960 if ev > -372
red dec -781 if tm < 1959
cox inc -831 if xoc < 621
b inc -830 if ty >= 559
anb inc 654 if cag < -1507
cag dec 995 if red >= -659
npo inc 356 if b < -361
tm dec 286 if qby > 2093
mk dec 369 if ty <= 569
mk dec -971 if qby != 2112
swy inc 137 if xoc > 617
alf dec -743 if npo > -1355
alf inc 39 if ty < 569
tm dec -42 if s <= 2413
anb inc -685 if alf != -48
ty dec 660 if anb <= 1360
npo dec 313 if ev < -359
cag inc -611 if obo >= -646
tm inc 728 if v <= -4067
swy inc -4 if cag != -3124
npo inc 359 if xoc >= 613
obo inc 694 if b != -358
obo dec 439 if e >= -452
npo dec -160 if xoc <= 626
red inc -659 if e <= -439
b inc -445 if obo == -382
red dec 543 if k > 931
s dec 274 if qby != 2093
cag dec -338 if k <= 934
alf dec -211 if alf == -45
v dec -451 if tm == 1706
obo dec -465 if v > -3606
k inc 415 if qby > 2100
anb dec 220 if swy == 109
alf inc -3 if cox < 477
d dec 164 if s > 2131
qby dec -974 if alf >= -40
cag dec 891 if anb < 1146
d dec -337 if qby > 2100
cag dec -628 if d != 3442
k dec -648 if alf == -46
red dec -329 if k == 1996
e inc -891 if alf <= -38
cox inc -626 if ty >= -99
mqk inc -349 if mqk >= -204
alf inc 388 if qby > 2092
mk inc -430 if d > 3443
eb dec 338 if alf < 352
mk inc -215 if cox == -147
e dec 171 if b >= -819
alf dec -205 if d != 3433
d inc -490 if qby == 2102
e inc 612 if he > -528
anb inc -811 if mqk > -543
he inc 685 if cox >= -154
red dec -748 if anb <= 1129
ty dec -872 if ty >= -97
b inc -920 if eb == -2908
s inc -701 if mqk >= -548
tm inc 136 if alf > 545
he dec -100 if e != -898
qby inc 552 if anb >= 1132
xoc inc 700 if xoc == 617
xoc dec 827 if obo != -385
anb inc 738 if d != 2956
eb inc 476 if mqk == -544
s dec -100 if ev == -366
obo inc -602 if e == -898
tm inc -80 if xoc == 490
eb inc 160 if eb == -2432
he inc 229 if he == 159
tm inc 453 if b > -1739
obo dec 786 if qby < 2655
swy inc -120 if qby != 2651
ev dec -382 if obo != -1770
qby dec 41 if eb < -2265
cox inc -991 if obo > -1765
cag inc -713 if obo > -1773
he inc -958 if red <= -1522
ty inc -426 if mqk != -551
k inc 902 if ty == 349
npo dec -550 if eb < -2274
swy inc -30 if d < 2954
anb dec 990 if npo < -1139
ty dec 301 if red >= -1529
ev inc -687 if ty > 347
ty dec 261 if he <= -802
b dec -313 if ev < -1043
alf dec 231 if k != 2889
npo inc 956 if qby >= 2618
cag inc -39 if npo <= -1135
he dec 117 if qby > 2605
v inc -574 if cag > -4421
npo inc -179 if mk <= 538
v inc -737 if he < -910
npo inc 722 if swy < -37
mk inc 200 if alf != 313
npo dec -604 if mqk == -544
ev dec -794 if ev > -1048
eb inc -384 if npo == 3
he dec -10 if qby != 2614
e dec -317 if e > -906
e inc -662 if d >= 2946
s inc 941 if npo != 0
xoc inc 765 if e <= -1235
b inc 434 if ty > 354
e inc 513 if anb > 880
cox inc 146 if mk > 734
mk inc -31 if alf == 316
qby inc 887 if mqk == -547
obo dec -87 if mqk != -536
he inc -230 if tm != 2217
e dec -469 if alf < 326
alf inc 608 if d != 2950
mqk dec -78 if v < -4355
b inc -865 if ty < 356
s dec 57 if npo < 7
npo dec -100 if cag > -4428
cag inc -217 if red > -1522
b dec 338 if k == 2898
npo inc -324 if k <= 2906
mk dec -763 if obo >= -1687
e inc 760 if d > 2945
e dec 984 if v == -4350
obo inc 651 if he <= -1126
red dec -380 if d >= 2950
b inc 11 if k > 2888
v dec -406 if red <= -1142
anb dec -900 if e <= 501
ty inc -2 if mk != 1471
tm dec -655 if qby > 2619
k inc 142 if mqk < -540
ev inc -346 if xoc != 1265
xoc dec -284 if k >= 3032
d inc -908 if k == 3040
e inc 36 if swy == -41
xoc dec 372 if alf != 930
s dec -327 if xoc != 1159
e inc 56 if ty < 343
v inc -445 if npo == -221
xoc dec 265 if s >= 2746
k dec 604 if swy >= -42
alf inc 918 if eb > -2662
mk inc -227 if cox != -150
cox dec -693 if qby <= 2614
d inc 88 if k != 2434
tm dec -278 if k > 2428
v dec 384 if red <= -1145
npo inc 854 if ev != -1390
he inc 69 if xoc >= 893
d inc 587 if eb <= -2650
b dec 873 if anb == 1785
tm dec -557 if mk != 1230
k dec -335 if k <= 2437
d inc -215 if cag != -4424
obo dec -754 if red == -1140
d inc 369 if v != -4766
tm dec -12 if red >= -1155
anb dec -836 if xoc <= 892
s inc -170 if cag <= -4422
k dec -717 if mk != 1234
npo inc -790 if k >= 2764
obo dec -87 if red <= -1157
v dec -894 if ev >= -1401
cag inc -440 if swy >= -49
e inc 31 if d > 3091
v dec -581 if he != -1057
anb dec 760 if k <= 2775
swy dec 513 if v != -3295
s inc 840 if mk > 1242
npo dec -359 if mqk > -549
red dec -958 if ev >= -1403
ty dec -246 if qby != 2617
k inc -28 if cox <= 544
npo inc -881 if cox > 545
ty dec -733 if ev < -1391
e dec 750 if cag <= -4863
cox dec -44 if alf < 1843
mk inc 751 if ev < -1390
he inc -968 if mk > 1980
d dec -431 if npo != 202
v inc 406 if xoc != 902
tm inc 635 if mk <= 1991
npo dec 401 if anb <= 1022
v dec 940 if obo >= -1034
npo dec 572 if qby < 2619
swy inc 934 if anb != 1021
eb inc 499 if swy == 893
mqk inc 17 if s < 2568
mqk dec -704 if alf != 1836
ty inc -301 if mqk != 162
npo dec 29 if cox >= 579
alf inc -677 if npo > -409
mk dec -399 if npo == -399
cox dec 639 if e == -213
ev inc 602 if qby < 2617
ev dec -238 if eb != -2149
mqk inc 26 if k > 2734
anb dec -153 if v > -4238
b inc -331 if cox != 584
mk dec 411 if s > 2574
xoc dec -631 if xoc == 902
mqk dec 795 if d != 3083
swy dec -911 if anb > 1184
anb dec 368 if e >= -217
b dec 463 if qby != 2612
qby dec -127 if v <= -4226
alf inc -848 if npo != -409
obo dec -499 if s >= 2571
swy dec 165 if v < -4225
alf dec 71 if mqk == -609
b dec -717 if d != 3090
cag inc 721 if tm <= 3694
xoc inc 304 if ty > 1033
swy inc 728 if d < 3096
tm inc -690 if eb >= -2163
qby inc 979 if ty == 1025
s dec -175 if swy >= 1449
s dec 639 if he > -2038
mqk dec 615 if red <= -189
he dec 481 if cox >= 578
anb inc -329 if xoc <= 1532
anb inc 350 if red != -192
obo inc 720 if s != 2108
ev dec -316 if s > 2111
mqk inc 322 if alf == 246
d dec -723 if cox > 580
b inc -301 if alf >= 254
cox dec 847 if xoc > 1528
v dec -298 if cox <= -254
eb dec -842 if cag != -4874
he inc 483 if eb != -1324
b inc -160 if npo >= -400
tm dec 885 if obo == 187
anb inc -505 if tm <= 2129
red dec 387 if e < -212
e dec 761 if npo <= -397
he dec 303 if tm == 2128
s dec 308 if b < -3718
mqk dec -207 if he > -2041
ev inc -655 if xoc != 1533
s inc -601 if d > 3809
ev dec 666 if cox < -256
ty inc 384 if he != -2041
v inc -560 if ty >= 1403
ty inc 446 if obo != 194
obo inc -570 if cag != -4868
obo inc 21 if k == 2743
cox dec -183 if mk != 1973
s inc -780 if eb <= -1306
b inc -173 if tm != 2131
b inc 489 if v > -4488
swy inc 337 if s < 431
ty inc 36 if alf >= 240
xoc inc -941 if s < 427
xoc dec -533 if xoc <= 600
eb dec 269 if swy == 1795
eb inc -718 if swy < 1796
xoc dec 319 if eb <= -2039
ev dec 855 if npo < -390
v inc 208 if d >= 3805
d inc -846 if tm == 2122
red inc -636 if mk <= 1977
d dec 695 if ev == -1764
e dec -864 if cox == -268
v dec -913 if d == 2270
alf inc -364 if cox > -270
he dec -211 if cag != -4862
b dec 624 if b != -3887
v dec -953 if b < -4514
ty dec 631 if b < -4518
e dec -496 if obo <= -367
d dec -911 if cag >= -4872
mk inc -549 if xoc >= 1117
alf dec -586 if e <= -971
cag dec -919 if k == 2743
qby inc -803 if cag <= -3943
k dec 18 if k > 2734
ty dec 537 if qby > 2909
swy dec 973 if tm >= 2122
d dec -502 if qby == 2916
ty inc 50 if he >= -1816
cag dec 344 if e > -985
cag inc -967 if anb < 311
ev inc 193 if npo != -393
ev inc -934 if cag >= -5265
ev dec 416 if d == 3683
ty inc -963 if d < 3688
s dec -904 if s <= 424
b dec 52 if b >= -4524
alf inc -427 if obo <= -363
red dec -686 if cox > -268
b dec 551 if mqk == -695
xoc dec -670 if v > -2428
anb inc 310 if ty >= 401
ev dec 768 if red < -519
tm dec -391 if k == 2725
b inc -624 if xoc < 1797
s inc -962 if ty != 391
xoc inc -482 if mqk > -703
alf inc -110 if v < -2418
mqk inc -812 if s < 1329
cag dec -584 if red >= -529
qby dec -503 if npo >= -405
npo dec -592 if npo <= -396
anb inc -611 if anb < 313
anb inc -537 if red < -538
eb inc 781 if cag < -4664
xoc dec -241 if he >= -1821
k inc -974 if swy >= 816
eb dec -649 if ev > -3690
he dec 258 if b > -5750
cag inc -618 if tm >= 2513
b inc -132 if npo == 190
cox inc -187 if mk > 1427
tm dec 244 if npo <= 198
cag inc -21 if alf != 350
npo inc -332 if ev >= -3696
obo inc -144 if ev < -3698
npo inc 652 if k >= 1744
anb inc 682 if b > -5739
mk dec -585 if eb > -607
ty dec -395 if red <= -521
d dec -118 if cox < -260
obo dec 97 if d < 3797
mk inc 561 if s != 1327
mqk inc 341 if xoc < 1558
eb dec 136 if xoc >= 1546
eb dec -144 if b == -5744
b dec 416 if xoc < 1560
e inc -688 if red <= -520
cag inc -846 if cox <= -264
e inc 858 if mk >= 2009
d inc 120 if mqk < -1165
eb inc 275 if anb < -304
d dec 426 if e <= -801
obo inc -175 if alf == 358
b inc 411 if v > -2433
anb dec -285 if qby != 3420
e inc -899 if cox != -257
ty inc 764 if xoc >= 1552
eb inc 157 if tm == 2273
alf dec 416 if xoc <= 1562
mqk inc -251 if d <= 3498
s inc -405 if e != -1698
he dec 280 if v < -2419
mqk dec 783 if e == -1705
s dec -530 if mk > 2013
cox dec 601 if qby >= 3421
alf dec -214 if cox <= -253
cox inc -280 if v < -2418
obo inc -617 if k > 1749
tm dec 400 if v >= -2427
obo inc -810 if swy == 820
cag inc 245 if red < -524
k inc 939 if obo < -1973
xoc inc -867 if d != 3499
ev inc 930 if ev >= -3690
npo inc 490 if npo == 513
obo dec -127 if tm <= 1874
v inc -937 if cox < -539
cox inc 806 if eb < -315
tm inc 868 if b == -5749
d inc 370 if cox != 264
mk dec 735 if d >= 3488
v dec 651 if tm >= 2736
d dec 751 if d == 3495
eb inc 73 if eb == -320
anb inc 946 if mqk >= -2202
k inc -879 if tm != 2731
cag dec 329 if cag >= -5075
v dec 110 if npo <= 998
cox inc -324 if v != -4012
s dec -43 if xoc <= 688
xoc inc 154 if ty <= 1552
npo dec 126 if e == -1705
mqk inc 631 if qby < 3428
obo dec 875 if k < 882
eb inc -159 if d >= 2737
ty inc 158 if mk < 1266
cox inc -726 if cox != -65
red inc 699 if he <= -2352
anb dec 348 if he == -2353
mqk dec -25 if cox <= -792
he inc -72 if ty == 1550
tm dec -396 if obo >= -2712
mqk inc -730 if alf >= 153
ty inc 950 if b <= -5741
qby inc 711 if b <= -5747
he inc 990 if ty == 2500
eb inc 691 if cag <= -5394
v dec 315 if v >= -4001
cag inc 831 if k == 872
cag dec -285 if s != 974
tm inc 500 if k >= 865
ty inc -407 if ev >= -2761
ev dec -960 if v >= -4019
anb dec 698 if cox < -781
swy dec 265 if cox <= -783
e dec -962 if anb < 237
he dec -152 if swy != 555
npo dec -416 if alf < 159
red dec 450 if qby > 4127
cag inc 253 if s <= 964
cox dec -969 if tm <= 3634
e dec 442 if qby < 4135
mk dec 890 if qby > 4128
cag dec -934 if ty != 2085
cox dec 905 if k != 876
red inc 595 if k <= 862
b inc 105 if npo > 1286
obo dec 502 if mk == 394
ty dec 533 if obo != -2718
ev inc -6 if mk < 386
npo inc -144 if d < 2746
e inc -232 if ev >= -1813
alf inc -78 if ty > 1553
eb inc -637 if ev != -1796
tm dec 132 if e != -1425
swy inc 667 if alf == 78
red dec -166 if mk <= 387
swy dec -173 if v >= -4014
qby inc 788 if qby > 4121
ty dec 172 if qby <= 4919
red dec 447 if tm == 3501
obo dec 366 if alf == 78
he dec -842 if mqk != -2305
xoc inc -10 if d >= 2736
ev inc 517 if cox != -726
mqk dec 865 if npo != 1151
obo inc 29 if red <= -552
qby inc -337 if b == -5644
cag dec -970 if mqk != -3172
e dec 343 if xoc != 827
d dec -890 if v == -4017
v dec 793 if anb > 220
cox dec -442 if he == -599
eb dec -759 if anb > 225
obo dec 45 if k == 872
obo dec -534 if e > -1764
cag inc 344 if npo >= 1149
eb inc -458 if e <= -1764
obo dec 55 if mqk > -3167
s dec -487 if v > -4813
xoc dec -51 if d >= 2744
ty dec 568 if ty >= 1387
cag inc -698 if cox <= -276
cag dec 162 if mqk > -3170
k inc 563 if qby > 4579
alf inc -784 if tm >= 3498
mqk dec 520 if obo > -2624
npo inc -780 if qby > 4576
alf dec 152 if he >= -602
v inc -614 if eb <= 415
he dec 659 if k < 1445
ev inc -49 if d >= 2740
e dec 302 if v >= -5424
ty dec -877 if alf < -867
qby inc 368 if swy != 1394
anb inc 732 if swy < 1396
mk inc 61 if ev > -1344
b inc -66 if cox == -280
npo inc 11 if swy <= 1397
swy inc 292 if alf >= -852
red inc -606 if mqk == -3684
e dec -460 if cox > -272
s dec -469 if cag > -2898
tm dec -717 if obo > -2618
xoc dec -796 if cag != -2893
ev inc -145 if tm < 4222
e dec 892 if red == -1167
red inc -403 if red == -1164
tm dec 519 if npo != 389
cag dec -107 if cag <= -2900
ev inc -385 if d > 2743
qby inc -444 if alf <= -857
ev inc -201 if swy != 1398
npo dec -851 if s >= 1922
s inc 975 if s <= 1923
alf inc -906 if v != -5413
obo dec 870 if ty >= 815
obo inc -26 if anb == 959
e dec -65 if e <= -2951
ty dec -835 if anb == 958
anb dec -49 if swy > 1394
mk inc -253 if mqk <= -3680
e dec -862 if b != -5708
qby dec -293 if s == 2896
s inc -845 if cox != -271
xoc inc 509 if npo >= 387
obo dec -758 if mqk >= -3675
v inc 370 if b < -5703
cox dec -986 if red >= -1172
b inc -682 if d >= 2739
k dec 568 if v >= -5055
mk inc 451 if alf == -1764
ty inc -56 if mk <= 645
eb dec 173 if s <= 2051
s inc 836 if ty != 764
alf dec 345 if cag <= -2887
cox dec 206 if k != 867
eb inc 44 if s > 2053
d dec -294 if cag <= -2891
he dec 904 if swy >= 1386
ty dec 499 if swy < 1399
e dec 891 if tm > 3698
b dec -732 if v < -5057
qby inc 722 if red != -1168
e inc -609 if npo != 379
b inc 741 if e < -3525
b inc -833 if tm >= 3701
red inc 282 if ev > -2072
obo dec 441 if b < -5647
xoc dec -612 if anb != 1005
s dec 67 if mk > 647
cox dec -341 if red > -879
v inc 316 if qby <= 5519
mk inc -251 if ty == 271
ty dec 587 if b <= -5658
anb dec 204 if d < 3044
he dec -358 if qby < 5521
s dec -594 if obo < -3946
xoc dec -802 if d > 3029
alf dec -536 if alf <= -2105
tm dec 209 if cox <= 711
s inc -142 if anb > 809
s inc -571 if mqk >= -3676
npo inc 607 if alf == -1563
swy inc -438 if swy >= 1388
b inc 325 if xoc != 3092
k dec 338 if s <= 2650
cag dec -748 if he == -1804
npo inc -186 if npo <= 382
cag inc -920 if tm != 3483
swy dec 288 if mk >= 652
ty dec 362 if mqk != -3685
alf inc 291 if anb == 811
mqk inc 203 if red == -890
swy inc 712 if eb >= 231
ty dec 327 if v > -5052
npo inc -453 if mqk == -3684
cox inc 747 if mqk < -3685
qby dec 844 if cox >= 698
cag inc 785 if swy != 1659
red dec -820 if xoc > 3086
anb inc -2 if cox < 709
cox inc 91 if cag <= -2280
cox inc 988 if mk <= 643
ev inc 895 if ty != -433
red dec -411 if npo == -259
npo dec 831 if npo > -261
k dec 799 if mk == 643
qby inc 373 if v != -5054
e inc 949 if swy > 1668
qby inc 278 if d > 3037
eb inc -839 if obo != -3946
mk dec 758 if anb <= 800
npo inc -662 if s != 2642
e inc -807 if e < -2575
cag inc 755 if swy <= 1669
e dec -532 if swy > 1662
alf dec -4 if eb <= -596
swy dec -552 if ev <= -1170
d dec -857 if cox != 1700
anb dec -31 if anb > 798
eb dec -482 if npo < -1759
ty inc 820 if d == 3897
xoc inc -846 if v >= -5054
alf dec 312 if d != 3895
obo dec -531 if npo >= -1752
b dec 722 if ev > -1177
d inc 912 if ev == -1173
e inc -497 if cag <= -1514
b dec 551 if eb <= -599
mqk dec -130 if alf >= -1576
obo dec 426 if s >= 2649
mqk inc 672 if cox >= 1698
red inc -879 if alf <= -1568
he dec -503 if cox != 1687
tm dec 117 if ev != -1173
he dec 237 if mk < 645
qby inc 189 if eb != -601
k inc 820 if k < -279
mqk inc -594 if obo <= -3421
k dec 375 if v > -5041
cag dec 298 if d != 4807
b inc 985 if ev != -1163
tm inc -697 if cox >= 1687
v dec -940 if swy >= 2212
k inc -260 if d >= 4806
ty dec 73 if k < -526
swy inc -530 if s <= 2654
mqk dec 636 if swy >= 1686
d inc 32 if qby <= 5516
v inc -809 if v <= -4103
e dec 380 if k != -540
ev inc -651 if b == -5939
qby inc 352 if alf >= -1576
v inc -256 if obo > -3416
mqk dec 28 if red <= -527
alf inc 395 if npo > -1762
ev dec 317 if d < 4837
cag inc 74 if red >= -528
mk dec -575 if anb == 833
cag dec 744 if obo == -3421
mk dec -455 if qby == 5868
k inc 350 if cag <= -2266
d inc 730 if npo <= -1749
ty dec 180 if mk != 1673
xoc inc 364 if cox == 1694
anb inc 937 if cox == 1694
s dec 459 if tm >= 2785
cox inc -594 if cag != -2271
cox dec 874 if cox > 1102
xoc dec 289 if alf > -1178
e inc -178 if obo < -3413
red dec -965 if obo >= -3416
npo dec -47 if ty == -497
alf inc -264 if npo < -1697
v inc -518 if alf == -1429
red dec -264 if swy == 1697
k inc 540 if obo > -3415
ev dec 208 if tm != 2799
npo dec -967 if tm >= 2788
xoc inc 106 if swy == 1691
eb dec -276 if s >= 2185
b inc -151 if mqk == -4812
he dec 120 if he <= -1534
e inc -236 if cox >= 1091
tm dec -25 if alf == -1436
xoc inc 618 if s == 2186
b dec 269 if qby != 5876
cag inc -911 if cag != -2267
mqk inc -68 if he == -1658
alf inc -267 if ev > -2035
cag dec -228 if npo < -733
k inc -527 if cag > -2043
qby inc 341 if red == -533
xoc dec 539 if mqk >= -4879
d dec 884 if ev == -2032
mk dec -837 if ty == -497
tm inc -879 if k == -707
k dec -32 if tm > 1907
e dec 771 if ev > -2026
qby inc -732 if s <= 2192
ev dec 629 if swy < 1700
obo dec -799 if e > -4138
e dec -954 if k >= -683
ev inc 177 if eb == -329
d inc -151 if cag < -2032
b dec 967 if alf > -1711
e dec 836 if b == -7326
he dec -543 if eb > -337
d inc 478 if alf == -1705
mqk dec 251 if xoc < 3047
v inc 235 if anb == 1770
xoc inc 94 if mqk > -5134
swy dec -106 if s > 2176
cox inc 591 if xoc != 3138
eb dec -594 if eb == -329
he inc -389 if qby == 5469
swy dec 680 if ty != -487
alf inc 713 if ev < -2483
mqk dec 533 if b != -7332
mk dec -282 if tm <= 1920
d dec -878 if tm >= 1913
ev inc -821 if b > -7327
s dec -229 if qby > 5475
tm dec -671 if swy > 1115
mk dec 703 if eb > 262
cag inc 96 if v == -4682
alf inc -771 if mqk != -5672
eb inc 140 if b > -7322
swy inc -493 if eb != 270
he inc 826 if eb == 273
v inc 82 if s <= 2408
anb inc -898 if tm <= 2585
mk dec 735 if red <= -531
obo inc -704 if eb >= 262
cag dec -145 if tm == 2585
k dec -952 if swy > 620
mqk inc 818 if eb == 265
he inc 229 if b > -7325
v dec -717 if npo != -746
npo inc -329 if d == 5890
xoc inc 820 if v > -3957
ev inc -751 if alf != -1768
he inc -105 if npo == -1066
tm inc -86 if swy <= 627
npo inc 83 if qby < 5486
npo inc 829 if mk != 1347
swy dec -554 if tm >= 2504
anb inc 836 if red <= -524
npo dec -878 if b > -7328
cag inc -276 if alf != -1763
red inc 215 if qby >= 5469
eb inc -509 if d >= 5884
xoc dec -707 if he >= -1119
s dec 42 if cag > -1801
s dec 63 if obo >= -4125
mk dec 358 if cag <= -1793
eb inc 95 if cag <= -1793
eb dec -637 if b >= -7323
ty inc -617 if xoc != 3854
mqk dec 510 if ty < -1112
he inc -285 if tm != 2499
ev inc -547 if qby < 5487
mk dec -915 if npo < 730
eb inc -953 if e > -4036
obo dec 412 if he >= -1108
npo inc -925 if d < 5888
ev inc 930 if cag < -1792
v inc 658 if npo <= 726
ev inc 892 if mk <= 1918
ev inc 756 if b == -7326
qby inc -574 if s > 2307
npo inc -638 if red >= -326
tm dec 248 if mk < 1913
cox inc 865 if s < 2317
cox dec -950 if d <= 5893
mk dec -632 if he <= -1108
v inc 176 if d <= 5893
mk inc -805 if ty != -1120
ev dec 973 if npo < 89
swy dec -601 if s < 2308
e dec -146 if cox < 3509
mqk inc 321 if cag > -1803
xoc inc -534 if alf >= -1764
d dec 747 if ev >= -3004
npo inc -826 if obo != -4120
xoc inc -343 if ev < -2994
b dec 415 if anb <= 1708
b dec 323 if xoc > 2976
b inc 697 if ty <= -1118
"""
}
