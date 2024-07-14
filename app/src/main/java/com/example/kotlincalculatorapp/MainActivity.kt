package com.example.kotlincalculatorapp

import android.os.Bundle
import android.text
import androidx.lifecycle.viewmodel.CreationExtras
import android
import android.app
import android.accessibilityservice
import android.widget.TextView
import android.view.view
import androidx.compose.runtime.composable
composable

class MainActivity : AppCompatActivity()
{
    private var canaddOperation= false
    private var canaddDecimal= true


    override fun oCreate(savedInstancesState: Bundle?) {
        super.onCreate(savedInstancesState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: view)
    {
        if(view is button)
        {
            if(view.text=".")
            {
                if(canaddDecimal)
                    workingsTV.append(view.text)

                canaddDecimal = false
            }
            else
            workingsTV.append(view.text)

            canaddOperation=true
        }
    }
    fun operationAction(view: view)
    {
        if(view is button && canaddOperation)
        {
            workingsTV.append(view.text)
            canaddOperation=false
            canaddDecimal = true
        }
    }
    fun allClearAction(view: view)
    {
        workingsTV.text=""
        resultsTV.text=""
    }
    fun backSpaceAction(view: view)
    {
        val lenth =workingsTV.lenth()
        if(lenth > 0)
            workingsTV.text = workingsTV.text.subsequence(0.lenth - 1)
    }
    fun equalAction(view: view)
    {
          resultsTV.text = calculateResults()
    }
    private fun calculateResults(): String
    {
       val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty())
            return""
     val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }
    private fun addSubtractCalculate(passedList: MutableList<Any>):Float
    {
        val  result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if(operator == '+')
                    result += nextDigit
                if(operator == '-')
                    result -= nextDigit
            }
        }

         return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list =passedList
        while(list.contains('x') || list.contains('/'))
        {
          list =  calcTimesDiv(list)
        }

        return list
    }
    private  fun calcTimesDiv(passedList: MutableList<Any>):MutableList<Any>
    {
        val newList = MutableList<Any>
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
             if(passedList[i] is char && i != lastIndex && i < restartIndex)
             {
                 val operator = passedList[i]
                 val prevDigit = passedList[i - 1] as Float
                 val nextDigit = passedList[i + 1] as Float
                 when(operator)
                 {
                     'x'->
                     {
                         newList = prevDigit * nextDigit
                         restartIndex = i + 1
                     }
                      '/'->
                  {
                      newList = prevDigit / nextDigit
                      restartIndex = i + 1
                    }
                     else ->
                     {
                         newList.add(prevDigit)
                         newList.add(operator)
                     }
                 }
             }
            if(i > restartIndex)
                newList = add(passedList[i])
        }
        return newList
    }

    private fun digitsOperators():MutableList<Any>
    {
        val list = mutableListOf<any>
          var currentdigit = ""
        for(character in workingsTV.text)
        {
           if(character.isdigit() || character == '.')
               currentDigit += character
            else
           {
               list.add(currentdigit.toFloat())
               currentdigit=""
               list.add(character)
           }
        }
        if(currentdigit != "")
            list.add(currentdigit.toFloat())
        return list
    }
}