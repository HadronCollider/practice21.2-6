package com.makentoshe.androidgithubcitemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import com.makentoshe.androidgithubcitemplate.data.*
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*

//class                      Card(var Ques : String, var Ans : String, var GlobScore : Int, var Score : Double, Islearning : Boolean, Id : Int)

class HiddenTest(var Tsts: MutableList<TestCardItem>, var NumOfTestCards : Int, var NumofReadyCards : Int)
class Tests(var Tsts: MutableList<TestCardItem>, var NumOfTestCards : Int, var NumofReadyCards : Int)
{
    val ElInHTest = 8
    var NumOfHiddTests = NumOfTestCards / ElInHTest
    var Htsts : MutableList<HiddenTest> = mutableListOf<HiddenTest>()

    init {
        if (NumOfHiddTests < NumOfTestCards / ElInHTest.toDouble())
            NumOfHiddTests++

        Tsts.shuffle()
        for (i in 0..NumOfHiddTests) {/*
            var HTest = HiddenTest(
                Tsts.subList(i * ElInHTest, (i + 1) * ElInHTest) as MutableList<TestCardItem>,
                ElInHTest,
                0
            ) <<<<< ERROR
            Htsts.add(HTest)
            */
        }

    }

}
fun ChangeConf(IsAns : Boolean, NumOfCards : Int, NumOfRemCards : Int) : Float
{
    if (IsAns == false)
        return -(1 / NumOfCards.toFloat()) * NumOfRemCards
    else
        return (1 / NumOfCards.toFloat()) * NumOfRemCards
}

class TestActivity : AppCompatActivity() {
    private lateinit var collectionItemDao: CollectionItemDao
    private lateinit var collectionWithTestCardItemDao: CollectionWithTestCardItemDao
    private lateinit var testCardItemDao: TestCardItemDao
    private var TestCnt = 0
    private var CurrentTest = 0
    private lateinit var T : Tests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        title = "Test"

        var NumOfTestCards = 16
        var tsts : MutableList<TestCardItem> = arrayListOf()
        // Database
        collectionItemDao = TestsDatabase.getDatabase(this).collectionItemDao()
        collectionWithTestCardItemDao = TestsDatabase.getDatabase(this).collectionWithTestCardItemDao()
        testCardItemDao = TestsDatabase.getDatabase(this).testCardItemDao()


        collectionItemDao.getByIsSelected(true).forEach {
            collectionWithTestCardItemDao.getByCollectionId(it.collectionId).forEach {
                tsts.addAll(testCardItemDao.getTestCardItems())
            }
        }

        T = Tests(tsts, NumOfTestCards, 0)

        val buttonFalse = findViewById<Button>(R.id.button_false)
        val buttonTrue = findViewById<Button>(R.id.button_right)
        val card = findViewById<CardView>(R.id.card)
        val cardText = findViewById<TextView>(R.id.card_element)

        val cardAnimationRight1 = AnimationUtils.loadAnimation(this, R.anim.rotate_out_to_right)
        val cardAnimationRight2 = AnimationUtils.loadAnimation(this, R.anim.rotate_in_to_rigth)
        val cardAnimationLeft1 = AnimationUtils.loadAnimation(this, R.anim.rotate_out_to_left)
        val cardAnimationLeft2 = AnimationUtils.loadAnimation(this, R.anim.rotate_in_to_left)

        val setDelay : Handler = Handler()

        buttonFalse.setOnClickListener {
            buttonTrue.isClickable = false
            buttonFalse.isClickable = false

            card.startAnimation(cardAnimationLeft1)

            val startDelay2 : Runnable = Runnable {
                buttonTrue.isClickable = true
                buttonFalse.isClickable = true
                cardText.text = if (tsts.isNotEmpty()) tsts[TestCnt % tsts.size].firstText else "Tests are not exist"
            }

            val startDelay1 : Runnable  = Runnable {
                card.startAnimation(cardAnimationLeft2)
            }
            setDelay.postDelayed(startDelay1, 300)
            setDelay.postDelayed(startDelay2, 300 * 2)

            val isTestOver = testStep(false)
        }

        buttonTrue.setOnClickListener {
            buttonTrue.isClickable = false
            buttonFalse.isClickable = false

            card.startAnimation(cardAnimationRight1)

            val startDelay2 : Runnable = Runnable {
                buttonTrue.isClickable = true
                buttonFalse.isClickable = true
                cardText.text = if (tsts.isNotEmpty()) tsts[TestCnt % tsts.size].firstText else "Tests are not exist"
            }

            val startDelay1 : Runnable  = Runnable {
                card.startAnimation(cardAnimationRight2)
            }
            setDelay.postDelayed(startDelay1, 300)
            setDelay.postDelayed(startDelay2, 300 * 2)

            val isTestOver = testStep(true)
        }



    }

    private fun testStep(isRightAnswer : Boolean) : Boolean {
        /* Debug */
        TestCnt++
        return false
        /* End of debug */

        T.Htsts[CurrentTest].Tsts[TestCnt].score += ChangeConf(isRightAnswer, T.Htsts[CurrentTest].NumOfTestCards, T.Htsts[CurrentTest].NumOfTestCards - T.Htsts[CurrentTest].NumofReadyCards)
        T.Htsts[CurrentTest].Tsts[TestCnt].globalScore += if (isRightAnswer) 1 else 0
        T.Htsts[CurrentTest].Tsts[TestCnt] = updateDataInDataBase(T.Htsts[CurrentTest].Tsts[TestCnt])
        TestCnt++

        if (TestCnt < T.Htsts[CurrentTest].NumOfTestCards)
            return false

        TestCnt = 0

        // Merging hidden tests
        if (T.Htsts[CurrentTest].NumofReadyCards + 3 == T.Htsts[CurrentTest].NumOfTestCards) {

            for (i in 0..T.Htsts[CurrentTest].NumOfTestCards)
                if (T.Htsts[CurrentTest].Tsts[i].score > 0.9) {
                    T.Htsts[CurrentTest].Tsts.removeAt(i)
                    T.Htsts[CurrentTest].NumOfTestCards--
                }
            for (i in 0..T.Htsts[CurrentTest].NumOfTestCards)
                T.Htsts[CurrentTest + 1].Tsts.add(T.Htsts[CurrentTest].Tsts[i])
            CurrentTest++
            T.Htsts[CurrentTest].NumofReadyCards = 0
            T.Htsts[CurrentTest].NumOfTestCards += 3
        }


        // Breaking condition
        return T.NumofReadyCards == T.NumOfTestCards
    }

    private fun updateDataInDataBase(testCardItem : TestCardItem) : TestCardItem {
        testCardItemDao.updateTestCardItem(testCardItem)
        return testCardItemDao.getById(testCardItem.testCardId)
    }
}