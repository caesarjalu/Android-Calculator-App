package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    TextView calculationTV;
    TextView resultTV;

    String calculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTextViews();
    }

    private void initTextViews() {
        calculationTV = (TextView) findViewById(R.id.calculation);
        resultTV = (TextView) findViewById(R.id.result);
        clear();
    }

    private void clear() {
        calculationTV.setText("");
        calculation = "";
        resultTV.setText("0");
    }

    private void setCalculation(String givenValue) {
        calculation += givenValue;
        resultTV.setText(calculation);
    }

    private void backspace() {
        try {
            StringBuffer sb = new StringBuffer(calculation);
            sb.deleteCharAt(sb.length()-1);
            if (sb.length() == 0)
                throw new Exception();
            calculation = String.valueOf(sb);
            resultTV.setText(calculation);
        } catch (Exception e) {
            calculation = "";
            resultTV.setText("0");
        }
    }

    private boolean isOperator(String substr) {
        if (substr.equals("+") || substr.equals("-")
                || substr.equals("×") || substr.equals("/") || substr.equals("%")) {
            return true;
        } else {
            return false;
        }
    }

    public void cOnClick(View view) {
        clear();
    }

    public void percentOnClick(View view) {
        if (!calculation.equals("")) {
            if (isOperator(calculation.substring(calculation.length() - 1))) {
                backspace();
            }
            setCalculation("%");
        }
    }

    public void backspaceOnClick(View view) {
        backspace();
    }

    public void divideOnClick(View view) {
        if (!calculation.equals("")) {
            if (isOperator(calculation.substring(calculation.length() - 1))) {
                backspace();
            }
            setCalculation("/");
        }
    }

    public void timesOnClick(View view) {
        if (!calculation.equals("")) {
            if (isOperator(calculation.substring(calculation.length() - 1))) {
                backspace();
            }
            setCalculation("×");
        }
    }

    public void minusOnClick(View view) {
        if (!calculation.equals("")) {
            if (isOperator(calculation.substring(calculation.length() - 1))) {
                backspace();
            }
        }
        setCalculation("-");
    }

    public void plusOnClick(View view) {
        if (!calculation.equals("")) {
            if (isOperator(calculation.substring(calculation.length() - 1))) {
                backspace();
            }
            setCalculation("+");
        }
    }

    public void dotOnClick(View view) {
        if (!(calculation.equals("") || isOperator(calculation.substring(calculation.length() - 1))))
            setCalculation(".");
    }

    public void zeroOnClick(View view) {
        if (!(calculation.equals("")))
            setCalculation("0");
    }

    public void tripleZeroOnClick(View view) {
        if (!(calculation.equals("") || isOperator(calculation.substring(calculation.length() - 1))))
            setCalculation("000");
    }

    public void oneOnClick(View view) {
        setCalculation("1");
    }

    public void twoOnClick(View view) {
        setCalculation("2");
    }

    public void threeOnClick(View view) {
        setCalculation("3");
    }

    public void fourOnClick(View view) {
        setCalculation("4");
    }

    public void fiveOnClick(View view) {
        setCalculation("5");
    }

    public void sixOnClick(View view) {
        setCalculation("6");
    }

    public void sevenOnClick(View view) {
        setCalculation("7");
    }

    public void eightOnClick(View view) {
        setCalculation("8");
    }

    public void nineOnClick(View view) {
        setCalculation("9");
    }

    public void equalOnClick(View view) {
        // gunakan try catch untuk meminimalisir error dan mengubah nilai calculate menjadi 0 saat terjadi error
        try {
            //jika pada akhir string terdapat operator, maka hapus operator di akhir tersebut
            if (isOperator(calculation.substring(calculation.length() - 1))) {
                backspace();
            }
            //menggunakan split dan regex untuk memisahkan angka dengan operator
            String[] splitCalculate = calculation.split("(?<=[\\d.])(?=[^\\d.])|(?<=[^\\d.])(?=[\\d.])");
            LinkedList<Double> operands = new LinkedList<>();
            LinkedList<String> operators = new LinkedList<>();
            double operand;
            String operator;

            //jika angka awal negatif (terdapat operator - di awal string), kosongkan array index[0] dan ubah angka pertama menjadi angka negatif
            if (splitCalculate[0].equals("-")) {
                splitCalculate[1] = "-" + splitCalculate[1];
                splitCalculate[0] = "";
            }
            //dari array splitCalculate, sortir operator dan operand ke dalam linkedlist masing2
            for (String split : splitCalculate) {
                if (split.equals("")) {
                    continue;
                } else if (isOperator(split)) {
                    operators.add(split);
                } else {
                    operands.add(Double.parseDouble(split));
                }
            }

            //loop untuk mengutamakan perkalian, pembagian, dan modulus
            for (int i = 0; i < operators.size(); i++) {
                operator = operators.get(i);
                while (operator.equals("×") || operator.equals("/") || operator.equals("%")) {
                    switch (operator) {
                        case "×":
                            operands.set(i, operands.get(i) * operands.get(i + 1));
                            operands.remove(i + 1);
                            operators.remove(i);
                            break;
                        case "/":
                            operands.set(i, operands.get(i) / operands.get(i + 1));
                            operands.remove(i + 1);
                            operators.remove(i);
                            break;
                        case "%":
                            operands.set(i, operands.get(i) % operands.get(i + 1));
                            operands.remove(i + 1);
                            operators.remove(i);
                            break;
                    }
                    if (!(operators.isEmpty() || i >= operators.size())) {
                        operator = operators.get(i);
                    } else {
                        break;
                    }
                }
            }

            double result = operands.poll();

            //loop untuk operator sisa (plus dan minus)
            while (!operators.isEmpty()) {
                operator = operators.poll();
                operand = operands.poll();
                switch (operator) {
                    case "+":
                        result += operand;
                        break;
                    case "-":
                        result -= operand;
                        break;
                }
            }

            //taruh result ke dalam textview
            calculationTV.setText(calculation);
            if (result == 0) {
                throw new Exception();
            } else if (result % 1 == 0) {
                calculation = Integer.toString((int) result);
            } else {
                calculation = Double.toString(result);
            }
            resultTV.setText(calculation);
        } catch (Exception e) { //jika ada error / exception, ubah hasil menjadi 0
            System.out.println(e);
            calculationTV.setText(calculation);
            calculation = "";
            resultTV.setText("0");
        }
    }
}