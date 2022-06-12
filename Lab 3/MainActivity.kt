package com.example.myapplication

import android.app.AlertDialog
import android.content.DialogInterface
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var prawidlowa_liczba = 0
        var wynik = 0
        var proby = 0

        fun getRecord() {
            val sharedScore = this.getSharedPreferences("com.example.myapplication.shared", 0)
            wynik = sharedScore.getInt("score", 0)
        }

        fun setRecord() {
            val sharedScore = this.getSharedPreferences("com.example.myapplication.shared", 0)
            val edit = sharedScore.edit()
            edit.putInt("score", wynik)
            edit.apply()
        }

        getRecord()

        val zgaduj = findViewById<Button>(R.id.zgadnij)
        val podaj_liczbe = findViewById<EditText>(R.id.podana_liczba)
        val liczba_prob = findViewById<TextView>(R.id.próby)
        val liczba_punktow = findViewById<TextView>(R.id.punkty)
        val nowa_gra = findViewById<Button>(R.id.nowa_gra)
        val reset = findViewById<Button>(R.id.reset)

        liczba_punktow.setText(wynik.toString())
        liczba_prob.setText(proby.toString())

        var builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Wiadomość")
        builder.setMessage(("Podano nieprawidłową wartość"))

        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int -> }

        val dialog: AlertDialog = builder.create()

        val mniejsza =
            Toast.makeText(applicationContext, "Twoja liczba jest mniejsza!", Toast.LENGTH_LONG)
        val wieksza =
            Toast.makeText(applicationContext, "Twoja liczba jest większa!", Toast.LENGTH_LONG)
        val wylosowano_liczbe =
            Toast.makeText(applicationContext, "Wylosowano liczbę!", Toast.LENGTH_LONG)

        nowa_gra.setOnClickListener() {
            prawidlowa_liczba = Random.nextInt(0, 20)
            proby = 0
            liczba_prob.setText(proby.toString())
            wylosowano_liczbe.show()
        }

        reset.setOnClickListener() {
            wynik = 0
            setRecord()
            liczba_punktow.setText(wynik.toString())
        }

        zgaduj.setOnClickListener() {
            val podana_liczba = podaj_liczbe.text
            if (podana_liczba.isEmpty() or podana_liczba.contains("[^0-9]")) {
                builder.setMessage(("Podano nieprawidłową wartość"))
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else if ((Integer.parseInt(podana_liczba.toString()) < 0) or (Integer.parseInt(podana_liczba.toString()) > 20)) {
                builder.setMessage("Podano liczbę spoza zakresu")
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {
                proby += 1
                if (proby > 10) {
                    liczba_punktow.setText(wynik.toString())
                    builder.setMessage("Nie udało ci się odgadnąć liczby w ciągu 10 prób!")
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                    prawidlowa_liczba = Random.nextInt(0, 20)
                    proby = 0
                    liczba_prob.setText(proby.toString())
                    wylosowano_liczbe.show()
                } else {
                    if (Integer.parseInt(podana_liczba.toString()) > prawidlowa_liczba) {
                        wieksza.show()
                        liczba_prob.setText(proby.toString())
                    }

                    if (Integer.parseInt(podana_liczba.toString()) < prawidlowa_liczba) {
                        mniejsza.show()
                        liczba_prob.setText(proby.toString())
                    } else if (Integer.parseInt(podana_liczba.toString()) == prawidlowa_liczba) {
                        builder.setMessage("Udało ci się odgadnąć liczbę przy próbie nr " + proby)
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                        if (proby == 1) {
                            wynik += 5
                            setRecord()
                        } else if ((proby > 1) and (proby < 5)) {
                            wynik += 3
                            setRecord()
                        } else if ((proby >= 5) and (proby < 7)) {
                            wynik += 2
                            setRecord()
                        } else if ((proby >= 7) and (proby < 11)) {
                            wynik += 1
                            setRecord()
                        } else {
                            builder.setMessage("Nie udało ci się odgadnąć liczby w ciągu 10 prób!")
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                        prawidlowa_liczba = Random.nextInt(0, 20)
                        proby = 0
                        liczba_prob.setText(proby.toString())
                        wylosowano_liczbe.show()
                        liczba_punktow.setText(wynik.toString())
                    }
                }
            }
        }
    }
}
